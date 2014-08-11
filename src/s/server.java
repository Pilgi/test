/*
 멀티쓰레드서버
  */
package s;

//실행파일에서 보내고 받는 중간 부분
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

public class server {
	 public static void main(String args[]) {
			int port = 6795;
			server server = new server( port );
			server.startServer();
	    }


	 	Date date = new Date();
	    ServerSocket server = null;
	    Socket socket = null;
	    //하루 누적 clinet 접속 횟수
	    int numConnections = 0, num10 = 0;
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
			    	if(numConnections>=Integer.MAX_VALUE)
			    	{
			    		numConnections = 0;
			    		num10++;
			    	}
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

	    Socket socket;
		OutputStream sos = null;
		InputStream sis = null;
		ObjectOutputStream oos = null;	
	    ObjectInputStream ois = null;
	    DataInputStream dis = null;
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
	    server_connector sc;
	    //client id!
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
		    ois=new ObjectInputStream(sis);
		    oos = new ObjectOutputStream(sos);
		    dis = new DataInputStream(sis);

		} catch (IOException e) {
		    System.out.println(e);
		}
	  }

	    public void run() {
	    	//client에서 보낸 data
	        data recv_data;
		try {
		       while(true){
		    	   
		    	   try
		    	   {
		    		   recv_data = (data)ois.readObject();
		    	   }
		    	   catch (Exception e)
		    	   {
		    		   //socket으로부터이 읽어오는 data가 null일 경우 (client에서 socekt을 닫은경우) 종료한다.
		    		   System.out.println("server:" + s_id-- + " - Connection " + id + " closed." );
			           oos.close();
			           ois.close();
			           socket.close();
			           break;
		    	   }
		    	   // 정산 요청을 했을 경우 나올 경우
		    	   if(recv_data.getPurpose().equals("calculate") )
		    	   {
		    		   
		    	   }
	               System.out.println("server:" + s_id +   " - Received " +recv_data.getPurpose()+ " from Connection " + id + "." ); 
					
	               sc = new server_connector(recv_data,s_id);
	               if(recv_data.purpose.equals("ADD MENU"))
	               {
	            	   // code 참고 http://warmz.tistory.com/601
	            	   
	            	   // 파일명을 전송 받고 파일명 수정.
	                   String fName = dis.readUTF();
	                   System.out.println("그림파일 " + fName + "을 전송받았습니다.");
	                   
	                   //파일 이름은 request data의 첫번째 content에 있는 menu 번호로 수정한다.
	                   fName = sc.request().getContent(0).getValue();
	        
	                   // 파일을 생성하고 파일에 대한 출력 스트림 생성
	                   File f = new File(fName);
	                   //현재경로안의 iamge 폴더에 사진을 저장한다.
	                   
	                   String path =ClassLoader.getSystemResource("").getPath();
	                   fos = new FileOutputStream(path+"\\image"+f);
	                   bos = new BufferedOutputStream(fos);
	                   System.out.println(fName + "파일을 생성하였습니다.");
	        
	                   // 바이트 데이터를 전송받으면서 기록
	                   int len;
	                   int size = 4096;
	                   byte[] data = new byte[size];
	                   while ((len = dis.read(data)) != -1) {
	                       bos.write(data, 0, len);
	                   }
	        
	                   bos.flush();
	                   bos.close();
	                   fos.close();
	                   dis.close();
	                   System.out.println("파일 수신 작업을 완료하였습니다.");
	                   System.out.println("받은 파일의 사이즈 : " + f.length());
	               }
	               oos.reset();
	               oos.writeObject(sc.request());
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
