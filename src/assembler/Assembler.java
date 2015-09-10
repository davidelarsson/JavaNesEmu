package assembler;

public class Assembler {
	private int opcode = -1;
	private int arg1 = -1;
	private int arg2 = -1;
	private String instructionStr = "";
	private String operandStr = "";
	private enum AddressingMode {
		IMPLIED, IMMEDIATE, ZEROPAGE, RELATIVE, ZEROPAGEX, ZEROPAGEY, ABSOLUTE, ABSOLUTEX, ABSOLUTEY, ACCUMULATOR, INDEXEDINDIRECT, INDIRECTINDEXED, INDIRECT
	}
	private AddressingMode addressingMode;
	private boolean success = false;

	/**
	 * Assembles a String of 6502 assembly code.
	 * 
	 * @param input
	 *            String of code
	 * 
	 *            To get the opcode and the argument use getOpcode() and
	 *            getArg(), respectively
	 */
	public Assembler(String input) {
		System.out.println("Analyzing the string: \"" + input + "\"");
		try {
			// First, check if the line _starts_ with a semicolon, if so, it's a
			// comment, and everything can be ignored
			if (input.startsWith(";")) {
				throw new Exception("comment");
			}

			// Check if there's a semicolon somewhere, if so, remove it and
			// everything afterwards
			if (input.contains(";")) {
				int index = input.indexOf(";");
				input = input.substring(0, index);
			}

			input = input.trim();

			// Truncate the string, by removing all whitespaces.
			input = input.replaceAll("\\s", "");

			// Make sure all characters are upper case
			input = input.toUpperCase();

			/*
			 * Get the first three character of the string, which (presumably)
			 * is the instruction
			 */
			if (input.length() < 3) {
				throw new Exception("Too short an instruction: " + input);
			}
			instructionStr = input.substring(0, 3);

			// The operand string is something we analyze later.
			operandStr = input.substring(3);

			// Now, check which instruction we have!
			if (instructionStr.equals("BRK"))
				instructionBRK();
			else if (instructionStr.equals("TAY"))
				instructionTAY();
			else if (instructionStr.equals("TAX"))
				instructionTAX();
			else if (instructionStr.equals("TSX"))
				instructionTSX();
			else if (instructionStr.equals("TYA"))
				instructionTYA();
			else if (instructionStr.equals("TXA"))
				instructionTXA();
			else if (instructionStr.equals("TXS"))
				instructionTXS();
			else if (instructionStr.equals("LDA"))
				instructionLDA();
			else if (instructionStr.equals("LDX"))
				instructionLDX();
			else if (instructionStr.equals("LDY"))
				instructionLDY();
			else if (instructionStr.equals("STA"))
				instructionSTA();
			else if (instructionStr.equals("STX"))
				instructionSTX();
			else if (instructionStr.equals("STY"))
				instructionSTY();
			else if (instructionStr.equals("PHA"))
				instructionPHA();
			else if (instructionStr.equals("PHP"))
				instructionPHP();
			else if (instructionStr.equals("PLA"))
				instructionPLA();
			else if (instructionStr.equals("PLP"))
				instructionPLP();
			else if (instructionStr.equals("ADC"))
				instructionADC();
			else if (instructionStr.equals("SBC"))
				instructionSBC();
			else if (instructionStr.equals("AND"))
				instructionAND();
			else if (instructionStr.equals("EOR"))
				instructionEOR();
			else if (instructionStr.equals("ORA"))
				instructionORA();
			else if (instructionStr.equals("CMP"))
				instructionCMP();
			else if (instructionStr.equals("CPX"))
				instructionCPX();
			else if (instructionStr.equals("CPY"))
				instructionCPY();
			else if (instructionStr.equals("BIT"))
				instructionBIT();
			else if (instructionStr.equals("INC"))
				instructionINC();
			else if (instructionStr.equals("INX"))
				instructionINX();
			else if (instructionStr.equals("INY"))
				instructionINY();
			else if (instructionStr.equals("DEC"))
				instructionDEC();
			else if (instructionStr.equals("DEX"))
				instructionDEX();
			else if (instructionStr.equals("DEY"))
				instructionDEY();
			else if (instructionStr.equals("ASL"))
				instructionASL();
			else if (instructionStr.equals("LSR"))
				instructionLSR();
			else if (instructionStr.equals("ROL"))
				instructionROL();
			else if (instructionStr.equals("ROR"))
				instructionROR();
			else if (instructionStr.equals("JMP"))
				instructionJMP();
			else if (instructionStr.equals("JSR"))
				instructionJSR();
			else if (instructionStr.equals("RTI"))
				instructionRTI();
			else if (instructionStr.equals("RTS"))
				instructionRTS();
			else if (instructionStr.equals("BPL"))
				instructionBPL();
			else if (instructionStr.equals("BMI"))
				instructionBMI();
			else if (instructionStr.equals("BVC"))
				instructionBVC();
			else if (instructionStr.equals("BVS"))
				instructionBVS();
			else if (instructionStr.equals("BCC"))
				instructionBCC();
			else if (instructionStr.equals("BCS"))
				instructionBCS();
			else if (instructionStr.equals("BNE"))
				instructionBNE();
			else if (instructionStr.equals("BEQ"))
				instructionBEQ();
			else if (instructionStr.equals("CLC"))
				instructionCLC();
			else if (instructionStr.equals("CLI"))
				instructionCLI();
			else if (instructionStr.equals("CLD"))
				instructionCLD();
			else if (instructionStr.equals("CLV"))
				instructionCLV();
			else if (instructionStr.equals("SEC"))
				instructionSEC();
			else if (instructionStr.equals("SEI"))
				instructionSEI();
			else if (instructionStr.equals("SED"))
				instructionSED();
			else if (instructionStr.equals("NOP"))
				instructionNOP();
			else
				throw new Exception("Invalid instruction: " + instructionStr);

		} catch (Exception e) {
			if (e.getMessage().equals("comment")) {
				instructionStr = "comment";
				success = true;
			} else {
				System.err.println("Syntax error!\n" + e);
				somethingWentWrong();
			}
		}
	}

