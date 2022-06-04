package Client.Models;


import org.json.simple.JSONObject;

//현재 방에서 학생들의 목록을 보여줄때 사용
public class ChatRoomMemberDTO {

	private int room_id;
	private int std_id;
	private int member;
	private String name;
	private String d_job;
	private int state;
	
	public ChatRoomMemberDTO(int std_id, int room_id, int member, String name, String d_job, int state) {
		this.std_id = std_id;
		this.room_id = room_id;
		this.member = member;
		this.name = name;
		this.d_job = d_job;
		this.state = state;
	}
	
	
	public ChatRoomMemberDTO() {
		this.std_id = 0;
		this.room_id = 0;
		this.member = 0;
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
	
	public int getMember() {
		return member;
	}
	
	public void setMember(int member) {
		this.member = member;
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

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("std_id", this.std_id);
		obj.put("room_id", this.room_id);
		obj.put("member", this.member);
		obj.put("name", this.name);
		obj.put("d_job", this.d_job);
		obj.put("state", this.state);
		return obj;
	}

	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
}
