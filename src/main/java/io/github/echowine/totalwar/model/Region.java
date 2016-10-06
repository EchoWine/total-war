package io.github.echowine.totalwar.model;


public class Region extends Model{
	
	public static String table = "regions";

	public static Region factory(){
		return new Region();
	}
	
	public static void ini(){
		Model.ini(Region.table);
	}
	
	public boolean delete(){
		return super.delete(Region.table);
	}
	
	public boolean save(){
		return super.save(Region.table);
	}
	
	public static Region findBy(String name,String column){
		return (Region)Model.findBy(factory(),Region.table,name,column);
	}

}
