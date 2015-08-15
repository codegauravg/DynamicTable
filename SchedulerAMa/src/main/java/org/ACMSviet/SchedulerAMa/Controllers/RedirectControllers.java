package org.ACMSviet.SchedulerAMa.Controllers;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/*
 * this Controller is the default Redirection controller, those request mapping will be included here
 * which only need to be redirected rather than computational conditioning and request mapping.
 */
@Controller
public class RedirectControllers {

	@Autowired
	private SessionFactory sessionFactory;
	
/*	@RequestMapping(value="/home")
	public ModelAndView homeRedirect() {
		ModelAndView mnv = new ModelAndView("/home");
		return mnv;
	}
	*/
	
	
	//getters and setters...
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
