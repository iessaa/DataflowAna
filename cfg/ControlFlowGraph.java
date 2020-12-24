package ASMBasicBlocks;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
/**
 *
 * @author Essa Imhmed
 */
public class ControlFlowGraph {
	/* a class to hold basic block info */
	private class BasicBlock {
		private Integer bBlockNo;				/* Basic block number */
		private List<Integer> bBlockInsn;		/* Basic block instructions */
		private List<BasicBlock> bBlockSucc;	/* Basic block successors */
		
		BasicBlock(Integer bBlockNo, Integer insn) {
	    	this.bBlockNo = bBlockNo;
	    	this.bBlockInsn = new ArrayList<Integer>();
	    	this.bBlockSucc = new ArrayList<BasicBlock>();
	    }
	}
	
	private Map<Integer, BasicBlock> vBlocks;	/* used by depthFirstSearch() to hold visited basic blocks */
    private List<BasicBlock> mBlocks;			/* list of all basic blocks in current method */
    private Integer bBlockSize;  				/* number of basic blocks in current method */
    private Map<Integer, String> insnOpcode;	/* list of all method instructions and their Opcodes */
	private Map<Integer, ArrayList<Integer>> insnSucc;	/* graph of method control flow */
	private Map<Integer, List<Integer>> insnPred;		/* reverse graph of method control flow */
	
	ControlFlowGraph(CyclomaticComplexity cc, MethodNode mn) {
		/* Initialize class fields */
		int idx = 0;
	    this.vBlocks = new LinkedHashMap<Integer, BasicBlock>();
	    this.mBlocks = new ArrayList<BasicBlock>();
	    this.bBlockSize = 0;
	    /* access graph of method control flow */
	    this.insnSucc = cc.getInstructionSuccessors();
	    /* create a reverse graph of method control flow */
	    this.insnPred = new LinkedHashMap<Integer, List<Integer>>();
	    for (Entry<Integer, ArrayList<Integer>> entry: this.insnSucc.entrySet()) {
	    	for (Integer insn : entry.getValue()) {
	    		if (!this.insnPred.containsKey(insn)) {
	    			this.insnPred.put(insn, new ArrayList<Integer>());
	    		}
	            this.insnPred.get(insn).add(entry.getKey());
	    	}
	    }
	        
	    this.insnOpcode = new LinkedHashMap<Integer, String>();
	    for (AbstractInsnNode in:  mn.instructions.toArray()) {
	    	if (in.getOpcode() >= 0) {
	    		this.insnOpcode.put(idx, Printer.OPCODES[in.getOpcode()]);
	    	}
	    	idx++;	
	    }
	}	
	    
	public void printBasicBlock() {
		
		/* generate basic blocks using depth first search */
		depthFirstSearch(0);
		/* loop over the basic blocks of method and print them 
		 * in certain format as required in the assignment
		 */
		for (BasicBlock bBlock: mBlocks) {
	  		if (bBlock != null) {
	  			/* print the basic block instructions */
	  			StringBuilder insnToStr = new StringBuilder();
	  			for (Integer insn: bBlock.bBlockInsn) {
	  				String Opcode = this.insnOpcode.get(insn); 
	  				if (Opcode != null) {
	  					insnToStr.append(bBlock.bBlockNo + ":  " + Opcode + "\n");
	  				}
	  			}
	  			if (insnToStr.length() > 0)
	  				System.out.println(insnToStr.toString());
	  			/* print the basic block successors */
	  			StringBuilder succToStr = new StringBuilder();
	  			succToStr.append("(succ: ");
	  			for (BasicBlock bSucc: bBlock.bBlockSucc) {
	  				if (bSucc != null) {
	  					succToStr.append(bSucc.bBlockNo);
	  					succToStr.append(",");
	  				}
	  			}
	  			if (succToStr.length() > 7) {
	  				succToStr.setLength(succToStr.length() - 1);
	  				succToStr.append(")\n");
	  				System.out.println(succToStr.toString());
	  			}
	  		}
	  	}
	}
 
	private BasicBlock depthFirstSearch(int insn) {
		
		BasicBlock bBlock = vBlocks.get(insn);
        if (bBlock == null) {
        	/* Create it a new basic block and let this instruction 
        	 * to be first instruction in the new block 
        	 */
        	bBlock = new BasicBlock(++bBlockSize, insn);
        	vBlocks.put(insn, bBlock);
        	mBlocks.add(bBlock);
	        while (insnSucc.get(insn) != null && insnSucc.get(insn).size() == 1) {
	        	insn = insnSucc.get(insn).get(0);
	            if (insnPred.get(insn) != null && insnPred.get(insn).size() != 1) {
	            	/* This could be a branch instruction.
	        		 * So, traverse it if possible
	        		 */
	            	bBlock.bBlockSucc.add(depthFirstSearch(insn));
	                break;
	            } else {
	            	/* Add current insn to current basic block */
	            	bBlock.bBlockInsn.add(insn);
	            	vBlocks.put(insn, bBlock);            
	            }
	        }
	        
	        if (insnSucc.get(insn) != null) {
	        	/* This could be a branch instruction.
        		 * So, traverse each of its successors if possible
        		 */
	        	for (int i = 0; i < insnSucc.get(insn).size(); i++) {
	        		if (!vBlocks.containsKey(insnSucc.get(insn).get(i))) {
	        			bBlock.bBlockSucc.add(depthFirstSearch(insnSucc.get(insn).get(i)));
	                }
	            }
	        }
        }
        return bBlock;
    }
    
	
 }

