package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc1.model.vo.Emp;
import edu.kh.jdbc1.model.vo.Employee;

public class JDBCExample4 {
	public static void main(String[] args) {
			
		
		// 직급명, 급여를 입력 받아
		// 해당 직급에서 입력 받은 급여보다 많이 받는 사원의
		// 이름 직급명, 급여, 연봉 조회
		// 이름, 직급명, 급여, 연봉을 조회하여 출력
		
		// 단, 조회결과가 없으면 "조회 결과 없음" 출력
		// 조회 결과가 있으면
		// 선동일 / 대표 / 8000000 / 96000000
		// 송종기 / 부장 / 6000000 / 72000000
		// ...
		
		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		try {
			System.out.print("직급명 : ");
	         String inputJobName = sc.nextLine();
	         
	         System.out.print("급여 : ");
	         int inputSalary = sc.nextInt();
	         
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	         
	         String type ="jdbc:oracle:thin:@"; 
	         String ip ="localhost";
	         
	         String port =":1521"; 
	         
	         String sid =":XE"; 
	         String user ="kh_bsh";
	         String pw ="kh1234";
	         
	         conn = DriverManager.getConnection(type+ip+port+sid, user, pw);
	      
	         String sql = "SELECT EMP_NAME, JOB_NAME, SALARY, SALARY * 12  ANNUAL_INCOME"
	                  + " FROM EMPLOYEE"
	                  + " JOIN JOB USING(JOB_CODE)"
	                  + " WHERE JOB_NAM = '"+inputJobName+"'"
	                  + " AND SALARY > " + inputSalary;
	         
	         stmt = conn.createStatement();
	         
	         rs = stmt.executeQuery(sql);
	         
	         List<Employee> list = new ArrayList<>(); 
	         
	         while(rs.next()) { 
	            String empName = rs.getString("EMP_NAME");
	            String jobName = rs.getString("JOB_NAME");
	            int salary = rs.getInt("SALARY");
	            int annualIncome = rs.getInt("ANNUALINCOME");
	            
	       
	            
	            Employee emp = new Employee(empName,jobName,salary,annualIncome);
	            list.add(emp);
	         }
	            if(list.isEmpty()) {
	               System.out.println("조회 결과 없음");
	             }else {
	                for(Employee e : list) {
	                   System.out.println(e);
	                }
	             }
	             
	                
	         } catch (Exception e) {
	             e.printStackTrace();
	             
	          }finally {
	             try {
	                if(rs != null) rs.close();
	                if(stmt != null) stmt.close();
	                if(conn !=  null) conn.close();
	             } catch (SQLException e) {
	                e.printStackTrace();
	               
	             }
	          }
		}   
	}

	

