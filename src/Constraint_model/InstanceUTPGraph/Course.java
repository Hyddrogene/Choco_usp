package Constraint_model.InstanceUTPGraph;

public class Course extends CourseElement {
	private Part[] parts;
	
	public Course(String id, int cpt, String type, Label[] label, Part[] parts) {
		super(id, cpt, type, label);
	}//FinMethod

	
	@Override
	public Session[] getSession() {
		for(int i = 0; i < parts.length ; i++) {
			
		}
		return null;
	}

}
