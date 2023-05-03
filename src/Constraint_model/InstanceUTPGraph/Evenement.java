package Constraint_model.InstanceUTPGraph;

public class Evenement {
	//Attribut
	public int cpt;
	public String id;
	public String name;
	public Label[] labels;
	
	public SelectorEvenement selector;
	public FilterEvenement filter;
	//Constructor
	public Evenement(String id,int cpt,String name,SelectorEvenement s,FilterEvenement f,Label[] labels) {
		this.id = id;
		this.cpt = cpt;
		this.name = name;
		this.selector = s;
		this.filter = f;
	}//FinMethod
	
	//Method
	public int getCpt() {
		return cpt;
	}//FinMethod
	
	public String getId() {
		return id;
	}//FinMethod
	
	public String getName() {
		return name;
	}//FinMethod
	
	public Label[] getLabels() {
		return labels;
	}//FinMethod
}//FinClass
