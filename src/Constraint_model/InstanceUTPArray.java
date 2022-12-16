package Constraint_model;
import java.util.Vector;

public class InstanceUTPArray {
	//Attribute
	//DATA
	public InstanceUTP UTPInstance;
	public int nr_weeks;
	public int nr_days_per_week;
	public int nr_slots_per_day;
	
	public int nr_slot;	
	
	public int nr_courses;
	public int nr_parts;
	public int nr_classes;
	public int nr_sessions;
	public int max_part_sessions;
	//public int[][] course_parts;
	public Vector<Vector<Integer>> course_parts;
	//public int[][] part_classes;
	public Vector<Vector<Integer>> part_classes;
	public int[] part_nr_sessions;
	public int nr_equipments;
	public int nr_rooms;
	public int nr_teachers;
	public int nr_students;
	public int nr_part_rooms;
	//public int[][] part_rooms;
	public Vector<Vector<Integer>> part_rooms;
	public int nr_part_teachers;
	//public int[][] part_teachers;
	public Vector<Vector<Integer>> part_teachers;
	//public int[][] part_dailyslots;
	public Vector<Vector<Integer>> part_dailyslots;
	//public int[][] part_days;
	public Vector<Vector<Integer>> part_days;
	//public int[][] part_weeks;
	public Vector<Vector<Integer>> part_weeks;
	public int[] part_session_length; 
	public int max_equipment_count;
	public int max_class_maxheadcount;
	public int max_teacher_session;
	public int max_teacher_sessions;
	public int[] equipment_count;
	public int max_room_capacity;
	public int[] room_capacity;
	public String[] part_room_use;
	public int nr_part_room_mandatory;
	public int[] part_room_mandatory;
	public int[][] part_teacher_sessions_count;
	//public Vector<Vector<Integer>> part_teacher_sessions_count;
	public int[] part_session_teacher_count;
	public int[] class_maxheadcount;
	public int[] class_parent;
	//public int[][] student_courses;
	public Vector<Vector<Integer>> student_courses;
	public String[] equipment_name;
	public String[] room_name;
	public String[] teacher_name;
	public String[] student_name;
	public String[] course_name;
	public String[] part_name;
	public String[] class_name;
	public int nr_labels;
	public String[] label_name;
	//public int[][] room_label;
	public Vector<Vector<Integer>> room_label;
	//public int[][] teacher_label;
	public Vector<Vector<Integer>> teacher_label;
	//public int[][] student_label;
	public Vector<Vector<Integer>> student_label;
	//public int[][] course_label;
	public Vector<Vector<Integer>> course_label;
	//public int[][] part_label;
	public Vector<Vector<Integer>> part_label;
	//RULES
	public int nr_rules;
	public int nr_scopes;
	//public int[][] rule_scopes;
	public Vector<Vector<Integer>> rule_scopes;
	public String[] scope_type;
	public int mask_length;
	//public int[][] scope_mask;
	public Vector<Vector<Integer>> scope_mask;
	public int nr_filters;
	//public int[][] scope_filters;
	public Vector<Vector<Integer>> scope_filters;
	public String[] filter_type;
	//public int[][] filter_elements;
	public Vector<Vector<Integer>> filter_elements;
	public String[] rule_constraint;
	public String[] constraint_hardness;
	public int nr_parameters;
	//public int[][] constraint_parameters;
	public Vector<Vector<Integer>> constraint_parameters;
	public String[] parameter_name;
	public String[] parameter_type;
	public int max_paramater_value;
	public int max_parameter_size;
	//public int[][] parameter_value;
	public Vector<Vector<String>> parameter_value;
	//SOLUTION
		//GROUPS
	public int nr_groups;
	public int max_group_headcount;
	public int[] group_headcount;
	public String[] group_name;
	//public int[][] group_students;
	public Vector<Vector<Integer>> group_students;
	//public int[][] group_classes;
	public Vector<Vector<Integer>> group_classes;
	//public int[][] group_sessions;
	public Vector<Vector<Integer>> group_sessions;
	//public int[][] class_groups;
	public Vector<Vector<Integer>> class_groups;
	//CONSTRAINTS
	public Vector<ConstraintUTP> constraints;
	
	public Vector<Vector<Integer>> part_slots;
	public int[] class_part;
	public int[] part_course;
	public int[] session_class;
	public int[] session_rank;
	public Vector<Vector<Integer>> class_sessions;
	public Vector<Vector<Integer>> teacher_parts;
	public Vector<Vector<Integer>> room_parts;
	//Method
	
