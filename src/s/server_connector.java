package s;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;


/*
 * 서버와 mysql을 연결해주는 커넥터
 */
public class server_connector {
	
	static int u_num = 0;
	private data recv_data;
	private String sql_url = "localhost:3306/order_test";
	Statement stmt;
	
	
	server_connector(data param) throws ClassNotFoundException, SQLException
	{
		if(param == null)
			return;
		recv_data = param;
		start_process();
		
	}
	public void seturl(String temp)
	{
		sql_url = temp;
	}
	private void start_process() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://"+sql_url+"\"","root","gkqpfla");
		System.out.println("sql 서버와 연결되었습니다..");
		//querry 문을 주고 받고 하기 위한 stmt 구문.
		stmt = con.createStatement();
		// TODO Auto-generated method stub
		if(recv_data.purpose.equals("JOIN"))
		{
			joinUser();
		}
	//	stmt.execute("INSERT INTO user_info (user_id,password,user_num,name,sex,e_mail) VALUES ('y30916','sbxjs314',002,'�紩��',0,'y30916@naver.com')");
	//	stmt.execute("insert into user_info(user_id,password,user_num,name,sex,e_mail) values ('pil113','sbxjs314',001,'���ʱ�',0,'pil113@naver.com')");

	}
	
	protected boolean joinUser() throws SQLException
	{
		String user_id = null,password = null,name = null,e_mail = null;
		int sex = 0;
		int i=0;
		data.data_structure temp ;
		while(recv_data.getContent(i)!=null)
		{
			temp = recv_data.getContent(i);
			//gettype으로 가져온 자료가 join일 경우 실행될 부분
			switch (temp.getType()) {
			case "id":
				user_id = temp.getValue();
				break;
			case "password":
				password = temp.getValue();
				break;
			case "name":
				name = temp.getValue();
				break;
			case "e_mail":
				e_mail = temp.getValue();
				break;
				//남자일 경우 sex는 0으로 표현
			case "sex":
				if(temp.getValue().equals("0"))
					sex = 0;
				else
					sex = 1;
				break;
			default:
				break;
			}
			
		}
		if(user_id != null && password !=null && name != null && e_mail !=null)
			return stmt.execute("INSERT INTO user_info (user_id,password,user_num,name,sex,e_mail) VALUES ("
				+user_id+","+password+","+ ++u_num + "," + name + "," + sex + "," + e_mail+")");
		else
			return false;
	}
	

}
