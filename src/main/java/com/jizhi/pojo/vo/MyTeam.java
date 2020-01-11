package com.jizhi.pojo.vo;

import java.io.Serializable;
import java.util.List;

public class MyTeam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer teamNum;//团队人数
	private Double teamProfit;//团队总收益
	private Integer level_1_num;//直推用户人数
	private Integer level_2_num;//二代会员人数
	private Integer level_3_num;//三代会员人数
	private List<TeamMate> teamMates;
	public Integer getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	public Double getTeamProfit() {
		return teamProfit;
	}
	public void setTeamProfit(Double teamProfit) {
		this.teamProfit = teamProfit;
	}
	public Integer getLevel_1_num() {
		return level_1_num;
	}
	public void setLevel_1_num(Integer level_1_num) {
		this.level_1_num = level_1_num;
	}
	public Integer getLevel_2_num() {
		return level_2_num;
	}
	public void setLevel_2_num(Integer level_2_num) {
		this.level_2_num = level_2_num;
	}
	public Integer getLevel_3_num() {
		return level_3_num;
	}
	public void setLevel_3_num(Integer level_3_num) {
		this.level_3_num = level_3_num;
	}
	public List<TeamMate> getTeamMates() {
		return teamMates;
	}
	public void setTeamMates(List<TeamMate> teamMates) {
		this.teamMates = teamMates;
	}
	
	
}
