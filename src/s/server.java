/* 
 * 硫�떚 �곕젅���쒕쾭
 */
package s;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import s.data;
import s.data.data_structure;

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
	    is=new ObjectInputStream(socket.getInputStream());
	    os = new ObjectOutputStream(socket.getOutputStream());
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

	            for(i=0; i<10; i++) {
	          /*  	test = (data)is.readObject();
	            	
	            	t = test.purpose;
	            	ab = test.content;
	            	
	                System.out.println( "Received " +" "+t+" "+ab+ " from Connection " + id + "." ); 
	  
		            if(test.equals("x")){
		            	serverStop=true;
		            	break;
		            }
		       */
	            	String d = "name";
/*send 해서 gui나타나게*/	           String s = "americano";
	            	
	            	data.data_structure u = test.new data_structure(d,s);
	            	data.data_structure u2 = test.new data_structure("price","2000");
	            	
	            	ab.add(u);
	            	ab.add(u2);
	            	
	            	test.content = ab;
	            	
	            	os.reset();
	            	
			        os.writeObject(test);
		            os.flush();
		            
	            } 
	            
	            System.out.println( "Connection " + id + " closed." );
	        
	            os.close();
	            is.close();
	            socket.close();

		    if ( serverStop ) server.stopServer();

	}catch (IOException e) {
	    System.out.println(e);
	}
    }
}
