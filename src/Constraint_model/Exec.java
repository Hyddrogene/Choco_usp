package Constraint_model;


//import org.chocosolver.solver.*;


public class Exec {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		boolean isXML = true;
		//String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_extension_v2.json"; 
		//String filename = "/home/etud/timetabling/instances/benchmarks/ua_l1_p1-p2/ua_l1_p1_v4_extension_v2.json";
		String filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_extension_v5.json";
		String filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_bis.xml";
		//filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v2_extension_v2.json";
		//filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v2.xml";
		filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v3_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v3.xml";

		filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v5_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v5bis.xml";

		filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v5_hard_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_v5bis.xml";
		
		filename = "/home/etud/eclipse-workspace/Constraint_model/instance/M2/ua_m2-informatique-s1-s2_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/M2/ua_m2-informatique-s1-s2Bis.xml";

		filename = "/home/etud/eclipse-workspace/Constraint_model/instance/M1/ua_m1-informatique-s1-s2_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/M1/ua_m1-informatique-s1-s2-bis.xml";
		
		filename = "/home/etud/timetabling/tools/tools_php/ua_m1-informatique-s1_extension_v2.json";
		filenamexml = "/home/etud/timetabling/tools/tools_php/ua_m1-informatique-s1.xml";

		filename = "/home/etud/timetabling/tools/tools_php/ua_l1-l2_p1-p6_l3info_m1info-s1_m2info-s1-s2_v1_extension_v2.json";
		filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l1-l2_p1-p6_l3info_m1info-s1_m2info-s1-s2_v1_bis.xml";
		filenamexml = "/home/etud/timetabling/tools/tools_php/ua_l1-l2_p1-p6_l3info_m1info-s1_m2info-s1-s2_v1.xml";
		
		//filename = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l3info_2021_bdd_dw-noPR_opt_plf_anglais_extension_v2.json";
		//filenamexml = "/home/etud/eclipse-workspace/Constraint_model/instance/ua_l3info_2021_bdd_dw-noPR_opt_plf_anglais.xml";
		
		String strategie1 = "/home/etud/testdestratégie.json";
		strategie1 = "/home/etud/test_strategie_v2.json";
		//strategie1 = "/home/etud/strategie_master.json";
		//strategie1 = "/home/etud/test_strategie_v3.json";

		TestCreator tc = new TestCreator(filename,filenamexml,isXML);
		tc.runTest(strategie1);
		
		/*
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
		System.out.println("Finish");*/
		//GraphUTP gutp = new GraphUTP(g.getInstanceUTPArray());

	}//FinMethod

}//FinMethod
