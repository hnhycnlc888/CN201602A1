package cn.richinfo.pojo;

import java.util.Date;

/**
 * 奖品信息
 * @author luhy
 *
 */
public class PrizeInfo {

	private String seqId;//奖品序号，自增序列
	private String projectCode;//项目编号
	private String code;//编号
	private String title;//奖品名称
	private int total;//奖品数量
	private long remain;//奖品剩余数量
	private int kind;//奖品类型：1 - 实物；2 - 话费；3 - 虚拟物品；4 -  积分；.... 5，杂志礼包 等（可自定义） 
	private String mailTemp;//邮件模板地址，如果为空则下发中奖邮件
	private Date endTime;//结束时间，为空不限制
	private Date startTime;//开始时间，为空不限制
	private int repeatcode;//是否可以重复中奖：0 - 不限中奖次数；1 - 不可重复中奖（活动期仅限一次）；2 - 每月仅限一次；3 - 每日仅限一次；
	private long minRegion;//中奖概率 计算
	private long maxRegion;//中奖概率 计算
	private double region;//中奖概率 用户填写
	private String picName;//图片地址
	private String magazineIds;//奖品获得的杂志id,多个用,隔开 可为空
	private int priority;//优先级
	private int personNum;//实际参与人数到达多少时才放入奖池
	
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String getMagazineIds() {
		return magazineIds;
	}
	public void setMagazineIds(String magazineIds) {
		this.magazineIds = magazineIds;
	}
	public String getSeqId() {
		return seqId;
	}
	
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getMailTemp() {
		return mailTemp;
	}
	public void setMailTemp(String mailTemp) {
		this.mailTemp = mailTemp;
	}

	public long getRemain() {
		return remain;
	}

	public void setRemain(long remain) {
		this.remain = remain;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getRepeatcode() {
		return repeatcode;
	}

	public void setRepeatcode(int repeatcode) {
		this.repeatcode = repeatcode;
	}

	public double getRegion() {
		return region;
	}
	public void setRegion(double region) {
		this.region = region;
	}
	public long getMinRegion() {
		return minRegion;
	}
	public void setMinRegion(long minRegion) {
		this.minRegion = minRegion;
	}
	public long getMaxRegion() {
		return maxRegion;
	}
	public void setMaxRegion(long maxRegion) {
		this.maxRegion = maxRegion;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
}
