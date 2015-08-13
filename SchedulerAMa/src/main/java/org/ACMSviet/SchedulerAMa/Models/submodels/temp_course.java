package org.ACMSviet.SchedulerAMa.Models.submodels;

public class temp_course {

	private int temp_cor_mod_id;
	private String temp_cor_name;									//temporary course name 
	private String temp_cor_description;							//temporary course description
	private int temp_cor_timing;									//temporary course timing
	private String temp_cor_dept,temp_cor_semstr,temp_cor_section;	//temporary course classroom details

	
	//getters and setters...
	public int getTemp_cor_mod_id() {
		return temp_cor_mod_id;
	}
	public void setTemp_cor_mod_id(int temp_cor_mod_id) {
		this.temp_cor_mod_id = temp_cor_mod_id;
	}
	public String getTemp_cor_name() {
		return temp_cor_name;
	}
	public void setTemp_cor_name(String temp_cor_name) {
		this.temp_cor_name = temp_cor_name;
	}
	public String getTemp_cor_description() {
		return temp_cor_description;
	}
	public void setTemp_cor_description(String temp_cor_description) {
		this.temp_cor_description = temp_cor_description;
	}
	public int getTemp_cor_timing() {
		return temp_cor_timing;
	}
	public void setTemp_cor_timing(int temp_cor_timing) {
		this.temp_cor_timing = temp_cor_timing;
	}
	public String getTemp_cor_dept() {
		return temp_cor_dept;
	}
	public void setTemp_cor_dept(String temp_cor_dept) {
		this.temp_cor_dept = temp_cor_dept;
	}
	public String getTemp_cor_semstr() {
		return temp_cor_semstr;
	}
	public void setTemp_cor_semstr(String temp_cor_semstr) {
		this.temp_cor_semstr = temp_cor_semstr;
	}
	public String getTemp_cor_section() {
		return temp_cor_section;
	}
	public void setTemp_cor_section(String temp_cor_section) {
		this.temp_cor_section = temp_cor_section;
	}
	

}
