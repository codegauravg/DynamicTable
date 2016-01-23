package org.ACMSviet.SchedulerAMa.Models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseListResponse {

	private String status;
	private String error;
	private List<Course> courses;
	
	public CourseListResponse() {
		// for json or xml response conversion auto.
	}

	
	//getters n setters..
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

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	//Chained methods..
	
	public CourseListResponse addStatus(String status) {
		this.status = status;
		return this;
	}
	
	public CourseListResponse addError(String error) {
		this.error = error;
		return this;
	}
	
	public CourseListResponse addCourses(List<Course> courses) {
		this.courses = courses;
		return this;
	}
}
