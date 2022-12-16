package Constraint_model;

import java.util.Arrays;
import java.util.Vector;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;

public class ModelUTP {
	//Attribute
	private InstanceUTPArray instanceUTP;
	private Model model;
	private IntVar[] x_teacher;
	//private SetVar[] x_teacher;
	private IntVar[] x_room;
	//private SetVar[] x_room;
	private IntVar[] x_slot;
	private Solution solution;
	//Method
	public ModelUTP(InstanceUTPArray instanceUTP) {
		this.model = new Model("Modele USP");
		this.instanceUTP = instanceUTP;
		x_slot = new IntVar[instanceUTP.nr_sessions];
		x_room = new IntVar[instanceUTP.nr_classes];
		x_teacher = new IntVar[instanceUTP.nr_classes];
		this.instanceUTP.calcul(); 
		initVariables();
		constraint();
		solve();
		print();
		
	}//FinMethod
	
	public int[] part_i_slots(Vector<Integer> t) {
		int[] tab = new int[t.size()];
		for(int i = 0; i < t.size() ;i++) {
			tab[i] = t.get(i).intValue();
		}
		return tab;
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
			x_room[i] = model.intVar("x_room_"+i,part_i_slots(this.instanceUTP.part_teachers.get(this.instanceUTP.class_part[i]-1)));
		}
	}//FinMethod
	
	public void constraint() {
		implicite_sequenced_sessions();
		teacher_service_v2();
		disjunctive_teacher_v2();
		disjunctive_group_v2();
		disjunctive_room_v2();
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

		}
	}//FinMethod
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
	//======================
	
	
	public Solution solve() {
		this.solution = model.getSolver().findSolution();
		return this.solution;
	}//FinMethod
	
	public void print() {
		if(this.solution != null){
			String out = "";
			int[] x_s = new int[this.instanceUTP.nr_sessions];
			int[] x_t = new int[this.instanceUTP.nr_classes];
			int[] x_r = new int[this.instanceUTP.nr_classes];
			
			for(int i = 0; i < this.instanceUTP.nr_sessions;i++) {
				x_s[i] = this.solution.getIntVal(this.x_slot[i]);
			}
			
			for(int i = 0; i < this.instanceUTP.nr_classes;i++) {
				x_t[i] = this.solution.getIntVal(this.x_teacher[i]);
			}
			
			for(int i = 0; i < this.instanceUTP.nr_classes;i++) {
				x_r[i] = this.solution.getIntVal(this.x_room[i]);
			}
			
			for(int i = 0; i<this.instanceUTP.nr_sessions;i++) {
				out +=  "session:"+(i+1)+";rank:"+this.instanceUTP.session_rank[i]+
						";course:"+this.instanceUTP.course_name[this.instanceUTP.part_course[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]-1]+
						";part:"+ this.instanceUTP.part_name[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]+
						";class:"+this.instanceUTP.class_name[this.instanceUTP.session_class[i]-1]+
				     	";rooms:"+this.instanceUTP.room_name[x_r[this.instanceUTP.session_class[i]-1]-1]+
						";teachers:"+this.instanceUTP.teacher_name[x_t[this.instanceUTP.session_class[i]-1]-1]+
						";group:"+
						";slot:[\""+x_s[i]+
						"\", \""+this.instanceUTP.part_session_length[this.instanceUTP.class_part[this.instanceUTP.session_class[i]-1]-1]+"\"]\n";
				//out +=  x_s[i]+" ";
			}
			System.out.println(out);
		   //System.out.println(this.solution.toString());
		}
		else {
			System.out.println("UNSAT");
		}
	}//FinMethod
	
}//FinClass
