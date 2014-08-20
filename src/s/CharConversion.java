package s;
/*
 * 참고 사이트 : javaservice.net
 * http://www.javaservice.com/~java/bbs/read.cgi?m=&b=javatip&c=r_p&n=956208406&p=15&s=t
 * 
 */
/**
 * 이 유형은 VisualAge에서 작성되었습니다.
 */
import java.io.UnsupportedEncodingException; 
public class CharConversion {
/**
 * CharConversion 생성자 주석.
 */
public CharConversion() {
	super();
}
	//NOTE: Oracle 8이 되면서 Character Set를 바꿔 줄 필요가 없어졌다.
	
	public static synchronized String E2K( String english )
	{
		String korean = null;
	
		//if (english == null ) return null;
		if (english == null ) return "";
		try { 
			korean = new String(new String(english.getBytes("8859_1"), "KSC5601"));
		}
		catch( UnsupportedEncodingException e ){
			korean = new String(english);
		}
		return korean;
	}
	public static synchronized String K2E( String korean )
	{
		String english = null;
		
		//if (korean == null ) return null;
		if (korean == null ) return "";
		english = new String(korean);
		try { 
			english = new String(new String(korean.getBytes("KSC5601"), "8859_1"));
		}
		catch( UnsupportedEncodingException e ){
			english = new String(korean);
		}
		return english;
	}
}