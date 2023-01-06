package Constraint_model.InstanceUTPGraph;

public class Lecturer extends Ressource {
	private Part[] parts;
	private int service;
	public Lecturer(String id, int cpt, String type, Label[] label) {
		super(id, cpt, type, label);
		service = 0;
	}//FinMethod
	
	public Part[] getParts() {
		return parts;
	}//FinMethod
	
	public void setParts(Part[] parts) {
		this.parts = parts;
		
	}//FinMethod
	
	/*public void setService() {
		int serv = 0;
		for(int p = 0; p < this.parts.length ;p++) {
			
		}
		this.service = serv;
	}//FinMethod*/
	
	public int getService() {
		return service;
	}//FinMethod
}//FinClass
