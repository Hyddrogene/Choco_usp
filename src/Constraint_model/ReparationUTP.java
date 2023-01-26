package Constraint_model;

public class ReparationUTP {
	
	private InstanceUTPArray instanceUTP;
	private GraphUTP graphUTP;
	
		ReparationUTP(GraphUTP graphUTP){
		this.instanceUTP = graphUTP.getInstanceUTPArray();
		this.graphUTP = graphUTP;
	}//FinMethod
	public String getReparation() {
		String out ="decalageTime";
		return out;
	}//FinMethod
	
	public void  repareEngine_v1() {
		
		for(int e = 0; e < graphUTP.getEvenements().length ;e++) {
			
		}
		
	}//FinMethod
	
	public void reparation_v1() {
		int[] repairSet = new int[1];
		for(int i = 0; i < repairSet.length ;i++) {
			
		}
	}//FinMethod

}//FinClass
