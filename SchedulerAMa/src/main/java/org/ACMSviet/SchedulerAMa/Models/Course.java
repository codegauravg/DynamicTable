package org.ACMSviet.SchedulerAMa.Models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Course {
	
	@Id
	private String name;
	
	//base type of Course : main / temp
	private String type;
	private String description;
	private String faculty;
	private String fac_contact;
	private String dept,sem,section;


	private String refBook;
	private String refBookLink;
	
	public Course() {
		// blank constructor for json and xml auto generation for web response.
	}

	//getters and setters..
	public String getName() {
		return name;
	}
	

	public String getRefBook() {
		return refBook;
	}

	public void setRefBook(String refBook) {
		this.refBook = refBook;
	}

	public String getRefBookLink() {
		return refBookLink;
	}

	public void setRefBookLink(String refBookLink) {
		this.refBookLink = refBookLink;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getFac_contact() {
		return fac_contact;
	}

	public void setFac_contact(String fac_contact) {
		this.fac_contact = fac_contact;
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


	//Chained methods..
	
	public Course addName(String name) {
		this.name = name;
		return this;
	}

	public Course addDescription(String description) {
		this.description = description;
		return this;
	}
	
	public Course addFaculty(String faculty) {
		this.faculty = faculty;
		return this;
	}
	
	public Course addFac_contact(String fac_contact) {
		this.fac_contact = fac_contact;
		return this;
	}
	
	public Course addDept(String dept) {
		this.dept = dept;
		return this;
	}
	
	public Course addSem(String sem) {
		this.sem = sem;
		return this;
	}
	
	public Course addSection(String section) {
		this.section = section;
		return this;
	}

	
	public Course addType(String type) {
		this.type = type;
		return this;
	}
	
	public Course addRefBook(String refBook) {
		this.refBook = refBook;
		return this;
	}
	
	public Course addtRefBookLink(String refBookLink) {
		this.refBookLink = refBookLink;
		return this;
	}
}
