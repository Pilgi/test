/*
 멀티쓰레드서버
  */
package s;

//실행파일에서 보내고 받는 중간 부분
import java.io.BufferedInputStream;
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.Date;


public class server extends Thread{
	
	/*
	 * 서버쪽이 2개일 필요가 없을듯? 1개에서 포트번호 2개 받아서 java용 C용 커넥션을 만들 수 있으면
	 * 그렇게 하자
	 * 08-21
	 */
	 public static void main(String args[]) {
			int port = 6795;
			server server = new server( port );
			server server2 = new server( 6799 );
			server.start();
			server2.start();
	    }
	public static Server2Connection connection_java;
	public static Server2Connection2 connection_c;

	 	Date date = new Date();
	    ServerSocket server = null;
	    Socket socket = null;
	    //하루 누적 clinet 접속 횟수
	    int numConnections = 0, num10 = 0;
	    int port;
	    static int num_of_server = 0;
	    int s_num;
	    
	    public server( int port ) {
	    	super("Thread" + num_of_server);
	    	s_num = num_of_server++;
	    	this.port = port;
	    }
	    
	    public void run()
	    {
	    	startServer(port);
	    }

	    public void stopServer() {
			System.out.println( "Server cleaning up." );
			System.exit(0);
	    }

	    public void startServer(int port_num) {
	    	try {
	    		server = new ServerSocket(port_num);
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
			    	
				if(s_num==0)
				{
					System.out.println("서버1 start!!");
					connection_java = new Server2Connection(socket, numConnections, this);
					new Thread(connection_java).start();
					
				}
				else
				{	        		
					System.out.println("서버2 start!!");
					connection_c = new Server2Connection2(socket, numConnections, this);				
					new Thread(connection_c).start();
				}
				
				    }   
			    catch (IOException e) {
				System.out.println(e);
			    }
			}
	    }
		public void startServer()
		{
			startServer(port);
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
	    CallBackSender cbs;
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
		    dis = new DataInputStream(sis);
		    cbs=s.server.connection_c;

		} catch (IOException e) {
		    System.out.println(e);
			e.printStackTrace();
		}
	  }

	    public void run() {
	    	//client에서 보낸 data
	        data recv_data;
	        try {
			    ois=new ObjectInputStream(sis);
			    oos = new ObjectOutputStream(sos);
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
	         //************************************************************
	               cbs.send(recv_data); //인터페이스 callback 부분 제대로 추가해야함
	               sc = new server_connector(recv_data,s_id);
	               oos.reset();
	               oos.writeObject(sc.request());
	               //메뉴추가를 했을 경우 data 클래스를 받아온 후에 이미지파일도 받아온다.
	               
	               if(recv_data.purpose.equals("ADD MENU"))
	               {
		             addPhoto();             
	            	   
	               }
	             } 
	            
	
			}catch (IOException e) {
				e.printStackTrace();		    
			    System.out.println("IOException :" + e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    System.out.println("ClassNotFoundException :" + e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    System.out.println("SQLException :" + e);
			}
		
	    }

	    
		public boolean addPhoto ()
		{
			try {
	     	   // code 참고 http://warmz.tistory.com/601 	   
	     	   // 파일명을 전송 받고 파일명 수정.
	            String fName = dis.readUTF();
	            System.out.println("그림파일 " + fName + "을 전송받았습니다.");
	            
	            //파일 이름은 request data의 첫번째 content에 있는 menu 번호로 수정한다.
	            if(sc.request().getContent(0).getValue().equals("OK"))
	            {
		            fName = sc.request().getContent(1).getValue();
		            String path =ClassLoader.getSystemResource("").getPath();
		            //String path = "/var/www/html/";
		            // 파일을 생성하고 파일에 대한 출력 스트림 생성
		            File f = new File(path +"image/"+fName+".jpg");
		            System.out.println("그림파일 " + path +"image/"+fName+".jpg" + "에 저장하겠습니다.");
		            //현재경로안의 iamge 폴더에 사진을 저장한다.
		            
		            fos = new FileOutputStream(f);
		            bos = new BufferedOutputStream(fos);
		            System.out.println(fName + "파일을 생성하였습니다.");
		 
		            // 바이트 데이터를 전송받으면서 기록
		            int len;
		            int size = 4096;
		            byte[] data = new byte[size];
		            while ((len = dis.read(data)) != -1) {
		                bos.write(data, 0, len);
		            }
		            System.out.println("파일 수신 작업을 완료하였습니다.");
		            System.out.println("받은 파일의 사이즈 : " + f.length());
	            }
	            bos.flush();
	            bos.close();
	            fos.close();
	            dis.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return false;
			
		}


}
	interface CallBackSender{
		public void send(data d);
	}
	class Server2Connection2 extends Server2Connection implements CallBackSender{

		protected BufferedInputStream bis;
	    protected BufferedOutputStream bos = null;
		public Server2Connection2(Socket socket, int id, s.server server) {
			super(socket, id, server);
			// TODO Auto-generated constructor stub
    		System.out.println("서버223232!!");
            bos = new BufferedOutputStream(sos);
            bis = new BufferedInputStream(sis);
		}
		
	    @SuppressWarnings("null")
		public void run() {
	    	//client에서 보낸 data
	        data recv_data = null;
	        try {
	        	while(true){
	        		System.out.println("서버2222222!!");
	        		try
	        		{ 
	        			recv_data = CtoJAVA();
	        			if(recv_data==null)
	        				throw new Exception();
	        		}
	        		catch (Exception e)
	        		{
		    		   //socket으로부터이 읽어오는 data가 null일 경우 (client에서 socekt을 닫은경우) 종료한다.
		    		   System.out.println("server:" + s_id-- + " - Connection " + id + " closed." );
		    		   bis.close();
		    		   bos.close();
			           socket.close();
			           break;
		    	   }
		    	   // 정산 요청을 했을 경우 나올 경우
		    	   if(recv_data != null && recv_data.getPurpose().equals("calculate") )
		    	   {
		    		   
		    	   }
	               if(recv_data !=null)
	            	   System.out.println("server:" + s_id +   " - Received " +recv_data.getPurpose()+ " from Connection " + id + "." ); 
					
	               sc = new server_connector(recv_data,s_id);
	               /*
	                * recv_data를 C로 보내줘야하는 부분
	                * 08-21
	                */
	               
	               
	               
	               
	               
	               
	            //   oos.reset();
	            //   oos.writeObject(sc.request());
	               //메뉴추가를 했을 경우 data 클래스를 받아온 후에 이미지파일도 받아온다.
	               if(recv_data.purpose.equals("ADD MENU"))
	               {
		             addPhoto();             
	            	   
	               }
	             } 
	            
	
			}catch (IOException e) {
				e.printStackTrace();		    
			    System.out.println("IOException :" + e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    System.out.println("ClassNotFoundException :" + e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    System.out.println("SQLException :" + e);
			}
		
	    }


		//BUffer에서 받아온 data를 data 클래스로 변경해줘야 함	    
		private data CtoJAVA(){
			// TODO Auto-generated method stub
			data recvdata = null;
			try{
				byte[] b = new byte [256];
				int size = bis.read(b);
				String protocol_id = new String(b,0,1);
				int buffer_point = 0;
				int data_length = 0 , data_type = 0;
				String recv_type = null , recv_value;
				boolean while_stop = true;

				while(while_stop)
				{
					buffer_point = 0;
					protocol_id = new String(b,buffer_point++,1);
					//protocol 이 제대로 된 data가 들어 오는지 확인 
					if(protocol_id.equals("S"))
						System.out.println("c와 통신 _ 수신 시작");
					else if(protocol_id.equals("C"))
					{
						
					}
					//null이 올때 (close socket을 받았을때)
					else if(protocol_id.charAt(0)==0)
						return null;
					else
					{
							throw new Exception("Socket protocol is incorrect");
					}
					
					while(buffer_point<=size)
					{
						//받은data가 최소단위인 8를 넘지 않으면 새로 받는다. (type,size)
						if(buffer_point + 8 >= size )
							break;
						data_type = byteArrayToInt(b, buffer_point);
						buffer_point += 4;
						data_length = byteArrayToInt(b, buffer_point);
						buffer_point += 4;
						//data type 1:purpose 2:content 3:type 4:value
						switch(data_type)
						{
						case 1:
							recvdata = new data(CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1")));
							buffer_point += data_length;
							break;
						case 2:
							continue;
						case 3:
							recv_type = CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1"));
							System.out.println("recv_type : " + recv_type);
							buffer_point += data_length;
							break;
						case 4:
							recv_value = CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1"));
							buffer_point += data_length;
							System.out.println("recv_value : " + recv_value);
							recvdata.addContent(recv_type, recv_value);
							break;
						default:
							throw new Exception("socket protocol error!!!__ type"+data_type+"isn't existence");
						}
						System.out.println("recv_Data="+recvdata.purpose + "___ type=" + data_type);
						//다음에 나오는 캐릭터가 E면 종료!
						System.out.println("buufer point == " + buffer_point + ", " + new String(b,buffer_point,1).equals("E"));
						if(new String(b,buffer_point,1).equals("E"))
						{
							while_stop = false;
							break;
						}
					}
			
				}
				//char a = (char)bis.read();
				//for (int i=0; i<3; i++) bis.read();
				int te = byteArrayToInt(b,1);
				
 				System.out.println("출력" + protocol_id);
 				System.out.println("출력" + te);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("covert 에러"+e);
				return null;
			}
			return recvdata;
        	
		}
		
		/*
		 * 코드 출저 http://aldehyde7.tistory.com/159
		 * 				Dynamic life 블로그
		 */
		private static byte[] intToByteArray(final int integer) {
			ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
			buff.putInt(integer);
			buff.order(ByteOrder.BIG_ENDIAN);
			//buff.order(ByteOrder.LITTLE_ENDIAN);
			return buff.array();
		}
		private static int byteArrayToInt(byte[] bytes , int idx ) {
			final int size = Integer.SIZE / 8;
			ByteBuffer buff = ByteBuffer.allocate(size);
			final byte[] newBytes = new byte[size];
			for (int i =0 ; i < size; i++) {
				if (i + bytes.length < size) {
					newBytes[i] = (byte) 0x00;
				} else {
					System.out.print(i+idx  + 4- size +"__" + bytes[i+idx + 4 - size] + "\t");
					newBytes[i] = bytes[i+idx + 4 - size];
				}
			}
			System.out.println();
			buff = ByteBuffer.wrap(newBytes);
			buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
			return buff.getInt();
		}

		@Override
		public void send(data d) {
			// TODO Auto-generated method stub
			// bytestream을 통해 전송할 부분을 구현하면 된다.
			System.out.println("전송합시다");
		}
		
		
	}

