package Constraint_model;

import Constraint_model.InstanceUTPGraph.Session;

public class Evenement {
	//=================================
	
	private Session[] sessions;
	private String [] parameters_name;
	private String []  parameters;
	private int[] num_sessions;
	private int id;
	private String name;
	//=================================
	
	public Session[] getSessions() {
		return sessions;
	}//FinMethod

	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}//FinMethod

	public String[] getParameters_name() {
		return parameters_name;
	}//FinMethod

	public String[] getParameters() {
		return parameters;
	}//FinMethod

	public int[] getNum_sessions() {
		return num_sessions;
	}//FinMethod

	public int getId() {
		return id;
	}//FinMethod

	public String getName() {
		return name;
	}//FinMethod
	
	public void setNum_sessions(int[] num_sessions) {
		this.num_sessions = num_sessions;
	}//FinMethod

	
	public Evenement(int id,String name,String[] parameters_name,String[] parameters) {
		this.id = id;
		this.name = name;
		this.parameters_name = parameters_name;
		this.parameters = parameters;
	}//FinMethod
	
	public String parameter_search(String name) {
		for(int i = 0; i < this.parameters_name.length ;i++) {
			if(parameters_name[i].equals(name)) {
				return this.parameters[i];
			}
		}
		return "";
	}//FinMethod
	
}//FinClass


