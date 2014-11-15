package s;

import java. util.Properties ;

import javax. mail.Authenticator ;
import javax. mail.Message ;
import javax. mail.MessagingException ;
import javax. mail.PasswordAuthentication ;
import javax. mail.Session ;
import javax. mail.Transport ;
import javax. mail.internet .InternetAddress;
import javax. mail.internet .MimeMessage;
import javax. mail.internet .MimeUtility;

public class Gmail extends Authenticator {
	Message msg = null;
	//보내는 서버 주소
	String host = "smtp.gmail.com";
	//받는사람 이메일 주소
	String from = "cafe1944mailsender@gmail.com"; 
	//보내는사람 이름
	String fromName = "CAFE1944" ; 
	//받는사람 이메일주소
   String to = null;
   Gmail(String toEmail,String toName)
   {
	   to = toEmail; 
	   try {
            Properties props = new Properties();
            props.put ("mail.smtp.starttls.enable", "true");
            props.put ("mail.transport.protocol", "smtp");
            props.put ("mail.smtp.host", host);
            props.setProperty ("mail.smtp.socketFactory.class",  "javax.net.ssl.SSLSocketFactory" );
            props.put ("mail.smtp.port", "465");
            props.put ("mail.smtp.user", from);
            props.put ("mail.smtp.auth", "true");


            //보내는사람의 STMP 로그인을 통한 Session 정보 취득
            Session mailSession = Session.getInstance (props,
                   new javax.mail. Authenticator() {
                       protected PasswordAuthentication getPasswordAuthentication () {
                           return new PasswordAuthentication("cafe1944mailsender@gmail.com","cafe1994admin");
                       }
                   });
            msg = new MimeMessage(mailSession );
            msg.setFrom (new InternetAddress(from , MimeUtility.encodeText (
                   fromName, "UTF-8", "B")));//

/*            //메일을 동시에 여러명을 보내고 싶을때는 배열로 이메일 주소를 작성한다.
            InternetAddress[] address1 = { new InternetAddress (to) };
            msg.setRecipients (Message. RecipientType.TO , address1);
            */


        } catch (MessagingException ex) {
        	ex.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
     }
    
    /**
     * 본문내용 작성
     * @return string 내용
     */
     public void send(String subject, String content){
    	 try {
    		 //메일 제목 설정
    		 msg.setSubject (subject); //제목
    		 msg.setSentDate (new java.util.Date());//보내는날짜
    		 msg.setContent ("CAFE1994 에서 보내는 메일입니다.<br>" + content + "<br><br> - CAFE 1944", "text/html;charset=euc-kr" ); //본문내용 보내기
    		 Transport.send (msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //만들어진 이메일 전송실행
     }
}