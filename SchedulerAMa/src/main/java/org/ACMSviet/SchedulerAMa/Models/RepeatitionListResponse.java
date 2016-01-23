package org.ACMSviet.SchedulerAMa.Models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RepeatitionListResponse {

	private String status;
	private String error;
	private List<Repeatition> repeatitions;
	
	public RepeatitionListResponse() {
		// blank constructor for auto xml or json conversion service..
	}

	//getters & setters...
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Repeatition> getRepeatitions() {
		return repeatitions;
	}

	public void setRepeatitions(List<Repeatition> repeatitions) {
		this.repeatitions = repeatitions;
	}
	
	//Chained Methods..
	public RepeatitionListResponse addStatus(String status) {
		this.status = status;
		return this;
	}
	
	public RepeatitionListResponse addError(String error) {
		this.error = error;
		return this;
	}
	
	public RepeatitionListResponse addRepeatitions(List<Repeatition> repeatitions) {
		this.repeatitions = repeatitions;
		return this;
	}

	
}
