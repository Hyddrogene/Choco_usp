package Constraint_model;

import java.util.Vector;

public class TestCreator {
	public String filenameData;
	public String filenameXML;
	public InstanceUTPArray utp;
	public boolean isXML;
	
	private ConverterJsonChoco g;
	
	public TestCreator(String filename,String filenameXML,boolean isXML){
		this.filenameData  = filename;
		this.filenameXML = filenameXML;
		this.g = new ConverterJsonChoco(this.filenameData);
		this.g.CreateInstance();
		this.utp  = g.getInstanceUTPArray();
		this.isXML = isXML;
	}//FinMethod
	
	public boolean checkConstraint(String[] tab,String element){
		for(int i= 0; i < tab.length ;i++) {
			if(tab[i].equals(element)) {
				return true;
			}
		}
		return false;
	}//FinMethod
	
	public boolean checkContain(Vector<String> tab,String element){
		for(int i= 0; i < tab.size() ;i++) {
			if(tab.get(i).equals(element)) {
				return true;
			}
		}
		return false;
	}//FinMethod
	public void changeUTP( StrategieBuilt sb){
		Vector<String> deactivate = new Vector<String>();
		for(int  i = 0; i < this.utp.constraints.size();i++) {
			if( checkConstraint(sb.flatten_constraint,this.utp.constraints.get(i).getConstraint())) {
				this.utp.constraints.get(i).setIsActivate(1);
			}
			else {
				this.utp.constraints.get(i).setIsActivate(0);
				if(!checkContain(deactivate,this.utp.constraints.get(i).getConstraint())) {
					deactivate.add(this.utp.constraints.get(i).getConstraint());
				}
			}
		}
		if(deactivate.size()>0) {
			String out = "contrainte desactivate : [";
			for(int i = 0; i < deactivate.size()-1 ;i++) {
				out += deactivate.get(i)+", ";
			}
			out += deactivate.get(deactivate.size()-1)+"]\n";
			System.out.println(out);
		}
		
		for(int k = 0; k < sb.deactivate_rule.size() ;k++) {
			for(int c = 0; c < this.utp.constraints.size() ;c++) {
				if( this.utp.constraints.get(c).getRule() == sb.deactivate_rule.get(k)) {
					this.utp.constraints.get(c).setIsActivate(0);
				}
			}
		}
		
		for(int k = 0; k < sb.deactivate_constraint.size() ;k++) {
			for(int c = 0; c < this.utp.constraints.size() ;c++) {
				if( this.utp.constraints.get(c).getCpt() == sb.deactivate_constraint.get(k)) {
					this.utp.constraints.get(c).setIsActivate(0);
				}
			}
		}

	}//FinMethod
	
	@SuppressWarnings("static-access")
	public void runTest(String config) {
		StrategieBuilt sb = new StrategieBuilt(utp,config);
		//System.out.println(sb.toString());
		changeUTP(sb);
		System.out.println("Run : "+this.filenameData+" ; Solver : Choco-solver\n");
		Solution_file_generator sfg = new Solution_file_generator(this.filenameData,isXML);
		String solution_name = sfg.getSolution_name();
		
		ModelUTP utp = new ModelUTP(this.utp);
		utp.setFilename_solution(solution_name);
		utp.setStrategie(sb);
		utp.solve();
		if(isXML) {
			utp.write_solution_file(utp.print_xml2(),isXML,this.filenameXML);
		}
		else {
			utp.write_solution_file(utp.print(),isXML,this.filenameXML);
		}
		System.out.println("Finish");
	}//FinMethod
	
	public void runMultipleTest(String[] configs) {
		for(int i = 0; i < configs.length;i++) {
			runTest(configs[i]);
		}
	}//FinMethod
	
	
}//FinClass
