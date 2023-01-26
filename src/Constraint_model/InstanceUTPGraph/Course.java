package Constraint_model.InstanceUTPGraph;

public class Course extends CourseElement {
	private Part[] parts;
	private Class[] classes;
	private Session[] sessions;
	
	
	public Course(String id, int cpt, String type, Label[] label, Part[] parts) {
		super(id, cpt, type, label);
		this.parts = parts;
	}//FinMethod

	
	@Override
	public Session[] getSession() {
		for(int i = 0; i < parts.length ; i++) {
			
		}
		return null;
	}


	public Class[] getClasses() {
		return classes;
	}


	public void setClasses(Class[] classes) {
		this.classes = classes;
	}


	public Session[] getSessions() {
		return sessions;
	}


	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}


	public Part[] getParts() {
		return parts;
	}

}
