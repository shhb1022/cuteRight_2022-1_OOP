package Server;

import Server.Models.DAO;
import Server.Models.UsersDTO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreateRoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        ArrayList<UsersDTO> friendlist = new ArrayList();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            Headers headers = exchange.getResponseHeaders();
            if(method.equals("GET")) {
                String query = exchange.getRequestURI().getQuery();
                String[] author = exchange.getRequestHeaders().get("Authorization").get(0).split(":");
                int id = Integer.parseInt(author[0]);
                String password = author[1];

                int lg = DAO.checkLogin(id, password);

                if(lg == 1) {
                    friendlist = DAO.getAllUsers();
                    exchange.sendResponseHeaders(200, 0);
                } else if (lg == 0 || lg == -1){
                    exchange.sendResponseHeaders(400, 0);
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
