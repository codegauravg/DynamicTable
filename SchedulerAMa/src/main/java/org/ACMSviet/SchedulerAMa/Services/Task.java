package org.ACMSviet.SchedulerAMa.Services;

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
	
	

}
