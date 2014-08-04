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
	
	int u_num = 0;
	private data recv_data;
	private String sql_url = "localhost:3306/order_test";
	private Connection con;
	private data request_data = null;
	Statement stmt;
	
	
	server_connector(data param) throws ClassNotFoundException, SQLException
	{
		if(param == null)
			return;
		recv_data = param;
		start_process();
		
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
		System.out.println("sql 서버와 연결되었습니다..");
		
		//querry 문을 주고 받고 하기 위한 stmt 구문.
		stmt = con.createStatement();
		System.out.println(recv_data.purpose);
		request_data = new data("request");
		if(recv_data.purpose.equals("JOIN"))
		{
			System.out.println("join 확인 ㄱㄱ");
			//joinuser가 false면 query 전달 부분에 문제가 있는것 -> 관련된 내용을 client로 보내줄 것.
			joinUser();
		}

	}
	//JOIN 일 경우 동작하는 부분.
	protected boolean joinUser()
	{
		int i=0;
		data.data_structure temp ;
		StringBuffer sql = new StringBuffer("insert into user_info(user_id,password,user_num,name,sex,e_mail) values (?,?,?,?,?,?)");
		//parameter 순서 1-id / 2-password / 3-user_number / 4-name / 5-sex / 6-e-mail
		PreparedStatement p_st = null;
		try {
			p_st = con.prepareStatement(sql.toString());

		System.out.println("join 확인 ㄱㄱㄱ"+recv_data.content.size());
		while(recv_data.getContent(i)!=null)
		{
			//gettype으로 가져온 자료가 join일 경우 실행될 부분
			temp = recv_data.getContent(i++);
			
			//test로 들어오는 data 확인하는 부분
			System.out.println("type =" + temp.getType() + ",  value =" + temp.getValue());
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
			p_st.setString(3, u_num+"");
			System.out.println(p_st.toString());
			request_data.addContent("error", "duplicate key");
			return p_st.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				if(e.getErrorCode() == 1062)
				{
					System.out.println("중복에러!!");
				}
				request_data.addContent("error", "duplicate key");
				return false;
			}
	}
	
	//Login일 경우 동작하는 부분
	protected boolean loginUser(){
		return false;
	}
	//menu를 불러올때 사용되는 부분
	protected boolean showMenu()
	{
		return false;
	}
	//주문할 경우 실행될 부분
	protected boolean orderMenu()
	{
		return false;
	}
	protected boolean addMenu()
	{
		return false;
	}
	protected boolean modifyMenu()
	{
		return false;
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
