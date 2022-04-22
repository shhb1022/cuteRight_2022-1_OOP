import javafx.application.Platform;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Client extends Application{
    Socket socket;
    public static final int MAX_BYTE_SIZE = 1024;
    public static final int MAX_CHAT_LENGTH = MAX_BYTE_SIZE / 4;
    
    void startClient() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 5001));
                    Platform.runLater(()->{
                        displayText("[연결 완료 " + socket.getRemoteSocketAddress()+"]");
                        btnConn.setText("종료");
                        btnSend.setDisable(false);
                    });
                } catch (Exception e) {
                    Platform.runLater(()->displayText("[서버 통신 안됨]"));
                    if(!socket.isClosed()) { stopClient(); }
                    return;
                }
                receive();
            }
        };
        thread.start();
    }

    void stopClient() {
        try {
            Platform.runLater(() -> {
                displayText("[연결 끊음]");
                btnConn.setText("시작");
                btnSend.setDisable(true);
            });
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) { }
    }

    void receive() {
        while (true) {
            try {
                InputStream inputStream = socket.getInputStream();
                MessagePacker packet = MessagePacker.unpack(inputStream);

                String msg = new String(packet.getMessage(), "utf-8");
                String ip =  InetAddress.getLocalHost().getHostAddress();
                String receiveIp = packet.getIp().getHostAddress();
                if(receiveIp.equals(ip)) {
                	Platform.runLater(()->displayText("[나] " + msg));
                } else {
                    Platform.runLater(()->displayText("[상대방] " + msg));
                }
            } catch (Exception e) {
                Platform.runLater(()->displayText("[서버 통신 안됨]"));
                e.printStackTrace();
                stopClient();
                break;
            }
        }
    }

    void send(String data) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    byte[] byteArr = data.getBytes("UTF-8");
                    MessagePacker packet = new MessagePacker(byteArr);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(packet.getPacket());
                    outputStream.flush();
                } catch (Exception e) {
                    Platform.runLater(()->displayText("[서버 통신 안됨]"));
                    stopClient();
                }
            }
        };
        thread.start();
    }

    //////////
    // UI 생성 코드

    TextArea txtDisplay, txtDisplayM;
    TextField txtInput;
    Button btnConn, btnSend;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0,0,2,0));
        txtDisplay.setWrapText(true);
        root.setCenter(txtDisplay);

        BorderPane bottom = new BorderPane();
        txtInput = new TextField();
        txtInput.setPrefSize(60, 30);
        BorderPane.setMargin(txtInput, new Insets(0,1,1,1));
        addTextLimiter(txtInput, MAX_CHAT_LENGTH);
        txtInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if(k.getCode().equals(KeyCode.ENTER)) {
                    send(txtInput.getText());
                    txtInput.clear();
                }
            }
        });

        btnConn = new Button("시작");
        btnConn.setPrefSize(60, 30);
        btnConn.setOnAction(e->{
            if (btnConn.getText().equals("시작")) {
                startClient();
            } else if(btnConn.getText().equals("종료")) {
                stopClient();
            }
        });

        btnSend = new Button("보내기");
        btnSend.setPrefSize(60, 30);
        btnSend.setDisable(true);
        btnSend.setOnAction(e-> {
            send(txtInput.getText());
            txtInput.clear();
        });

        bottom.setCenter(txtInput);
        bottom.setLeft(btnConn);
        bottom.setRight(btnSend);

        root.setBottom(bottom);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.setOnCloseRequest(event->stopClient());
        primaryStage.show();
    }

    void displayText(String text) {
        txtDisplay.appendText(text + "\n");
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
