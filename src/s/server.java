/* 
 * 硫�떚 �곕젅���쒕쾭
 */
package s;

//실행파일에서 보내고 받는 중간 부분
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import s.data;
import s.data.data_structure;
import s.Server2Connection;
import s.server;

public class server {
	 public static void main(String args[]) {
			int port = 6795;
			server server = new server( port );
			server.startServer();
	    }

	  
	    ServerSocket server = null;
	    Socket socket = null;
	    int numConnections = 0;
	    int port;
		
	    public server( int port ) {
	    	this.port = port;
	    }

	    public void stopServer() {
			System.out.println( "Server cleaning up." );
			System.exit(0);
	    }

	    public void startServer() {
		
	        try {
		    server = new ServerSocket(port);
	        }
	        catch (IOException e) {
		    System.out.println(e);
	        }   
		
		System.out.println( "Server is started and is waiting for connections." );


		
		while ( true ) {
		    try {
			socket = server.accept();
			numConnections ++;
		
			Server2Connection oneconnection = new Server2Connection(socket, numConnections, this);
			
			new Thread(oneconnection).start();
		    }   
		    catch (IOException e) {
			System.out.println(e);
		    }
		}
	    }
	}

	class Server2Connection implements Runnable {

		OutputStream sos = null;
		InputStream sis = null;
		ObjectOutputStream os = null;	
	    Socket socket;
	    ObjectInputStream is = null;
		int id;
	    server server;
	    int i=0;
	   

	    public Server2Connection(Socket socket, int id, server server) {
			this.socket = socket;
			this.id = id;
			this.server = server;
			System.out.println( "Connection " + id + " established with: " + socket );
			
		try {
			sos =socket.getOutputStream();
			sis =socket.getInputStream();
			
		    is=new ObjectInputStream(sis);
		    os = new ObjectOutputStream(sos);
		} catch (IOException e) {
		    System.out.println(e);
		}
	  }

	    public void run() {
	    	String t = null;
	    //    String line[]=new String[10];
	        data test = new data();
	        List<data.data_structure> ab = new ArrayList<data.data_structure>();
	  
		try {
		   
			boolean serverStop = false;
			data k = new data();
	           while(true){
	       //어플에서 서버로 서버에서 실행 파일 창에 보이는 것
	            	String d = "name";
	            	String s = "americano";
	            	
	            	data.data_structure u = test.new data_structure(d,s);
	            	data.data_structure u2 = test.new data_structure("price","2000");
	            	
	            	ab.add(u);
	            	ab.add(u2);
	            	
	            	test.content = ab;
	            	os.reset();
	            	
			        os.writeObject(test);
		            os.flush();
		            
		  //실행파일 주문목록을 add했을 때 받는 부분          
		            k = (data)is.readObject();
	            	
	            	t = k.purpose;
	            	ab = k.content;
	            	ab.toString();
	                System.out.println( "Received " +" "+" "+ab+ " from Connection " + id + "." ); 
	  
	                if(test.equals("\n")){
	                	serverStop = false;
	                	break;
	                }
	             } 
	            
	            System.out.println( "Connection " + id + " closed." );
	        
	            os.close();
	            is.close();
	            socket.close();

		    if ( serverStop ) server.stopServer();

		}catch (IOException e) {
		    System.out.println(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
}
