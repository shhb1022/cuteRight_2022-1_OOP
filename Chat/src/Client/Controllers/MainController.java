package Client.Controllers;

import Client.SocketConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ComboBox chooseRoomList;
    ObservableList<String> list = FXCollections.observableArrayList("내 채팅방", "전체채팅방");

    @FXML private ListView roomDisplay;

    @FXML private Button logoutBtn, createRoomBtn;

    public void initialize(URL location, ResourceBundle resources) {

    	createRoomBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    URL url = new URL("http://localhost:3000/main");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");

                    Stage currStage = (Stage) createRoomBtn.getScene().getWindow();
                    currStage.close();

                    Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/CreateRoom.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    // 현재 창을 종료한다.
                    Stage currStage = (Stage) logoutBtn.getScene().getWindow();
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
    }
}
