/*package s;
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

	
	
	try {
			Scanner keyboard = new Scanner(System.in);
			data k = new data();
		    String a = "id";
		    String b;
		    List<data.data_structure> kkk = new ArrayList<data.data_structure>();
		    
			for(i=0; i<10 ; i++){
				
				
				System.out.print( "사용자 정보 입력:" );
				b = keyboard.next();
				
				data.data_structure ab = k.new data_structure(a,b);
				kkk.add(ab);
				k.purpose = a;
				k.content = kkk;
				ss.reset();
				
				ss.writeObject(k);
				ss.flush();
				
				
			Object o = ois.readObject();
			if(o instanceof data)
			{
				k = (data)o;
				System.out.println("type="+k.purpose);
//				for(int j=0;j<k.content.size();j++)
	//			{
				//	System.out.println("th content =" + k.content.add(ab));
		//		}
			//	k.toString();
			
			}
			else
			{
				break;
			}
			
			
	
		    
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
*/