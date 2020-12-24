package cs581;

/*
 * Essa Imhmed. Aggie ID: 800589512
 */



/*
 * each elewment in the hashmap represents a class field with its four values,
 *  which indicates to the instructions, getstatic, putstatic, getfield, and putfield.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMVisitor {
	/*
	 * declaring a hashmap variable, fieldACounter,  to keep track
	 * object data field accessed in the class
	 */
	private Map<String, List<Integer>> fieldACounter;

	ASMVisitor(String cname) throws IOException {
		/*
		 * reading the bytecode of the passed in class.
		 */
		FileInputStream cin = new FileInputStream(cname);
        ClassReader cr = new ClassReader(cin);
        fieldACounter = new  HashMap<String, List<Integer>>();
        cr.accept(cv, 0);
	}

	ClassVisitor cv = new ClassVisitor(Opcodes.ASM4) {
		/*
         * When a field is encountered.
         */
        @Override
		public FieldVisitor visitField  (int access, String name,
				String desc, String signature, Object value) {

        	/*
        	 * register the encountered class fields in
        	 *  the fieldACounter and initialize the field
        	 *  counters to zeros
        	 */
        	fieldACounter.put(name,  Arrays.asList(0, 0, 0, 0));
			return null;
        }
		/*
         * When a method is encountered
         */
        @Override
        public MethodVisitor visitMethod(int access, String name,
                String desc, String signature, String[] exceptions) {
            return mv;
        }

        public MethodVisitor mv = new MethodVisitor(Opcodes.ASM4) {
        	/*
        	 *when a field instruction is encountered
        	 */
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {

            	/*
            	 * ignore non-registered class field in the fieldACounter.
            	 */
            	if ((opcode >= Opcodes.GETSTATIC && opcode <= Opcodes.PUTFIELD) &&
            		(fieldACounter.containsKey(name))) {
            		/*
            		 * Incrementing the counter of each class field accessed's instruction
            		 * such GetStatic, PutStatic, GetField, and PutField
            		 * by 1 when it is captured.
            		 */
            		int idx = opcode - Opcodes.GETSTATIC;
            		Integer val = fieldACounter.get(name).get(idx);
            		fieldACounter.get(name).set(idx, val+1);
            	}

            	super.visitFieldInsn(opcode, owner, name, desc);
            	return;
            }

        };

	};

	public Set<Map.Entry<String, List<Integer>>> GetFieldsCounters() {
		return this.fieldACounter.entrySet();
	}
}