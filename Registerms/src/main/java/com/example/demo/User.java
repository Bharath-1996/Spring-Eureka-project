package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue
	private Long id;
    private String userid;
    private String password;
    private String email;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public User(String userid, String password, String email) {
		super();
		this.userid = userid;
		this.password = password;
		this.email = email;
	}
	public User()
	{
		
	}
	@Override
	public String toString() {
		return "Employee [userid=" + userid + ", password=" + password + ", email=" + email + "]";
	}
 
    // standard getters and setters
}