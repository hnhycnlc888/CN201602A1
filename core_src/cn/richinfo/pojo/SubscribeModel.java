package cn.richinfo.pojo;

public class SubscribeModel {
	//手机号码
	private String usermobile;
	//省份
	private int provcode;
	//地区
	private int areacode;
	//loginid
	private long loginid;
	//ip
	private String ipaddress;
	//杂志ID
	private int magid;
	//来源
	private String comefrom;
	
	public String getUsermobile() {
		return usermobile;
	}
	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}
	public int getProvcode() {
		return provcode;
	}
	public void setProvcode(int provcode) {
		this.provcode = provcode;
	}
	public int getAreacode() {
		return areacode;
	}
	public void setAreacode(int areacode) {
		this.areacode = areacode;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public int getMagid() {
		return magid;
	}
	public void setMagid(int magid) {
		this.magid = magid;
	}
	public String getComefrom() {
		return comefrom;
	}
	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}
	
	public void setLoginId(long logid){
		this.loginid = logid;
	}
	
	public long getLoginId(){
		return loginid;
	}
}
