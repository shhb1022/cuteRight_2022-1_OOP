package Client;

public class UserInfo {
    private static String id;
    private static String pw;
    private static String room_id;

    public static void setId(String id) {
        UserInfo.id = id;
    }

    public static void setPw(String pw) {
        UserInfo.pw = pw;
    }

    public static void setRoom_id(String room_id) {
        UserInfo.room_id = room_id;
    }

    public static String getId() {
        return id;
    }

    public static String getPw() {
        return pw;
    }

    public static String getRoom_id() {
        return room_id;
    }
}
