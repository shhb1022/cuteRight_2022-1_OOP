package Server.Models;

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
   
   //로그인 id,pwd 비교해서 로그인-> select하고 동시에 update하는게 복잡한것 같아서 
   //로그인 성공시 state=1로 변경은 로그인 핸들러부분에서 lg=1이면 dao.setLogin(std_id) 이거 한번 더 실행시키는 방식이 좋아보임,,
   public static int checkLogin(int std_id, String pwd) {
       Connection con = null;
       //Statement stmt = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       String SQL = "SELECT pwd FROM Users WHERE std_id = ?";
       try {
           con = makeConnection();
           pstmt=con.prepareStatement(SQL);
           pstmt.setInt(1, std_id);
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
               //if(stmt != null) stmt.close();
               if(con != null) con.close();
           } catch (Exception e2) {
               e2.printStackTrace();
           }
       }
       return -2;//db 오류,,
   }
      
   //로그인 가능한 상태인지 확인
   public static boolean checkState(int std_id) {
       Connection con = null;
       Statement stmt = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       String SQL = "SELECT state FROM Users WHERE std_id = ?";
       try {
           con = makeConnection();
           pstmt=con.prepareStatement(SQL);
           pstmt.setInt(1, std_id);
           rs = pstmt.executeQuery();
           if(rs.next()) {
               if(rs.getString(1).contentEquals("0")) {
                   return true;//로그인 가능 상태
               }
               else {
                   return false;//이미 state = 1이므로 로그인 불가능 상태
               }
           }
           return false; //db에 아이디 존재하지 않음 
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
       return false; //db 연결오류      
    }
      
   //로그인 성공시 state=1으로 변경
   public static int setLogin(int std_id) {
        Connection con = null;
        Statement stmt = null;
        String update = "UPDATE Users SET state = 1 WHERE std_id ="+std_id;
        try {
           con = makeConnection();
           stmt = con.createStatement();
           int i = stmt.executeUpdate(update);
           if(i==1)
              System.out.println("state=1 변경 성공");
           else 
              System.out.println("state=1 변경 실패");
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
        return -2;//db 오류
    }
   //로그아웃 가능한 상태인지 확인
   public static boolean checkState2(int std_id) {
         Connection con = null;
         Statement stmt = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         String SQL = "SELECT state FROM Users WHERE std_id = ?";
         try {
            con = makeConnection();
            pstmt=con.prepareStatement(SQL);
            pstmt.setInt(1, std_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
               if(rs.getString(1).contentEquals("1")) {
                  return true;//state =1이므로 로그아웃 가능
               }
               else {
                  return false;//state=0 로그아웃 불가능
               }
            }
            return false; //db에 아이디 존재하지 않음
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
         return false; //db 연결오류
      }
   
   //로그아웃 state=0으로 변경
   public static int setLogout(int std_id) {
        Connection con = null;
        Statement stmt = null;
        String update = "UPDATE Users SET state = 0 WHERE std_id ="+std_id;
        try {
           con = makeConnection();
           stmt = con.createStatement();
           int i = stmt.executeUpdate(update);
           if(i==1)
              System.out.println("state=0 변경 성공");
           else 
              System.out.println("state=0 변경실패");
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
        return -2;//db 오류
    }
   //회원가입
   public static boolean addSignUp(UsersDTO Users) {
       Connection con = null;
       Statement stmt = null;
       try {
           con = makeConnection();
           stmt = con.createStatement();
           String insert = "INSERT INTO Users (std_id, name, d_job, state, pwd) VALUES ";
           insert+="('"+Users.getStd_id()+"','"+Users.getName()+"','"+Users.getD_job()+"','"+Users.getState()+"','"+Users.getPwd()+"')";
           System.out.println(insert);
           int i = stmt.executeUpdate(insert);
           if(i==1)
                 return true;
           else 
               return false;
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           return false;
       }finally {
           try {
               if(stmt != null) stmt.close();
               if(con != null) con.close();
           } catch (Exception e2) {
               e2.printStackTrace();
           }
       }
   }
   //회원가입시 중복아이디 검사 미작동..
   public static boolean checkDuplicate(int std_id) {
       Connection con = null;
       Statement stmt = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       String SQL = "SELECT std_id FROM Users";
       try {
           con = makeConnection();
           pstmt=con.prepareStatement(SQL);
           //pstmt.setInt(1, std_id);
           rs = pstmt.executeQuery();
           if(rs.next()) {
               if(rs.getString("std_id").contentEquals(toString(std_id))) {
                   return false;//이미 회원가입된 학번
               }
               else {
                   return true;//중복된 아이디 없으므로 회원가입 가능 상태
               }
           }
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
       return false; //db 연결오류      
   }

   //checkDuplicate에서 std_id string으로 사용하기 위한 사전작업
   private static StringBuffer toString(int std_id) {
      // TODO Auto-generated method stub
      return null;
   }
   //내 채팅방 출력
   public static ArrayList<ChatRoomInfoDTO> getMyRoom(int std_id){
      Connection con = null;
      Statement stmt= null;
      ResultSet rs = null;
      ArrayList<ChatRoomInfoDTO> result = new ArrayList<ChatRoomInfoDTO>();
      try {
         con = makeConnection();
         stmt = con.createStatement();
         rs = stmt.executeQuery("SELECT * FROM ChatRoomJoin WHERE std_id ="+ std_id);
         
         while(rs.next()) {
            ChatRoomInfoDTO chatRoom = new ChatRoomInfoDTO();
            
            chatRoom.setRoom_id(rs.getInt("rood_id"));
            chatRoom.setTitle(rs.getString("title"));
            chatRoom.setLimit_person(rs.getInt("limit_person"));
            chatRoom.setCur_person(rs.getInt("cur_person"));
            chatRoom.setLeader_id(rs.getInt("leader_id"));

            result.add(chatRoom);
         }
         return result;
         
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return null;
      } finally {
         try {
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            if(con != null) con.close();
         } catch (Exception e2) {
            e2.printStackTrace();
         }
      }
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
         rs = stmt.executeQuery("SELECT * FROM ChatMessage WHERE room_id ="+room_id);
         
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
   // 채팅 시 이름 출력때문에 있는듯? 
   public static String stdName(int user_id){
      Connection con = null;
      Statement stmt = null;
      ResultSet rs = null;
      try {
         con = makeConnection();
         stmt = con.createStatement();
         rs = stmt.executeQuery("SELECT name FROM Users WHERE std_id="+user_id);
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

<<<<<<< HEAD
	}
	//
	//
	//모든 user 정보 가져오기->방 생성 시 친구 선택할 때 필요
	public static ArrayList<UsersDTO> getAllUsers(){
		Connection con = null;
		Statement stmt= null;
		ResultSet rs = null;
		ArrayList<UsersDTO> result = new ArrayList<UsersDTO>();
		try {
			con = makeConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT std_id,name,d_job FROM Users");
			
			while(rs.next()) {
				UsersDTO user = new UsersDTO();
				user.setStd_id(rs.getInt("std_id"));
				user.setName(rs.getString("name"));
				//user.setD_job(rs.getString("d_job"));
				result.add(user);
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
	
	//방 생성 정보 추가
	//room_id는 AI 설정해놓긴 했다만, 추가적인 작업 필요없나?
	//방을 생성한 leader에 대해 addMember 수행, Cur_Person 증가 수행(cur_person=1로 삽입 시 Cur_person 증가 수행은 필요 없음)
	public static int addRoom(ChatRoomInfoDTO room) {
		Connection con = null;
		Statement stmt = null;
		int generated_key = 0;
		try {
			con = makeConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			String insert = "INSERT INTO ChatRoomInfo (title,limit_person,cur_person,leader_id) VALUES ";
			insert+="('"+room.getTitle()+"','"+room.getLimit_person()+"','"+0+"','"+room.getLeader_id()+"')";
			System.out.println(insert);
			int i = stmt.executeUpdate(insert);

			if (i == 1) {
				ResultSet generated_keys = stmt.executeQuery("SELECT seq FROM sqlite_sequence WHERE name='ChatRoomInfo'");
				if (generated_keys.next()) {
					generated_key = generated_keys.getInt(1);
					System.out.println("레코드 추가 성공, room_id: "+ generated_key);
					con.commit();
				}
			}
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
			return generated_key;
		}
	}
	
	//user들의 join=1 상태 참가로 추가(채팅방 생성할 때 선택한 친구 추가하는 메서드로, 입장 신청과는 별개)
	public static void addMember(int user_id,int room_id) {
		Connection con = null;
	    Statement stmt = null;
	    try {
			con = makeConnection();
			stmt = con.createStatement();
			String insert = "INSERT INTO ChatRoomJoin (room_id,std_id,member) VALUES ";
			insert+="('"+room_id+"','"+user_id+"','"+1+"')";
			System.out.println(insert);
			int i = stmt.executeUpdate(insert);
			if(i==1) {
				System.out.println("레코드 추가 성공");
				//Chat_Info에 cur_person증가는 따로 메소드로 뻄
				//String update = "UPDATE ChatRoomInfo SET cur_person=cur_person+1 WHERE room_id = "+room_id;
				//int j = stmt.executeUpdate(update);
				//if(j==1)System.out.println("레코드 갱신 성공");
				//else System.out.println("레코드 갱신 실패");	
			}
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
	
	//현재인원 증가
	public static void increCur_person(int room_id) {
		Connection con = null;
	    Statement stmt = null;
	    String update = "UPDATE ChatRoomInfo SET cur_person=cur_person+1 WHERE room_id = "+room_id;
	    try {
	        con = makeConnection();
	        stmt = con.createStatement();
	        int i = stmt.executeUpdate(update);
	        if(i==1)
	            System.out.println("레코드 갱신 성공");
	        else 
	            System.out.println("레코드 갱신 실패");
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
	
	//현재인원 감소
	public void decreCur_person(int room_id) {
		Connection con = null;
	    Statement stmt = null;
	    String update = "UPDATE ChatRoomInfo SET cur_person=cur_person-1 WHERE room_id = "+room_id;
	    try {
	        con = makeConnection();
	        stmt = con.createStatement();
	        int i = stmt.executeUpdate(update);
	        if(i==1)
	            System.out.println("레코드 갱신 성공");
	        else 
	            System.out.println("레코드 갱신 실패");
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
	
	//방에 참가중인 모든 user(member=*) 정보 가져오기
	public ArrayList<ChatRoomMemberDTO> getRoomMembers(int room_id){
		Connection con = null;
		Statement stmt= null;
		ResultSet rs = null;
		ArrayList<ChatRoomMemberDTO> result = new ArrayList<ChatRoomMemberDTO>();
		try {
			con = makeConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Users INNER JOIN (SELECT * FROM ChatRoomJoin WHERE room_id="+room_id+")");
			
			while(rs.next()) {
				ChatRoomMemberDTO user = new ChatRoomMemberDTO();
				user.setStd_id(rs.getInt("std_id"));
				user.setName(rs.getString("name"));
				user.setD_job(rs.getString("d_job"));
				user.setMember(rs.getInt("member"));
				user.setState(rs.getInt("state"));
				result.add(user);
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
	
	//user가 방에 입장 가능한지 확인
	public boolean checkJoin(int user_id,int room_id) {
	    Connection con = null;
	    Statement stmt = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String SQL ="SELECT member FROM ChatRoomJoin WHERE std_id="+user_id+"AND room_id="+room_id ;	    
	    try {
	        con = makeConnection();
	        pstmt=con.prepareStatement(SQL);
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	            if(rs.getString("member").contentEquals("1")) {
	                return true;//입장 가능 상태
	            }
	            else {
	                return false;//입장 불가 상태
	            }
	        }
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
	    return false; //db 연결오류 	
	}
	
}
=======
   }
   //
   //
   //모든 user 정보 가져오기->방 생성 시 친구 선택할 때 필요
   public static ArrayList<UsersDTO> getAllUsers(){
      Connection con = null;
      Statement stmt= null;
      ResultSet rs = null;
      ArrayList<UsersDTO> result = new ArrayList<UsersDTO>();
      try {
         con = makeConnection();
         stmt = con.createStatement();
         rs = stmt.executeQuery("SELECT std_id,name,d_job FROM Users");
         
         while(rs.next()) {
            UsersDTO user = new UsersDTO();
            user.setStd_id(rs.getInt("std_id"));
            user.setName(rs.getString("name"));
            //user.setD_job(rs.getString("d_job"));
            result.add(user);
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
   
   //방 생성 정보 추가
   //room_id는 AI 설정해놓긴 했다만, 추가적인 작업 필요없나?
   //방을 생성한 leader에 대해 addMember 수행, Cur_Person 증가 수행(cur_person=1로 삽입 시 Cur_person 증가 수행은 필요 없음)
   public static void addRoom(ChatRoomInfoDTO room) {
      Connection con = null;
      Statement stmt = null;
      try {
         con = makeConnection();
         stmt = con.createStatement();
         String insert = "INSERT INTO ChatRoomInfo (title,limit_person,cur_person,leader_id) VALUES ";
         insert+="('"+room.getTitle()+"','"+room.getLimit_person()+"','"+0+"','"+room.getLeader_id()+"')";
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
   
   //user들의 join=1 상태 참가로 추가(채팅방 생성할 때 선택한 친구 추가하는 메서드로, 입장 신청과는 별개)
   public static void addMember(int user_id,int room_id) {
      Connection con = null;
       Statement stmt = null;
       try {
         con = makeConnection();
         stmt = con.createStatement();
         String insert = "INSERT INTO ChatRoomJoin (room_id,std_id,member) VALUES ";
         insert+="('"+room_id+"','"+user_id+"','"+1+"')";
         System.out.println(insert);
         int i = stmt.executeUpdate(insert);
         if(i==1) {
            System.out.println("레코드 추가 성공");
            //Chat_Info에 cur_person증가는 따로 메소드로 뻄
            //String update = "UPDATE ChatRoomInfo SET cur_person=cur_person+1 WHERE room_id = "+room_id;
            //int j = stmt.executeUpdate(update);
            //if(j==1)System.out.println("레코드 갱신 성공");
            //else System.out.println("레코드 갱신 실패");   
         }
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
   
   //현재인원 증가
   public void increCur_person(int room_id) {
      Connection con = null;
       Statement stmt = null;
       String update = "UPDATE ChatRoomInfo SET cur_person=cur_person+1 WHERE room_id = "+room_id;
       try {
           con = makeConnection();
           stmt = con.createStatement();
           int i = stmt.executeUpdate(update);
           if(i==1)
               System.out.println("레코드 갱신 성공");
           else 
               System.out.println("레코드 갱신 실패");
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
   
   //현재인원 감소
   public void decreCur_person(int room_id) {
      Connection con = null;
       Statement stmt = null;
       String update = "UPDATE ChatRoomInfo SET cur_person=cur_person-1 WHERE room_id = "+room_id;
       try {
           con = makeConnection();
           stmt = con.createStatement();
           int i = stmt.executeUpdate(update);
           if(i==1)
               System.out.println("레코드 갱신 성공");
           else 
               System.out.println("레코드 갱신 실패");
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
   
   //방에 참가중인 모든 user(member=*) 정보 가져오기
   public ArrayList<ChatRoomMemberDTO> getRoomMembers(int room_id){
      Connection con = null;
      Statement stmt= null;
      ResultSet rs = null;
      ArrayList<ChatRoomMemberDTO> result = new ArrayList<ChatRoomMemberDTO>();
      try {
         con = makeConnection();
         stmt = con.createStatement();
         rs = stmt.executeQuery("SELECT * FROM Users INNER JOIN (SELECT * FROM ChatRoomJoin WHERE room_id="+room_id+")");
         
         while(rs.next()) {
            ChatRoomMemberDTO user = new ChatRoomMemberDTO();
            user.setStd_id(rs.getInt("std_id"));
            user.setName(rs.getString("name"));
            user.setD_job(rs.getString("d_job"));
            user.setMember(rs.getInt("member"));
            user.setState(rs.getInt("state"));
            result.add(user);
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
   
   //user가 방에 입장 가능한지 확인
   public boolean checkJoin(int user_id,int room_id) {
       Connection con = null;
       Statement stmt = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       String SQL ="SELECT member FROM ChatRoomJoin WHERE std_id="+user_id+"AND room_id="+room_id ;       
       try {
           con = makeConnection();
           pstmt=con.prepareStatement(SQL);
           rs = pstmt.executeQuery();
           if(rs.next()) {
               if(rs.getString("member").contentEquals("1")) {
                   return true;//입장 가능 상태
               }
               else {
                   return false;//입장 불가 상태
               }
           }
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
       return false; //db 연결오류    
   }
   
}
>>>>>>> f777e39857b8f1bdeca67a712a369e1364a42bb1
