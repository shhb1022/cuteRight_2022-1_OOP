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
import org.w3c.dom.Text;

import java.io.*;
import java.net.*;
import java.util.Arrays;

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
                InputStream inputStream = socket.getInputStream();
                byte[] headerBuffer = new byte[8];

                int readByteCount = inputStream.read(headerBuffer);
                //클라이언트가 비정상 종료를 했을 경우 IOException 발생
                if(readByteCount == -1) { throw new IOException(); }

                // 정상적으로 수신된 메세지가 아닐 경우
                if(headerBuffer[0] != 0x02) {
                    continue;
                }

                // ip 받기
                String receiveIp = InetAddress.getByAddress(Arrays.copyOfRange(headerBuffer, 2,6)).getHostAddress();

                // data 길이 체크
                byte[] lengthChk = new byte[2];
                lengthChk[0] = headerBuffer[6];
                lengthChk[1] = headerBuffer[7];
                int dataLength = MessagePacker.byteArrayToInt(lengthChk,2);

                // Message 내용을 담을 버퍼
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] receiveData = new byte[dataLength];
                int read;

                // 버퍼 안의 데이터를 다 읽을 때까지 반복문을 돌린다.
                while((read = inputStream.read(receiveData,0,receiveData.length))!=-1) {
                    buffer.write(receiveData,0,read);
                    dataLength = dataLength - read;
                    if (dataLength<=0) {
                        break;
                    }
                }

                String data = new String(buffer.toByteArray(), "utf-8");
                buffer.flush();
                buffer.close();

                InetAddress ip =  InetAddress.getLocalHost();
                if(receiveIp.equals(ip.getHostAddress())) {
                	Platform.runLater(()->displayTextM("[나]" + data));
                } else {
                    Platform.runLater(()->displayText("[상대방]" + data ));
                }
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
                    MessagePacker packet = new MessagePacker(byteArr);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(packet.getMessage());
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
        root.setCenter(txtDisplay);

        txtDisplayM = new TextArea();
        txtDisplayM.setEditable(false);
        txtDisplayM.setStyle("-fx-text-fill: gray;");
        BorderPane.setMargin(txtDisplayM, new Insets(0,0,2,0));
        root.setCenter(txtDisplayM);

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

    void displayTextM(String text) {
        int maxLine = 25;
        for(int i = 0; i < text.length(); i+= maxLine) {
            if (i + 25 > text.length()) {
                txtDisplayM.appendText(text.substring(i, text.length()));
                break;
            }
            txtDisplayM.appendText(text.substring(i, i+maxLine)+"\n");
        }
        txtDisplayM.appendText("\n");
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
