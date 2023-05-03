package Constraint_model;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

public class MinConflict implements IntValueSelector {
	
	private Model model;
	public InstanceUTPArray  utp;
	private Random rand;
	
	public MinConflict(long seed,Model model,InstanceUTPArray  utp) {
		super();
		this.model = model;
		this.utp = utp;
		 this.rand = new Random(seed);
	}//FinMethod
	
	@Override
	public int selectValue(IntVar var) {
		/* while() {
			 
		 }*/
		int i = rand.nextInt(var.getDomainSize());
		int u = var.getLB();
		while (i > 0) {
			u = var.nextValue(u);
			i--;
		}
		return u;
	}//FinMethod

}//FinClass
