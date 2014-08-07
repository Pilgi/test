/*
 * join test를 위해 7월30일날 수정함.
 */
package s;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class client {
    public static void main(String[] args) {
	
		String hostname = "54.92.16.107";
		int port = 6795;
		int i=0;
		ObjectOutputStream ss = null;
	     
        Socket socket = null;  
        
        ObjectInputStream ois = null;
     
    	
     try {
            socket = new Socket(hostname, port);
           
            ss=new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        
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
<<<<<<< HEAD
		/*
			k = new data("JOIN");
			System.out.println("JOIN 정보 입력");
			k.addContent("id", "pilgi");
=======
			data k = new data("JOIN");
			System.out.println("JOIN 정보 입력");
			k.addContent("id", "r44444r4r");
>>>>>>> FETCH_HEAD
			k.addContent("password", "1234");
			k.addContent("sex", "0");
			k.addContent("e_mail", "abcdef");
			k.addContent("name", "hihirs");
			
<<<<<<< HEAD
=======
			//objectstream 을 통해 data class를 전송
>>>>>>> FETCH_HEAD
			ss.reset();
			ss.writeObject(k);
			ss.flush();
	
			System.out.println("소켓 전송 완료");
<<<<<<< HEAD
				
				
			o = (data)ois.readObject();
=======
				

			//objectstream 을 통해 data class를 수신	
			data o = (data)ois.readObject();
>>>>>>> FETCH_HEAD
			
			System.out.println(o.purpose);	   
			System.out.println(o.getContent(0).toString());
			*/
			k = new data("LOGIN");
			System.out.println("LOGIN 정보 입력");
			k.addContent("id", "pilgi");
			k.addContent("password", "1234");
			
			ss.reset();
			ss.writeObject(k);
			ss.flush();
	
			System.out.println("소켓 전송 완료");
				
				
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
