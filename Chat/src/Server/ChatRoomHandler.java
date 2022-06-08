package Server;

import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ChatRoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
        	// Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("GET")) {
            	String[] querys = exchange.getRequestURI().getQuery().split("=|&");
            	int room_id = Integer.parseInt(querys[1]);
            	int std_id = Integer.parseInt(querys[3]);
            	DAO dao = new DAO();
            	
            	boolean check = dao.checkJoin(std_id, room_id);
            	
            	if(check) {
            		exchange.sendResponseHeaders(200, 0);
            	}else {
            		exchange.sendResponseHeaders(400,0);
            	}
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
