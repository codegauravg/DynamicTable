package org.ACMSviet.SchedulerAMa.Models.submodels;

import javax.persistence.Embeddable;

@Embeddable
public class Course {

	private String cor_name;						//course name 
	private String cor_description;					//course description
	private int cor_timing;							//course timing
	private String cor_dept,cor_semstr,cor_section;	//course classroom details
	private int cor_mod_token;						//course modified token (0 official,1 modified)
	private int cor_mod_id;							//course modified id (used only if the course is modified)

	// getters and setters...
	public String getCor_name() {
		return cor_name;
	}
	public void setCor_name(String cor_name) {
		this.cor_name = cor_name;
	}
	public String getCor_description() {
		return cor_description;
	}
	public void setCor_description(String cor_description) {
		this.cor_description = cor_description;
	}
	public int getCor_timing() {
		return cor_timing;
	}
	public void setCor_timing(int cor_timing) {
		this.cor_timing = cor_timing;
	}
	public String getCor_dept() {
		return cor_dept;
	}
	public void setCor_dept(String cor_dept) {
		this.cor_dept = cor_dept;
	}
	public String getCor_semstr() {
		return cor_semstr;
	}
	public void setCor_semstr(String cor_semstr) {
		this.cor_semstr = cor_semstr;
	}
	public String getCor_section() {
		return cor_section;
	}
	public void setCor_section(String cor_section) {
		this.cor_section = cor_section;
	}
	public int getCor_mod_token() {
		return cor_mod_token;
	}
	public void setCor_mod_token(int cor_mod_token) {
		this.cor_mod_token = cor_mod_token;
	}
	public int getCor_mod_id() {
		return cor_mod_id;
	}
	public void setCor_mod_id(int cor_mod_id) {
		this.cor_mod_id = cor_mod_id;
	}

}
