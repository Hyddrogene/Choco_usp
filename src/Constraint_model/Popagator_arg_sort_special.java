package Constraint_model;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.tools.ArrayUtils;


public class Popagator_arg_sort_special extends Propagator<IntVar> {
	public int sizeTabvars;
	public int sizeElementVars;
	public int sizeSelectedVars;
	
	public int val;
	public int selectedVarsSize;
	public InstanceUTPArray utp;
	public int[] _sessions;
	
	public Popagator_arg_sort_special(IntVar[] tabVars, IntVar[] elementVars, IntVar[] selectedVars, int val, int selectedVarsSize,InstanceUTPArray utp) {
		super(ArrayUtils.append(tabVars,elementVars,selectedVars), PropagatorPriority.LINEAR, false);
		this.sizeTabvars = tabVars.length;
		this.sizeElementVars = elementVars.length;
		this.sizeSelectedVars = selectedVars.length;
		
		this.val = val;
		this.utp = utp;
		int sumforSessions = 0;
		for(int i = 0; i < utp.teacher_parts.get(val).size() ;i++) {
			for(int u = 0; u < utp.part_classes.get( utp.teacher_parts.get(val).get(i)-1).size() ;u++) {
				sumforSessions += utp.class_sessions.get(utp.part_classes.get( utp.teacher_parts.get(val).get(i)-1).get(u)-1).size();

				
			}
		}
		int w = 0;
		_sessions  = new int[sumforSessions];
		for(int i = 0; i < utp.teacher_parts.get(val).size() ;i++) {
			for(int u = 0; u < utp.part_classes.get( utp.teacher_parts.get(val).get(i)-1).size() ;u++) {
				for(int y = 0; y < utp.class_sessions.get(utp.part_classes.get( utp.teacher_parts.get(val).get(i)-1).get(u)-1).size();y++) {
					_sessions[w] = utp.class_sessions.get(utp.part_classes.get( utp.teacher_parts.get(val).get(i)-1).get(u)-1).get(y)-1;
					w++;
				}
				
			}
		}
	
	}//FinMethod
	@Override
	public ESat isEntailed() {
		if(this.isCompletelyInstantiated()) {
			int c = 0;
			int[] _x=new int[this.sizeSelectedVars];
			for(int i = 0; i < this._sessions.length ;i++) {
				if(vars[_sessions[i]].getValue() == val) {
					_x[c] = _sessions[i];
					c++;
				}
			}	
			for(int i = 0; i < _x.length ;i++) {
				if(vars[_x[i]+this.sizeTabvars] != vars[this.sizeTabvars+this.sizeElementVars+_x[i]]) {
					return ESat.FALSE;
				}
			}
		}
        return ESat.TRUE;
	}//FinMethod

	@Override
	public void propagate(int arg0) throws ContradictionException {
		int c =0;
		
		for(int i = 0; i < _sessions.length ;i++) {
			if(vars[_sessions[i]].isInstantiated()) {
			if(vars[_sessions[i]].getValue() == val) {
				
				//int ub = vars[c].getUB(); //this.sizeTabvars+this.sizeElementVars+i
				//vars[this.sizeTabvars+i].eq(vars[c]);
				//vars[c].eq(vars[this.sizeTabvars+i]);
				vars[c] = vars[this.sizeTabvars+_sessions[i]];

				/*while(ub != vars[this.sizeTabvars+i].getValue()) {
					ub = vars[c].nextValue(ub);
				}*/
				c++;
			}
			}
		}		
	}//FinMethod

}
