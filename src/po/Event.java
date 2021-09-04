package po;

import java.sql.*;
import java.util.*;

public class Event {

	private java.sql.Date date;
	private String name;
	private String remark;
	private String type;

	public Event(){
	}

	public Event(java.sql.Date date,String name,String remark,String type){
		this.date = date;
		this.name = name;
		this.remark = remark;
		this.type = type;
	}

	public java.sql.Date getDate(){
		return date;
	}

	public String getName(){
		return name;
	}

	public String getRemark(){
		return remark;
	}

	public String getType(){
		return type;
	}

	public void setDate(java.sql.Date date){
		this.date = date;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public void setType(String type){
		this.type = type;
	}

}
