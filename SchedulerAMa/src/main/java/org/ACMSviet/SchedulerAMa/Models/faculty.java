package org.ACMSviet.SchedulerAMa.Models;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.ACMSviet.SchedulerAMa.Models.submodels.course;
import org.ACMSviet.SchedulerAMa.Models.submodels.temp_course;

@Entity
public class faculty {
	@Id
	private String fac_name;	//faculty name (primanry key)
	private int  fac_contact;	//faculty contact number
	private ArrayList<course> fac_course;	//faculty courses list
	private ArrayList<temp_course> fac_temp_course;	//faculty modified(temporary) courses list
	

	// getters and setters...
	public String getFac_name() {
		return fac_name;
	}
	public void setFac_name(String fac_name) {
		this.fac_name = fac_name;
	}
	public int getFac_contact() {
		return fac_contact;
	}
	public void setFac_contact(int fac_contact) {
		this.fac_contact = fac_contact;
	} 
	public ArrayList<course> getFac_course() {
		return fac_course;
	}
	public void setFac_course(ArrayList<course> fac_course) {
		this.fac_course = fac_course;
	}
	public ArrayList<temp_course> getFac_temp_course() {
		return fac_temp_course;
	}
	public void setFac_temp_course(ArrayList<temp_course> fac_temp_course) {
		this.fac_temp_course = fac_temp_course;
	}
	
}
