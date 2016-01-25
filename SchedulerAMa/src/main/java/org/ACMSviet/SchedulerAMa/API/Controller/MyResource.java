package org.ACMSviet.SchedulerAMa.API.Controller;

import org.ACMSviet.SchedulerAMa.Models.Course;
import org.ACMSviet.SchedulerAMa.Models.CourseListResponse;
import org.ACMSviet.SchedulerAMa.Models.RepeatitionListResponse;
import org.ACMSviet.SchedulerAMa.Models.ResponseReport;
import org.ACMSviet.SchedulerAMa.Services.CourseService;
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

	
    /*
     * All fetch Services below.
     */
  
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
    
    // Add Services end.
    
    /*
     * All Delete Services below.
     */

	  @RequestMapping(value="/API/course/{name}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseReport deleteCourseService(@PathVariable("name") String name) {
		  return courseService.deleteCourseByName(name);
	  }
	  
	  @RequestMapping(value="/API/course/{name}/flushRepeatitions",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseReport flushRepeatitionsByCourseNameService(@PathVariable("name") String name) {
		  return courseService.flushRepeatitionsByCourseName(name);
	  }
	  
	  // Delete Services end.
	  
	//update course..
	@RequestMapping(value="/API/course/update",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseReport updateCourseService(Course course) {
		return courseService.updateCourse(course);
	}
	
	@RequestMapping(value="/API/repeatition/{name}/{weekDay}/{lectureNo}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseReport deleteUniqueRepeatitionByCourseNameService(@PathVariable("name")String name,@PathVariable("weekDay")int weekDay,
			@PathVariable("lectureNo")int lectureNo) {
		return courseService.deleteUniqueRepeatitionByCourseName(name, weekDay, lectureNo);
	}

/*	  return courseService.addCourse(new Course().addName("SE").addDept("CSE").addDescription("SE - Software Engineering").addFac_contact("9501669223")
			  .addFaculty("Gurpreet Mam").addRefBook("SE - Bilal Saeed").addSection("6X").addSem("6").addtRefBookLink("google.in")
			  .addType("main")); */
	
}
