package po;

import java.sql.*;
import java.util.*;

public class Employee {

	private Integer departmentID;
	private String sex;
	private String name;
	private java.sql.Date birth;
	private Integer id;
	private Double salary;
	private Integer age;

	public Employee(){
	}

	public Employee(Integer departmentID,String sex,String name,java.sql.Date birth,Integer id,Double salary,Integer age){
		this.departmentID = departmentID;
		this.sex = sex;
		this.name = name;
		this.birth = birth;
		this.id = id;
		this.salary = salary;
		this.age = age;
	}

	public Integer getDepartmentID(){
		return departmentID;
	}

	public String getSex(){
		return sex;
	}

	public String getName(){
		return name;
	}

	public java.sql.Date getBirth(){
		return birth;
	}

	public Integer getId(){
		return id;
	}

	public Double getSalary(){
		return salary;
	}

	public Integer getAge(){
		return age;
	}

	public void setDepartmentID(Integer departmentID){
		this.departmentID = departmentID;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setBirth(java.sql.Date birth){
		this.birth = birth;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public void setSalary(Double salary){
		this.salary = salary;
	}

	public void setAge(Integer age){
		this.age = age;
	}

}
