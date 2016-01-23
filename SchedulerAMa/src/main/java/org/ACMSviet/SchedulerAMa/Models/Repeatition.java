package org.ACMSviet.SchedulerAMa.Models;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//the above inheritance annotation tells to hibernate that this object is to be inherited by other classes and the strategy yo inherite is single table,
// e.i, create single table for the parent and the child classes. single_table is by default value, so if we only require it, no need for this annotation.
@XmlRootElement
public class Repeatition {
	@Id @GeneratedValue
	private int id;
	private int weekDay;
	private int lectureNo;
	


	@ManyToOne
	private Course course;
	
	public Repeatition() {
		// blank constructor for json or xml response generation.
	}

	// getters n setters..
	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getLectureNo() {
		return lectureNo;
	}

	public void setLectureNo(int lectureNo) {
		this.lectureNo = lectureNo;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	//Chained Methods..


	public Repeatition addWeekDay(int weekDay) {
		this.weekDay = weekDay;
		return this;
	}
	
	public Repeatition addLectureNo(int lectureNo) {
		this.lectureNo = lectureNo;
		return this;
	}
	
	public Repeatition addCourse(Course course) {
		this.course = course;
		return this;
	}
	
	public Repeatition addId(int id) {
		this.id = id;
		return this;
	}

}
