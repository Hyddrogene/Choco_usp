package Constraint_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class StrategieBuilt {
	public String[] hard_constraint;
	public String[] flatten_constraint;
	public InstanceUTPArray utp;
	public String timeout;
	public Vector<Integer> deactivate_rule;
	public Vector<Integer> deactivate_constraint;
	
	private String strategie;
	private JSONObject jsonObject;
	
	public ElementStrategie[] strategies;
	
	public StrategieBuilt(InstanceUTPArray utp,String strategie) {
		this.utp = utp;
		this.timeout = "";
		if(!strategie.equals("")) {
			readFile(strategie);
			createStrategie();
		}
	}//FinMethod
	
	public void readFile(String f) {
		this.strategie = f;
		File file = new File(this.strategie);
		JSONParser parser = new JSONParser();
		
		try {
			 this.jsonObject = (JSONObject) parser.parse(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//FinMethod
	
	public Vector<Integer> convertToNumberList(String input) {
		Vector<Integer> tab = new Vector<Integer>();
		String[] fsplit = input.split(",");
		
		for(int i = 0; i < fsplit.length ;i++) {
			if(fsplit[i].matches("(\\d+)-(\\d+)")) {
				String[] g2 = fsplit[i].split("-"); 
				for(int c = Integer.parseInt(g2[0]); c <= Integer.parseInt(g2[1]) ;c++) {
					tab.add(c);
				}
			}
			else if(fsplit[i].matches("(\\d+)")) {
				tab.add(Integer.parseInt(fsplit[i]));
			}
			else {
				System.out.println("Wrong format in deactivate_rules");
			}
			}
		return tab;
	}//FinMethod
	
	
	public void  createStrategie() {
		//vars
		JSONArray varsArray = (JSONArray) this.jsonObject.get("vars");
		this.strategies = new ElementStrategie[varsArray.size()];
		for(int i = 0; i < varsArray.size() ;i++) {
			JSONObject obj = (JSONObject)varsArray.get(i);
			String[] pts = new String[0];
			Iterator<?> it = obj.values().iterator();
			while(it.hasNext()) {
				String pt = ",\"";
				pts = it.next().toString().split(pt);	
			}
			String[] g = pts[0].replaceAll("\\[|\\{|\\}|\"|\\]", "").split(":");
			String g1 = pts[1].replaceAll("\\[|\\{|\\}|\"|\\]", "");
			String g2 = pts[2].replaceAll("\\[|\\{|\\}|\"|\\]", "");
			ElementStrategie strTmp =  new ElementStrategie(i,obj.keySet().toArray()[0].toString(),g[0],g[1],g1,g2,this.utp.max_part_sessions);
			this.strategies[i] = strTmp;
			//System.out.println(strTmp.toString());
		}
		//constraint_hard
		JSONArray hConstraint = (JSONArray) this.jsonObject.get("constraint_hard");
		this.hard_constraint = new String[hConstraint.size()];
		for(int i = 0; i < hConstraint.size() ;i++) {
			this.hard_constraint[i] = hConstraint.get(i).toString().replaceAll("\"", "");
		}
		//constraint_soft
		JSONArray sConstraint = (JSONArray) this.jsonObject.get("constraint_soft");
		this.flatten_constraint = new String[sConstraint.size()];
		for(int i = 0; i < sConstraint.size() ;i++) {
			this.flatten_constraint[i] = sConstraint.get(i).toString().replaceAll("\"", "");
		}
		
		this.timeout = (String)this.jsonObject.get("time_out");
		
		JSONArray deact_rules = (JSONArray) this.jsonObject.get("deactivate_rules");
		this.deactivate_rule = new Vector<Integer>();
		this.deactivate_constraint = new Vector<Integer>();
		for(int i = 0; i < deact_rules.size() ;i++) {
			JSONObject obj = (JSONObject)deact_rules.get(i);
			String res = (String)obj.get(obj.keySet().toArray()[0].toString());
			Vector<Integer> resTab = convertToNumberList(res);
			if(obj.keySet().toArray()[0].toString().equals("rules") || obj.keySet().toArray()[0].toString().equals("rule")) {
				for(int j = 0; j < resTab.size() ;j++) {
					this.deactivate_rule.add(resTab.get(j));
				}
			}
			else if (obj.keySet().toArray()[0].toString().equals("constraints")|| obj.keySet().toArray()[0].toString().equals("constraint")) {
				for(int j = 0; j < resTab.size() ;j++) {
					this.deactivate_constraint.add(resTab.get(j));
				}
			}
			else {
				System.out.println("Error deactivate_rules the entry is wrong");
				System.exit(0);
			}
		}
		String outRul = "deactivate_rule : ";
		for(int i = 0 ; i < this.deactivate_rule.size() ;i++) {
			outRul += this.deactivate_rule.get(i)+",";
		}
		System.out.println(outRul);
		//this.timeout = timeoutObj.values().toString().replaceAll("\\[|\\{|\\}|\"|\\]", "");  
		//System.out.println(Arrays.toString(this.hard_constraint));
		//System.out.println(Arrays.toString(this.flatten_constraint));
	}//FinMethod
	public String toString() {
		String out = "";
		for(int i = 0; i < this.strategies.length ;i++) {
			out += this.strategies[i].toString();
		}
		out += Arrays.toString(this.hard_constraint);
		out += Arrays.toString(this.flatten_constraint);
		return out;
	}//FinMethod
}//FinClass
