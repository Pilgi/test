package s;

import java.io.Serializable;
import java.util.ArrayList;

public class data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 225217573093265697L;
	
	String purpose;
	ArrayList<data_structure> content = new ArrayList<data_structure>();
	
	data(String p)
	{
		purpose = p;
	}
	public boolean addContent(String type, String value) {
		data_structure temp = new data_structure(type,value);
		return content.add(temp);
		}
	public boolean deleteContent(int index)
	{
		if(index>=content.size() || index <0)
			return false;
		content.remove(index);
		return true;
	}
	public data_structure getContent(int index)
	{
		if(index>=content.size() || index <0)
			return null;
		return content.get(index);
	}
	

	
	protected class data_structure implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String type;
		private String value;
		
		
		data_structure(String t, String c)
		{
			type = t;
			value = c;
		}
		
		// read Data
		public String getType()
		{
			return type;
		}
		public String getValue()
		{
			return value;
		}
		public String toString(){
			return "Type : " + getType() + ", value: " + getValue();
		}
		
	}

	
}
