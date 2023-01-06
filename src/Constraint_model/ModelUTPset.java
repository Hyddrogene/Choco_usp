package Constraint_model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMax;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
//import org.chocosolver.solver.search.strategy.selectors.values.IntDomainRandom;
import org.chocosolver.solver.search.strategy.selectors.values.SetDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.InputOrder;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.solver.variables.Task;

public class ModelUTPset {
	//Attribute
	private InstanceUTPArray instanceUTP;
	private Model model;
	private IntVar[] x_teacher;
	//private SetVar[] x_teacher;
	private IntVar[] x_room;
	//private SetVar[] x_room;
	private IntVar[] x_slot;
	private Solution solution;
	//private StatisticUtils statistics;
	private Solver solver;
	private static String filename_solution;
	private SetVar[] x_teachers;
	private SetVar[] x_rooms;
	
	private BoolVar[][] x_bool_t;

	//private IntVar[] t_m_r;
	//Method
	public ModelUTPset(InstanceUTPArray instanceUTP) {
		this.model = new Model("Modele USP set");
		this.instanceUTP = instanceUTP;
		this.instanceUTP.resizemultiplevariable();
		x_slot = new IntVar[this.instanceUTP.nr_sessions];
		x_room = new IntVar[this.instanceUTP.nr_classes];
		x_teacher = new IntVar[this.instanceUTP.nr_classes];
		
		x_rooms = new SetVar[this.instanceUTP.nr_class_multiple_room];
		
		//x_rooms = new IntVar[];
		x_teachers = new SetVar[this.instanceUTP.nr_class_multiple_teacher];
		//this.instanceUTP.calcul(); 
		initVariables();
		initMultpileVariables();
		initMultpileVariablesRoom();
		constraint();
		//solve();
		//print();
		
	}//FinMethod
	
	public int[] part_i_slots(Vector<Integer> t) {
		int[] tab = new int[t.size()];
		for(int i = 0; i < t.size() ;i++) {
			tab[i] = t.get(i).intValue();
		}
		return tab;
	}//FinMethod
	
	public int[] part_i_slots_rooms(Vector<Integer> t) {
		int[] tab = new int[t.size()+1];
		tab[0] = 0;
		for(int i = 1; i <= t.size() ;i++) {
			tab[i] = t.get(i-1).intValue();
		}
		//tab[t.size()] = this.instanceUTP.nr_rooms+1;
		return tab;
	}//FinMethod
	
	public void initMultpileVariables() {
		SetVar[] tab = new SetVar[this.instanceUTP.class_multiple_teacher.length];
		//System.out.println("j = "+j);
		for (int i = 0; i < this.instanceUTP.class_multiple_teacher.length ;i++) {
			int c = this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1];
			
			tab[i] = model.setVar("x_teacher_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1]-1)));
			tab[i].setCard(model.intVar(this.instanceUTP.part_session_teacher_count[c]));
		}
		this.x_teachers = tab;
		//different_teachers();
		
	}//FinMethod
	
