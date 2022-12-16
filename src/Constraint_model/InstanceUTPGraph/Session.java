package Constraint_model.InstanceUTPGraph;

public class Session {
	//Attribute
	private int rank;
	private int cpt;
	private int id;
	private String type = "session";
	//Method
	public Session(int rank,int cpt) {
		this.rank = rank;
		this.cpt = cpt;
		this.id = cpt;
	}//FinMethod
	
	public int getRank() {
		return rank;
	}//FinMethod
	
	public void setRank(int rank) {
		this.rank = rank;
	}//FinMethod
	
	public int getCpt() {
		return cpt;
	}//FinMethod
	
	public void setCpt(int cpt) {
		this.cpt = cpt;
	}//FinMethod
	
	public int getId() {
		return id;
	}//FinMethod
	
	public void setId(int id) {
		this.id = id;
	}//FinMethod
	
	public String getType() {
		return type;
	}//FinMethod

	
}//FinClass
