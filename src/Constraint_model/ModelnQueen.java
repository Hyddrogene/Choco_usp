package Constraint_model;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class ModelnQueen {
	public int n;
	public Model model;
	public IntVar[] vars;
	public Solution solution;
	
	public ModelnQueen(int n) {
		this.n = n;
		//for(int i = 0;i<n;i++) {
			//vars[] = model.intVarArray("X",)
		//}
		model = new Model("Test");
		vars = model.intVarArray("x", n*n,0,1);
		one_per_line();
		one_per_collumn();
		//all_different(vars);
	}//FinMethod
	
	public void all_different(IntVar[] vars) {
		model.allDifferent(vars).post();
	}//FinMethod
	
	public void one_per_line() {
		for(int i = 0 ; i < n; i++) {
			model.sum(sousInt(i*n,(n*i)+(n-1)),"=",1).post();
		}
	}//FinMethod
	
	public void one_per_collumn() {
		for(int i = 0 ; i < n; i++) {
			model.sum(sousInt2(i,((n-1)*n)+i),"=",1).post();
		}
	}//FinMethod
	
	public void one_per_diag_left() {
		for(int i = 0 ; i < n; i++) {
			model.sum(sousInt2(i,((n-1)*n)+i),"=",1).post();
		}
	}//FinMethod
	
	public void one_per_diag_rigth() {
		for(int i = 0 ; i < n; i++) {
			model.sum(sousInt2(i,((n-1)*n)+i),"=",1).post();
		}
	}//FinMethod
	
	public IntVar[] sousInt(int n0,int n1) {
		IntVar[] t = new IntVar[n];
		int j = 0;
		System.out.println(n0+" "+n1);
		for(int i = n0; i <= n1 ;i++) {
			t[j] = vars[i];
			j++;
		}
		return t;
	}//FinMethod
	
	public IntVar[] sousInt2(int n0,int n1) {
		IntVar[] t = new IntVar[n];
		int j = 0;
		System.out.println(n0+" "+n1);
		for(int i = n0; i <= n1 ;i = i+n) {
			t[j] = vars[i];
			j++;
		}
		return t;
	}//FinMethod
	
	/*public void diag_bas(IntVar[] vars) {
		for(int i = 0; i < n;i++) {
			for(int j = 0; j < n-i ;j++) {
				vars[i] 
			}
			
		}
		model.allDifferent(vars).post();
	}//FinMethod*/
	
	public Solution solve() {
		this.solution = model.getSolver().findSolution();
		return this.solution;
		
	}//FinMethod
	
	public void print() {
		if(this.solution != null){
		    System.out.println(this.solution.toString());
		}
	}//FinMethod

}//FinClass
