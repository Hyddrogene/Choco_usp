package Constraint_model.InstanceUTPGraph;

import java.util.Vector;

public class Constraint {
	//Attribute
	private int rule;
	private int cpt;
	private String constraint;
	private String hardness;
	private int arity;
	private String[] type;
	private int[] elements;
	private Vector<Vector<Session>> sessions;
	private int[] parameters;
	
	//Method


	public Constraint(int rule, int cpt, String constraint, String hardness, int arity, String[] type, int[] elements,
			Vector<Vector<Session>> sessions, int[] parameters) {
		this.rule = rule;
		this.cpt = cpt;
		this.constraint = constraint;
		this.hardness = hardness;
		this.arity = arity;
		this.type = type;
		this.elements = elements;
		this.sessions = sessions;
		this.parameters = parameters;
	}//FinMethod

	public int getRule() {
		return rule;
	}//FinMethod

	public int getCpt() {
		return cpt;
	}//FinMethod

	public String getConstraint() {
		return constraint;
	}//FinMethod

	public String getHardness() {
		return hardness;
	}//FinMethod

	public int getArity() {
		return arity;
	}//FinMethod

	public String[] getType() {
		return type;
	}//FinMethod

	public int[] getElements() {
		return elements;
	}//FinMethod

	public Vector<Vector<Session>> getSessions() {
		return sessions;
	}//FinMethod

	public int[] getParameters() {
		return parameters;
	}//FinMethod

}//FinClass
