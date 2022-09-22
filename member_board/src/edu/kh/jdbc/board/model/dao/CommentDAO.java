package edu.kh.jdbc.board.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;

public class CommentDAO {

	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Properties prop;
	
	
	public CommentDAO() {
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("comment-query.xml"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** 댓글 목록 조회 DAO
	 * @param conn
	 * @param boardNo
	 * @return commentList
	 * @throws Exception
	 */
	public List<Comment> selectCommentList(Connection conn, int boardNo) throws Exception{
		List<Comment> commentList = new ArrayList<>();
		
		try {
			String sql = prop.getProperty("selectCommentList"); // SQL 얻어오기
			
			pstmt = conn.prepareStatement(sql); // PreparedStatement 생성
			
			pstmt.setInt(1, boardNo); // ? 알맞은 값 대입
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				Comment comment = new Comment();
				
				comment.setCommentNo(rs.getInt(1));
				comment.setCommentContent(rs.getString(2));
				comment.setMemberNo(rs.getInt(3));
				comment.setMemberName(rs.getString(4));
				comment.setCreateDate(rs.getString(5));
				
				commentList.add(comment);			
				}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return commentList; // 조회 결과 결과
	}
	
	
}
