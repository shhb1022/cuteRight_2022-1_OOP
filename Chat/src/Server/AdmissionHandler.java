package Server;

import Server.Models.ChatRoomInfoDTO;
import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class AdmissionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // 입장 신청, 수락, 거절 요청을 처리한다.

            // query 분석
            HashMap<String, String> querys = new HashMap<>();
            StringTokenizer tokenizer = new StringTokenizer(exchange.getRequestURI().getQuery(),"&");
            while(tokenizer.hasMoreTokens()) {
                String[] q = tokenizer.nextToken().split("=");
                querys.put(q[0], q[1]);
            }
            int std_id = Integer.parseInt(querys.get("std_id"));
            int room_id = Integer.parseInt(querys.get("room_id"));
            System.out.println("std_id = "+ std_id + " Room_id = "+room_id);

            String method = exchange.getRequestMethod();
            if(method.equals("PROPOSAL")) {
                DAO.addRequest(std_id, room_id);
            } else if (method.equals("ACCEPT")) {
                DAO.setAccept(std_id, room_id);
            } else if (method.equals("REFUSE")) {
                DAO.setForbid(std_id, room_id);
            }
            // Send Response Headers
            exchange.sendResponseHeaders(200, 0);
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
