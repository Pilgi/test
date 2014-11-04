package com.example.cafeapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * <pre>
 * kisti.server
 *   |_ socket2.java
 *
 * </pre>
 *
 * Desc :
 * @Company : KISTI
 * @Author :grkim
 * @Date   :2011. 7. 11. 오후 10:34:06
 * @Version:
 *
 */
public class socket2 {

	
	
	/**
	 * 
	 * Desc :
	 * @Method Name : main
	 * @param argv
	 *
	 */
    public static void main(String[] argv) {
    	Socket socket = null;
    	BufferedOutputStream bos = null;
    	BufferedInputStream bis = null;
    	         
        try {
        	socket = new Socket("150.183.234.168",8197);
        	        
        	// server -> client 출력용 스트림
        	bos = new BufferedOutputStream(socket.getOutputStream());        	        	
        	byte[] b = new byte[1024];
        	b = "abc".getBytes();
        	bos.write(b);
        	bos.flush();
        	
              	
        	
        	//Client--> server 입력용 스트림        	
        	bis = new BufferedInputStream(socket.getInputStream());        	        	        	
        	byte[] c = new byte[1024];
                system.out.println(c);


//       또다른 방법
//        	int s = 0;
//        	while ( (s = bis.read()) != -1) {
//				System.out.print((char)s);				
//		}


        	bos.close();
        	bis.close();
        	socket.close();
            

        } catch (UnknownHostException e) {
            System.out.println("Unkonw exception " + e.getMessage());

        } catch (IOException e) {
            System.out.println("IOException caught " + e.getMessage());
            e.printStackTrace();
        } 
    }

	
}