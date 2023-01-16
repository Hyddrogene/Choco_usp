package Constraint_model;


//import org.chocosolver.solver.*;


public class Exec {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		boolean isXML = true;
		//JsonParser jsonParser = new JsonParser("/home/etud/eclipse-workspace/exemple.json");
		//jsonParser.readJsonFile();
		//jsonParser.generateUTPInstance();
		//String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_extension_v2.json"; 
		//String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_v4_extension_v2.json";
		String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1-l2_p1-p6_extension_v2.json";
		String filenamexml = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1-l2_p1-p6.xml";
		filename = "/home/etud/timetabling/tools/tools_php/ua_l1-l2_p1-p6_l3info_2021_extension_v2.json";
		filenamexml = "/home/etud/timetabling/tools/tools_php/ua_l1-l2_p1-p6_l3info_2021.xml";
		ConverterJsonChoco g = new ConverterJsonChoco(filename);
		g.CreateInstance();
		
		//int n = 4;//size
		//ModelnQueen nqueen = new ModelnQueen(n);
		//nqueen.solve();
		//nqueen.print();
		//solution_ua_l1_p1_extensio_16112022_05_36_34_v3
		System.out.println("Run : "+filename+" ; Solver : Choco-solver\n");
		Solution_file_generator sfg = new Solution_file_generator(filename,isXML);
		String solution_name = sfg.getSolution_name();
		ModelUTP utp = new ModelUTP(g.getInstanceUTPArray());
		//ModelUTPset utp = new ModelUTPset(g.getInstanceUTPArray());
		utp.setFilename_solution(solution_name);
		utp.solve();
		if(isXML) {
			utp.write_solution_file(utp.print_xml(),isXML,filenamexml);
		}
		else {
			utp.write_solution_file(utp.print(),isXML,filenamexml);

		}
		System.out.println("Finish");
		

	}//FinMethod

}//FinMethod
