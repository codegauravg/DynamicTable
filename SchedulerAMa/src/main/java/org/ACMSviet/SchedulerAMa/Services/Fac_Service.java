package org.ACMSviet.SchedulerAMa.Services;

import java.util.ArrayList;
import java.util.List;

import org.ACMSviet.SchedulerAMa.Models.Faculty;
import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Fac_Service {
	
	private Faculty fac;
	@Autowired
	private SessionFactory sessionFactory;
	


//Traversal or READ structure services...
	public Faculty getFacByName(String fac_name) {//getting the faculty object using the primary key(faculty NAME)
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Faculty _return = (Faculty) session.get(Faculty.class, fac_name);
		try {
		session.getTransaction().commit();
		}catch(Exception e) {
			
			session.getTransaction().rollback();
			System.err.println("[HIBERNATE FAC SERVICE]exception coming");
		}
		return _return;
	}
	
	public Course getCourseByName(String cor_name) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Course cor_details=new Course();
		Criteria fac_trav_criteria = session.createCriteria(Faculty.class);
		
		List<Faculty> fac_trav_list = fac_trav_criteria.list();
		try {session.getTransaction().commit();}catch(Exception e) {session.getTransaction().rollback();System.err.println(e);}
		for(Faculty fac_trav : fac_trav_list) {
			for(Course trav_course : fac_trav.getFac_course()) {
				if(trav_course.getCor_name().equals(cor_name)) {
					cor_details=trav_course;
				}
			}
		}
		return cor_details;

	}
	
	public Course getCourseByTDSS(int cor_timing,String cor_dept,String cor_semstr,String cor_section) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Course cor_details=new Course();
		Criteria fac_trav_criteria = session.createCriteria(Faculty.class);
		
		List<Faculty> fac_trav_list = fac_trav_criteria.list();
		try {session.getTransaction().commit();}catch(Exception e) {session.getTransaction().rollback();System.err.println(e);}
		for(Faculty fac_trav : fac_trav_list) {
			for(Course trav_course : fac_trav.getFac_course()) {
				if(trav_course.getCor_dept().equals(cor_dept)&&trav_course.getCor_semstr().equals(cor_semstr)&&
						trav_course.getCor_section().equals(cor_section)&&trav_course.getCor_timing()==cor_timing) {
					cor_details=trav_course;System.out.println("[TDSS]::match found");
				}
				
			}
		}
		return cor_details;

	}
	
	public List<Faculty> getAllFacultyDetails(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Faculty> list_fac =session.createCriteria(Faculty.class).list();
		try {session.getTransaction().commit();}catch(Exception e) {session.getTransaction().rollback();System.err.println(e);}
		return list_fac;
		
	}

	public List<Course> getAllCourseDetails(){
		List<Course> course_list = new ArrayList<Course>();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Faculty> all_fac = (List<Faculty>)session.createCriteria(Faculty.class).list();
		for(Faculty trav_fac : all_fac) {
			for(Course course : trav_fac.getFac_course()) {
				course_list.add(course);
			}
		}
		try {session.getTransaction().commit();}catch(Exception e) {session.getTransaction().rollback();System.err.println(e);}
		return course_list;
	}

/* how to use an element of a collection list as an example in criteria API...??
 * 
 * public Course getCourseByExample(Faculty fac) {
		Course course = new Course();
		Example example = Example.create(fac);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Faculty.class).add(example);
		Faculty cri_fac = (Faculty) criteria.uniqueResult();
		for(Course trav_course : cri_fac.getFac_course()) {
			course = trav_course;
		}
		try {session.getTransaction().commit();}catch(Exception e) {session.getTransaction().rollback();System.err.println(e);}
		return course;
	}*/

	
	//getters and setters..
	public Faculty getFac() {
		return fac;
	}

	public void setFac(Faculty fac) {
		this.fac = fac;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
}
