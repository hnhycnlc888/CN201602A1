package cn.richinfo.pojo;

public class SendMailModel
{
	// 中奖手机号码
	private String usermobile;
	// 省份
	private int provcode;
	// 地区
	private int areacode;
	//IP地址
	private String ipaddress;
	// 来源，分为WEB和WAP
	private String comefrom;	
	// 奖品名称
	private String prizename;
	
	//是否成功
	private int issend;
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
	public String getComefrom() {
		return comefrom;
	}
	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}
	public String getPrizename() {
		return prizename;
	}
	public void setPrizename(String prizename) {
		this.prizename = prizename;
	}
	public int getIssend() {
		return issend;
	}
	public void setIssend(int issend) {
		this.issend = issend;
	}
	
}
