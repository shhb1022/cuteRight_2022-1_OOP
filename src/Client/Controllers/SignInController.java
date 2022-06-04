package Client.Controllers;

import Client.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.Headers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Client.Models.*;

public class SignInController implements Initializable {
    @FXML private TextField nameInput,newIdInput,jobInput,newPwdInput;
    @FXML private Button signUpDoneBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	signUpDoneBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = nameInput.getText();
                String std_id = newIdInput.getText();
                String d_job = jobInput.getText();
                String pwd = newPwdInput.getText();

                System.out.println("이름: " + name);
                System.out.println("아이디" + std_id);
                System.out.println("직업: " + d_job);
                System.out.println("패스워드: " + pwd); 
                
            	UsersDTO users = new UsersDTO(Integer.parseInt(std_id),name,d_job,0,pwd);
                // 서버와 연결
                try {               	
                	URL url = new URL("http://localhost:3000/signin");
                	HttpURLConnection http = (HttpURLConnection) url.openConnection();
                	http.setRequestMethod("POST");                
                	http.setDoOutput(true);
                	
                    ByteBuffer bb = StandardCharsets.UTF_8.encode(users.toJSONString());
                    int contentLength = bb.limit();
                    byte[] content = new byte[contentLength];
                    bb.get(content, 0, contentLength);
            		
                	http.setRequestProperty("Content-Type", "application/json");
                	http.setRequestProperty("Content-Length", String.valueOf(contentLength));
                        
                	OutputStream os = http.getOutputStream();
            		os.write(content,0,content.length);
                	
                    // 요청 방식 구하기
                	System.out.println("getRequestMethod():" + http.getRequestMethod());
                    // 응답 코드 구하기
                	System.out.println("getResponseCode():" + http.getResponseCode());
                    // 응답 메시지 구하기
                	System.out.println("getResponseMessage():" + http.getResponseMessage());
                	
                	if (http.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                		System.out.println("회원가입에 성공했습니다.");
                		// 현재 창을 종료한다.
                        Stage currStage = (Stage) signUpDoneBtn.getScene().getWindow();
                        currStage.close();

                        // 새 창을 띄운다.
                        Stage stage = new Stage();
                        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Login.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                		
                		}
                	else if (http.getResponseCode() == HttpURLConnection.HTTP_CONFLICT) {
                		// 경고 메세지를 출력한다.
                		Alert alert = new Alert(AlertType.INFORMATION);                            
                		alert.setHeaderText(null);
                		alert.setContentText("아이디가 중복됩니다.");
                		alert.showAndWait();
                		}
                	else if (http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                		// 경고 메세지를 출력한다.
                		Alert alert = new Alert(AlertType.INFORMATION);
                		alert.setHeaderText(null);                		
                		alert.setContentText("오류가 발생했습니다.");
                		alert.showAndWait();
                		}
                } catch (Exception e) {
                	e.printStackTrace();
                	}             
            }
        });

    }
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
