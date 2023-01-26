package Constraint_model;

public class sessionSolution {
	//Attribute
	private int rank;
	private int cpt;
	private int id;
	private String type = "session";
	
	private int dailySlot;
	private int day;
	private int week;
	
	private int[] teachers;
	private int[] rooms;
	
	//Method
	public sessionSolution(int rank,int cpt,int ds, int d, int w,int[] ts, int[] rs) {
		this.rank = rank;
		this.cpt = cpt;
		this.id = cpt;
		this.dailySlot = ds;
		this.day = d;
		this.week = w;
		this.teachers = ts;
		this.rooms = rs;
	}//FinMethod
	
	public int getDailySlot() {
		return dailySlot;
	}

	public int getDay() {
		return day;
	}

	public int getWeek() {
		return week;
	}

	public int[] getTeachers() {
		return teachers;
	}

	public int[] getRooms() {
		return rooms;
	}

	public int getRank() {
		return rank;
	}//FinMethod
	
	
	public int getCpt() {
		return cpt;
	}//FinMethod
	
	
	public int getId() {
		return id;
	}//FinMethod
	
	public String getType() {
		return type;
	}//FinMethod

	
}//FinClass
