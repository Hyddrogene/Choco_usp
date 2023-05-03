package Constraint_model;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class constraint_arg_sort extends Constraint {

	public constraint_arg_sort(IntVar[] tVar,IntVar[] elVar,IntVar[] resVar,int u,int i,InstanceUTPArray utp) {
		super("Constraint_arg_sort", new Popagator_arg_sort_special(tVar, elVar,resVar, u, i,utp));
	}

}