	//private IntVar[] s_room;
	public void initMultpileVariablesRoom() {
		SetVar[] tab = new SetVar[this.instanceUTP.class_multiple_room.length];
		for (int i = 0; i < this.instanceUTP.class_multiple_room.length ;i++) {
			int c = this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1];
			tab[i] = model.setVar("x_rooms_"+i,part_i_slots_rooms(this.instanceUTP.part_rooms.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1]-1)));
			tab[i].setCard(model.intVar(this.instanceUTP.part_room_worst_case[c]));
		}
		this.x_rooms = tab;
		x_bool_t = new BoolVar[this.instanceUTP.class_multiple_room.length][this.instanceUTP.nr_teachers+1];
		System.out.println(this.instanceUTP.nr_teachers+1);
		for(int i = 0; i < this.instanceUTP.class_multiple_room.length ;i++) {
			for(int j = 0; j < this.instanceUTP.nr_teachers+1 ;j++) {
				x_bool_t[i][j] = model.boolVar(); 
			}
			
		}
		for(int i = 0; i < this.instanceUTP.class_multiple_room.length ;i++) {
			model.setBoolsChanneling(x_bool_t[i], x_rooms[i]);
		}
	}//FinMethod
	
	public void initVariables() {
		for(int i = 0; i < instanceUTP.nr_sessions ;i++ ) {
			//x_slot[i] = model.intVar("x_slot_"+i,1,this.instanceUTP.nr_slot());
			//Integer[] t = this.instanceUTP.part_slots.get(i).toArray(new Integer[this.instanceUTP.part_slots.get(i).size()]);
			x_slot[i] = model.intVar("x_slot_"+i,part_i_slots(this.instanceUTP.part_slots.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1)));
		}
		
		for(int i = 0; i < instanceUTP.nr_classes ;i++ ) {
			x_teacher[i] = model.intVar("x_teacher_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[i]-1)));
		}
		
		for(int i = 0; i < instanceUTP.nr_classes ;i++ ) {
			x_room[i] = model.intVar("x_room_"+i,part_i_slots(this.instanceUTP.part_rooms.get(this.instanceUTP.class_part[i]-1)));
		}
	}//FinMethod
	
	public void constraint() {
		implicite_sequenced_sessions();
		teacher_service_v2();
		disjunctive_teacher_v2();
		disjunctive_group_v2();
		disjunctive_room_v2();
		size_of_multiroom();
		//cardinal_x_rooms();
		flatten_constraint();
		//search_strategie();
	}//FinMethod
	
	/**
	 * Permet d'avoir les séances au sein d'une même classe de manière séquencé
	 * */
	public void implicite_sequenced_sessions() {
		for(int c = 0; c < this.instanceUTP.nr_classes ;c++) {
			if(this.instanceUTP.class_sessions.get(c).size() >= 1 ) {
				int o = this.instanceUTP.part_session_length[this.instanceUTP.class_part[c]-1];
				for(int s = 1; s < this.instanceUTP.class_sessions.get(c).size() ;s++) {
					model.arithm(x_slot[ this.instanceUTP.class_sessions.get(c).get(s)-1],">=", x_slot[ this.instanceUTP.class_sessions.get(c).get(s)-2],"+",o ).post();
				}
			}
		}
	}//FinMethod
		
	
	public IntVar[] t_s_v2_vars(int p) {
		IntVar[] tab = new IntVar[this.instanceUTP.part_classes.get(p).size()];
		for(int i = 0; i < this.instanceUTP.part_classes.get(p).size() ;i++) {
			tab[i] = x_teacher[this.instanceUTP.part_classes.get(p).get(i)-1];
		}
		//System.out.println("t.size "+tab.length);
		return tab;
	}//FinMethod
	
	public IntVar[] t_s_v2_cards(int p) {
		IntVar[] tab = new IntVar[this.instanceUTP.part_teachers.get(p).size()];
		//this.instanceUTP.part_teachers.get(p)
		for(int i = 0; i < this.instanceUTP.part_teachers.get(p).size() ;i++) {
			int val = this.instanceUTP.part_teacher_sessions_count[p][this.instanceUTP.part_teachers.get(p).get(i)-1]/this.instanceUTP.part_nr_sessions[p];
			//System.out.println("Part "+(p+1)+" Teacher "+this.instanceUTP.part_teachers.get(p).get(i)+" val :"+val+" serv "+this.instanceUTP.part_teacher_sessions_count[p][this.instanceUTP.part_teachers.get(p).get(i)-1]);
			tab[i] = model.intVar("",val);
		}
		/*int j= 0;
		for(int i = 0; i < tab.length ;i++) {
			j += tab[i].getLB();
		}
		System.out.println("sum "+j);*/
		return tab;
	}//FinMethod
	
	public IntVar[] generate_Sum_gcc(int teacher,int part) {
		IntVar[] tab = new IntVar[this.instanceUTP.part_classes.get(part).size()];
		
		for(int classe = 0; classe < this.instanceUTP.part_classes.get(part).size() ;classe++) {
			int classe_position_x_teachers = this.instanceUTP.class_position_multiple_teacher[classe];
			tab[classe] = x_bool_t[classe_position_x_teachers][teacher].intVar();
		}
		return tab;
	}//FinMethod
	
	/**
	 * 
	 * */
	public void teacher_service_v2() {
		for(int p = 0; p < this.instanceUTP.nr_parts ;p++) {
			//System.out.println("size : "+part_i_slots(this.instanceUTP.part_teachers.get(p)).length);
			//System.out.println("size : "+t_s_v2_cards(p).length);
			if(this.instanceUTP.part_session_teacher_count[p] <= 1) {
				model.globalCardinality(t_s_v2_vars(p),part_i_slots(this.instanceUTP.part_teachers.get(p)),t_s_v2_cards(p),true).post();			
			}
			else {
				//System.out.println(this.instanceUTP.part_name[p]);
				//System.out.println("size "+t_ss_v2_vars(p).length);
				for(int teacher = 0; teacher < this.instanceUTP.part_teachers.get(p).size() ;teacher++) {
					int val = this.instanceUTP.part_teacher_sessions_count[p][this.instanceUTP.part_teachers.get(p).get(teacher)-1]/this.instanceUTP.part_nr_sessions[p];
					model.sum(generate_Sum_gcc(teacher,p),"=", val).post();
				}
				//model.globalCardinality(t_ss_v2_vars(p),part_i_slots(this.instanceUTP.part_teachers.get(p)),t_s_v2_cards(p),true).post();			
			}
		}
	}//FinMethod
	
	//======================
	
	public int max_tab(int[] tab) {
		int max = 0;
		for(int i =0; i < tab.length ;i++ ) {
			if(max < tab[i]) {
				max = tab[i];
			}
		}
		return max;
	}//FinMethod
	

	
	/*public void cardinal_x_rooms() {
		int max_card = max_tab(this.instanceUTP.part_room_worst_case);
		IntVar[] card_xrooms = new IntVar[this.instanceUTP.nr_class_multiple_room];
		for(int i = 0; i < this.instanceUTP.nr_class_multiple_room ;i++) {
			int part = this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1];
			//int p_case = this.instanceUTP.part_room_worst_case[part-1];
			card_xrooms[i] = model.intVar(0,max_card+1);
			x_rooms[i].setCard(card_xrooms[i]);
			model.arithm(card_xrooms[i], ">=", this.instanceUTP.part_room_worst_case[part-1]).post();
		}
	}//FinMethod
	*/
	
	//======================
	public int size_teacher_sessions(int t) {
		int max_size = 0;
		for(int i = 0; i < this.instanceUTP.teacher_parts.get(t).size() ;i++) {
			max_size += this.instanceUTP.part_classes.get(this.instanceUTP.teacher_parts.get(t).get(i) -1).size() * this.instanceUTP.part_nr_sessions[(this.instanceUTP.teacher_parts.get(t).get(i)-1)];
		}
		return max_size;
	}//FinMethod
	
	public Task[] sessions_of_teacher_disjun_v2(int t) {
		int max_size = size_teacher_sessions(t);
		Task[] tab = new Task[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.teacher_parts.get(t).size() ;i++) {
			for(int j = 0; j < this.instanceUTP.part_classes.get(this.instanceUTP.teacher_parts.get(t).get(i)-1).size() ;j++) {
				int classe = this.instanceUTP.part_classes.get(this.instanceUTP.teacher_parts.get(t).get(i)-1).get(j);
				for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
					tab[it] = model.taskVar(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(k)-1],this.instanceUTP.part_session_length[this.instanceUTP.teacher_parts.get(t).get(i)-1]);;
					it++;
				}
			}
		}
		//System.out.println("y : "+it);
		return tab;
		
	}//FinMethod
	
	public IntVar[] generate_heigth_teacher_v2(int t) {
		int max_size = size_teacher_sessions(t);
		IntVar[] tab = new IntVar[max_size];
		BoolVar[] tabBool = new BoolVar[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.teacher_parts.get(t).size() ;i++) {
			for(int j = 0; j < this.instanceUTP.part_classes.get(this.instanceUTP.teacher_parts.get(t).get(i)-1).size() ;j++) {
				
				int classe = this.instanceUTP.part_classes.get(this.instanceUTP.teacher_parts.get(t).get(i)-1).get(j);
				//System.out.println("p : "+(this.instanceUTP.teacher_parts.get(t).get(i)));
				//System.out.println("c : "+classe);
				for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
					tab[it] = model.intVar(0,1);
					tabBool[it] = model.intEqView(x_teacher[classe-1], t+1);
					model.arithm(tab[it], "=", tabBool[it].intVar()).post(); 
					//model.arithm(tabBool[it],"=",x_teacher[classe],"=",(t+1));
					it++;
				}
			}
		}
		//System.out.println("x : "+it);
		return tab;
	}//FinMethod
	
	
	public void disjunctive_teacher_v2() {
		for(int t = 0; t < this.instanceUTP.nr_teachers ;t++) {
			if(this.instanceUTP.teacher_parts.get(t).size() > 0) {
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				//Disjunctive
				model.cumulative(sessions_of_teacher_disjun_v2(t),generate_heigth_teacher_v2(t),model.intVar(1)).post();
			}
		}
		
	}//FinMethod
	
	public IntVar[] sessions_of_teacher_disjun_v3(int t) {
		int s_max = 0;
		//int s_max_class = 0;
		int[] part_service_t = new int[this.instanceUTP.nr_parts];
		Vector<Integer> part_concerned =new Vector<Integer>(); 
		for (int i = 0; i < this.instanceUTP.nr_parts ; i++) {
			if(this.instanceUTP.part_teacher_sessions_count[i][t-1] > 0) {
				part_service_t[i] = this.instanceUTP.part_teacher_sessions_count[i][t-1] / this.instanceUTP.part_nr_sessions[i];
				part_concerned.add(i+1);
				s_max += part_service_t[i];
			}
		}
		int k = 0;
		IntVar[] tab =new IntVar[s_max];
		IntVar[] tabTmp =new IntVar[s_max];
		for(int i = 0; i < part_concerned.size() ;i++) {
			for(int j = 0; j < this.instanceUTP.part_classes.get(i).size() ;j++) {//part_service_t[part_concerned.get(i)-1] ;j++) {
				x_teacher[this.instanceUTP.part_classes.get(i).get(j)-1].eq(t).imp(tabTmp[k].eq(this.instanceUTP.part_classes.get(i).get(j))).post();	
				k++;
				
			}
		}
		return tab;
	}//FinMethod
	
	public void disjunctive_teacher_v3() {
		for(int t = 0; t < this.instanceUTP.nr_teachers ;t++) {
			if(this.instanceUTP.teacher_parts.get(t).size() > 0) {
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				//Disjunctive
				model.cumulative(sessions_of_teacher_disjun_v2(t),generate_heigth_teacher_v2(t),model.intVar(1)).post();
			}
		}
		
	}//FinMethod
	//======================
	
	//======================
	public int size_room_sessions(int t) {
		int max_size = 0;
		for(int i = 0; i < this.instanceUTP.room_parts.get(t).size() ;i++) {
			max_size += this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i) -1).size() * this.instanceUTP.part_nr_sessions[(this.instanceUTP.room_parts.get(t).get(i)-1)];
		}
		return max_size;
	}//FinMethod
	
	public Task[] sessions_of_room_disjun_v2(int t) {
		int max_size = size_room_sessions(t);
		Task[] tab = new Task[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.room_parts.get(t).size() ;i++) {
			for(int j = 0; j < this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).size() ;j++) {
				int classe = this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).get(j);
				for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
					tab[it] = model.taskVar(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(k)-1],this.instanceUTP.part_session_length[this.instanceUTP.room_parts.get(t).get(i)-1]);;
					it++;
				}
			}
		}
		//System.out.println("y : "+it);
		return tab;
		
	}//FinMethod
	
	
	public IntVar[] generate_heigth_room_v2(int t) {
		int max_size = size_room_sessions(t);
		IntVar[] tab = new IntVar[max_size];
		BoolVar[] tabBool = new BoolVar[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.room_parts.get(t).size() ;i++) {
			int part = this.instanceUTP.room_parts.get(t).get(i);
			if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
				for(int j = 0; j < this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).size() ;j++) {
					int classe = this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).get(j);
					//System.out.println("p : "+(this.instanceUTP.teacher_parts.get(t).get(i)));
					//System.out.println("c : "+classe);
					int cl = this.instanceUTP.class_position_multiple_room[classe-1];
					for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
						tab[it] = model.intVar(0,1);
						//System.out.println("cl = "+t);
						model.arithm(tab[it],"=", x_bool_t[cl][t]);
						it++;
					}
				}
			}
			else {
				for(int j = 0; j < this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).size() ;j++) {
					
					int classe = this.instanceUTP.part_classes.get(this.instanceUTP.room_parts.get(t).get(i)-1).get(j);
					//System.out.println("p : "+(this.instanceUTP.teacher_parts.get(t).get(i)));
					//System.out.println("c : "+classe);
					for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
						tab[it] = model.intVar(0,1);
						tabBool[it] = model.intEqView(x_room[classe-1], t+1);
						model.arithm(tab[it], "=", tabBool[it].intVar()).post(); 
						//model.arithm(tabBool[it],"=",x_teacher[classe],"=",(t+1));
						it++;
					}
				}
			}

		}
		//System.out.println("x : "+it);
		return tab;
	}//FinMethod
	
	
	public void disjunctive_room_v2() {
		for(int t = 0; t < this.instanceUTP.nr_rooms ;t++) {
			if(this.instanceUTP.room_parts.get(t).size() > 0) {
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				//Disjunctive
				model.cumulative(sessions_of_room_disjun_v2(t),generate_heigth_room_v2(t),model.intVar(1)).post();
			}
		}
		
	}//FinMethod
	
	//======================
	
	//======================
	
	public Task[] sessions_of_group_disjun_v2(int t) {
		int max_size = this.instanceUTP.group_sessions.get(t).size();
		Task[] tab = new Task[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.group_sessions.get(t).size() ;i++) {
			tab[it] = model.taskVar(x_slot[this.instanceUTP.group_sessions.get(t).get(i)-1],this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[this.instanceUTP.group_sessions.get(t).get(i)-1]-1]-1]);
			it++;
		}
		//System.out.println("y : "+it);
		return tab;
		
	}//FinMethod
	
	public void disjunctive_group_v2() {
		for(int g = 0; g < this.instanceUTP.nr_groups ;g++) {
			if(this.instanceUTP.group_sessions.get(g).size() > 0) {
				IntVar[] heights = new IntVar[this.instanceUTP.group_sessions.get(g).size()];
				Arrays.fill(heights, model.intVar(1));
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				//Disjunctive
				model.cumulative(sessions_of_group_disjun_v2(g),heights,model.intVar(1)).post();
			}
		}
		
	}//FinMethod
	

	
	public void size_of_multiroom() {
		//IntVar[] t_m_r = new IntVar[this.instanceUTP.nr_class_multiple_room];
		//this.t_m_r = new IntVar[this.instanceUTP.nr_class_multiple_room];
		//this.jjjjjj = 0;
		int max_size = 2000;
		//System.out.println("nr_rooms "+this.instanceUTP.nr_rooms);
		for(int i = 0; i < this.instanceUTP.nr_class_multiple_room ;i++) {
			int size_groups = 0;
			int cl_r = this.instanceUTP.class_multiple_room[i];
			for(int g = 0; g < this.instanceUTP.class_groups.get(cl_r-1).size() ;g++) {
				size_groups += this.instanceUTP.group_headcount[this.instanceUTP.class_groups.get(cl_r-1).get(g)-1];
			}
			int cl = this.instanceUTP.class_position_multiple_room[cl_r-1];
			IntVar tmp = model.intVar(0,max_size);
			model.sumElements(this.x_rooms[cl],this.instanceUTP.room_capacity,1, tmp).post();
			model.arithm(tmp, ">=", size_groups).post();	
		}
	}//FinMethod
	
	//======================
	
	public int search_value(ConstraintUTP cons,String value) {
		
		for(int i = 0; i < cons.getParameters().length ;i++) {
			if(this.instanceUTP.parameter_name[cons.getParameters()[i]-1].equals(value)) {
				return cons.getParameters()[i]-1;
			}
		}
		return -1;
		
	}//FinMethod
	
	public int unit_value(String unite) {
		//System.out.println(unite);
		if(unite.equals("week")) {
			 return this.instanceUTP.nr_days_per_week * this.instanceUTP.nr_slots_per_day;
		}
		
		if(unite.equals("day")) {return this.instanceUTP.nr_slots_per_day;}
		return -1;
	}//FinMethod
	
	public int unit_value_bound(String unite) {
		//System.out.println(unite);
		if(unite.equals("week")) {
			 return this.instanceUTP.nr_weeks;
		}
		
		if(unite.equals("day")) {return this.instanceUTP.nr_days_per_week*this.instanceUTP.nr_weeks;}
		return -1;
	}//FinMethod
	
	public void sameSlot(ConstraintUTP constraint) {
		
		for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1], "=", x_slot[constraint.getSessions().get(0).get(i)-1]).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public void forbiddenPeriod(ConstraintUTP constraint) {
		//Vector<Integer> allTimes = new Vector<Integer>();
		int first_num = search_value(constraint,"first");
		int last_num = search_value(constraint,"last");
		int unite_num = search_value(constraint,"period");
		//System.out.println("Val "+value_num+" unite "+unite_num);
		int first = Integer.parseInt(this.instanceUTP.parameter_value.get(first_num).get(0));
		int last = Integer.parseInt(this.instanceUTP.parameter_value.get(last_num).get(0));
		int unite = unit_value(this.instanceUTP.parameter_value.get(unite_num).get(0));
		int unite_bound = unit_value_bound(this.instanceUTP.parameter_value.get(unite_num).get(0));
		//System.out.println("unite "+unite);
		//System.out.println("unitebound "+unite_bound);
		/*for(int i = first; i <= last;i++) {
			allTimes.add(i);
		}*/
		int[][] valuePeriod = new int[unite_bound][2];
		//Vector<Integer> allPeriod = new Vector<Integer>();
		/*for(int i = 0; i < allTimes.size() ;i++) {
			for(int j = 0; j < unite_bound  ;j++) {
				allPeriod.add(allTimes.get(i)+(j*unite));
			}
		}*/
		for(int i = 0; i < unite_bound ;i++) {
			valuePeriod[i][0] = first + (i*unite);
			valuePeriod[i][1] = last + (i*unite);
		}
		for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			for(int j = 0; j < unite_bound ;j++) {
				try {
					x_slot[constraint.getSessions().get(0).get(i)-1].removeInterval(valuePeriod[j][0], valuePeriod[j][1], solution);
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1],"!=", allPeriod.get(j).intValue()).post();
			}
		}
	}//FinMethod
	
	
	public void sameTeachers(ConstraintUTP constraint) {
		//System.out.println();
	}//FinMethod
	
	public void sameRooms(ConstraintUTP constraint) {
		
		//System.out.println();
	}//FinMethod
	
	public void periodic(ConstraintUTP constraint) {
		  //System.out.println("PERIODIC");
		  int value_num = search_value(constraint,"value");
		  int unite_num = search_value(constraint,"unit");
		  //System.out.println("Val "+value_num+" unite "+unite_num);
		  int value = Integer.parseInt(this.instanceUTP.parameter_value.get(value_num).get(0));
		  int unite = unit_value(this.instanceUTP.parameter_value.get(unite_num).get(0));
		  int sum_val = value * unite;
		  
		  if(constraint.getSessions().get(0).size()>1) {
			  for(int i = 1; i < constraint.getSessions().get(0).size() ;i++) {
				  model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1], "=", x_slot[constraint.getSessions().get(0).get(0)-1],"+",sum_val*i).post();
			  }
		  }
		//System.out.println();
	}//FinMethod
	
	public void allowedPeriod(ConstraintUTP constraint) {
			int first_num = search_value(constraint,"first");
		  int last_num = search_value(constraint,"last");
		  //System.out.println("Val "+value_num+" unite "+unite_num);
		  int first = Integer.parseInt(this.instanceUTP.parameter_value.get(first_num).get(0));
		  int last = Integer.parseInt(this.instanceUTP.parameter_value.get(last_num).get(0));
		  for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			  model.arithm(x_slot[constraint.getSessions().get(0).get(i).intValue()-1],">=",first).post();
			  model.arithm(x_slot[constraint.getSessions().get(0).get(i).intValue()-1],"<=",last).post();
		  }
	}//FinMethod
	
	public void sequenced(ConstraintUTP constraint) {
		for (int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			for(int j = 0; j < constraint.getSessions().get(1).size() ;j++) {
				int session_length = this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1]-1];
				model.arithm(x_slot[constraint.getSessions().get(1).get(j)-1], ">=", x_slot[constraint.getSessions().get(0).get(i)-1],"+",session_length).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public void sameWeek(ConstraintUTP constraint) {
		  
		 int unite =this.instanceUTP.nr_days_per_week * this.instanceUTP.nr_slots_per_day;
		
		for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].div(unite).intVar(), "=", x_slot[constraint.getSessions().get(0).get(i)-1].div(unite).intVar()).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public void assignRoom(ConstraintUTP constraint) {
		int room_num = search_value(constraint,"room");
		  //System.out.println("Val "+value_num+" unite "+unite_num);
		int room = Integer.parseInt(this.instanceUTP.parameter_value.get(room_num).get(0));
		//System.out.println(this.instanceUTP.room_name[room-1]);
		for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			model.arithm(x_room[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1],"=",room).post();
		}
	}//FinMethod
	
	public void forbiddenRooms(ConstraintUTP constraint) {
		int room_chain_num = search_value(constraint,"roomChain");
		Vector<String> rooms = this.instanceUTP.parameter_value.get(room_chain_num);
		int[] rooms_id = new int[rooms.size()];
		for(int i = 0; i < rooms_id.length ;i++) {
			rooms_id[i] = Integer.parseInt(rooms.get(i));
		}
		//System.out.println("emet "+this.instanceUTP.room_name[Integer.parseInt(rooms.get(0))-1]);
		for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			for(int j = 0; j < rooms_id.length ;j++) {
				try {
					x_room[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1].removeValue(rooms_id[j], solution) ;
				} catch (ContradictionException e) {
					e.printStackTrace();
				}
			}
		}
	}//FinMethod
	
	public void sameWeekDay(ConstraintUTP constraint) {
		 //int unite = this.instanceUTP.nr_weeks;
		 int unite2 = this.instanceUTP.nr_days_per_week;
		for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].mod(unite2).intVar(), "=", x_slot[constraint.getSessions().get(0).get(i)-1].mod(unite2).intVar()).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public void sameWeeklySlot(ConstraintUTP constraint) {
		 int unite = this.instanceUTP.nr_slots_per_day * this.instanceUTP.nr_days_per_week;
		 //int unite2 = this.instanceUTP.nr_days_per_week;
			
		for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].mod(unite).intVar(), "=", x_slot[constraint.getSessions().get(0).get(i)-1].mod(unite).intVar()).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public IntVar[] generateWeekDay(ConstraintUTP cons, int m) {
		IntVar[] tab = new IntVar[cons.getSessions().get(0).size()];
		for(int i = 0; i < cons.getSessions().get(0).size() ;i++) {
			tab[i] = x_slot[cons.getSessions().get(0).get(i)-1].sub(1).div(this.instanceUTP.nr_slots_per_day).mod(m).add(1).intVar();
		}
		return tab;
	}//FinMethod
	
	public void differentWeekDay(ConstraintUTP constraint) {
		 int unite2 = this.instanceUTP.nr_days_per_week;
		 model.allDifferent(generateWeekDay(constraint,unite2)).post();;
		 //for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
				//for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
					
					//model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].mod(unite2).intVar(), "!=", x_slot[constraint.getSessions().get(0).get(i)-1].mod(unite2).intVar()).post();
					//precedence()
				//}
			//}
	}//FinMethod
	
	public void disjunct(ConstraintUTP constraint) {
		//System.out.println();
	}//FinMethod
	
	
	//======================
	
	
	public void flatten_constraint() {
		for(int i = 0; i < this.instanceUTP.constraints.size() ;i++) {
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("periodic")) {periodic(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("forbiddenPeriod")) {forbiddenPeriod(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameTeachers")) {sameTeachers(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameRooms")) {sameRooms(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameSlot")) {sameSlot(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("allowedPeriod")) {allowedPeriod(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sequenced")) {sequenced(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeek")) {sameWeek(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("assignRoom")) {assignRoom(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeekDay")) {sameWeekDay(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeeklySlot")) {sameWeeklySlot(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("disjunct")) {disjunct(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("differentWeekDay")) {differentWeekDay(this.instanceUTP.constraints.get(i));}
			if(this.instanceUTP.constraints.get(i).getConstraint().equals("forbiddenRooms")) {forbiddenRooms(this.instanceUTP.constraints.get(i));}
			
			//System.out.println("Constraint "+this.instanceUTP.constraints.get(i).getConstraint()+" is not implemented");
			
		}
		
	}//FinMethod
	
	public static String getFilename_solution() {
		return filename_solution;
	}//FinMethod

	public static void setFilename_solution(String filename_solution) {
		ModelUTPset.filename_solution = filename_solution;
	}//FinMethod

	
	public void write_solution_file(String out) {
		FileWriter FWriter;
		BufferedWriter writer;
		try {
			FWriter = new FileWriter(filename_solution);
			writer = new BufferedWriter(FWriter);
					
			writer.write(out);			    
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//FinMethod

	public int[] tab_CC_course() {
		Vector<Integer> CC = new Vector<Integer>();
		for(int i = 0; i < this.instanceUTP.nr_sessions  ;i++) {
			for(int j = 0; j < this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).size() ;j++) {
				if(this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("EXAM") || this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("EVAL") || this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("CC")) {
					//System.out.println("Session "+i+" "+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]);
					CC.add(i+1);
					break;
				}
			}
		}
		int[] tab = new int[CC.size()];
		for(int i = 0; i < CC.size() ;i++) {
			tab[i] = CC.get(i).intValue();
		}
		return tab;
	}//FinMethod
	
	public boolean session_not_in(int session,int[] tab) {
		for(int i = 0; i < tab.length ;i++) {
			if(tab[i] == session) {
				return false;
			}
		}
		return true;
	}//FinMethod
	
	public SessionRank[] tab_normal_course() {
		Vector<SessionRank> normalCourse = new Vector<SessionRank>();
		int[] tab = tab_CC_course();
		for(int i = 0; i < this.instanceUTP.nr_sessions  ;i++) {
			if(session_not_in(i+1,tab)) {
				int rank = i - this.instanceUTP.class_sessions.get(this.instanceUTP.session_class[i]-1).get(0).intValue() + 1;
				normalCourse.add(new SessionRank(i+1,rank));

			}
		}
		
		SessionRank[] tt = new SessionRank[normalCourse.size()];
		for(int i = 0; i < normalCourse.size() ;i++) {
			tt[i] = normalCourse.get(i);
		}
		Arrays.sort(tt,new Comparator<SessionRank>() {
			@Override
			public int  compare(SessionRank sr1,SessionRank sr2) {return Integer.compare(sr1.rank, sr2.rank);}
		});
		return tt;
	}//FinMethod
	
	public IntVar[] sort_xslot1() {
		//IntVar[] tab = new IntVar[this.instanceUTP.nr_sessions];

		SessionRank[] tt = tab_normal_course();
		IntVar[] tab = new IntVar[tt.length/2];
		for(int i = 0; i < tt.length/2 ;i++) {	
			tab[i] =  x_slot[tt[i].cpt-1];
		}

		//System.out.println("gardien : "+this.instanceUTP.nr_sessions);
		//System.out.println("size slot :  "+tt.length);
		//System.out.println("size slot1 :  "+tab.length);

		return tab;
	}//FinMethod
	
	public IntVar[] sort_xslot2() {
		//IntVar[] tab = new IntVar[this.instanceUTP.nr_sessions];
		//this.model.arithm(x_slot[180],">", 30000).post();
		SessionRank[] tt = tab_normal_course();
		IntVar[] tab = new IntVar[tt.length-tt.length/2];
		int j = 0;
		for(int i = tt.length/2; i < tt.length ;i++) {
			tab[j] =  x_slot[tt[i].cpt-1];
			j++;
		}

		//System.out.println("size slot2 :  "+tab.length);
		return tab;
	}//FinMethod
	
	public IntVar[] sort_xslot3() {

		int[] CC = tab_CC_course();
		IntVar[] tab = new IntVar[CC.length];
		for(int i = 0; i < CC.length;i++) {
			tab[i] =  x_slot[CC[i]-1];
		}
		
		//System.out.println("size slot3 :  "+CC.length);
		return tab;
	}//FinMethod
	
	public void search_strategie() {
		double ran = Math.random();
		System.out.println(ran);
		this.solver.setSearch(Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                //new IntDomainRandom((long) ran),
                new IntDomainMin(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                x_room
				),
			//	Search.setVarSearch(null, null, false, x_rooms)
		Search.setVarSearch(
				
				//new FirstFail(model),
				new InputOrder<SetVar>(model),
				//true,
				new SetDomainMin(),
				true,
				x_rooms
		        
				),
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                x_teacher
				),
		Search.setVarSearch(
                // selects the variable of smallest domain size
                new InputOrder<SetVar>(model),
                // selects the smallest domain value (lower bound)
                new SetDomainMin(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                true,
                x_teachers
				),
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                //new IntDomainRandom((long) ran),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                sort_xslot1()
				),
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMiddle(true),
                //new IntDomainMin(),
                //new IntDomainRandom((long) ran),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                sort_xslot2()
				),
		/*Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMiddle(true),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                t_m_r
				),
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMiddle(true),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                s_room
				),*/
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                //new IntDomainMin(),
                new IntDomainMax(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                sort_xslot3()
				));
		//this.solver.
		
	}//FinMethod
	
	
	public Solution solve() {
		this.solver = model.getSolver(); 
		search_strategie();
		//this.solver.showContradiction();;
		this.solution = this.solver.findSolution();
		this.solver.printStatistics();
		return this.solution;
	}//FinMethod
	
	public String print_group_solution(int session) {
		String out = "";
		Vector<Integer> groups = this.instanceUTP.class_groups.get(this.instanceUTP.session_class[session]-1);
		for(int g = 0; g < groups.size() ;g++) {
			if(g < groups.size()-1) {out += "\""+this.instanceUTP.group_name[groups.get(g)-1]+"\",";}
			else {out += "\""+this.instanceUTP.group_name[groups.get(g)-1]+"\"";}
		}
		return out;
	}//FinMethod
	
	public String print_teacher_solution(int session) {
		String out = "";
		int cl = this.instanceUTP.session_class[session];
		int part = this.instanceUTP.class_part[cl-1];
		if(this.instanceUTP.part_session_teacher_count[part-1] > 1) {
			//this.solution.getSetVal()-1
			int[] tt = this.solution.getSetVal(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl-1]]);
			for(int j = 0; j < this.instanceUTP.part_session_teacher_count[part-1];j++) {
				if(j < this.instanceUTP.part_session_teacher_count[part-1]-1 ) {
					out += "\""+this.instanceUTP.teacher_name[tt[j]-1]+"\",";
				}
				else {
					out += "\""+this.instanceUTP.teacher_name[tt[j]-1]+"\"";
				}
			}
		}
		else {
			out += "\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teacher[cl-1])-1]+"\"";
		}
		return out;
	}//FinMethod
	//public int room_multiple_total;
	public String print_room_solution(int session) {
		String out = "";
		int cl = this.instanceUTP.session_class[session];
		int part = this.instanceUTP.class_part[cl-1];
		if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
			int[] cl_j = this.solution.getSetVal(this.x_rooms[this.instanceUTP.class_position_multiple_room[cl-1]]);
			for(int i = 0; i < cl_j.length ;i++) { 
				int t_room = cl_j[i];
				if(t_room > 0) {
					if(i < this.instanceUTP.part_room_worst_case[part-1]-1 ) {
						
						out += "\""+this.instanceUTP.room_name[t_room]+"\",";
					}
					else {
						out += "\""+this.instanceUTP.room_name[t_room]+"\",";
					}
				}

				
				//this.room_multiple_total++;
			}
			if(out.length() > 0) {
				out = out.substring(0,out.length()-1);
			}

		}
		else {
			out += "\""+this.instanceUTP.room_name[this.solution.getIntVal(this.x_room[cl-1])]+"\"";
		}
		if(out.equals("")) {
			out = "\"Amphi-B-EVAL\"";
		}
		return out;
	}//FinMethod
	
	public String print() {
		//this.room_multiple_total = 0;
		if(this.solution != null){
			String out = "";
			
			for(int i = 0; i<this.instanceUTP.nr_sessions;i++) {
				out +=  //"session:"+(i+1)+";"+
						"rank:"+this.instanceUTP.session_rank[i]+
						";course:"+this.instanceUTP.course_name[this.instanceUTP.part_course[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]-1]+
						";part:"+ this.instanceUTP.part_name[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]+
						";class:"+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]+
						";teachers:"+"["+print_teacher_solution(i)+"]"+
				     	";rooms:"+"["+print_room_solution(i)+"]"+
						";group:["+print_group_solution(i)+"]"+
						";slot:["+this.solution.getIntVal(x_slot[i])+
						", "+this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]+"]\n";
				//out +=  x_s[i]+" ";
			}
			//return out;
			/*for(int i = 0; i < t_m_r.length ;i++) {
				int cl = this.instanceUTP.class_multiple_room[i];
				System.out.println(this.instanceUTP.class_name[cl-1]+" t_m_r["+i+"] = "+t_m_r[i]+" ");
			}
			System.out.println("====");
			for(int i = 0; i < s_room.length ;i++) {
				//int cl = this.instanceUTP.class_multiple_room[i];
				System.out.println(" s_room["+i+"] = "+s_room[i]+" "+x_rooms[i]+" "+this.instanceUTP.room_name[solution.getIntVal(x_rooms[i])]);
			}*/
			//System.out.println(out);
			return out;
		   //System.out.println(this.solution.toString());
		}
		else {
			String out = "UNSAT";
			return out;
		}
	}//FinMethod
	
}//FinClass
