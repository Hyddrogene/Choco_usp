package Constraint_model.InstanceUTPGraph;

public class Label {
	private int id;
	private int cpt;
	private String label;
	
	public Label(int c, String val) {
		this.cpt = c;
		this.id = c;
		this.label = val;
	}//FinMethod

	public String getLabel() {
		return label;
	}//FinMethod
	
	public String getValue() {
		return label;
	}//FinMethod

	public int getCpt() {
		return cpt;
	}//FinMethod

	public int getId() {
		return id;
	}//FinMethod

}//FinClass
