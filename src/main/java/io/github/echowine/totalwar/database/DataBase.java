package io.github.echowine.totalwar.database;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import com.google.inject.Inject;

public class DataBase {
	
	@Inject
	private static Logger logger;
	
	private static String localhost;
	private static String username;
	private static String password;
	private static String database;
	
	private SqlService sql;

	/**
	 * Set static logger
	 *
	 * @param Logger logger
	 * @return
	 */
	public static void setLogger(Logger logger){
		DataBase.logger = logger;
	}

	/**
	 * Initialize the DataBase class
	 * 
	 * @param logger
	 */
	public static void connect(String localhost,String username,String password,String database){
	    
		DataBase.localhost = localhost;
		DataBase.username = username;
		DataBase.password = password;
		DataBase.database = database;
	}

	/**
	 * Execute the query and return the result
	 * 
	 * @param query
	 *
	 * @return ResultSet
	 */
	public static boolean query(String query){

		DataBase db = new DataBase();
		
		try{
			Connection con = db.getConnection();        
			return con.prepareStatement(query).execute();
			
		}catch(Exception e){
			db.getLogger().error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Execute the query and return the result
	 * 
	 * @param query
	 *
	 * @return ResultSet
	 */
	public static boolean query(String query,Object[] obj){

		DataBase db = new DataBase();
		
		try{
			Connection con = db.getConnection();
			PreparedStatement statement = con.prepareStatement(query);
			
			for (int i = 0; i < obj.length; i++){
				statement.setString(i+1, obj[i].toString());

			}
			
			db.getLogger().debug(statement.toString());
			
			return statement.execute();
			
		}catch(Exception e){
			db.getLogger().error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Execute the query and return ResultSet
	 * 
	 * @param query
	 *
	 * @return ResultSet
	 */
	public static ResultSet fetch(String query,Object[] obj){
		
		DataBase db = new DataBase();
		
		try{
			Connection con = db.getConnection();
			PreparedStatement statement = con.prepareStatement(query);
			
			for (int i = 0; i < obj.length; i++){
				db.getLogger().info(obj[i].toString());
				db.getLogger().info(String.valueOf(i));
				statement.setString(i+1, obj[i].toString());

			}
			
		    return statement.executeQuery();
		}catch(Exception e){
			db.getLogger().error(e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * Return connection localhost
	 * 
	 * @return
	 */
	public static String getLocalhost(){
		return DataBase.localhost;
	}

	/**
	 * Return connection username
	 * 
	 * @return
	 */
	public static String getUsername(){
		return DataBase.username;
	}

	/**
	 * Return connection password
	 * 
	 * @return
	 */
	public static String getPassword(){
		return DataBase.password;
	}

	/**
	 * Return connection database
	 * 
	 * @return
	 */
	public static String getDatabase(){
		return DataBase.database;
	}
	
	/**
	 *  Constructor
	 */
	@Inject
	public DataBase(){
		
	}

	/**
	 * Get static logger
	 *
	 * @return
	 */
	public Logger getLogger() {
	    return DataBase.logger;
	}
	
	/**
	 * Get Data source
	 * 
	 * @param jdbcUrl
	 * 
	 * @return javax.sql.DataSource
	 * 
	 * @throws SQLException
	 */
	public javax.sql.DataSource getDataSource(String jdbcUrl) throws SQLException {
	    if (sql == null) {
	        sql = Sponge.getServiceManager().provide(SqlService.class).get();
	    }
	    return sql.getDataSource(jdbcUrl);
	}

	/**
	 * Get connection
	 * 
	 * @return Connection
	 */
	public Connection getConnection() throws SQLException{    
		return getDataSource("jdbc:mysql://"+getLocalhost()+"/"+getDatabase()+"?user="+getUsername()+"&password="+getPassword()).getConnection();
	}
	
}
