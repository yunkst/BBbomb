package model;

import java.io.Serializable;

public class gamerInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Name;
	public byte[] address;
	//当端口号等于0时，表明拒绝邀请
	public int Port;
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof gamerInfo)
		{
			gamerInfo tGamerInfo = (gamerInfo)obj;

			if (Port==tGamerInfo.Port){
				for (int i=0;i<address.length;i++) {
					if (address[i]!=tGamerInfo.address[i])
						return false;
				}
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	};
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
