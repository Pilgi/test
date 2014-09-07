/*
 * join test를 위해 7월30일날 수정함.
 */
package s;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class client {
    public static void main(String[] args) {
	
		String hostname = "203.252.118.17";
		int port = 6795;
		int i=0;
		ObjectOutputStream ss = null;
	     
        Socket socket = null;  
        
        ObjectInputStream ois = null;
        BufferedReader read = null;
     
    	
     try {
            socket = new Socket(hostname, port);
           
            ss=new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            read = new BufferedReader(new InputStreamReader(System.in));
        
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }
	
	
	if (socket == null ) {
	    System.err.println( "error" );
	    return;
	}
/*
 * 소켓 연결 완료
 */
	data k;
	data o;
	try {
		
			k = new data("JOIN");
			System.out.println("JOIN 정보 입력");
			k.addContent("id", "pijjlgi");
			k.addContent("password", "1234");
			k.addContent("sex", "0");
			k.addContent("e_mail", "abcdef");
			k.addContent("name", "hihhhirs");
			k.addContent("birthday", "19920113");
			
			ss.reset();
			ss.writeObject(k);
			ss.flush();
	
			System.out.println("소켓 전송 완료");
				
				
			o = (data)ois.readObject();
			
			System.out.println(o.purpose);	   
			System.out.println(o.getContent(0).toString());
			
			k = new data("LOGIN");
			System.out.println("LOGIN 정보 입력");
			k.addContent("id", "pilgi");
			k.addContent("password", "1234");
			
			ss.reset();
			ss.writeObject(k);
			ss.flush();
	
			System.out.println("소켓 전송 완료");
		/*
			k = new data("ADDMENU");
			System.out.println("MENU 추가");
			k.addContent("menu_name","아메리카노");
			k.addContent("category", value)
			*/	
			o = (data)ois.readObject();
			
			System.out.println(o.purpose);	   
			for(i=0;o.getContent(i)!=null;i++)
			{
				System.out.println(o.getContent(i).toString());
				
			}
		    ois.close();
		    ss.close();
		    socket.close();    
		    
		    
		} catch (UnknownHostException e) {
		    System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
		    System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }           
	}
