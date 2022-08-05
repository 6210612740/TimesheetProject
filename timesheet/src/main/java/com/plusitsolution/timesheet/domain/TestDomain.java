package com.plusitsolution.timesheet.domain;

import com.plusitsolution.common.toolkit.PlusSheetMap;

public class TestDomain {
	private String firstName;
	private String lastName;
	private String age;
	
	public TestDomain(String firstName,String lastName,String age) {
		this.firstName = firstName;
		this.lastName =lastName;
		this.age = age;
	}
	
	@PlusSheetMap(index = 0)
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@PlusSheetMap(index = 1)
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@PlusSheetMap(index = 2)
	public void setAge(String age) {
		this.age = age;
	}
	
	@PlusSheetMap(index = 0)
	public String getFirstName() {
		return firstName;
	}
	@PlusSheetMap(index = 1)
	public String getLastName() {
		return lastName;
	}
	@PlusSheetMap(index = 2)
	public String getAge() {
		return age;
	}
}
