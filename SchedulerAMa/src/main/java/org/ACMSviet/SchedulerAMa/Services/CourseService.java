package org.ACMSviet.SchedulerAMa.Services;

import java.util.ArrayList;
import java.util.List;

import org.ACMSviet.SchedulerAMa.Models.Course;
import org.ACMSviet.SchedulerAMa.Models.CourseListResponse;
import org.ACMSviet.SchedulerAMa.Models.Repeatition;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionListResponse;
import org.ACMSviet.SchedulerAMa.Models.ResponseReport;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseService {
	
	/*
	 * TODO List:
	 * -> Code Update Services. *done*
	 * -> Code Deletion Services. *done*
	 * -> Code Repeation adding,updating and deletion services. (also check for no duplicacy of repeatitions in same DSS, different types are excluded)
	 * -> Create a service for getting all repeatitions(temp type course overlapping the main type course) of a specific DSS. use hibernate EXAMPLE API.
	 * -> Views:
	 * 	-> filter by class.*done*
	 * 	-> filter by faculty.*done*
	 */

	@Autowired
	private SessionFactory sessionFactory;
	
	private String TAG = "[CourseService] : ",
			addOK = "ADD OK",
			statusOK = "STATUS OK",
			deleteOK = "DELETE OK",
			addFailed = "ADD FAILED",
			statusFailed = "STATUS FAILED",
			deleteFailed = "DELETE FAILED",
			updateOK = "UPDATE OK",
			updateFailed = "UPDATE FAILED";
	
	
	//function: add course to the data set.
	public ResponseReport addCourse(Course course) {
		if(course.getName().isEmpty()||course.getFaculty().isEmpty()) {
			return new ResponseReport().addStatus(addFailed).addError("Required fields : Name & Faculty");
		}
		else if(getCourseByName(course.getName()).getStatus().equals(statusOK)) {
			return new ResponseReport().addStatus(addFailed).addError("Course Already exists.");
		}
		
		System.out.println(TAG+"Course Add service called for "+ course.getName());
		this.sessionFactory.getCurrentSession().save(course);
		return new ResponseReport().addStatus(addOK);
	}
	
	//function: gather full list of courses from the data set.
	public CourseListResponse getAllCourses() {
		List<Course> courses = (List<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class).list();
		if(courses.isEmpty()) {
			return new CourseListResponse().addStatus(statusOK).addError("Empty List of Courses.");
		}
		
		return new CourseListResponse().addCourses(courses).addStatus(statusOK);
	
	}
	
	//function: find a course by its name (Primary Key)
	public CourseListResponse getCourseByName(String name) {
		List<Course> courses = (List<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class)
				.add(Restrictions.eq("name", name)).list();
		if(courses.isEmpty()) {
			return new CourseListResponse().addStatus(statusFailed).addError("No such Course found.");
		}
		
		else {
			return new CourseListResponse().addCourses(courses).addStatus(statusOK);
		}
	}
	
	//function: find a course by its faculty 
	public CourseListResponse getCourseByFaculty(String faculty) {
		List<Course> courses = (List<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class)
				.add(Restrictions.eq("faculty", faculty)).list();
		if(courses.isEmpty()) {
			return new CourseListResponse().addStatus(statusFailed).addError("No such Course found.");
		}
		
		else {
			return new CourseListResponse().addCourses(courses).addStatus(statusOK);
		}
	}
	
	//function: gather Courses list according to common Department, Semester and Section values. to get Courses for a unique class hall.
	public CourseListResponse findCoursesByDSS(String dept,String sem,String section) {
		List<Course> courses = (List<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class)
				.add(Restrictions.and(Restrictions.eq("dept", dept), 
						Restrictions.and(Restrictions.eq("sem", sem), Restrictions.eq("section",section)))).list();
		
		if(courses.isEmpty()) {
			return new CourseListResponse().addStatus(statusFailed).addError("No such Course found.");
		}
		else {
			return new CourseListResponse().addCourses(courses).addStatus(statusOK);
		}
	}
	
	//function: gather Courses list according to common Department, Semester, Section and Faculty values. to get Courses for a unique class and faculty hall.
	public CourseListResponse findCoursesByDSSF(String dept,String sem,String section,String faculty) {
			List<Course> courses = (List<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class)
					.add(Restrictions.and(
							Restrictions.and(Restrictions.eq("dept", dept), Restrictions.eq("faculty", faculty))
							, 
							Restrictions.and(Restrictions.eq("sem", sem), Restrictions.eq("section",section))
							)
							).list();
			
			if(courses.isEmpty()) {
				return new CourseListResponse().addStatus(statusFailed).addError("No such Course found.");
			}
			else {
				return new CourseListResponse().addCourses(courses).addStatus(statusOK);
			}
		}
	
	//function: Update the contents of course data..
	public ResponseReport updateCourse(Course course) {
		
		try {
			List<Course> courses = getCourseByName(course.getName()).getCourses();
			this.sessionFactory.getCurrentSession().update(courses.get(0).addFaculty(course.getFaculty()));
			return new ResponseReport().addStatus(updateOK);
			
		}catch(Exception e) {
			return new ResponseReport().addStatus(updateFailed).addError("No such Course Found.");
		}
			
	}
		
	//function: delete course details from the database.(also all child repeatitions)
	public ResponseReport deleteCourseByName(String name) {
		//exception handling for no course found case.
		try {
			List<Course> courses = getCourseByName(name).getCourses();
			System.out.println(TAG+"Course found:"+courses.get(0).getName());
			//exception handing for no repeatition gathered
			try{
				ArrayList<Repeatition> repeatitions = (ArrayList<Repeatition>) getCourseRepeatitions(name).getRepeatitions();
				if(!repeatitions.isEmpty()) {
					for(Repeatition rep : repeatitions) {
						this.sessionFactory.getCurrentSession().delete(rep);
					}
				}
			}catch(Exception e) {
				//Do Nothing..
				System.out.println(TAG+"No Repeatitions associated with mentioned course");
			}
			this.sessionFactory.getCurrentSession().delete(courses.get(0));
			return new ResponseReport().addStatus(deleteOK);
			
		}catch(Exception e) {
			return new ResponseReport().addStatus(deleteFailed).addError("No such Course found.");
		}

	}
	
	//function: delete all repeatitions for course available.
	public ResponseReport flushRepeatitionsByCourseName(String name) {
		try {
			ArrayList<Repeatition> repeatitions = (ArrayList<Repeatition>) getCourseRepeatitions(name).getRepeatitions();
			for(Repeatition rep : repeatitions) {
				this.sessionFactory.getCurrentSession().delete(rep);
			}
			return new ResponseReport().addStatus(deleteOK);
		}catch(Exception e) {
			return new ResponseReport().addStatus(deleteFailed).addError("No Repeatitions found for mentioned Course.");
		}
	}
	
	//function: add repeatition from a course.
	public ResponseReport addRepeatitions(String name,int weekDay,int lectureNo) {
		try {
			this.sessionFactory.getCurrentSession().save(new Repeatition().addWeekDay(weekDay).addLectureNo(lectureNo).addCourse(getCourseByName(name).getCourses().get(0)));
			return new ResponseReport().addStatus(updateOK);
		}catch(Exception e) {
			return new ResponseReport().addStatus(updateFailed).addError("No such Course found.");
		}
	}
	
	//function: get Repeatitions list for a Course.
	public RepeatitionListResponse getCourseRepeatitions(String name){
		try {
			ArrayList<Repeatition> repeatitions = (ArrayList<Repeatition>)this.sessionFactory.getCurrentSession().createCriteria(Repeatition.class)
					.add(Restrictions.eq("course", getCourseByName(name).getCourses().get(0))).list();
			
			if(repeatitions.isEmpty()) {
				return new RepeatitionListResponse().addStatus(statusFailed).addError("No Repeatitions for mentioned course.");
			}else {
				return new RepeatitionListResponse().addStatus(statusOK).addRepeatitions(repeatitions);
			}	
		}catch(Exception e) {
			return new RepeatitionListResponse().addStatus(statusFailed).addError("No such Course found.");
		}
	}
	
	//function: Course list filtered by type
	public CourseListResponse getCourseListByType(String type) {
		try {
			ArrayList<Course> courses = (ArrayList<Course>) this.sessionFactory.getCurrentSession().createCriteria(Course.class).add(Restrictions.eq("type", type)).list();
			if(courses.isEmpty()) {
				return new CourseListResponse().addStatus(statusFailed).addError("No such Course found.");
			}
			return new CourseListResponse().addCourses(courses).addStatus(statusOK);
		}catch(Exception e) {
			return new CourseListResponse().addStatus(statusFailed).addError("Fetch Error Occured.");
		}
	}
		
	
}