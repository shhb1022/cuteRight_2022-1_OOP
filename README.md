# 팀프로젝트용 오픈채팅 프로그램 '팀프리'_cuteRight
2022 객체지향 패러다임 프로젝트


*프로그램 설명  
-팀 프로젝트용 오픈채팅 프로그램 '팀프리'  
-팀원 모집에서부터 팀 회의까지의 과정을 한번에!  
-학교와 같은 소규모 집단에서의 협업을 하는 학생들을 위한 프로그램  
-Leader 기능을 두어 프로젝트 리더로서 역할을 할 수 있게함  
-팀원 구인, 단톡방 생성의 과정을 최소화 

*기능  
-1:n 채팅(n>=1): 원하는 팀원을 선택하여 채팅방 생성 가능, 참여한 상대와 자유롭게 대화 가능. 
-회원가입: 회원가입을 통해 DB에 등록된 사용자는 자동으로 친구 명단에 포함됨(프로그램을 이용하기 위해서는 필수로 회원가입 해야함)  
-로그인, 로그아웃: 등록한 계정으로 로그인하여 프로그램을 시작, 로그아웃 버튼을 눌러 접속 해제.  
-친구 상태 실시간 확인: 채팅방에 접속중인 사용자를 대상으로 온라인, 오프라인 상태 확인 가능, 친구 목록은 채팅방에서 즉각적으로 확인 가능  
-Leader: 팀프로젝트 진행이 주 목표인 프로그램이므로 프로젝트 리더에게 '방장' 권한을 부여해 채팅방 운영에 필요한 기능이 활성화 됨.  
-채팅방 입장제어: 승인된 사용자만이 채팅방에 입장할 수 있도록 Leader는 입장 신청한 사용자에 대한 통제권이 있음, 입장을 원하는 사용자는 입장 신청 시, 입장 대기열에 들어감.  
-강제퇴장: 프로젝트 기획 도중, 팀 취지에 맞지 않는 팀원이거나 문제가 있는 경우 팀원들과 상의 후, Leader는 해당 사용자를 강제퇴장 시킬 수 있음, 강제 퇴장당한 채팅방에는 다시 입장신청 할 수 없음.  

*Use-case Diagram  

![image](https://user-images.githubusercontent.com/65672220/187188123-e8d80bf0-c763-4893-b85b-63c282cdd575.png)   

*클래스 구성도  

![image](https://user-images.githubusercontent.com/65672220/187188187-f2230356-507f-4b71-882f-cf95ec1bd42c.png)  

*ER-diagram  

![image](https://user-images.githubusercontent.com/65672220/187188328-2053c5a8-c8a3-4115-b2c3-c2796c15715b.png)  

*프로그램 구동 화면  

![image](https://user-images.githubusercontent.com/65672220/187188435-61826b85-5ec2-49b4-bad0-a8b76075b87f.png)  
![image](https://user-images.githubusercontent.com/65672220/187188496-9f7f497e-69fe-4ac6-861e-5ff8038e51be.png)  
![image](https://user-images.githubusercontent.com/65672220/187188540-5f85d3d2-8f7b-4676-9f10-a0156768d0b2.png)  
![image](https://user-images.githubusercontent.com/65672220/187188589-107c1072-0715-46ee-9af6-fe3aa4d52f06.png)  
![image](https://user-images.githubusercontent.com/65672220/187188646-1a91fa11-27bc-45fb-8665-54dcdf518037.png)  
![image](https://user-images.githubusercontent.com/65672220/187188676-398d5ac6-38ea-4315-8673-895edd8f4683.png)  



