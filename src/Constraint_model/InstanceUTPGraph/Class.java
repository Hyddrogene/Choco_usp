package Constraint_model.InstanceUTPGraph;

public class Class extends CourseElement{
	private Session[] sessions;
	private int maxHeadCount;
	private Class parent;
	private int nrSessions;
	public Class(String id, int cpt, String type, Label[] label, Session[] sessions) {
		super(id, cpt, type, label);
		this.sessions = sessions;
		this.nrSessions = sessions.length;
	}//FinMethod
	@Override
	public Session[] getSession() {
		return this.sessions;
	}
	
	
	public Session getSessionRank(int rank) throws Exception {
		if(rank > sessions.length ) {
			throw new Exception("Rank is not possible in "+this.getId()+" type "+this.getType()+"\n");
		}
		for(int i =0 ; i < sessions.length; i++) {
			if(sessions[i].getRank() == rank) {return sessions[i];}
		}
		throw new Exception("Rank is not possible in "+this.getId()+" type "+this.getType()+" with rank : "+rank+"\n");
	}//FinMethod
	
	public int getMaxHeadCount() {
		return maxHeadCount;
	}//FinMethod
	
	public Class getParent() {
		return parent;
	}//FinMethod

	public void setParent(Class parent) {
		this.parent = parent;
	}//FinMethod
	
	public int getNrSessions() {
		return nrSessions;
	}//FinMethod

}//FinClass
