import javafx.application.Platform;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client extends Application{
    Socket socket;
    public static final int MAX_BYTE_SIZE = 400;
    public static final int MAX_CHAT_LENGTH = MAX_BYTE_SIZE / 4;
    
    void startClient() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("localhost", 5001));
                    Platform.runLater(()->{
                        displayText("[연결 완료 " + socket.getRemoteSocketAddress()+"]");
                        btnConn.setText("stop");
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
                btnConn.setText("start");
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
                byte[] byteArr = new byte[MAX_BYTE_SIZE];
                InputStream inputStream = socket.getInputStream();

                //서버가 비정상적으로 종료했을 경우 IOEXCEPTIOn 발생
                int readByteCount = inputStream.read(byteArr);

                // 서버가 정상적으로 Socket의 close를 호출 했을 경우
                if(readByteCount == -1) { throw new IOException(); }

                String data = new String(byteArr, 0, readByteCount, "UTF-8");
                
                //
                //추가한 부분
                String splitdata[] = data.split("/");
                InetAddress ip =  InetAddress.getLocalHost();
                if(splitdata[1].equals(ip.getHostAddress())) {
                	Platform.runLater(()->displayText("[나]" + data));
                }
                else Platform.runLater(()->displayText("[상대방]" + data ));
                //
                //
                
            } catch (Exception e) {
                Platform.runLater(()->displayText("[서버 통신 안됨]"));
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
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(byteArr);
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

    TextArea txtDisplay;
    TextField txtInput;
    Button btnConn, btnSend;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 300);

        txtDisplay = new TextArea();
        txtDisplay.setEditable(false);
        BorderPane.setMargin(txtDisplay, new Insets(0,0,2,0));
        root.setCenter(txtDisplay);

        BorderPane bottom = new BorderPane();
        txtInput = new TextField();
        txtInput.setPrefSize(60, 30);
        BorderPane.setMargin(txtInput, new Insets(0,1,1,1));
        addTextLimiter(txtInput, MAX_CHAT_LENGTH);

        btnConn = new Button("start");
        btnConn.setPrefSize(60, 30);
        btnConn.setOnAction(e->{
            if (btnConn.getText().equals("start")) {
                startClient();
            } else if(btnConn.getText().equals("stop")) {
                stopClient();
            }
        });

        btnSend = new Button("send");
        btnSend.setPrefSize(60, 30);
        btnSend.setDisable(true);
        btnSend.setOnAction(e->send(txtInput.getText()));

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
