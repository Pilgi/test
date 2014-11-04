/*
 * join test를 위해 7월30일날 수정함.
 */
package com.example.cafeapp;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class client {
    static Socket socket = null;  
    static ObjectOutputStream oos = null;
    static ObjectInputStream ois = null;
    static DataOutputStream dos = null;
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    public static void main(String[] args) {
    	String hostname = "54.92.16.107";
    	//String hostname = "203.252.118.17";
		
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

			System.out.println("소켓 전송 완료");  
        	
        	k=new data("SHOW MENU");
   			System.out.println("MENU 자세히 확인");
			k.addContent("category", "커피");
			oos.reset();
			oos.writeObject(k);
			oos.flush();
	
			System.out.println("소켓 전송 완료");  
			o = (data)ois.readObject();

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

		    ois.close();
		    oos.close();
		    socket.close();    
		    
		    
		} catch (UnknownHostException e) {
		    System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
		    System.err.println("IOException:  " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }         
   
    
	}
