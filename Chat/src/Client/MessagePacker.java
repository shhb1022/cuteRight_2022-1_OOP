package Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MessagePacker {
    public static final int HEADER_SIZE = 8;
    public static final int BUFFER_SIZE = 1024;
    private byte[] byteArr;
    private InetAddress ip;
    private byte[] message;

    public MessagePacker(byte[] msg) throws UnknownHostException {
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
            byteArr[HEADER_SIZE+i] = msg[i];
        }
        this.message = msg;
        this.ip = ip;
    }

    MessagePacker(byte[] header, byte[] msg) throws UnknownHostException {
        byteArr = new byte[msg.length + HEADER_SIZE];
        for(int i=0; i<header.length; i++) {
            byteArr[i] = header[i];
        }
        for(int i=0; i<msg.length; i++) {
            byteArr[header.length + i] = msg[i];
        }
        this.message = msg;
        this.ip = InetAddress.getByAddress(Arrays.copyOfRange(header, 2,6));
    }

    public static MessagePacker unpack(InputStream inputStream) throws Exception {
        // packet의 시작점이 나올때까지 while문을 돌림
        int readByteCount;
        do {
            readByteCount = inputStream.read();
            if(readByteCount == -1) { throw new IOException(); }
        } while (readByteCount != 0x02);

        byte[] headerBuffer = new byte[HEADER_SIZE];
        byte[] tmp = new byte[HEADER_SIZE - 1];
        inputStream.read(tmp);
        headerBuffer[0] = 0x02;
        System.arraycopy(tmp, 0,headerBuffer,1,tmp.length);

        // data 길이 체크
        byte[] lengthChk = new byte[2];
        lengthChk[0] = headerBuffer[6];
        lengthChk[1] = headerBuffer[7];
        int dataLength = byteArrayToInt(lengthChk,2);

        // Message 내용을 담을 버퍼
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] receiveData = new byte[dataLength];
        int read;

        // 버퍼 안의 데이터를 다 읽을 때까지 반복문을 돌린다.
        while((read = inputStream.read(receiveData,0,receiveData.length))!=-1) {
            buffer.write(receiveData,0,read);
            dataLength = dataLength - read;
            if (dataLength<=0) {
                break;
            }
        }

        MessagePacker packet = new MessagePacker(headerBuffer, buffer.toByteArray());
        buffer.flush();
        buffer.close();
        // MessagePacker 객체를 리턴한다.
        return packet;
    }

    public byte[] getPacket() {
        return byteArr;
    }

    public byte[] getMessage() { return message; }

    public InetAddress getIp() { return ip; }

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
