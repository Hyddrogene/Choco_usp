package Constraint_model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Solution_file_generator {
	private String solution_name;
	public Solution_file_generator(String filename, boolean isXML ) {
		Date date = new Date();
		SimpleDateFormat formater1 = new SimpleDateFormat("ddMMyy");
		SimpleDateFormat formater2 = new SimpleDateFormat("hh_mm_ss");
		SimpleDateFormat formater3 = new SimpleDateFormat("dd-MM-yy");

		String date_formated1 = formater1.format(date).toString();
		String date_formated2 = formater2.format(date).toString();
		String date_formated3 = formater3.format(date).toString();

		String[] words = filename.split("/");
		String name_instance = words[words.length-1].substring(0,words[words.length-1].length()-5);
		test_directory("./tmp/experiment_"+date_formated3);
		if(isXML) {
			solution_name  = "./tmp/experiment_"+date_formated3+"/solution_"+name_instance+"_"+date_formated1+"_"+date_formated2+".xml";

		}
		else {
			solution_name  = "./tmp/experiment_"+date_formated3+"/solution_"+name_instance+"_"+date_formated1+"_"+date_formated2+".txt";

		}
	}//FinMethod
	
	public void test_directory(String directory) {
		Path path = Paths.get(directory);
		if(!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}//FinMethod

	public String getSolution_name() {
		return solution_name;
	}//FinMethod
	
}//FinClass
