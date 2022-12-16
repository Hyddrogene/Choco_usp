package Constraint_model.InstanceUTPGraph;

public class Entity {
	//Attribute
	private int cpt;
	private String id;
	private String type;
	private Label[] labels;
	//Method
	public Entity(String id,int cpt,String type,Label[] label) {
		this.id = id;
		this.cpt = cpt;
		this.type = type;
		this.labels = label;
	}//FinMethod
	
	public int getCpt() {
		return cpt;
	}//FinMethod
	
	public String getId() {
		return id;
	}//FinMethod
	
	public String getType() {
		return type;
	}//FinMethod
	
	public Label[] getLabels() {
		return labels;
	}//FinMethod
	
}//FinClass
