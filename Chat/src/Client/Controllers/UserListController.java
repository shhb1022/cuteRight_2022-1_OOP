package Client.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Client.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

import Client.Models.*;

public class UserListController implements Initializable {
    @FXML private ImageView backBtn2;
    @FXML private ListView entranceDisplay,waitingDisplay;

    ObservableList<GridPane> ListEntrance = FXCollections.observableArrayList();
    ObservableList<GridPane> ListWaiting = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        backBtn2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
    		URL url = new URL("http://"+SocketConnection.SERVER_IP+":3000/userList?room_id="+Status.getCurrentRoom().getRoom_id());
    		HttpURLConnection http = (HttpURLConnection) url.openConnection();
    		http.setRequestMethod("GET");

    		if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
    			
    			InputStream is = http.getInputStream();
    			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
    			JSONParser parser = new JSONParser();
    			JSONArray list = (JSONArray)parser.parse(br);
    			
    			//리스트 초기화
    			ListEntrance.clear();
    			ListWaiting.clear();
    			
    			for(int i=0; i<list.size(); i++) {
    				JSONObject obj = (JSONObject) list.get(i);
    				
    				String std_id = obj.get("std_id").toString();
    				String room_id = obj.get("room_id").toString();
    				String member_S = obj.get("member").toString();
    				String name = obj.get("name").toString();
    				String d_job = obj.get("d_job").toString();
    				String state = obj.get("state").toString();

    				
    				ChatRoomMemberDTO member =new ChatRoomMemberDTO(std_id,room_id,member_S,name,d_job,state);
    				
    				//1이면 접속인원 0이면 대기인원 나머지는 무시
    				if(member.getMember().equals("1")) {
    					ListEntrance.add(EntranceInfoBox(member));
    				}else if(member.getMember().equals("0")) {
    					ListWaiting.add(WaitingInfoBox(member));
    				}
    			}
    			entranceDisplay.setItems(ListEntrance);
    			waitingDisplay.setItems(ListWaiting);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

//	public AnchorPane EntranceInfoBox(ChatRoomMemberDTO member) {
////		Label userState = new Label();
////		Label userStd_id = new Label();
////		Label userName = new Label();
////		Label userJob = new Label();
////		Button banBtn = new Button();
////		userState.setText(member.getState());
////		userStd_id.setText(member.getStd_id());
////		userName.setText(member.getName());
////		userJob.setText(member.getD_job());
////		banBtn.setText("퇴장");
////
////		userName.setFont(new Font("System", 20));
//		userStd_id.setFont(new Font("System", 20));
//		CheckBox userCheck = new CheckBox();
//		Image img = new Image("/Client/Views/img/user.png");
//		ImageView view = new ImageView(img);
//		view.setFitHeight(30);
//		view.setFitWidth(30);
//
//		AnchorPane userInfoBox = new AnchorPane();
//
//		AnchorPane.setTopAnchor(userStd_id,10.0);
//		AnchorPane.setTopAnchor(userName,10.0);
//		AnchorPane.setLeftAnchor(userStd_id,130.0);
//		AnchorPane.setLeftAnchor(userName,40.0);
//
//
//		AnchorPane.setTopAnchor(view,5.0);
//		AnchorPane.setLeftAnchor(view, 5.0);
//		AnchorPane.setBottomAnchor(view, 5.0);
//		//AnchorPane.setRightAnchor(view,70.0);
//
//		UserInfoBox.getChildren().add(view);
//
//		AnchorPane.setTopAnchor(userCheck, 15.0);
//		AnchorPane.setLeftAnchor(userCheck,260.0);
//		AnchorPane.setBottomAnchor(userCheck,15.0);
//		AnchorPane.setRightAnchor(userCheck,7.0);
//		UserInfoBox.getChildren().add(userCheck);
//		UserInfoBox.setMaxHeight(40);
//		UserInfoBox.setMaxWidth(300);
    
