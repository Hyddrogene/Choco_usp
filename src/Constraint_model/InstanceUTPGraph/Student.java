package Constraint_model.InstanceUTPGraph;

public class Student {
	//Attribute
	private Course[] courses;
	private Class[] classes;
	private int cpt;
	private String id;
	private String type = "student";
	private Session[] sessions;
	private Label[] labels;

	private Group group;
	//Method
	public Student(int c , String id,Label[] labels, Course[] courses) {
		this.cpt = c;
		this.id = id;
		this.courses = courses;
		this.labels = labels;
	}//FinMethod
	
	public Class[] getClasses() {
		return classes;
	}//FinMethod
	
	public void setClasses(Class[] classes) {
		this.classes = classes;
	}//FinMethod
	
	public Session[] getSessions() {
		return sessions;
	}//FinMethod
	
	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}//FinMethod
	
	public Group getGroup() {
		return group;
	}//FinMethod
	
	public void setGroup(Group group) {
		this.group = group;
	}//FinMethod
	
	public Course[] getCourses() {
		return courses;
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
