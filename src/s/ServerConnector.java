package s;

/*
 * sql injection 해킹에 대비해 모든 statement를 preparestatement 로 수정할 것!!!
 * 14.07.30 
 * 수정완료
 * 14.08.01
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/*
 * 서버와 mysql을 연결해주는 커넥터
 */
public class ServerConnector {
	
	private Data recv_data;
	private String sql_url = "localhost:3306/cafe";
	private Connection con;
	private Data reply_data = null;
	private int s_id;
	Statement stmt;
	
	
	ServerConnector(Data param , int ser_id) throws ClassNotFoundException, SQLException
	{
		if(param == null)
			return;
		recv_data = param;
		s_id = ser_id;
		start_process();
		
	}
	//만약에 serv_id가 없는 상태로 들어온다면 -1을 출력
	ServerConnector(Data param) throws ClassNotFoundException, SQLException
	{
		this(param,-1);
	}
	
	public Data reply()
	{
		return reply_data;
	}
	public void seturl(String temp)
	{
		sql_url = temp;
	}
	private void start_process() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://"+sql_url,"root","1234");
		System.out.println("server:" + s_id + " - " +"sql 서버와 연결되었습니다..");
		
		//querry 문을 주고 받고 하기 위한 stmt 구문.
		stmt = con.createStatement();
		System.out.println("server:" + s_id + " - 연결 목적:" +recv_data.purpose);
		reply_data = new Data("reply");
		System.out.println("server:"+ s_id + " - "+ recv_data.purpose +" 명령 확인");
		switch(recv_data.purpose)
		{
		case "JOIN":
			joinUser();
			break;
		case "LOGIN":
			loginUser();
			break;
		case "ID CHECK":
			idCheck();
			break;
		case "SHOW USER":
			showUser();
			break;
		case "MODIFY USER":
			modifyUser();
			break;
		case "DELETE USER":
			deleteUser();
			break;
		case "SHOW STAMP":
			showStamp();
			break;
		case "SHOW STAMPRANKING":
			showStampRanking();
			break;
		case "SHOW CATEGORY":
			showCateogory();
			break;
		case "SHOW MENU":
			showMenu();
			break;
		case "SHOW DETAIL":
			showDetailMenu();
			break;
		case "ORDER MENU":
			orderMenu();
			break;
		case "SHOW PAYMENT":
			showPayment();
			break;
		case "PAY ORDER":
			payOrder();
			break;
		case "ADD MENU":
			addMenu();
			break;
		case "MODIFY MENU":
			modifyMenu();
			break; 
		case "DELETE MENU":
			deleteMenu();
			break;
		case "ADD STAMP":
			addStamp();
			break;
		case "ADD BALANCE":
			addBalance();
			break;
		case "SHOW BALANCE":
			showBalance();
			break;
		case "MAKE COUPON":
			makeCoupon();
			break;
		case "GIFT STAMP":
			giftStamp();
			break;
		case "GIFT COUPON":
			giftCoupon();
			break;
		case "USE COUNPON":
			useCoupon();
			break;
		case "ADD EMPLOYEE":
			addEmployee();
			break;
		case "MODIFY EMPLOYEE":
			modfiyEmployee();
			break;
		case "DELETE EMPLOYEE":
			deleteEmployee();
			break;
		case "CHECK EMPLOYEE":
			loginEmployee();
			break;
		case "SHOW EMPLOYEE":
			showEmployee();
			break;
		case "SHOW NOTICE":
			showNotice();
			break;
		case "ADD NOTICE":
			addNotice();
			break;
		case "MODIFY NOTICE":
			modifyNotice();
			break;
		case "DELETE NOTICE":
			deleteNotice();
			break;
		case "REQUEST MUSIC":
			requestMusic();
			break;
		default:
			System.out.println("purpose error ___ " + recv_data.purpose);
			reply_data = new Data("ERROR");
			reply_data.addContent("error_code", "not exist purpose");
		}
	}
	// SQL ERROR 가 뭔지를 확인하는 세션
	protected boolean sqlErrorCheck (SQLException e)
	{
		if(e.getErrorCode() == 1062)
		{	
			System.out.println("server:" + s_id + " - " +"중복에러!!");
			//join의 경우 0일 경우만 있음
			reply_data.content.clear();
			reply_data.addContent("ERROR CODE", e.getErrorCode()+"");
			reply_data.addContent("error", "duplicate key");
			return true;
		}
		else
		{
			reply_data.content.clear();
			reply_data.addContent("ERROR CODE", e.getErrorCode()+"");
			reply_data.addContent("error", e.toString());
			System.out.println(e.toString());
			return false;
		}
		
	}
	//JOIN 일 경우 동작하는 부분.
	//0811 수정(생년월일추가)
	protected boolean joinUser()
	{
		int i=0;
		int u_num = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into user_info(user_id,password,user_num,name,sex,e_mail,birthday,phone) values (?,?,?,?,?,?,?,?)");
		//parameter 순서 1-id / 2-password / 3-user_number / 4-name / 5-sex / 6-e-mail
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"join 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 join일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) { 
				case "id":
					p_st.setString(1,temp.getValue());
					break;
				case "password":
					p_st.setString(2,temp.getValue());
					break;
				case "name":
					p_st.setString(4,temp.getValue());
					break;
				case "e_mail":
					p_st.setString(6,temp.getValue());
					break;
					//남자일 경우 sex는 0으로 표현
				case "sex":
					if(temp.getValue().equals("0"))
						p_st.setString(5,"0");
					else
						p_st.setString(5,"1");
					break;
				case "birthday":
					//2014-02-01 같은 형태로 전송해야함!
					p_st.setString(7, temp.getValue());
				case "phone":
					//010-0000-0000 같은 형태로 전송해야함!
					p_st.setString(8, temp.getValue());
				default:
					break;
				}
			}
		//column 에 있는 user num중 최대값을 가져와 그위에 +1을 해준다. (user 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(user_num) from user_info");
			if(rs.next())
			{
				u_num=rs.getInt(1);
			}
			p_st.setString(3, ++u_num +"");
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("JOIN", "OK");				
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				/*
				if(e.getErrorCode() == 1062)
				{
					System.out.println("server:" + s_id + " - " +"중복에러!!");
					//join의 경우 0일 경우만 있음
					reply_data.deleteContent(0);
					reply_data.addContent("error", "duplicate key");
				}
				*/
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}

	
	/*
	 * Login일 경우 동작하는 부분
	 * 개발일 : 14.08.07
	 * 개발자 : 김필기 
	 */
	protected boolean loginUser(){
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT user_id, password, user_num FROM user_info WHERE user_id = ? and password = ?");
		String id = null;
		//parameter 순서 1-id / 2-password / 3-user_number / 4-name / 5-sex / 6-e-mail
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"login 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "id":
					id = temp.getValue();
					p_st.setString(1,id);
					break;
				case "password":
					p_st.setString(2,temp.getValue());
					break;
				case "device id":
					PreparedStatement ps_dev = con.prepareStatement("update user_info set device_id = ? where id = ?");
					ps_dev.setString(1, temp.getValue());
					ps_dev.setString(2, id);
					ps_dev.executeQuery();
					break;
				default:
					throw new SQLException("temp.type error in login _" + temp.getType());
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("LOGIN", "OK");
				reply_data.addContent("user_num",rs.getString("user_num"));
				return true;
			}
			else
			{
				reply_data.addContent("LOGIN","FAIL");
				sql = new StringBuffer("SELECT * FROM user_info WHERE user_id = ?");
				p_st = con.prepareStatement(sql.toString());
				p_st.setString(1,id);
				rs = p_st.executeQuery();
				if(rs.next())
				{
					reply_data.addContent("ERROR CODE", "WORNG PASSWORD");
				}
				else
					reply_data.addContent("ERROR CODE", "ID does not exist");
				return false;
			}
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

	}

	/*
	 * id 중복확인
	 * 개발일 : 14.11.09
	 * 개발자 : 김필기
	 */
	protected boolean idCheck(){
		int i = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT user_id FROM user_info WHERE user_id = ?");
		String id = null;
		//parameter 순서 1-id
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"중복 id 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "id":
					id = temp.getValue();
					p_st.setString(1,id);
					break;
				default:
					throw new SQLException("content error");
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("ID CHECK", "DUPLICATE");
				return true;
			}
			else
			{
				reply_data.addContent("ID CHECK","OK");
				return true;
			}
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
	}
	/*
	 * member 목록를 불러올때 사용되는 부분
	 * 개발일 : 14.11.08 ~ 14.11.09
	 * 개발자 : 김필기
	 */
	protected boolean showUser()
	{
		int i=0;
		Data.data_structure temp = null ;
		StringBuffer sql = null;
		PreparedStatement p_st = null;
	
		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show member 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "all":
					sql = new StringBuffer("SELECT * from user_info");
					p_st = con.prepareStatement(sql.toString());
					break;
				case "number":
					sql = new StringBuffer("SELECT * from user_info where user_num = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
					//휴대폰 번호 추가되면 추가할부눈
				case "phone":
					sql = new StringBuffer("SELECT * from user_info where phone like ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1, '%'+ temp.getValue() );
					break;
				case "id":
					sql = new StringBuffer("SELECT * from user_info where id = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				case "name":
					sql = new StringBuffer("SELECT * from user_info where name = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				case "modify":
					sql = new StringBuffer("SELECT * from user_info where user_num = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new SQLException("type_error _" + temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			int count=0;
			reply_data.addContent(temp.getType(), "OK");
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_user_num", rs.getString("user_num"));
				reply_data.addContent(count +"_name", rs.getString("name"));
				reply_data.addContent(count +"_sex", rs.getString("sex"));
				reply_data.addContent(count +"_birthday", rs.getString("birthday"));					
				reply_data.addContent(count +"_e_mail", rs.getString("e_mail"));
				reply_data.addContent(count + "_phone",rs.getString("phone"));
				reply_data.addContent(count +"_balance", rs.getString("balance"));
				reply_data.addContent(count +"_register_date", rs.getString("register_date"));
				reply_data.addContent(count +"_user_id", rs.getString("user_id"));
				if(temp.getType().equals("all"))
					continue;
				reply_data.addContent(count +"_latest_login", rs.getString("latest_login"));
				if(temp.getType().equals("modify"))
					reply_data.addContent(count +"_password", rs.getString("password"));
				
			}
			reply_data.modifyContent(0, temp.getType(), count+"");
			} 
		catch (SQLException e) {
				reply_data.addContent("SHOW USER","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
		}
		return true;
		
	}

	/*
	 * 메뉴를 수정할때 실행되는 부분 ( 사진 수정은 다른 부분에서 )
	 * 개발일 : 14.09.07~
	 * 개발자 : 김필기
	 */
	protected boolean modifyUser()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update user_info set user_id = ? , name = ? , sex = ? ,"
				+ "birthday = ?, e_mail = ?, phone = ?, password = ? where user_num = ?");
		//1_user_id / 2-name / 3-sex / 4-birthday / 5-e_mail / 6-phone / 7-password / 8 - user_num
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"modfiy Menu ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "user_id":
					p_st.setString(1,temp.getValue());
					break;
				case "name":
					//category 내에서 순서를 조정할때 menu_num과 상관없이 정렬하기 위해서 category_order를 사용한다.
					p_st.setString(2,temp.getValue());
					break;
				case "sex":
					p_st.setString(3,temp.getValue());
					break;
				case "birthday":
					p_st.setString(4,temp.getValue());
					break;
				case "e_mail":				
					p_st.setString(5,temp.getValue());
					break;
				case "phone":
					p_st.setString(6, temp.getValue());
					break;
				case "password":
					p_st.setString(7, temp.getValue());
					break;
				case "user_num":
					p_st.setString(8,temp.getValue());
					break;
				default:
					throw new SQLException("invalid type!! in modify user__"+temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("MODIFY USER", "OK");						
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}

	/*
	 * 메뉴를 수정할때 실행되는 부분 ( 사진 수정은 다른 부분에서 )
	 * 개발일 : 14.09.07~
	 * 개발자 : 김필기
	 */
	protected boolean deleteUser()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("delete from user_info where user_num = ?");
		//1_user_num 
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"delete user ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "user_num":
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new SQLException("invalid type!! in delete user__"+temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("DELETE USER", "OK");						
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 개인의 stamp 정보를 불러올때 사용되는 부분
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기
	 */
	protected boolean showStamp()
	{
		int i=0;
		Data.data_structure temp = null ;
		StringBuffer sql = null;
		PreparedStatement p_st = null;
	
		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show stamp 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "user_num":
					sql = new StringBuffer("SELECT user_num, stamp_total,stamp_available,stamp_month from user_info where user_num = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				case "id":
					sql = new StringBuffer("SELECT user_num, stamp_total,stamp_available,stamp_month from user_info where user_id = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new SQLException ("type error");
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			reply_data.addContent(temp.getType(), "OK");

			if((temp.getType().equals("id") || temp.getType().equals("user_num")) && rs.next())
			{
				reply_data.addContent("stamp_available", rs.getString("stamp_available"));
				return true;
			}
			} 
		catch (SQLException e) {
				reply_data.addContent("SHOW STAMP","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
		}
		return false;
		
	}

	/*
	 * stamp ranking을 불러올때 쓰는 함수
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기
	 */
	protected boolean showStampRanking()
	{
		int i=0;
		Data.data_structure temp = null ;
		StringBuffer sql = null;
		StringBuffer sql2 = null;
		Statement st = null;

		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show stamp 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "num":
					//total 순위 구하기
					StringBuffer rank = new StringBuffer("select count(*) + 1 as rank from user_info where stamp_total > (Select stamp_total from user_info where user_num = ?)");
					PreparedStatement rank_sql = con.prepareStatement(rank.toString());
					PreparedStatement stamp_sql = con.prepareStatement("Select stamp_total from user_info where user_id = ?");
					stamp_sql.setString(1, temp.getValue());
					rank_sql.setString(1, temp.getValue());
					ResultSet rs2 = rank_sql.executeQuery();
					ResultSet rs3 = stamp_sql.executeQuery();
					if(rs2.next()&&rs3.next())
					{
						reply_data.addContent("rank_total",rs2.getString("rank"));
						reply_data.addContent("stamp_total",rs3.getString("stamp_total"));
					}
					else
						throw new SQLException("No result about rank total");
					//stamp_month 순위 구하기
					stamp_sql = con.prepareStatement("Select stamp_month from user_info where user_num = ?");
					stamp_sql.setString(1, temp.getValue());
					rs3 = stamp_sql.executeQuery();
					rank = new StringBuffer("select count(*) + 1 as rank from user_info where stamp_month > (Select stamp_month from user_info where user_num = ?)");
					rank_sql = con.prepareStatement(rank.toString());
					rank_sql.setString(1, temp.getValue());
					rs2 = rank_sql.executeQuery();
					if(rs2.next() && rs3.next())
					{
						reply_data.addContent("rank_month",rs2.getString("rank"));
						reply_data.addContent("stamp_month",rs3.getString("stamp_month"));
					}
					else
						throw new SQLException("No result about rank stamp_month");
					break;
				case "id":
					//total 순위 구하기
					stamp_sql = con.prepareStatement("Select stamp_total from user_info where user_id = ?");
					stamp_sql.setString(1, temp.getValue());
					rs3 = stamp_sql.executeQuery();
					rank = new StringBuffer("select count(*) + 1 as rank from user_info where stamp_total > (Select stamp_total from user_info where user_id = ?)");
					rank_sql = con.prepareStatement(rank.toString());
					rank_sql.setString(1, temp.getValue());
					rs2 = rank_sql.executeQuery();
					if(rs2.next()&&rs3.next())
					{
						reply_data.addContent("rank_total",rs2.getString("rank"));
						reply_data.addContent("stamp_total",rs3.getString("stamp_total"));
					}
					else
						throw new SQLException("No result about rank total");
					//stamp_month 순위 구하기
					stamp_sql = con.prepareStatement("Select stamp_month from user_info where user_id = ?");
					stamp_sql.setString(1, temp.getValue());
					rs3 = stamp_sql.executeQuery();
					rank = new StringBuffer("select count(*) + 1 as rank from user_info where stamp_month > (Select stamp_month from user_info where user_id = ?)");
					rank_sql = con.prepareStatement(rank.toString());
					rank_sql.setString(1, temp.getValue());
					rs2 = rank_sql.executeQuery();
					if(rs2.next() && rs3.next())
					{
						reply_data.addContent("rank_month",rs2.getString("rank"));
						reply_data.addContent("stamp_month",rs3.getString("stamp_month"));
					}
					else
						throw new SQLException("No result about rank stamp_month");
					break;
				case "total":
					sql = new StringBuffer("SELECT  user_num, user_id, stamp_total,IF "
							+ "(stamp_total=@_last_stamp,@curRank:=@curRank,@curRank:=@_sequence)"
							+ "AS rank, @_sequence:=@_sequence+1,@_last_stamp:=stamp_total FROM user_info,"
							+ "(SELECT @curRank := 1, @_sequence:=1, @_last_stamp:=0) r ORDER BY  stamp_total DESC LIMIT 10");
					break;
				case "month":
					sql2 = new StringBuffer("SELECT  user_num, user_id, stamp_month,IF "
							+ "(stamp_month=@_last_stamp,@curRank:=@curRank,@curRank:=@_sequence)"
							+ "AS rank, @_sequence:=@_sequence+1,@_last_stamp:=stamp_month FROM user_info,"
							+ "(SELECT @curRank := 1, @_sequence:=1, @_last_stamp:=0) r ORDER BY  stamp_month DESC LIMIT 10");
					break;
				default:
					throw new SQLException("invalid value_ 'total' or 'month' is valid");
				}
			}
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			int count=0;
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_user_id", rs.getString("user_id"));
				reply_data.addContent(count +"_stamp_total", rs.getString("stamp_total"));
			}
			rs = st.executeQuery(sql2.toString());
			count=0;
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_user_id", rs.getString("user_id"));
				reply_data.addContent(count +"_stamp_month", rs.getString("stamp_month"));
			}
		} 
		catch (SQLException e) {
			reply_data.addContent("SHOW STAMP","FAIL");
			//reply_data.addContent("ERROR CODE", e.toString());
			e.printStackTrace();
			sqlErrorCheck(e);
			return false;
		}
		return false;
	}
	/*
	 * category를 불러올때 사용되는 부분
	 * 개발일 : 14.11.11 
	 * 개발자 : 김필기
	 */
	protected boolean showCateogory()
	{	
		int count=0;
		StringBuffer sql = new StringBuffer("SELECT DISTINCT category FROM menu");
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"category 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			reply_data.addContent("category", "");
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_category", rs.getString("category"));
			
			}
			reply_data.modifyContent(0, "category", count+"");
			} catch (SQLException e) {
				if(reply_data.getContentSize() > 1)
					reply_data.modifyContent(0,"ERROR CODE", e.toString());
				else
					reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
			return true;
	}
	/*
	 * menu를 불러올때 사용되는 부분
	 * 개발일 : 14.08.14 
	 * 개발자 : 김필기
	 */
	protected boolean showMenu()
	{	
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT menu_num,menu_name,size,price FROM menu WHERE category = ?");
		//parameter 순서 1 - category 이름
		String category = null;
		PreparedStatement p_st = null;
	
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"menu 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "category":
					category = temp.getValue();
					p_st.setString(1,category);
					break;
				default:
					break;
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			int count=0;
			reply_data.addContent(category, "OK");
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_menu_name", rs.getString("menu_name"));
				reply_data.addContent(count +"_size", rs.getString("size"));
				reply_data.addContent(count +"_price", rs.getString("price"));
				reply_data.addContent(count +"_menu_num", rs.getString("menu_num"));

			}
			reply_data.modifyContent(0, category, count+"");
			} catch (SQLException e) {
				reply_data.addContent("DETAIL MENU","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
			return true;
	

	}
	
	/*
	 * 메뉴 상세정보 확인하기
	 * 개발일 : 14.08.14 ~ 14.08.14
	 * 개발자 : 김필기
	 */
	protected boolean showDetailMenu()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT menu_name,category,size,price,detail,image FROM menu WHERE menu_num = ?");
		String menu_num = null;
		//parameter 순서 1-id / 2-password / 3-user_number / 4-name / 5-sex / 6-e-mail
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"menu 자세히 확인하기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type = " + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "menu_num":
					menu_num = temp.getValue();
					p_st.setString(1,menu_num);
					break;
				default:
					break;
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("DETAIL", "OK");
				reply_data.addContent("menu_name", rs.getString("menu_name"));
				reply_data.addContent("category", rs.getString("category"));
				reply_data.addContent("size", rs.getString("size"));
				reply_data.addContent("price", rs.getString("price"));
				reply_data.addContent("detail", rs.getString("detail"));
				reply_data.addContent("image", rs.getString("image"));
				return true;
			}
			else
			{
				reply_data.addContent("DETAIL MENU","FAIL");
				reply_data.addContent("ERROR CODE", "menu num does not exist");
				return false;
			}
			} catch (SQLException e) {
				reply_data.addContent("DETAIL MENU","FAIL");
				reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

		
	}
	//주문할 경우 실행될 부분
	@SuppressWarnings("null")
	protected boolean orderMenu()
	{
		int i = 0 , order_num = 0, total_count = 0 , item_count=0 , total_price = 0;
		int same_menu = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into order_info(order_num, num_total_item, user_num, total_price, payment, table_name, etc) values (?,?,?,?,?,?,?,?)");
		//parameter 순서 1 - order_num / 2 - num_totatl_item / 3 - user_num / 4 - total_price
		// 5 - payment /  6 - table_name / 7 - etc
		StringBuffer sql2 = new StringBuffer("insert into order_list(order_num,item_num,menu_num,payment,payment_option,coupon_num,menu_name,size,price) values (?,?,?,?,?,?,?,?,?,?)");
		//parameter 순서 1 - order_num / 2 - item_num / 3 - menu_num / 4 - payment / 5 - payment_option  / 6 - coupon_num / 7 - menu_name / 8 - size / 9 - price
		String item_num = "1" , menu_num = null, table_name = null, user_num = null , temp_menu_name = null;
		PreparedStatement p_st = null , p_st2 = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			p_st = con.prepareStatement(sql2.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"order_Menu 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			
			//column 에 있는 menu num중 최대값을 가져와 그위에 +1을 해준다. (menu 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(order_num) from order_info");
			if(rs.next())
			{
				order_num=rs.getInt(1);
			}
			p_st.setString(1, ++order_num +"");
			
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "user_num":
					user_num = temp.getValue();
					p_st.setString(3,user_num);
					break;
				case "table_name":
					table_name = temp.getValue();
					p_st.setString(6,table_name);
					break;
				case "etc":
					p_st.setString(7,temp.getValue());
					break;
				case "total_count":
					total_count = Integer.valueOf(temp.getValue());
					p_st.setString(2,temp.getValue());
					break;
				default:
					//order_list 를 추가하기위해
					if(temp.getValue().startsWith(item_num))
					{
						item_count++;
						if(temp.getValue().contains("menu_num"))
						{
							String tempString;
							tempString = temp.getValue().substring(item_num.length()+1);
							menu_num = tempString;
							p_st2.setString(3,tempString);
						}
						else if(temp.getValue().contains("payment"))
						{
							String tempString;
							tempString = temp.getValue().substring(item_num.length()+1);
							p_st2.setString(4,tempString);
						}
						else if(temp.getValue().contains("payment_option"))
						{
							String tempString;
							tempString = temp.getValue().substring(item_num.length()+1);
							p_st2.setString(5,tempString);
						}
						else if(temp.getValue().contains("coupon_num"))
						{
							String tempString;
							tempString = temp.getValue().substring(item_num.length()+1);
							p_st2.setString(6,tempString);
						}
						
					}
					//주문 정보 4개가 다 들어왔을 경우에
					if(item_count>4)
					{
						if(Integer.parseInt(item_num)>total_count)
							throw new SQLException("error: item num is larger than total item number!!");
						item_count = 0;
						p_st2.setString(1,order_num+"");
						p_st2.setString(2,item_num);
						ResultSet rs1 = stmt.executeQuery("select menu_name, size , price from menu where menu_num = '"+ menu_num +"'");
						if(rs1.next())
						{
							p_st2.setString(7,rs.getString("menu_name"));
							p_st2.setString(8,rs.getString("size"));
							p_st2.setString(9,rs.getString("price"));
							total_price = total_price + Integer.parseInt(rs.getString("price"));
						}
						item_num = (Integer.parseInt(item_num) + 1) + "";
						//order list에 추가
						p_st2.executeQuery();
					}
					break;
				}
			}

			p_st.setString(4, total_price+"");
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("ORDER NUM", order_num+"");
			reply_data.addContent("ORDER COUNT", total_count+"");
			reply_data.addContent("table name", table_name);
			ResultSet rs2 = stmt.executeQuery("select name from user_num where user_num ='"+ user_num +"'");
			if(rs2.next())
				reply_data.addContent("name", rs2.getString("name"));
			//시간 받아오기
			rs2 = stmt.executeQuery("select order_time from order_info whrer order_num ='"+ order_num +"'");
			if(rs2.next())
				reply_data.addContent("order_time", rs2.getString("order_time"));
			/*
			 * while 문으로 주문받은 것들 reply에 넣을것.
			 */
			//메뉴번호로 오더해서 받아올 것.
			rs2 = stmt.executeQuery("select menu_name from order_list where order_num = '"+ order_num +"'");
			while(rs2.next())
			{
				if(temp_menu_name.equals(rs2.getString("menu_name")))
				{
					reply_data.deleteContent(reply_data.getContentSize());
					reply_data.addContent("menu_name",temp_menu_name);
					reply_data.addContent("menu_count",++same_menu+"");
				}
				else
				{
					same_menu = 1;
					temp_menu_name = rs2.getString("menu_name");
					reply_data.addContent("menu_name",temp_menu_name);
					reply_data.addContent("menu_count",same_menu+"");
				}
				
			}
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * MFC 처음 켰을때 주문 정보 요청용
	 * 개발일 : 14.11.15
	 * 개발자 : 김필기
	 */
	protected boolean showOrder()
	{
		int i = 0 ;
		int same_menu = 0;
		Data.data_structure temp ;
		PreparedStatement p_st = null;
		try {
			System.out.println("server:" + s_id + " - " +"show order 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			p_st = con.prepareStatement("SELECT * FROM order_info where payment = 0 and complete = ?");
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type = " + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "complete":
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new SQLException("invalid type!! in show order__"+temp.getType());
				}
			}
			ResultSet rs = p_st.executeQuery();
			while(rs.next())
			{
				reply_data.addContent("ORDER COUNT", rs.getString("num_total_item"));
				reply_data.addContent("table name", rs.getString("table_name"));
				String user_num = rs.getString("user_num");
				String order_num = rs.getString("order_num");
				String temp_menu_name = null;
				ResultSet rs2 = stmt.executeQuery("select name from user_num where user_num ='"+ user_num +"'");
				if(rs2.next())
					reply_data.addContent("name", rs2.getString("name"));
				//시간 받아오기
				rs2 = stmt.executeQuery("select order_time from order_info whrer order_num ='"+ order_num +"'");
				if(rs2.next())
					reply_data.addContent("order_time", rs2.getString("order_time"));
				/*
				 * while 문으로 주문받은 것들 reply에 넣을것.
				 */
				//메뉴번호로 오더해서 받아올 것.
				rs2 = stmt.executeQuery("select menu_name from order_list where order_num = '"+ order_num +"'");
				while(rs2.next())
				{
					if(temp_menu_name.equals(rs2.getString("menu_name")))
					{
						reply_data.deleteContent(reply_data.getContentSize());
						reply_data.addContent("menu_name",temp_menu_name);
						reply_data.addContent("menu_count",++same_menu+"");
					}
					else
					{
						same_menu = 1;
						temp_menu_name = rs2.getString("menu_name");
						reply_data.addContent("menu_name",temp_menu_name);
						reply_data.addContent("menu_count",same_menu+"");
					}
					
				}

				
			}
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 결제 해야 하는 금액 , 결제 된 금액 표시
	 * 개발일 : 14.11.15
	 * 개발자 : 김필기
	 */
	protected boolean showPayment()
	{
		int i=0;
		String order_num = "";
		Data.data_structure temp ;
		PreparedStatement p_st = null;
		PreparedStatement p_st2 = null;
		try {
			p_st = con.prepareStatement("select sum(price) from order_list where order_num = ? and payment = '0'");
			p_st2 = con.prepareStatement("select sum(price) from order_list where order_num = ? and payment = '1'");
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"payMenu ㄱㄱㄱ size:"+recv_data.content.size());
			
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "order_num":
					order_num = temp.getValue();
					p_st.setString(1,order_num);
					p_st2.setString(1, order_num);
					break;
				default :
					throw new SQLException("invalid type!! in show payment__"+temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			ResultSet rs2 = p_st2.executeQuery();
			if(rs.next() && rs2.next())
			{
				reply_data.addContent("deferred payment",rs.getString("price"));
				reply_data.addContent("prepaid ",rs.getString("price"));
			}
			else
				throw new SQLException("No ordered information!");
			
			return true;
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 결제 한 주문정보 수정하기
	 * 개발일 : 14.11.15
	 * 개발자 : 김필기
	 */
	protected boolean payOrder()
	{
		int i=0;
		int price = 0, ordered_price = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update order_list set payment = ? , payment_option = ?  where order_num = ?");
		PreparedStatement p_st = null;
		PreparedStatement p_st2 = null;
		String order_num = "";
		try {
			p_st = con.prepareStatement(sql.toString());
			p_st2 = con.prepareStatement("SELECT SUM(price) FROM odre_list WHERE order_num = ?");
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"payMenu ㄱㄱㄱ size:"+recv_data.content.size());
			
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "order_num":
					order_num = temp.getValue();
					p_st.setString(2,order_num);
					p_st2.setString(1, order_num);
					break;
				case "price":
					price = Integer.parseInt(temp.getValue());
					break;
				default :
					throw new SQLException("invalid type!! in pay Order__"+temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st2.executeQuery();
			if(rs.next())
			{
				ordered_price=Integer.parseInt(rs.getString("price"));
			}
			if(ordered_price != price)
				throw new SQLException("No ordered information!");
			p_st2 = con.prepareStatement("update order_info set payment = 1 where order_num = ?");
			p_st.setString(1,"1");
			p_st.setString(2,"직접결제");
			if(p_st.execute())
			{
				p_st2.setString(1, order_num);
				//결제 완료로 order_info 수정
				p_st2.execute();
				//stamp 수정
				p_st2 = con.prepareStatement("select user_num from order_info where order_num = ?");
				p_st2.setString(1, order_num);
				rs = p_st2.executeQuery();
				if(!(rs.next()))
					throw new SQLException("user num을 못가져왔습니다");
				p_st2 = con.prepareStatement("select user_num from order_info where order_num = ?");
				String user_num = rs.getString("user_num");
				
				//가져온 user_num 으로 total count를 구하고 stamp를 total count 만큼 증가시켜준다.
				Statement stmt = con.createStatement();
				rs = stmt.executeQuery("select total_count from order_info where user_num = '" + user_num + "'");
				if(!(rs.next()))
					throw new SQLException("total_count를 못가져왔습니다");
				int total_count = rs.getInt("total_count");
				stmt.execute("update user_info set stamp_total=stamp_total+"+total_count
						+ " ,stamp_available=stamp_available+"+total_count+",stamp_month=stamp_month+"+total_count
						+" where user_num = '"+user_num+"'");
			}
			
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 메뉴를 추가할 때 실행되는 부분
	 * 개발일 : 14.08.07 ~
	 * 개발자 : 김필기 
	 */
	protected boolean addMenu()
	{
		int i=0;
		int 	menu_num = 0;
		String img_dir = "/image/";
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into menu(menu_name, category, price, menu_num, detail, thumbnail,  image , category_order,size) values (?,?,?,?,?,?,?,?,?)");
		//parameter 순서 1-menuname / 2-category / 3-price / 4-menu_num / 5-detail / 6-thumbnail / 7-image / 8-category_odrer
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"add Menu 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 add menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "menu_name":
					p_st.setString(1,temp.getValue());
					break;
				case "category":
					//category 내에서 순서를 조정할때 menu_num과 상관없이 정렬하기 위해서 category_order를 사용한다.
					p_st.setString(2,temp.getValue());
					ResultSet rs = stmt.executeQuery("select max(category_order) from menu where category = '"+temp.getValue()+"'");
					int cat_num=0;
					if(rs.next())
					{
						cat_num=rs.getInt(1);
						p_st.setString(8,++cat_num+"");
					}
					break;
				case "price":
					p_st.setString(3,temp.getValue());
					break;
				case "detail":
					p_st.setString(5,temp.getValue());
					break;
				case "size":
					p_st.setString(9,temp.getValue());
					break;
				default:
					throw new SQLException("invalid type!! in add menu__"+temp.getType());
				}
			}
		//column 에 있는 menu num중 최대값을 가져와 그위에 +1을 해준다. (menu 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(menu_num) from menu");
			if(rs.next())
			{
				menu_num=rs.getInt(1);
			}
			p_st.setString(4, ++menu_num +"");
			p_st.setString(6,img_dir+"thum_"+menu_num + ".jpg");
			p_st.setString(7,img_dir+menu_num+ ".jpg");
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("ADD MENU", "OK");
			reply_data.addContent("MENU NUM", menu_num+"");								
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 메뉴를 수정할때 실행되는 부분 ( 사진 수정은 다른 부분에서 )
	 * 개발일 : 14.09.07~
	 * 개발자 : 김필기
	 */
	protected boolean modifyMenu()
	{
		int i=0;
		int 	menu_num = 0, category_num = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update menu set menu_name = ? , category = ? , price = ? ,"
				+ "detail = ?, category_order = ?, size = ? where menu_num = ?");
		//parameter 순서 1-menuname / 2-category / 3-price /  4-detail / 5-category_odrer / 6-size / 7-menu_num
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"modfiy Menu ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "menu_name":
					p_st.setString(1,temp.getValue());
					break;
				case "category":
					//category 내에서 순서를 조정할때 menu_num과 상관없이 정렬하기 위해서 category_order를 사용한다.
					p_st.setString(2,temp.getValue());
					category_num = Integer.parseInt(temp.getValue());
					break;
				case "price":
					p_st.setString(3,temp.getValue());
					break;
				case "detail":
					p_st.setString(4,temp.getValue());
					break;
				case "size":				
					p_st.setString(6,temp.getValue());
					break;
				case "menu_num":
					menu_num = Integer.parseInt(temp.getValue());
					p_st.setString(7, temp.getValue());
					break;
				//category order가 0인 경우는 카테고리를 바꾸면서 새로운 카테고리로 들어갈 경우
				case "category_odrer":
					if(temp.getValue().equals("0") && category_num != 0)
					{
						ResultSet rs = stmt.executeQuery("select max(category_order) from menu where category = '"+temp.getValue()+"'");
						int cat_num=0;
						if(rs.next())
						{
							cat_num=rs.getInt(1);
							p_st.setString(5,++cat_num+"");
						}
					}
				//0이 아닌 경우는 사용자가 category order를 정해줬을 경우
					else
						p_st.setString(5, temp.getValue());
					break;
				default:
					throw new SQLException("invalid type!! in modify menu__"+temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("MODIFY MENU", "OK");						
			reply_data.addContent("menu_num" , menu_num +"");
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 메뉴 넘버를 받으면 그 넘버를 카테고리 0으로 이동 (휴지통 같은 개념)
	 * 개발일 : 14.10.03
	 * 개발자 : 김필기
	 * 
	 */
	protected boolean deleteMenu()
	{
		int i=0;
		int 	menu_num = 0,category_order = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update menu set category = ? category_order = ? where menu_num = ?");
		//parameter 순서 1- category / 2 - category_order / 3 - munu_num
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"deleteMenu size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "menu_num":
					menu_num = Integer.parseInt(temp.getValue());
					p_st.setString(3, temp.getValue());
					p_st.setString(1, "0");
					break;
				default:
					break;
				}
			}
			//column 에 있는 menu num중 최대값을 가져와 그위에 +1을 해준다. (menu 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(category_order) from menu where menu_num =" + menu_num);
			if(rs.next())
			{
				category_order=rs.getInt(1);
			}
			p_st.setString(2, ++category_order +"");
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("DELETE MENU", "OK");						
			reply_data.addContent("menu_num" , menu_num +"");
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}

	/*
	 * user_id 나 user_num을 주면 stamp 1개 추가!
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기
	 */
	protected boolean addStamp()
	{
		int i=0;
		String total = null,available = null,month = null,user_num = null;
		Data.data_structure temp ;
		StringBuffer sql = null;
		//parameter 순서 1-menu_num
		PreparedStatement p_st = null;
		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"add Stamp 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 add menu 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "id":
					sql = new StringBuffer("select user_num,stamp_total, stamp_available, stamp_month from user_info where user_id = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				case "user_num":
					sql = new StringBuffer("select user_num,stamp_total, stamp_available, stamp_month from user_info where user_num = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new Exception("content type error!");
				}
			}
			//id로 찾은 user의 stamp 갯수를 1개씩 올려준다.
			ResultSet rs = p_st.executeQuery();
			
			if(rs.next())
			{
				total = (Integer.parseInt(rs.getString("stamp_total")) + 1)+"";
				available = (Integer.parseInt(rs.getString("stamp_available")) + 1)+"";
				month = (Integer.parseInt(rs.getString("stamp_month")) + 1)+"";
				user_num = rs.getString("user_num");
			}
			else
				throw new Exception("존재하지 않는 사용자입니다.");
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("ADD STAMP", "OK");
			sql = new StringBuffer("update user_info SET stamp_total = ? , stamp_available = ? , stamp_month = ? where user_num = ?");
			p_st = con.prepareStatement(sql.toString());
			p_st.setString(1, total);
			p_st.setString(2, available);
			p_st.setString(3, month);
			p_st.setString(4, user_num);
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		return false;
			
	}

	protected boolean addBalance()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update user_info set balance = balance + ? WHERE user_num = ?");
		int balance = 0;
		//parameter 순서 1 -추가된 blance / 1-user_num
		PreparedStatement p_st = null;
		PreparedStatement p_st2 = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			p_st2 = con.prepareStatement("insert into balance_log(user_num,increase,balacne,employee_num,employee_name)"
					+ " values (?,?,?,?,?)");
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show Balance size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "user_num":
					p_st.setString(2,temp.getValue());
					p_st2.setString(1,temp.getValue());
					PreparedStatement p_st3 = con.prepareStatement("select balance from user_info where user_num = ?");
					p_st3.setString(1, temp.getValue());
					ResultSet r_s = p_st3.executeQuery();
					if(r_s.next())
					{
						balance = r_s.getInt(1);
					}
					break;
				case "increase":
					p_st2.setString(2,temp.getValue());
					System.out.println(temp.getValue());
					balance = balance + Integer.parseInt(temp.getValue());
					p_st2.setString(3,balance+"");
					p_st.setString(1,balance+"");
					break;
				case "employee_num":
					p_st2.setString(4,temp.getValue());
					p_st3 = con.prepareStatement("select name from employee where employee_num = ?");
					p_st3.setString(1, temp.getValue());
					r_s = p_st3.executeQuery();
					if(r_s.next())
						p_st2.setString(5,r_s.getString("name"));
					break;
				default:
					break;
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			p_st.execute();
			p_st2.execute();
			return true;
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
	}
	protected boolean showBalance()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT balance from user_info WHERE user_num = ?");
		//parameter 순서 1-user_num
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show Balance size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "user_num":
					p_st.setString(1, temp.getValue());
					ResultSet rs = p_st.executeQuery();
					if(rs.next())
					{
						reply_data.addContent("balance", rs.getString("balance"));
					}
					break;
				case "all":
					p_st = con.prepareStatement("SELECT user_num, user_id , name, balance from user_info");
					rs = p_st.executeQuery();
					int count=0;
					reply_data.addContent(temp.getType(), "OK");
					while(rs.next())
					{
						count++;
						reply_data.addContent(count +"_user_num", rs.getString("user_num"));
						reply_data.addContent(count +"_user_id", rs.getString("user_id"));
						reply_data.addContent(count +"_name", rs.getString("name"));
						reply_data.addContent(count +"_balance", rs.getString("balance"));
					}
					reply_data.modifyContent(0,temp.getType(),count+"" );
					break;
				default:
					throw new SQLException("type_error _" + temp.getType());
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
		return false;
	}
	protected boolean showBalanceLog()
	{
		int i=0;
		Data.data_structure temp ;
		//parameter 순서 1-user_num
		PreparedStatement p_st = null;

		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show Balance size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "user_num":
					p_st = con.prepareStatement("SELECT * FROM balance_log where user_num = ?");
					p_st.setString(1, temp.getValue());
					break;
				case "employee_num":
					p_st = con.prepareStatement("SELECT * FROM balance_log where employee_num = ?");
					p_st.setString(1, temp.getValue());
					break;
				case "all":
					p_st = con.prepareStatement("SELECT user_num, user_id , name, balance from user_info");
					break;
				default:
					throw new SQLException("type_error _" + temp.getType());
				}
				int count=0;
				reply_data.addContent(temp.getType(), "OK");
				ResultSet rs = p_st.executeQuery();
				while(rs.next())
				{
					count++;
					reply_data.addContent(count +"_user_num", rs.getString("user_num"));
					reply_data.addContent(count +"_user_id", rs.getString("user_id"));
					reply_data.addContent(count +"_name", rs.getString("name"));
					reply_data.addContent(count +"_balance", rs.getString("balance"));
				}
				reply_data.modifyContent(0,temp.getType(),count+"" );
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
		return false;
	}
	protected boolean makeCoupon()
	{
		return false;
	}
	protected boolean giftStamp()
	{
		return false;
	}
	protected boolean giftCoupon()
	{
		return false;
	}
	protected boolean useCoupon()
	{
		return false;
	}

	/*
	 * 알바생을 추가 할때 실행되는 부분 (test 미완료)
	 * 개발일 : 14.09.27 ~ 14.09.27
	 * 개발자 : 김필기
	 */
	protected boolean addEmployee()
	{

		int i=0;
		int u_num = 0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into employee(employee_num,name,phone) values (?,?,?)");
		//parameter 순서 1-id / 2-password / 3-employee_number / 4-name / 5-phone
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"addEmployee 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 join일 경우 실행될 부분
				temp = recv_data.getContent(i++);				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) { 
				case "name":
					p_st.setString(2,temp.getValue());
					break;
				case "phone":
					p_st.setString(3,temp.getValue());
					break;
				default:
					break;
				}
			}
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateformat = new SimpleDateFormat("yyMMdd");
		//column 에 있는 employee num중 최대값을 가져와 그위에 +1을 해준다. (user 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select Count(*) from employee");
			if(rs.next())
			{
				u_num=rs.getInt(1);
				String employee_num = dateformat.format(calendar.getTime()).toString() + String.format("%03d", (++u_num)%1000);
				p_st.setString(1, employee_num);
			}
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("addEmployee", "OK");				
			return p_st.execute();
			
			} catch (SQLException e) {
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 알바생을 수정 할때 실행되는 부분 (test 미완료)
	 * 개발일 : 14.09.27 ~ 14.09.27
	 * 개발자 : 김필기
	 */
	protected boolean modfiyEmployee()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update employee set name = ?, phone = ? where employee_num = ?");
		
		//parameter 순서 1-id / 2-password / 3-name / 4-phone / 5-employee_num
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"modifyEmployee 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			ResultSet rs;
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 join일 경우 실행될 부분
				temp = recv_data.getContent(i++);				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) { 
				case "name":
					p_st.setString(1,temp.getValue());
					break;
				case "phone":
					p_st.setString(2,temp.getValue());
					break;
				//수정 안하는 부분은 기본 data로 채운다.
				case "employee_num":
					p_st.setString(5,temp.getValue());
					break;
				default:
					break;
				}
			}
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("modifyEmployee", "OK");				
			return p_st.execute();
			
			} catch (SQLException e) {
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}	
	}
    /*
     * 삭제보다 숨기기 정도가 좋지 않을까?
     * 
     */
	protected boolean deleteEmployee()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("delete FROM employee WHERE employee_num = ?");
		String id = null;
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"employee_login 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "employee_num":
					p_st.setString(1,id);
					break;
				default:
					throw new SQLException("emplyee_num 만 type으로 보낼 수 있습니다.");					
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("employee_DELETE", "OK");
				return true;
			}
			else
			{
				reply_data.addContent("employee_DELETE","FAIL");
				return false;
			}
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

	}
	/*
	 * employee_Login일 경우 동작하는 부분  (test 미완료)
	 * 개발일 : 14.09.27
	 * 개발자 : 김필기 
	 */
	protected boolean loginEmployee(){
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT * FROM employee WHERE employee_num = ?");
		String id = null;
		PreparedStatement p_st = null;

		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"employee_login 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "employee_num":
					p_st.setString(1,temp.getValue());
					break;
				default:
					throw new SQLException("emplyee_num 만 type으로 보낼 수 있습니다.");					
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("employee_LOGIN", "OK");
				return true;
			}
			else
			{
				reply_data.addContent("LOGIN","FAIL");
				return false;
			}
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

	}

	/*
	 * member 목록를 불러올때 사용되는 부분
	 * 개발일 : 14.11.08 ~ 14.11.09
	 * 개발자 : 김필기
	 */
	protected boolean showEmployee()
	{
		PreparedStatement p_st = null;
	
		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show Employee 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			p_st = con.prepareStatement("select * from employee");
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			int count=0;
			reply_data.addContent("show employee", "OK");
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_employee_num", rs.getString("employee_num"));
				reply_data.addContent(count +"_name", rs.getString("name"));
				reply_data.addContent(count + "_phone",rs.getString("phone"));
			}
			reply_data.modifyContent(0, "show employee", count+"");
			} 
		catch (SQLException e) {
				reply_data.addContent("SHOW employee","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
		}
		return true;
		
	}
	
	protected boolean showNotice()
	{
		int i = 0;
		PreparedStatement p_st = null;
		Data.data_structure temp = null ;
		try {
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"show notice 보여주기 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 LOGIN 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
				switch (temp.getType()) {
				case "notice_num":
					p_st = con.prepareStatement("select * from notice where num = ?");
					p_st.setString(1,temp.getValue());
					break;
				case "all":
					p_st = con.prepareStatement("select * from notice");
					break;
				default:
					throw new SQLException("notice num_만 type으로 보낼 수 있습니다.");					
				}
			}
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			int count=0;
			reply_data.addContent("SHOW NOTICE", "");
			while(rs.next())
			{
				count++;
				reply_data.addContent(count +"_num", rs.getString("num"));
				if(rs.getString("writing_type").equals("0"))
					reply_data.addContent(count +"_writing_type", "공지사항");
				else if(rs.getString("writing_type").equals("1"))
					reply_data.addContent(count +"_writing_type", "이벤트");
				reply_data.addContent(count +"_title", rs.getString("title"));
				reply_data.addContent(count +"_image", rs.getString("image"));					
				reply_data.addContent(count +"_content", rs.getString("content"));
				reply_data.addContent(count + "_write_date",rs.getString("write_date"));
			}
			reply_data.modifyContent(0, "SHOW NOTICE", count+"");
			if(temp.getType().equals(("notice_num")))
				reply_data.modifyContent(0, "SHOW NOTICE", "OK");
				
			} 
		catch (SQLException e) {
				reply_data.addContent("SHOW NOTICE","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
		}
		return true;
		
	}
	/*
	 * 공지사항을 추가할 때 실행되는 부분
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기 
	 */
	protected boolean addNotice()
	{
		int i=0;
		int 	notice_num = 0;
		String img_dir = "/notice/";
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into notice(num, writing_type, title, image, content) values (?, ?, ?, ?, ?)");
		//parameter 순서 1-notice_num / 2 - writing_type / 3 - title / 4 - image / 5 - content
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"add notice 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 add notice 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "writing_type":
					p_st.setString(2,temp.getValue());
					break;
				case "title":
					p_st.setString(3,temp.getValue());
					break;
				case "content":
					p_st.setString(5,temp.getValue());
					break;
				default:
					throw new SQLException("Error type_"+temp.getType());
				}
			}
		//column 에 있는 notice num중 최대값을 가져와 그위에 +1을 해준다. (notice 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(num) from notice");
			if(rs.next())
			{
				notice_num=rs.getInt(1);
			}
			p_st.setString(1, ++notice_num +"");
			p_st.setString(4,img_dir+notice_num+ ".jpg");
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("ADD NOTICE", "OK");
			reply_data.addContent("NOTICE NUM", notice_num+"");								
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 메뉴를 수정할때 실행되는 부분 ( 사진 수정은 다른 부분에서 )
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기
	 */
	protected boolean modifyNotice()
	{
		int i=0;
		String	notice_num = null;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update notice set writing_type = ? , title = ? , image = ? ,"
				+ "content = ? where _num = ?");
		//parameter 순서 1- writing  type  / 2-title
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"modfiy notice ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 mdofiy notice 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "writing_type":
					p_st.setString(1,temp.getValue());
					break;
				case "title":
					p_st.setString(2,temp.getValue());
					break;
				case "content":
					p_st.setString(3,temp.getValue());
					break;
				case "num":
					notice_num=temp.getValue();
					p_st.setString(4, temp.getValue());
					break;
				default:
					break;
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("MODIFY notice", "OK");						
			reply_data.addContent("notice_num" , notice_num +"");
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
	/*
	 * 공지사항 지우는 곳
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기
	 * 
	 */
	protected boolean deleteNotice()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("delete from notice where num = ?");
		//parameter 순서 1- num
		PreparedStatement p_st = null;
		String notice_num = null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"deletenotice size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				temp = recv_data.getContent(i++);
				
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "notice_num":
					notice_num = temp.getValue();
					p_st.setString(1, temp.getValue());
					break;
				default:
					throw new SQLException("wrong type_"+temp.getType());
				}
			}
			//column 에 있는 notice num중 최대값을 가져와 그위에 +1을 해준다. (notice 번호의 중복을 막기 위해서)
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("DELETE notice", "OK");						
			reply_data.addContent("notice_num" , notice_num +"");
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}


	/*
	 * 음악을 신청할 때 실행되는 부분
	 * 개발일 : 14.11.10
	 * 개발자 : 김필기 
	 */
	protected boolean requestMusic()
	{
		int i=0;
		Data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into music(user_num, message) values (?, ?)");
		PreparedStatement p_st = null;
		String user_num=null;
		try {
			p_st = con.prepareStatement(sql.toString());
			//정상독작인지 test 하는 부분
			System.out.println("server:" + s_id + " - " +"request music 확인 ㄱㄱㄱ size:"+recv_data.content.size());
			while(recv_data.getContent(i)!=null)
			{
				//gettype으로 가져온 자료가 add notice 일 경우 실행될 부분
				temp = recv_data.getContent(i++);
				//test로 들어오는 data 확인하는 부분
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());

				switch (temp.getType()) {
				case "user_num":
					user_num = temp.getValue();
					p_st.setString(1,temp.getValue());
					break;
				case "message":
					p_st.setString(2,temp.getValue());
					break;
				default:
					throw new SQLException("Error type_"+temp.getType());
				}
			}
		//column 에 있는 notice num중 최대값을 가져와 그위에 +1을 해준다. (notice 번호의 중복을 막기 위해서)
	
			System.out.println("server:" + s_id + " - " +p_st.toString());
			reply_data.addContent("REQUEST MUSIC", "OK");
			PreparedStatement p_st2 = con.prepareStatement("select name from user_info where user_num = ?");
			p_st2.setString(1, user_num);
			ResultSet rs = p_st2.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("user_name", rs.getString("name"));
			}
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(sqlErrorCheck(e))
					return true;
				else
					return false;
			}
	}
}
