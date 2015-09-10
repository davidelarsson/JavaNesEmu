package assembler;

public class Disassembler {

	private int pc;
	/**
	 * In order to make relative addressing mode work, we need a PC in the
	 * contructor, that points to the start of the current instruction being
	 * disassembled.
	 * 
	 * @param pc
	 */
	public Disassembler(int pc) {
		this.pc = pc;

	}
	/**
	 * 
	 * @param bytes
	 *            - a string of at least 3 ints (yes, ints, not bytes!)
	 * @return - an array of two Objects. First an Integer that represents the
	 *         length of the instruction in number of bytes, and the second
	 *         object is a String that is the disassembled code.
	 */
	public Object[] disasm(int[] bytes) {
		String returnString = new String();
		int length = 0;
		int instruction = bytes[0];
		int argument1 = bytes[1];
		int argument2 = bytes[2];

		switch (instruction) {
			case 0x00 :
				returnString = "BRK";
				length = 1;
				break;
			case 0x01 :
				returnString = "ORA " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0x05 :
				returnString = "ORA " + zeroPage(argument1);
				length = 2;
				break;
			case 0x06 :
				returnString = "ASL " + zeroPage(argument1);
				length = 2;
				break;
			case 0x08 :
				returnString = "PHP";
				length = 1;
				break;
			case 0x09 :
				returnString = "ORA " + immediate(argument1);
				length = 2;
				break;
			case 0x0A :
				returnString = "ASL A";
				length = 1;
				break;
			case 0x0D :
				returnString = "ORA " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x0E :
				returnString = "ASL " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x10 :
				returnString = "BPL " + relative(argument1);
				length = 2;
				break;
			case 0x11 :
				returnString = "ORA " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0x15 :
				returnString = "ORA " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x16 :
				returnString = "ASL " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x18 :
				returnString = "CLC";
				length = 1;
				break;
			case 0x19 :
				returnString = "ORA " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0x1D :
				returnString = "ORA " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x1E :
				returnString = "ASL " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x20 :
				returnString = "JSR " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x21 :
				returnString = "AND " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0x24 :
				returnString = "BIT " + zeroPage(argument1);
				length = 2;
				break;
			case 0x25 :
				returnString = "AND " + zeroPage(argument1);
				length = 2;
				break;
			case 0x26 :
				returnString = "ROL " + zeroPage(argument1);
				length = 2;
				break;
			case 0x28 :
				returnString = "PLP";
				length = 1;
				break;
			case 0x29 :
				returnString = "AND " + immediate(argument1);
				length = 2;
				break;
			case 0x2A :
				returnString = "ROL A ";
				length = 1;
				break;
			case 0x2C :
				returnString = "BIT " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x2D :
				returnString = "AND " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x2E :
				returnString = "ROL " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x30 :
				returnString = "BMI " + relative(argument1);
				length = 2;
				break;
			case 0x31 :
				returnString = "AND " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0x35 :
				returnString = "AND " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x36 :
				returnString = "ROL " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x38 :
				returnString = "SEC";
				length = 1;
				break;
			case 0x39 :
				returnString = "AND " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0x3D :
				returnString = "AND " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x3E :
				returnString = "ROL " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x40 :
				returnString = "RTI";
				length = 1;
				break;
			case 0x41 :
				returnString = "EOR " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0x45 :
				returnString = "EOR " + zeroPage(argument1);
				length = 2;
				break;
			case 0x46 :
				returnString = "LSR " + zeroPage(argument1);
				length = 2;
				break;
			case 0x48 :
				returnString = "PHA";
				length = 1;
				break;
			case 0x49 :
				returnString = "EOR " + immediate(argument1);
				length = 2;
				break;
			case 0x4A :
				returnString = "LSR A";
				length = 1;
				break;
			case 0x4C :
				returnString = "JMP " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x4D :
				returnString = "EOR " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x4E :
				returnString = "LSR " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x50 :
				returnString = "BVC " + relative(argument1);
				length = 2;
				break;
			case 0x51 :
				returnString = "EOR " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0x55 :
				returnString = "EOR " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x56 :
				returnString = "LSR " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x58 :
				returnString = "CLI";
				length = 1;
				break;
			case 0x59 :
				returnString = "EOR " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0x5D :
				returnString = "EOR " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x5E :
				returnString = "LSR " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x60 :
				returnString = "RTS";
				length = 1;
				break;
			case 0x61 :
				returnString = "ADC " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0x65 :
				returnString = "ADC " + zeroPage(argument1);
				length = 2;
				break;
			case 0x66 :
				returnString = "ROR " + zeroPage(argument1);
				length = 2;
				break;
			case 0x68 :
				returnString = "PLA";
				length = 1;
				break;
			case 0x69 :
				returnString = "ADC " + immediate(argument1);
				length = 2;
				break;
			case 0x6A :
				returnString = "ROR A";
				length = 1;
				break;
			case 0x6C :
				returnString = "JMP (" + absolute(argument1, argument2) + ")";
				length = 3;
				break;
			case 0x6D :
				returnString = "ADC " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x6E :
				returnString = "ROR " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x70 :
				returnString = "BVS " + relative(argument1);
				length = 2;
				break;
			case 0x71 :
				returnString = "ADC " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0x75 :
				returnString = "ADC " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x76 :
				returnString = "ROR " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x78 :
				returnString = "SEI";
				length = 1;
				break;
			case 0x79 :
				returnString = "ADC " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0x7D :
				returnString = "ADC " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x7E :
				returnString = "ROR " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0x81 :
				returnString = "STA " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0x84 :
				returnString = "STY " + zeroPage(argument1);
				length = 2;
				break;
			case 0x85 :
				returnString = "STA " + zeroPage(argument1);
				length = 2;
				break;
			case 0x86 :
				returnString = "STX " + zeroPage(argument1);
				length = 2;
				break;
			case 0x88 :
				returnString = "DEY";
				length = 1;
				break;
			case 0x8A :
				returnString = "TXA";
				length = 1;
				break;
			case 0x8C :
				returnString = "STY " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x8D :
				returnString = "STA " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x8E :
				returnString = "STX " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0x90 :
				returnString = "BCC " + relative(argument1);
				length = 2;
				break;
			case 0x91 :
				returnString = "STA " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0x94 :
				returnString = "STY " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x95 :
				returnString = "STA " + zeroPageX(argument1);
				length = 2;
				break;
			case 0x96 :
				returnString = "STX " + zeroPageY(argument1);
				length = 2;
				break;
			case 0x98 :
				returnString = "TYA";
				length = 1;
				break;
			case 0x99 :
				returnString = "STA " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0x9A :
				returnString = "TXS";
				length = 1;
				break;
			case 0x9D :
				returnString = "STA " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xA0 :
				returnString = "LDY " + immediate(argument1);
				length = 2;
				break;
			case 0xA1 :
				returnString = "LDA " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0xA2 :
				returnString = "LDX " + immediate(argument1);
				length = 2;
				break;
			case 0xA4 :
				returnString = "LDY " + zeroPage(argument1);
				length = 2;
				break;
			case 0xA6 :
				returnString = "LDX " + zeroPage(argument1);
				length = 2;
				break;
			case 0xA8 :
				returnString = "TAY";
				length = 1;
				break;
			case 0xAA :
				returnString = "TAX";
				length = 1;
				break;
			case 0xA9 :
				returnString = "LDA " + immediate(argument1);
				length = 2;
				break;
			case 0xA5 :
				returnString = "LDA " + zeroPage(argument1);
				length = 2;
				break;
			case 0xAC :
				returnString = "LDY " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xAD :
				returnString = "LDA " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xAE :
				returnString = "LDX " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xB0 :
				returnString = "BCS " + relative(argument1);
				length = 2;
				break;
			case 0xB1 :
				returnString = "LDA " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0xB4 :
				returnString = "LDY " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xB5 :
				returnString = "LDA " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xB6 :
				returnString = "LDX " + zeroPageY(argument1);
				length = 2;
				break;
			case 0xB8 :
				returnString = "CLV";
				length = 1;
				break;
			case 0xB9 :
				returnString = "LDA " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0xBA :
				returnString = "TSX";
				length = 1;
				break;
			case 0xBC :
				returnString = "LDY " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xBD :
				returnString = "LDA " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xBE :
				returnString = "LDX " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0xC0 :
				returnString = "CPY " + immediate(argument1);
				length = 2;
				break;
			case 0xC1 :
				returnString = "CMP " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0xC4 :
				returnString = "CPY " + zeroPage(argument1);
				length = 2;
				break;
			case 0xC5 :
				returnString = "CMP " + zeroPage(argument1);
				length = 2;
				break;
			case 0xC6 :
				returnString = "DEC " + zeroPage(argument1);
				length = 2;
				break;
			case 0xC8 :
				returnString = "INY";
				length = 1;
				break;
			case 0xC9 :
				returnString = "CMP " + immediate(argument1);
				length = 2;
				break;
			case 0xCA :
				returnString = "DEX";
				length = 1;
				break;
			case 0xCC :
				returnString = "CPY " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xCD :
				returnString = "CMP " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xCE :
				returnString = "DEC " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xD0 :
				returnString = "BNE " + relative(argument1);
				length = 2;
				break;
			case 0xD1 :
				returnString = "CMP " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0xD5 :
				returnString = "CMP " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xD6 :
				returnString = "DEC " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xD8 :
				returnString = "CLD";
				length = 1;
				break;
			case 0xD9 :
				returnString = "CMP " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0xDD :
				returnString = "CMP " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xDE :
				returnString = "DEC " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xE0 :
				returnString = "CPX " + immediate(argument1);
				length = 2;
				break;
			case 0xE1 :
				returnString = "SBC " + indexedIndirect(argument1);
				length = 2;
				break;
			case 0xE4 :
				returnString = "CPX " + zeroPage(argument1);
				length = 2;
				break;
			case 0xE5 :
				returnString = "SBC " + zeroPage(argument1);
				length = 2;
				break;
			case 0xE6 :
				returnString = "INC " + zeroPage(argument1);
				length = 2;
				break;
			case 0xE8 :
				returnString = "INX";
				length = 1;
				break;
			case 0xE9 :
				returnString = "SBC " + immediate(argument1);
				length = 2;
				break;
			case 0xEA :
				returnString = "NOP";
				length = 1;
				break;
			case 0xEC :
				returnString = "CPX " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xED :
				returnString = "SBC " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xEE :
				returnString = "INC " + absolute(argument1, argument2);
				length = 3;
				break;
			case 0xF0 :
				returnString = "BEQ " + relative(argument1);
				length = 2;
				break;
			case 0xF1 :
				returnString = "SBC " + indirectIndexed(argument1);
				length = 2;
				break;
			case 0xF5 :
				returnString = "SBC " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xF6 :
				returnString = "INC " + zeroPageX(argument1);
				length = 2;
				break;
			case 0xF8 :
				returnString = "SED";
				length = 1;
				break;
			case 0xF9 :
				returnString = "SBC " + absoluteY(argument1, argument2);
				length = 3;
				break;
			case 0xFD :
				returnString = "SBC " + absoluteX(argument1, argument2);
				length = 3;
				break;
			case 0xFE :
				returnString = "INC " + absoluteX(argument1, argument2);
				length = 3;
				break;
			default :
				returnString = "Illegal!";
				length = 1;
		}

		Object returnArray[] = new Object[2];
		returnArray[0] = new Integer(length);
		returnArray[1] = returnString;

		return returnArray;
	}

	private String immediate(int argument) {
		return String.format("#$%02X", argument);
	}

	private String zeroPage(int argument) {
		return String.format("$%02X", argument);
	}

	private String zeroPageX(int argument) {
		return String.format("$%02X,X", argument);
	}

	private String zeroPageY(int argument) {
		return String.format("$%02X,Y", argument);
	}

	private String absolute(int argument2, int argument1) {
		return String.format("$%02X%02X", argument1, argument2);
	}

	private String absoluteX(int argument1, int argument2) {
		return String.format("$%02X%02X,X", argument2, argument1);
	}

	private String absoluteY(int argument1, int argument2) {
		return String.format("$%02X%02X,Y", argument2, argument1);
	}

	private String indexedIndirect(int argument) {
		return String.format("($%02X,X)", argument);
	}

	private String indirectIndexed(int argument) {
		return String.format("($%02X),Y", argument);
	}

	private String relative(int argument) {
		if ((argument & 0x80) != 0)
			argument -= 0x100;
		pc += 2;
		return String.format("$%02X", ((pc + argument) & 0x0000ffff));
	}

}