	private void somethingWentWrong() {
		// Something went wrong, reset all return values
		opcode = -1;
		arg1 = -1;
		arg2 = -1;
		instructionStr = null;
		operandStr = null;
		addressingMode = null;

	}

	/*
	 * Interpret what addressing mode was meant by lookint at the operand (this
	 * is the heavy stuff!)
	 */
	private void decodeAddressingMode() {
		try {
			String tmp = operandStr;

			/*
			 * First, check whether the operand starts with a '#'. If so, we
			 * know righ away that it's immediate addressing mode
			 */
			if (tmp.startsWith("#")) {
				addressingMode = AddressingMode.IMMEDIATE;

				// Skip the '#' sign
				tmp = tmp.substring(1);

				// If our argument starts with '$', interpret value in hex
				if (tmp.startsWith("$")) {
					if (tmp.substring(1).length() < 2)
						arg1 = Integer.parseInt(tmp.substring(1, 2), 16);
					else
						arg1 = Integer.parseInt(tmp.substring(1, 3), 16);
					if (tmp.length() > 3)
						throw new Exception(
								"Immediate value with trail characters: "
										+ tmp.substring(3));
				}
				// Our argument did not start witn '$', it's a decimal value
				else {
					if (tmp.substring(0).length() < 2)
						arg1 = Integer.parseInt(tmp.substring(0, 1), 10);
					else
						arg1 = Integer.parseInt(tmp.substring(0, 2));
					if (tmp.length() > 2)
						throw new Exception(
								"Immediate value with trail characters: "
										+ tmp.substring(2));
				}

			}

			/*
			 * If it starts with an 'A', we have accumulator addressing mode!
			 */
			else if (operandStr.startsWith("A")) {
				addressingMode = AddressingMode.ACCUMULATOR;
				String str = operandStr;
				if (str.length() > 1)
					throw new Exception(
							"Accumulator addressing mode with trail characters: "
									+ str);
			}

			/*
			 * If it starts and ends with parentheses, and contains an 'X', we
			 * know it's indexed indirect addressing mode
			 */
			else if (operandStr.startsWith("(") && operandStr.endsWith(")")
					&& operandStr.contains("X")) {
				addressingMode = AddressingMode.INDEXEDINDIRECT;

				// Pass by the first parenthesis
				tmp = tmp.substring(1);

				// If it starts with '$', interpret as hex
				if (tmp.startsWith("$")) {
					// It's only one digit
					if (tmp.charAt(2) == ',') {
						arg1 = Integer.parseInt(tmp.substring(1, 2), 16);
						// If it doesn't end with ',X)', or if it's longer than
						// six characters, something is wrong!
						if (!tmp.endsWith(",X)") || tmp.length() > 6)
							throw new Exception(
									"Badly formatted indexed indirect addressing mode?");
					}
					// It's two digits
					else
						arg1 = Integer.parseInt(tmp.substring(1, 3), 16);
					// If it doesn't end with ',X)', or if it's longer than
					// seven characters, something is wrong!
					if (!tmp.endsWith(",X)") || tmp.length() > 7)
						throw new Exception(
								"Badly formatted indexed indirect addressing mode?");
				}
				// Our argument did not start witn '$', it's a decimal value
				else {
					// It's only one digit
					if (tmp.charAt(1) == ',') {
						arg1 = Integer.parseInt(tmp.substring(0, 1), 10);
						// If it doesn't end with ',X)', or if it's longer than
						// six characters, something is wrong!
						if (!tmp.endsWith(",X)") || tmp.length() > 5)
							throw new Exception(
									"Badly formatted indexed indirect addressing mode?");
					}
					// It's two digits
					else
						arg1 = Integer.parseInt(tmp.substring(0, 2), 10);
					// If it doesn't end with ',X)', or if it's longer than
					// seven characters, something is wrong!
					if (!tmp.endsWith(",X)") || tmp.length() > 6)
						throw new Exception(
								"Badly formatted indexed indirect addressing mode?");
				}

			}

			/*
			 * If it starts with a parenthesis, contains, but not ends with a
			 * parenthesis, and contains an 'Y', we know it's indexed indirect
			 * addressing mode
			 */
			else if (operandStr.startsWith("(") && operandStr.contains("Y")
					&& operandStr.contains(")") && !operandStr.endsWith(")")) {
				addressingMode = AddressingMode.INDIRECTINDEXED;
				// Pass by the first parenthesis
				tmp = tmp.substring(1);

				// If it starts with '$', interpret as hex
				if (tmp.startsWith("$")) {
					// It's only one digit
					if (tmp.charAt(2) == ')') {
						arg1 = Integer.parseInt(tmp.substring(1, 2), 16);
						// If it doesn't end with '),Y', or if it's longer than
						// six characters, something is wrong!
						if (!tmp.endsWith("),Y") || tmp.length() > 6)
							throw new Exception(
									"Badly formatted indirect indexed addressing mode?");
					}
					// It's two digits
					else
						arg1 = Integer.parseInt(tmp.substring(1, 3), 16);
					// If it doesn't end with '),Y', or if it's longer than
					// seven characters, something is wrong!
					if (!tmp.endsWith("),Y") || tmp.length() > 7)
						throw new Exception(
								"Badly formatted indirect indexed addressing mode?");
				}
				// Our argument did not start witn '$', it's a decimal value
				else {
					// It's only one digit
					if (tmp.charAt(1) == ')') {
						arg1 = Integer.parseInt(tmp.substring(0, 1), 10);
						// If it doesn't end with ',X)', or if it's longer than
						// six characters, something is wrong!
						if (!tmp.endsWith(",X)") || tmp.length() > 5)
							throw new Exception(
									"Badly formatted indexed indirect addressing mode?");
					}
					// It's two digits
					else
						arg1 = Integer.parseInt(tmp.substring(0, 2), 10);
					// If it doesn't end with ',X)', or if it's longer than
					// seven characters, something is wrong!
					if (!tmp.endsWith(",X)") || tmp.length() > 6)
						throw new Exception(
								"Badly formatted indexed indirect addressing mode?");
				}
			}

			/*
			 * If it starts and ends with parentheses, but does not contain an
			 * 'X' we know it's indirect mode
			 */
			else if (operandStr.startsWith("(") && operandStr.endsWith(")")
					&& !operandStr.contains("X")) {
				addressingMode = AddressingMode.INDIRECT;

				// Now, remove the parantheses
				tmp = operandStr.substring(1);
				tmp = tmp.substring(0, tmp.length() - 1);

				// If it starts with '$', interpret as hex
				if (tmp.startsWith("$")) {
					tmp = tmp.substring(1);
					if (tmp.length() > 4) {
						throw new Exception(
								"Badly formatted indirect indexed addressing mode?");
					}
					int address = Integer.parseInt(tmp, 16);
					arg1 = address & 0x00ff;
					arg2 = address >> 8;

				}
				// Our argument did not start witn '$', it's a decimal value
				else {
					// Remember, that in decimal form, 0xffff is five digits
					// (65535)
					if (tmp.length() > 5) {
						throw new Exception(
								"Badly formatted indirect indexed addressing mode?");
					}
					int address = Integer.parseInt(tmp, 10);
					if (address > 0xffff)
						throw new Exception("Out of bounds: " + address);
					arg1 = address & 0x00ff;
					arg2 = address >> 8;
				}

			}

			/*
			 * If it ends with ",X", we have either absoluteX or zeroPageX
			 * addressing mode. We determine which by the number of digits of
			 * the address in hex. To force absolute mode, even when addressing
			 * zero page, use hex notation with at least three digits.
			 */
			else if (operandStr.endsWith(",X")) {
				// Rid the ",X"
				int index = operandStr.indexOf(',');
				tmp = operandStr.substring(0, index);

				// If it starts with a '$', it's a hex value.
				if (tmp.startsWith("$")) {
					tmp = tmp.substring(1);
					// If the length of the value is at least three digits, we
					// use absolute addressing mode.
					int address = Integer.parseInt(tmp, 16);
					if (tmp.length() >= 3) {
						addressingMode = AddressingMode.ABSOLUTEX;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGEX;
						arg1 = address & 0x00ff;
					}

				} else {
					// No '$', interpret as decimal.
					int address = Integer.parseInt(tmp);
					if (address > 0xff) {
						addressingMode = AddressingMode.ABSOLUTEX;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGEX;
						arg1 = address & 0x00ff;
					}
				}

			}

			/*
			 * If it ends with ",Y", we have either absoluteY or zeroPageY
			 * addressing mode. We determine which by the number of digits of
			 * the address in hex. To force absolute mode, even when addressing
			 * zero page, use hex notation with at least three digits.
			 */
			else if (operandStr.endsWith(",Y")) {
				// Rid the ",Y"
				int index = operandStr.indexOf(',');
				tmp = operandStr.substring(0, index);

				// If it starts with a '$', it's a hex value.
				if (tmp.startsWith("$")) {
					tmp = tmp.substring(1);
					// If the length of the value is at least three digits, we
					// use absolute addressing mode.
					int address = Integer.parseInt(tmp, 16);
					if (tmp.length() >= 3) {
						addressingMode = AddressingMode.ABSOLUTEY;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGEY;
						arg1 = address & 0x00ff;
					}

				} else {
					// No '$', interpret as decimal.
					int address = Integer.parseInt(tmp);
					if (address > 0xff) {
						addressingMode = AddressingMode.ABSOLUTEY;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGEY;
						arg1 = address & 0x00ff;
					}
				}

			}

			/*
			 * The only condition we have left, is to make sure that it's an
			 * interpretable number as argument. If so, it's either absolute or
			 * zeroPage addressing mode. We determine which by the number of
			 * digits of the address in hex. To force absolute mode, even when
			 * addressing zero page, use hex notation with at least three
			 * digits.
			 */
			else if (operandIsAbsolute(operandStr)) {
				tmp = operandStr;
				if (tmp.startsWith("$")) {
					tmp = tmp.substring(1);
					// If the length of the value is at least three digits, we
					// use absolute addressing mode.
					int address = Integer.parseInt(tmp, 16);
					if (tmp.length() >= 3) {
						addressingMode = AddressingMode.ABSOLUTE;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGE;
						arg1 = address & 0x00ff;
					}

				} else {
					// No '$', interpret as decimal.
					int address = Integer.parseInt(tmp);
					if (address > 0xff) {
						addressingMode = AddressingMode.ABSOLUTE;
						arg1 = address & 0x00ff;
						arg2 = address >> 8;
					} else {
						addressingMode = AddressingMode.ZEROPAGE;
						arg1 = address & 0x00ff;
					}

				}

			}

			else {
				throw new Exception("Uninterpretable addressing mode: "
						+ operandStr);
			}

		} catch (Exception e) {
			System.err.println("Operand syntax error!\n" + e);
			somethingWentWrong();

		}

	}

