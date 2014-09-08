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
	private data request_data = null;
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
	
	public data request()
	{
		return request_data;
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
		request_data = new data("request");
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
	}
	// SQL ERROR 가 뭔지를 확인하는 세션
	protected boolean sqlErrorCheck (SQLException e)
	{
		if(e.getErrorCode() == 1062)
		{	
			System.out.println("server:" + s_id + " - " +"중복에러!!");
			//join의 경우 0일 경우만 있음
			request_data.content.clear();
			request_data.addContent("ERROR CODE", e.getErrorCode()+"");
			request_data.addContent("error", "duplicate key");
			return true;
		}
		else
		{
			request_data.content.clear();
			request_data.addContent("ERROR CODE", e.getErrorCode()+"");
			request_data.addContent("error", e.toString());
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
		StringBuffer sql = new StringBuffer("insert into user_info(user_id,password,user_num,name,sex,e_mail,birthday) values (?,?,?,?,?,?,?)");
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
					p_st.setString(7, temp.getValue());
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
			request_data.addContent("JOIN", "OK");				
			return p_st.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				/*
				if(e.getErrorCode() == 1062)
				{
					System.out.println("server:" + s_id + " - " +"중복에러!!");
					//join의 경우 0일 경우만 있음
					request_data.deleteContent(0);
					request_data.addContent("error", "duplicate key");
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
				request_data.addContent("LOGIN", "OK");
				return true;
			}
			else
			{
				request_data.addContent("LOGIN","FAIL");
				sql = new StringBuffer("SELECT * FROM user_info WHERE user_id = ?");
				p_st = con.prepareStatement(sql.toString());
				p_st.setString(1,id);
				rs = p_st.executeQuery();
				if(rs.next())
				{
					request_data.addContent("ERROR CODE", "WORNG PASSWORD");
				}
				else
					request_data.addContent("ERROR CODE", "ID does not exist");
				return false;
			}
			} catch (SQLException e) {
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

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
			request_data.addContent(category, "OK");
			while(rs.next())
			{
				count++;
				request_data.addContent(count +"_menu_name", rs.getString("menu_name"));
				request_data.addContent(count +"_size", rs.getString("size"));
				request_data.addContent(count +"_price", rs.getString("price"));
				request_data.addContent(count +"_menu_num", rs.getString("menu_num"));

			}
			request_data.modifyContent(0, category, count+"");
			} catch (SQLException e) {
				request_data.addContent("DETAIL MENU","FAIL");
				request_data.addContent("ERROR CODE", e.toString());
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
				//System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
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
				request_data.addContent("DETAIL", "OK");
				request_data.addContent("menu_name", rs.getString("menu_name"));
				request_data.addContent("category", rs.getString("category"));
				request_data.addContent("size", rs.getString("size"));
				request_data.addContent("price", rs.getString("price"));
				request_data.addContent("detail", rs.getString("detail"));
				request_data.addContent("image", rs.getString("image"));
				return true;
			}
			else
			{
				request_data.addContent("DETAIL MENU","FAIL");
				request_data.addContent("ERROR CODE", "menu num does not exist");
				return false;
			}
			} catch (SQLException e) {
				request_data.addContent("DETAIL MENU","FAIL");
				request_data.addContent("ERROR CODE", e.toString());
				e.printStackTrace();
				sqlErrorCheck(e);
				return false;
			}

		
	}
	//주문할 경우 실행될 부분
	protected boolean orderMenu()
	{
		return false;
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
			request_data.addContent("ADD MENU", "OK");
			request_data.addContent("MENU NUM", menu_num+"");								
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
	 * 메뉴를 수정할때 실행되는 부분
	 * 개발일 : 14.09.07~
	 * 개발자 : 김필기
	 */
	protected boolean modifyMenu()
	{
		int i=0;
		int 	menu_num = 0, category_num = 0;
		String img_dir = "/image/";
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
			request_data.addContent("MODIFY MENU", "OK");						
			request_data.addContent("menu_num" , menu_num +"");
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
	protected boolean deleteMenu()
	{
		return false;
	}
	protected boolean addStamp()
	{
		return false;
	}

	protected boolean addBalance()
	{
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
}
