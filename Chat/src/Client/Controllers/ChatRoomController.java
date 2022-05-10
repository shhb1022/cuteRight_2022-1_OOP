package Client.Controllers;

import Client.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {
    Socket socket = null;
    @FXML private TextArea txtDisplay;
    @FXML private TextField txtInput;
    @FXML private Button backBtn, sendBtn, connBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addTextLimiter(txtInput, SocketConnection.MAX_CHAT_LENGTH);
        txtInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if(k.getCode().equals(KeyCode.ENTER)) {
                    send(txtInput.getText());
                    txtInput.clear();
                }
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    // 현재 창을 종료한다.
                    Stage currStage = (Stage) backBtn.getScene().getWindow();
                    currStage.close();
                    // 새 창을 띄운다.
                    Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Login.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                send(txtInput.getText());
                txtInput.clear();
            }
        });

        connBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SocketConnection.close();
            }
        });

        startChatting();
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

    void startChatting() {
        socket = SocketConnection.socket;
        Thread thread = new Thread() {
            @Override
            public void run() {
                if(socket != null || !socket.isClosed()) {
                    receive();
                } else {
                    Platform.runLater(()->displayText("[서버 통신 안됨]"));
                }
            }
        };
        thread.start();
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
                SocketConnection.close();
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
                    SocketConnection.close();
                }
            }
        };
        thread.start();
    }

    void displayText(String text) {
        txtDisplay.appendText(text + "\n"); }
}
