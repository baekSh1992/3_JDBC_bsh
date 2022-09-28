package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExampleCopy {
	public static void main(String[] args) {

	// JDBC : Java에서 DB에 연결 할 수 있게 해주는 Java Programming API
	//											(Java에 기본 내장된 인터페이스)
	// java.sql 패키지에서 제공
	
	// * JDBC를 이용한 애플리케이션을 만들 때 필요한 것 * (API / DBMS / ojdbc라이브러리
	// 1. Java의 JDBC 관련 인터페이스가 있어야 한다. (API)
	// 2. DBMS가 필요(우리는 Oracle 사용)
	// 3. Oracle에서 Java 애플리케이션과 연결할 때 사용할 
	//	  JDBC를 상속 받아 구현한 클래스 모음(ojdbc11.jar 라이브러리)
	//	  -> OracleDriver.class (JDBC 드라이버) 이용
	
	// 1단계 : JDBC 객체 참조 변수 선언 (java.sql패키지의 인터페이스)
		
	Connection conn = null; // 제일 중요 
	// DB연결 정보를 담은 객체
	// -> DBMS타입, 이름, IP, Port, 계정명, 비밀번호 저장
	// -> DBeaver의 계정 접속 방법과 유사함
	// * Java와 DB 사이를 연결해주는 통로 (Stream과 유사함)
	
	Statement stmt = null;
	// Connection을 통해서 
	// SQL문을 DB에 전달하여 실행하고
	// 생성된 결과("Result Set" 혹은 "성공한 행의 갯수") 를 반환(Java)
	// 하는데 사용되는 객체
	
	ResultSet rs = null;
	// SELECT 질의 성공 시 반환됨
	// 조회 결과 집합을 나타내는 객체
		
	
	try {
		// 2단계 : 참조 변수에 알맞은 객체 대입
		
		// 1. DB연결에 필요한 Oracle JDBC Driver 메모리에 로드하기
											// -> 객체로 만들어두기
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// -> () 안에 작성된 클래스의 객체를 반환
		// -> 메모리에 객체가 생성되어지고 JDBC필요시 알아서 참조해서 사용
		// ->  생략해도 자동으로 메모리 로드가 진행됨(명시적 작성 권장)
		
		
		// Class.forName = 드라이버를 로드
		// DriverManager = 로드된 드라이버를 통해 conn 활성화 
		// Connection = DB와 자바를 연동시켜준다
		// Statement = SQL을 실행하는 객체
		// ResultSet = SQL 실행후 데이터를 반환 받음
		
		// 2. 연결 정보를 담은 Connection을 생성
		// -> DriverManager객체를 이용해서 Connection객체를 만들어 얻어옴.
		
		String type = "jdbc:oracle:thin:@"; // JDBC 드라이버의 종류
		
		String ip = "localhost"; // DB서버 컴퓨터 IP
		
		String port = ":1521"; // 포트 번호
		// 1521 기본값
		// 9000 서버컴퓨터
		String sid = ":XE"; // DB이름
		String user = "kh_bsh"; 
		String pw = "kh1234";
		
		
		// [ DriverManager ] 
		// 메모리에 로드된 JDBC드라이버를 이용해서
		// Conn 객체를 만드는 역할
		
		conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
		
		// SQLException : DB 관련 최상위 예외
		
		// 3. SQL작성
		// ** JAVA에서 작성되는 SQL마지막에는 ; 세미콜론 x
		
		String sql = "SELECT EMP_ID, EMP_NAME, SALARY, HIRE_DATE FROM EMPLOYEE"; 
														// <- 자바니까 세미콜론 찍음
		
		// 4. Statement 객체 생성
		// -> Connection 객체를 통해서 생성
		stmt = conn.createStatement();
		
		// 5. 생성된 Statement 객체에
		//		sql을 적재하여 실행후
		//		결과를 반환받아 rs변수에 저장
		rs = stmt.executeQuery(sql); // executeQuery 질의를 실행하다

		
		// 3단계 : SQL을 수행해서 반환받은 결과 (ResultSet)를
		// 			한 행씩 접근해서 컬럼 값 얻어오기
		
		while(rs.next()) { 
			// rs.next() < rs가 참조하는 rs객체 첫번쨰 컬럼부터
			//			순서대로 한 행씩 이동하여 다음행이 있을 경우 t
			//			없으면 f 반환
			
			String empId = rs.getString("EMP_ID");
			String empName = rs.getString("EMP_NAME");
			int salary = rs.getInt("SALARY");
			Date hireDate = rs.getDate("HIRE_DATE"); 
			
			// 조회 결과 출력
			System.out.printf("사번 : %s / 이름 : %s / 급여 : %d / 입사일 : %s\n",
							empId, empName, salary, hireDate.toString() );
		}
		
		
		
	} catch (ClassNotFoundException e) {
		System.out.println("JDBC 드라이버 경로가 잘못 작성되었습니다.");
		
	}catch(SQLException e) {
		e.printStackTrace();
		
	}finally {
		// 4단계 : 사용한 JDBC객체 자원 반환 ( close() )
		// rs, stmt, conn 닫기 (생성 역순으로 닫는게 좋음)
		try {
			
			if(rs != null) rs.close();
			if(stmt != null) rs.close();
			if(conn != null) rs.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
}
}
