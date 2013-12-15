
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.*;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;


public class Main {
	public static void main(String[] args) 
	{
		
		String Test= "testcase3";										//Stupid code to print out the class under analysis 
		System.out.println("Testing " + Test);
		
		SootClass c = Scene.v().loadClassAndSupport(Test); 				//load the class 
		c.setApplicationClass(); 
		
		Options.v().setPhaseOption("jb", "use-original-names:true");     //Use same variable names as in test program
		Options.v().set_keep_line_number(true);
		
		List<SootMethod> methods=c.getMethods();
		for(SootMethod m:methods){
			System.out.println("\n\nAnalysis of  "+m.getName()+"()  :");    //Print method name. Just User Friendly way!
			
			HashMap results = new HashMap();                                // Put nodes in data structure "Hash Map" results. Faster Implementation
			Body b = m.retrieveActiveBody();
			UnitGraph g = new ExceptionalUnitGraph(b); 
			
			FaintCodeAnalysis an = new FaintCodeAnalysis(g); 			   //Run the analysis
			
			Iterator sIt = b.getUnits().iterator();							//Get next units corresponding to body from the graph| Use iterator method 
			Unit u=null;													//Initialize units
			
			while( sIt.hasNext() ) {
				u=(Unit)sIt.next();											//Fill up the the units of the graph
				FlowSet FaintVariables = (FlowSet) an.getFlowAfter(u);	
				Iterator variableIt = FaintVariables.iterator();			//Make an iterator on flowset
				
				while( variableIt.hasNext() ) {
					Value variable = (Value)variableIt.next();
					Iterator defBox = u.getDefBoxes().iterator();			//Get all left hand side variables
				
					while (defBox.hasNext()){
						final ValueBox box1 = (ValueBox) defBox.next();
						Value def = box1.getValue();						//put all the left hand side variables in def 
					
						if(variable == def){								//Check between flowset and def

							if(!variable.toString().startsWith("$") && !variable.toString().equals("this")) {  //Parse the line number tag jimple implementation for local and aliased variables
								LineNumberTag tag = (LineNumberTag)u.getTag("LineNumberTag"); 				//get tags|line numbers of the code 
							
								if(results.containsKey(u.toString()))										//containskey is a hashmap function which relates to index
									results.put(u.toString(), results.get(u.toString())+", "+tag);			//get tag if line is mapped to hashmap function
								else
									results.put(u.toString(), tag);

							}

						}

					}
				}



			}

			Set<String> set = results.keySet();
			Iterator<String> i = set.iterator();
			while(i.hasNext()){
				String unit = i.next().toString();
				System.out.println("\nFaint code at line"+" "+results.get(unit));
				System.out.println(unit);
			} 
			}
		}
	}
