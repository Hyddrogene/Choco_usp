package Constraint_model.InstanceUTPGraph;

public class Room extends Ressource{
	private int size;
	public Room(String id, int cpt, String type, Label[] label, int size) {
		super(id, cpt, type, label);
		this.size = size;
	}//FinMethod
	
	public int getSize() {
		return size;
	}//FinMethod

}//FinClass
