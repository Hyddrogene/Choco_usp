package Constraint_model;

//==================
import Constraint_model.InstanceUTPGraph.Constraint;
import Constraint_model.InstanceUTPGraph.Course;
import Constraint_model.InstanceUTPGraph.Group;
import Constraint_model.InstanceUTPGraph.Label;
import Constraint_model.InstanceUTPGraph.Lecturer;
import Constraint_model.InstanceUTPGraph.Part;
import Constraint_model.InstanceUTPGraph.Room;
import Constraint_model.InstanceUTPGraph.Session;
import Constraint_model.InstanceUTPGraph.Student;

import java.util.Vector;

import Constraint_model.InstanceUTPGraph.Class;
//==================

public class GraphUTP {
	//==================
	private InstanceUTPArray instanceUTPArray;
	
	private Session[] sessions;
	private Class[] classes;
	private Part[] parts;
	private Course[] courses;
	
	private Lecturer[] lecturers;
	private Group[] groups;
	private Student[] students;
	private Room[] rooms;
	
	private Label[] labels;
	
	private Evenement[] evenements;
	private sessionSolution[] sessionSolutions;
	
	//private Rule[] rules;
	private Constraint[] constraints;
	//==================
	public GraphUTP(InstanceUTPArray instanceUTPArray) {
		this.instanceUTPArray = instanceUTPArray;
		generateGraph();
	}//finMethod
	
	public void generateGraph() {
		generateLabels();
		generateSessions();
		generateLecturers();
		generateRooms();
		generateClasses();
		generateParts();
		generateCourses();
		
		generateStudents();
		generateGroups();
		generateConstraint();
		
	}//FinMethod
	
	public void generateStudents() {
		Student[] tab = new Student[this.instanceUTPArray.nr_students];
		for(int i = 0; i < this.instanceUTPArray.nr_students ;i++) {
			Course[] courses = new Course[this.instanceUTPArray.student_courses.get(i).size()];
			int sumSC = 0;
			int sumSS = 0;
			for(int c = 0; c < this.instanceUTPArray.student_courses.get(i).size() ;c++) {
				courses[c] = this.courses[this.instanceUTPArray.student_courses.get(i).get(c)-1];
				sumSC += courses[c].getParts().length;
			}
			tab[i] = new Student(i+1,this.instanceUTPArray.student_name[i],getLabel(this.instanceUTPArray.student_label.get(i)),courses);
			
			Class[] cls = new Class[sumSC];
			Part[] prts = new Part[sumSC];
			int iPrts = 0;
			for(int c = 0; c < courses.length ;c++) {
				for(int p = 0; p < this.instanceUTPArray.course_parts.get(c).size() ;p++) {
					prts[iPrts] = this.parts[this.instanceUTPArray.course_parts.get(c).get(p)-1];
					sumSS += this.instanceUTPArray.part_nr_sessions[this.instanceUTPArray.course_parts.get(c).get(p)-1];
				} 
			}
			int student_group = this.instanceUTPArray.student_group[i];
			Session[] ss = new Session[sumSS];
			int iSS = 0;
			for(int cl = 0; cl < this.instanceUTPArray.group_classes.get(student_group-1).size() ;cl++) {
				cls[cl] = this.classes[this.instanceUTPArray.group_classes.get(student_group-1).get(cl)-1];
				for(int s = 0; s < this.instanceUTPArray.class_sessions.get(this.instanceUTPArray.group_classes.get(student_group-1).get(cl)-1).size() ;s++) {
					ss[iSS] = this.sessions[this.instanceUTPArray.class_sessions.get(this.instanceUTPArray.group_classes.get(student_group-1).get(cl)-1).get(s)-1];
					iSS++;
				}
			}
			tab[i].setClasses(cls);
			//tab[i].setGroup(student_group);
			tab[i].setSessions(ss);
		}
		this.students = tab;
	}//FinMethod
	
