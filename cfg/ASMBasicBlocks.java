package ASMBasicBlocks;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
/**
 *
 * @author Essa Imhmed
 */
public class ASMBasicBlocks {

	public static void main(String[] args) throws IOException, AnalyzerException  
    {
		if(args.length <= 0) {
			System.out.println("Please supply Enter your class file name");
			return;
		} else if(!args[0].endsWith(".class")) { 
			System.out.println("Please supply your class file name");
			return; 
		}
  
		FileInputStream TheClass = new FileInputStream(args[0].toString());
        ClassNode cn = new ClassNode();
        ClassReader cr = new ClassReader(TheClass);
        cr.accept(cn, 0);

        Iterator<MethodNode> i = cn.methods.iterator();
        while (i.hasNext()) {
        	MethodNode mn = i.next();
        	CyclomaticComplexity cc = new CyclomaticComplexity();
        	cc.getCyclomaticComplexity(cn.name, mn);   
        	ControlFlowGraph cfg = new ControlFlowGraph(cc, mn);
        	System.out.print('\n'+ "Analyzing Method: " + mn.name + "()\n");
        	cfg.printBasicBlock(); 
        }
    }  
}