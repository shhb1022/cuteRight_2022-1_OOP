package Client.Models;


//현재 방에서 학생들의 목록을 보여줄때 사용
public class ChatRoomJoinDTO {

	private int room_id;
	private int std_id;
	private int join;
	private String name;
	private String d_job;
	private int state;
	
	public ChatRoomJoinDTO(int std_id, int room_id, int join, String name, String d_job, int state) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.join = join;
		this.name = name;
		this.d_job = d_job;
		this.state = state;
	}
	
	
	public ChatRoomJoinDTO() {
		this.std_id = 0;
		this.room_id = 0;
		this.join = 0;
		this.name = null;
		this.d_job = null;
		this.state = 0;
		
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
	
	public int getJoin() {
		return join;
	}
	
	public void setJoin(int join) {
		this.join = join;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getD_job() {
		return d_job;
	}
	
	public void setD_job(String d_job) {
		this.d_job = d_job;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	//나중에 name,d_job,state 추가
}
