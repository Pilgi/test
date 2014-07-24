/* 
 * 멀티 쓰레드 서버
 */
package s;

import java.io.DataInputStream;
import java.io.IOException;
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
    Socket clientSocket = null;
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
		clientSocket = server.accept();
		numConnections ++;
	
		Server2Connection oneconnection = new Server2Connection(clientSocket, numConnections, this);
		new Thread(oneconnection).start();
	    }   
	    catch (IOException e) {
		System.out.println(e);
	    }
	}
    }
}

class Server2Connection implements Runnable {
    DataInputStream a;
    DataInputStream b;
    ObjectOutputStream os;	
    //DataOutputStream os;
    Socket clientSocket;
    int id;
    server server;
    int i=0;
    types test = new types();

    public Server2Connection(Socket clientSocket, int id, server server) {
	this.clientSocket = clientSocket;
	this.id = id;
	this.server = server;
	System.out.println( "Connection " + id + " established with: " + clientSocket );
	try {
	    a=new DataInputStream(clientSocket.getInputStream());
	    b=new DataInputStream(clientSocket.getInputStream());
	    
	    os = new ObjectOutputStream(clientSocket.getOutputStream());
	} catch (IOException e) {
	    System.out.println(e);
	}
    }

    public void run() {
    	String t;
        String line[]=new String[10];
	try {
	   
		boolean serverStop = false;

            while (true) {
            	t=a.readUTF();
            	
                line[i] = b.readUTF();
                System.out.println( "Received " +" "+t+" "+ line[i] + " from Connection " + id + "." ); //�Ѳ����� ���̵��� ���ϰ� ������ �޾����� �� �ذ���
              
               os.writeObject(test);
            if(line[i].equals("x")){
            	serverStop=true;
            	break;
            }
            } 
            
            System.out.println( "Connection " + id + " closed." );
            
            a.close();
            b.close();
            os.close();
            clientSocket.close();

	    if ( serverStop ) server.stopServer();

	}catch (IOException e) {
	    System.out.println(e);
	}
    }
}
