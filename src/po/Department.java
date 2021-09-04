package po;

import java.sql.*;
import java.util.*;

public class Department {

	private String address;
	private String name;
	private Integer id;

	public Department(){
	}

	public Department(String address,String name,Integer id){
		this.address = address;
		this.name = name;
		this.id = id;
	}

	public String getAddress(){
		return address;
	}

	public String getName(){
		return name;
	}

	public Integer getId(){
		return id;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setId(Integer id){
		this.id = id;
	}

}
