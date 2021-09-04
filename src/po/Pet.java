package po;

import java.sql.*;
import java.util.*;

public class Pet {

	private String owner;
	private java.sql.Date death;
	private String species;
	private String sex;
	private String name;
	private java.sql.Date birth;

	public Pet(){
	}

	public Pet(String owner,java.sql.Date death,String species,String sex,String name,java.sql.Date birth){
		this.owner = owner;
		this.death = death;
		this.species = species;
		this.sex = sex;
		this.name = name;
		this.birth = birth;
	}

	public String getOwner(){
		return owner;
	}

	public java.sql.Date getDeath(){
		return death;
	}

	public String getSpecies(){
		return species;
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

	public void setOwner(String owner){
		this.owner = owner;
	}

	public void setDeath(java.sql.Date death){
		this.death = death;
	}

	public void setSpecies(String species){
		this.species = species;
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

}
