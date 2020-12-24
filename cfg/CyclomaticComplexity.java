package ASMBasicBlocks;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

/**
 *
 * @author Essa Imhmed
 */

public class CyclomaticComplexity {
    
	private Map<Integer, ArrayList<Integer>> insnSucc;

	public Map<Integer, ArrayList<Integer>> getInstructionSuccessors() {
		return insnSucc;
	}
	
	CyclomaticComplexity(){
		this.insnSucc = new LinkedHashMap<Integer, ArrayList<Integer>>();
	}
	
        public void getCyclomaticComplexity(String owner, MethodNode mn)
        throws AnalyzerException 
	{ 
		this.insnSucc = new LinkedHashMap<Integer, ArrayList<Integer>>();
		
              Analyzer a = new Analyzer(new BasicInterpreter()) 
		{ 
			protected void newControlFlowEdge(int src, int dst) 
			{ 
				ArrayList<Integer> dsts = insnSucc.get(src);
				if (dsts == null) {
					dsts = new ArrayList<Integer>();
					insnSucc.put(src, dsts);
				}
				dsts.add(dst);
			} 
		}; 	

		a.analyze(owner, mn); 
	}    
}
