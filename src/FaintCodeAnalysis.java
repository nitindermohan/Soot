
import java.util.Iterator;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;


public  class FaintCodeAnalysis extends BackwardFlowAnalysis
	{
	
	private FlowSet localVariables; 
	
		
	public	FaintCodeAnalysis (UnitGraph g)
	{
	  super(g);
	  
	    Chain locs=g.getBody().getLocals();
		localVariables=new ArraySparseSet();
		Iterator it=locs.iterator();
		while(it.hasNext()) {
		    Local loc=(Local) it.next();
		    localVariables.add(loc);
		}
	    
	  doAnalysis();

	}
	
	protected Object newInitialFlow()		// Used to initialize the in and out sets for each node. In our case, all variables are considered to be faint
	{
		FlowSet ret=new ArraySparseSet();		//Type of flowset
		localVariables.copy(ret);
		return ret;
	
	}
	

	protected Object entryInitialFlow() {	//Returns a flow set representing the initial set of the entry node
		FlowSet ret=new ArraySparseSet();
		localVariables.copy(ret);			//Will be same as newInitialFlow
		return ret;
	}
	
	
	protected void merge(Object in1, Object in2, Object out)
	{
		FlowSet inSet1 = (FlowSet) in1,
				inSet2 = (FlowSet) in2,
		        outSet = (FlowSet) out;
		inSet1.intersection(inSet2, outSet);
	}
	
	
	protected void copy(Object source, Object dest)
	{
	FlowSet sourceSet = (FlowSet) source,
	destSet = (FlowSet) dest;
	sourceSet.copy(destSet);
	}
    
	       
    protected void flowThrough(Object outValue, Object unit,Object inValue)	{
		FlowSet in  = (FlowSet) inValue,
				out = (FlowSet) outValue;
		Stmt    s   = (Stmt)    unit;
		out.copy( in );
		Iterator useBox = s.getUseBoxes().iterator(),  //Right hand side variables
				 defBox = s.getDefBoxes().iterator();  //Left hand side variables
		boolean flag = true;
		
		if(s instanceof JInvokeStmt){       //Nested if-else of Kill functions 
			
			while (useBox.hasNext()){       //Const Kill
				final ValueBox box1 = (ValueBox) useBox.next();
				Value use = box1.getValue();
				if(use instanceof Local){
					in.remove(use);
			}

			}

		}

		else if(s instanceof AssignStmt){   //Dep Kill
			while (defBox.hasNext()){

				final ValueBox box = (ValueBox) defBox.next();
				Value def = box.getValue();
				if(def instanceof Local){
					if(in.contains(def)){   //Checks if the left hand side variable belongs in the In set
						flag=false;
					}
				}	
			}
			if(flag){   //Do not run this if the variable is in the In set
				while (useBox.hasNext()){   //get all the right hand side variables
					final ValueBox box1 = (ValueBox) useBox.next();
					Value use = box1.getValue();
					if(use instanceof Local){
						in.remove(use);

					}

				}
			}
			
		}
		
		else{ 									//for all other types of statements, kill the right hand side variables
			while (useBox.hasNext()){
				final ValueBox box1 = (ValueBox) useBox.next();
				Value use = box1.getValue();
				if(use instanceof Local){
					in.remove(use);


				}
			}
		}
			
		if(s instanceof AssignStmt){    //Now on to Generate function
		    Boolean flag2=true;
			while (defBox.hasNext()){   //ConstGen

				final ValueBox box = (ValueBox) defBox.next();
				Value def = box.getValue();  //Put all left hand variables to valuebox
				if(def instanceof Local){
					while (useBox.hasNext()){
						final ValueBox box1 = (ValueBox) useBox.next();
						Value use = box1.getValue();  //Put all right hand side variables to valuebox
						if(use instanceof Local){
							if(use==def){           //statements like x=x+1
								flag2=false;       //Don't add anything
							}


						}
					}
					
					if(flag2){                  //If left hand side variable is not available in the Operand section
						in.add(def);			   //Generate it in InSet!!!	
					}
					}	
				
			}



		}

		

	}
    
	
	
}




