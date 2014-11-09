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


/*
 * 서버와 mysql을 연결해주는 커넥터
 */
public class server_connector {
	
	private data recv_data;
	private String sql_url = "localhost:3306/cafe";
	private Connection con;
	private data reply_data = null;
	private int s_id;
	Statement stmt;
	
	
	server_connector(data param , int ser_id) throws ClassNotFoundException, SQLException
	{
		if(param == null)
			return;
		recv_data = param;
		s_id = ser_id;
		start_process();
		
	}
	//만약에 serv_id가 없는 상태로 들어온다면 -1을 출력
	server_connector(data param) throws ClassNotFoundException, SQLException
	{
		this(param,-1);
	}
	
	public data reply()
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
		reply_data = new data("reply");
		if(recv_data.purpose.equals("JOIN"))
		{
			System.out.println("server:" + s_id + " - " +" if 문에서 join 명령 확인");
			//joinuser가 false면 query 전달 부분에 문제가 있는것 -> 관련된 내용을 client로 보내줄 것.
			joinUser();
		}
		else if (recv_data.purpose.equals("LOGIN"))
		{
			System.out.println("server:"+ s_id + " - login 명령 확인");
			loginUser();
		}
		else if (recv_data.purpose.equals("SHOW USER"))
		{

			System.out.println("server:"+ s_id + " - show user 명령 확인");
			showUser();
		}
		else if (recv_data.purpose.equals("ADD MENU"))
		{
			System.out.println("server:" + s_id + " - add menu 명령 확인");
			addMenu();
		}
		else if (recv_data.purpose.equals("SHOW DETAIL"))
		{
			System.out.println("server:" + s_id + " - show detail 명령 확인");
			showDetailMenu();

		}
		else if (recv_data.purpose.equals("SHOW MENU"))
		{
			System.out.println("server:" + s_id + " - show menu 명령 확인");
			showMenu();

		}
		else if (recv_data.purpose.equals("ID CHECK"))
		{
			System.out.println("server:" + s_id + " - id check 명령 확인");
			idCheck();

		}
		else if (recv_data.purpose.equals("ADD ORDER"))
		{
			System.out.println("server:" + s_id + " - add order 명령 확인");
			orderMenu();

		}
		else if (recv_data.purpose.equals("SHOW CATEGORY"))
		{
			System.out.println("server:" + s_id + " - show category 명령 확인");
			showCateogory();

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
		data.data_structure temp ;
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
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT user_id, password FROM user_info WHERE user_id = ? and password = ?");
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
				default:
					break;
				}
			}
			//Query 구문을 날려 result가 도착한다면 존재하는 아이디/패스워드 이다.
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("LOGIN", "OK");
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
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT user_id,FROM user_info WHERE user_id = ?");
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
					throw new Exception("content error");
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
			} catch (Exception e)
			{
					e.printStackTrace();
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
		data.data_structure temp = null ;
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
				default:
					break;
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
				if(temp.getType().equals("all"))
					continue;
				reply_data.addContent(count +"_user_id", rs.getString("user_id"));
				reply_data.addContent(count +"_stamp_total", rs.getString("stamp_total"));
				reply_data.addContent(count +"_stamp_available", rs.getString("stamp_available"));
				reply_data.addContent(count +"_stamp_month", rs.getString("stamp_month"));
				reply_data.addContent(count +"_latest_login", rs.getString("latest_login"));
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
	 * member 목록를 불러올때 사용되는 부분
	 * 개발일 : 14.11.08 ~ 14.11.09
	 * 개발자 : 김필기
	 */
	protected boolean showStamp()
	{
		int i=0;
		data.data_structure temp = null ;
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
					sql = new StringBuffer("SELECT stamp_total,stamp_available,stamp_month from user_info where user_num = ?");
					p_st = con.prepareStatement(sql.toString());
					break;
				case "id":
					sql = new StringBuffer("SELECT stamp_total,stamp_available,stamp_month from user_info where user_id = ?");
					p_st = con.prepareStatement(sql.toString());
					p_st.setString(1,temp.getValue());
					break;
				default:
					break;
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			int count=0;
			reply_data.addContent(temp.getType(), "OK");

			if(temp.getType().equals("id") || temp.getType().equals("user_num") && rs.next())
			{
				reply_data.addContent("stamp_total", rs.getString("statmp_total"));
				reply_data.addContent("stamp_available", rs.getString("statmp_available"));
				reply_data.addContent("stamp_month", rs.getString("statmp_month"));
			}
			//ranking 목록 불러올때 사용할 부분
			else
			{
				while(rs.next())
				{
					count++;
	
				}
				reply_data.modifyContent(0, temp.getType(), count+"");
				
			}
			} 
		catch (SQLException e) {
				reply_data.addContent("SHOW STAMP","FAIL");
				//reply_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
		}
		return true;
		
	}
	/*
	 * category를 불러올때 사용되는 부분
	 * 개발일 : 14.08.14 
	 * 개발자 : 김필기
	 */
	protected boolean showCateogory()
	{	
		int count=0;
		data.data_structure temp ;
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
		data.data_structure temp ;
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
		data.data_structure temp ;
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
		data.data_structure temp ;
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
						if(Integer.getInteger(item_num)>total_count)
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
							total_price = total_price + Integer.getInteger(rs.getString("price"));
						}
						item_num = (Integer.getInteger(item_num) + 1) + "";
						//order list에 추가
						p_st2.executeQuery();
					}
					break;
				}
			}

			p_st.setString(4, total_price+"");
			System.out.println("server:" + s_id + " - " +p_st.toString());
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
	 * 메뉴를 추가할 때 실행되는 부분
	 * 개발일 : 14.08.07 ~
	 * 개발자 : 김필기 
	 */
	protected boolean addMenu()
	{
		int i=0;
		int 	menu_num = 0;
		String img_dir = "/image/";
		data.data_structure temp ;
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
					break;
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
		data.data_structure temp ;
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
					category_num = Integer.getInteger(temp.getValue());
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
					menu_num = Integer.getInteger(temp.getValue());
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
					break;
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
		data.data_structure temp ;
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
					menu_num = Integer.getInteger(temp.getValue());
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
			p_st.setString(4, ++category_order +"");
			
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
	protected boolean addStamp()
	{
		return false;
	}

	protected boolean addBalance()
	{
		//balnace log 확인
		return false;
		//249-1110
		//281-2223
	}
	protected boolean showBalance()
	{
		int i=0;
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT balance WHERE usre_num = ?");
		String id = null;
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
					p_st.setString(1,temp.getValue());
					break;
				default:
					break;
				}
			}
			
			System.out.println("server:" + s_id + " - " +p_st.toString());
			ResultSet rs = p_st.executeQuery();
			if(rs.next())
			{
				reply_data.addContent("balance", rs.getString("balance"));
				return true;

			}
			else
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}
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
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into employee(employee_id,employee_password,employee_num,name,phone) values (?,?,?,?,?)");
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
				case "id":
					p_st.setString(1,temp.getValue());
					break;
				case "password":
					p_st.setString(2,temp.getValue());
					break;
				case "name":
					p_st.setString(4,temp.getValue());
					break;
				case "phone":
					p_st.setString(6,temp.getValue());
					break;
				default:
					break;
				}
			}
		//column 에 있는 employee num중 최대값을 가져와 그위에 +1을 해준다. (user 번호의 중복을 막기 위해서)
			ResultSet rs = stmt.executeQuery("select max(employee_num) from employee");
			if(rs.next())
			{
				u_num=rs.getInt(1);
			}
			p_st.setString(3, ++u_num +"");
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
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("update employee set employee_id = ? , employee_password = ? ,"
				+ "name = ?, phone = ? where employee_num = ?");
		
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
				case "id":
					p_st.setString(1,temp.getValue());
					break;
				case "password":
					p_st.setString(2,temp.getValue());
					break;
				case "name":
					p_st.setString(3,temp.getValue());
					break;
				case "phone":
					p_st.setString(4,temp.getValue());
					break;
				//수정 안하는 부분은 기본 data로 채운다.
				case "num":
					p_st.setString(5,temp.getValue());
					rs = stmt.executeQuery("select employee_id,employee_paswword,name,phone where employee_num = " + temp.getValue());
					if(rs.next())
					{
						p_st.setString(1,rs.getString("employee_id"));
						p_st.setString(2,rs.getString("employee_password"));
						p_st.setString(3,rs.getString("name"));
						p_st.setString(4,rs.getString("phone"));
						break;
					}
					else
						throw new SQLException("not exist employee number!");
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
		return false;
	}
	/*
	 * employee_Login일 경우 동작하는 부분  (test 미완료)
	 * 개발일 : 14.09.27
	 * 개발자 : 김필기 
	 */
	protected boolean loginEmployee(){
		int i=0;
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("SELECT employee_id, employee_password FROM employee WHERE employee_id = ? and employee_password = ?");
		String id = null;
		//parameter 순서 1-id / 2-password 
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
				case "id":
					id = temp.getValue();
					p_st.setString(1,id);
					break;
				case "password":
					p_st.setString(2,temp.getValue());
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
				reply_data.addContent("employee_LOGIN", "OK");
				return true;
			}
			else
			{
				reply_data.addContent("LOGIN","FAIL");
				sql = new StringBuffer("SELECT * FROM employee WHERE employee_id = ?");
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

}
