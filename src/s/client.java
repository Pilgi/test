package s;
import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) throws ClassNotFoundException {
	
		String hostname = "localhost";
		int port = 6795;
		int i=0;
		types k = new types();
		
        Socket clientSocket = null;  
        DataOutputStream os = null;
        DataOutputStream os2 = null;
        DataInputStream is = null;
        ObjectInputStream is2 = null;
        BufferedReader br = null;
     
    	String responseLine[]=new String[10];
    	
     try {
            clientSocket = new Socket(hostname, port);
            os = new DataOutputStream(clientSocket.getOutputStream());
            os2 = new DataOutputStream(clientSocket.getOutputStream());
            
            
            is = new DataInputStream(clientSocket.getInputStream());
            is2 = new ObjectInputStream(clientSocket.getInputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }
	
	
	if (clientSocket == null || os == null ||	os2 == null || is == null) {
	    System.err.println( "error" );
	    return;
	}

	
	try {
		    
			while ( true ) {
				System.out.print( "사용자 정보 입력:" );
				
		Object o = is2.readObject();
			if(o instanceof types)
			{
				types test = (types)o;
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
			
			// responseLine[i] = is.readUTF();
			// System.out.println("Server returns it as: " + responseLine[i]);
			i++;
		    
		    }   
	   
		     
		    os.close();
		    os2.close();
		    is.close();
		    is2.close();
		    clientSocket.close();   //서버에서 끝나면 client가 안끝남
		    
		    
		} catch (UnknownHostException e) {
		    System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
		    System.err.println("IOException:  " + e);
		}
	    }           
	}
