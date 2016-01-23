package org.ACMSviet.SchedulerAMa.Models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseReport {

	private String status;
	private String error;
	
	public ResponseReport() {
		// adding json, xml conversion functionality..
	}

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
	
	//Chained Methods..
	public ResponseReport addStatus(String status) {
		this.status = status;
		return this;
	}
	
	public ResponseReport addError(String error) {
		this.error = error;
		return this;
	}
	
}
