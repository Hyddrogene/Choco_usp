package Constraint_model;

import java.util.Arrays;

import java.util.Vector;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;
public class IndomainMinDecalage implements IntValueSelector {

	public InstanceUTPArray  utp;

	public Vector<Vector<Vector<Integer>>> group_of_classes_eq;
	
	
	public IndomainMinDecalage(Vector<Vector<Vector<Integer>>> eq,InstanceUTPArray utp ) {
		super();
		group_of_classes_eq = eq;
		this.utp = utp;
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
		
		
		/*int part_cl = utp.part_classes.get(utp.class_part[getSessionForVar(var)]-1).size();
		part_cl = utp.class_part[getSessionForVar(var)];
		
		int u = chooseMinimum(linear_room(part_cl));
		//System.out.println(Arrays.toString(linear_room(part_cl)));*/
		int u =var.getLB();
		return u;
	}//FinMethod

}//FinClass
