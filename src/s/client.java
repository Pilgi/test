package s;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws ClassNotFoundException {
	
		String hostname = "localhost";
		int port = 6795;
		int i=0;
	
        Socket socket = null;  
      
        ObjectInputStream ois = null;
        BufferedReader read = null;
     
    	
     try {
            socket = new Socket(hostname, port);
           
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

	
	try {
			Scanner keyboard =  new Scanner(System.in);
		    String a;
			while ( true ) {
				System.out.print( "사용자 정보 입력:" );
		
				
			Object o = ois.readObject();
			if(o instanceof data)
			{
				data test = (data)o;
				System.out.println("type="+test.purpose);
				for(int j=0;j<test.content.size();j++)
				{
					System.out.println(j+"th content =" + test.content.get(j));
				}
			}
			else
			{
				break;
			}
			
			
			i++;
		    
		    }   
	   
		    
		    ois.close();
		    socket.close();    
		    
		    
		} catch (UnknownHostException e) {
		    System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
		    System.err.println("IOException:  " + e);
		}
	    }           
	}
