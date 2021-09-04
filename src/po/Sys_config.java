package po;

import java.sql.*;
import java.util.*;

public class Sys_config {

	private java.sql.Timestamp set_time;
	private String set_by;
	private String variable;
	private String value;

	public Sys_config(){
	}

	public Sys_config(java.sql.Timestamp set_time,String set_by,String variable,String value){
		this.set_time = set_time;
		this.set_by = set_by;
		this.variable = variable;
		this.value = value;
	}

	public java.sql.Timestamp getSet_time(){
		return set_time;
	}

	public String getSet_by(){
		return set_by;
	}

	public String getVariable(){
		return variable;
	}

	public String getValue(){
		return value;
	}

	public void setSet_time(java.sql.Timestamp set_time){
		this.set_time = set_time;
	}

	public void setSet_by(String set_by){
		this.set_by = set_by;
	}

	public void setVariable(String variable){
		this.variable = variable;
	}

	public void setValue(String value){
		this.value = value;
	}

}
