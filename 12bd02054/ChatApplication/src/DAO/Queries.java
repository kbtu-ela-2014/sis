package DAO;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.IMessage;
import Model.User;

public class Queries {
	   static final String DB_URL = "jdbc:mysql://localhost:3306/test";

	   //  Database credentials
	   static final String USER = "username";
	   static final String PASS = "";
	   
	  
	public static void insertUser(String firsName, String lastName, String login, int password){
		
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			stmt = conn.createStatement();
			String sql = "insert into users (first_name, last_name, login, password_hash) values ('"+firsName+"', '"
			                                   +lastName+"', '"+login+"', '"+password+"'); ";
			
		   stmt.execute(sql);
			
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	
	} 
public static void insertMes(IMessage im){
		
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			stmt = conn.createStatement();
			System.out.println(im.getSender().getId());
			String sql = "insert into MESSAGES (SENDER_ID, Date, BODY, ROOM_ID) values ('"+im.getSender().getId()+
					"', sysdate(),'"+im.getBody()+"', '"+im.getRoomID()+"'); ";
			
		   stmt.execute(sql);
			
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
	
	} 

	public static TreeSet<User> findAll(){
		TreeSet<User> allUsers = new TreeSet<>();
		int id;
		String firstName;
		String lastName ;
		String login;
		int password;
		
		
		 Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");

		      conn = DriverManager.getConnection(DB_URL, USER, PASS);

		      //STEP 4: Execute a query
		      stmt = conn.createStatement();
		      
		      String sql = "Select ID, first_name, last_name, login, password_hash from users;";
		      
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  User user = new User();
		    	  id = rs.getInt("id");
		    	  firstName = rs.getString("first_name");
		    	  lastName = rs.getString("last_name");
		    	  login = rs.getString("login");
		    	  password = rs.getInt("password_hash");
		    	  user.setId(id);
		    	  user.setFirstName(firstName);
		    	  user.setLastName(lastName);
		    	  user.setLogin(login);
		    	  user.setPassword(password);
		    	  
		    	  allUsers.add(user);  
		      }
		  	
		     
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		return allUsers;
	}
	
	public static ArrayList<IMessage> getHistory(int roomID){
		ArrayList<IMessage> mesHistory = new ArrayList<>();
		
		int id;
		int sender_id = 0;
		String body;
		Date date; 
		String room_name ;
		
		
		 Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.mysql.jdbc.Driver");

		      conn = DriverManager.getConnection(DB_URL, USER, PASS);

		      //STEP 4: Execute a query
		      stmt = conn.createStatement();
		      
		      //String sql = "Select STUDENT_ID, first_name, last_name from USERS;";
		      String sql = "SELECT messages.id, sender_id, date, body, chat_rooms.name FROM Messages, CHAT_ROOMS"
		      		+ " WHERE chat_rooms.id=Messages.room_id and chat_rooms.id='"+roomID+"' ORDER BY date desc LIMIT 30";
		      
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  User user = new User();
		    	  IMessage im = new IMessage();
		    	  id = rs.getInt("id");
		    	  sender_id = rs.getInt("sender_id");
		    	  body = rs.getString("body");
		    	  date = rs.getDate("date");
		    	  room_name = rs.getString("chat_rooms.name");
		    	  user = findSender(sender_id);
		    	  im.setId(id);
		    	  im.setBody(body);
		    	  im.setDate(date);
		    	  im.setSender(user);
		    	  im.setRoomName(room_name);
		    	  mesHistory.add(im);
		    	  
		      }
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		return mesHistory;
	}
	 private static User findSender(int sender_id){
			User user = new User();
				String f_name;
				String l_name;
				String login;
				int pass;
				
				
				 Connection conn = null;
				   Statement stmt = null;
				   try{
				      Class.forName("com.mysql.jdbc.Driver");

				      conn = DriverManager.getConnection(DB_URL, USER, PASS);

				      //STEP 4: Execute a query
				      stmt = conn.createStatement();
				      
				      //String sql = "Select STUDENT_ID, first_name, last_name from USERS;";
				      String sql  = "SELECT * FROM USERS WHERE ID = '"+sender_id+"'";
				      
				      ResultSet rs = stmt.executeQuery(sql);
				      rs.next();
				    	  f_name = rs.getString("first_name");
				    	  l_name = rs.getString("last_name");
				    	  login = rs.getString("login");
				    	  pass = rs.getInt("password_hash");
				    	  
				    	  
				    	  
				    	  user.setId(sender_id);
				    	  user.setFirstName(f_name);
				    	  user.setLastName(l_name);
				    	  user.setLogin(login);
				    	  user.setPassword(pass);
				     
				     
				   }catch(SQLException se){
				      //Handle errors for JDBC
				      se.printStackTrace();
				   }catch(Exception e){
				      //Handle errors for Class.forName
				      e.printStackTrace();
				   }finally{
				      //finally block used to close resources
				      try{
				         if(stmt!=null)
				            stmt.close();
				      }catch(SQLException se2){
				      }// nothing we can do
				      try{
				         if(conn!=null)
				            conn.close();
				      }catch(SQLException se){
				         se.printStackTrace();
				      }//end finally try
				   }
				return user;
			}
/*	public static void updateUser(String id, String firstName, String lastName){
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			stmt = conn.createStatement();
			String sql = "update students1 set first_name = '"+ firstName+"', last_name = '"+lastName+"' where student_id = '"
			                                  +id+"'";
			
		   stmt.execute(sql);
			
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		
		}
	*/
/*	public static void deleteteUser(String id){
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER,PASS);
			
			stmt = conn.createStatement();
			String sql = "delete from students1  where student_id = '"+id+"'";
			
		   stmt.execute(sql);
			
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		
		}
	*/
	/*public static User findByID(String id){
		User user = new User();
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER,PASS);
			
			stmt = conn.createStatement();
			String sql = "select * from students1  where student_id = '"+id+"'";
			
		   ResultSet rs = stmt.executeQuery(sql);
		  
		   if( rs.next()){
		   int id1 = rs.getInt("student_id");
		   String firstName = rs.getString("first_name");
		   String lastName = rs.getString("last_name");
	
		    	  user.setId(id1);
		    	  user.setFirstName(firstName);
		    	  user.setLastName(lastName);
		   }
		   else user = null;
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		
		  return user;
		}*/
/*	public static Vector<User> findByName(String name, String column){
		Vector<User> users = new Vector<>();
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER,PASS);
			
			stmt = conn.createStatement();
			String sql = "select * from students1  where "+column+" = UPPER('"+name+"')";
			
		   ResultSet rs = stmt.executeQuery(sql);
		   while( rs.next()){
		   int id1 = rs.getInt("student_id");
		   String firstName = rs.getString("first_name");
		   String lastName = rs.getString("last_name");
		   User user = new User();
		    	  user.setId(id1);
		    	  user.setFirstName(firstName);
		    	  user.setLastName(lastName);
		    	  users.add(user);
		   }
		  
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }
		
		  return users;
		}
	*/
}
