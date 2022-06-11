package Client;

import Client.Models.ChatRoomInfoDTO;

public class Status {
    private static String id;
    private static String pw;
    private static String name;
    private static ChatRoomInfoDTO cur_room;

    public static void setId(String id) {
        Status.id = id;
    }

    public static void setPw(String pw) {
        Status.pw = pw;
    }

    public static void setName(String name) {
        Status.name = name;
    }

    public static void setCurrentRoom(ChatRoomInfoDTO currentRoom) {
        Status.cur_room = currentRoom;
    }

    public static String getId() {
        return id;
    }

    public static String getPw() {
        return pw;
    }

    public static String getName() {
        return name;
    }

    public static ChatRoomInfoDTO getCurrentRoom() {
        return cur_room;
    }

}
