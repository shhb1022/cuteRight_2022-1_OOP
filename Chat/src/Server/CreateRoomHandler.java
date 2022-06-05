package Server;

import Server.Models.ChatMessageDTO;
import Server.Models.DAO;
import Server.Models.UsersDTO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CreateRoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("GET")) {

//                String query = exchange.getRequestURI().getQuery();
//                String[] q  = query.split("=",2);

                    ArrayList<UsersDTO> names =  DAO.getAllUsers();
                    JSONArray list = new JSONArray();
                    for(UsersDTO name : names) {
                        JSONObject obj = new JSONObject();
                        obj.put("name",name.getName());
                        list.add(obj);
                    }

                    //이 부분도 추가해야하나??
                    // Encoding to UTF-8
                    ByteBuffer bb = StandardCharsets.UTF_8.encode(list.toJSONString());
                    int contentLength = bb.limit();
                    byte[] content = new byte[contentLength];
                    bb.get(content, 0, contentLength);

                    // Set Response Headers
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Content-Type", "application/json");
                    headers.add("Content-Length", String.valueOf(contentLength));

                    // Send Response Headers
                    exchange.sendResponseHeaders(200, contentLength);

                    respBody.write(content);
                    System.out.println("success to submit messages");
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
