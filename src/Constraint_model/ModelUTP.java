package Constraint_model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cumulative.Cumulative;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMax;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainRandom;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.AntiFirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.DomOverWDeg;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.ImpactBased;
import org.chocosolver.solver.search.strategy.selectors.variables.InputOrder;
import org.chocosolver.solver.search.strategy.selectors.variables.Largest;
import org.chocosolver.solver.search.strategy.selectors.variables.Smallest;
import org.chocosolver.solver.search.strategy.selectors.variables.Random;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;

@SuppressWarnings("unused")//TODO
public class ModelUTP {
	//Attribute
	private InstanceUTPArray instanceUTP;
	private Model model;
	private IntVar[] x_teacher;
	//private SetVar[] x_teacher;
	private IntVar[] x_room;
	//private SetVar[] x_room;
	private IntVar[] x_slot;
	private IntVar[][] x_slot2d;
	private IntVar[] y_slot;
	private Solution solution;
	//private StatisticUtils statistics;
	private Solver solver;
	private static String filename_solution;
	private IntVar[] x_teachers;
	private IntVar[] x_rooms;
	private int strategie_choice;
	private StrategieBuilt strategie;
	
	private IntVar[] x_session;
	
	private double ran;

	//private IntVar[] t_m_r;
	//Method
	public ModelUTP(InstanceUTPArray instanceUTP) {
		this.model = new Model("Modele USP");
		this.strategie_choice = 1;//TODO
		this.instanceUTP = instanceUTP;
		x_slot = new IntVar[instanceUTP.nr_sessions];
		x_slot2d = new IntVar[1][instanceUTP.nr_sessions];
		y_slot = new IntVar[instanceUTP.nr_sessions];
		x_room = new IntVar[instanceUTP.nr_classes];
		x_teacher = new IntVar[instanceUTP.nr_sessions];
		
		x_rooms = new IntVar[instanceUTP.nr_class_multiple_room];
		
		//x_rooms = new IntVar[];
		x_teachers = new IntVar[this.instanceUTP.nr_class_multiple_teacher];
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
	
	public IntVar[] different_multiple_teacher(int val,int count,int c) {
		IntVar[] tab = new IntVar[count];
		
		for(int i = 0; i < count ;i++) {
			tab[i] = x_teachers[val+i]; 
		}
		return tab;
		
	}//FinMethod
	
	public IntVar[] different_multiple_room(int val,int count,int c) {
		IntVar[] tab = new IntVar[count];
		
		for(int i = 0; i < count ;i++) {
			tab[i] = x_rooms[val+i]; 
		}
		return tab;
		
	}//FinMethod
	
	public void different_rooms() {
		int val = 0;
		for(int c = 0; c < this.instanceUTP.class_multiple_room.length ;c++) {
			//System.out.println(val+"  "+(val+this.instanceUTP.part_room_worst_case[this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[c]-1]-1]));
			//model.allDifferent(different_multiple_room(val,this.instanceUTP.part_room_worst_case[this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[c]-1]-1],c)).post();
			model.allDifferentExcept0(different_multiple_room(val,this.instanceUTP.part_room_worst_case[this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[c]-1]-1],c)).post();
			val += this.instanceUTP.part_room_worst_case[this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[c]-1]-1];
		}
	}//FinMethod
	
	
	public void different_teachers() {
		int val = 0;
		for(int c = 0; c < this.instanceUTP.class_multiple_teacher.length ;c++) {
			model.allDifferent(different_multiple_teacher(val,this.instanceUTP.part_session_teacher_count[this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[c]-1]-1],c)).post();
			val += this.instanceUTP.part_session_teacher_count[this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[c]-1]-1];
		}
	}//FinMethod
	public IntVar[] t;
	public void testgcc() {
		int y =4;//instanceUTP.nr_days_per_week * (instanceUTP.nr_weeks-17);
		
		Vector<Integer> ts = new Vector<Integer>();
		int cunt= 0;
		for(int  i = 0; i < instanceUTP.nr_classes ; i++) {
			int u = 1;
			int p = instanceUTP.class_part[i]-1;
			if (!labelExist(p) && this.instanceUTP.part_weeks.get(p).get(0) == 1) {
				if (instanceUTP.class_sessions.get(i).size() < u) {
					u = instanceUTP.class_sessions.get(i).size();
				}
				if(instanceUTP.class_sessions.get(i).get(0) >= 763) {
					for(int yi = 0; yi < u ;yi++ ){
						ts.add(instanceUTP.class_sessions.get(i).get(yi)-1);
						cunt++;
					}
				}

			}
		}
		t = new IntVar[cunt];
		System.out.println(cunt+"i willbeback");
		for(int i = 0 ; i < cunt ;i++) {
			t[i] = model.intVar(1,120);
			
			model.arithm(t[i], "=", x_slot[ts.get(i)].sub(1).div(instanceUTP.nr_slots_per_day).add(1).intVar()).post();;
		}
		int[] kk = new int[y];
		int[] kk0 = new int[y];
		IntVar[] kkmax = new IntVar[y];
		for(int i = 0 ; i < y ; i++) {
			kk[i] = i+1; 
			kk0[i] = 0;
			kkmax[i] = model.intVar(34);
		}
		kk[y-1] = 4; 
		
		model.globalCardinality(t, kk, kkmax, false).post();;
		
		
	}//FinMethod
	
	public void initMultpileVariables() {
		int j = 0;
		for (int i = 0; i < this.instanceUTP.class_multiple_teacher.length ;i++) {
			j += this.instanceUTP.part_session_teacher_count[this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1]-1];
		}
		IntVar[] tab = new IntVar[j];
		int m = 0;
		//System.out.println("j = "+j);
		for (int i = 0; i < this.instanceUTP.class_multiple_teacher.length ;i++) {
			
			int c = this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1];
			for(int k = 0; k <  this.instanceUTP.part_session_teacher_count[c-1] ;k++) {
				tab[m] = model.intVar("x_teacher_"+m,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1]-1)));
				m++;
			}
			//tab[i] = model.intVar("x_teacher_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1]-1)));
		}
		this.x_teachers = tab;
		different_teachers();
		
	}//FinMethod
	
	//private IntVar[] s_room;
	public void initMultpileVariablesRoom() {
		int j = 0;
		for (int i = 0; i < this.instanceUTP.class_multiple_room.length ;i++) {
			j += this.instanceUTP.part_room_worst_case[this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1]-1];
		}
		IntVar[] tab = new IntVar[j];
		int m = 0;
		//System.out.println("j = "+j);
		//this.s_room = new IntVar[j];
		for (int i = 0; i < this.instanceUTP.class_multiple_room.length ;i++) {
			
			int c = this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1];
			for(int k = 0; k <  this.instanceUTP.part_room_worst_case[c-1] ;k++) {
				tab[m] = model.intVar("x_rooms_"+m,part_i_slots_rooms(this.instanceUTP.part_rooms.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_room[i]-1]-1)));
				m++;
			}
			//tab[i] = model.intVar("x_teacher_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.class_multiple_teacher[i]-1]-1)));
		}
		this.x_rooms = tab;
		different_rooms();
		
	}//FinMethod
	
	public void decalage() {
		int d = instanceUTP.nr_days_per_week;
		int s = instanceUTP.nr_slots_per_day;
		for(int cl = 0 ; cl < this.instanceUTP.nr_classes ; cl++) {
			if(!labelExist(this.instanceUTP.class_part[cl]-1) && this.instanceUTP.class_sessions.get(cl).size() >=4) {
				for(int j = 0 ; j < 4 ;j++) {
					model.arithm(
							x_slot[this.instanceUTP.class_sessions.get(cl).get(j)-1].sub(1).div(s).mod(d).add(1).intVar(),
							"=",
							y_slot[this.instanceUTP.class_sessions.get(cl).get(j)-1].sub(1).div(s).mod(d).add(j).mod(d).add(1).intVar()).post();;;
				
				}
			}
			
		}
		
	}//FinMethod
	
	public void initVariables() {
		for(int i = 0; i < instanceUTP.nr_sessions ;i++ ) {
			//x_slot[i] = model.intVar("x_slot_"+i,1,this.instanceUTP.nr_slot());
			//Integer[] t = this.instanceUTP.part_slots.get(i).toArray(new Integer[this.instanceUTP.part_slots.get(i).size()]);
			x_slot[i] = model.intVar("x_slot_"+(i+1),part_i_slots(this.instanceUTP.part_slots.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1)));
			x_slot2d[0][i] = x_slot[i];
		}
		
		for(int i = 0; i < instanceUTP.nr_sessions ;i++ ) {
			//x_slot[i] = model.intVar("x_slot_"+i,1,this.instanceUTP.nr_slot());
			//Integer[] t = this.instanceUTP.part_slots.get(i).toArray(new Integer[this.instanceUTP.part_slots.get(i).size()]);
			y_slot[i] = model.intVar("y_slot_"+(i+1),part_i_slots(this.instanceUTP.part_slots.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1)));
		}
		
		for(int i = 0; i < instanceUTP.nr_sessions ;i++ ) {
			if(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).size() == 0) {
				x_teacher[i] = model.intVar("x_teacher_"+i,0);
			}
			else {
				x_teacher[i] = model.intVar("x_teacher_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1)));
			}
		}
		
		for(int i = 0; i < instanceUTP.nr_classes ;i++ ) {
			if(this.instanceUTP.part_rooms.get(this.instanceUTP.class_part[i]-1).size() == 0 ) {
				x_room[i] = model.intVar("x_room_"+i,0);
			}
			else {
				x_room[i] = model.intVar("x_room_"+instanceUTP.class_name[i]+"_"+i,part_i_slots(this.instanceUTP.part_rooms.get(this.instanceUTP.class_part[i]-1)));
			}
			
		}
	}//FinMethod
	
	public boolean labelExist(int part) {
		String[] labels = new String[] {"REPAS","CC","EXAM","CT","EEO"};
		//System.out.println("Size of : "+this.instanceUTP.part_label.get(part).size());
		for(int pl = 0; pl < this.instanceUTP.part_label.get(part).size() ;pl++) {
			for(int lnum = 0; lnum < labels.length ; lnum++) {
				//System.out.println(part+" "+labels[lnum]+": l : "+this.instanceUTP.label_name[this.instanceUTP.part_label.get(part).get(pl)-1]);
				if(this.instanceUTP.label_name[this.instanceUTP.part_label.get(part).get(pl)-1].equals(labels[lnum])) {
					//System.out.println(part+" "+labels[lnum]);
					return true;
				}
			}	 
		}
		return false;
	}//FinMethod
	
	public void constraint_day_share(int part) {
		if(this.instanceUTP.group_of_classes_eq.get(part).size() == 3) {
			//int[] day = new int[]{1,2,4};
			//int[] day = new int[]{1,1250,2500};
			int[] day = new int[]{1,1250,3000};
			//int[] day = new int[]{1,3000,6000};

			for(int p_bench = 0; p_bench < day.length ;p_bench++) {
				for( int i = 0; i < this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).size(); i++){
					int classe = this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).get(i);
					//this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)].div(this.instanceUTP.nr_slots_per_day).sub(1).mod(this.instanceUTP.nr_days_per_week).add(1).intVar(),">=",day[p_bench]).post();
					//this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)].sub(1).mod(7200).add(1).intVar(),">=",day[p_bench]).post();
					this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)],">=",day[p_bench]).post();
				}
			}
		}
		else if(this.instanceUTP.group_of_classes_eq.get(part).size() == 4){
			//int[] day = new int[]{1,2,4,5};
			//int[] day = new int[]{1,1440,4320,5000};//2880
			//int[] day = new int[]{1,1800,3600,5200};//2880
			int[] day = new int[]{1,4000,4000,4000};//2880
			//int[] day = new int[]{1,2000,4000,6000};//2880
			for(int p_bench = 0; p_bench < day.length ;p_bench++) {
				for( int i = 0; i < this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).size(); i++){
					int classe = this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).get(i);
					//this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)-1].sub(1).div(this.instanceUTP.nr_slots_per_day).mod(this.instanceUTP.nr_days_per_week).add(1).intVar(),">=",day[p_bench]).post();//-1)*this.instanceUTP.nr_slots_per_day
					this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)],">=",day[p_bench]).post();
				}
			}
		}
		else if(this.instanceUTP.group_of_classes_eq.get(part).size() == 5) {
			//int[] day = new int[]{1,2,3,4,5};
			//int[] day = new int[]{1,1440,2880,4320,5760};
			//int[] day = new int[]{1,1500,3000,4500,6000};//2880
			int[] day = new int[]{1,4000,4000,4000,4000};//2880
			for(int p_bench = 0; p_bench < day.length ; p_bench++) {
				for( int i = 0; i < this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).size(); i++){
					int classe = this.instanceUTP.group_of_classes_eq.get(part).get(p_bench).get(i);
					//this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)].div(this.instanceUTP.nr_slots_per_day).sub(1).mod(this.instanceUTP.nr_days_per_week).add(1).intVar(),">=",day[p_bench]).post();
					this.model.arithm(x_slot[this.instanceUTP.class_sessions.get(classe-1).get(0)],">=",day[p_bench]).post();

				}
			}
		}
		/* out = part+": {";
		for(int j =0;j< this.instanceUTP.group_of_classes_eq.get(part).size();j++) {
			out+="[";
			for(int u =0;u< this.instanceUTP.group_of_classes_eq.get(part).get(j).size();u++){
				out+=""+this.instanceUTP.group_of_classes_eq.get(part).get(j).get(u)+",";
			}
			out+="],";
		}
		out+="}\n";
		System.out.println(out);*/
		
	}//FinMethod
	
	public void bench_class_equilibrate() {
		for(int p = 0; p < this.instanceUTP.nr_parts ; p++) {
			if(!labelExist(p)) {
				constraint_day_share(p);
			}
		}
	}//FinMethod
	
	public void x_session_generator() {
		int nr_day = this.instanceUTP.nr_days_per_week * this.instanceUTP.nr_weeks;
		this.x_session = new IntVar[nr_day];
		for(int i =0 ; i < nr_day ;i++) {
			this.x_session[i] = model.intVar("x_session_d"+i,1,this.instanceUTP.nr_sessions);
		}
	}//FinMethod
	
	public void constraint() {
		implicite_sequenced_sessions();
		teacher_service_v2();
		//disjunctive_teacher_v2();
		disjunctive_teacher_ags();
		disjunctive_group_v2();
		disjunctive_room_v2();
		size_of_multiroom();
		//cardinal_x_rooms();
		flatten_constraint();
		bench_class_equilibrate();
		//decalage();
		//testgcc();
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
	
	public IntVar[] t_ss_v2_vars(int p) {
		int part_teacher_count = this.instanceUTP.part_session_teacher_count[p];
		int size_tab = part_teacher_count * this.instanceUTP.part_classes.get(p).size();
		
		IntVar[] tab = new IntVar[size_tab];
		//System.out.println(size_tab);
		int k = 0;
		for(int i = 0; i < this.instanceUTP.part_classes.get(p).size() ;i++) {
			for(int j = 0; j < part_teacher_count ;j++) {
				tab[k] = x_teachers[this.instanceUTP.class_position_multiple_teacher[this.instanceUTP.part_classes.get(p).get(i)-1]+j];
				//System.out.println("t.k "+k);
				k++;
			}
		}
		//System.out.println("t.size "+k);
		return tab;
	}//FinMethod	
	
	public IntVar[] t_s_v2_vars(int p) {
		IntVar[] tab = new IntVar[this.instanceUTP.part_classes.get(p).size() * this.instanceUTP.part_nr_sessions[p]];
		int count = 0;
		for(int i = 0; i < this.instanceUTP.part_classes.get(p).size() ;i++) {
			for(int j = 0;j < this.instanceUTP.class_sessions.get(this.instanceUTP.part_classes.get(p).get(i)-1).size(); j++) {
				tab[count] = x_teacher[this.instanceUTP.class_sessions.get(this.instanceUTP.part_classes.get(p).get(i)-1).get(j)-1];
				count++;
			}
		}
		//System.out.println("t.size "+tab.length);
		return tab;
	}//FinMethod
	
	public IntVar[] t_s_v2_cards(int p) {
		IntVar[] tab = new IntVar[this.instanceUTP.part_teachers.get(p).size()];
		//this.instanceUTP.part_teachers.get(p)
		for(int i = 0; i < this.instanceUTP.part_teachers.get(p).size() ;i++) {
			int val = this.instanceUTP.part_teacher_sessions_count[p][this.instanceUTP.part_teachers.get(p).get(i)-1];///this.instanceUTP.part_nr_sessions[p];
			//System.out.println("Part "+(p+1)+" Teacher "+(this.instanceUTP.part_teachers.get(p).get(i)-1)+" val :"+val+" serv "+this.instanceUTP.part_teacher_sessions_count[p][this.instanceUTP.part_teachers.get(p).get(i)-1]);
			tab[i] = model.intVar("",val);
		}
		/*int j= 0;
		for(int i = 0; i < tab.length ;i++) {
			j += tab[i].getLB();
		}
		System.out.println("sum "+j);*/
		return tab;
	}//FinMethod
	
	/**
	 * 
	 * */
	public void teacher_service_v2() {
		for(int p = 0; p < this.instanceUTP.nr_parts ;p++) {
			//System.out.println("size : "+part_i_slots(this.instanceUTP.part_teachers.get(p)).length+" "+this.instanceUTP.part_name[p]);
			//System.out.println("size : "+t_s_v2_cards(p).length);
			if(this.instanceUTP.part_teachers.get(p).size() !=0 ) {
				if(this.instanceUTP.part_session_teacher_count[p] <= 1) {
					model.globalCardinality(t_s_v2_vars(p),part_i_slots(this.instanceUTP.part_teachers.get(p)),t_s_v2_cards(p),true).post();			
				}
				else {
					//System.out.println(this.instanceUTP.part_name[p]);
					//System.out.println("size "+t_ss_v2_vars(p).length);
					model.globalCardinality(t_ss_v2_vars(p),part_i_slots(this.instanceUTP.part_teachers.get(p)),t_s_v2_cards(p),true).post();			
				}
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
					tabBool[it] = model.intEqView(x_teacher[this.instanceUTP.class_sessions.get(classe-1).get(k)-1], t+1);
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
				model.cumulative(sessions_of_teacher_disjun_v2(t),generate_heigth_teacher_v2(t),model.intVar(1),true,Cumulative.Filter.DISJUNCTIVE_TASK_INTERVAL).post();
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
				part_service_t[i] = this.instanceUTP.part_teacher_sessions_count[i][t-1]; /// this.instanceUTP.part_nr_sessions[i];
				part_concerned.add(i+1);
				s_max += part_service_t[i];
			}
		}
		int k = 0;
		IntVar[] tab =new IntVar[s_max];
		IntVar[] tabTmp =new IntVar[s_max];
		for(int i = 0; i < part_concerned.size() ;i++) {
			for(int j = 0; j < this.instanceUTP.part_classes.get(i).size() ;j++) {//part_service_t[part_concerned.get(i)-1] ;j++) {
				int cl = this.instanceUTP.part_classes.get(i).get(j)-1;
				for(int m=0; m < instanceUTP.class_sessions.get(cl).size() ;m++){
					int sessi = instanceUTP.class_sessions.get(cl).get(m) -1;
					x_teacher[sessi].eq(t).imp(tabTmp[k].eq(this.instanceUTP.part_classes.get(i).get(j))).post();	
					k++;
				}
				
				
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
	
	public IntVar[] sum_var_rooms_disjunct(int classe, int t) {
		int cl = this.instanceUTP.class_position_multiple_room[classe-1];
		int part = this.instanceUTP.class_part[classe-1];
		//IntVar[] tab = new IntVar[this.instanceUTP.part_room_worst_case[part-1]];
		IntVar[] tabtmp = new IntVar[this.instanceUTP.part_room_worst_case[part-1]];
		//System.out.println("rooms"+this.instanceUTP.class_name[cl_r-1]+" : "+this.instanceUTP.part_room_worst_case[part-1]);
		for(int i = 0; i < this.instanceUTP.part_room_worst_case[part-1];i++) {
			//tabtmp[i] = model.intVar(0,1);
			tabtmp[i] = model.intEqView(x_rooms[cl+i], t+1);
		}
		return tabtmp;
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
					for(int k = 0; k < this.instanceUTP.class_sessions.get(classe-1).size() ;k++) {
						tab[it] = model.intVar(0,1);
						for(int m = 0; m < this.instanceUTP.part_room_worst_case[part-1];m++) {
							//model.arithm(model.intVar(0),"=",tab[it]).post();
							model.sum(sum_var_rooms_disjunct(classe,t),"=",tab[it]).post();
						}
						//model.arithm(tab[it], "=", tabBool[it].intVar()).post(); 
						//model.arithm(tabBool[it],"=",x_teacher[classe],"=",(t+1));
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
						//tabBool[it] = model.boolVar(false);//model.intEqView(x_room[classe-1], t+1);
						tabBool[it] = model.intEqView(x_room[classe-1],t+1);
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
			if(this.instanceUTP.room_parts.get(t).size() > 0 && t!=3 ) {
				
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				//Disjunctive
				Constraint cons = model.cumulative(sessions_of_room_disjun_v2(t),generate_heigth_room_v2(t),model.intVar(1),true,Cumulative.Filter.DISJUNCTIVE_TASK_INTERVAL);
				cons.post();
				cons.setName("cumulative_"+(t+1));
				//System.out.println("room "+this.instanceUTP.room_name[t+1]+" constId "+cons.getName());
			}
			/*else {
				System.out.println("MAUVAISE room :"+this.instanceUTP.room_name[t]);
				int p_bad = 8;
				System.out.println("bad part "+this.instanceUTP.part_name[this.instanceUTP.room_parts.get(t).get(p_bad)-1]);
				//this.instanceUTP.room_parts.get(t).removeElementAt(p_bad);

				Constraint cons = model.cumulative(sessions_of_room_disjun_v2(t),generate_heigth_room_v2(t),model.intVar(1));
				cons.post();
				cons.setName("cumulative_"+(t+1));
			}*/
		}
		
	}//FinMethod
	
	//======================
	
	//======================
	public IntVar[] tttt;
	public Task[] sessions_of_group_disjun_v2(int t) {
		int max_size = this.instanceUTP.group_sessions.get(t).size();
		Task[] tab = new Task[max_size];
		tttt = new IntVar[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.group_sessions.get(t).size() ;i++) {
			tab[it] = model.taskVar(x_slot[this.instanceUTP.group_sessions.get(t).get(i)-1],this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[this.instanceUTP.group_sessions.get(t).get(i)-1]-1]-1]);
			tttt[it] = tab[it].getStart();
			it++;
		}
		//System.out.println("y : "+it);
		return tab;
		
	}//FinMethod
	
	public IntVar[] sessions_of_group_disjun_v2_intvar(int t) {
		int max_size = this.instanceUTP.group_sessions.get(t).size();
		IntVar[] tab = new IntVar[max_size];
		int it = 0;
		for(int i = 0; i < this.instanceUTP.group_sessions.get(t).size() ;i++) {
			tab[it] = x_slot[this.instanceUTP.group_sessions.get(t).get(i)-1];
			it++;
		}
		//System.out.println("y : "+it);
		return tab;
		
	}//FinMethod

	public IntVar[] sessions_of_group_disjun_v2_intvar_crtl(int t) {

		int maxinterv = 50;
		int mininterv = 30;
		int intver = maxinterv-mininterv+1;
		
		int p = 0;
		int max_size = this.instanceUTP.group_sessions.get(t).size();
		IntVar[] tab = new IntVar[max_size-intver];
		int it = 0;
		System.out.println("maxsize = "+max_size);
		String out = "";
		
		for(int i = p; i < max_size ;i++) {
			if(i < mininterv || i > maxinterv) {
				tab[it] = x_slot[this.instanceUTP.group_sessions.get(t).get(i)-1];
				it++;
			}
			else {
				out+=this.instanceUTP.group_sessions.get(t).get(i)+" ";
			}
		}
		//System.out.println("y : "+it);
		System.out.println("problem = "+out);
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
				model.cumulative(sessions_of_group_disjun_v2(g),heights,model.intVar(1),false,Cumulative.Filter.DEFAULT).post();
				if(g < 7 || g > 12) {
					System.out.println(g);
					//model.allDifferent(tttt,"BC").post();;
					//model.allDifferent(sessions_of_group_disjun_v2_intvar(g)).post();;
				}
				else {
				String out = "";
				for(int i = 0; i < this.instanceUTP.group_sessions.get(g).size() ;i++) {
					out+=this.instanceUTP.group_sessions.get(g).get(i)+" ";
				}
				System.out.println(out);
				//model.allDifferent(sessions_of_group_disjun_v2_intvar_crtl(g)).post();;

				}
				//model.allDifferent(tttt,"BC").post();;
			}
		}
		
		
	}//FinMethod
	
	public IntVar[] sum_var_rooms(int num) {
		int cl_r = this.instanceUTP.class_multiple_room[num];
		int cl = this.instanceUTP.class_position_multiple_room[cl_r-1];
		int part = this.instanceUTP.class_part[cl_r-1];
		//IntVar[] tab = new IntVar[this.instanceUTP.part_room_worst_case[part-1]];
		IntVar[] tabtmp = new IntVar[this.instanceUTP.part_room_worst_case[part-1]];
		//System.out.println("rooms"+this.instanceUTP.class_name[cl_r-1]+" : "+this.instanceUTP.part_room_worst_case[part-1]);
		for(int i = 0; i < this.instanceUTP.part_room_worst_case[part-1];i++) {
			tabtmp[i] = model.intVar("size_r_"+""+this.instanceUTP.class_name[cl_r-1],0,this.instanceUTP.max_room_capacity);
		    model.element(tabtmp[i], this.instanceUTP.room_capacity_v2,x_rooms[cl+i]).post();
			//this.s_room[this.jjjjjj] = tabtmp[i];
			//this.jjjjjj++;
		}
		return tabtmp;
	}//FinMethod
	//public int jjjjjj;
	public IntVar sum_var_room(int num) {
		int cl_r = this.instanceUTP.class_multiple_room[num];
		int cl = this.instanceUTP.class_position_multiple_room[cl_r-1];
		//int part = this.instanceUTP.class_part[cl_r-1];
		//IntVar[] tab = new IntVar[this.instanceUTP.part_room_worst_case[part-1]];
		IntVar tabtmp;// = new IntVar();
		//System.out.println("room : "+this.instanceUTP.class_name[cl_r-1]+" : "+this.instanceUTP.part_room_worst_case[part-1]);
		tabtmp = model.intVar("size_r_"+""+this.instanceUTP.class_name[cl_r-1],0,this.instanceUTP.max_room_capacity);
		model.element(tabtmp, this.instanceUTP.room_capacity_v2,x_rooms[cl]).post();
		//this.s_room[this.jjjjjj] = tabtmp;
		//this.jjjjjj++;
		return tabtmp;
	}//FinMethod
	
	public void size_of_multiroom() {
		//IntVar[] t_m_r = new IntVar[this.instanceUTP.nr_class_multiple_room];
		//this.t_m_r = new IntVar[this.instanceUTP.nr_class_multiple_room];
		//this.jjjjjj = 0;
		//int max_size = 2000;
		//System.out.println("nr_rooms "+this.instanceUTP.nr_rooms);
		for(int i = 0; i < this.instanceUTP.nr_class_multiple_room ;i++) {
			int size_groups = 0;
			int cl_r = this.instanceUTP.class_multiple_room[i];
			for(int g = 0; g < this.instanceUTP.class_groups.get(cl_r-1).size() ;g++) {
				size_groups += this.instanceUTP.group_headcount[this.instanceUTP.class_groups.get(cl_r-1).get(g)-1];
			}
			//System.out.println("g_size = "+size_groups);
			//t_m_r[i] = model.intVar(0, max_size);
			//model.arithm(t_m_r[i],">=", size_groups).post();
			//sum_var_rooms(i);
			
			//int cl = this.instanceUTP.class_position_multiple_room[cl_r-1];
			int part = this.instanceUTP.class_part[cl_r-1];
			if(this.instanceUTP.part_room_worst_case[part-1] <= 1) {
				model.arithm(sum_var_room(i), ">=", size_groups).post();
				//model.sum(sum_var_rooms(i),">=",model.intVar(size_groups)).post();
			}
			else {
				//System.out.println("multi : "+this.instanceUTP.class_name[cl_r-1]);
				model.sum(sum_var_rooms(i),">=",model.intVar(size_groups)).post();
				//model.sum(sum_var_rooms(i),"=",t_m_r[i]).post();
				//model.arithm(t_m_r[i],">=",model.intVar(size_groups)).post();
			}
			//System.out.println("=============");
			//model.arithm(x_rooms[i]., filename_solution, i)
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
	
	public IntVar[] generateXSlot(Vector<Vector<Integer>> cs) {
		IntVar[] tab = new IntVar[cs.get(0).size()];
		for(int i = 0; i< cs.get(0).size() ;i++) {
			tab[i] = x_slot[cs.get(0).get(i)-1];
		}
		return tab;
	}//FinMethod
	
	public IntVar[] generateXrooms(Vector<Vector<Integer>> cs) {
		Vector<IntVar> tab = new Vector<IntVar>();
		Vector<IntVar> tab2 = new Vector<IntVar>();
		for(int i = 0; i< cs.get(0).size() ;i++) {
			int sess = cs.get(0).get(i)-1;
			int part = this.instanceUTP.class_part[this.instanceUTP.session_class[sess]-1];
			///System.out.println("passsage");
			if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
				for(int j = 0; j < this.instanceUTP.part_room_worst_case[part-1] ;j++) {
					tab2.add(this.x_rooms[this.instanceUTP.class_position_multiple_room[this.instanceUTP.session_class[sess]-1]+j]);
				}
				
			}
			else {
				tab.add(x_room[this.instanceUTP.session_class[sess]-1]);
			}
		}
		IntVar[] tabFinal = new IntVar[tab.size()+tab2.size()];
		for(int i = 0; i < tab.size() ;i++) {
			tabFinal[i] = tab.get(i);
		}
		int j = 0;
		for(int i = tab.size(); i < tab.size()+tab2.size() ;i++) {
			tabFinal[i] = tab2.get(j);
			//System.out.println("name : "+tab2.get(j).getName());
			j++;
		}
		//System.out.println("length "+tab.size()+" size : "+tab2.size());
		return tabFinal;
	}//FinMethod
	
	public void sameSlot(ConstraintUTP constraint) {
		
		/*if(constraint.getRule() > 90 & constraint.getRule() < 94) {
			//System.out.println("");
			System.out.println("MAUVAISE : "+constraint.getConstraint()+" "+constraint.getRule());
			//return;
		}*/
		//System.out.println(constraint.getConstraint()+" "+constraint.getRule());
		this.model.allEqual(generateXSlot(constraint.getSessions())).post();
		this.model.allDifferentExcept0(generateXrooms(constraint.getSessions())).post();
		/*for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1], "=", x_slot[constraint.getSessions().get(0).get(i)-1]).post();
				//precedence()
			}
		}*/
	}//FinMethod

	public void forbiddenPeriod(ConstraintUTP constraint) {
		//Vector<Integer> allTimes = new Vector<Integer>();
		int first_num = search_value(constraint,"first");
		int last_num = search_value(constraint,"last");
		//System.out.println("Contrainte "+constraint.getCpt()+" rule "+constraint.getRule());

		
		int first = Integer.parseInt(this.instanceUTP.parameter_value.get(first_num).get(0));
		int last = Integer.parseInt(this.instanceUTP.parameter_value.get(last_num).get(0));
		
		if(constraint.getParameters().length >=3) {
			int unite_num = search_value(constraint,"period");

			int unite = unit_value(this.instanceUTP.parameter_value.get(unite_num).get(0));
			int unite_bound = unit_value_bound(this.instanceUTP.parameter_value.get(unite_num).get(0));
			//System.out.println("unite "+unite);
			//System.out.println("unitebound "+unite_bound);
			/*for(int i = first; i <= last;i++) {
				allTimes.add(i);
			}*/
			int[][] valuePeriod = new int[unite_bound][2];
			for(int i = 0; i < unite_bound ;i++) {
				valuePeriod[i][0] = first + (i*unite);
				valuePeriod[i][1] = last + (i*unite);
			}
			for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
				for(int j = 0; j < unite_bound ;j++) {
					try {
						x_slot[constraint.getSessions().get(0).get(i)-1].removeInterval(valuePeriod[j][0]-this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1]-1], valuePeriod[j][1], solution);
					} catch (ContradictionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1],"!=", allPeriod.get(j).intValue()).post();
				}
			}
		}
		else {
			for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
					try {
						x_slot[constraint.getSessions().get(0).get(i)-1].removeInterval(first, last, solution);
					} catch (ContradictionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		}

	}//FinMethod
	
	
	public void sameTeachers(ConstraintUTP constraint) {
		IntVar[] tab = new IntVar[constraint.getSessions().get(0).size()];
		IntVar[] tabDiff = new IntVar[constraint.getSessions().get(0).size()];
		for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			tab[i] = x_teacher[constraint.getSessions().get(0).get(i)-1];
			tabDiff[i] = x_slot[constraint.getSessions().get(0).get(i)-1];
		}
		//model.allDifferent(tabDiff).post();
		model.allEqual(tab).post();
		//System.out.println();
	}//FinMethod
	
	public void sameRooms(ConstraintUTP constraint) {
		IntVar[] tabDiff = new IntVar[constraint.getSessions().get(0).size()];
		for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			tabDiff[i] = x_slot[constraint.getSessions().get(0).get(i)-1];
		}
		//model.allDifferent(tabDiff).post();
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
		  IntVar[] tabDiff = new IntVar[constraint.getSessions().get(0).size()];
		  tabDiff[0] = x_slot[constraint.getSessions().get(0).get(0)-1];
		  
		  if(constraint.getSessions().get(0).size()>1) {
			  for(int i = 1; i < constraint.getSessions().get(0).size() ;i++) {
				  tabDiff[i] = x_slot[constraint.getSessions().get(0).get(i)-1];
				  model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1], "=", x_slot[constraint.getSessions().get(0).get(0)-1],"+",sum_val*i).post();
			  }
			  //model.allDifferent(tabDiff).post();
		  }
		  
		//System.out.println();
	}//FinMethod
	
	public void soft_periodic(ConstraintUTP constraint) {
		  //System.out.println("SOFT PERIODIC");
		  int value_num = search_value(constraint,"value");
		  int unite_num = search_value(constraint,"unit");
		  //System.out.println("Val "+value_num+" unite "+unite_num);
		  int value = Integer.parseInt(this.instanceUTP.parameter_value.get(value_num).get(0));
		  int unite = unit_value(this.instanceUTP.parameter_value.get(unite_num).get(0));
		  int sum_val = value * unite;
		  
		  IntVar[] X = new IntVar[constraint.getSessions().get(0).size()];
		  for(int i = 0 ; i < constraint.getSessions().get(0).size() ; i++ ) {
			  X[i] = model.intVar(1,this.instanceUTP.nr_days_per_week);
			  
		  }
		  for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
			  //X[i] == x_slot[i]-> 
			  //model.arithm(, filename_solution, i)
		  }
		  
		  if(constraint.getSessions().get(0).size()>1) {
			  for(int i = 1; i < constraint.getSessions().get(0).size() ;i++) {
				  model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1], "=", x_slot[constraint.getSessions().get(0).get(0)-1],"+",sum_val*i).post();
			  }
		  }
		//System.out.println();
	}//FinMethod
	
	public void weekly(ConstraintUTP constraint) {
		  //System.out.println("PERIODIC");
		  //System.out.println("Val "+value_num+" unite "+unite_num);
		  int sum_val = this.instanceUTP.nr_days_per_week * this.instanceUTP.nr_slots_per_day;
		  
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
		  
		  if(constraint.getParameters().length >= 3) {
			  //int unite_num = search_value(constraint,"period");

			//	int unite = unit_value(this.instanceUTP.parameter_value.get(unite_num).get(0));
				//int unite_bound = unit_value_bound(this.instanceUTP.parameter_value.get(unite_num).get(0));

				int weekslot_duration = this.instanceUTP.nr_slots_per_day * instanceUTP.nr_days_per_week;
				for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
					
					model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1].sub(1).mod(weekslot_duration).add(1).intVar(), ">=", first).post();
					model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1].sub(1).mod(weekslot_duration).add(1).intVar(), "<=", last).post();
					
		
						//model.arithm(x_slot[constraint.getSessions().get(0).get(i)-1],"!=", allPeriod.get(j).intValue()).post();
					
				}
		  }
		  else {
			  for(int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
				  model.arithm(x_slot[constraint.getSessions().get(0).get(i).intValue()-1],">=",first).post();
				  model.arithm(x_slot[constraint.getSessions().get(0).get(i).intValue()-1],"<=",last).post();
			  }
		  }
		  

	}//FinMethod
	
	public void sequenced(ConstraintUTP constraint) {
		//System.out.println(constraint.getConstraint()+" "+constraint.getRule());
		if(constraint.getArity() == 1) {
			/*for (int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
				for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
					int session_length = this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1]-1];
					model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1], ">=", x_slot[constraint.getSessions().get(0).get(i)-1],"+",session_length).post();
					//precedence()
				}
			}*/
		}
		else {
			for (int i = 0; i < constraint.getSessions().get(0).size() ;i++) {
				for(int j = 0; j < constraint.getSessions().get(1).size() ;j++) {
					int session_length = this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[constraint.getSessions().get(0).get(i)-1]-1]-1];
					model.arithm(x_slot[constraint.getSessions().get(1).get(j)-1], ">=", x_slot[constraint.getSessions().get(0).get(i)-1],"+",session_length).post();
					//precedence()
				}
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
		 int unite3 = this.instanceUTP.nr_slots_per_day;
		for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
			for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
				model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].div(unite3).mod(unite2).intVar(), "=", x_slot[constraint.getSessions().get(0).get(i)-1].div(unite3).mod(unite2).intVar()).post();
				//precedence()
			}
		}
	}//FinMethod
	
	public void sameWeeklySlot(ConstraintUTP constraint) {
		 int unite = this.instanceUTP.nr_slots_per_day * this.instanceUTP.nr_days_per_week;
		 //int unite2 = this.instanceUTP.nr_days_per_week;
			
		if(constraint.getSessions().get(0).size() > 1 ) {
			for (int i = 0; i < constraint.getSessions().get(0).size()-1 ;i++) {
				for(int j = i+1; j < constraint.getSessions().get(0).size() ;j++) {
					model.arithm(x_slot[constraint.getSessions().get(0).get(j)-1].mod(unite).intVar(), "=", x_slot[constraint.getSessions().get(0).get(i)-1].mod(unite).intVar()).post();
					//precedence()
				}
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
	
	public IntVar[] generateWeek(ConstraintUTP cons) {
		int m = this.instanceUTP.nr_slots_per_day*this.instanceUTP.nr_days_per_week;
		IntVar[] tab = new IntVar[cons.getSessions().get(0).size()];
		for(int i = 0; i < cons.getSessions().get(0).size() ;i++) {
			tab[i] = x_slot[cons.getSessions().get(0).get(i)-1].sub(1).div(m).add(1).intVar();
		}
		return tab;
	}//FinMethod
	
	public void differentWeek(ConstraintUTP constraint) {
		 model.allDifferent(generateWeek(constraint)).post();;
	}//FinMethod
	
	public IntVar[] generateSlots(ConstraintUTP cons) {
		IntVar[] tab = new IntVar[cons.getSessions().get(0).size()];
		for(int i = 0; i < cons.getSessions().get(0).size() ;i++) {
			tab[i] = x_slot[cons.getSessions().get(0).get(i)-1];
		}
		return tab;
	}//FinMethod
	
	public void differentSlots(ConstraintUTP constraint) {
		 model.allDifferent(generateSlots(constraint)).post();
	}//FinMethod
	
	
	public void disjunct(ConstraintUTP constraint) {
		//System.out.println();
	}//FinMethod
	
	public IntVar[] arg_sort(IntVar[] tab,int part) {
		IntVar[] j = new IntVar[tab.length];
		IntVar[][] tab2 = new IntVar[1][tab.length];
		IntVar[][] tab3 = new IntVar[1][tab.length];
		for(int i = 0; i < tab.length;i++) {
			j[i] = model.intVar("j_"+part+"_"+i,0,tab.length);
			tab2[0][i] = tab[i];
			//tab3[0][i] = model.intVar(1,200000);
		}

		
		//j = model.intVarArray("j",tab.length,1,tab.length);
		//forall(j in 1..length(x)-1)
	       //(x[p[j]] <= x[p[j+1]] /\ (x[p[j]] == x[p[j+1]] -> p[j] < p[j+1]));
		model.allDifferent(j).post();
		//model.keySort(tab2, j, tab3, 0).post();
		for(int i = 0 ; i < tab.length-1 ;i++) {
			IntVar var1 = model.intVar("tmp1",this.part_i_slots(instanceUTP.part_teachers.get(part)));
			IntVar var2 = model.intVar("tmp2",this.part_i_slots(instanceUTP.part_teachers.get(part)));
			model.element(var1,tab2,model.intVar(0),0,j[i],0);
			model.element(var2,tab2,model.intVar(0),0,j[i+1],0);
			model.arithm(var1,"<=", var2).post();
			//var1.eq(var2).imp(tab[i].ge(tab[i+1])).post();;
		}
		return j;
	}
	
	public IntVar[] generate_x_slot_arg_sort(int p,int t) {
		int uu = 0;
		for (int j = 0; j < instanceUTP.part_classes.get(p).size() ;j++) {
			uu += instanceUTP.class_sessions.get(instanceUTP.part_classes.get(p).get(j)-1).size();
		}
		IntVar[] tab = new IntVar[uu];
		int count = 0;
		int minSess = instanceUTP.class_sessions.get(instanceUTP.part_classes.get(p).get(0)-1).get(0);
		for (int j = 0; j < instanceUTP.part_classes.get(p).size() ;j++) {
			for(int ji = 0; ji < instanceUTP.class_sessions.get(instanceUTP.part_classes.get(p).get(j)-1).size() ;ji++) {
				tab[count] = x_teacher[instanceUTP.class_sessions.get(instanceUTP.part_classes.get(p).get(j)-1).get(ji)-1];
				count++;
			}
		}
		//IntVar[] tab_res = arg_sort(tab,p);
		int cumul_serv_t = 0;
		for(int i = 0; i < instanceUTP.part_teachers.get(p).size() ;i++) {
			if(instanceUTP.part_teachers.get(p).get(i) < t) {
				cumul_serv_t += instanceUTP.part_teacher_sessions_count[p][instanceUTP.part_teachers.get(p).get(i)-1] ;	
			}
			
		}
		int serv_t = instanceUTP.part_teacher_sessions_count[p][t];
		int offset = cumul_serv_t+minSess-1;
		IntVar[] res_var = new IntVar[serv_t];
		for(int i = 0; i < serv_t ;i++) {
			res_var[i] = model.intVar("x_slot_sorted["+i+"]_"+p+"_t_"+t,0,200000);//part_i_slots(this.instanceUTP.part_slots.get(this.instanceUTP.class_part[this.instanceUTP.session_class[p]-1]-1)));
			//model.element(res_var[i],x_slot2d,model.intVar(0),0,tab_res[i].add(offset).intVar(),0);
			
		}
		model.allDifferent(res_var).post();
		Constraint ags = new constraint_arg_sort(x_teacher,x_slot,res_var,t,serv_t,instanceUTP);
		ags.post();
		//model.addConstructiveDisjunction(ags);
		return res_var;
	}//FinMethod
	
	//======================
	
	public Task[] sessions_of_teacher_disjun_ags_v2(int teacher) {
		int cumul_service = 0;
		for(int pi = 0;pi < instanceUTP.teacher_parts.get(teacher).size();pi++) {
			cumul_service += instanceUTP.part_teacher_sessions_count[instanceUTP.teacher_parts.get(teacher).get(pi)-1][teacher];
		}
		
		Task[] t = new Task[cumul_service];
		int g = 0;
		for(int pi = 0;pi < instanceUTP.teacher_parts.get(teacher).size();pi++) {
			IntVar[] tmp_tab = generate_x_slot_arg_sort(instanceUTP.teacher_parts.get(teacher).get(pi)-1,teacher);
			for(int i = 0 ;i < tmp_tab.length; i++) {
				t[g] = model.taskVar(tmp_tab[i], 80);
				g++;
			}
		}
		
		return t;
	}
	
	
	public void disjunctive_teacher_ags() {
		for(int t = 0; t < this.instanceUTP.nr_teachers ;t++) {
			if(this.instanceUTP.teacher_parts.get(t).size() > 0) {
				//System.out.println("tasks : "+sessions_of_teacher_disjun_v2(t).length);
				//System.out.println("heigh : "+generate_heigth(t).length);
				int cumul_service = 0;
				for(int pi = 0;pi < instanceUTP.teacher_parts.get(t).size();pi++) {
					cumul_service += instanceUTP.part_teacher_sessions_count[instanceUTP.teacher_parts.get(t).get(pi)-1][t];
				}
				//Disjunctive
				model.cumulative(sessions_of_teacher_disjun_ags_v2(t),model.intVarArray(cumul_service, 1,1),model.intVar(1)).post();
			}
		}
	}//FinMethod
	
	//======================
	
	
	public void flatten_constraint() {
		for(int i = 0; i < this.instanceUTP.constraints.size() ;i++) {
			if(this.instanceUTP.constraints.get(i).getIsActivate() == 1) {
				if(this.instanceUTP.constraints.get(i).getConstraint().equals("periodic")) {periodic(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("forbiddenPeriod")) {forbiddenPeriod(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameTeachers")) {sameTeachers(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameRooms")) {sameRooms(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameSlot")) {sameSlot(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameSlots")) {sameSlot(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("allowedPeriod")) {allowedPeriod(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sequenced")) {sequenced(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeek")) {sameWeek(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("assignRoom")) {assignRoom(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeekDay")) {sameWeekDay(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("sameWeeklySlot")) {sameWeeklySlot(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("disjunct")) {disjunct(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("differentWeekDay")) {differentWeekDay(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("weekly")) {weekly(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("differentWeek")) {differentWeek(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("differentSlots")) {differentSlots(this.instanceUTP.constraints.get(i));}
				else if(this.instanceUTP.constraints.get(i).getConstraint().equals("forbiddenRooms")) {forbiddenRooms(this.instanceUTP.constraints.get(i));}
				else System.out.println("Constraint "+this.instanceUTP.constraints.get(i).getConstraint()+" is not implemented"+" provide from rule : "+this.instanceUTP.constraints.get(i).getRule());
			}
			if(i == 0) {}

		}
		
	}//FinMethod
	
	public static String getFilename_solution() {
		return filename_solution;
	}//FinMethod

	public static void setFilename_solution(String filename_solution) {
		ModelUTP.filename_solution = filename_solution;
	}//FinMethod

	
	public void write_solution_file(String out,boolean isXml,String filename) {
		if(!isXml) {
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
		}
		else {
			File or1 = new File(filename);
			File or2 = new File(filename_solution);
			try {
				Files.copy(or1.toPath(), or2.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			RandomAccessFile f;
			try {
				f = new RandomAccessFile(filename_solution, "rw");
				long length = f.length() - 20;
				byte b = 0;
				do {                     
				  length -= 1;
				  try {
				  f.seek(length);
					b = f.readByte();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				} while(b != 10 && length > 0);
				try {
					f.setLength(length+1);
					f.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			FileWriter FWriter;
			BufferedWriter writer;
			//out ="";
			out += "\n</solution>\n</timetabling>";
			try {
				FWriter = new FileWriter(filename_solution,true);
				writer = new BufferedWriter(FWriter);
						
				writer.write(out);			    
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public int[] tab_REPAS_course() {
		Vector<Integer> CC = new Vector<Integer>();
		for(int i = 0; i < this.instanceUTP.nr_sessions  ;i++) {
			for(int j = 0; j < this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).size() ;j++) {
				if(this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("REPAS") || this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("REPAS") || this.instanceUTP.label_name[this.instanceUTP.part_label.get(this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1).get(j)-1].equals("REPAS")) {
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
		int[] tab2 = tab_REPAS_course();
		for(int i = 0; i < this.instanceUTP.nr_sessions  ;i++) {
			if(session_not_in(i+1,tab) && session_not_in(i+1,tab2)) {
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
			//System.out.println(" tt "+this.instanceUTP.class_name[this.instanceUTP.session_class[tt[i].cpt-1]-1]);
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
			//System.out.println("Element : "+  this.instanceUTP.class_name[this.instanceUTP.session_class[tt[i].cpt-1]-1] +" rank : "+this.instanceUTP.session_rank[tt[i].cpt-1]);
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
	
	public IntVar[] sort_xslot4() {
		int[] CC = tab_REPAS_course();
		IntVar[] tab = new IntVar[CC.length];
		for(int i = 0; i < CC.length;i++) {
			tab[i] =  x_slot[CC[i]-1];
		}
		
		//System.out.println("size slot4 :  "+CC.length);
		return tab;
	}//FinMethod
	
	public void setStrategie(StrategieBuilt sb) {
		this.strategie = sb;
		this.strategie_choice = 3;
	}//FinMethod
	
//===========================
	public boolean inIntTab(int value,Vector<Integer> tab) {
		for(int i = 0;i<tab.size();i++) {
			if(tab.get(i) == value) {
				return true;
			}
		}
		return false;
	}//FinMethod
	
	public IntVar[] convertVecToTab(Vector<IntVar> tab) {
		IntVar[] res = new IntVar[tab.size()];
		for(int i =0; i<tab.size() ;i++) {
			res[i] = tab.get(i);
		}
		return res;
	}//FinMethod
//=============  Label ==============
	public boolean labelVectorExist(int session,Vector<String> forbidden) {
		int part = this.instanceUTP.class_part[this.instanceUTP.session_class[session]-1];
		for(int pl = 0; pl < this.instanceUTP.part_label.get(part-1).size() ;pl++) {
			for(int lnum = 0; lnum < forbidden.size() ; lnum++) {
				if(this.instanceUTP.label_name[this.instanceUTP.part_label.get(part-1).get(pl)-1].equals(forbidden.get(lnum))) {
					return true;
				}
			}	 
		}
		return false;
	}//FinMethod

	public boolean labelVectorExistClass(int classe,Vector<String> forbidden) {
		int part = this.instanceUTP.class_part[classe]-1;
		for(int pl = 0; pl < this.instanceUTP.part_label.get(part).size() ;pl++) {
			for(int lnum = 0; lnum < forbidden.size() ; lnum++) {
				if(this.instanceUTP.label_name[this.instanceUTP.part_label.get(part).get(pl)-1].equals(forbidden.get(lnum))) {
					return true;
				}
			}
		}
		return false;
	}//FinMethod
	
//=============  Label ==============	
	public void createFunctionAndVerificationStrategie(ElementStrategie s,Vector<String> labelTab,int[] rankTab) {
		if (s.filterScope.equals("label")) {
			String[] t = s.filter.split(",");
			for(int ti = 0 ; ti < t.length ;ti++) {
				labelTab.add(t[ti]);
			}
		}
		else if(s.filterScope.equals("rank")) {
			for(int ii = 0; ii < s.ranks.size() ;ii++) {
				rankTab[s.ranks.get(ii)-1] = 0;//
			}
		}
		else {
			System.out.println("Strategie don't exist\n");
		}
	}//FinMethod
	
//======== SLOT ==========
	public IntVar[] getSlotStrategieRank(Vector<String> forbiddenLabel,Vector<Integer> rank) {
		Vector<IntVar> vec = new Vector<IntVar>();
		for(int i = 1; i <= this.instanceUTP.nr_sessions ;i++) {
			
			boolean ff1 = inIntTab(this.instanceUTP.session_rank[i-1],rank);
			boolean ff2 = !labelVectorExist(i-1,forbiddenLabel);
			boolean ff = ff1 && ff2;
					
			if(ff) {
				//vec.add(this.y_slot[i-1]);
				vec.add(this.x_slot[i-1]);
				//vec.add(this.t[i-1]);
				
				
				//System.out.println("-> session "+(i-1)+" class "+ this.instanceUTP.class_name[this.instanceUTP.session_class[i-1]-1]+" rank "+this.instanceUTP.session_rank[i-1]+" b:"+ff+" b1:"+ff1+" b2:"+ff2);
			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
	public IntVar[] getSlotStrategieLabel(String[] label1) {
		Vector<IntVar> vec = new Vector<IntVar>();
		Vector<String> label = new Vector<String>(label1.length);
		for(int i = 0;i< label1.length;i++) {
			label.add(i, label1[i]);
		}
		for(int i = 1; i <= this.instanceUTP.nr_sessions ;i++) {
			if(labelVectorExist(i-1,label)) {
				//vec.add(this.y_slot[i-1]);
				vec.add(this.x_slot[i-1]);
				//vec.add(this.t[i-1]);
				
				//System.out.println("-> session "+(i-1)+" class "+ this.instanceUTP.class_name[this.instanceUTP.session_class[i-1]-1]+" rank "+this.instanceUTP.session_rank[i-1]);

			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
//======== Room ==========
	
	public IntVar[] getRoomStrategieLabel(String[] label1) {
		Vector<IntVar> vec = new Vector<IntVar>();
		Vector<String> label = new Vector<String>(label1.length);
		for(int i = 0;i< label1.length;i++) {
			label.add(i, label1[i]);
		}
		for(int i = 1; i <= this.instanceUTP.nr_classes ;i++) {
			if(labelVectorExistClass(i-1,label)) {
				int part = this.instanceUTP.class_part[i-1];
				if(this.instanceUTP.part_room_use[part-1].equals("single")) {
					vec.add(this.x_room[i-1]);
				}
				else {
					//vec.add(this.x_room[i-1]);
					for(int h = 0; h < this.instanceUTP.part_room_worst_case[part-1] ;h++) {
						vec.add(this.x_rooms[this.instanceUTP.class_position_multiple_room[i-1]+h]);
					}
				}
			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
	public IntVar[] getRoomStrategieLabelForbidden(Vector<String> forbiddenLabel,boolean is_x_room) {
		
		Vector<IntVar> vec = new Vector<IntVar>();
		for(int i = 1; i <= this.instanceUTP.nr_classes ;i++) {
			if(!labelVectorExistClass(i-1,forbiddenLabel)) {
				int part = this.instanceUTP.class_part[i-1];
				if(is_x_room){
					if(this.instanceUTP.part_room_use[part-1].equals("single")) {
						vec.add(this.x_room[i-1]);
					}
					else {
						vec.add(this.x_room[i-1]);
					}
				}
				else {
					if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
						//vec.add(this.x_room[i-1]);
						for(int h = 0; h < this.instanceUTP.part_room_worst_case[part-1] ;h++) {
							vec.add(this.x_rooms[this.instanceUTP.class_position_multiple_room[i-1]+h]);
						}
				}
				
				


				}
				
			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
	public IntVar[] getRoomStrategieLabelForbiddenAll(Vector<String> forbiddenLabel) {
		
		Vector<IntVar> vec = new Vector<IntVar>();
		for(int i = 1; i <= this.instanceUTP.nr_classes ;i++) {
			if(!labelVectorExistClass(i-1,forbiddenLabel)) {
				int part = this.instanceUTP.class_part[i-1];
					if(this.instanceUTP.part_room_use[part-1].equals("single")) {
						vec.add(this.x_room[i-1]);
					}
					else {
						for(int h = 0; h < this.instanceUTP.part_room_worst_case[part-1] ;h++) {
							vec.add(this.x_rooms[this.instanceUTP.class_position_multiple_room[i-1]+h]);
						}
					}

			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
//======== Teacher ==========
	
	public IntVar[] getTeacherStrategieLabel(String[] label1) {
		Vector<IntVar> vec = new Vector<IntVar>();
		Vector<String> label = new Vector<String>(label1.length);
		for(int i = 0;i< label1.length;i++) {
			label.add(i, label1[i]);
		}
		for(int i = 1; i <= this.instanceUTP.nr_sessions ;i++) {
			int cl =  this.instanceUTP.session_class[i-1]-1;
			if(labelVectorExistClass(cl,label)) {
				vec.add(this.x_teacher[i-1]);
			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
	public IntVar[] getTeacherStrategieLabelForbidden(Vector<String> forbiddenLabel) {
		
		Vector<IntVar> vec = new Vector<IntVar>();
		for(int i = 1; i <= this.instanceUTP.nr_sessions ;i++) {
			int cl =  this.instanceUTP.session_class[i-1]-1;
			if(!labelVectorExistClass(cl,forbiddenLabel)) {
				int part = this.instanceUTP.class_part[cl];
				if(this.instanceUTP.part_session_teacher_count[part-1] == 1) {
					vec.add(this.x_teacher[i-1]);
				}
				else {
					//vec.add(this.x_teacher[i-1]);
					for(int h = 0; h < this.instanceUTP.part_session_teacher_count[part-1] ;h++) {
						vec.add(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl]+h]);
					}
				}
			}
		}
		return convertVecToTab(vec);
	}//FinMethod
	
//============= VarOrdering & ValueOrdering ============
	
	public  IntValueSelector valueOrdering(String vo,IntVar[] t) {
		if(vo.equals("indomain_min")) {
			return new IntDomainMin();
		}
		else if(vo.equals("indomain_max")) {
			return new IntDomainMax();
		}
		else if(vo.equals("indomain_middle")) {
			return  new IntDomainMiddle(true);
		}
		else if(vo.equals("equilibrate_room_disposition")) {
			return  new EquilibrateRoomDisposition(t,this.instanceUTP);
		}
		else if (vo.equals("indomain_random")) {
			return new IntDomainRandom((long) ran);
		}
		else {
			return new IntDomainMin();
		}
	}//FinMethod
	
	public  VariableSelector<IntVar>  varOrdering(String vo,IntVar[] t) {
		if(vo.equals("anti_first_fail")) {
			return  new AntiFirstFail(model);
		}
		else if(vo.equals("first_fail")) {
			return new FirstFail(model);
		}
		else if(vo.equals("input_order")) {
			return  new InputOrder<IntVar>(model);
		}
		else if(vo.equals("largest")) {
			return  new Largest();
		}
		else if(vo.equals("smallest")) {
			return  new Smallest();
		}
		else if (vo.equals("dom_over_wdeg")) {
			return new  DomOverWDeg<IntVar>(t,(long) this.ran);
		}
		else if (vo.equals("random")) {
			return new  Random<IntVar>((long) ran);
		}
		else {
			System.out.println("ERROR");
			return new InputOrder<IntVar>(model);
		}
	}//FinMethod
	
//===========================
	public void search_strategie_set() {
		this.ran = Math.random();
		System.out.println("Strategie choice : "+"random_seed : "+this.ran);
		this.solver.limitTime( this.strategie.timeout);
		Vector<String> label_x_rooms = new Vector<String>();
		Vector<String> label_x_teachers = new Vector<String>();
		Vector<String> label_x_slot =  new Vector<String>();
		Vector<String> label_x_teacher = new Vector<String>();
		Vector<String> label_x_room =  new Vector<String>();
		
		int[] rank_x_rooms = new int[this.instanceUTP.max_part_sessions];
		int[] rank_x_teachers = new int[this.instanceUTP.max_part_sessions];
		int[] rank_x_room = new int[this.instanceUTP.max_part_sessions];
		int[] rank_x_teacher = new int[this.instanceUTP.max_part_sessions];
		int[] rank_x_slot =  new int[this.instanceUTP.max_part_sessions];
		
		for(int i = 0; i < this.instanceUTP.max_part_sessions ;i++) {
			rank_x_slot[i] = 1;//rank_x_slot[i] =i+1;
			rank_x_teachers[i] = 1;//rank_x_teachers[i] =i+1;
			rank_x_rooms[i] = 1;//rank_x_rooms[i] =i+1;
			rank_x_teacher[i] = 1;//rank_x_teacher[i] =i+1;
			rank_x_room[i] = 1;//rank_x_room[i] =i+1;
		}
		
		for(int i = 0; i < this.strategie.strategies.length ;i++) {
			if(this.strategie.strategies[i].var.equals("x_slot")) {
				createFunctionAndVerificationStrategie(this.strategie.strategies[i],label_x_slot,rank_x_slot);
			}
			else if(this.strategie.strategies[i].var.equals("x_rooms")){
				createFunctionAndVerificationStrategie(this.strategie.strategies[i],label_x_rooms,rank_x_rooms);
			}
			else if(this.strategie.strategies[i].var.equals("x_teachers")) {
				createFunctionAndVerificationStrategie(this.strategie.strategies[i],label_x_teachers,rank_x_teachers);
			}
			else if(this.strategie.strategies[i].var.equals("x_room")){
				createFunctionAndVerificationStrategie(this.strategie.strategies[i],label_x_room,rank_x_room);
			}
			else if(this.strategie.strategies[i].var.equals("x_teacher")) {
				createFunctionAndVerificationStrategie(this.strategie.strategies[i],label_x_teacher,rank_x_teacher);
			}
		}
		
		
		
		IntVar[] vars = new IntVar[0];
		IntVar[] vars2 = new IntVar[0];
		IntStrategy[] tst = new IntStrategy[this.strategie.strategies.length];
		int uu = 0;
		for(int i =0 ; i < this.strategie.strategies.length  ;i++) {
			System.out.println(this.strategie.strategies[i].var+" "+this.strategie.strategies[i].filterScope+" , "+this.strategie.strategies[i].filter);
			//x_slot
			if(this.strategie.strategies[i].var.equals("x_slot")) {			
				
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getSlotStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					System.out.println(this.strategie.strategies[i].toStringRanks());
					vars = getSlotStrategieRank(label_x_slot,this.strategie.strategies[i].ranks);
					
				}
				
			}
			//x_room
			else if(this.strategie.strategies[i].var.equals("x_room")){
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getRoomStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					vars = getRoomStrategieLabelForbidden(label_x_room,true);
				}
			}
			//x_teacher
			else if(this.strategie.strategies[i].var.equals("x_teacher")) {
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getTeacherStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					vars = getTeacherStrategieLabelForbidden(label_x_teacher);
				}
			}
			//x_rooms
			else if(this.strategie.strategies[i].var.equals("x_rooms")){
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getRoomStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					vars = getRoomStrategieLabelForbidden(label_x_rooms,false);
				}
			}
			//x_teachers
			else if(this.strategie.strategies[i].var.equals("x_teachers")) {
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getTeacherStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					vars = getTeacherStrategieLabelForbidden(label_x_teachers);
				}
			}
			//x_rooms/x_room
			else if(this.strategie.strategies[i].var.equals("x_room*")){
				if(this.strategie.strategies[i].filterScope.equals("label")) {
					vars = getRoomStrategieLabel(this.strategie.strategies[i].filter.split(","));
				}
				else if(this.strategie.strategies[i].filterScope.equals("rank")) {
					vars = getRoomStrategieLabelForbiddenAll(label_x_room);
				}
			}
			
			IntStrategy t = new IntStrategy(vars,varOrdering(this.strategie.strategies[i].varOrdering,vars),valueOrdering(this.strategie.strategies[i].valueOrdering,vars));
			tst[i] = t;
		}
		this.solver.setSearch(tst);
		
	}//FinMethod
	
	public IntVar[] all_x(){
		IntVar[] tab_res = new IntVar[x_slot.length+x_rooms.length+ x_room.length+x_teacher.length+x_teachers.length];
		int count = 0;
		for(int i=0;i < x_slot.length ;i++) {
			tab_res[count] = x_slot[i];
			count++;
		}
		for(int i=0;i < x_rooms.length ;i++) {
			tab_res[count] = x_rooms[i];
			count++;
		}
		for(int i=0;i < x_room.length ;i++) {
			tab_res[count] = x_room[i];
			count++;
		}
		for(int i=0;i < x_teacher.length ;i++) {
			tab_res[count] = x_teacher[i];
			count++;
		}
		for(int i=0;i < x_teachers.length ;i++) {
			tab_res[count] = x_teachers[i];
			count++;
		}
		
		return tab_res;
	}
	
	public IntVar[] somme_variable_tab(IntVar[]...tabs) {
		
		int ksum = 0;
		for(IntVar[] tab:tabs) {
			ksum += tab.length;
		}
		IntVar[] tab_res = new IntVar[ksum];
		int c = 0;
		for(IntVar[] tab:tabs) {
			for(IntVar t:tab) {
				tab_res[c] = t;
				c++;
			}
		}
		return tab_res;
	}//FinMethod
	
	public void search_strategie_min_min() {
		double ran1 = Math.random();
		double ran2 = Math.random();
		System.out.println("str chelou : ran1 ="+ran1+" ran2 ="+ran2);
		
		this.solver.setSearch(
				Search.intVarSearch(
						//new Smallest(),
						new Random<IntVar>((long) ran1),
						//new FirstFail(model),
		                //new IntDomainRandom((long) ran2),
		                new EquilibrateRoomDisposition(x_room,this.instanceUTP),
		                x_room
						),
				/*Search.intVarSearch(
						new Smallest(),
						//new Random<IntVar>((long) ran2),
						//new FirstFail(model),
		                //new IntDomainRandom((long) ran),
		                //new EquilibrateRoomDisposition(x_room,this.instanceUTP),
						new IntDomainMin(),
		                x_teacher
						),*/
				//Search.activityBasedSearch(x_room),
				//Search.activityBasedSearch(all_x()));
				Search.intVarSearch(
						//new Smallest(),
						new FirstFail(model),
						//new Random<IntVar>((long) ran2),
		                new IntDomainMin(),
		                //new decisionWeekEquilibrate(t, sort_xslot1(),this.x_slot,this.instanceUTP),
		                all_x()
						)
				);
	}//FinMethod
	
	public void strategie_choice() {
		this.strategie_choice = 4;
		switch(this.strategie_choice) {
		case 0: search_strategie();break;
		case 1: search_strategie_2();break;
		case 2: search_strategie_3();break;
		case 3: search_strategie_set();break;
		case 4: search_strategie_min_min();break;
		default :System.out.println("Strategie not referenced");
		}
	}//FinMethod
	
	/*public IntVar[] get_session_per_rank() {
		
	}//FinMethod*/
	
	public void search_strategie_3() {
		double ran = Math.random();
		Integer t = 1;
		System.out.println("random_seed : "+ran);
		this.solver.setSearch(
				Search.intVarSearch(
		                new FirstFail(model),
		                new IntDomainMin(),
		                //new decisionWeekEquilibrate(t, sort_xslot1(),this.x_slot,this.instanceUTP),
		                sort_xslot1()
						),
				Search.intVarSearch(
                //new FirstFail(model),
                new AntiFirstFail(model),
                new EquilibrateRoomDisposition(x_room,this.instanceUTP),
                //new IntDomainRandom((long) ran),
                x_room
				),
		Search.intVarSearch(
				
				new InputOrder<IntVar>(model),
				//new AntiFirstFail(model),
				new IntDomainMin(),
				//new IntDomainRandom((long) ran),
				x_rooms
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMin(),
                x_teacher
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMin(),
                x_teachers
				),

		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMiddle(true),
                sort_xslot2()
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMax(),
                sort_xslot3()
				));

	}//FinMethod
	
	
	public void search_strategie_2() {
		double ran = Math.random();
		//Integer t = 1;
		System.out.println("Stratégie 2 : random_seed : "+ran);
		//IntStrategy st =new IntStrategy(x_room,new FirstFail(model),new EquilibrateRoomDisposition(x_room,this.instanceUTP,this.instanceUTP.nr_rooms));
		this.solver.setSearch(
				Search.intVarSearch(
               	new FirstFail(model),
				//new Smallest(),
                //new AntiFirstFail(model),
                //new EquilibrateRoomDisposition(x_room,this.instanceUTP,this.instanceUTP.nr_rooms),
                new IntDomainRandom((long) ran),
                x_room
				),
			
		Search.intVarSearch(
				
				new InputOrder<IntVar>(model),
				//new AntiFirstFail(model),
				new IntDomainMin(),
				//new IntDomainRandom((long) ran),
				x_rooms
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMin(),
                x_teacher
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMin(),
                x_teachers
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMax(),
                //new IntDomainRandom((long) ran),
                //new decisionWeekEquilibrate(1, sort_xslot1(),this.x_slot,this.instanceUTP),
                sort_xslot4()
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMin(),
                //new IntDomainRandom((long) ran),
                //new decisionWeekEquilibrate(1, sort_xslot1(),this.x_slot,this.instanceUTP),
                sort_xslot1()
				),

		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMiddle(true),
                //new IntDomainMin(),
                sort_xslot2()
				),
		Search.intVarSearch(
                new FirstFail(model),
                new IntDomainMax(),
                sort_xslot3()
				));

	}//FinMethod
	
	public void search_strategie() {
		double ran = Math.random();
		System.out.println(ran);
		this.solver.setSearch(Search.intVarSearch(
                // selects the variable of smallest domain size
                //new FirstFail(model),
                new AntiFirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainRandom((long) ran),
                //new IntDomainMin(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
                x_room
				),
			//	Search.setVarSearch(null, null, false, x_rooms)
		Search.intVarSearch(
				
				//new FirstFail(model),
				new InputOrder<IntVar>(model),
				//true,
				new IntDomainMin(),
				//new IntDomainRandom((long) ran),
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
		Search.intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // apply equality (var = val)
                //DecisionOperator.int_eq,
                // variables to branch on
                //x, y
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
		//this.solver.limitTime("20s");
		//search_strategie();
		strategie_choice();
		this.solver.showContradiction();
		//this.solver.verboseSolving(1);
		//this.solver.showStatisticsDuringResolution(1000);
		//this.solver.setNoLearning();//OFF
		
		//this.solver.showDecisions();
		//this.solver.showDashboard();
		//this.solver.isLearnOff();//OFF
		//this.solver.showShortStatistics();
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
			for(int j = 0; j < this.instanceUTP.part_session_teacher_count[part-1];j++) {
				if(j < this.instanceUTP.part_session_teacher_count[part-1]-1 ) {
					out += "\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl-1]+j])-1]+"\",";
				}
				else {
					out += "\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl-1]+j])-1]+"\"";
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
			//int[] cl_j = this.solution.getSetVal(this.x_rooms[this.instanceUTP.class_position_multiple_room[cl-1]]);
			for(int i = 0; i < this.instanceUTP.part_room_worst_case[part-1] ;i++) { 
				int cl_p = this.instanceUTP.class_position_multiple_room[cl-1];
				int t_room = this.solution.getIntVal(x_rooms[i+cl_p]);
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
	
	//====================================
	
	public int[] slot2Time(int slot) {
		String[] val = new String[]{"slot", "week", "day", "dailyslot"};
		int[] tab = new int[val.length];
		tab[0] = slot;		
		int week = (int)slot / (this.instanceUTP.nr_slots_per_day*this.instanceUTP.nr_days_per_week);
		int slot2 = slot - (week * (this.instanceUTP.nr_slots_per_day*this.instanceUTP.nr_days_per_week));
		int day = (int) slot2 / this.instanceUTP.nr_slots_per_day;
		int dailySlot = slot2 - (day * this.instanceUTP.nr_slots_per_day);
		tab[1] = week+1;
		tab[2] = day+1;
		tab[3] = dailySlot;
		return tab;
	}
	public String print_xml() {
		if(this.solution == null) {
			return "<!-- UNSAT ->";
		}
		int u = instanceUTP.nr_days_per_week*instanceUTP.nr_weeks;
		int[] day_charge = new int[u];
		int[] week_charge = new int [instanceUTP.nr_weeks];
		for(int i = 0;  i <  instanceUTP.nr_weeks ;i++) {
			week_charge[i] = 0;
		}
		for(int i = 0;  i <  u ;i++) {
			day_charge[i] = 0;
		}
		getStatistics();//show statitics
		getStatistics_rooms();//show rooms statistics
		String out = "<classes>\n";
		for(int i = 0; i<this.instanceUTP.nr_classes;i++) {
			out += "<class refId = \""+this.instanceUTP.class_name[i]+"\">\n";
				out+= "    <groups>\n"+print_xml_group_solution(this.instanceUTP.class_sessions.get(i).get(0)-1)+"    </groups>\n";
				out+= "    <teachers>\n"+print_xml_teacher_solution(this.instanceUTP.class_sessions.get(i).get(0)-1)+"    </teachers>\n";
				out+= "    <rooms>\n"+print_xml_room_solution(this.instanceUTP.class_sessions.get(i).get(0)-1)+"    </rooms>\n";
			out += "</class>\n";
		}
		out +="</classes>\n";
		out += "<sessions>\n";
		
		for(int i = 0; i<this.instanceUTP.nr_sessions;i++) {
			int[] slot2time = slot2Time(this.solution.getIntVal(x_slot[i]));
			out +=  "<session rank=\""+(this.instanceUTP.session_rank[i]-1)+"\" class=\""+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]+"\">\n";
			out += "    <startingSlot dailySlot=\""+slot2time[3]+"\" day =\""+slot2time[2]+"\" week =\""+slot2time[1]+"\" />\n";
			out += "    <rooms>\n"+print_xml_room_solution(i)+"    </rooms>\n";
			out += "    <teachers>\n"+print_xml_teacher_solution(i)+"    </teachers>\n";
			out += "</session>\n";
			//out +=  x_s[i]+" ";
			//System.out.println("i = "+i+" xw "+ (slot2time[1]-1));
			week_charge[slot2time[1]-1]++;
			day_charge[slot2time[2]+(instanceUTP.nr_days_per_week*(slot2time[1]-1))-1]++;
		}
		out +="</sessions>\n";
		int cunt = 0;
		String stat_eq = "";
		String stat_eq_csv = instanceUTP.nr_weeks+";"+instanceUTP.nr_days_per_week+";"+u+"\n";
		for(int i = 0; i < instanceUTP.nr_weeks ;i++) {
			stat_eq+= "week "+(i+1)+" : "+week_charge[i]+"\n";
			stat_eq_csv+= "w"+";"+(i+1)+";"+week_charge[i]+"\n";
			for(int j = 0; j < instanceUTP.nr_days_per_week ; j++){
				stat_eq+= "   -day "+(cunt+1)+" : "+day_charge[cunt]+"\n";
				stat_eq_csv+= "d"+";"+(cunt+1)+";"+day_charge[cunt]+"\n";
				cunt++;
			}
		}
		FileOutputStream outputStream = null;
		FileOutputStream outputStream2 = null;
		String file_log_stat_eq = ModelUTP.filename_solution.substring(0,filename_solution.length()-4)+".txt";
		@SuppressWarnings("static-access")
		String file_log_stat_eq_csv = this.filename_solution.substring(0,filename_solution.length()-4)+".csv";
		try {
			outputStream = new FileOutputStream(file_log_stat_eq);
			outputStream2 = new FileOutputStream(file_log_stat_eq_csv);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] strToBytes = stat_eq.getBytes();
	    byte[] strToBytes2 = stat_eq_csv.getBytes();
	    try {
			outputStream.write(strToBytes);
			outputStream.close();
			outputStream2.write(strToBytes2);
			outputStream2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}//FinMethod
	
	
	public String print_xml2() {
		if(this.solution == null) {
			return "<!-- UNSAT ->";
		}
		getStatistics();
		int u = instanceUTP.nr_days_per_week*instanceUTP.nr_weeks;
		int[] day_charge = new int[u];
		int[] week_charge = new int [instanceUTP.nr_weeks];
		for(int i = 0;  i <  instanceUTP.nr_weeks ;i++) {
			week_charge[i] = 0;
		}
		for(int i = 0;  i <  u ;i++) {
			day_charge[i] = 0;
		}
		String out ="";
		out += "<sessions>\n";
		for(int i = 0; i<this.instanceUTP.nr_sessions;i++) {
			int[] slot2time = slot2Time(this.solution.getIntVal(x_slot[i]));
			out +=  "<session rank=\""+(this.instanceUTP.session_rank[i]-1)+"\" class=\""+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]+"\">\n";
			out += "    <startingSlot dailySlot=\""+slot2time[3]+"\" day =\""+slot2time[2]+"\" week =\""+slot2time[1]+"\" />\n";
			if(!print_xml_room_solution(i).equals("        <room refId =\"vide\" />\n")) {
				out += "    <rooms>\n"+print_xml_room_solution(i)+"    </rooms>\n";
			}
			if(!print_xml_teacher_solution(i).equals("        <teacher refId=\"vide\" />\n")) {
				out += "    <teachers>\n"+print_xml_teacher_solution(i)+"    </teachers>\n";
			}
			out += "</session>\n";
			week_charge[slot2time[1]-1]++;
			day_charge[slot2time[2]+(instanceUTP.nr_days_per_week*(slot2time[1]-1))-1]++;
			//out +=  x_s[i]+" ";
		}
		out +="</sessions>\n";
		int cunt = 0;
		String stat_eq = "";
		String stat_eq_csv = instanceUTP.nr_weeks+";"+instanceUTP.nr_days_per_week+";"+u+"\n";
		for(int i = 0; i < instanceUTP.nr_weeks ;i++) {
			stat_eq+= "week "+(i+1)+" : "+week_charge[i]+"\n";
			stat_eq_csv+= "w"+";"+(i+1)+";"+week_charge[i]+"\n";
			for(int j = 0; j < instanceUTP.nr_days_per_week ; j++){
				stat_eq+= "   -day "+(cunt+1)+" : "+day_charge[cunt]+"\n";
				stat_eq_csv+= "d"+";"+(cunt+1)+";"+day_charge[cunt]+"\n";
				cunt++;
			}
		}
		FileOutputStream outputStream = null;
		FileOutputStream outputStream2 = null;
		String file_log_stat_eq = ModelUTP.filename_solution.substring(0,filename_solution.length()-4)+".txt";
		@SuppressWarnings("static-access")
		String file_log_stat_eq_csv = this.filename_solution.substring(0,filename_solution.length()-4)+".csv";
		try {
			outputStream = new FileOutputStream(file_log_stat_eq);
			outputStream2 = new FileOutputStream(file_log_stat_eq_csv);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] strToBytes = stat_eq.getBytes();
	    byte[] strToBytes2 = stat_eq_csv.getBytes();
	    try {
			outputStream.write(strToBytes);
			outputStream.close();
			outputStream2.write(strToBytes2);
			outputStream2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}//FinMethod
	
	public String print_xml_group_solution(int session) {
		String out = "";
		Vector<Integer> groups = this.instanceUTP.class_groups.get(this.instanceUTP.session_class[session]-1);
		for(int g = 0; g < groups.size() ;g++) {
			if(g < groups.size()-1) {out += "        <group refId=\""+this.instanceUTP.group_name[groups.get(g)-1]+"\"/>\n";}
			else {out += "        <group refId=\""+this.instanceUTP.group_name[groups.get(g)-1]+"\"/>\n";}
		}
		return out;
	}//FinMethod
	
	public String print_xml_teacher_solution(int session) {
		String out = "";
		int cl = this.instanceUTP.session_class[session];
		int part = this.instanceUTP.class_part[cl-1];
		if(this.instanceUTP.part_session_teacher_count[part-1] > 1) {
			for(int j = 0; j < this.instanceUTP.part_session_teacher_count[part-1];j++) {
				if(j < this.instanceUTP.part_session_teacher_count[part-1]-1 ) {
					out += "        <teacher refId=\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl-1]+j])-1]+"\"/>\n";
				}
				else {
					out += "        <teacher refId=\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teachers[this.instanceUTP.class_position_multiple_teacher[cl-1]+j])-1]+"\"/>\n";
				}
			}
		}
		else {
			if(this.instanceUTP.part_teachers.get(part-1).size() != 0) {
				out += "        <teacher refId=\""+this.instanceUTP.teacher_name[this.solution.getIntVal(this.x_teacher[session])-1]+"\" />\n";
			}
			
		}
		if(out.equals("")) {
			out = "        <teacher refId=\"vide\" />\n";
		}
		return out;
	}//FinMethod
	//public int room_multiple_total;
	public String print_xml_room_solution(int session) {
		String out = "";
		int cl = this.instanceUTP.session_class[session];
		int part = this.instanceUTP.class_part[cl-1];
		if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
			//int[] cl_j = this.solution.getSetVal(this.x_rooms[this.instanceUTP.class_position_multiple_room[cl-1]]);
			for(int i = 0; i < this.instanceUTP.part_room_worst_case[part-1] ;i++) { 
				int cl_p = this.instanceUTP.class_position_multiple_room[cl-1];
				int t_room = this.solution.getIntVal(x_rooms[i+cl_p]);
				if(t_room > 0) {
					if(i < this.instanceUTP.part_room_worst_case[part-1]-1 ) {
						
						out += "        <room refId = \""+this.instanceUTP.room_name[t_room]+"\" />\n";
					}
					else {
						out += "        <room refId = \""+this.instanceUTP.room_name[t_room]+"\" />\n";
					}
				}

				
				//this.room_multiple_total++;
			}
			if(out.length() > 0) {
				//out = out.substring(0,out.length()-1);
			}

		}
		else {
			out += "        <room refId =\""+this.instanceUTP.room_name[this.solution.getIntVal(this.x_room[cl-1])]+"\" />\n";
		}
		if(out.equals("")) {
			out = "        <room refId=\"Amphi-B-EVAL\" />\n";
		}
		return out;
	}//FinMethod
	//===================================
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
	
	public String print_json() {
		if(this.solution == null) {
			return "{ \"solution\" : \"UNSAT\" }";
		}
		String out = "\"sessions\" : ";
		for(int i = 0; i<this.instanceUTP.nr_sessions;i++) {
			int[] slot2time = slot2Time(this.solution.getIntVal(x_slot[i]));
			out +=  "<session rank=\""+(this.instanceUTP.session_rank[i]-1)+"\" class=\""+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]+"\">\n";
			out += "    <startingSlot dailySlot=\""+slot2time[3]+"\" day =\""+slot2time[2]+"\" week =\""+slot2time[1]+"\" />\n";
			out += "    <rooms>\n"+print_xml_room_solution(i)+"    </rooms>\n";
			out += "    <teachers>\n"+print_xml_teacher_solution(i)+"    </teachers>\n";
			out += "</session>\n";
			//out +=  x_s[i]+" ";
		}
		out +="</sessions>\n";
		
		return out;
	}//FinMethod
	
	public void  getStatistics() {
		DecimalFormat df = new DecimalFormat("0.000");
		String out_statistics = "";
		out_statistics += "getSeed​;"+model.getSeed()+"\n";
		out_statistics += "getNbCstrs;​"+model.getNbCstrs()+"\n";
		out_statistics += "getCreationTime;​"+df.format(solver.getReadingTimeCount())+"\n";
		out_statistics += "getNbIntVar​;"+model.getNbIntVar(false)+"\n";
		out_statistics += "getNbBoolVar;​"+model.getNbBoolVar()+"\n";
		out_statistics += "getNbRealVar​;"+model.getNbRealVar()+"\n";
		out_statistics += "getSetVar​;"+model.getNbSetVar()+"\n";
		out_statistics += "getNbVar​s;"+model.getNbVars()+"\n";
		//Solver stats
		out_statistics += "getResolutionTime;"+df.format(solver.getTimeCount())+"\n";
		out_statistics += "getNrSolutions;1"+"\n";
		out_statistics += "getNodeCount;"+solver.getNodeCount()+"\n";
		out_statistics += "getFailCount;"+solver.getFailCount()+"\n";
		out_statistics += "getBackTrackCount​;"+solver.getBackTrackCount()+"\n";
		out_statistics += "getBackjumpCount;"+solver.getBackjumpCount()+"\n";
		out_statistics += "getCurrentDepth;"+solver.getCurrentDepth()+"\n";
		out_statistics += "getDecisionCount;"+solver.getDecisionCount()+"\n";
		out_statistics += "getRestartCount;"+solver.getRestartCount()+"\n";
		FileOutputStream outputStream = null;
		String file_write_statistics = ModelUTP.filename_solution.substring(0,filename_solution.length()-4)+"-statistics_solver.csv";
		try {
			outputStream = new FileOutputStream(file_write_statistics);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] strToBytes = out_statistics.getBytes();
	    try {
			outputStream.write(strToBytes);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(out_statistics);
		
		
	}//FinMethod
	
	public void getStatistics_rooms() {
		int[] room_use = new int[instanceUTP.nr_rooms+1];
		Arrays.fill(room_use, 0);
		for(int i  = 0; i < instanceUTP.nr_sessions ;i++) {
			int part = instanceUTP.class_part[instanceUTP.session_class[i]-1];
			int cl = instanceUTP.session_class[i];
			
			if(this.instanceUTP.part_room_use[part-1].equals("multiple")) {
				
				for(int j = 0; j < this.instanceUTP.part_room_worst_case[part-1] ;j++) { 
					
					int cl_p = this.instanceUTP.class_position_multiple_room[cl-1];
					
					int t_room = this.solution.getIntVal(x_rooms[j+cl_p]);
					
					//if(t_room > 0) {
						room_use[t_room] += instanceUTP.part_session_length[part-1];
					//}

				}
			}
			else {
				//if(this.solution.getIntVal(this.x_room[cl-1]) > 0) {
					room_use[this.solution.getIntVal(this.x_room[cl-1])] += instanceUTP.part_session_length[part-1];
				//}
				
			}
		}
		String out_statistics = "";
		for(int i = 0; i < room_use.length;i++) {
			int convert = room_use[i] /60;
			int convertmn = room_use[i] % 60;
			out_statistics+= this.instanceUTP.room_name[i]+";"+convert+"h"+convertmn+";"+room_use[i]+"\n";
		}
		
		FileOutputStream outputStream = null;
		String file_write_statistics = ModelUTP.filename_solution.substring(0,filename_solution.length()-4)+"-statistics_rooms.csv";
		try {
			outputStream = new FileOutputStream(file_write_statistics);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] strToBytes = out_statistics.getBytes();
	    try {
			outputStream.write(strToBytes);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//FinMethod
	
}//FinClass
