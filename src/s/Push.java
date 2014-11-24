package s;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
public class Push {
	private Sender sender = null;
	private String regId = "";
	Push()
	{
		 sender = new Sender("AIzaSyAfuaIjcxwBpX37_UZm6AWl4aTXoDOLaBA");
	}
	public boolean sendMessage(String Id,String content) throws IOException
	{
		regId = Id;
		System.out.println(Id + "\n" +content);
		Message message = new Message.Builder().addData("msg", content).build();
		List<String> list = new ArrayList<String>();
		list.add(regId);
		MulticastResult multiResult = sender.send(message, list, 5);
		if (multiResult != null) {
			List<Result> resultList = multiResult.getResults();
			for (Result result : resultList) {
				System.out.println(result.getMessageId());
			}
		}
		return false;
		
	}
}