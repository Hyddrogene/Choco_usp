package Constraint_model.InstanceUTPGraph;

public class Group extends Ressource{
	//Attribute
	private int headCount;
	private Student[] students;
	private Class[] classes;
	private Session[] sessions;
	//Method
	public Group(String id, int cpt, String type, Label[] label,int headCount, Student[] students,Class[] classes) {
		super(id, cpt, type, label);
		this.headCount = headCount;
		this.students = students;
		this.classes = classes;
	}//FinMethod
	
	public int getHeadCount() {
		return headCount;
	}//FinMethod
	
	public Student[] getStudents() {
		return students;
	}//FinMethod
	
	public Class[] getClasses() {
		return classes;
	}//FinMethod
	
	public Session[] getSessions() {
		return sessions;
	}//FinMethod
	
}//FinClass
