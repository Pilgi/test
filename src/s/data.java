package s;

import java.io.Serializable;
import java.util.List;

import s.data.data_structure;

public class data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 225217573093265697L;
	
	String purpose;
	List<data_structure> content;
	
	data(String p)
	{
		purpose = p;
	}
	public boolean addContent(String t, String c) {
		return content.add(new data_structure(t,c));
		}
	
	protected class data_structure implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String type;
		private String content;
		
		
		data_structure(String t, String c)
		{
			type = t;
			content = c;
		}
		
		// read Data
		public String getType()
		{
			return type;
		}
		public String getContent()
		{
			return content;
		}
		public String toString(){
			return "Type : " + getType() + ", content: " + getContent();
		}
		
	}

	
}
