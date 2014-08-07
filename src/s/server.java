/*
 멀티쓰레드서버
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
import java.sql.SQLException;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import s.data;
import s.data.data_structure;
import s.Server2Connection;
import s.server;
=======
import java.util.Date;
>>>>>>> FETCH_HEAD

public class server {
	 public static void main(String args[]) {
			int port = 6795;
			server server = new server( port );
			server.startServer();
	    }

<<<<<<< HEAD
	  
	    ServerSocket server = null;
	    Socket socket = null;
	    int numConnections = 0;
=======

	 	Date date = new Date();
	    ServerSocket server = null;
	    Socket socket = null;
	    //하루 누적 clinet 접속 횟수
	    int numConnections = 0, num10 = 0;
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
				socket = server.accept();
				numConnections ++;
			
				Server2Connection oneconnection = new Server2Connection(socket, numConnections, this);
				
				new Thread(oneconnection).start();
			    }   
=======
			    	socket = server.accept();
			    	if(numConnections>=Integer.MAX_VALUE)
			    	{
			    		numConnections = 0;
			    		num10++;
			    	}
			    	numConnections ++;
			    	
				
					Server2Connection oneconnection = new Server2Connection(socket, numConnections, this);
					
					new Thread(oneconnection).start();
				    }   
>>>>>>> FETCH_HEAD
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
	    server_connector sc;
<<<<<<< HEAD
=======
	    //client id!
>>>>>>> FETCH_HEAD
		int id;
	    server server;
	    static int s_id = 0;
	   

	    public Server2Connection(Socket socket, int id, server server) {
	    	// socket establish 부분
			this.socket = socket;
			this.id = id;
			this.server = server;
			System.out.println("server:" + ++s_id +   " - Connection " + id + " established with: " + socket );
			
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
<<<<<<< HEAD
	    	String t = null;

=======
>>>>>>> FETCH_HEAD
	        data test;
	    	/* 이 부분 왜있는지 모르겠음.
	   
	    //    String line[]=new String[10];
	        List<data.data_structure> ab = new ArrayList<data.data_structure>();
	  */
		try {
		       while(true){
	       //어플에서 서버로 서버에서 실행 파일 창에 보이는 것
	        	 
	            /*	String d = "name";
	            	String s = "americano";
	            	
	            	data.data_structure u = test.new data_structure(d,s);
	            	data.data_structure u2 = test.new data_structure("price","2000");
	            	*/
	            	/* ab.add(u);
	            	ab.add(u2);
	            
	            	test.content = ab;
	            	os.reset();
	            	
			        os.writeObject(test);
		            os.flush();
		        	*/
	                
		  //실행파일 주문목록을 add했을 때 받는 부분          
		    	   
		    	   try
		    	   {
		    		   test = (data)is.readObject();
		    	   }
		    	   catch (Exception e)
		    	   {
		    		   //socket으로부터이 읽어오는 data가 null일 경우 (client에서 socekt을 닫은경우) 종료한다.
		    		   System.out.println("server:" + s_id-- + " - Connection " + id + " closed." );
			           os.close();
			           is.close();
			           socket.close();
			           break;
		    	   }
<<<<<<< HEAD
		    	   
	               System.out.println("server:" + s_id +   " - Received " +" "+" "+t+ " from Connection " + id + "." ); 
=======
		    	   // 정산 요청을 했을 경우 나올 경우
		    	   if(test.getPurpose().equals("calculate") )
		    	   {
		    		   
		    	   }
	               System.out.println("server:" + s_id +   " - Received " +test.getPurpose()+ " from Connection " + id + "." ); 
>>>>>>> FETCH_HEAD
					
	               sc = new server_connector(test,s_id);
	               os.reset();
	               os.writeObject(sc.request());
	             } 
	            
	
			}catch (IOException e) {
			    System.out.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    }
}