	public void generateGroups() {
		Group[] groups = new Group[this.instanceUTPArray.nr_groups];
		for(int g = 0; g < this.instanceUTPArray.nr_groups ;g++) {
			Class[] cls = new Class[this.instanceUTPArray.group_classes.get(g).size()];
			for(int c = 0; c < this.instanceUTPArray.group_classes.get(g).size() ;c++) {
				cls[c] = this.classes[this.instanceUTPArray.group_classes.get(g).get(c)-1];
			}
			int hc = this.instanceUTPArray.group_headcount[g];
			Student[] students = new Student[hc];
			for(int st = 0; st < hc ;st++) {
				students[st] = this.students[this.instanceUTPArray.group_students.get(g).get(st)-1]; 
			}
			groups[g] = new Group(this.instanceUTPArray.group_name[g],g+1,"group",new Label[0],hc,students,classes);
			for(int st = 0; st < hc ;st++) {
				this.students[this.instanceUTPArray.group_students.get(g).get(st)-1].setGroup(groups[g]);
			}
		}
		this.groups = groups;
	}//FinMethod
	
	public void generateLecturers() {
		Lecturer[] lecturers = new Lecturer[this.instanceUTPArray.nr_teachers];
		for(int t = 0; t < this.instanceUTPArray.nr_teachers ;t++) {
			lecturers[t] = new Lecturer(this.instanceUTPArray.teacher_name[t],t+1,"lecturer",getLabel(this.instanceUTPArray.teacher_label.get(t)));
		}
		this.lecturers = lecturers;
	}//FinMethod
	
	public void generateRooms() {
		Room[] rooms = new Room[this.instanceUTPArray.nr_rooms];
		for(int r = 0; r < this.instanceUTPArray.nr_rooms ;r++) {
			rooms[r] = new Room(this.instanceUTPArray.room_name[r],r+1,"room",getLabel(this.instanceUTPArray.room_label.get(r)),this.instanceUTPArray.room_capacity[r]);
		}
		this.rooms = rooms;
	}//FinMethod
	
	
	
	public void generateSessions() {
		Session[] tab = new Session[this.instanceUTPArray.nr_sessions];
		int sess = 0;
		for (int i = 0; i < this.instanceUTPArray.nr_parts ;i++) {
			for(int j = 0; j < this.instanceUTPArray.part_classes.get(i).size() ;j++) {
				for(int k = 0; k < this.instanceUTPArray.part_nr_sessions[i] ;k++) {
					tab[sess] = new Session(k+1,sess+1);
					sess++;
				}
			}
		}
		this.sessions = tab;
	}//FinMethod
	
	public void generateClasses() {
		Class[] tab = new Class[this.instanceUTPArray.nr_classes];
		int sess = 0;
		int cl = 0;
		for (int i = 0; i < this.instanceUTPArray.nr_parts ;i++) {
			for(int j = 0; j < this.instanceUTPArray.part_classes.get(i).size() ;j++) {
				Session[] sessions = new Session[this.instanceUTPArray.part_nr_sessions[i]];
				for(int k = 0; k < this.instanceUTPArray.part_nr_sessions[i] ;k++) {
					sessions[k] = this.sessions[sess];
					sess++;
				}
				//Label[] lbls = getLabel(this.instanceUTPArray.label);
				tab[cl] = new Class(this.instanceUTPArray.class_name[this.instanceUTPArray.part_classes.get(i).get(j)-1],cl+1,"class",sessions);
				cl++;
				}
			}
		this.classes = tab;
	}//FinMethod
	
