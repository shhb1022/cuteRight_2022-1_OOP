package Server;

import Server.Models.UsersDTO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Set;

public class SignInHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("POST")) {
                InputStream is = exchange.getRequestBody();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(br);
                UsersDTO user;

//                obj.forEach((k, v)-> {
//                    if()
//                });

                // Encoding to UTF-8
//                ByteBuffer bb = Charset.forName("UTF-8").encode(msg);
//                int contentLength = bb.limit();
//                byte[] content = new byte[contentLength];
//                bb.get(content, 0, contentLength);

                // Set Response Headers
//                Headers headers = exchange.getResponseHeaders();
//                headers.add("Content-Type", "application/text");
//                headers.add("Content-Length", String.valueOf(contentLength));
//
//                // Send Response Headers
//                exchange.sendResponseHeaders(200, contentLength);
//
//                respBody.write(content);
            }
            // Close Stream
            // 반드시, Response Header를 보낸 후에 닫아야함
            respBody.close();

        } catch (IOException | ParseException e ) {
            e.printStackTrace();

            if( respBody != null ) {
                respBody.close();
            }
        } finally {
            exchange.close();
        }
    }
}
