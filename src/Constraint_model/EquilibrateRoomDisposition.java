package Constraint_model;

import java.util.Arrays;

import java.util.Vector;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;
public class EquilibrateRoomDisposition implements IntValueSelector {

	public InstanceUTPArray  utp;

	public int nrRooms;
	public int[] rooms_use;
	public int nrVars;
	public int[] labelVars;
	
	public Vector<Vector<Integer>> tabu_room;
	
	public int chooseMinimum(int[] tab) {
		int minimum = tab[0];
		for(int r = 1; r < tab.length ;r++) {
			if(rooms_use[minimum-1] > rooms_use[tab[r]-1]) {
				minimum = tab[r];
			}
		}
		return minimum;
	}//FinMethod
	
	public EquilibrateRoomDisposition(IntVar[] vars,InstanceUTPArray  utp,int nrRooms) {
		super();
		this.nrRooms = nrRooms;
		this.nrVars = vars.length;
		this.tabu_room = new Vector<Vector<Integer>>();
		this.utp = utp;
		this.rooms_use = new int[this.nrRooms];
		this.labelVars = new int[this.nrVars];
		for(int i = 0; i < this.nrRooms ;i++) {
			this.rooms_use[i] = 0;
		}
		
		for(int i = 0; i < this.nrVars ;i++) {
			labelVars[i] = vars[i].getId();
		}
	}//FinMethod
	
	public int getSessionForVar(IntVar var) {
		for(int i = 0; i < labelVars.length ;i++) {
			if(labelVars[i] == var.getId()) {
				return i;
			}
		}
		return 0;
	}//FinMethod
	
	public int[] linear_room(int part) {
		int[] rooms = new int[ this.utp.part_rooms.get(part-1).size()];
		for(int r = 0; r < this.utp.part_rooms.get(part-1).size() ;r++) {
			rooms[r] =  this.utp.part_rooms.get(part-1).get(r);
		}
		return rooms;
	}//FinMethod
	
	public void outPut(int[] tab) {
		System.out.println(Arrays.toString(tab));
	}//FinMethod
	@Override
	public int selectValue(IntVar var) {
		//var.getDomainSize();
		
		
		int part_cl = utp.part_classes.get(utp.class_part[getSessionForVar(var)]-1).size();
		part_cl = utp.class_part[getSessionForVar(var)];
		
		int u = chooseMinimum(linear_room(part_cl));
		//System.out.println(Arrays.toString(linear_room(part_cl)));
		this.rooms_use[u-1]++;
		
		/*if() {
			
		}else {
			u = chooseMinimum(linear_room(part_cl));
			this.rooms_use[u-1]++;
		}*/
		
		//System.out.println("u ="+utp.room_name[u]+" u = "+u);
		//System.out.println("v ="+utp.class_name[getSessionForVar(var)]+" p ="+getSessionForVar(var));
		/*int value = var.getLB();
		while (  u != value) {
			value = var.nextValue(value);
		}*/
		//System.out.println("u ="+utp.room_name[u]);
		return u;
	}//FinMethod

}//FinClass
