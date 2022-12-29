package Constraint_model;


//import org.chocosolver.solver.*;


public class Exec {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		//JsonParser jsonParser = new JsonParser("/home/etud/eclipse-workspace/exemple.json");
		//jsonParser.readJsonFile();
		//jsonParser.generateUTPInstance();
		//String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_extension_v2.json"; 
		String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_v4_extension_v2.json";
		ConverterJsonChoco g = new ConverterJsonChoco(filename);
		g.CreateInstance();
		
		//int n = 4;//size
		//ModelnQueen nqueen = new ModelnQueen(n);
		//nqueen.solve();
		//nqueen.print();
		//solution_ua_l1_p1_extensio_16112022_05_36_34_v3
		System.out.println("Run : "+filename+" ; Solver : Choco-solver\n");
		Solution_file_generator sfg = new Solution_file_generator(filename);
		String solution_name = sfg.getSolution_name();
		ModelUTP utp = new ModelUTP(g.getInstanceUTPArray());
		utp.setFilename_solution(solution_name);
		utp.solve();
		utp.write_solution_file(utp.print());
		System.out.println("Finish");
		

	}//FinMethod

}//FinMethod
