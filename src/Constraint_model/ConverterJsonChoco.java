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

public class ConverterJsonChoco {
	//Attribute
	private String filename;
	private JSONParser parser;
	private File file;
	private JSONObject jsonObject;
	private InstanceUTPArray instanceUTPArray;

	//Method
	public ConverterJsonChoco(String filename) {
		this.filename = filename;
		this.parser = new JSONParser();
		this.file = new File(this.filename);
		readJsonFile();
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
		JSONObject DATA = (JSONObject)jsonObject.get("DATA");
		this.instanceUTPArray.nr_weeks = convertToInt(DATA,"nr_weeks");
		this.instanceUTPArray.nr_days_per_week =  convertToInt(DATA,"nr_days_per_week");
		this.instanceUTPArray.nr_slots_per_day = convertToInt(DATA,"nr_slots_per_day");
		this.instanceUTPArray.nr_courses = convertToInt(DATA,"nr_courses");
		this.instanceUTPArray.nr_parts = convertToInt(DATA,"nr_parts");
		this.instanceUTPArray.nr_classes = convertToInt(DATA,"nr_classes");
		this.instanceUTPArray.nr_sessions = convertToInt(DATA,"nr_sessions");
		this.instanceUTPArray.max_part_sessions = convertToInt(DATA,"max_part_sessions");
		this.instanceUTPArray.nr_equipments = convertToInt(DATA,"nr_equipments");
		this.instanceUTPArray.nr_rooms = convertToInt(DATA,"nr_rooms");
		this.instanceUTPArray.nr_teachers = convertToInt(DATA,"nr_teachers");
		this.instanceUTPArray.nr_students = convertToInt(DATA,"nr_students");
		this.instanceUTPArray.nr_part_rooms = convertToInt(DATA,"nr_part_rooms");
		this.instanceUTPArray.nr_part_teachers = convertToInt(DATA,"nr_part_teachers");
		this.instanceUTPArray.max_equipment_count = convertToInt(DATA,"max_equipment_count");
		this.instanceUTPArray.max_class_maxheadcount = convertToInt(DATA,"max_class_maxheadcount");
		this.instanceUTPArray.max_teacher_session = convertToInt(DATA,"max_teacher_session");
		this.instanceUTPArray.max_teacher_sessions = convertToInt(DATA,"max_teacher_sessions");
		this.instanceUTPArray.max_room_capacity = convertToInt(DATA,"max_room_capacity");
		this.instanceUTPArray.nr_part_room_mandatory = convertToInt(DATA,"nr_part_room_mandatory");
		this.instanceUTPArray.nr_labels = convertToInt(DATA,"nr_labels");
		JSONObject RULES = (JSONObject)jsonObject.get("RULES");
		this.instanceUTPArray.nr_rules = convertToInt(RULES,"nr_rules");
		this.instanceUTPArray.nr_scopes = convertToInt(RULES,"nr_scopes");
		this.instanceUTPArray.mask_length = convertToInt(RULES,"mask_length");
		this.instanceUTPArray.nr_filters = convertToInt(RULES,"nr_filters");
		this.instanceUTPArray.nr_parameters = convertToInt(RULES,"nr_parameters");
		this.instanceUTPArray.max_paramater_value = convertToInt(RULES,"max_paramater_value");
		this.instanceUTPArray.max_parameter_size = convertToInt(RULES,"max_parameter_size");
		JSONObject SOLUTION = (JSONObject)jsonObject.get("SOLUTION");
		JSONObject GROUPS = (JSONObject)SOLUTION.get("GROUPS");
		this.instanceUTPArray.nr_groups = convertToInt(GROUPS,"nr_groups");
		this.instanceUTPArray.max_group_headcount = convertToInt(GROUPS,"max_group_headcount");
		createArray(DATA,RULES,SOLUTION);
		createConstraints();
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
		System.out.println(f);
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
	
	public InstanceUTPArray createArray(JSONObject DATA,JSONObject RULES, JSONObject SOLUTION) {
		//Array DATA
		this.instanceUTPArray.course_parts = createSetArray(DATA,"course_parts");
		this.instanceUTPArray.part_classes = createSetArray(DATA,"part_classes");
		this.instanceUTPArray.part_rooms = createSetArray(DATA,"part_rooms");
		this.instanceUTPArray.part_teachers = createSetArray(DATA,"part_teachers");
		this.instanceUTPArray.part_dailyslots = createSetArray(DATA,"part_dailyslots");
		this.instanceUTPArray.part_weeks = createSetArray(DATA,"part_weeks");
		this.instanceUTPArray.part_days = createSetArray(DATA,"part_days");
		//this.instanceUTPArray.part_teacher_sessions_count = createSetArray(DATA,"part_teacher_sessions_count");
		this.instanceUTPArray.student_courses = createSetArray(DATA,"student_courses");
		this.instanceUTPArray.room_label = createSetArray(DATA,"room_label");
		this.instanceUTPArray.teacher_label = createSetArray(DATA,"teacher_label");
		this.instanceUTPArray.student_label = createSetArray(DATA,"student_label");
		this.instanceUTPArray.course_label = createSetArray(DATA,"course_label");
		this.instanceUTPArray.part_label = createSetArray(DATA,"part_label");
		
		this.instanceUTPArray.part_nr_sessions = createIntArray(DATA,"part_nr_sessions");
		this.instanceUTPArray.part_session_length = createIntArray(DATA,"part_session_length");
		this.instanceUTPArray.equipment_count = createIntArray(DATA,"equipment_count");
		this.instanceUTPArray.room_capacity = createIntArray(DATA,"room_capacity");
		//this.instanceUTPArray.part_room_mandatory = createIntArray(DATA,"part_room_mandatory");
		this.instanceUTPArray.part_session_teacher_count = createIntArray(DATA,"part_session_teacher_count");
		this.instanceUTPArray.class_maxheadcount = createIntArray(DATA,"class_maxheadcount");
		this.instanceUTPArray.class_parent = createIntArray(DATA,"class_parent");
		
		this.instanceUTPArray.part_teacher_sessions_count = createIntArray2d(DATA,"part_teacher_sessions_count");
		this.instanceUTPArray.part_room_mandatory = createIntArray2d(DATA,"part_room_mandatory");
		//print(this.instanceUTPArray.part_teacher_sessions_count);
		
		this.instanceUTPArray.part_room_use = createStringArray(DATA,"part_room_use");
		this.instanceUTPArray.equipment_name = createStringArray(DATA,"equipment_name");
		this.instanceUTPArray.room_name = createStringArray(DATA,"room_name");
		this.instanceUTPArray.teacher_name = createStringArray(DATA,"teacher_name");
		this.instanceUTPArray.student_name = createStringArray(DATA,"student_name");	
		this.instanceUTPArray.course_name = createStringArray(DATA,"course_name");
		this.instanceUTPArray.part_name = createStringArray(DATA,"part_name");
		this.instanceUTPArray.class_name = createStringArray(DATA,"class_name");
		this.instanceUTPArray.label_name = createStringArray(DATA,"label_name");
		
		//Array RULES
		this.instanceUTPArray.rule_scopes = createSetArray(RULES,"rule_scopes");
		this.instanceUTPArray.scope_mask = createSetArray(RULES,"scope_mask");
		this.instanceUTPArray.scope_filters = createSetArray(RULES,"scope_filters");
		this.instanceUTPArray.filter_elements = createSetArray(RULES,"filter_elements");
		this.instanceUTPArray.constraint_parameters = createSetArray(RULES,"constraint_parameters");
		this.instanceUTPArray.parameter_value = createSetStringArray(RULES,"parameter_value");//TODO createSetStringArray
	
		
		this.instanceUTPArray.scope_type = createStringArray(RULES,"scope_type");
		this.instanceUTPArray.filter_type = createStringArray(RULES,"filter_type");
		this.instanceUTPArray.rule_constraint = createStringArray(RULES,"rule_constraint");
		this.instanceUTPArray.constraint_hardness = createStringArray(RULES,"constraint_hardness");
		this.instanceUTPArray.parameter_name = createStringArray(RULES,"parameter_name");
		this.instanceUTPArray.parameter_type = createStringArray(RULES,"parameter_type");
		
		//Array SOLUTION
			//Array GROUPS
		JSONObject GROUPS = (JSONObject)SOLUTION.get("GROUPS");
		this.instanceUTPArray.group_students = createSetArray(GROUPS,"group_students");
		this.instanceUTPArray.group_classes = createSetArray(GROUPS,"group_classes");
		this.instanceUTPArray.group_sessions = createSetArray(GROUPS,"group_sessions");
		
		this.instanceUTPArray.group_headcount = createIntArray(GROUPS,"group_headcount");

		this.instanceUTPArray.group_name = createStringArray(GROUPS,"group_name");
		
			//Array CLASS
		JSONObject CLASS = (JSONObject)SOLUTION.get("CLASS");
		this.instanceUTPArray.class_groups = createSetIntArray2d(CLASS,"class_groups");
		//print(this.instanceUTPArray.class_groups);
		
		//this.instanceUTPArray.course_parts
		return this.instanceUTPArray;
	}//FinMethod
	
	
}//FinClass
