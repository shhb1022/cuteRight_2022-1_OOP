package Server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("GET")) {
                JSONObject js = new JSONObject();
                js.put("hello", "word");
                js.put("this", "is");
                js.put("simple", "server");

                // Encoding to UTF-8
                ByteBuffer bb = Charset.forName("UTF-8").encode(js.toJSONString());
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
            } else if(method.equals("POST")) {
                List<String> author = exchange.getRequestHeaders().get("Authorization");
                System.out.println(author.get(0));

                InputStream is = exchange.getRequestBody();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println(sb.toString());

                String msg = "this is POST METHOD";
                // Encoding to UTF-8
                ByteBuffer bb = Charset.forName("UTF-8").encode(msg);
                int contentLength = bb.limit();
                byte[] content = new byte[contentLength];
                bb.get(content, 0, contentLength);

                // Set Response Headers
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "application/text");
                headers.add("Content-Length", String.valueOf(contentLength));

                // Send Response Headers
                exchange.sendResponseHeaders(200, contentLength);

                respBody.write(content);
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
