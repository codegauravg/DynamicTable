package org.ACMSviet.SchedulerAMa.Services;

import java.util.ArrayList;
import java.util.List;

import org.ACMSviet.SchedulerAMa.Models.Course;
import org.ACMSviet.SchedulerAMa.Models.CourseListResponse;
import org.ACMSviet.SchedulerAMa.Models.DSS;
import org.ACMSviet.SchedulerAMa.Models.DSSModificationLog;
import org.ACMSviet.SchedulerAMa.Models.Repeatition;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionListResponse;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionUnit;
import org.ACMSviet.SchedulerAMa.Models.ResponseReport;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
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
	 * -> Code Repeation adding,updating and deletion services. (also check for no duplicacy of repeatitions in same DSS, different types are excluded) *done*
	 * -> Create a service for getting all repeatitions(temp type course overlapping the main type course) of a specific DSS. use hibernate EXAMPLE API.*done*
	 * -> Views:
	 * 	-> filter by class.*done*
	 * 	-> filter by faculty.*done*
	 * -> add error control in addingCourse functions.*done*
	 * 
	 * -> error in schedule function, giving duplicate data sets.*done*
	 * 
	 * -> create a service to generate course options available for a specific lecture.
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
		if(course.getName().isEmpty()||course.getFaculty().isEmpty()||course.getType().isEmpty()||course.getDept().isEmpty()||course.getSem().isEmpty()||course.getSection().isEmpty()) {
			return new ResponseReport().addStatus(addFailed).addError("Required fields : Name, Faculty, Type, Dept, Sem, Section.");
		}
		else if(getCourseByName(course.getName()).getStatus().equals(statusOK)) {
			return new ResponseReport().addStatus(addFailed).addError("Course Already exists.");
		}
		this.sessionFactory.getCurrentSession().save(course);
		notifyModification(course);	
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
			ArrayList<Course> courses = (ArrayList<Course>) getCourseByName(course.getName()).getCourses();
			if(courses.isEmpty()) {
				return new ResponseReport().addStatus(updateFailed).addError("No Such Course Found.");}
			courses.get(0)
				.addDept(course.getDept())
				.addDescription(course.getDescription())
				.addFac_contact(course.getFac_contact())
				.addFaculty(course.getFaculty())
				.addRefBook(course.getRefBook())
				.addSection(course.getSection())
				.addSem(course.getSem())
				.addtRefBookLink(course.getRefBookLink())
				.addType(course.getType());
			this.sessionFactory.getCurrentSession().update(courses.get(0));
			notifyModification(course);
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
			notifyModification(courses.get(0));
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
				notifyModification(rep.getCourse());
			}
			
			return new ResponseReport().addStatus(deleteOK);
		}catch(Exception e) {
			return new ResponseReport().addStatus(deleteFailed).addError("No Repeatitions found for mentioned Course.");
		}
	}
	
	//function: add repeatition from a course.
	public ResponseReport addRepeatitions(String name,int weekDay,int lectureNo) {
		try {
			
			if(weekDay>5||lectureNo>7||weekDay<1||lectureNo<1) {
				return new ResponseReport().addStatus(addFailed).addError("Range for weekDay : 1 - 5 & lectureNo : 1 -7");
			}
			
			Course course = getCourseByName(name).getCourses().get(0);
			
			ArrayList<Repeatition> repeatitions = (ArrayList<Repeatition>)this.sessionFactory.getCurrentSession().createCriteria(Repeatition.class)
					.add(
							//finding repeatition schedule similarities
							Restrictions.and(Restrictions.eq("weekDay", weekDay),Restrictions.eq("lectureNo", lectureNo))
						).list();
			
			if(!repeatitions.isEmpty()) {
				
				for(Repeatition travRep : repeatitions) {
					if(travRep.getCourse().getDept().equals(course.getDept())&&travRep.getCourse().getSem().equals(course.getSem())&&
							travRep.getCourse().getSection().equals(course.getSection())&&travRep.getCourse().getType().equals(course.getType())) {
						return new ResponseReport().addStatus(addFailed).addError("Similar Repeatition is available in the Schedule.");
					}
				}
				this.sessionFactory.getCurrentSession().save(new Repeatition().addWeekDay(weekDay).addLectureNo(lectureNo).addCourse(course));
				notifyModification(course);
				return new ResponseReport().addStatus(updateOK);
	
			}
			else {
				this.sessionFactory.getCurrentSession().save(new Repeatition().addWeekDay(weekDay).addLectureNo(lectureNo).addCourse(course));
				notifyModification(course);
				return new ResponseReport().addStatus(updateOK);
			}
		}catch(Exception e) {
			return new ResponseReport().addError(addFailed).addError("No Such Course Found.");
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
		if(!type.equals("main")&&!type.equals("temp")) {
			return new CourseListResponse().addStatus(statusFailed).addError("Wrong Course Type entered.");
		}
		
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
	
	//function: Delete Unique Repeatition for a mentioned course
	public ResponseReport deleteUniqueRepeatitionByCourseName(String name,int weekDay,int lectureNo) {
		
		if(weekDay>5||lectureNo>7||weekDay<1||lectureNo<1) {
			return new ResponseReport().addStatus(deleteFailed).addError("Range for weekDay : 1 - 5 & lectureNo : 1 -7");
		}
		
		try {
			Course course = getCourseByName(name).getCourses().get(0);
			Repeatition repeatition = (Repeatition) this.sessionFactory.getCurrentSession().createCriteria(Repeatition.class)
					.add(Restrictions.and(Restrictions.eq("course", course),Restrictions.and(
							Restrictions.eq("weekDay", weekDay), Restrictions.eq("lectureNo", lectureNo)) )).uniqueResult();
			if(repeatition==null) {
				return new ResponseReport().addStatus(deleteFailed).addError("No Such Repeatition Found for mentioned Course.");
			}
			
			this.sessionFactory.getCurrentSession().delete(repeatition);
			notifyModification(course);
			return new ResponseReport().addStatus(deleteOK);
			
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseReport().addStatus(deleteFailed).addError("No such Course Found.");
			
		}
	}
	
	//function: get Repeatitions by Course type.
	public RepeatitionListResponse getRepeatitionsByCourseType(String type) {
		if(!type.equals("main")&&!type.equals("temp")){return new RepeatitionListResponse().addStatus(statusFailed).addError("Unsupported Course type entered.");}
		try {
			ArrayList<Repeatition> repeatitions = (ArrayList<Repeatition>) this.sessionFactory.getCurrentSession().createCriteria(Repeatition.class).list();
			if(!repeatitions.isEmpty()) {
				ArrayList<Repeatition> repeatitionList = new ArrayList<Repeatition>();
				for(Repeatition repeatition : repeatitions ) {
					if(repeatition.getCourse().getType().equals(type)) {
						repeatitionList.add(repeatition);
					}
				}
				
				if(!repeatitionList.isEmpty()) {return new RepeatitionListResponse().addRepeatitions(repeatitionList).addStatus(statusOK);}
				else {return new RepeatitionListResponse().addStatus(statusFailed).addError("No repeatitions for mentioned Type.");}
			}else {
				return new RepeatitionListResponse().addStatus(statusFailed).addError("Repeatitions List Empty.");
			}
		}catch(Exception e) {
			return new RepeatitionListResponse().addStatus(statusFailed).addError("No repeatitions Found");
		}	
	}
	
	//function: Get all unique repeatitions of a specific DSS(Where temp course overlaps main course).
	//TODO: optimize this function to perform lesser iterations.
	//TODO: this code crashes if there is no temp or main course individually. FIX THIS.
	public RepeatitionListResponse getScheduleForDSS(String dept,String sem,String section) {
		ArrayList<Repeatition> tempRepeatitions = (ArrayList<Repeatition>) getRepeatitionsByCourseType("temp").getRepeatitions();
		ArrayList<Repeatition> mainRepeatitions = (ArrayList<Repeatition>) getRepeatitionsByCourseType("main").getRepeatitions();
		ArrayList<Repeatition> scheduleList = new ArrayList<Repeatition>();
		
		try {
			if(mainRepeatitions.isEmpty()) {}
			if(tempRepeatitions.isEmpty()) {}
		}catch(Exception e) {
			return new RepeatitionListResponse().addStatus(statusFailed).addError("No Temp or Main Course Found.");		
		}
		
		for(int weekDay=1;weekDay<=5;weekDay++) {
			for(int lectureNo=1;lectureNo<=7;lectureNo++) {
				//for checking if a temp course for this repeatition is found.
				boolean tempGet = false;
				if(!tempRepeatitions.isEmpty()) {
					//iteration for adding temp courses in scheduleList.
					for(Repeatition tempRep : tempRepeatitions) {
						if(tempRep.getWeekDay()==weekDay&&tempRep.getLectureNo()==lectureNo&&
						tempRep.getCourse().getDept().equals(dept)&&tempRep.getCourse().getSem().equals(sem)&&tempRep.getCourse().getSection().equals(section)) {
							scheduleList.add(tempRep);
							tempGet=true;
						}
					}
				}
				//if tempCourse is found, 
				if(tempGet) {continue;}
				if(!mainRepeatitions.isEmpty()) {
					//iteration for adding main courses in scheduleList.
					for(Repeatition mainRep : mainRepeatitions) {
						if(mainRep.getWeekDay()==weekDay&&mainRep.getLectureNo()==lectureNo&&
						mainRep.getCourse().getDept().equals(dept)&&mainRep.getCourse().getSem().equals(sem)&&mainRep.getCourse().getSection().equals(section)) {
							scheduleList.add(mainRep);
						}
					}
				}
			}
		}
		
		if(!scheduleList.isEmpty()) {return new RepeatitionListResponse().addRepeatitions(scheduleList).addStatus(statusOK); }
		else{
			return new RepeatitionListResponse().addStatus(statusFailed).addError("Schedule for selected category is empty.");
		}
	}

		//function: Get all unique repeatitions of a specific DSS and weekDay(Where temp course overlaps main course).
		//TODO: optimize this function to perform lesser iterations.
		//TODO: this code crashes if there is no temp or main course individually. FIX THIS.
		public RepeatitionListResponse getScheduleForDSSWeekDay(String dept,String sem,String section,int weekDay) {
			if(weekDay<1||weekDay>5) {
				return new RepeatitionListResponse().addStatus(statusFailed).addError("Range for weekDay : 1 - 5");
			}
			
			ArrayList<Repeatition> tempRepeatitions = (ArrayList<Repeatition>) getRepeatitionsByCourseType("temp").getRepeatitions();
			ArrayList<Repeatition> mainRepeatitions = (ArrayList<Repeatition>) getRepeatitionsByCourseType("main").getRepeatitions();
			ArrayList<Repeatition> scheduleList = new ArrayList<Repeatition>();
			
			try {
				if(mainRepeatitions.isEmpty()) {}
				if(tempRepeatitions.isEmpty()) {}
			}catch(Exception e) {
				return new RepeatitionListResponse().addStatus(statusFailed).addError("No Temp or Main Course Found.");		
			}
			
			
				for(int lectureNo=1;lectureNo<=7;lectureNo++) {
					//for checking if a temp course for this repeatition is found.
					boolean tempGet = false;
					if(!tempRepeatitions.isEmpty()) {
						//iteration for adding temp courses in scheduleList.
						for(Repeatition tempRep : tempRepeatitions) {
							if(tempRep.getWeekDay()==weekDay&&tempRep.getLectureNo()==lectureNo&&
							tempRep.getCourse().getDept().equals(dept)&&tempRep.getCourse().getSem().equals(sem)&&tempRep.getCourse().getSection().equals(section)) {
								scheduleList.add(tempRep);
								tempGet=true;
							}
						}
					}
					
					//if tempCourse is found, 
					if(tempGet) {continue;}
					if(!mainRepeatitions.isEmpty()) {
						//iteration for adding main courses in scheduleList.
						for(Repeatition mainRep : mainRepeatitions) {
							if(mainRep.getWeekDay()==weekDay&&mainRep.getLectureNo()==lectureNo&&
							mainRep.getCourse().getDept().equals(dept)&&mainRep.getCourse().getSem().equals(sem)&&mainRep.getCourse().getSection().equals(section)) {
								scheduleList.add(mainRep);
							}
						}
					}
				}
			
			if(!scheduleList.isEmpty()) {return new RepeatitionListResponse().addRepeatitions(scheduleList).addStatus(statusOK); }
			else{
				return new RepeatitionListResponse().addStatus(statusFailed).addError("Schedule for selected category is empty.");
			}
		}
		
		//function: generate available course options for a specific lecture of a class.
		public CourseListResponse getCourseOptions(String dept,String sem,String section,int weekDay,int lectureNo) {
			try {
				ArrayList<Course> allCourses = (ArrayList<Course>) getAllCourses().getCourses();
				for(Course course : allCourses){
					//TODO Add functionality.
				}
				
			}catch(Exception e) {}
			
			return null;
		}
		
		//function: Add a list of repeatitions to a specific Course as a bundle.
		public ResponseReport addRepeatitionListToCourseByName(String name,ArrayList<RepeatitionUnit> repeatitions) {
			for(RepeatitionUnit repeat : repeatitions) {
			
				if(addRepeatitions(name, repeat.getWeekDay(), repeat.getLectureNo()).getStatus().equals(addFailed)) {
					return new ResponseReport().addStatus(addFailed).addError("Repeatition Add error occured.");
				}
				
			}
			return new ResponseReport().addStatus(addOK);
			
		}
		
		//DSS services
		
		//function: create a new Dept Sem Section modification log unit for every new DSS value.
		public void createDSSModLog(DSS dss) {
			this.sessionFactory.getCurrentSession().save(new DSSModificationLog().addModifiedCount(0)
					.addDss(dss));
		}
		
		//function: get the DSS modification Log foor a specific DSS value.(Primary Key)
		public DSSModificationLog getDSSModLog(DSS dss) {
			return (DSSModificationLog) this.sessionFactory.getCurrentSession().get(DSSModificationLog.class, dss);
		}
		
		//function: add new log modification increment for any new change in the specific DSS.
		public void DSSModLogInc(DSS dss) {
			DSSModificationLog dssmodlog = (DSSModificationLog) this.sessionFactory.getCurrentSession().get(DSSModificationLog.class, dss);
			dssmodlog.setModifiedCount(dssmodlog.getModifiedCount()+1);
			this.sessionFactory.getCurrentSession().update(dssmodlog);
			System.out.println(TAG+"DSSModification Increment Implemented.");
		}	
		
		//function: Modification notification to DSS service for logging the modification counter.
		public void notifyModification(Course course) {
			try {
				getDSSModLog(
						new DSS().addDept(course.getDept()).addSection(course.getSection()).addSem(course.getSem())
						);
				DSSModLogInc(
						new DSS().addDept(course.getDept()).addSection(course.getSection()).addSem(course.getSem())
						);
			}catch(Exception e) {
					createDSSModLog(
							new DSS().addDept(course.getDept()).addSection(course.getSection()).addSem(course.getSem())
							);
				
			}
		}
}
