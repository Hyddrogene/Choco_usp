package Constraint_model.InstanceUTPGraph;

public class Part extends CourseElement {
	//Attribute 
	private Class[] classes;
	private int nrSessions;
	//Method
	public Part(String id, int cpt, String type, Label[] label) {
		super(id, cpt, type, label);
		this.nrSessions = -1;
		this.countSessions();
	}//FinMethod
	
	public int countSessions() {
		if(this.nrSessions == -1) {
			this.nrSessions = 0;
			for(int i = 0 ;i< this.classes.length ;i++ ) {
				this.nrSessions += this.classes[i].getNrSessions();
			}
		}
		return this.nrSessions;
	}//FinMethod
	
	public Class[] getClasses() {
		return classes;
	}//FinMethod

	public int getNrSessions() {
		return nrSessions;
	}//FinMethod

	@Override
	public Session[] getSession() {
		for(int i= 0 ; i < this.classes.length; i++) {
			
		}
		return null;
	}//FinMethod

}//FinClass
