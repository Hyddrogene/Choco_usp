package Constraint_model.InstanceUTPGraph;

public class Part extends CourseElement {
	//Attribute 
	private Class[] classes;
	private Session[] sessions;
	private int nrSessions;
	private int nr_sessions_per_class;
	private Room[] rooms;
	private Lecturer[] lecturers;
	private int session_length;
	private int[] lecturer_service;
	private int[] mandatory;
	
	//Method
	public Part(String id, int cpt, String type, Label[] label,Class[] classes,Room[] rooms, Lecturer[] lecturers,int[] lecturer_service, int session_length, int nr_sessions, int[] mandatory) {
		super(id, cpt, type, label);
		this.nrSessions = -1;
		this.nr_sessions_per_class = nr_sessions;
		this.rooms = rooms;
		this.lecturers = lecturers;
		this.lecturer_service = lecturer_service;
		this.mandatory= mandatory; 
		this.countSessions();
		this.session_length = session_length;
		this.classes = classes;
	}//FinMethod
	
	public int getNr_sessions_per_class() {
		return nr_sessions_per_class;
	}//FinMethod

	public Room[] getRooms() {
		return rooms;
	}//FinMethod

	public Lecturer[] getLecturers() {
		return lecturers;
	}//FinMethod

	public int getSession_length() {
		return session_length;
	}//FinMethod

	public int[] getLecturer_service() {
		return lecturer_service;
	}//FinMethod

	public int[] getMandatory() {
		return mandatory;
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

	public Session[] getSessions() {
		return sessions;
	}//FinMethod

	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}//FinMethod

}//FinClass