	public void generateParts() {
		Part[] tab = new Part[this.instanceUTPArray.nr_classes];
		for (int i = 0; i < this.instanceUTPArray.nr_parts ;i++) {
			Class[] cls = new Class[this.instanceUTPArray.part_classes.get(i).size()];
			
			Session[] sessions = new Session[this.instanceUTPArray.part_classes.get(i).size()*this.instanceUTPArray.part_nr_sessions[i]];
			int sess = 0;
			
			for(int j = 0; j < this.instanceUTPArray.part_classes.get(i).size() ;j++) {
				for(int k = 0; k < this.instanceUTPArray.part_nr_sessions[i] ;k++) {
					sessions[sess] = this.sessions[sess];
					sess++;
				}
				cls[j] = this.classes[this.instanceUTPArray.part_classes.get(i).get(j)-1];
				//System.out.println("part "+this.instanceUTPArray.part_name[i]+" class "+cls[j].getId());
			}
			Label[] lbls = getLabel(this.instanceUTPArray.part_label.get(i));
			int[] service_t = new int[this.instanceUTPArray.part_teachers.get(i).size()];//this.instanceUTPArray.part_teacher_sessions_count[i];
			int[] mandatory = new int[this.instanceUTPArray.part_rooms.get(i).size()];
			Lecturer[] lecturers = new Lecturer[this.instanceUTPArray.part_teachers.get(i).size()];
			Room[] rooms = new Room[this.instanceUTPArray.part_rooms.get(i).size()];
			int ss_len = this.instanceUTPArray.part_session_length[i];
			int nr_sess = this.instanceUTPArray.part_nr_sessions[i];
			
			for(int g = 0; g < this.instanceUTPArray.part_teachers.get(i).size() ;g++) {
				lecturers[g] = this.lecturers[this.instanceUTPArray.part_teachers.get(i).get(g)-1];
				service_t[g] = this.instanceUTPArray.part_teacher_sessions_count[i][this.instanceUTPArray.part_teachers.get(i).get(g)-1];
			}
			for(int g = 0; g < this.instanceUTPArray.part_rooms.get(i).size() ;g++) {
				rooms[g] = this.rooms[this.instanceUTPArray.part_rooms.get(i).get(g)-1];
				mandatory[g] = 0;//this.instanceUTPArray.part_room_mandatory
			}
			
		
			tab[i] = new Part(this.instanceUTPArray.part_name[i],i+1,"part",lbls,cls,rooms,lecturers,service_t,ss_len,nr_sess,mandatory);
			tab[i].setSessions(sessions);
			}
		this.parts = tab;
	}//FinMethod
	
	public void generateCourses() {
		Course[] tab = new Course[this.instanceUTPArray.nr_courses];
		for(int c = 0; c < this.instanceUTPArray.nr_courses ;c++) {
			Part[] prts = new Part[this.instanceUTPArray.course_parts.get(c).size()];
			int sumPC = 0;
			int sumPS = 0;
			for(int p = 0; p < this.instanceUTPArray.course_parts.get(c).size() ;p++) {
				sumPC += this.instanceUTPArray.part_classes.get(p).size(); 
				sumPS += this.instanceUTPArray.part_classes.get(p).size()*this.instanceUTPArray.part_nr_sessions[p];
				prts[p] = this.parts[this.instanceUTPArray.course_parts.get(c).get(p)];
			}
			Class[] cls = new Class[sumPC]; 
			Session[] ss = new Session[sumPS]; 
			int iPC = 0;
			int iPS = 0;
			for(int p = 0 ; p < this.instanceUTPArray.course_parts.get(c).size() ;p++) {
				for(int cl = 0; cl < this.instanceUTPArray.part_classes.get(p).size() ;cl++) {
					cls[iPC] = this.classes[this.instanceUTPArray.part_classes.get(p).get(cl)-1];
					iPC++;
					for(int s = 0; s < this.instanceUTPArray.part_nr_sessions[p] ;s++ ) {
						ss[iPS] = this.sessions[this.instanceUTPArray.class_sessions.get(this.instanceUTPArray.part_classes.get(p).get(cl)-1).get(s)-1];
						iPS++;
					}
				}
			}
			tab[c] = new Course(this.instanceUTPArray.course_name[c],c+1,"course",getLabel(this.instanceUTPArray.course_label.get(c)),prts);
			tab[c].setClasses(cls);
			tab[c].setSessions(ss);
		}
		this.courses = tab;
	}//FinMethod
	
	
	public void generateConstraint() {
		Constraint[] constraints = new Constraint[this.instanceUTPArray.constraints.size()];
		for(int i = 0; i < this.instanceUTPArray.constraints.size() ;i++) {
			ConstraintUTP cns = this.instanceUTPArray.constraints.get(i);
			Vector<Vector<Session>> ss = new Vector<Vector<Session>>(cns.getSessions().size());
			for(int s1 = 0; s1 < cns.getSessions().size() ;s1++) {
				Vector<Session> ss_p = new Vector<Session>(cns.getSessions().get(s1).size());
				for(int s2 = 0; s2 < cns.getSessions().get(s1).size() ;s2++) {
					ss_p.add(s2,this.sessions[cns.getSessions().get(s1).get(s2)-1]);
				}
				ss.add(s1, ss_p);
			}
			constraints[i] = new Constraint(cns.getRule(),cns.getCpt(),cns.getConstraint(),
					cns.getHardness(),cns.getArity(),cns.getType(),cns.getElements(),ss,cns.getParameters());
		}
	}//FinMethod
	
