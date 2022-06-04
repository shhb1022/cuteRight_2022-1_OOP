package Client.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import Client.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class FriendController implements Initializable {
//    Socket socket = null;
    @FXML private Button backBtn2;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backBtn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override

            public void handle(ActionEvent event) {
                try {
                    Stage stage = (Stage) backBtn2.getScene().getWindow();
                    stage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
