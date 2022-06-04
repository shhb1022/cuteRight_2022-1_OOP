package Client.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateRoomController implements Initializable {
    @FXML private TextField setTitle;
    @FXML private ComboBox setLimitPersonnel;
   // ObservableList list = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
    @FXML private ListView usersDisplay;
    @FXML private Button backtoMainBtn2,createBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Integer> comboNumList = FXCollections.observableArrayList(1,2,3,4,5,6);
        setLimitPersonnel.setItems(comboNumList);





        //ObservableList<String> friendList = FXCollections
    	backtoMainBtn2.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			try {
                	// 현재 창을 종료한다.
                    Stage currStage = (Stage) backtoMainBtn2.getScene().getWindow();
                    currStage.close();
                    
        			Stage stage = new Stage();
                    Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Main.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
    			} catch(Exception e) {
                    e.printStackTrace();
                }
    		}
    	});
//    	createBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                String title = setTitle.getText();
//                int limit_person = 10;
//                int leader_id = Integer.parseInt(UserInfo.getId());
//
//                System.out.println("title: " + title);
//                System.out.println("limit_person: " + limit_person);
//
//                
//            	ChatRoomInfoDTO Room = new ChatRoomInfoDTO(0, title, limit_person, 1, leader_id);
//            	int[] users = new int[limit_person -1];
//            	
//                // 서버와 연결                
//                try {               	
//                	URL url = new URL("http://localhost:3000/createroom");
//                	HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                	http.setRequestMethod("POST");  
//                	http.setDoOutput(true);
//                	
//                	//json 처음에 방 정보를 처음에 추가하고 나머지는 추가할 유저 목록
//                    JSONArray list = new JSONArray();
//                    list.add(Room.toString());
//                    for(int user : users) {
//                        JSONObject obj = new JSONObject();
//                        obj.put("std_id",user);
//                        list.add(obj);
//                    }
//                	                   
//                    ByteBuffer bb = StandardCharsets.UTF_8.encode(list.toJSONString());
//                    int contentLength = bb.limit();
//                    byte[] content = new byte[contentLength];
//                    bb.get(content, 0, contentLength);
//
//                    // Set Response Headers
//                	http.setRequestProperty("Content-Type", "application/json;utf-8");
//                    http.setRequestProperty("Content-Length", String.valueOf(contentLength));
//    	
//    				OutputStream os = http.getOutputStream();
//					os.write(content,0,content.length);
//                    
//                    // 요청 방식 구하기
//                	System.out.println("getRequestMethod():" + http.getRequestMethod());
//                    // 응답 코드 구하기
//                	System.out.println("getResponseCode():" + http.getResponseCode());
//                    // 응답 메시지 구하기
//                	System.out.println("getResponseMessage():" + http.getResponseMessage());
//                	
//                	if (http.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
//                		System.out.println("방 생성에 성공했습니다.");
//                		// 현재 창을 종료한다.
//                		Stage currStage = (Stage) setTitle.getScene().getWindow();
//                		currStage.close();
//                		}
//                	else if (http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
//                		// 경고 메세지를 출력한다.
//                		Alert alert = new Alert(AlertType.INFORMATION);
//                		alert.setHeaderText(null);                		
//                		alert.setContentText("오류가 발생했습니다.");
//                		alert.showAndWait();
//                		}
//                } catch (Exception e) {
//                	e.printStackTrace();
//                	}             
//            }
//        });
//   	usersList();
    }
    
    //userslist Request
//    void usersList() {
//    	 try {
//    	URL url = new URL("http://localhost:3000/createroom");
//        HttpURLConnection http = (HttpURLConnection) url.openConnection();
//        http.setRequestMethod("GET");
//        http.setRequestProperty("Authorization", UserInfo.getId()+":"+UserInfo.getPw());
//        
//        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            String resBody = getResponseBody(http.getInputStream());
//            System.out.println(resBody);
//            JSONParser parser = new JSONParser();
//            JSONArray list = (JSONArray)parser.parse(resBody);
//            for(int i=0; i<list.size(); i++) {
//                JSONObject obj = (JSONObject) list.get(i);
//                //리스트 뷰 추가하고 어딘가에 저장
//                }
//            }
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	}
//    }
    
    public static String getResponseBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

}
