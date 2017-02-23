package com.weather.xm.newsbill.model;

/**
 * 新闻审核单
 */
public class NewsBill {
	private Long id;//唯一主键
	private String content;// 新闻内容
	private String planTime;// 计划播报时间
	private String remark;// 备注
	private String userId;// 请假人
	
	private Integer state=0;// 请假单状态 0初始录入,1.开始审批,2为审批完成

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlanTime() {
		return planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
