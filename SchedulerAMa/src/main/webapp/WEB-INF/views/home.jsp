<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.ACMSviet.SchedulerAMa.Models.Faculty"%>
<%@page import="org.ACMSviet.SchedulerAMa.Models.submodels.Course"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<br>
<br>

NAME : ${Faculty.fac_name}
<br>
Contact : ${Faculty.fac_contact }
<fieldset><legend>COURSES</legend>
<%
Faculty fac =(Faculty) request.getAttribute("Faculty");
int i=0;
for(Course course : fac.getFac_course()){
i++;
%>
[COURSE <%=i %>] Name : <%=course.getCor_name() %>
 || Department : <%=course.getCor_dept() %>
 || Timing : <%=course.getCor_timing() %>
 || Description : <%=course.getCor_description() %>
<br><br>
<% }%>
</fieldset>
<fieldset><legend>TEMP COURSE FINDING BY MODID SERVICE CALL</legend>
	TEMP COURSE NAME: ${TEMP_COR.temp_cor_name }
	TEMP COURSE Description : ${TEMP_COR.temp_cor_description }
</fieldset>

<fieldset><legend>COURSE finding service result</legend>
Course Name : ${Course.cor_name }
Course Department : ${Course.cor_dept }
Course Timing : ${Course.cor_timing }
Course Description : ${Course.cor_description }
</fieldset>

<fieldset><legend>Faculty List Traversal Service</legend>
<%List<Faculty> fac_list = (List<Faculty>)request.getAttribute("FacultyList");
for(Faculty trav_fac : fac_list){
	%>
	Faculty NAME : <%=trav_fac.getFac_name() %> || CONTACT : <%=trav_fac.getFac_contact() %> 
	<br><br>
	
	<%}%>
</fieldset>

<fieldset><legend>Course List Traversal Service</legend>
<%ArrayList<Course> cor_list = (ArrayList<Course>)request.getAttribute("CourseList");
for(Course trav_cor : cor_list){
	%>
	Course NAME : <%=trav_cor.getCor_name() %> || Description : <%=trav_cor.getCor_description() %> || MOD_TOKEN : <%=trav_cor.getCor_mod_token() %>
	<br><br>
	
	<%}%>
</fieldset>

<fieldset><legend>COURSE finding TDSS(Time, Dept, Sem, Section) service result</legend>
Course Name : ${TDSSCourse.cor_name }
Course Department : ${TDSSCourse.cor_dept }
Course Timing : ${TDSSCourse.cor_timing }
Course Description : ${TDSSCourse.cor_description }
</fieldset>
</body>
</html>
