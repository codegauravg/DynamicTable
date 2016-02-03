package org.ACMSviet.SchedulerAMa.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class AndroidDeviceList implements Serializable {

	@EmbeddedId
	private DSS dss;
	
	private HashSet<String> deviceIdList;
	
	public AndroidDeviceList() {}

	public DSS getDss() {
		return dss;
	}

	public void setDss(DSS dss) {
		this.dss = dss;
	}

	public HashSet<String> getDeviceIdList() {
		return deviceIdList;
	}

	public void setDeviceIdList(HashSet<String> deviceIdList) {
		this.deviceIdList = deviceIdList;
	}
	
	//Chained Methods..
	public AndroidDeviceList addDss(DSS dss) {
		this.dss = dss;
		return this;
	}
	
	public AndroidDeviceList addDeviceIdList(HashSet<String> deviceIdList) {
		this.deviceIdList = deviceIdList;
		return this;
	}
	
}
