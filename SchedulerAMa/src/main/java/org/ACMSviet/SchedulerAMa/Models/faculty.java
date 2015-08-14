package org.ACMSviet.SchedulerAMa.Models;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.ACMSviet.SchedulerAMa.Models.submodels.Temp_Course;

@Entity
public class Faculty {
	@Id
	private String fac_name;	//faculty name (primary key)
	private String  fac_contact;	//faculty contact number
	@ElementCollection(fetch=FetchType.LAZY)
	private Collection<Course> fac_course;	//faculty courses list
	@ElementCollection(fetch=FetchType.LAZY)
	private Collection<Temp_Course> fac_temp_course;	//faculty modified(temporary) courses list
	
	//getters and setters...
	public String getFac_name() {
		return fac_name;
	}
	public void setFac_name(String fac_name) {
		this.fac_name = fac_name;
	}
	public String getFac_contact() {
		return fac_contact;
	}
	public void setFac_contact(String fac_contact) {
		this.fac_contact = fac_contact;
	}
	public Collection<Course> getFac_course() {
		return fac_course;
	}
	public void setFac_course(Collection<Course> fac_course) {
		this.fac_course = fac_course;
	}
	public Collection<Temp_Course> getFac_temp_course() {
		return fac_temp_course;
	}
	public void setFac_temp_course(Collection<Temp_Course> fac_temp_course) {
		this.fac_temp_course = fac_temp_course;
	}



	
}
