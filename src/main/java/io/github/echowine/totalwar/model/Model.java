package io.github.echowine.totalwar.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.github.echowine.totalwar.database.DataBase;
import java.sql.ResultSet;

public class Model {

	protected int id;
	protected String name;
	protected String created_at;
	protected String updated_at;
	protected boolean persisted = false;
	
	public boolean getPersisted(){
		return this.persisted;
	}
	
	public void setPersisted(boolean persisted){
		this.persisted = persisted;
	}
	
	public static void ini(String table){
		DataBase.query(""
		+ "CREATE TABLE IF NOT EXISTS "+table+"("
			+ "id int(11) AUTO_INCREMENT,"
			+ "name varchar(255) UNIQUE,"
			+ "created_at timestamp,"
			+ "updated_at timestamp,"
			+ "PRIMARY KEY(id)"
		+ ")");
	}
	
	public Model(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String str = (LocalDateTime.now()).format(formatter);
		this.created_at = str;
		this.updated_at = str;
	}
	
	public boolean save(String table){
		if(this.getPersisted()){
			Object[] obj = new Object[]{this.getName(),this.getRawUpdatedAt(),this.getId()};
			return DataBase.query("UPDATE "+table+" SET name = ?,updated_at = ? WHERE id = ?",obj);
			
		}else{
			Object[] obj = new Object[]{this.getName(),this.getRawCreatedAt(),this.getRawUpdatedAt()};
			return DataBase.query("INSERT INTO "+table+" (name,created_at,updated_at) VALUES (?,?,?)",obj);
		}
		
	}
	
	public boolean delete(String table){
		if(this.getPersisted()){
			Object[] obj = new Object[]{this.getId()};
			this.setPersisted(false);
			return DataBase.query("DELETE FROM "+table+" WHERE id = ?",obj);
		}
		
		return false;
		
	}
	
	
	public static Model findBy(Model model,String table,String column,String value){
		
			
		Object[] obj = new Object[]{value};
		ResultSet results = DataBase.fetch("SELECT * FROM "+table+" WHERE "+column+" = ?",obj);
		
		try{
			results.next();
			int id = results.getInt("id");
			String name = results.getString("name");
			String created_at = results.getString("created_at");
			String updated_at = results.getString("updated_at");
			
			model.setId(id);
			model.setName(name);
			model.setRawCreatedAt(created_at);
			model.setRawUpdatedAt(updated_at);
			model.setPersisted(true);
			
			return model;
		}catch(Exception e){
			return null;
		}
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public String getRawCreatedAt(){
		return this.created_at;
	}

	public void setRawCreatedAt(String datetime){
		this.created_at = datetime;
	}
	
	public LocalDateTime getCreatedAt(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(this.created_at, formatter);
	}
	
	public void setCreatedAt(LocalDateTime datetime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.created_at = datetime.format(formatter);
	}
	
	public String getRawUpdatedAt(){
		return this.updated_at;
	}

	public void setRawUpdatedAt(String datetime){
		this.updated_at = datetime;
	}
	
	public LocalDateTime getUpdatedAt(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(this.updated_at, formatter);
		
	}
	
	public void setUpdatedAt(LocalDateTime datetime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.updated_at = datetime.format(formatter);
	}
	
}
