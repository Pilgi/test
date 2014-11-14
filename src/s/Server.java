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
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Server extends Thread{
	
	/*
	 * 서버쪽이 2개일 필요가 없을듯? 1개에서 포트번호 2개 받아서 java용 C용 커넥션을 만들 수 있으면
	 * 그렇게 하자
	 * 08-21
	 */
	 public static void main(String args[]) {
			int port = 6795;
			Server server = new Server( port );
			Server server2 = new Server( 6799 );
			server.start();
			server2.start();
	    }
	public static Server2Connection connection_java;
	public Server2Connection2 connection_c;

	 	Date date = new Date();
	    ServerSocket server = null;
	    Socket socket = null;
	    //하루 누적 clinet 접속 횟수
	    int numConnections = 0, num10 = 0;
	    int port;
	    static int num_of_server = 0;
	    int s_num;
	    
	    public Server( int port ) {
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
					Server2Connection.cbs = connection_c;
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
	    ServerConnector sc;
	    static CallBackSender cbs;
	    //client id!
		int id;
	    Server server;
	    static int s_id = 0;
	   

	    public Server2Connection(Socket socket, int id, Server server) {
	    	// socket establish 부분
			this.socket = socket;
			this.id = id;
			this.server = server;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd / HH.mm.ssSSS");
			System.out.println("server:" + ++s_id + dateformat.format(calendar.getTime())+  " - Connection " + id + " established with: " + socket );
			
		try {
			
			
			sos =socket.getOutputStream();
			sis =socket.getInputStream();
		    dis = new DataInputStream(sis);

		} catch (IOException e) {
		    System.out.println(e);
			e.printStackTrace();
		}
	  }

	    public void run() {
	    	//client에서 보낸 data
	        Data recv_data;
	        try {
			    ois=new ObjectInputStream(sis);
			    oos = new ObjectOutputStream(sos);
	        	while(true){
	        		try
	        		{
	        			recv_data = (Data)ois.readObject();
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

	               try{
	            	   //CALL BACK SENDER 부분
	            	   if(recv_data.getPurpose().equals("ADD ORDER"))
	            		   cbs.send(recv_data); //인터페이스 callback 부분 제대로 추가해야함
	            	   else if(recv_data.getPurpose().equals("REQUEST MUSIC"))
	            		   cbs.send(recv_data);
	               }
	               catch(Exception e)
	               {
	            	   //e.printStackTrace();
	            	   System.out.println("CallBackSender error :" + e);
	               }
	               sc = new ServerConnector(recv_data,s_id);
	               oos.reset();
	               oos.writeObject(sc.reply());
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

	    
		public Data addPhoto ()
		{
			try {
	     	   // code 참고 http://warmz.tistory.com/601 	   
	     	   // 파일명을 전송 받고 파일명 수정.
	            String fName = dis.readUTF();
	            System.out.println("그림파일 " + fName + "을 전송받았습니다.");
	            
	            //파일 이름은 reply data의 첫번째 content에 있는 menu 번호로 수정한다.
	            if(sc.reply().getContent(0).getValue().equals("OK"))
	            {
		            fName = sc.reply().getContent(1).getValue();
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
			return null;
			
		}


}
	interface CallBackSender{
		public void send(Data d);
	}
	class Server2Connection2 extends Server2Connection implements CallBackSender{

		protected BufferedInputStream bis;
	    protected BufferedOutputStream bos = null;
		public Server2Connection2(Socket socket, int id, Server serve) {
			super(socket, id, serve);
			// TODO Auto-generated constructor stub
    		System.out.println("서버223232!!");
            bos = new BufferedOutputStream(sos);
            bis = new BufferedInputStream(sis);
		}
		
	    @SuppressWarnings("null")
		public void run() {
	    	//client에서 보낸 data
	        Data recv_data = null;
	        try {
	        	while(true){
	        		System.out.println("C와의 통신 시작");
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
	               sc = new ServerConnector(recv_data,s_id);

		             
	               /*
	                * recv_data를 C로 보내줘야하는 부분
	                * 08-21
	                */
	               JAVAtoC(sc.reply());
	               System.out.println("server:" + s_id + "JAVA -> C data 전송");
	               
	            //   oos.reset();
	            //   oos.writeObject(sc.reply());
	               //메뉴추가를 했을 경우 data 클래스를 받아온 후에 이미지파일도 받아온다.

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
		private Data CtoJAVA(){
			// TODO Auto-generated method stub
			Data recvdata = null;
			try{
				byte[] b = new byte [1024];
				int size = bis.read(b);
				int count = 0;
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
					else if(protocol_id.equals("I"))
					{
						return addPhoto(byteArrayToInt(b, buffer_point));
					}
					//null이 올때 (close socket을 받았을때)
					else if(protocol_id.charAt(0)==0)
						return null;
					else
						throw new Exception("Socket protocol is incorrect");
					
					data_type = byteArrayToInt(b, buffer_point);
					buffer_point += 4;
					data_length = byteArrayToInt(b, buffer_point);
					buffer_point += 4;
					if(data_type!=1)
						throw new Exception("1이 안왔음;" + data_type);
					recvdata = new Data(CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1")));
					buffer_point += data_length;
							
					data_type = byteArrayToInt(b, buffer_point);
					buffer_point += 4;
					if(data_type != 2)				
						throw new Exception("node 개수인 2가 안왔음 ㅠㅠ __ "+data_type);
					count = byteArrayToInt(b, buffer_point);
					buffer_point += 4;
					//while(buffer_point<=size)
					
					for( int i = 0 ; i < count ;)
					{
						//받은data가 최소단위인 8를 넘지 않으면 새로 받는다. (type,size)
						if(buffer_point + 8 >= size )
							break;
						data_type = byteArrayToInt(b, buffer_point);
						buffer_point += 4;
						data_length = byteArrayToInt(b, buffer_point);
						buffer_point += 4;
						//data type 1:purpose 2:node 갯수 3:node정보 4:type 5:value
						switch(data_type)
						{
						case 3:
							continue;
						case 4:
							recv_type = CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1"));
							//System.out.println("recv_type : " + recv_type);
							buffer_point += data_length;
							break;
						case 5:
							recv_value = CharConversion.E2K(new String(b,buffer_point,data_length,"8859_1"));
							buffer_point += data_length;
							//System.out.println("recv_value : " + recv_value);
							recvdata.addContent(recv_type, recv_value);
							i++;
							break;
						case 99:
							size = bis.read(b);
							buffer_point = 0;
							protocol_id = new String(b,buffer_point++,1);
							if(protocol_id.equals('C'))
								break;
							else
								throw new Exception("잘라서 받는 부분에서 에러 발생");											
						default:
							throw new Exception("socket protocol error!!!__ type"+data_type+"isn't existence");
						}
						//System.out.println("recv_Data="+recvdata.purpose + "___ type=" + data_type);
						//다음에 나오는 캐릭터가 E면 종료!
						//System.out.println("buufer point == " + buffer_point + ", " + new String(b,buffer_point,1).equals("E"));
						if(new String(b,buffer_point,1).equals("E"))
						{
							while_stop = false;
							break;
						}
					}
						while_stop = false;
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

		//data클래스를 buffer 형태로 보내줌
		private boolean JAVAtoC(Data recv_data)
		{
			try{
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				
				int num_of_node;
				num_of_node = recv_data.getContentSize();
				buffer.put((byte) 'S');
				buffer.put(intToByteArray(1));
				
				buffer.put(intToByteArray(recv_data.purpose.length()));
				buffer.put(CharConversion.K2E(recv_data.purpose).getBytes("8859_1"));

				buffer.put(intToByteArray(2));
				buffer.put(intToByteArray(num_of_node));

							
				for(int i = 0; i < num_of_node ; i++)
				{

					//node 인것을 표시

					pushStream(buffer,3,
							intToByteArray(i),
							bos);
					/*
					buffer.put(intToByteArray(2));
					buffer.put(intToByteArray(num_of_node));
					*/
					//Type 전송
					pushStream(buffer,4,
							CharConversion.K2E(recv_data.getContent(i).getType()).getBytes("8859_1"),
							bos);
					/*
					buffer.put(intToByteArray(3));
					byte[] temp = CharConversion.K2E(recv_data.getContent(i).getType()).getBytes("8859_1");
					buffer.put(intToByteArray(temp.length));
					buffer.put(temp);
					*/
					//Value 전송
					pushStream(buffer, 5,
							CharConversion.K2E(recv_data.getContent(i).getValue()).getBytes("8859_1"),
							bos);
					/*
					buffer.put(intToByteArray(4));
					temp = CharConversion.K2E(recv_data.getContent(i).getValue()).getBytes("8859_1");
					buffer.put(intToByteArray(temp.length));
					buffer.put(temp);
					*/	
				}
				bos.write(buffer.array());
				bos.flush();

			}
			catch (Exception e){
				e.printStackTrace();				
			}
			return false;
		}
		private boolean pushStream(ByteBuffer buffer,int protocol, byte[] temp, BufferedOutputStream _bos)
		{
			try{
				if(buffer.position() + 4 + 4 +temp.length >= buffer.capacity())
				{
					//99는 continue를 뜻함!
					buffer.put(intToByteArray(99));
					bos.write(buffer.array());
					bos.flush();
					buffer.clear();
					buffer.put((byte) 'C');
				}
				if(protocol == 3)
				{
					buffer.put(intToByteArray(3));
					buffer.put(temp);
					return true;
					
				}
				buffer.put(intToByteArray(protocol));
				buffer.put(intToByteArray(temp.length));
				buffer.put(temp);
				
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			
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
					//System.out.print(i+idx  + 4- size +"__" + bytes[i+idx + 4 - size] + "\t");
					newBytes[i] = bytes[i+idx + 4 - size];
				}
			}
			//System.out.println();
			buff = ByteBuffer.wrap(newBytes);
			buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
			return buff.getInt();
		}

		@Override
		public void send(Data d) {
			// TODO Auto-generated method stub
			// bytestream을 통해 전송할 부분을 구현하면 된다.
			String hostname = "127.0.0.1";
		    BufferedOutputStream send_bos = null;
		    Socket send_socket = null;
			int port = 4545;
			System.out.println("callback sender_동작");
	        try {
	        	send_socket = new Socket(hostname, port);           
	            send_bos = new BufferedOutputStream(send_socket.getOutputStream());

	            }
	        catch (UnknownHostException e) {
	            System.err.println("Don't know about host: " + hostname);
	        }
	        catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to: " + hostname);
	        }
		
		
	        if (socket == null ) {
	        	System.err.println( "error" );
	        	return;
	        }
	        /*
	         * 소켓 연결 완료
	         */
			try{
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				System.out.println("접속합니다아아아아ㅏ아222222");
				int num_of_node;
				num_of_node = d.getContentSize();
				buffer.put((byte) 'S');
				buffer.put(intToByteArray(1));
				buffer.put(intToByteArray(d.purpose.length()));
				buffer.put(CharConversion.K2E(d.purpose).getBytes("8859_1"));
				
				for(int i = 0; i < num_of_node ; i++)
				{
					buffer.put(intToByteArray(2));
					buffer.put(intToByteArray(num_of_node));
					
					//Type 전송
					buffer.put(intToByteArray(3));
					byte[] temp = CharConversion.K2E(d.getContent(i).getType()).getBytes("8859_1");
					buffer.put(intToByteArray(temp.length));
					buffer.put(temp);
					
					//Value 전송
					buffer.put(intToByteArray(4));
					temp = CharConversion.K2E(d.getContent(i).getValue()).getBytes("8859_1");
					buffer.put(intToByteArray(temp.length));
					buffer.put(temp);	
				}
				send_bos.write(buffer.array());
				send_bos.flush();
			    send_bos.close();
			    send_socket.close();  
			}
			catch (Exception e){
				e.printStackTrace();				
			}
		}
		
		public Data addPhoto(int f_number) {
			// TODO Auto-generated method stub
			try {
		     	   // code 참고 http://warmz.tistory.com/601 	   
		     	   // 파일명을 전송 받고 파일명 수정.
		            //파일 이름은 reply data의 첫번째 content에 있는 menu 번호로 수정한다.
		                String fName = String.valueOf(f_number);
			            //String path =ClassLoader.getSystemResource("").getPath();
			            String path = "/var/www/html/";
			            // 파일을 생성하고 파일에 대한 출력 스트림 생성
			            File f = new File(path +"image/"+fName+".jpg");
			            System.out.println("그림파일 " + path +"image/"+fName+".jpg" + "에 저장하겠습니다.");
			            //현재경로안의 iamge 폴더에 사진을 저장한다.
			            
			            fos = new FileOutputStream(f);
			            System.out.println(fName + "파일을 생성하였습니다.");
			 
			            // 바이트 데이터를 전송받으면서 기록
			            int len;
			            int size = 4096;
			            byte[] data = new byte[size];
			            while ((len = socket.getInputStream().read(data))>0) {
			                fos.write(data,0,len);
			                fos.flush();
			            }
			            System.out.println("파일 수신 작업을 완료하였습니다.");
			            System.out.println("받은 파일의 사이즈 : " + f.length());
		            bos.flush();
		            fos.close();
		            dis.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
				
			}
	}

