package Constraint_model;

//import org.chocosolver.solver.*;


public class Exec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello le code\n ça fontionne pour le moment ?");
		
		
		JsonParser jsonParser = new JsonParser("/home/etud/eclipse-workspace/exemple.json");
		jsonParser.readJsonFile();
		jsonParser.generateUTPInstance();
		String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_extension_v2.json"; 
		ConverterJsonChoco g = new ConverterJsonChoco(filename);
		g.CreateInstance();
		
		//int n = 4;//size
		//ModelnQueen nqueen = new ModelnQueen(n);
		//nqueen.solve();
		//nqueen.print();
		
		ModelUTP utp = new ModelUTP(g.getInstanceUTPArray());
		/*
		Model model = new Model("Modele de bg");
		IntVar[] vars = new IntVar[n];
		for(int q = 0; q < n; q++){
		    vars[q] = model.intVar("Q_"+q, 1, n);
		}
		for(int i  = 0; i < n-1; i++){
		    for(int j = i + 1; j < n; j++){
		        model.arithm(vars[i], "!=",vars[j]).post();
		        model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
		        model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
		    }
		}
		for(int i = 0; i < n-1 ;i++) {
			
		}
		Solution solution = model.getSolver().findSolution();
		if(solution != null){
		    System.out.println(solution.toString());
		}*/

	}//FinMethod

}//FinMethod
