package assembler;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Assembler asm = new Assembler("lda $4012");
		
		System.out.println("Assembly " + (asm.isSuccess() ? "" : "not ") + "successful");
		System.out.println("Instruction is:  " + asm.getInstructionStr());
		System.out.println("Operand is:      " + asm.getOperandStr());
		System.out.println("Addressing mode: " + asm.getAddressingMode());
		System.out.println(String.format("Opcode is:     0x%02X", asm.getOpcode()));
		System.out.println(String.format("Argument 1 is: 0x%02X", asm.getArg1()));
		System.out.println(String.format("Argument 2 is: 0x%02X", asm.getArg2()));
		int i = 47;
		i = i / 2;
		i = i % 2;
		System.out.println("i: " + i);
	}

}