	public Label[] getLabel(Vector<Integer> labels) {
		Label[] tab = new Label[labels.size()];
		
		for(int i = 0; i < labels.size() ;i++) {
			tab[i] = this.labels[labels.get(i)-1]; 
		}
		
		return tab;
	}//FinMethod

	public void generateLabels() {
		Label[] tab = new Label[this.instanceUTPArray.nr_labels];
		for(int i = 0; i < this.instanceUTPArray.nr_labels ;i++) {
			tab[i] = new Label(i+1,this.instanceUTPArray.label_name[i]);
		}
		this.labels = tab;
	}//FinMethod

	public InstanceUTPArray getInstanceUTPArray() {
		return instanceUTPArray;
	}//FinMethod

	public Session[] getSessions() {
		return sessions;
	}//FinMethod

	public Class[] getClasses() {
		return classes;
	}//FinMethod

	public Part[] getParts() {
		return parts;
	}//FinMethod

	public Course[] getCourses() {
		return courses;
	}//FinMethod

	public Lecturer[] getLecturers() {
		return lecturers;
	}//FinMethod

	public Group[] getGroups() {
		return groups;
	}//FinMethod

	public Student[] getStudents() {
		return students;
	}//FinMethod

	public Room[] getRooms() {
		return rooms;
	}//FinMethod

	public Label[] getLabels() {
		return labels;
	}//FinMethod

	public Constraint[] getConstraints() {
		return constraints;
	}//FinMethod
	
	public Session getSessionWithIdSession() {
		return this.sessions[0];
	}//FinMethod
	
	public void searchGoodSessionsTime(int d, int w,Vector<Session> s) {
		/*int nrdayperweek = this.instanceUTPArray.nr_days_per_week;
		int nrslotday = this.instanceUTPArray.nr_slots_per_day;
		int daystart = ((w-1) * (nrslotday * nrdayperweek))+(nrslotday * (d-1)) + d;*/
		Vector<Integer> tabInt = new Vector<Integer>();
		for(int  i = 0; i < this.sessionSolutions.length ;i++) {
			//int slot = this.sessionSolutions[i].getDailySlot() * this.sessionSolutions[i].getWeek() *;
			if(d == this.sessionSolutions[i].getDay() && w == this.sessionSolutions[i].getWeek()) {
				tabInt.add(this.sessionSolutions[i].getCpt());
			}
		}
		
		for(int i = 0; i < tabInt.size() ;i++) {
			for(int j = 0; j < this.sessions.length ;j++) {
				if(sessions[j].getCpt() == tabInt.get(i)) {
					s.add(sessions[j]);
					break;
				}
			}
		}
		
	}//FinMethod
	
	/*public Session[] getSessionsWith(Evenement e) {
		Vector<Session> tab = new Vector<Session>();
		
		for(int i = 0; i < 5 ;i++) {
			
		}
		
		Session[] tabFinal = new Session[tab.size()];
		for(int i = 0; i < tab.size() ;i++) {
			tabFinal[i] = tab.get(i);
		}
		return tabFinal;
	}//FinMethod*/
	 
	public void perturbedSessions(Evenement[] evenements) {
		this.evenements = evenements;
		for(int e = 0; e < this.evenements.length ;e++) {
			if(this.evenements[e].getName().equals("unmarkedDay")) {
				Vector<Session> sess = new Vector<Session>();
				searchGoodSessionsTime(Integer.parseInt(this.evenements[e].parameter_search("day")) ,Integer.parseInt(this.evenements[e].parameter_search("week")),sess);
			}
			else {System.out.println("Evenement non implémenté ");}
		}
		
	}//FinMethod

	public Evenement[] getEvenements() {
		return this.evenements;
	}//FinMethod
	
}//FinClass
