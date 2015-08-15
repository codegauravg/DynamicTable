package org.ACMSviet.SchedulerAMa.Services;

import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModTokenService {
	/*
	 * this service class contains all unique services that deals with the token 
	 * generation or manipulation strategies.
	 */
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Fac_Service fac_Service;
	
	@Transactional
	public void setModToken(int _token_count , String _ip_cor_name) {
		this.sessionFactory.getCurrentSession();
		Course _ip_course = fac_Service.getCourseByName(_ip_cor_name);
		_ip_course.setCor_mod_token(_token_count);
		fac_Service.updateCourseByName(_ip_course);
		System.out.println("[MODTOKEN-SERVICE] set mod token of course service called...");
	}
	
	@Transactional
	public void addToModToken(int _add_token_count , String _ip_cor_name) {
		this.sessionFactory.getCurrentSession();
		Course _ip_course = fac_Service.getCourseByName(_ip_cor_name);
		_ip_course.setCor_mod_token(_ip_course.getCor_mod_token()+_add_token_count);
		fac_Service.updateCourseByName(_ip_course);
		System.out.println("[MODTOKEN-SERVICE] Add To mod token of course service called...");
	}
	
	@Transactional
	public void decModToken(String _ip_cor_name)throws Exception {
		this.sessionFactory.getCurrentSession();
		Course _ip_course = fac_Service.getCourseByName(_ip_cor_name);
		_ip_course.setCor_mod_token(_ip_course.getCor_mod_token()-1);
		fac_Service.updateCourseByName(_ip_course);
		System.out.println("[MODTOKEN-SERVICE] decriment mod token of course service called...");
	}
	
	
}
