package Client.Controllers;

import Client.SocketConnection;
import Client.UserInfo;
import Server.Models.UsersDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ComboBox chooseRoomList;
    @FXML private ListView roomDisplay;
    @FXML private Button logoutBtn, createRoomBtn;

    ObservableList<GridPane> roomList = FXCollections.observableArrayList();
    

	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {


        ObservableList<String> requestRoomList = FXCollections.observableArrayList("내 채팅방", "전체채팅방");
        chooseRoomList.setItems(requestRoomList);
        chooseRoomList.getSelectionModel().selectFirst();

        String selectedRoom = (String) chooseRoomList.getValue();
        try {
            if (selectedRoom.equals("내 채팅방")) {
                URL url = new URL("http://localhost:3000/?std_id=" + UserInfo.getId());
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = http.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    JSONParser parser = new JSONParser();
                    JSONArray list = (JSONArray) parser.parse(br);
                    roomList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject obj = (JSONObject) list.get(i);
                        roomList.add(MyRoomBox(obj.get("title").toString(),obj.get("room_id").toString()));
                    }
                    roomDisplay.setItems(roomList);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }







        chooseRoomList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedRoom = (String) chooseRoomList.getValue();
                try {
                	if(selectedRoom.equals("내 채팅방")) {
                		URL url = new URL("http://localhost:3000/?std_id="+UserInfo.getId());
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        
                        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                			InputStream is = http.getInputStream();
            				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                			JSONParser parser = new JSONParser();
                			JSONArray list = (JSONArray)parser.parse(br);
                			roomList.clear();
                			for(int i=0; i<list.size(); i++) {
                				JSONObject obj = (JSONObject) list.get(i);
                				roomList.add(MyRoomBox(obj.get("title").toString(),obj.get("room_id").toString()));
                			}
                			roomDisplay.setItems(roomList);
                		}
                	}
                	else if(selectedRoom.equals("전체채팅방")) {
                		URL url = new URL("http://localhost:3000/?std_id=-"+UserInfo.getId());
                		HttpURLConnection http = (HttpURLConnection) url.openConnection();
                		http.setRequestMethod("GET");
                            
                		if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                			InputStream is = http.getInputStream();
                			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                			JSONParser parser = new JSONParser();
                			JSONArray list = (JSONArray)parser.parse(br);
                			roomList.clear();
                			for(int i=0; i<list.size(); i++) {
                				JSONObject obj = (JSONObject) list.get(i);
                				roomList.add(OpenRoomBox(obj.get("title").toString(),obj.get("room_id").toString()));
                				}
                			roomDisplay.setItems(roomList);
                        }
                	}
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



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
//                String id = UserInfo.getId();
//                String pw = UserInfo.getPw(); //굳이??
                Stage stage = new Stage();
                try {
                    URL url = new URL("http://localhost:3000/logout");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    http.setRequestProperty("Authorization", UserInfo.getId());

                    // 응답 코드 테스트중
                    System.out.println("getResponseCode():" + http.getResponseCode());

                    if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 현재 창을 종료한다.
                        Stage currStage = (Stage) logoutBtn.getScene().getWindow();
                        currStage.close();
                        // 새 창을 띄운다.
                        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Login.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
        

    public GridPane MyRoomBox(String title, String room_id) {
        GridPane RoomInfoBox = new GridPane();
        Label roomTitle = new Label();
        Label roomId = new Label();
        roomId.setPrefWidth(50);
        Button in = new Button();
        in.setText("입장");

        
        roomTitle.setText(title);
        roomId.setText(room_id);
        
        RoomInfoBox.add(roomTitle, 1, 0);
        RoomInfoBox.add(roomId,0,0);
        RoomInfoBox.add(in, 2,1);

        //버튼을 누르면 입장 가능한지 확인을 요청하고 http_ok가 들어오면 입장을 한다.
        in.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                	URL url = new URL("http://localhost:3000/chatRoom?room_id="+room_id+"&std_id="+UserInfo.getId());
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    
                    if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            			UserInfo.setRoom_id(room_id);
            			
            			// 현재 창을 종료한다.
                        Stage currStage = (Stage) in.getScene().getWindow();
                        currStage.close();

                        // 새 창을 띄운다.
                        Stage stage = new Stage();
                        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/ChatRoom.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
            		}
                    else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    	Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setHeaderText(null);
                        alert.setContentText("입장할 수 없는 방 입니다.(갱신이 필요합니다.)");
                        alert.showAndWait();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });



        return RoomInfoBox;
    }
    
    public GridPane OpenRoomBox(String title, String room_id) {
        GridPane RoomInfoBox = new GridPane();
        Label roomTitle = new Label();
        Label roomId = new Label();
        roomId.setPrefWidth(50);
        Button in = new Button();
        in.setText("입장 신청");

        
        roomTitle.setText(title);
        roomId.setText(room_id);
        
        RoomInfoBox.add(roomTitle, 1, 0);
        RoomInfoBox.add(roomId,0,0);
        RoomInfoBox.add(in, 2,1);

        //ChatRoomController랑 조정해서 구현
        in.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    System.out.println("입장 신청 완료");
                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });



        return RoomInfoBox;
    }
        

//    public static String getResponseBody(InputStream is) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//        String line;
//        while((line = br.readLine()) != null) {
//            sb.append(line).append("\n");
//        }
//        br.close();
//        return sb.toString();
//    }
}
