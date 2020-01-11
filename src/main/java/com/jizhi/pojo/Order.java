package com.jizhi.pojo;
/**
 * 订单类
 * @author Administrator
 *
 */
public class Order {
	private Integer id;
	private Integer userId;
	private Integer animalId;
	private String date;
	private String time;
	private Integer state;//预约状态，0失败，1预约中，2成功
	private Integer role;//1表示是管理员的，0表示普通玩家
	private Integer flag;//0表示未删除，1表示已删除
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Integer animalId) {
		this.animalId = animalId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
