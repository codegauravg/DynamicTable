#API SERVICE LINKS DETAILS

Below is the list of all services provided by th SchedulerAMa RESTfull API with all the required specifications for that service


LINK	                                            METHOD	    CONSUMES	        PRODUCES	        DESCPRITION
/API/course	                                        GET	-	                        APPLICATION_JSON    Get All Courses.
/API/course/{name}	                                GET	-	                        APPLICATION_JSON	Get Course By Name.
/API/course/faculty/{faculty}	                    GET	-                       	APPLICATION_JSON	GetCourses of a Faculty.
/API/course/{dept}/{sem}/{section}	                GET	-                       	APPLICATION_JSON	Get Courses By Their Dept, Sem &Section.
/API/course/{dept}/{sem}/{section}/{faculty}    	GET	-	                        APPLICATION_JSON	Get Courses By Their Dept, Sem, Section &Faculty.
/API/course/{name}/repeatition	                    GET	-	                        APPLICATION_JSON	Get All Repeatitions of Mentioned Course.
/API/course/main                                	GET	-	                        APPLICATION_JSON	Get All main scheduled Courses.
/API/course/main                                	GET	-	                        APPLICATION_JSON	Get All arranged scheduled Courses.
/API/course	                                        PUT -   	APPLICATION_JSON	APPLICATION_JSON	Create a New Course.
/API/course/{name}	                                DELETE	-	                    APPLICATION_JSON	Delete a Course By name.
/API/repeatition/{name}/flushRepeatitions            	DELETE	-	                    APPLICATION_JSON	Clear All Repeatitions for mentioned Course.
/API/course/update                              	POST -  	APPLICATION_JSON	APPLICATION_JSON	Update a mentioned Course.
/API/repeatition/{name}/{weekDay}/{lectureNo}        	POST -                      	APPLICATION_JSON	Add repeatition to a mentioned course.
/API/repeatition/{name}/{weekDay}/{lectureNo} 		DELETE	-											APPLICATION_JSON	Delete a unique repeatition for mentioned course.
/API/repeatition/main															GET			-											APPLICATION_JSON	Get list of main Repeatitions
/API/repeatition/temp															GET			-											APPLICATION_JSON	Get list of temp Repeatitions
/API/schedule/{dept}/{sem}/{section}							GET			-											APPLICATION_JSON	Get Scheduled Repeatitions List for mentioned dept-sem-section unit.
##Response Body Formats

###All Methods excluding GET return a ResponseSheet Format that only includes a status and error report. APPLICATION_JSON Body Format is mentioned below:

{
	"status": "STATUS MESSAGE",
	"error": "ERROR MESSAGE (IF ANY)"
}

###In case of GET method, there are 2 possibilities.

1. Either a course list is returned in links like "/API/course", which include a status, error(if any), and a list of courses. APPLICATION_JSON Body Format is mentioned below:

{
	"status": "STATUS OK",
	"error": "ERROR (IF ANY)",
	"courses":
	[
		{
			"name": "SE",
			"type": "main",
			"description": "SE - Software Engineering",
			"faculty": "Gurpreet Mam",
			"fac_contact": "9501669223",
			"dept": "CSE",
			"sem": "6",
			"section": "6X",
			"refBook": "SE - Bilal Saeed",
			"refBookLink": "google.in"
		},
		{
			"name": "SMTP-TEMP",
			"type": "temp",
			"description": "SM - Simnulation Modelling(Temp Course)",
			"faculty": "Harkomal Mam",
			"fac_contact": "9501669223",
			"dept": "CSE",
			"sem": "6",
			"section": "6X",
			"refBook": "SMTP – Raftaaar",
			"refBookLink": "mafia.studios"
		}
	]
}

2. Or a Repeatitions list is returned in links like "/API/course/{name}/repeatition", which include a status, error(if any), and a list of Repeatitions. APPLICATION_JSON Body Format is mentioned below:

{
	"status": "STATUS OK",
	"error": "ERROR (IF ANY)",
	"repeatitions":
	[
		{
			"id": 3,
			"weekDay": 4,
			"lectureNo": 5,
			"course":
			{
				"name": "SE",
				"type": "main",
				"description": "SE - Software Engineering",
				"faculty": "Gurpreet Mam",
				"fac_contact": "9501669223",
				"dept": "CSE",
				"sem": "6",
				"section": "6X",
				"refBook": "SE - Bilal Saeed",
				"refBookLink": "google.in"
			}
		},
		{
			"id": 21,
			"weekDay": 5,
			"lectureNo": 6,
			"course":
			{
				"name": "SE",
				"type": "main",
				"description": "SE - Software Engineering",
				"faculty": "Gurpreet Mam",
				"fac_contact": "9501669223",
				"dept": "CSE",
				"sem": "6",
				"section": "6X",
				"refBook": "SE - Bilal Saeed",
				"refBookLink": "google.in"
			}
		}
	]
}

###Request Body Formats

All API links (PUT/POST METHODS) that consumes APPLICATION_JSON format have a specific FORMAT. Mentioned below:
Course APPLIATION_JSON BODY FORMAT(for eg,  link : "/API/course" method : PUT):

			{
				"name": "SE",
				"type": "main",
				"description": "SE - Software Engineering",
				"faculty": "Gurpreet Mam",
				"fac_contact": "9501669223",
				"dept": "CSE",
				"sem": "6",
				"section": "6X",
				"refBook": "SE - Bilal Saeed",
				"refBookLink": "google.in"
			}
