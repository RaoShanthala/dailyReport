package co.jp.arche1.kdrs.pdf.dto;


public class Employee {
	  private String name;
	  private String dept;
	  private int salary;

	 public Employee(String name, String dept, int salary){
	    this.name = name;
	    this.dept = dept;
	    this.salary = salary;
	  }
	  public String getName() {
	    return name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public int getSalary() {
	    return salary;
	  }
	  public void setSalary(int salary) {
	    this.salary = salary;
	  }
	  public String getDept() {
	    return dept;
	  }
	  public void setDept(String dept) {
	    this.dept = dept;
	  }
}