    //접속유저
    public GridPane EntranceInfoBox(ChatRoomMemberDTO member) {
        GridPane userInfoBox = new GridPane();
		userInfoBox.setHgap(2);

        String userState;
		userState=member.getState();
        //userState.setPrefWidth(60);

		if(userState.equals("0")){
			Image img = new Image("/Client/Views/img/multiply.png");
			ImageView view = new ImageView(img);
			view.setFitWidth(10);
			view.setFitHeight(10);
			userInfoBox.add(view, 3, 0);
		}else {
			Image img = new Image("/Client/Views/img/check.png");
			ImageView view = new ImageView(img);
			view.setFitWidth(10);
			view.setFitHeight(10);
			userInfoBox.add(view, 3, 0);
		}

        Label userStd_id = new Label();
        userStd_id.setPrefWidth(65);
        Label userName = new Label();
        userName.setPrefWidth(50);
        Label userJob = new Label();
        userJob.setPrefWidth(60);
        Button banBtn = new Button();

        

        userStd_id.setText(member.getStd_id());
        userName.setText(member.getName());
        userJob.setText(member.getD_job());
        banBtn.setText("퇴장");

		userName.setTextAlignment(TextAlignment.CENTER);
		userStd_id.setTextAlignment(TextAlignment.CENTER);
		userJob.setTextAlignment(TextAlignment.CENTER);
		//userState.setTextAlignment(TextAlignment.CENTER);

        
;
        userInfoBox.add(userStd_id, 1, 0);
        userInfoBox.add(userName, 0, 0);
        userInfoBox.add(userJob, 2, 0);
        userInfoBox.add(banBtn, 5, 0);
        
        //내가 방장이거나 member의 이름 나와 갖지 않다면 활성화
        if((Status.getCurrentRoom().getLeader_id() == Integer.parseInt(Status.getId())) && !(member.getStd_id().equals(Status.getId()))) {
        	banBtn.setDisable(false);
        }
        else {
        	banBtn.setDisable(true);
        }
        
        banBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			System.out.println(member.getName()+" 강퇴");
    			try {
					URL url = new URL("http://"+SocketConnection.SERVER_IP+":3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "BAN");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(member.getName()+"님이 방에서 퇴장 당하였습니다");
						alert.showAndWait();
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("오류가 발생하였습니다.");
						alert.showAndWait();
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

    
    //대기 유저
    public GridPane WaitingInfoBox(ChatRoomMemberDTO member) {
        GridPane userInfoBox = new GridPane();
		userInfoBox.setHgap(2);

		String userState;
		userState=member.getState();
		//userState.setPrefWidth(60);

		if(userState.equals("0")){
			Image img = new Image("/Client/Views/img/multiply.png");
			ImageView view = new ImageView(img);
			view.setFitWidth(10);
			view.setFitHeight(10);
			userInfoBox.add(view, 3, 0);
		}else {
			Image img = new Image("/Client/Views/img/check.png");
			ImageView view = new ImageView(img);
			view.setFitWidth(10);
			view.setFitHeight(10);
			userInfoBox.add(view, 3, 0);
		}

		Label userStd_id = new Label();
		userStd_id.setPrefWidth(65);
		Label userName = new Label();
		userName.setPrefWidth(50);
		Label userJob = new Label();
		userJob.setPrefWidth(60);;
		Button acceptBtn = new Button();
		Button refuseBtn = new Button();


		userStd_id.setText(member.getStd_id());
		userName.setText(member.getName());
		userJob.setText(member.getD_job());
		acceptBtn.setText("수락");
		refuseBtn.setText("거절");


		userName.setTextAlignment(TextAlignment.CENTER);
		userStd_id.setTextAlignment(TextAlignment.CENTER);
		userJob.setTextAlignment(TextAlignment.CENTER);
		//userState.setTextAlignment(TextAlignment.CENTER);

		;
		userInfoBox.add(userStd_id, 1, 0);
		userInfoBox.add(userName, 0, 0);
		userInfoBox.add(userJob, 2, 0);
        userInfoBox.add(acceptBtn, 4, 0);
        userInfoBox.add(refuseBtn, 5, 0);
        
        //내가 방장이면 활성화
        if(Status.getCurrentRoom().getLeader_id() == Integer.parseInt(Status.getId())) {
        	acceptBtn.setDisable(false);
        	refuseBtn.setDisable(false);
        }
        else {
        	acceptBtn.setDisable(true);
        	refuseBtn.setDisable(true);
        }
        
        acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			System.out.println(member.getName()+" 수락");
    			try {
					URL url = new URL("http://"+SocketConnection.SERVER_IP+":3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "ACCEPT");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(member.getName()+"님을 수락하였습니다.");
						alert.showAndWait();
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_CONFLICT) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("현재 방의 인원이 최대입니다.");
						alert.showAndWait();
					}
					else if(http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("오류가 발생하였습니다.");
						alert.showAndWait();
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
					URL url = new URL("http://"+SocketConnection.SERVER_IP+":3000/chatRoom?room_id="+Status.getCurrentRoom().getRoom_id()+"&std_id="+member.getStd_id());
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("GET");
					http.setRequestProperty("Admission", "REFUSE");
					
					if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText(member.getName()+"님을 거절하였습니다.");
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
    
    
}
