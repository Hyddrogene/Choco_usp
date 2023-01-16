package Constraint_model;

import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;
public class decisionWeekEquilibrate implements IntValueSelector {
	public Integer decalage;
	public IntVar[] vars;
	public int[] rank;
	public int[] xsession;
	public int[] xpassage;
	public InstanceUTPArray  utp;
	public int week_size;
	public int middleWeek;
	public int finalWeek;
	
	public decisionWeekEquilibrate(Integer v,IntVar[] tab,IntVar[] allxSlot,InstanceUTPArray  utp) {
		super();
		this.decalage = v++;
		this.vars = tab;
		this.rank = new int[this.vars.length];
		this.xsession = new int[this.vars.length];
		this.xpassage = new int[this.vars.length];
		this.utp = utp;
		for(int i = 0; i < tab.length;i++) {
			for(int j = 0; j < allxSlot.length ;j++) {
				if(tab[i].getId() == allxSlot[j].getId() ) {
					xsession[i] = j+1;
				}
			}
		}
		
		this.week_size = utp.nr_days_per_week * utp.nr_slots_per_day;
		this.middleWeek = (int) this.week_size - utp.nr_slots_per_day*2 - utp.nr_slots_per_day/2;
		this.finalWeek = (int)( this.week_size-utp.nr_slots_per_day)+1;

	}//FinMethod
	
	public int getSessionForVar(IntVar var) {
		for(int i = 0; i < vars.length ;i++) {
			if(vars[i].getId() == var.getId()) {
				return i;
			}
		}
		return 0;
	}//FinMethod
	int fi = 1;
	@Override
	public int selectValue(IntVar var) {
		//var.getDomainSize();
		int part_cl = utp.part_classes.get(utp.class_part[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1]-1).size();
		int u = (part_cl-1)/3;
		int u1 = 1 + (part_cl-1)/3;
		if(fi == 0) {
			System.out.println("Certes u1 = "+u1+" u = "+u);
		}
		//System.out.println(var.getId()+" "+this.decalage);
		int slen = utp.part_session_length[utp.class_part[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1]-1];
		//var.nextValue(decalage);
		if( utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1]-1 <= u1 ) {
		//if( utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1] == 1 || utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1] ==2  ) {
			return var.getLB();
		}
		else if(utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1]-1  <= u1 +u ){
		//else if(utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1] == 3 || utp.class_rank[utp.session_class[this.xsession[getSessionForVar(var)]-1]-1] == 4 ){

			int value = var.getLB();
			if(this.xpassage[getSessionForVar(var)] > 0) {
				if(this.xpassage[getSessionForVar(var)]  % 4 == 1) {
					//while ( this.middleWeek+(slen*slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
					while ( this.middleWeek+(slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
						value = var.nextValue(value);
					}
				}
				else {
					while ( this.middleWeek-(slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
					//while ( this.middleWeek-(slen*slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
						value = var.nextValue(value);
					}
				}

			}
			else {
				while ( this.middleWeek >= value) {
					value = var.nextValue(value);
				}
			}

			this.xpassage[getSessionForVar(var)]++;
			return value;
		}
		else {
			int value = var.getLB();
			if(this.xpassage[getSessionForVar(var)] >= 0) {
				if(this.xpassage[getSessionForVar(var)] < 0) {//%2 =0
					while ( this.finalWeek-(slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
						value = var.nextValue(value);
					}
				}
				else {
					while ( this.finalWeek-(slen*(((this.xpassage[getSessionForVar(var)]-1)/2)+1)) > value) {
						value = var.nextValue(value);
					}
				}

			}
			else {
				while ( this.finalWeek > value) {
					value = var.nextValue(value);
				}
			}

			this.xpassage[getSessionForVar(var)]++;
			return value;
		}
		//return var.getLB();
	}

}
