package org.ACMSviet.SchedulerAMa.Models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RepeatitionUnit {

	private int weekDay;
	private int lectureNo;
	
	public RepeatitionUnit() {
		//blank Constructor for json or xml auto conversion.
	}

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
	
	//chained methods..
	public RepeatitionUnit addWeekDay(int weekDay) {
		this.weekDay = weekDay;
		return this;
	}
	
	public RepeatitionUnit addLectureNo(int lectureNo) {
		this.lectureNo = lectureNo;
		return this;
	}
	
}
