package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChattingServer {
    ExecutorService executorService;
    ServerSocket serverSocket;
    List<Client> connections = new Vector<Client>();

    void start() {
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(serverSocket.getInetAddress(), 5001));
        } catch (Exception e) {
            if(!serverSocket.isClosed()) { stop(); }
            System.out.println("채팅 서버가 연결이 안됩니다.");
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Start Chatting Server.Server");
                while(true) {
                    try {
                        Socket socket = serverSocket.accept();
                        String message = "[연결 수락:" + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
                        System.out.println(message);
                        Client client = new Client(socket);
                        connections.add(client);
                        System.out.println("[연결 개수" + connections.size() + "]");
                    } catch (Exception e) {
                        if(!serverSocket.isClosed()) {stop();}
                        break;
                    }
                }
            }
        };
        executorService.submit(runnable);
    }

    void stop() {
        try {
            Iterator<Client> iterator = connections.iterator();
            while(iterator.hasNext()) {
                Client client = iterator.next();
                client.socket.close();
                iterator.remove();
            }
            if(serverSocket!=null && !serverSocket.isClosed()) { serverSocket.close(); }
            if(executorService!=null && !executorService.isShutdown()) { executorService.shutdown(); }
            System.out.println("서버 멈춤");
        } catch (Exception e) { }
    }

    public class Client {
        Socket socket;
        int room_id;
        int std_id;

        Client(Socket socket) {
            this.socket = socket;
            receive();
        }

        void receive() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            InputStream is = socket.getInputStream();
                            MessagePacker packet = MessagePacker.unpack(is);
                            String message = "[요청 처리: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
                            for(Client client : connections) {
                                client.send(packet.getPacket());
                            }
                        }
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                            connections.remove(Client.this);
                            System.out.println("클라이언트 통신 안됨");
                            socket.close();
                        } catch (IOException e2) {}
                    }
                }
            };
            executorService.submit(runnable);
        }

        void send(byte[] byteArr) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(byteArr);
                outputStream.flush();
            } catch (Exception e) {
                try {
                    System.out.println("클라이언트 통신 안됨");
                    connections.remove(Client.this);
                    socket.close();
                } catch (IOException e2) {}
            }
        }
    }
}
