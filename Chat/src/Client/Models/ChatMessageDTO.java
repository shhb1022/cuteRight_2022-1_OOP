package Client.Models;

public class ChatMessageDTO {

	int std_id;
	int room_id;
	String message;
	String time;
	
	public ChatMessageDTO(int std_id, int room_id, String message) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.message = message;
		this.time = null;
	}
	
	public ChatMessageDTO(int std_id, int room_id, String message, String time) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.message = message;
		this.time = time;
	}
	
	public ChatMessageDTO() {
		this.std_id = 0;
		this.room_id = 0;
		this.message = null;
		this.time = null;
		
	}
	
	public int getStd_id() {
		return std_id;
	}
	
	public void setStd_id(int std_id) {
		this.std_id = std_id;
	}
	
	public int getRoom_id() {
		return room_id;
	}
	
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
