package model;

import java.io.Serializable;

public class gamerInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Name;
	public byte[] address;
	public int Port;
	@Override
	public String toString(){
		String add="";
		if (address == null)
			return Name;
		for (byte b : address) {
			add+=String.valueOf(b)+".";
		}
		add = add.substring(0,add.length()-1);
		return String.format("%s@%s", Name,add);
	}
	
}
