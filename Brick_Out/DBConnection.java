package Brick_Out;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
	
	Connection con;		//DB연결
	Statement st;		//sql문구 사용할 수 있도록 
	ResultSet rs;		//mysql의 데이터를 rs에다가 저장 
	
	int count;
	int DBscore[];
	String DBname[];
	
	public DBConnection() {
		
		DBscore=new int[10];
		DBname=new String[10];
		count=0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ranking?useSSL=false","root","gjlee7257");
			st=con.createStatement();
		}
		catch(Exception e) {
			System.out.println("데이터베이스 연결 오류:"+e.getMessage());
		}
	}
	public boolean isAdmin(String adminID,String adminPassword) {
		try {
			String SQL="select * from admin where adminID= '"+adminID+"'and adminPassword='"+adminPassword+"'";
			rs=st.executeQuery(SQL);
			if(rs.next()) {
				return true;
			}
		}
		catch(Exception e) {
			System.out.println("데이터베이스 검색 오류:"+e.getMessage());
		}
		return false;
	}
	public void getData() {
		try {
			String SQL="select * from rankdata order by userScore desc";
			rs=st.executeQuery(SQL);
			count=0;
			while(rs.next()) {
				DBname[count]=rs.getString("userName");
				DBscore[count]=rs.getInt("userScore");
	
				count++;
			}
		}
		catch(Exception e) {
			System.out.println("데이터베이스 읽기 오류:"+e.getMessage());
		}
		
	}
	public void setData(String userName,int userScore) {
		try {
			String SQL="insert into rankdata values ('"+userName+" ', "+userScore+")";
			st.executeUpdate(SQL);			//select -> executeQuery
											//insert,delete,updata -> executeUpdate
		}
		catch(Exception e) {
			System.out.println("데이터베이스 쓰기 오류:"+e.getMessage());
		}
	}
}

