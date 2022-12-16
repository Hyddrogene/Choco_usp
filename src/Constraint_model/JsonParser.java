package Constraint_model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class JsonParser {
	//Attribute
	private String instanceFilenameJson;
	private JSONParser parser;
	private File file;
	private JSONObject jsonObject;
	
	//Method
	
	public JsonParser(String instanceFilenameJson) {
		this.instanceFilenameJson = instanceFilenameJson;
		this.parser = new JSONParser();
		this.file = new File(this.instanceFilenameJson);
		System.out.println("JsonParser en action !");
	}//FinMethod
	
	public void readJsonFile() {
		try {
			 this.jsonObject = (JSONObject) this.parser.parse(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//FinMethod
	
	public void generateUTPInstance() {
		
		System.out.println(jsonObject.get("data"));
		
	}//FinMethod
	
	
	public String getInstanceFilenameJson() {
		return instanceFilenameJson;
	}//FinMethod
	
	public void setInstanceFilenameJson(String instanceFilenameJson) {
		this.instanceFilenameJson = instanceFilenameJson;
	}//FinMethod
}
