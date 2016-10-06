package io.github.echowine.totalwar.model;


public class Faction extends Model{
	
	public static String table = "factions";

	public static Faction factory(){
		return new Faction();
	}
	
	public static void ini(){
		Model.ini(Faction.table);
	}
	
	public boolean delete(){
		return super.delete(Faction.table);
	}
	
	public boolean save(){
		return super.save(Faction.table);
	}
	
	public static Faction findBy(String name,String column){
		return (Faction)Model.findBy(factory(),Faction.table,name,column);
	}

}
