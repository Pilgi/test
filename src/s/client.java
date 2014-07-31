/*
 * join test를 위해 7월30일날 수정함.
 */
package s;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class client {
    public static void main(String[] args) {
	
		String hostname = "localhost";
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
	
	
	try {
		data k = new data("JOIN");
		System.out.println("JOIN 정보 입력");
		k.addContent("id", "root");
		k.addContent("password", "1234");
		k.addContent("sex", "0");
		k.addContent("e_mail", "pil13@naver.com");
		k.addContent("name", "김필기");
		
		ss.reset();
		ss.writeObject(k);
		ss.flush();

		System.out.println("소켓 전송 완료");
				
				
			Object o = ois.readObject();

	   
		    
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
