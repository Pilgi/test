/*
 * join test를 위해 7월30일날 수정함.
 */
package s;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class client_add_menu {
    static Socket socket = null;  
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
    static DataOutputStream dos = null;
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    public static void main(String[] args) {
    	//String hostname = "54.92.16.107";
    	String hostname = "127.0.0.1";
		
		int port = 6795;
		int i=0;
        
        try {
        	socket = new Socket(hostname, port);           
            oos=new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
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
        data k;
        data o;
        try {
        	/*
			k = new data("JOIN");
			System.out.println("JOIN 정보 입력");
			k.addContent("id", "pilgi");
			k.addContent("password", "1234");
			k.addContent("sex", "0");
			k.addContent("e_mail", "abcdef");
			k.addContent("name", "hihirs");
			
			ss.reset();
			ss.writeObject(k);
			ss.flush();
	
			System.out.println("소켓 전송 완료");
				
				
			o = (data)ois.readObject();
			
			System.out.println(o.purpose);	   
			System.out.println(o.getContent(0).toString());
			*/
        	/*
			k = new data("LOGIN");
			System.out.println("LOGIN 정보 입력");
			k.addContent("id", "pilgi");
			k.addContent("password", "1234");
			*/
        	/*
			k = new data("ADD MENU");
			System.out.println("MENU 추가");
			k.addContent("menu_name", "테스트 음료11123");
			k.addContent("category", "커피");
			k.addContent("price","5000");
			k.addContent("size","소");
			k.addContent("detail","양덕에서 공수한 슈퍼 커피");
			/*
        	
        	k=new data("SHOW DETAIL");
   			System.out.println("MENU 자세히 확인");
			k.addContent("menu_num", "2");
			oos.reset();
			oos.writeObject(k);
			oos.flush();
        	 */
			System.out.println("소켓 전송 완료");
			o = (data)ois.readObject();

			//전송 테스트 (image 전송을 위해서)
			filesender(socket,"001.JPG");
			
			System.out.println(o.purpose);	   
			for(i=0;o.getContent(i)!=null;i++)
			{
				System.out.println(o.getContent(i).toString());
				
			}	
			/*
			//서버에서 보내주는 파일 보여주는 코드
			o = (data)ois.readObject();
			System.out.println(o.purpose);	   
			for(i=0;o.getContent(i)!=null;i++)
			{
				System.out.println(o.getContent(i).toString());
				
			}
			*/
            dos.close();
            bis.close();
            fis.close();
		    ois.close();
		    oos.close();
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
    public static void filesender(Socket socket, String file_name) {

        try {
        	dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("파일 전송 작업을 시작합니다.");
            String fName = file_name;
            dos.writeUTF(fName);
            System.out.printf("파일 이름(%s)을 전송하였습니다.\n", fName);
 
            // 파일 내용을 읽으면서 전송
            File f = new File(fName);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
 
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = bis.read(data)) != -1) {
                dos.write(data, 0, len);
            }
 
            dos.flush();

            
            System.out.println("파일 전송 작업을 완료하였습니다.");
            System.out.println("보낸 파일의 사이즈 : " + f.length());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
	}
