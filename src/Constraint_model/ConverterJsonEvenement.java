package Constraint_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Constraint_model.InstanceUTPGraph.Label;

public class ConverterJsonEvenement {
	//Attribute
		private String filename;
		private JSONParser parser;
		private File file;
		private JSONObject jsonObject;
		private InstanceUTPArray instanceUTPArray;

		//Method
		public ConverterJsonEvenement(String filename) {
			this.filename = filename;
			this.parser = new JSONParser();
			this.file = new File(this.filename);
			readJsonFile();
			CreateInstance();
		}//FinMethod
		
		public void readJsonFile() {
			try {
				 this.jsonObject = (JSONObject) this.parser.parse(new FileReader(file));
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
		
		public int convertToInt(JSONObject j,String val) {
			Long i = (Long)j.get(val);
			return i.intValue();
		}//FinMethod
		
		public InstanceUTPArray getInstanceUTPArray() {
			return instanceUTPArray;
		}//FinMethod
		
		public InstanceUTPArray CreateInstance() {
			this.instanceUTPArray = new InstanceUTPArray();
			JSONArray EVENEMENTS = (JSONArray)jsonObject.get("EVENEMENTS");
			
			createArray(EVENEMENTS);
			return this.instanceUTPArray;
		}//FinMethod
		
		public void createConstraints() {
			JSONArray CONSTRAINTS = (JSONArray)jsonObject.get("CONSTRAINTS"); 
			int cpt = 1;
			Vector<ConstraintUTP> constraints = new Vector<ConstraintUTP>(CONSTRAINTS.size());
			for(int i = 0 ; i < CONSTRAINTS.size() ;i++) {
				JSONObject j = (JSONObject) CONSTRAINTS.get(i);
				Long rule = (Long) j.get("rule");
				String cons = (String) j.get("constraint");
				String hardness = (String) j.get("hardness");
				Long arity = (Long) j.get("arity");
				String[] type = createStringArray(j,"type");
				int[] elements = createIntArray(j,"elements");
				int[] parameters = createIntArray(j,"parameters");
				Vector<Vector<Integer>> sessions = createSetIntArray2dConstraint(j,"sessions");
				constraints.add(new ConstraintUTP(cpt,rule.intValue(),cons,hardness,arity.intValue(),type,elements,sessions,parameters));
				cpt++;
			}
			this.instanceUTPArray.constraints = constraints;
			this.instanceUTPArray.calcul();
			//print_constraints(constraints);
		}//FinMethod
		
		public void print_constraints(Vector<ConstraintUTP> t) {
			for(int i = 0 ; i < t.size()  ; i++) {
				System.out.println(t.get(i).ToString());
			}
		}//FinMethod
		
		@SuppressWarnings("unchecked")
		public Vector<Vector<Integer>> createSetArray(JSONObject j,String f){
			JSONArray setArray = (JSONArray) j.get(f);
			Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(setArray.size());
			Iterator<JSONObject> it = setArray.iterator();
			JSONObject tmp;
			while (it.hasNext()) {
				tmp =  it.next();
				JSONArray tabi = (JSONArray)tmp.get("set");
				Vector<Integer> tabTmp = new Vector<Integer>(tabi.size());
				for(int i = 0 ; i<tabi.size() ; i++) {
					Long itt = (Long) tabi.get(i);
					tabTmp.add(itt.intValue());
				}
				tab.add(tabTmp);
			}
			return tab;
		}//FinMethod
		
		public Vector<Vector<String>> createSetStringArray(JSONObject j,String f){
			JSONArray setArray = (JSONArray) j.get(f);
			Vector<Vector<String>> tab = new Vector<Vector<String>>(setArray.size());
			
			for(int k = 0; k<setArray.size() ;k++ ) {
				JSONArray sizeo = (JSONArray) setArray.get(k);
				Vector<String> tabTmp = new Vector<String>(sizeo.size());
				for(int l = 0 ; l < sizeo.size() ;l++) {
					String itt = (String)sizeo.get(l).toString();
					tabTmp.add(itt);
				}
				tab.add(tabTmp);
			}
			return tab;
		}//FinMethod
		
		public Vector<Vector<Integer>> createSetIntArray2dConstraint(JSONObject j,String f){
			JSONArray setArray = (JSONArray) j.get(f);
			Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(setArray.size());
			
			for(int k = 0; k<setArray.size() ;k++ ) {
				JSONObject sizeo = (JSONObject) setArray.get(k);
				Vector<Integer> tabTmp = new Vector<Integer>(sizeo.size());
				JSONArray itt1 = (JSONArray) sizeo.get("set");
				for(int l = 0 ; l < itt1.size() ;l++) {
					Long itt1i = (Long) itt1.get(l);
					tabTmp.add(itt1i.intValue());
				}
				tab.add(tabTmp);
			}
			return tab;
		}//FinMethod
		
		public Vector<Vector<Integer>> createSetIntArray2d(JSONObject j,String f){
			JSONArray setArray = (JSONArray) j.get(f);
			Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(setArray.size());
			for(int k = 0; k<setArray.size() ;k++ ) {
				JSONArray sizeo = (JSONArray) setArray.get(k);
				Vector<Integer> tabTmp = new Vector<Integer>(sizeo.size());
				JSONObject sizeo1 = (JSONObject) sizeo.get(0);
				JSONObject sizeo2 = (JSONObject) sizeo.get(1);
				JSONArray itt1 = (JSONArray) sizeo1.get("set");
				JSONArray itt2 = (JSONArray) sizeo2.get("set");
				Long itt1i = (Long) itt1.get(0);
				tabTmp.add(itt1i.intValue());
				for(int l = 0 ; l < itt2.size() ;l++) {
					Long itt2i = (Long) itt2.get(l);
					tabTmp.add(itt2i.intValue());
				}
				tab.add(tabTmp);
			}
			return tab;
		}//FinMethod
		
		public void print(Vector<Vector<Integer>> tab){
			String tt = "";
			for(int i = 0;i < tab.size();i++ ) {
				for(int j = 0 ; j < tab.get(i).size();j++ ) {
					tt += tab.get(i).get(j)+" ";
				}
				tt += "\n";
			}
			System.out.println(tt);
		}//FinMethod
		
		public void print(int[][] tab){
			String tt = "";
			for(int i = 0;i < tab.length;i++ ) {
				for(int j = 0 ; j < tab[i].length;j++ ) {
					tt += tab[i][j]+" ";
				}
				tt += "\n";
			}
			System.out.println(tt);
		}//FinMethod
		
		@SuppressWarnings("unchecked")
		public int[] createIntArray(JSONObject j,String f) {
			JSONArray setArray = (JSONArray) j.get(f);
			Iterator<Long> it = setArray.iterator();
			int[] tab = new int[setArray.size()];
			int i = 0;
			while (it.hasNext()) {
				Long it_value  =  it.next();
				tab[i] = it_value.intValue();
				i++;
			}
			return tab;
		}//FinMethod
		
		public int[][] createIntArray2d(JSONObject j,String f) {
			//System.out.println(f);
			JSONArray setArray = (JSONArray) j.get(f);
			if(setArray.size() <= 0){return new int[0][0] ;}
			else {
				JSONArray sizeo = (JSONArray)setArray.get(0);
				int[][] tab = new int[setArray.size()][sizeo.size()];
				for(int k = 0 ; k < setArray.size() ;k++) {
					JSONArray setArrayi = (JSONArray) setArray.get(k);
					for(int l = 0 ; l < sizeo.size() ; l++) {
						Long m = (Long) setArrayi.get(l);
						tab[k][l] = m.intValue();
					}
				}
				return tab;
			}
		}//FinMethod
		
		@SuppressWarnings("unchecked")
		public String[] createStringArray(JSONObject j,String f) {
			JSONArray setArray = (JSONArray) j.get(f);
			Iterator<String> it = setArray.iterator();
			String[] tab = new String[setArray.size()];
			int i = 0;
			while (it.hasNext()) {
				tab[i]  =  it.next();
				i++;
			}
			return tab;
		}//FinMethod
		
		public InstanceUTPArray createArray(JSONArray Obj_json) {
			//Array DATA

			//Array SOLUTION
				//Array GROUPS
			int counter = 0;
			for(int i = 0; i < Obj_json.size() ;i++) {
				JSONObject evenementJSON = (JSONObject)Obj_json.get(i);
				String name = (String)evenementJSON.get("name");
				String id = (String)evenementJSON.get("id");
				JSONObject selector = (JSONObject)evenementJSON.get("selector");
				JSONObject filter = (JSONObject)evenementJSON.get("filter");
				
				
				
				//Evenement evenement = new Evenement(id,counter,name,new Label[0]);
				counter++;
				if (filter!= null) {
					System.out.println((String)evenementJSON.get("id")+" "+filter.values());
				}
				else {
					System.out.println((String)evenementJSON.get("id"));
				}
				
			}

			
		
			//print(this.instanceUTPArray.class_groups);
			

			
			//this.instanceUTPArray.course_parts
			return this.instanceUTPArray;
		}//FinMethod
}//FinClass