	public int nr_slot() {
		this.nr_slot = nr_weeks*nr_days_per_week*nr_slots_per_day;
		return this.nr_slot;
	}//FinMethod
	
	public void part_slots(){
		Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(nr_parts);
		for(int i = 0 ; i < nr_parts ;i++) {
			Vector<Integer> tabTmp = new Vector<Integer>();
			for(int k = 0; k < part_weeks.get(i).size();k++) {
				for(int l = 0; l < part_days.get(i).size();l++) {
					for(int m = 0; m < part_dailyslots.get(i).size();m++) {
						int val = part_dailyslots.get(i).get(m) + ((part_days.get(i).get(l)-1) * nr_slots_per_day) + ((part_weeks.get(i).get(k)-1) * (nr_slots_per_day * nr_days_per_week));
						tabTmp.add(val);
					}
				}
			}
			tab.add(tabTmp);
		}
		this.part_slots= tab; 
	}//FinMethod
	
	public void class_part() {
		int[] class_part = new int[nr_classes];
		int i = 0;
		for (int p = 0; p < nr_parts ;p++) {
			for(int c = 0 ; c < part_classes.get(p).size() ; c++) {
				class_part[i] = p+1;
				i++;
			}
		}
		this.class_part = class_part;
	}//FinMethod
	
	public void part_course() {
		int[] part_course = new int[nr_parts];
		System.out.println(nr_courses);
		System.out.println(nr_parts);
		int i = 0;
		for (int p = 0; p < nr_courses ;p++) {
			System.out.println(p);
			for(int c = 0 ; c < course_parts.get(p).size() ; c++) {
				part_course[i] = p+1;
				i++;
			}
		}
		this.part_course = part_course;
	}//FinMethod
	
	public void session_class() {
		int[] session_class = new int[nr_sessions];
		int i = 0;
		for (int p = 0; p < nr_classes ;p++) {
			for(int c = 0 ; c < class_sessions.get(p).size() ; c++) {
				session_class[i] = p+1;
				i++;
			}
		}
		this.session_class = session_class;
	}//FinMethod
	
	
	public void class_sessions() {
		Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(); 
		int s = 1;
		for (int c = 0 ; c < nr_classes ;c++) {
			Vector<Integer> tabTmp = new Vector<Integer>();
			for(int i = 0;i < part_nr_sessions[class_part[c]-1] ;i++) {
				tabTmp.add(s);
				s++;
			}
			tab.add(tabTmp);
		}
		this.class_sessions = tab;
	}//FinMethod
	
	public void session_rank() {
		int[] session_rank = new int[nr_sessions];
		int i = 0;
		for (int p = 0; p < nr_classes ;p++) {
			for(int c = 0 ; c < class_sessions.get(p).size() ; c++) {
				session_rank[i] = c+1;
				i++;
			}
		}
		this.session_rank = session_rank;
	}//FinMethod
	
	public void teacher_parts() {
		Vector<Vector<Integer>> teacher_parts = new Vector<Vector<Integer>>(nr_teachers);
		for(int i = 0; i < nr_teachers ;i++) {
			Vector<Integer> tabTmp = new Vector<Integer>();
			for(int p = 0; p < nr_parts ;p++) {
				for(int t = 0; t < part_teachers.get(p).size() ;t++) {
					if(part_teachers.get(p).get(t) == (i+1)) {
						tabTmp.add(p+1);
					}
				}
			}
			teacher_parts.add(tabTmp);
		}
		this.teacher_parts = teacher_parts;
	}//FinMethod
	
	public void room_parts() {
		Vector<Vector<Integer>> room_parts = new Vector<Vector<Integer>>(nr_rooms);
		for(int i = 0; i < nr_rooms ;i++) {
			Vector<Integer> tabTmp = new Vector<Integer>();
			for(int p = 0; p < nr_parts ;p++) {
				for(int t = 0; t < part_rooms.get(p).size() ;t++) {
					if(part_rooms.get(p).get(t) == (i+1)) {
						tabTmp.add(p+1);
					}
				}
			}
			room_parts.add(tabTmp);
		}
		this.room_parts = room_parts;
	}//FinMethod
	
	public void calcul() {
		nr_slot();
		part_slots();
		part_course();
		class_part();
		class_sessions();
		session_class();
		session_rank();
		teacher_parts();
		room_parts();
	}//FinMethod

}//FinClass
