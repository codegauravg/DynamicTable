package org.ACMSviet.SchedulerAMa;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.ACMSviet.SchedulerAMa.Models.Course;
import org.ACMSviet.SchedulerAMa.Models.CourseListResponse;
import org.ACMSviet.SchedulerAMa.Models.Repeatition;
import org.ACMSviet.SchedulerAMa.Models.ResponseReport;
import org.ACMSviet.SchedulerAMa.Services.CourseService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/** Example resource class hosted at the URI path "/myresource"
 */

@Path("/myresource")
public class MyResource {
	private AbstractApplicationContext mycontext = new ClassPathXmlApplicationContext("/../spring/appServlet/servlet-context2.xml");
	private CourseService courseService =(CourseService) mycontext.getBean("courseService") ;

	
	/** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Produces("text/plain")
    public String home() {
    	
    	
    	return "Hi bro...";
    	
    }
    
    /*
     * All fetch Services below.
     */
  @GET
  @Path("/course")
  @Produces("application/json")
  public CourseListResponse getAllCoursesService() {
	  return courseService.getAllCourses();
  }
    
  @GET
  @Path("/course/{name}")
  @Produces("application/json")
  public CourseListResponse getCourseByNameService(@PathParam("name") String name) {
	  return courseService.getCourseByName(name);  
	  }
  
  @GET
  @Path("/course/faculty/{faculty}")
  @Produces("application/json")
  public CourseListResponse getCourseByFaculty(@PathParam("faculty") String faculty) {
	  return courseService.getCourseByFaculty(faculty);
  }
  
  @GET
  @Path("/course/{dept}/{sem}/{section}")
  @Produces("application/json")
  public CourseListResponse getCourseByDSSService(@PathParam("dept")String dept,
		  @PathParam("sem")String sem,@PathParam("section")String section) {
	  return courseService.findCoursesByDSS(dept, sem, section);
  }
  
  @GET
  @Path("/course/{dept}/{sem}/{section}/{faculty}")
  @Produces("application/json")
  public CourseListResponse getCourseByDSSFService(@PathParam("dept")String dept,
		  @PathParam("sem")String sem,@PathParam("section")String section,@PathParam("faculty") String faculty) {
	  return courseService.findCoursesByDSSF(dept, sem, section, faculty);
  }
  
  @GET
  @Path("/course/{name}/repeatition")
  @Produces("application/json")
  public ArrayList<Repeatition> getCourseRepetitions(@PathParam("name") String name) {
	  return courseService.getCourseRepeatitions(name);
  }
    // fetch services end.
    
    /*
     * All Add Services Below.
     */
    
  @POST
  @Path("/course")
  @Consumes("application/json")
  @Produces("application/json")
  public ResponseReport addCourseService(Course course) {
	  return courseService.addCourse(course); 
  }
  
  @POST
  @Path("/course/{name}/{weekDay}/{lectureNo}")
  @Produces("application/json")
  public ResponseReport addRepeatitionService(@PathParam("name") String name,@PathParam("weekDay") int weekDay,
		  @PathParam("lectureNo")int lectureNo) {
	  return courseService.addRepeatitions(name, weekDay, lectureNo);
  }
    
    // Add Services end.
    
    /*
     * All Delete Services below.
     */
  
	  @DELETE
	  @Path("/course/delete/{name}")
	  @Produces("application/json")
	  public ResponseReport deleteCourseService(@PathParam("name") String name) {
		  return courseService.deleteCourseByName(name);
	  }
  
	  // Delete Services end.
	  
	//update course..
	@POST
	@Consumes("application/json")
	@Path("course/update")
	@Produces("application/json")
	public ResponseReport updateCourseService(Course course) {
		return courseService.updateCourse(course);
	}

/*	  return courseService.addCourse(new Course().addName("SE").addDept("CSE").addDescription("SE - Software Engineering").addFac_contact("9501669223")
			  .addFaculty("Gurpreet Mam").addRefBook("SE - Bilal Saeed").addSection("6X").addSem("6").addtRefBookLink("google.in")
			  .addType("main")); */
	
}
