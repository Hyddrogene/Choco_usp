package Constraint_model.InstanceUTPGraph;

public abstract class CourseElement extends Entity{
	protected int nrChildren;
	public CourseElement(String id, int cpt, String type, Label[] label) {
		super(id, cpt, type, label);
	}//FinMethod

	public abstract Session[] getSession();
    /*public Session[] getSession(){
        if(this.childrens[0].getType() == "session"){
            return (Session[]) this.childrens;
        }
        else{
        	int sizeSessions = 0;
        	for(int i = 0 ;i < this.childrens.length ;i++){
        		sizeSessions += this.childrens[i].getSession().length;
        	}
            Session[] sessions = new Session[];
            for(int i = 0 ;i < this.childrens.length ;i++){
                sessions. = array_merge($sessions,$child->getSession($utp));
            }
            return $sessions;
        }
    }//FinMethod*/
	public int getNrChildren() {
		return nrChildren;
	}
    
}//FinClass
