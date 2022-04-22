import java.net.InetAddress;
import java.util.Arrays;

public class PacketTest {
    public static void main(String args[]) {
        try {
            byte[] data = new String("hellow world!").getBytes("utf-8");
            MessagePacker msg = new MessagePacker(data);
            byte[] result = msg.getPacket();
            byte[] ip = Arrays.copyOfRange(result, 2, 6);
            int msgLength = MessagePacker.byteArrayToInt(Arrays.copyOfRange(result, 6, 8), 2);
            String m = new String(Arrays.copyOfRange(result,8,8+msgLength), "utf-8");
            InetAddress ipAdd = InetAddress.getByAddress(ip);

            System.out.println("ip address: "+ipAdd.getHostAddress());
            System.out.println("message: " + m);
            System.out.println("Message Length: "+Integer.toString(msgLength));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
