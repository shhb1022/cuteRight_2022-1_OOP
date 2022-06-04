package Client.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class DAO {
 
	// DB 접근
	public static Connection makeConnection() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("드라이버 적재 성공");
			con = DriverManager.getConnection("jdbc:sqlite:Chat_Server.db");
			System.out.println("데이터베이스 연결 성공");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다.");
		}
		return con;
	}
	//로그인
	public static int loginUser(String std_id, String pwd) {
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT pwd FROM Users WHERE std_id = ?";

		try {
			con = makeConnection();
			pstmt=con.prepareStatement(SQL);
			pstmt.setString(1, std_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).contentEquals(pwd)) {
					return 1;//로그인 성공
				}
				else {
					return 0;//비밀번호 불일치
				}
			}
			return -1; //db에 아이디 존재하지 않음
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();//5) 자원반납
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -2;//db 오류,,
	}
	
	// 받은 메시지를 db에 추가
	public static void addchatMessage(ChatMessageDTO Chat){
		Connection con = null;
		Statement stmt = null;
		try {
			con = makeConnection();
			stmt = con.createStatement();
			String insert = "INSERT INTO ChatMessage (std_id, room_id, message, time) VALUES ";
			
			//받은 시간
			LocalTime now = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			String formatedNow = now.format(formatter);
			
			insert+="('"+Chat.getStd_id()+"','"+Chat.getRoom_id()+"','"+Chat.getMessage()+"','"+formatedNow+"')";
			System.out.println(insert);
			int i = stmt.executeUpdate(insert);
			if(i==1)
				System.out.println("레코드 추가 성공");
			else 
				System.out.println("레코드 추가 실패");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	//
	// 채팅방 입장시 채팅내용 출력
	public static ArrayList<ChatMessageDTO> allMessage(int room_id) {
		Connection con = null;
		Statement stmt= null;
		ResultSet rs = null;
		ArrayList<ChatMessageDTO> result = new ArrayList<ChatMessageDTO>();
		try {
			con = makeConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ChatMessage WHERE room_id LIKE "+room_id);
			
			while(rs.next()) {
				ChatMessageDTO chat = new ChatMessageDTO();
				
				chat.setStd_id(rs.getInt("std_id"));
				chat.setRoom_id(room_id);
				chat.setMessage(rs.getString("message"));
				chat.setTime(rs.getString("time"));
			
				result.add(chat);
			}
			
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();//5) 자원반납
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	//
	// 학생의 정보
	public static String stdName(int std_id){
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = makeConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT name FROM Users INNER JOIN chatMessage ON chatMessage.std_id = Users.std_id");
			return rs.getString("name");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();//5) 자원반납
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
	
	// 현재 방에 참여하고 있는 학생의 목록
	public static ArrayList<ChatRoomJoinDTO> memberJoin(int room_id) {
		Connection con = null;
		Statement stmt= null;
		ResultSet rs = null;
		ArrayList<ChatRoomJoinDTO> result = new ArrayList<ChatRoomJoinDTO>();
		try {
			con = makeConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Users INNER JOIN (SELECT * FROM ChatRoomJoin WHERE room_id LIKE "+ room_id + ") roomjoin "
					+ "ON roomjoin.std_id = Users.std_id");
			
			while(rs.next()) {
				ChatRoomJoinDTO join = new ChatRoomJoinDTO();
				
				join.setStd_id(rs.getInt("std_id"));
				join.setRoom_id(rs.getInt("room_id"));
				join.setJoin(rs.getInt("join"));
				join.setName(rs.getString("name"));
				join.setD_job(rs.getString("d_job"));
				join.setState(rs.getInt("state"));
			
				result.add(join);
			}
			
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				//if(rs != null) rs.close();//5) 자원반납
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
