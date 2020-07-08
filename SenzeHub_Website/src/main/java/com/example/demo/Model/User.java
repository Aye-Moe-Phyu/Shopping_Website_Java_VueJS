package com.example.demo.Model;

public class User {
	 private String name;
	 private String account;
	 private String password;

	 public User()
	 {}
	 
	 public User(String name,String gmail,String password) {
		 this.name = name;
	        this.account = gmail;
	        this.password = password;
	    }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "user [" + " name=" + name + ", account=" + account + ", password=" + password + "]";
	}
   
  
}
