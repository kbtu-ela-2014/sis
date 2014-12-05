package DAO;


import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS; 
import org.apache.commons.dbcp.datasources.SharedPoolDataSource; 
     
import javax.sql.DataSource; 
import java.sql.Connection; 
import java.sql.SQLException; 

public class DBconnectionPool {
	
	private static final int MAX_ACTIVE = 100;
	private static final int MAX_WAIT = 100;
	
	private Connection connection = null;
	private static DataSource ds = null;
	
	public DBconnectionPool(){
		 if (ds == null) { 
			                try { 
			                    //Адаптер для JDBC-драйвера. Хранит параметры подключения к БД 
			                    DriverAdapterCPDS pcds = new DriverAdapterCPDS(); 
			                    pcds.setDriver("com.mysql.jdbc.Driver"); 
			                    pcds.setUrl("jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=cp1251"); 
			                    pcds.setUser("dbuser"); 
			                    pcds.setPassword("dbpassword"); 
			     
			                    //Реализация интерфейса DataSource для разделяемого пула соеднинений 
			                    SharedPoolDataSource tds = new SharedPoolDataSource(); 
			                    tds.setConnectionPoolDataSource(pcds); 
			     
			                    tds.setMaxActive(MAX_ACTIVE); 
			     
			                    tds.setMaxWait(MAX_WAIT); 
			     
			                    ds = tds; 
			     
			                } 
			                 catch (ClassNotFoundException e) { 
			                     e.printStackTrace(); 
			                } 
			            } 
		 
	}
		  public Connection openConnection()throws SQLException{ 
			             if (connection == null) connection = ds.getConnection(); 
			             return connection; 
			      
		}
		  
		   public static int getActiveConnection() { 
			              return ((SharedPoolDataSource)ds).getNumActive(); 
			          }
		  
		   public Connection getConnection() throws SQLException { 
			              return openConnection(); 
			         }
		   
		   public void close() { 
			             try { 
			                  if (connection != null) { 
			                      connection.close(); 
			                      connection = null; 
			                 } 
			              } 
			             catch (SQLException e) { 
			                 e.printStackTrace(); 
			             } 
			} 
			     
	}

