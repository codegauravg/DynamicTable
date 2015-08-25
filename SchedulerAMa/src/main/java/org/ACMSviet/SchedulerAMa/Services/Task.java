package org.ACMSviet.SchedulerAMa.Services;

import java.util.List;
import java.util.Random;

import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.ACMSviet.SchedulerAMa.Models.submodels.Temp_Course;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/*
 * this service includes all the heavy tasks that the project need to implement for appropriate data representation
 * and manipulation. 
 */
@Service
public class Task {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Fac_Service fac_Service;
	@Autowired
	private ModTokenService modTokenService;
	
	/*
	 * check whether the generated Modification identity is unique in the List or not (boolean)
	 */
	public boolean isMODIDUnique(int _ip_MODID) {
		System.out.println("[HEAVY-TASK-SERVICE] modid unique checking service called.");
		List<Temp_Course> _temp_cor_list = fac_Service.getAllTemp_CoursesDetails();
		for(Temp_Course _trav_temp_cor : _temp_cor_list) {
			if(_trav_temp_cor.getTemp_cor_mod_id()==_ip_MODID) {return false;}
		}
		return true;
	}
	
	/*
	 * method to add a temporary course in replacement of the true Course
	 * generating the modification id randomly and checking for unique entry itself.
	 */
	public boolean createReplacementCourse(String _fac_name,Course _ip_cor,Temp_Course _ip_temp_course,int _mod_token) {
		
		Random randomNumGenX = new Random();
		int randomNum = randomNumGenX.nextInt(100000);
		System.out.println("[HEAVY-TASK-SERVICE] Random Number Generated :" + randomNum);
		if(isMODIDUnique(randomNum)) {
			_ip_cor.setCor_mod_id(randomNum);
			_ip_cor.setCor_mod_token(_mod_token);
			_ip_temp_course.setTemp_cor_mod_id(randomNum);
			try{fac_Service.updateCourseByName(_ip_cor);
			fac_Service.addTemp_Course(_fac_name, _ip_temp_course);}catch(Exception e) {return false;}
			
		}else {createReplacementCourse(_fac_name,_ip_cor, _ip_temp_course, _mod_token);}
		
		
		return true;
	}

}
