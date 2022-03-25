import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MessagePacker {
    public static final int HEADER_SIZE = 8;
    private int bufferSize = 1024;
    private byte[] byteArr;

    MessagePacker(byte[] msg) throws UnknownHostException {
        byteArr = new byte[msg.length + HEADER_SIZE];
        // 헤더 정의
        // STX
        byteArr[0] = (byte) 0x02;
        // OPCODE
        byteArr[1] = (byte) 0x23;
        // ip address
        InetAddress ip = InetAddress.getLocalHost();
        byte[] bytes = ip.getAddress();
        byteArr[2] = bytes[0];
        byteArr[3] = bytes[1];
        byteArr[4] = bytes[2];
        byteArr[5] = bytes[3];
        // body 길이
        byte[] bodySize = intToByteArray(msg.length,2);
        byteArr[6] = (byte) bodySize[0];
        byteArr[7] = (byte) bodySize[1];

        for (int i=0; i< msg.length; i++) {
            byteArr[i+8] = msg[i];
        }
    }

    public byte[] getMessage() {
        return byteArr;
    }

    public static byte[] intToByteArray(int value, int lengthDiv) {
        byte[] byteArray = new byte[lengthDiv];
        if (lengthDiv == 2){
            byteArray[0] = (byte) value;
            byteArray[1] = (byte) (value >>> 8);
        }else if (lengthDiv == 4){
            byteArray[0] = (byte)(value >> 24);
            byteArray[1] = (byte)(value >> 16);
            byteArray[2] = (byte)(value >> 8);
            byteArray[3] = (byte)(value);
        }
        return byteArray;
    }

    public static int byteArrayToInt(byte[] b, int lengthDiv) {
        int byteInt = 0;
        if (lengthDiv == 2) {
            byteInt = ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
        } else if (lengthDiv == 4) {
            byteInt = b[0] & 0xFF |
                    (b[1] & 0xFF) << 8 |
                    (b[2] & 0xFF) << 16 |
                    (b[3] & 0xFF) << 24;
        }
        return byteInt;
    }
}