	/*
	 * Yeah, yeah. I know, it's ugly to make a whole method determine whether
	 * the operand is an absolute value. But it works.
	 */
	public boolean operandIsAbsolute(String str) {
		if (str.startsWith("$"))
			str = str.substring(2);
		char ch = 0;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			int val = Character.digit(ch, 16);
			if ((16 < val) || val < 0)
				return false;
		}
		return true;

	}
	private void instructionTAY() {
		if (operandStr.length() != 0) {
			System.out.println("TAY doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xA8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionTAX() {
		if (operandStr.length() != 0) {
			System.out.println("TAX doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xAA;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionTSX() {
		if (operandStr.length() != 0) {
			System.out.println("TSX doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xBA;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionTYA() {
		if (operandStr.length() != 0) {
			System.out.println("TYA doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x98;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionTXA() {
		if (operandStr.length() != 0) {
			System.out.println("TXA doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x8A;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionTXS() {
		if (operandStr.length() != 0) {
			System.out.println("TXS doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x9A;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionLDA() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xA9;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xA5;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xB5;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0xB9;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xAD;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xBD;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0xB9;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0xA1;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0xB1;
		} else {
			System.out.println("Illegal addressing mode for LDA: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionLDX() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xA2;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xA6;
		} else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			opcode = 0xB6;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xAE;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0xBE;
		} else {
			System.out.println("Illegal addressing mode for LDX: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionLDY() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xA0;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xA4;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xB4;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xAC;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xBC;
		} else {
			System.out.println("Illegal addressing mode for LDY: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionSTA() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x85;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x95;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0x99;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x8D;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x9D;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0x99;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0x81;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0x91;
		} else {
			System.out.println("Illegal addressing mode for STA: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionSTX() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x86;
		} else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			opcode = 0x96;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x8E;
		} else {
			System.out.println("Illegal addressing mode for STX: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionSTY() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x84;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x94;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x8C;
		} else {
			System.out.println("Illegal addressing mode for STY: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionPHA() {
		if (operandStr.length() != 0) {
			System.out.println("PHA doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x48;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionPHP() {
		if (operandStr.length() != 0) {
			System.out.println("PHP doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x08;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionPLA() {
		if (operandStr.length() != 0) {
			System.out.println("PLA doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x68;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionPLP() {
		if (operandStr.length() != 0) {
			System.out.println("PLP doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x28;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionADC() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0x69;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x65;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x75;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0x79;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x6D;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x7D;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0x79;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0x61;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0x71;
		} else {
			System.out.println("Illegal addressing mode for ADC: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionSBC() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xE9;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xE5;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xF5;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0xF9;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xED;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xFD;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0xF9;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0xE1;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0xF1;
		} else {
			System.out.println("Illegal addressing mode for SBC: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionAND() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0x29;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x25;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x35;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0x39;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x2D;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x3D;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0x39;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0x21;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0x31;
		} else {
			System.out.println("Illegal addressing mode for AND: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionEOR() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0x49;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x45;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x55;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0x59;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x4D;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x5D;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0x59;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0x41;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0x51;
		} else {
			System.out.println("Illegal addressing mode for EOR: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionORA() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0x09;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x05;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x15;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0x19;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x0D;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x1D;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0x19;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0x01;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0x11;
		} else {
			System.out.println("Illegal addressing mode for ORA: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionCMP() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xC9;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xC5;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xD5;
		}
		/*
		 * Wait, what?! -Oh yeah, motherfuckers! This instruction doesn't exist,
		 * so we cheat, and use absolute addressing mode instead! Clever, huh?
		 */
		else if (addressingMode == AddressingMode.ZEROPAGEY) {
			success = true;
			arg2 = 0x00;
			opcode = 0xD9;
			// Yeah, this is safe, nothing changed down the loop.
			addressingMode = AddressingMode.ABSOLUTEY;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xCD;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xDD;
		} else if (addressingMode == AddressingMode.ABSOLUTEY) {
			success = true;
			opcode = 0xD9;
		} else if (addressingMode == AddressingMode.INDEXEDINDIRECT) {
			success = true;
			opcode = 0xC1;
		} else if (addressingMode == AddressingMode.INDIRECTINDEXED) {
			success = true;
			opcode = 0xD1;
		} else {
			System.out.println("Illegal addressing mode for CMP: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionCPX() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xE0;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xE4;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xEC;
		} else {
			System.out.println("Illegal addressing mode for CPX: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionCPY() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.IMMEDIATE) {
			success = true;
			opcode = 0xC0;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xC4;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xCC;
		} else {
			System.out.println("Illegal addressing mode for CPY: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionBIT() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x24;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x2C;
		} else {
			System.out.println("Illegal addressing mode for BIT: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionINC() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xE6;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xF6;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xEE;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xFE;
		} else {
			System.out.println("Illegal addressing mode for INC: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionINX() {
		if (operandStr.length() != 0) {
			System.out.println("INX doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xE8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionINY() {
		if (operandStr.length() != 0) {
			System.out.println("INY doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xC8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionDEC() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0xC6;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0xD6;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0xCE;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0xDE;
		} else {
			System.out.println("Illegal addressing mode for DEC: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionDEX() {
		if (operandStr.length() != 0) {
			System.out.println("DEX doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xCA;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionDEY() {
		if (operandStr.length() != 0) {
			System.out.println("DEY doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x88;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionASL() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ACCUMULATOR) {
			success = true;
			opcode = 0x0A;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x06;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x16;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x0E;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x1E;
		} else {
			System.out.println("Illegal addressing mode for ASL: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionLSR() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ACCUMULATOR) {
			success = true;
			opcode = 0x4A;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x46;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x56;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x4E;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x5E;
		} else {
			System.out.println("Illegal addressing mode for LSR: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionROL() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ACCUMULATOR) {
			success = true;
			opcode = 0x2a;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x26;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x36;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x2E;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x3E;
		} else {
			System.out.println("Illegal addressing mode for ROL: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionROR() {
		decodeAddressingMode();

		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.ACCUMULATOR) {
			success = true;
			opcode = 0x6A;
		} else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			opcode = 0x66;
		} else if (addressingMode == AddressingMode.ZEROPAGEX) {
			success = true;
			opcode = 0x76;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x6E;
		} else if (addressingMode == AddressingMode.ABSOLUTEX) {
			success = true;
			opcode = 0x7E;
		} else {
			System.out.println("Illegal addressing mode for ROR: "
					+ addressingMode);
			somethingWentWrong();
		}

	}

	private void instructionJMP() {
		decodeAddressingMode();
		if (addressingMode == null) {

		} else if (addressingMode == AddressingMode.INDIRECT) {
			success = true;
			opcode = 0x6C;
		}
		/*
		 * Yeah, we're cheating. No, but seriously, of course you need to be
		 * able to write a zero page address as an argument to JMP, even though
		 * it's assembled as an absolute address.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			arg2 = 0x00;
			addressingMode = AddressingMode.ABSOLUTE;
			opcode = 0x4C;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x4C;
		} else {
			System.out.println("Illegal addressing mode for JMP: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionJSR() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * Yeah, we're cheating. No, but seriously, of course you need to be
		 * able to write a zero page address as an argument to JSR, even though
		 * it's assembled as an absolute address.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			arg2 = 0x00;
			addressingMode = AddressingMode.ABSOLUTE;
			opcode = 0x20;
		} else if (addressingMode == AddressingMode.ABSOLUTE) {
			success = true;
			opcode = 0x20;
		} else {
			System.out.println("Illegal addressing mode for JSR: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionRTI() {
		if (operandStr.length() != 0) {
			System.out.println("RTI doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x40;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	
	private void instructionRTS() {
		if (operandStr.length() != 0) {
			System.out.println("RTS doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x60;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	
	private void instructionBPL() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0x10;
		} else {
			System.out.println("Illegal addressing mode for BPL: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBMI() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0x30;
		} else {
			System.out.println("Illegal addressing mode for BMI: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBVC() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0x50;
		} else {
			System.out.println("Illegal addressing mode for BVC: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBVS() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0x70;
		} else {
			System.out.println("Illegal addressing mode for BVS: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBCC() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0x90;
		} else {
			System.out.println("Illegal addressing mode for BCC: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBCS() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0xB0;
		} else {
			System.out.println("Illegal addressing mode for BCS: "
					+ addressingMode);
			somethingWentWrong();
		}
	}

	private void instructionBNE() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0xD0;
		} else {
			System.out.println("Illegal addressing mode for BNE: "
					+ addressingMode);
			somethingWentWrong();
		}
	}
	private void instructionBEQ() {
		decodeAddressingMode();
		if (addressingMode == null) {
		}
		/*
		 * No. I know. It's not really zero page. It's relative. But that
		 * problem is up to someone else to solve. We don't really see the
		 * difference here, since we don't have access to the location in memory
		 * to which this instruction will be written.
		 */
		else if (addressingMode == AddressingMode.ZEROPAGE) {
			success = true;
			addressingMode = AddressingMode.RELATIVE;
			opcode = 0xF0;
		} else {
			System.out.println("Illegal addressing mode for BEQ: "
					+ addressingMode);
			somethingWentWrong();
		}
	}


	private void instructionBRK() {
		if (operandStr.length() != 0) {
			System.out.println("BRK doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x00;
			arg1 = 0x00;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	

	private void instructionCLC() {
		if (operandStr.length() != 0) {
			System.out.println("CLC doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x18;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionCLI() {
		if (operandStr.length() != 0) {
			System.out.println("CLI doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x58;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionCLD() {
		if (operandStr.length() != 0) {
			System.out.println("CLD doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xD8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionCLV() {
		if (operandStr.length() != 0) {
			System.out.println("CLV doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xB8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionSEC() {
		if (operandStr.length() != 0) {
			System.out.println("SEC doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x38;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionSEI() {
		if (operandStr.length() != 0) {
			System.out.println("SEI doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0x78;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionSED() {
		if (operandStr.length() != 0) {
			System.out.println("SED doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xF8;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	private void instructionNOP() {
		if (operandStr.length() != 0) {
			System.out.println("NOP doesn't need an operand: " + operandStr);
			somethingWentWrong();
		} else {
			opcode = 0xEA;
			success = true;
			addressingMode = AddressingMode.IMPLIED;
		}
	}

	public AddressingMode getAddressingMode() {
		return addressingMode;
	}

	public String getInstructionStr() {
		return instructionStr;
	}

	public String getOperandStr() {
		return operandStr;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getArg1() {
		return arg1;
	}

	public int getArg2() {
		return arg2;
	}

	public boolean isSuccess() {
		return success;
	}

}
