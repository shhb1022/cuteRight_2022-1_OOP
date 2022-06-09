package Server;

import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class ChatRoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
        	// Write Response Body
            String method = exchange.getRequestMethod();
            DAO dao = new DAO();
            // 쿼리 파싱
            String[] querys = exchange.getRequestURI().getQuery().split("=|&");
            int room_id = Integer.parseInt(querys[1]);
            int std_id = Integer.parseInt(querys[3]);
            if(method.equals("GET")) {
            	boolean check = dao.checkJoin(std_id, room_id);
            	
            	if(check) {
            		exchange.sendResponseHeaders(200, 0);
            		// 채팅 서버에 방이 없다면 추가
                    ChattingServer instance = ChattingServer.getInstance();
            		if(!instance.hasConnection(room_id)) {
            		    instance.addRoom(room_id);
                    }
            	}else {
            		exchange.sendResponseHeaders(400,0);
            	}
            } else if(method.equals("PROPOSAL")) {
                dao.addRequest(std_id, room_id);
                exchange.sendResponseHeaders(200, 0);
            } else if (method.equals("ACCEPT")) {
                //제한인원>현재인원일 때만 입장 수락 가능
                if(dao.checkAccept(room_id)) {
                    dao.setAccept(std_id, room_id);
                    exchange.sendResponseHeaders(200, 0);
                }
                else { //리더에게 '제한인원이 꽉 차서 입장수락 불가능한 상태~' 안내 팝업 출력
                    exchange.sendResponseHeaders(409, 0);
                }
            } else if (method.equals("REFUSE")) {
                dao.setForbid(std_id, room_id);
                exchange.sendResponseHeaders(200, 0);
            }
            // Close Stream
            // 반드시, Response Header를 보낸 후에 닫아야함
            respBody.close();

        } catch ( IOException e ) {
            e.printStackTrace();

            if( respBody != null ) {
                respBody.close();
            }
        } finally {
            exchange.close();
        }
    }
}
