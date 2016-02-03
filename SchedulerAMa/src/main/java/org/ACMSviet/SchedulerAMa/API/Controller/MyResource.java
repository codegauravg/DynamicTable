package org.ACMSviet.SchedulerAMa.API.Controller;

import java.util.ArrayList;

import org.ACMSviet.SchedulerAMa.Models.Content;
import org.ACMSviet.SchedulerAMa.Models.Course;
import org.ACMSviet.SchedulerAMa.Models.CourseListResponse;
import org.ACMSviet.SchedulerAMa.Models.DSS;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionListResponse;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionUnit;
import org.ACMSviet.SchedulerAMa.Models.ResponseReport;
import org.ACMSviet.SchedulerAMa.Services.CourseService;
import org.ACMSviet.SchedulerAMa.Services.Post2Gcm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyResource {
	@Autowired private CourseService courseService;

	private String sch_auth_token = "thehehelllord";
	
    /*
     * All fetch Services below.
     */
	
	@RequestMapping(value="/API/options/{dept}/{sem}/{section}/{weekDay}/{lectureNo}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public CourseListResponse getCourseOptions(@PathVariable("dept") String dept,@PathVariable("sem") String sem,@PathVariable("section") String section
			,@PathVariable("weekDay") int weekDay,@PathVariable("lectureNo") int lectureNo)	{
		return courseService.getCourseOptions(dept, sem, section, weekDay, lectureNo);
	}
	
	@RequestMapping(value="/API/schedule/{dept}/{sem}/{section}/{weekDay}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public RepeatitionListResponse getScheduleForDSSweekDayService(@PathVariable("dept")String dept,
			@PathVariable("sem")String sem,@PathVariable("section")String section,@PathVariable("weekDay") int weekDay) {
		return courseService.getScheduleForDSSWeekDay(dept, sem, section,weekDay);
	}
	
  
  @RequestMapping(value="/API/course",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getAllCoursesService() {
	  return courseService.getAllCourses();
  }
    

  @RequestMapping(value="/API/course/{name}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getCourseByNameService(@PathVariable("name") String name) {
	  return courseService.getCourseByName(name);  
	  }
  
  @RequestMapping(value="/API/course/faculty/{faculty}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getCourseByFaculty(@PathVariable("faculty") String faculty) {
	  return courseService.getCourseByFaculty(faculty);
  }
  
  @RequestMapping(value="/API/course/{dept}/{sem}/{section}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getCourseByDSSService(@PathVariable("dept")String dept,
  @PathVariable("sem")String sem,@PathVariable("section")String section) {
	  return courseService.findCoursesByDSS(dept, sem, section);
  }
  
  @RequestMapping(value="/API/course/{dept}/{sem}/{section}/{faculty}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getCourseByDSSFService(@PathVariable("dept")String dept,
  @PathVariable("sem")String sem,@PathVariable("section")String section,@PathVariable("faculty") String faculty) {
	  return courseService.findCoursesByDSSF(dept, sem, section, faculty);
  }
  
  @RequestMapping(value="/API/course/{name}/repeatition",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
  public RepeatitionListResponse getCourseRepetitions(@PathVariable("name") String name) {
	  return courseService.getCourseRepeatitions(name);
  }
  
  @RequestMapping(value="/API/course/main",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getMainCoursesService() {
	  return courseService.getCourseListByType("main");
  }
  
  @RequestMapping(value="/API/course/temp",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
  public CourseListResponse getTempCoursesService() {
	  return courseService.getCourseListByType("temp");
  }
  
	@RequestMapping(value="/API/repeatition/main",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public RepeatitionListResponse getRepeatitionsOfMainCourse() {
		return courseService.getRepeatitionsByCourseType("main");
	}
	
	@RequestMapping(value="/API/repeatition/temp",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public RepeatitionListResponse getRepeatitionsOfTempCourse() {
		return courseService.getRepeatitionsByCourseType("temp");
	}
	
	@RequestMapping(value="/API/schedule/{dept}/{sem}/{section}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public RepeatitionListResponse getScheduleForDSSService(@PathVariable("dept")String dept,
			@PathVariable("sem")String sem,@PathVariable("section")String section) {
		return courseService.getScheduleForDSS(dept, sem, section);
	}
  
	// fetch services end.
	
	/*
	 * All Add Services Below.
	 */

  @RequestMapping(value="/API/course",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseReport addCourseService(@RequestBody Course course) {
	  return courseService.addCourse(course); 
  }
  
  @RequestMapping(value="/API/repeatition/{name}/{weekDay}/{lectureNo}",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseReport addRepeatitionService(@PathVariable("name") String name,@PathVariable("weekDay") int weekDay,
		  @PathVariable("lectureNo")int lectureNo) {
	  return courseService.addRepeatitions(name, weekDay, lectureNo);
  }

  @RequestMapping(value="/API/repeatition/{name}",method=RequestMethod.POST,
		  consumes=MediaType.APPLICATION_JSON_VALUE,
		  produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseReport addRepeatitionListByCourseName(@PathVariable("name") String name,@RequestBody ArrayList<RepeatitionUnit> repeatitions) {
	  return courseService.addRepeatitionListToCourseByName(name, repeatitions);
  }
	@RequestMapping(value="/API/device/{dept}/{sem}/{section}/{ID}",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseReport addAndroidDeviceIdService(@PathVariable("dept") String dept,
			@PathVariable("sem") String sem,@PathVariable("section") String section,@PathVariable("ID") String ID) {
		return courseService.addAndroidDeviceID(dept, sem, section, ID);
	}

    // Add Services end.
    
    /*
     * All Delete Services below.
     */

	  @RequestMapping(value="/API/course/{name}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseReport deleteCourseService(@PathVariable("name") String name) {
		  return courseService.deleteCourseByName(name);
	  }
	  
	  @RequestMapping(value="/API/repeatition/{name}/flushRepeatitions",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseReport flushRepeatitionsByCourseNameService(@PathVariable("name") String name) {
		  return courseService.flushRepeatitionsByCourseName(name);
	  }
	  
		@RequestMapping(value="/API/repeatition/{name}/{weekDay}/{lectureNo}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseReport deleteUniqueRepeatitionByCourseNameService(@PathVariable("name")String name,@PathVariable("weekDay")int weekDay,
				@PathVariable("lectureNo")int lectureNo) {
			return courseService.deleteUniqueRepeatitionByCourseName(name, weekDay, lectureNo);
		}
	  // Delete Services end.
	  
	//update course..
	@RequestMapping(value="/API/course/update",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseReport updateCourseService(@RequestBody Course course) {
		return courseService.updateCourse(course);
	}
	
	
	
	//TODO updated in API SERVICE SPECS till this point.

	@RequestMapping("/API/doit")
	public String doit() {
		Content content = new Content();
		content.addRegId("APA91bHnfLBmW_2Dm_s24587ImTD_Qsf2ETvhlVGgw8gtTZwH50tVTNJOUPdZAej2bgXQPBj-S19jxwq6H5MxhMKMi5-ifiyijSQ3Cgg6m5_WuGhhifKue8Uc_fPcEBsQqVtgbszvcyKgwhvyyw9by3ePEgSBsI8GA");
		content.createData("Gcm Gen Title", "Message by gender prediction api.");
				
		return Post2Gcm.post("AIzaSyCZeYrZrX6IV_k_M2A_PcPhp8Pu284zFpw", content);
	}
	

	
}
