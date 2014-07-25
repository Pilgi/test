/* 
 * 멀티 쓰레드 서버
 */
package s;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    DataInputStream dis;
    DataInputStream dis2;
    ObjectOutputStream os;	
    Socket socket;
    InputStream ab = null;
	ObjectInputStream cd = null;
	
    int id;
    server server;
    int i=0;
    data test = new data();

    public Server2Connection(Socket socket, int id, server server) {
	this.socket = socket;
	this.id = id;
	this.server = server;
	System.out.println( "Connection " + id + " established with: " + socket );
	try {
	    dis=new DataInputStream(socket.getInputStream());
	    dis2=new DataInputStream(socket.getInputStream());
	    
	    ab=socket.getInputStream();
	    cd=new ObjectInputStream(ab);
	    os = new ObjectOutputStream(socket.getOutputStream());
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    public void run() {
    	String t = "id",l = "yubin";
        String line[]=new String[10];
        data.data_structure ab ;
	try {
	   
		boolean serverStop = false;

            while (true) {
            	ab =  test.new data_structure(t,l);
            	t=ab.getType();
            	l=ab.getContent();
            	
            	ab = (data.data_structure)cd.readObject();
            	
<<<<<<< HEAD
            
            //   test.toString(); 
=======
                line[i] = b.readUTF();
                System.out.println( "Received " +" "+t+" "+ line[i] + " from Connection " + id + "." ); 
>>>>>>> origin/master
              
               os.writeObject(test);
            if(line[i].equals("x")){
            	serverStop=true;
            	break;
            }
            } 
            
            System.out.println( "Connection " + id + " closed." );
            
            dis.close();
            dis2.close();
            os.close();
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
