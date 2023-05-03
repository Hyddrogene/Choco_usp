package Constraint_model;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class InstanceUTPArray {
	//Attribute
	static public String[] constraint_hard = new String[] {
			"implicite_sequenced_sessions",
			"teacher_service",
			"disjunctive_teacher",
			"disjunctive_group",
			"disjunctive_room",
			"size_of_multiroom"
	};
	static public String[] constraint_soft = new String[] {
			"sameSlot",
			"forbiddenPeriod",
			"sameTeachers",
			"sameRooms",
			"periodic",
			"weekly",
			"allowedPeriod",
			"sequenced",
			"sameWeek",
			"assignRoom",
			"forbiddenRooms",
			"sameWeekDay",
			"sameWeeklySlot",
			"differentWeekDay",
			"differentWeek",
			"differentSlots",
			"disjunct"
	}; 
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
	public int[][] part_room_mandatory;
	//public int[] part_room_mandatory;
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
		//SESSIONS
	public int[] session_week;
	public int[] session_day;
	public int[] session_slot;
	public Vector<Vector<Integer>> session_teachers;
	public Vector<Vector<Integer>> session_rooms;
	
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
	
	public int[] class_multiple_teacher;
	public int nr_class_multiple_teacher;
	public int[] class_position_multiple_teacher;
	public int[] class_multiple_room;
	public int nr_class_multiple_room;
	public int[] class_position_multiple_room;
	public int[] part_room_worst_case;
	public int[] room_capacity_v2;
	public int[] student_group;
	
	public int[] session_xrooms;
	public int vars_room;
	public int var_same_room;
	
	public int[] session_xteachers;
	public int vars_teachers;
	public int var_same_teachers;
	
	public int[] class_rank;
	
	public Vector<Vector<Vector<Integer>>> group_of_classes_eq;
	
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
		//System.out.println(nr_courses);
		//System.out.println(nr_parts);
		int i = 0;
		for (int p = 0; p < nr_courses ;p++) {
			//System.out.println(p);
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
	
	public void class_groups() {
		Vector<Vector<Integer>> class_groups = new Vector<Vector<Integer>>(nr_classes);
		for(int c = 0 ; c < nr_classes ;c++) {
			Vector<Integer> gTmp = new Vector<Integer>();
			for(int g = 0; g < nr_groups ;g++) {
				for(int i = 0; i < group_classes.get(g).size() ;i++)
				if(group_classes.get(g).get(i)-1 == c) {
					gTmp.add(g+1);
				}
			}
			class_groups.add(gTmp);
		}
		this.class_groups = class_groups;
	}//FinMethod
	
	public void class_multiple() {
		Vector<Integer> cl_ml = new Vector<Integer>();
		for(int i = 0; i < nr_classes ;i++) {
			if( this.part_session_teacher_count[class_part[i]-1] >1) {
				cl_ml.add(i+1);
			}
		}
		int[] class_multiple = new int[cl_ml.size()];
		for(int i = 0; i < cl_ml.size();i++) {
			class_multiple[i] = cl_ml.get(i).intValue();
		}
		this.nr_class_multiple_teacher = cl_ml.size();
		this.class_multiple_teacher = class_multiple;
	}//FinMethod
	
	public void class_multiple_teacher_position() {
		int[] class_position_multiple_teacher = new int[nr_classes];
		int j = 0;
		for(int i = 0; i < this.nr_class_multiple_teacher ;i++) {
			class_position_multiple_teacher[this.class_multiple_teacher[i]-1] = j;
			j += this.part_session_teacher_count[this.class_part[this.class_multiple_teacher[i]-1]-1];
		}
		this.class_position_multiple_teacher = class_position_multiple_teacher;
	}//FinMethod
	
	public int size_class_groups(int p) {
		int[] tab = new int[this.part_classes.get(p).size()];
		for(int i = 0; i < this.part_classes.get(p).size() ;i++) {
			int cg = this.part_classes.get(p).get(i);
			int m_s_g = 0;
			for(int c = 0; c < this.class_groups.get(cg-1).size() ;c++) {
				m_s_g += this.group_headcount[this.class_groups.get(cg-1).get(c)-1];
			}
			tab[i] = m_s_g;
		}
		int max = 0;
		for(int i =0; i < tab.length ;i++) {
			if(tab[i]>max) {
				max = tab[i];
			}
		}
		return max;
	}//FinMethod
	
	public void part_room_worst_case() {
		int[] part_room_worst_case = new int[nr_parts];

		for(int i = 0; i < nr_parts;i++) {
			if(this.part_room_use[i].equals("single")) {
				part_room_worst_case[i] = 1;
				
			}
			else {
				//System.out.println(this.part_name[i]);
				SessionRank[] tt = new SessionRank[this.part_rooms.get(i).size()];
				for(int r = 0; r < this.part_rooms.get(i).size() ;r++) {
					//System.out.println(this.room_name[this.part_rooms.get(i).get(r)-1]+" "+this.room_capacity[this.part_rooms.get(i).get(r)-1]);
					tt[r] = new SessionRank(this.part_rooms.get(i).get(r),this.room_capacity[this.part_rooms.get(i).get(r)-1]);
				}
				
				Arrays.sort(tt,new Comparator<SessionRank>() {
					@Override
					public int  compare(SessionRank sr1,SessionRank sr2) {return Integer.compare(sr1.rank, sr2.rank);}
				});
				int s_g = 0;
				int p_max = 0;
				int g_size_part = size_class_groups(i);
				for(int j = 0; j < tt.length ;j++) {
					s_g += tt[j].rank;
					//System.out.println("s_g "+s_g);
					p_max++;
					if( g_size_part <= s_g) {
						break;
					}
				}
				part_room_worst_case[i] = p_max;
			}

			//System.out.println("p_max "+p_max);
		}
		this.part_room_worst_case = part_room_worst_case;
	}//FinMethod
	
	public void class_multiple_rooms() {
		Vector<Integer> cl_ml_r = new Vector<Integer>();
		for(int i = 0; i < nr_classes ;i++) {
			if( this.part_room_use[class_part[i]-1].equals("multiple")) {
				cl_ml_r.add(i+1);
			}
		}
		int[] class_multiple_room = new int[cl_ml_r.size()];
		for(int i = 0; i < cl_ml_r.size();i++) {
			class_multiple_room[i] = cl_ml_r.get(i).intValue();
		}
		this.nr_class_multiple_room = cl_ml_r.size();
		this.class_multiple_room = class_multiple_room;
	}//FinMethod
	
	public void class_multiple_room_position() {
		int[] class_position_multiple_room = new int[nr_classes];
		int j = 0;
		for(int i = 0; i < this.nr_class_multiple_room ;i++) {
			class_position_multiple_room[this.class_multiple_room[i]-1] = j;
			j +=  this.part_room_worst_case[this.class_part[this.class_multiple_room[i]-1]-1];
		}
		this.class_position_multiple_room = class_position_multiple_room;
	}//FinMethod
	
	public void room_capacity_v2() {
		int[] room_capacity_v2 = new int[this.nr_rooms+1];
		for(int i = 1; i <= room_capacity.length ;i++) {
			room_capacity_v2[i] = room_capacity[i-1];
		}
		room_capacity_v2[0] = 0;
		this.room_capacity_v2 = room_capacity_v2;
	
	}//FinMethod
	
	public void room_name_v2() {
		String[] room_name_v2 = new String[this.nr_rooms+1];
		for(int i = 1; i <= room_name.length ;i++) {
			//System.out.println("i "+i);
			room_name_v2[i] = room_name[i-1];
		}
		room_name_v2[0] = "vide";
		this.room_name= room_name_v2;
		//System.out.println(Arrays.toString(this.room_name));
	}//FinMethod
	
	public void student_group() {
		int[] student_group = new int[nr_students];
		for(int student = 0; student < nr_students ;student++) {
			for(int group = 0; group < nr_groups ;group++) {
				for(int st = 0; st < this.group_students.get(group).size() ; st++) {
					if(this.group_students.get(group).get(st)-1 == student) {
						student_group[student] = group+1;
						group = nr_groups;
						break;//st = this.group_students.get(group).size();
					}
				}
			}
		}
		//System.out.println(Arrays.toString(student_group));
		this.student_group = student_group;
	}//FinMethod
	
	public void resizemultiplevariable() {
		for(int i = 0; i < this.nr_class_multiple_room; i++) {
			this.class_position_multiple_room[this.class_multiple_room[i]-1] = i;
		}
		for(int i = 0; i < this.nr_class_multiple_teacher; i++) {
			this.class_position_multiple_teacher[this.class_multiple_teacher[i]-1] = i;
		}
	}//FinMethod
	
	public boolean isSameVar(int session, Vector<ConstraintUTP> tab) {
		for(int i = 0; i < tab.size();i++) {
			for(int s= 0; s < tab.get(i).getSessions().get(0).size() ;s++) {
				if(tab.get(i).getSessions().get(0).get(s).intValue() == session+1) {
					return true;
				}
			}

		}
		return false;
	}//FinMethod
	
	public boolean isSameVarForThis(int session, ConstraintUTP constraint) {
		for(int s= 0; s < constraint.getSessions().get(0).size() ;s++) {
			if(constraint.getSessions().get(0).get(s).intValue() == session+1) {
				return true;
			}
		}
		return false;
	}//FinMethod
	
	public int getVarsSession(int session, Vector<ConstraintUTP> tab) {
		int res = 0;
		
		for(int i = 0; i < tab.size() ;i++) {
			if(isSameVarForThis(session,tab.get(i))) {
				return i;
			}
		}
		
		return res;
	}//FinMethod
	
	public void sessions_xrooms() {
		Vector<ConstraintUTP> tab = new Vector<ConstraintUTP>();
		for(int constraint = 0; constraint < this.constraints.size() ;constraint++) {
			if(this.constraints.get(constraint).getConstraint().equals("sameRooms")) {
				tab.add(this.constraints.get(constraint));
			}
		}
		
		int var_same = tab.size();
		int vars = var_same;
		
		for(int session = 0; session < this.nr_sessions ;session++) {
			if(!isSameVar(session,tab)) {
				vars++;
			}
		}
		//int[] session_xrooms = new int[vars];
		int vars_init = var_same;
		int[] session_xrooms = new int[nr_sessions];
		for(int session = 0; session < this.nr_sessions ;session++) {
			if(!isSameVar(session,tab)) {
				session_xrooms[session] = vars_init;
				vars_init++;
			}
			else {
				session_xrooms[session] = getVarsSession(session,tab);
			}
		}
		
		this.session_xrooms = session_xrooms;
		this.vars_room = vars;
		this.var_same_room = var_same;
		//System.out.println(Arrays.toString(session_xrooms));
		//System.out.println("vars = "+vars+" var_same = "+var_same+" nr_sessions = "+nr_sessions);
	}//FinMethod
	
	public void sessions_xteachers() {
		Vector<ConstraintUTP> tab = new Vector<ConstraintUTP>();
		for(int constraint = 0; constraint < this.constraints.size() ;constraint++) {
			if(this.constraints.get(constraint).getConstraint().equals("sameTeachers")) {
				tab.add(this.constraints.get(constraint));
			}
		}
		
		int var_same = tab.size();
		int vars = var_same;
		
		for(int session = 0; session < this.nr_sessions ;session++) {
			if(!isSameVar(session,tab)) {
				vars++;
			}
		}
		//int[] session_xrooms = new int[vars];
		int vars_init = var_same;
		int[] session_xrooms = new int[nr_sessions];
		for(int session = 0; session < this.nr_sessions ;session++) {
			if(!isSameVar(session,tab)) {
				session_xrooms[session] = vars_init;
				vars_init++;
			}
			else {
				session_xrooms[session] = getVarsSession(session,tab);
			}
		}
		
		this.session_xteachers = session_xrooms;
		this.vars_teachers = vars;
		this.var_same_teachers = var_same;
		//System.out.println(Arrays.toString(session_xrooms));
		//System.out.println("teacher vars = "+vars+" var_same = "+var_same+" nr_sessions = "+nr_sessions);
	}//FinMethod
	
	public void class_rank() {
		int classe = 0;
		int[] class_rank = new int[this.nr_classes];
		for(int p =0; p < this.nr_parts ;p++) {
			for(int c = 0; c < this.part_classes.get(p).size() ;c++) {
				class_rank[classe] = c+1;
				classe++;
			}
		}
		this.class_rank = class_rank;
	}//FinMethod
	
	public Vector<Vector<Integer>>  bitDay(int a,int s,int part) {
		int sBitDay = s;
		int[] bitDay = new int [sBitDay];
		Arrays.fill(bitDay,0);
		int u = 0;
		for (int i = 0;i < a;i++) {
			u = i % sBitDay;
			bitDay[u]++;
		}
		
		int cl = 0;
		Vector<Vector<Integer>> tab = new Vector<Vector<Integer>>(s);
		for(int sbd = 0 ; sbd < s ;sbd++) {
			Vector<Integer> tmp = new Vector<Integer>(bitDay[sbd]);
			for(int c = 0; c < bitDay[sbd] ;c++) {
				tmp.add(c, this.part_classes.get(part).get(cl));
				cl++;
			}
			tab.add(sbd,tmp);
		}
		
		return tab;
	}//FinMethod
	
	public void bach_classes() {
		Vector<Vector<Vector<Integer>>> tab = new Vector<Vector<Vector<Integer>>>();
		for(int p = 0; p < this.nr_parts ;p++) {
			int a = this.part_classes.get(p).size();
			Vector<Vector<Integer>> benchClasses;
			int sa1 = 1;
			int sa2 = 8;
			int sa3 = 10;
			
			
			if(a >= sa1 && a < sa2) {
				int u = 3;
				benchClasses = bitDay(a,u,p); 
			}
			else if(a >= sa2 && a < sa3 ){
				int u = 4;
				benchClasses = bitDay(a,u,p); 
			}
			else if(a >= sa2) { 
				int u = 5;
				benchClasses = bitDay(a,u,p); 
			}
			else {
				benchClasses = new Vector<Vector<Integer>>(); 
			}

			tab.add(benchClasses);
		}
		
		/*String out = "";
		for(int i =0; i < tab.size();i++) {
			out+=i+": {";
			for(int j =0;j< tab.get(i).size();j++) {
				out+="[";
				for(int u =0;u< tab.get(i).get(j).size();u++){
					out+=""+tab.get(i).get(j).get(u)+",";
				}
				out+="],";
			}
			out+="}\n";
		}
		System.out.println(out);*/
		this.group_of_classes_eq = tab;
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
		class_groups();
		class_multiple();
		class_multiple_teacher_position();
		
		part_room_worst_case();
		class_multiple_rooms();
		class_multiple_room_position();
		
		room_capacity_v2();
		room_name_v2();
		student_group();
		sessions_xrooms();
		sessions_xteachers();
		class_rank();
		bach_classes();
	}//FinMethod

}//FinClass
