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
	private Integer activedNum;//活跃人数
	private Integer activeNum;//激活人数
	private Integer unActiveNum;//未激活人数
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
	public Integer getActivedNum() {
		return activedNum;
	}
	public void setActivedNum(Integer activedNum) {
		this.activedNum = activedNum;
	}
	public Integer getActiveNum() {
		return activeNum;
	}
	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
	}
	public Integer getUnActiveNum() {
		return unActiveNum;
	}
	public void setUnActiveNum(Integer unActiveNum) {
		this.unActiveNum = unActiveNum;
	}
	public List<TeamMate> getTeamMates() {
		return teamMates;
	}
	public void setTeamMates(List<TeamMate> teamMates) {
		this.teamMates = teamMates;
	}
	
	
}
