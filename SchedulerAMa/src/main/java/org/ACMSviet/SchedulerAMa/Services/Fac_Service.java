package org.ACMSviet.SchedulerAMa.Services;

import java.util.ArrayList;
import java.util.List;

import org.ACMSviet.SchedulerAMa.Models.Faculty;
import org.ACMSviet.SchedulerAMa.Models.submodels.Course;
import org.ACMSviet.SchedulerAMa.Models.submodels.Temp_Course;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Distinct;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Fac_Service {
	/*
	 * this is the basic CRUD service class for Faculty model and all its collections.
	 */
	@Autowired
	private SessionFactory sessionFactory;


	//Traversal or READ structure services...
	
	public Faculty getFacByName(String fac_name) {//getting the faculty object using the primary key(faculty NAME)
		Session session = sessionFactory.getCurrentSession();

		
		Faculty _return = (Faculty) session.get(Faculty.class, fac_name);
		
		return _return;
	}
	
	public Course getCourseByName(String cor_name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria fac_trav_criteria = session.createCriteria(Faculty.class);
		
		try{List<Faculty> fac_trav_list = fac_trav_criteria.list();
		for(Faculty fac_trav : fac_trav_list) {
			for(Course trav_course : fac_trav.getFac_course()) {
				if(trav_course.getCor_name().equals(cor_name)) {
					return trav_course;
				}
			}
		}
		}catch(Exception e) {System.err.println(e);}
		return null;

	}
	
	public Course getCourseByTDSS(int cor_timing,String cor_dept,String cor_semstr,String cor_section) {
		Session session = sessionFactory.getCurrentSession();
		 
		Course cor_details=new Course();
		Criteria fac_trav_criteria = session.createCriteria(Faculty.class);
		
		List<Faculty> fac_trav_list = fac_trav_criteria.list();
		for(Faculty fac_trav : fac_trav_list) {
			for(Course trav_course : fac_trav.getFac_course()) {
				if(trav_course.getCor_dept().equals(cor_dept)&&trav_course.getCor_semstr().equals(cor_semstr)&&
						trav_course.getCor_section().equals(cor_section)&&trav_course.getCor_timing()==cor_timing) {
					System.out.println("[TDSS]::match found");
					return trav_course;
				}
				
			}
		}
		return cor_details;

	}
	
	public Temp_Course getTemp_CourseByMODID(int _ip_temp_course_modid) {
		Temp_Course _op_temp_course = new Temp_Course();
		List<Faculty> _fac_list = getAllFacultyDetails();
		for (Faculty _trav_fac : _fac_list) {
			for(Temp_Course _trav_temp_cor : _trav_fac.getFac_temp_course()) {
				if(_trav_temp_cor.getTemp_cor_mod_id()==_ip_temp_course_modid) {
					System.out.println("[Service]Temp Course Details List service called:");
					System.err.println(_op_temp_course.getTemp_cor_name());
					return  _trav_temp_cor;
				
				}
			}
		}
		System.out.println("[Service]Temp Course Details List service called[no result]:");
		
		return _op_temp_course;
	}
	
	public List<Faculty> getAllFacultyDetails(){
		Session session = sessionFactory.getCurrentSession();
		List<Faculty> list_fac =session.createCriteria(Faculty.class).list();
		System.err.println("[Service-DEBUGGER] faculty traversal output:");
		for(Faculty facs : list_fac) {
			System.err.print(facs.getFac_name()+ " || ");
		}
		return list_fac;
		
	}
	
	public List<Course> getAllCourseDetails(){
		List<Course> course_list = new ArrayList<Course>();
		Session session = sessionFactory.getCurrentSession();
		 
		List<Faculty> all_fac = getAllFacultyDetails();
		for(Faculty trav_fac : all_fac) {
			for(Course course : trav_fac.getFac_course()) {
				course_list.add(course);
			}
		}
		System.out.println("[Service-DEBUGGER] Course traversal output:/n"+course_list);
		return course_list;
	}
	
	public List<Temp_Course> getAllTemp_CoursesDetails(){
		List<Temp_Course> _op_list_temp_cor = new ArrayList<Temp_Course>();
		List<Faculty> _list_fac = getAllFacultyDetails();
		for(Faculty _trav_fac : _list_fac) {
			for(Temp_Course _tav_temp_cor : _trav_fac.getFac_temp_course()) {
				_op_list_temp_cor.add(_tav_temp_cor);
	
			}
		}
		System.out.println("[Service] All Temp Course Details List service called");
		for(Temp_Course _debug_temp_cor : _op_list_temp_cor) {
			System.err.println("[Temp_Course traversal element gotten]"+_debug_temp_cor.getTemp_cor_name());
		}
		return _op_list_temp_cor;
	}

/* TODO how to use an element of a collection list as an example in criteria API...??
 * 
 * 
 * public Course getCourseByExample(Faculty fac) {
		Course course = new Course();
		Example example = Example.create(fac);
		Session session = sessionFactory.getCurrentSession();
		 
		Criteria criteria = session.createCriteria(Faculty.class).add(Restrictions.eq("fac_course", fac.getFac_course()));
		Faculty cri_fac = (Faculty) criteria.uniqueResult();
		for(Course trav_course : cri_fac.getFac_course()) {
			course = trav_course;
		}
		return course;
	}
 */

 
 	// model write services...
	
 	public void addFaculty(Faculty _ip_faculty) {
 		Session session = sessionFactory.getCurrentSession();
 		 
 		session.save(_ip_faculty);
 		System.out.println("[Services]Faculty Add service called");
 	}
	
 	public void addCourse(String _fac_name,Course _ip_course) {
 		Session session = sessionFactory.getCurrentSession();
 		Faculty _op_fac = getFacByName(_fac_name);
 		_op_fac.getFac_course().add(_ip_course);
 		session.update(_op_fac);
 		System.out.println("[Services]Course Add service called for Faculty : "+_fac_name);
 	}
	
	public void addTemp_Course(String _fac_name,Temp_Course _ip_temp_course) {
 		Faculty _op_fac = getFacByName(_fac_name);
 		_op_fac.getFac_temp_course().add(_ip_temp_course);
 		Session session = sessionFactory.getCurrentSession();
 		 
 		session.update(_op_fac);
 		System.out.println("[Services]Temporary Course Add service called for Faculty : "+_fac_name);		
 	}
	
 	//model delete services..
	
 	public void deleteFacultyByName(String  _ip_fac_name) {
 		Session session = sessionFactory.getCurrentSession();
 		 
 		Faculty _del_fac = getFacByName(_ip_fac_name);
 		session.delete(_del_fac);
 		System.err.println("[Services]Faculty Delete service called");
 	}
	
 	public void deleteCourseByName(String _ip_cor_name){
 		Course _del_cor = getCourseByName(_ip_cor_name);
 		List<Faculty> _fac_list = getAllFacultyDetails();
 		Session session = sessionFactory.getCurrentSession();
 		 
 		for(Faculty _fac : _fac_list) {
 			try{for(Course _cor : _fac.getFac_course()) {
 				if(_cor.getCor_name().equals(_ip_cor_name)) {System.err.println("true that");
 					_fac.getFac_course().remove(_del_cor);System.err.println("deleted");
 					session.update(_fac);
 					System.err.println("update");
 						
 					}
 			}
 				}catch(Exception e) {System.err.println(e);}
 		}
 		for(Course cor : getAllCourseDetails()) {System.err.println("Course left :"+cor.getCor_name());}
		System.err.println("[Services]Course Delete service called");
 	}
	
 	public void deleteTemp_CourseByMODID(String _ip_temp_cor_name) {
 		Course _del_temp_cor = null;//TODO create read service for temporary courses...
 		List<Faculty> _fac_list = getAllFacultyDetails();
 		Session session = sessionFactory.getCurrentSession();
 		 
 		try{for(Faculty _fac : _fac_list) {
 			for(Temp_Course _cor : _fac.getFac_temp_course()) {
 				if(_cor.getTemp_cor_name().equals(_ip_temp_cor_name)) {
 					_fac.getFac_temp_course().remove(_del_temp_cor);
 				}
 				session.update(_fac);
 			}
 		}
 		}catch(Exception e) {System.err.println(e);}
		System.err.println("[Services]Temporary Course Delete service called");
 	}
 	
 	
 	//model update services..
	
	public void updateFaculty(Faculty _ip_fac) {
		this.sessionFactory.getCurrentSession().update(_ip_fac);
		System.out.println("[SERVICE]Faculty details update service called...");
	}
	
	
	public void updateCourseByName(Course _ip_course) {
		List<Faculty> _list_fac = getAllFacultyDetails();
		Faculty temp_fac= new Faculty();
			try{for(Faculty _trav_fac : _list_fac) {
				if(_trav_fac.equals(temp_fac)) {
					continue;
				}
				temp_fac=_trav_fac;
				
				for(Course _trav_cor : _trav_fac.getFac_course()) {
					if(_trav_cor.getCor_name().equals(_ip_course.getCor_name())) {
						_trav_fac.getFac_course().remove(_trav_cor);
						_trav_fac.getFac_course().add(_ip_course);
						
						System.out.println("[SERVICE-DEBUG-MODE]Course Update done");
					}
					
				}
				updateFaculty(_trav_fac);
			}
			}catch(Exception e) {System.err.println(e);}
	}
	
	
	public void updateTemp_CourseByMODID(Temp_Course _ip_temp_course) {
		List<Faculty> _list_fac = getAllFacultyDetails();
		try{
			for(Faculty _trav_fac : _list_fac) {
				for(Temp_Course _trav_temp_cor : _trav_fac.getFac_temp_course()) {
					if(_trav_temp_cor.getTemp_cor_mod_id()==_ip_temp_course.getTemp_cor_mod_id()) {
						_trav_fac.getFac_temp_course().remove(_trav_temp_cor);
						_trav_fac.getFac_temp_course().add(_ip_temp_course);
						this.sessionFactory.getCurrentSession().update(_trav_fac);
						System.out.println("[SERVICE-DEBUG-MODE]Temporary Course Update done");
					}
				}
			}
		}catch(Exception e) {System.err.println(e);}
		System.out.println("[SERVICE]Temporary Course details update By Modification unique ID (MODID) service called...");
	}
	
	
	//TODO create all update services
 	
	
	
 	//getters and setters..
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
