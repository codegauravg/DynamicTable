package org.ACMSviet.SchedulerAMa.Controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ACMSviet.SchedulerAMa.Models.Faculty;
import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.ACMSviet.SchedulerAMa.Models.submodels.Temp_Course;
import org.ACMSviet.SchedulerAMa.Services.Fac_Service;
import org.ACMSviet.SchedulerAMa.Services.ModTokenService;
import org.ACMSviet.SchedulerAMa.Services.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.CreateKeySecondPass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired(required=true)
	private Fac_Service fac_Service;
	@Autowired(required=true)
	private ModTokenService modTokenService;
	@Autowired(required=true)
	private Task task;
	
	//getters and setters...
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public Fac_Service getFac_Service() {
		return fac_Service;
	}
	public void setFac_Service(Fac_Service fac_Service) {
		this.fac_Service = fac_Service;
	}
	
	

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model){
		
		
		/* 
		 * models DEBUG test runs below..
		 */
		Course c1 = new Course(); c1.setCor_name("DISCRETE II");c1.setCor_timing(0);c1.setCor_dept("CSE");c1.setCor_description("good one");c1.setCor_section("X");
								  c1.setCor_mod_id(0);c1.setCor_mod_token(0);c1.setCor_semstr("4");
		Course c2 = new Course(); c2.setCor_name("CN II");c2.setCor_timing(1);c2.setCor_dept("CSE");c2.setCor_description("good one");c2.setCor_section("X");
								  c2.setCor_mod_id(1);c2.setCor_mod_token(0);c2.setCor_semstr("4");
		Course c3 = new Course(); c3.setCor_name("OS II");c3.setCor_timing(2);c3.setCor_dept("CSE");c3.setCor_description("good one");c3.setCor_section("X");
		  						  c3.setCor_mod_id(2);c3.setCor_mod_token(0);c3.setCor_semstr("4");
		Course c4 = new Course(); c4.setCor_name("RDBMS");c4.setCor_timing(3);c4.setCor_dept("CSE");c4.setCor_description("good one");c4.setCor_section("X");
		  						  c4.setCor_mod_id(3);c4.setCor_mod_token(0);c4.setCor_semstr("4");
		
		ArrayList<Course> fac_course = new ArrayList<Course>();
		fac_course.add(c1);
		fac_course.add(c2);
		fac_course.add(c3);
		fac_course.add(c4);
		
		Temp_Course tc1 = new Temp_Course();
		tc1.setTemp_cor_dept("CSE");tc1.setTemp_cor_description("OKOK");tc1.setTemp_cor_mod_id(0);tc1.setTemp_cor_name("WASTED");
		tc1.setTemp_cor_section("Y");tc1.setTemp_cor_semstr("4");tc1.setTemp_cor_timing(4);
		
		ArrayList<Temp_Course> fac_temp_course = new ArrayList<Temp_Course>();
		fac_temp_course.add(tc1);
		
		Faculty faculty1=new Faculty();
		faculty1.setFac_name("Anurag");
		faculty1.setFac_contact("9811894747");
		faculty1.setFac_course(fac_course);
		faculty1.setFac_temp_course(fac_temp_course);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(faculty1);
		try {
		session.getTransaction().commit();
		}catch(Exception e) {
			session.getTransaction().rollback();
			System.out.println(e);
		}
		// all modifier token service test run here
		modTokenService.addToModToken(15, "RDBMS");
		modTokenService.addToModToken(15, "RDBMS");

		
		Faculty fac_gotten = fac_Service.getFacByName("Anurag");
/*			Faculty example_fac = new Faculty();
		fac_course.clear();
		fac_course.add(c2);
		example_fac.setFac_course(fac_course);*/
		
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
/*		
		Faculty new_fac = new Faculty();
		new_fac.setFac_name("Gaurav");
		new_fac.setFac_contact("9595959595");
		fac_Service.addFaculty(new_fac);
		new_fac.setFac_name("Pankaj");
		fac_Service.addFaculty(new_fac);
		fac_Service.deleteCourseByName("DISCRETE II");

	
		Course new_c = new Course();
		new_c.setCor_name("NEW COURSE");
		new_c.setCor_description("New One");
		new_c.setCor_dept("ECE");
		fac_Service.addCourse("Pankaj", new_c);
		
		new_fac = fac_Service.getFacByName("Pankaj");new_fac.setFac_contact("4554456654");
		fac_Service.updateFaculty(new_fac);
		tc1.setTemp_cor_description("NEW WASTED PRO UPDATE");
		fac_Service.updateTemp_CourseByMODID(tc1);
		
		modTokenService.setModToken(9, "CN II");
		modTokenService.setModToken(10, "CN II");
		modTokenService.setModToken(3, "CN II");
		modTokenService.setModToken(56, "OS II");*/
/*		modTokenService.addToModToken(900, "CN II");
		try {
			modTokenService.decModToken("CN II");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

		//check multi decriment for mod token services..no working, exception,:: no handler found::??
		
		System.err.println("Mod id unique boolean return ::"+task.isMODIDUnique(50));	
		
		task.createReplacementCourse("Anurag", c1, tc1, 5);
		
		String formattedDate = dateFormat.format(date);
		model.addAttribute("Faculty",fac_gotten);
		 model.addAttribute("serverTime", formattedDate );
		 model.addAttribute("Course",fac_Service.getCourseByName("NEW COURSE"));
		 model.addAttribute("FacultyList",fac_Service.getAllFacultyDetails());
		 model.addAttribute("CourseList", fac_Service.getAllCourseDetails());
		 model.addAttribute("TEMP_COR", null);
		 
		 model.addAttribute("TDSSCourse", fac_Service.getCourseByTDSS(2,"CSE", "4", "X"));
		/* model.addAttribute("EXM_Course", fac_Service.getCourseByExample(example_fac));*/
		return "home";
	}

}
