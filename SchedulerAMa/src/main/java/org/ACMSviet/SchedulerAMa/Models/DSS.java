package org.ACMSviet.SchedulerAMa.Models;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement @Embeddable
public class DSS implements Serializable{
	String dept;
	String sem;
	String section;
	
	public DSS(){}
	
	
	
	public DSS(String dept, String sem, String section) {
		super();
		this.dept = dept;
		this.sem = sem;
		this.section = section;
	}



	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getSem() {
		return sem;
	}
	public void setSem(String sem) {
		this.sem = sem;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	//Chained Methods..
	public DSS addDept(String dept) {
		this.dept = dept;
		return this;
	}
	
	public DSS addSem(String sem) {
		this.sem = sem;
		return this;
	}
	
	public DSS addSection(String section) {
		this.section = section;
		return this;
	}	
}
