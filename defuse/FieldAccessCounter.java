

/*
 * Essa Imhmed. Aggie ID: 800589512
 * the program was written using Eclipse as java IDE with JavaSE 1.7
 * and  java ASM-4.2 library. to run the program from terminal, you need to include
 * the mentioned library during compiling and running the classes
 *from the terminal using Javac and java commands.
 */

package cs581;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FieldAccessCounter {
	public static void main(String[] args) throws IOException {
		for (String arg : args) {
			if (arg.endsWith(".class")) {
				ASMVisitor asm = new ASMVisitor(arg);
				int totalRead=0;
				int totalWrite=0;
				for (Map.Entry<String, List<Integer>> entry: asm.GetFieldsCounters()) {
					int read = entry.getValue().get(0) + entry.getValue().get(2);
					int write = entry.getValue().get(1) + entry.getValue().get(3);
					/*
					 * Printing each object data field, with the name,
					 * the number of reads (getfield), the number of writes (putfield),
					 * and the total accesses (sum of reads and writes)
					 */
					System.out.println( "\t"+entry.getKey()+
										"\t"+read+
										"\t"+write+
										"\t"+(read+write));
					totalRead+= read;
					totalWrite+=write;
				}
				/*
				 * printing the total number of reads, writes
				 * , and all accesses for all data fields.
				 */
				System.out.println("\t" + "total"+
									"\t" + totalRead +
									"\t" + totalWrite +
									"\t" + (totalRead+totalWrite));
			}
		}
	}
}
