package Constraint_model;

import java.util.Vector;

public class ElementStrategie {
	public String var;
	public String filterScope;
	public String filter;
	public String valueOrdering;
	public String varOrdering;
	public Vector<Integer> ranks;
	public int max_rank;
	public int id;
	
	public ElementStrategie(int c,String var,String fs,String f,String vo1,String vo2,int maxSessions) {
		this.id = c;
		this.var = var;
		this.filterScope = fs;
		this.filter = f;
		this.valueOrdering = vo2;
		this.varOrdering = vo1;
		this.ranks = new Vector<Integer>();
		this.max_rank = maxSessions;
		 if(fs.equals("rank")) {
			 generateRanks();
		 }
	}//FinMethod
	
	public void generateRanks() {
		String[] fsplit = filter.split(",");
		this.ranks = new Vector<Integer>();
		for(int i = 0; i < fsplit.length ;i++) {
			if(fsplit[i].matches("(\\d+)-(\\d+)")) {
				String[] g2 = fsplit[i].split("-"); 
				for(int c = Integer.parseInt(g2[0]); c <= Integer.parseInt(g2[1]) ;c++) {
					ranks.add(c);
				}
			}
			else if(fsplit[i].matches("(\\d+)-\\*")) {
				String[] g2 = fsplit[i].split("-"); 
				for(int c = Integer.parseInt(g2[0]); c <= this.max_rank ;c++) {
					ranks.add(c);
				}
			}
			else if(fsplit[i].matches("\\*")){
				for(int c = 1; c <= this.max_rank ;c++) {
					ranks.add(c);
				}
			}
			else {
				ranks.add(Integer.parseInt(fsplit[i]));
			}
		}
	}//FinMethod
	public String toString() {
		String out = "";
		out += "id:"+this.id+", var :"+this.var+", filterScope:"+this.filterScope+",filter:"+this.filter+", valueOrdering:"+this.valueOrdering+", varOrdering:"+this.varOrdering+"\n";
		out += toStringRanks()+"\n";
		return out;
	}//FinMethod
	public String toStringRanks() {
		String out = "";
		for(int i = 0; i  < ranks.size();i++) {
			out+=" "+ranks.get(i);
		}
		return out;
	}//FinMethod
}//FinClass
