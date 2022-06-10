package Client.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Client.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import Client.Models.*;

public class UserListController implements Initializable {
//    Socket socket = null;
    @FXML private Button backBtn2;
    @FXML private ListView entranceDisplay,watingDisplay;
    ObservableList<GridPane> ListEntrance = FXCollections.observableArrayList();
    ObservableList<GridPane> ListWating = FXCollections.observableArrayList();
    
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
        roomUsersList();
    }

    //유저 정보를 요청하고 리스트에 출력한다.
    void roomUsersList() {
    	try {
    		URL url = new URL("http://localhost:3000/userList?room_id="+Status.getCurrentRoom().getRoom_id());
    		HttpURLConnection http = (HttpURLConnection) url.openConnection();
    		http.setRequestMethod("GET");

    		if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
    			InputStream is = http.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				
    			JSONParser parser = new JSONParser();
    			JSONArray list = (JSONArray)parser.parse(br);
    			
    			ListEntrance.clear();
    			ListWating.clear();
    			for(int i=0; i<list.size(); i++) {
    				JSONObject obj = (JSONObject) list.get(i);
    				
    				ChatRoomMemberDTO member =new ChatRoomMemberDTO(obj.get("std_id").toString(),null,obj.get("member").toString(),
    						obj.get("name").toString(),obj.get("d_job").toString(),obj.get("state").toString());
    				
    				if(member.getMember().equals("1")) {
    					ListEntrance.add(EntranceInfoBox(member));
    				}else if(member.getMember().equals("0")) {
    					ListWating.add(WatingInfoBox(member));
    				}
    			}
    			entranceDisplay.setItems(ListEntrance);
    			watingDisplay.setItems(ListWating);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public GridPane EntranceInfoBox(ChatRoomMemberDTO member) {
        GridPane userInfoBox = new GridPane();
        Label userState = new Label();
        userState.setPrefWidth(20);
        Label userStd_id = new Label();
        userStd_id.setPrefWidth(60);
        Label userName = new Label();
        userName.setPrefWidth(60);
        Label userJob = new Label();
        userJob.setPrefWidth(60);
        Button banBtn = new Button();
        
        userState.setText(member.getState());
        userStd_id.setText(member.getStd_id());
        userName.setText(member.getName());
        userJob.setText(member.getD_job());
        banBtn.setText("퇴장");
        
        userInfoBox.add(userState, 0, 0);
        userInfoBox.add(userStd_id, 1, 0);
        userInfoBox.add(userName, 2, 0);
        userInfoBox.add(userJob, 3, 0);
        userInfoBox.add(banBtn, 4, 0);
        
        banBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			System.out.println(member.getName()+" 강퇴");
    			try {
					URL url = new URL("http://localhost:3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "BAN");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(Status.getId()+"가 방에서 퇴장 당하였습니다");
						alert.showAndWait();
						//재갱신
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("오류가 발생하였습니다.");
						alert.showAndWait();
						//재갱신
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			roomUsersList();
    		}
    	});
        
        return userInfoBox;
    }
    public GridPane WatingInfoBox(ChatRoomMemberDTO member) {
        GridPane userInfoBox = new GridPane();
        Label userState = new Label();
        userState.setPrefWidth(20);
        Label userStd_id = new Label();
        userStd_id.setPrefWidth(60);
        Label userName = new Label();
        userName.setPrefWidth(60);
        Label userJob = new Label();
        userJob.setPrefWidth(60);
        Button acceptBtn = new Button();
        Button refuseBtn = new Button();
        
        userState.setText(member.getState());
        userStd_id.setText(member.getStd_id());
        userName.setText(member.getName());
        userJob.setText(member.getD_job());
        acceptBtn.setText("수락");
        refuseBtn.setText("거절");
        
        userInfoBox.add(userState, 0, 0);
        userInfoBox.add(userStd_id, 1, 0);
        userInfoBox.add(userName, 2, 0);
        userInfoBox.add(userJob, 3, 0);
        userInfoBox.add(acceptBtn, 4, 0);
        userInfoBox.add(refuseBtn, 5, 0);
        
        acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			System.out.println(member.getName()+" 수락");
    			try {
					URL url = new URL("http://localhost:3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "ACCEPT");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(Status.getId()+"를(을) 수락하였습니다.");
						alert.showAndWait();
						//재갱신
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("오류가 발생하였습니다.");
						alert.showAndWait();
						//재갱신
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			roomUsersList();
    		}
    	});
        
        refuseBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			System.out.println(member.getName()+" 거절");
    			try {
					URL url = new URL("http://localhost:3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "REFUSE");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(Status.getId()+"를(을) 거절하였습니다.");
						alert.showAndWait();
						//재갱신
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("오류가 발생하였습니다.");
						alert.showAndWait();
						//재갱신
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    	});
        return userInfoBox;
    }
    
    
}
