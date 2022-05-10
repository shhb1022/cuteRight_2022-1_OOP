package Client.Controllers;

import Client.SocketConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    Socket socket = null;
    @FXML private TextField idInput;
    @FXML private TextField pwInput;
    @FXML private Button loginBtn;
    @FXML private Button signUpBtn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            SocketConnection.connect();
        } catch (Exception e) {
            System.out.println("인터넷 연결이 불안정 합니다!!!!!");
            e.printStackTrace();
        }

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("아이디: " + idInput.getText());
                System.out.println("패스워드: " + pwInput.getText());
                Stage stage = new Stage();
                try {
                    // 현재 창을 종료한다.
                    Stage currStage = (Stage) idInput.getScene().getWindow();
                    currStage.close();
                    // 새 창을 띄운다.
                    Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/ChatRoom.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
