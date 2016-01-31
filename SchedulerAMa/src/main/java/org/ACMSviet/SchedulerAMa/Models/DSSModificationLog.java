package org.ACMSviet.SchedulerAMa.Models;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement @Entity
public class DSSModificationLog implements Serializable{

	@EmbeddedId
	private DSS dss;
	
	private int modifiedCount;

	public DSSModificationLog() {}
	
	public DSS getDss() {
		return dss;
	}

	public void setDss(DSS dss) {
		this.dss = dss;
	}

	public int getModifiedCount() {
		return modifiedCount;
	}

	public void setModifiedCount(int modifiedCount) {
		this.modifiedCount = modifiedCount;
	}
	
	//Chained Methods..
	public DSSModificationLog addDss(DSS dss) {
		this.dss = dss;
		return this;
	}
	
	public DSSModificationLog addModifiedCount(int modifiedCount) {
		this.modifiedCount = modifiedCount;
		return this;
	}
	
	
}

