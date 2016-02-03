package org.ACMSviet.SchedulerAMa.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AndroidDeviceList implements Serializable {

	@Embedded
	private DSS dss;
	
	@Id
	private String deviceId;
	
	public AndroidDeviceList() {}

	public DSS getDss() {
		return dss;
	}

	public void setDss(DSS dss) {
		this.dss = dss;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	//Chained Methods..
	public AndroidDeviceList addDss(DSS dss) {
		this.dss = dss;
		return this;
	}
	
	public AndroidDeviceList addDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}
	
}
