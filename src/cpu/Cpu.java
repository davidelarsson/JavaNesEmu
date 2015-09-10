package cpu;

import memory.CpuMem;

public class Cpu {

	private static Cpu instance;
	ExecuteNMI nmiHandler;
	private InstructionHandler[] instructionHandlers;
	long illegalInstructions;

	public static Cpu getInstance() {
		if (instance == null) {
			instance = new Cpu();
		}
		return instance;
	}

	private Cpu() {
		initializeHandlers();
		illegalInstructions = 0;
	}

	/**
	 * Initalize an array of instruction handlers. Arguments are:
	 * AddressingMode mode, int length, int cycles
	 * 
	 * length is the length of the instruction in bytes in memory and cycles
	 * is the number of cycles it takes to execute the instruction. Add 0x10
	 * if page boundary crossing adds one cycle.
	 */
	private void initializeHandlers() {

		nmiHandler = new ExecuteNMI(0);

		instructionHandlers = new InstructionHandler[0x100];

		instructionHandlers[0x00] = new InstructionBRK(
				new AddressingModeImmediate(), 2, 7); // BRK
		instructionHandlers[0x01] = new InstructionORA(
				new AddressingModeIndexedIndirect(), 2, 6); // ORA ($nn,X)
		instructionHandlers[0x02] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x03] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x04] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x05] = new InstructionORA(
				new AddressingModeZeroPage(), 2, 3); // ORA $nn
		instructionHandlers[0x06] = new InstructionASL(
				new AddressingModeZeroPage(), 2, 5); // ASL $nn
		instructionHandlers[0x07] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x08] = new InstructionPHP(
				new AddressingModeDummy(), 1, 3); // PHP
		instructionHandlers[0x09] = new InstructionORA(
				new AddressingModeImmediate(), 2, 2); // ORA #$nn
		instructionHandlers[0x0A] = new InstructionASLA(
				new AddressingModeDummy(), 1, 2); // ASL A
		instructionHandlers[0x0B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x0C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x0D] = new InstructionORA(
				new AddressingModeAbsolute(), 3, 4); // ORA $nnnn
		instructionHandlers[0x0E] = new InstructionASL(
				new AddressingModeAbsolute(), 3, 6); // ASL $nnnn
		instructionHandlers[0x0F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x10] = new InstructionBPL(
				new AddressingModeRelative(), 2, 2); // BPL
		instructionHandlers[0x11] = new InstructionORA(
				new AddressingModeIndirectIndexed(), 2, 5); // ORA ($nn),Y
		instructionHandlers[0x12] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x13] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x14] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x15] = new InstructionORA(
				new AddressingModeZeroPageX(), 2, 4); // ORA $nn,X
		instructionHandlers[0x16] = new InstructionASL(
				new AddressingModeZeroPageX(), 2, 6); // ASL $nn,X
		instructionHandlers[0x17] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x18] = new InstructionCLC(
				new AddressingModeDummy(), 1, 2); // CLC
		instructionHandlers[0x19] = new InstructionORA(
				new AddressingModeAbsoluteY(), 3, 4); // ORA $nnnn,Y
		instructionHandlers[0x1A] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x1B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x1C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x1D] = new InstructionORA(
				new AddressingModeAbsoluteX(), 3, 4); // ORA $nnnn,X
		instructionHandlers[0x1E] = new InstructionASL(
				new AddressingModeAbsoluteX(), 3, 7); // ASL $nnnn,X
		instructionHandlers[0x1F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x20] = new InstructionJSR(
				new AddressingModeDummy(), 3, 6); // JSR $nnnn
		instructionHandlers[0x21] = new InstructionAND(
				new AddressingModeIndexedIndirect(), 2, 6); // AND ($nn,X)
		instructionHandlers[0x22] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x23] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x24] = new InstructionBIT(
				new AddressingModeZeroPage(), 2, 3); // BIT $nn
		instructionHandlers[0x25] = new InstructionAND(
				new AddressingModeZeroPage(), 2, 3); // AND $nn
		instructionHandlers[0x26] = new InstructionROL(
				new AddressingModeZeroPage(), 2, 5); // ROL $nn
		instructionHandlers[0x27] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x28] = new InstructionPLP(
				new AddressingModeDummy(), 1, 3); // PLP
		instructionHandlers[0x29] = new InstructionAND(
				new AddressingModeImmediate(), 2, 2); // AND #$nn
		instructionHandlers[0x2A] = new InstructionROLA(
				new AddressingModeDummy(), 1, 2); // ROL A
		instructionHandlers[0x2B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x2C] = new InstructionBIT(
				new AddressingModeAbsolute(), 3, 3); // BIT $nnnn
		instructionHandlers[0x2D] = new InstructionAND(
				new AddressingModeAbsolute(), 3, 4); // AND $nnnn
		instructionHandlers[0x2E] = new InstructionROL(
				new AddressingModeAbsolute(), 3, 6); // ROL $nnnn
		instructionHandlers[0x2F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x30] = new InstructionBMI(
				new AddressingModeRelative(), 2, 2); // BMI
		instructionHandlers[0x31] = new InstructionAND(
				new AddressingModeIndirectIndexed(), 2, 5); // AND ($nn),Y
		instructionHandlers[0x32] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x33] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x34] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x35] = new InstructionAND(
				new AddressingModeZeroPageX(), 2, 4); // AND $nn,X
		instructionHandlers[0x36] = new InstructionROL(
				new AddressingModeZeroPageX(), 2, 6); // ROL $nn,X
		instructionHandlers[0x37] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x38] = new InstructionSEC(
				new AddressingModeDummy(), 1, 2); // SEC
		instructionHandlers[0x39] = new InstructionAND(
				new AddressingModeAbsoluteY(), 3, 4); // AND $nnnn,Y
		instructionHandlers[0x3A] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x3B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x3C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x3D] = new InstructionAND(
				new AddressingModeAbsoluteX(), 3, 4); // AND $nnnn,X
		instructionHandlers[0x3E] = new InstructionROL(
				new AddressingModeAbsoluteX(), 3, 7); // ROL $nnnn,X
		instructionHandlers[0x3F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x40] = new InstructionRTI(
				new AddressingModeDummy(), 1, 6); // RTI
		instructionHandlers[0x41] = new InstructionEOR(
				new AddressingModeIndexedIndirect(), 2, 6); // EOR ($nn,X)
		instructionHandlers[0x42] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x43] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x44] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x45] = new InstructionEOR(
				new AddressingModeZeroPage(), 2, 3); // EOR $nn
		instructionHandlers[0x46] = new InstructionLSR(
				new AddressingModeZeroPage(), 2, 5); // LSR $nn
		instructionHandlers[0x47] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x48] = new InstructionPHA(
				new AddressingModeDummy(), 1, 3); // PHA
		instructionHandlers[0x49] = new InstructionEOR(
				new AddressingModeImmediate(), 2, 2); // EOR #$nn
		instructionHandlers[0x4A] = new InstructionLSRA(
				new AddressingModeDummy(), 1, 2); // LSR A
		instructionHandlers[0x4B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x4C] = new InstructionJMPABS(
				new AddressingModeDummy(), 0, 3); // JMP $nnnn
		instructionHandlers[0x4D] = new InstructionEOR(
				new AddressingModeAbsolute(), 3, 4); // EOR $nnnn
		instructionHandlers[0x4E] = new InstructionLSR(
				new AddressingModeAbsolute(), 3, 6); // LSR $nnnn
		instructionHandlers[0x4F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x50] = new InstructionBVC(
				new AddressingModeRelative(), 2, 2); // BVC
		instructionHandlers[0x51] = new InstructionEOR(
				new AddressingModeIndirectIndexed(), 2, 5); // EOR ($nn),Y
		instructionHandlers[0x52] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x53] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x54] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x55] = new InstructionEOR(
				new AddressingModeZeroPageX(), 2, 4); // EOR $nn,X
		instructionHandlers[0x56] = new InstructionLSR(
				new AddressingModeZeroPageX(), 2, 6); // LSR $nn,X
		instructionHandlers[0x57] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x58] = new InstructionCLI(
				new AddressingModeDummy(), 1, 2); // CLI
		instructionHandlers[0x59] = new InstructionEOR(
				new AddressingModeAbsoluteY(), 3, 4); // EOR $nnnn,Y
		instructionHandlers[0x5A] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x5B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x5C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x5D] = new InstructionEOR(
				new AddressingModeAbsoluteX(), 3, 4); // EOR $nnnn,X
		instructionHandlers[0x5E] = new InstructionLSR(
				new AddressingModeAbsoluteX(), 3, 7); // LSR $nnnn,X
		instructionHandlers[0x5F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x60] = new InstructionRTS(
				new AddressingModeDummy(), 1, 6); // RTS
		instructionHandlers[0x61] = new InstructionADC(
				new AddressingModeIndexedIndirect(), 2, 6); // ADC ($nn,X)
		instructionHandlers[0x62] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x63] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x64] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x65] = new InstructionADC(
				new AddressingModeZeroPage(), 2, 3); // ADC $nn
		instructionHandlers[0x66] = new InstructionROR(
				new AddressingModeZeroPage(), 2, 5); // ROR $nn
		instructionHandlers[0x67] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x68] = new InstructionPLA(
				new AddressingModeDummy(), 1, 3); // PLA
		instructionHandlers[0x69] = new InstructionADC(
				new AddressingModeImmediate(), 2, 2); // ADC #$nn
		instructionHandlers[0x6A] = new InstructionRORA(
				new AddressingModeDummy(), 1, 2); // ROR A
		instructionHandlers[0x6B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x6C] = new InstructionJMP(
				new AddressingModeIndirect(), 0, 3); // JMP ($nnnn)
		instructionHandlers[0x6D] = new InstructionADC(
				new AddressingModeAbsolute(), 3, 4); // ADC $nnnn
		instructionHandlers[0x6E] = new InstructionROR(
				new AddressingModeAbsolute(), 3, 6); // ROR $nnnn
		instructionHandlers[0x6F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x70] = new InstructionBVS(
				new AddressingModeRelative(), 2, 2); // BVS
		instructionHandlers[0x71] = new InstructionADC(
				new AddressingModeIndirectIndexed(), 2, 5); // ADC ($nn),Y
		instructionHandlers[0x72] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x73] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x74] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x75] = new InstructionADC(
				new AddressingModeZeroPageX(), 2, 4); // ADC $nn,X
		instructionHandlers[0x76] = new InstructionROR(
				new AddressingModeZeroPageX(), 2, 6); // ROR $nn,X
		instructionHandlers[0x77] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x78] = new InstructionSEI(
				new AddressingModeDummy(), 1, 2); // SEI
		instructionHandlers[0x79] = new InstructionADC(
				new AddressingModeAbsoluteY(), 3, 4); // ADC $nnnn,Y
		instructionHandlers[0x7A] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x7B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x7C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x7D] = new InstructionADC(
				new AddressingModeAbsoluteX(), 3, 4); // ADC $nnnn,X
		instructionHandlers[0x7E] = new InstructionROR(
				new AddressingModeAbsoluteX(), 3, 7); // ROR $nnnn,X
		instructionHandlers[0x7F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x80] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x81] = new InstructionSTA(
				new AddressingModeIndexedIndirect(), 2, 6); // STA ($nn,X)
		instructionHandlers[0x82] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x83] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x84] = new InstructionSTY(
				new AddressingModeZeroPage(), 2, 3); // STY $nn
		instructionHandlers[0x85] = new InstructionSTA(
				new AddressingModeZeroPage(), 2, 3); // STA $nn
		instructionHandlers[0x86] = new InstructionSTX(
				new AddressingModeZeroPage(), 2, 3); // STX $nn
		instructionHandlers[0x87] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x88] = new InstructionDEY(
				new AddressingModeDummy(), 1, 2); // DEY
		instructionHandlers[0x89] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x8A] = new InstructionTXA(
				new AddressingModeDummy(), 1, 2); // TXA
		instructionHandlers[0x8B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x8C] = new InstructionSTY(
				new AddressingModeAbsolute(), 3, 4); // STY $nnnn
		instructionHandlers[0x8D] = new InstructionSTA(
				new AddressingModeAbsolute(), 3, 4); // STA $nnnn
		instructionHandlers[0x8E] = new InstructionSTX(
				new AddressingModeAbsolute(), 3, 4); // STX $nnnn
		instructionHandlers[0x8F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x90] = new InstructionBCC(
				new AddressingModeRelative(), 2, 2); // BCC
		instructionHandlers[0x91] = new InstructionSTA(
				new AddressingModeIndirectIndexed(), 2, 6); // STA ($nn),Y
		instructionHandlers[0x92] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x93] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x94] = new InstructionSTY(
				new AddressingModeZeroPageX(), 2, 4); // STY $nn,X
		instructionHandlers[0x95] = new InstructionSTA(
				new AddressingModeZeroPageX(), 2, 4); // STA $nn,X
		instructionHandlers[0x96] = new InstructionSTX(
				new AddressingModeZeroPageY(), 2, 3); // STX $nn,Y
		instructionHandlers[0x97] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x98] = new InstructionTYA(
				new AddressingModeDummy(), 1, 2); // TYA
		instructionHandlers[0x99] = new InstructionSTA(
				new AddressingModeAbsoluteY(), 3, 5); // STA $nnnn,Y
		instructionHandlers[0x9A] = new InstructionTXS(
				new AddressingModeDummy(), 1, 2); // TXS
		instructionHandlers[0x9B] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x9C] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x9D] = new InstructionSTA(
				new AddressingModeAbsoluteX(), 3, 5); // STA $nnnn,X
		instructionHandlers[0x9E] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0x9F] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xA0] = new InstructionLDY(
				new AddressingModeImmediate(), 2, 2); // LDY #$nn
		instructionHandlers[0xA1] = new InstructionLDA(
				new AddressingModeIndexedIndirect(), 2, 6); // LDA ($nn,X)
		instructionHandlers[0xA2] = new InstructionLDX(
				new AddressingModeImmediate(), 2, 2); // LDX #$nn
		instructionHandlers[0xA3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xA4] = new InstructionLDY(
				new AddressingModeZeroPage(), 2, 3); // LDY $nn
		instructionHandlers[0xA5] = new InstructionLDA(
				new AddressingModeZeroPage(), 2, 3); // LDA $nn
		instructionHandlers[0xA6] = new InstructionLDX(
				new AddressingModeZeroPage(), 2, 3); // LDX $nn
		instructionHandlers[0xA7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xA8] = new InstructionTAY(
				new AddressingModeDummy(), 1, 2); // TAY
		instructionHandlers[0xA9] = new InstructionLDA(
				new AddressingModeImmediate(), 2, 2); // LDA #$nn
		instructionHandlers[0xAA] = new InstructionTAX(
				new AddressingModeDummy(), 1, 2); // TAX
		instructionHandlers[0xAB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xAC] = new InstructionLDY(
				new AddressingModeAbsolute(), 3, 4); // LDY $nnnn
		instructionHandlers[0xAD] = new InstructionLDA(
				new AddressingModeAbsolute(), 3, 4); // LDA $nnnn
		instructionHandlers[0xAE] = new InstructionLDX(
				new AddressingModeAbsolute(), 3, 4); // LDA $nnnn
		instructionHandlers[0xAF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xB0] = new InstructionBCS(
				new AddressingModeRelative(), 2, 2); // BCS
		instructionHandlers[0xB1] = new InstructionLDA(
				new AddressingModeIndirectIndexed(), 2, 5); // LDA ($nn),Y
		instructionHandlers[0xB2] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xB3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xB4] = new InstructionLDY(
				new AddressingModeZeroPageX(), 2, 4); // LDY $nn,X
		instructionHandlers[0xB5] = new InstructionLDA(
				new AddressingModeZeroPageX(), 2, 4); // LDA $nn,X
		instructionHandlers[0xB6] = new InstructionLDX(
				new AddressingModeZeroPageY(), 2, 4); // LDA $nn,X
		instructionHandlers[0xB7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xB8] = new InstructionCLV(
				new AddressingModeDummy(), 1, 2); // CLV
		instructionHandlers[0xB9] = new InstructionLDA(
				new AddressingModeAbsoluteY(), 3, 4); // LDA $nnnn,Y
		instructionHandlers[0xBA] = new InstructionTSX(
				new AddressingModeDummy(), 1, 2); // TSX
		instructionHandlers[0xBB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xBC] = new InstructionLDY(
				new AddressingModeAbsoluteX(), 3, 4); // LDY $nnnn,X
		instructionHandlers[0xBD] = new InstructionLDA(
				new AddressingModeAbsoluteX(), 3, 4); // LDA $nnnn,X
		instructionHandlers[0xBE] = new InstructionLDX(
				new AddressingModeAbsoluteY(), 3, 4); // LDA $nnnn,Y
		instructionHandlers[0xBF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xC0] = new InstructionCPY(
				new AddressingModeImmediate(), 2, 2); // CPY #$nn
		instructionHandlers[0xC1] = new InstructionCMP(
				new AddressingModeIndexedIndirect(), 2, 6); // CMP ($nn,X)
		instructionHandlers[0xC2] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xC3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xC4] = new InstructionCPY(
				new AddressingModeZeroPage(), 2, 3); // CPY $nn
		instructionHandlers[0xC5] = new InstructionCMP(
				new AddressingModeZeroPage(), 2, 3); // CMP $nn
		instructionHandlers[0xC6] = new InstructionDEC(
				new AddressingModeZeroPage(), 2, 5); // DEC $nn
		instructionHandlers[0xC7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xC8] = new InstructionINY(
				new AddressingModeDummy(), 1, 2); // INY
		instructionHandlers[0xC9] = new InstructionCMP(
				new AddressingModeImmediate(), 2, 4); // CMP #$nn
		instructionHandlers[0xCA] = new InstructionDEX(
				new AddressingModeDummy(), 1, 2); // DEX
		instructionHandlers[0xCB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xCC] = new InstructionCPY(
				new AddressingModeAbsolute(), 3, 3); // CPY $nnnn
		instructionHandlers[0xCD] = new InstructionCMP(
				new AddressingModeAbsolute(), 3, 4); // CMP $nnnn
		instructionHandlers[0xCE] = new InstructionDEC(
				new AddressingModeAbsolute(), 3, 6); // DEC $nnnn
		instructionHandlers[0xCF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xD0] = new InstructionBNE(
				new AddressingModeRelative(), 2, 2); // BNE
		instructionHandlers[0xD1] = new InstructionCMP(
				new AddressingModeIndirectIndexed(), 2, 5); // CMP ($nn),Y
		instructionHandlers[0xD2] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xD3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xD4] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xD5] = new InstructionCMP(
				new AddressingModeZeroPageX(), 2, 4); // CMP $nn,X
		instructionHandlers[0xD6] = new InstructionDEC(
				new AddressingModeZeroPageX(), 2, 6); // DEC $nn,X
		instructionHandlers[0xD7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xD8] = new InstructionCLD(
				new AddressingModeDummy(), 1, 2); // CLD
		instructionHandlers[0xD9] = new InstructionCMP(
				new AddressingModeAbsoluteY(), 3, 4); // CMP $nnnn,Y
		instructionHandlers[0xDA] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xDB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xDC] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xDD] = new InstructionCMP(
				new AddressingModeAbsoluteX(), 3, 4); // CMP $nnnn,X
		instructionHandlers[0xDE] = new InstructionDEC(
				new AddressingModeAbsoluteX(), 3, 6); // DEC $nnnn,X
		instructionHandlers[0xDF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xE0] = new InstructionCPX(
				new AddressingModeImmediate(), 2, 2); // CPX #$nn
		instructionHandlers[0xE1] = new InstructionSBC(
				new AddressingModeIndexedIndirect(), 2, 6); // SBC ($nn,X)
		instructionHandlers[0xE2] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xE3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xE4] = new InstructionCPX(
				new AddressingModeZeroPage(), 2, 3); // CPX $nn
		instructionHandlers[0xE5] = new InstructionSBC(
				new AddressingModeZeroPage(), 2, 3); // SBC $nn
		instructionHandlers[0xE6] = new InstructionINC(
				new AddressingModeZeroPage(), 2, 5); // INC $nn
		instructionHandlers[0xE7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xE8] = new InstructionINX(
				new AddressingModeDummy(), 1, 2); // INX
		instructionHandlers[0xE9] = new InstructionSBC(
				new AddressingModeImmediate(), 2, 2); // SBC #$nn
		instructionHandlers[0xEA] = new InstructionNOP(
				new AddressingModeImmediate(), 1, 2); // NOP
		instructionHandlers[0xEB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xEC] = new InstructionCPX(
				new AddressingModeAbsolute(), 3, 4); // CPX $nnnn
		instructionHandlers[0xED] = new InstructionSBC(
				new AddressingModeAbsolute(), 3, 4); // SBC $nnnn
		instructionHandlers[0xEE] = new InstructionINC(
				new AddressingModeAbsolute(), 3, 6); // INC $nnnn
		instructionHandlers[0xEF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xF0] = new InstructionBEQ(
				new AddressingModeRelative(), 2, 2); // BEQ
		instructionHandlers[0xF1] = new InstructionSBC(
				new AddressingModeIndirectIndexed(), 2, 5); // SBC ($nn),Y
		instructionHandlers[0xF2] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xF3] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xF4] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xF5] = new InstructionSBC(
				new AddressingModeZeroPageX(), 2, 4); // SBC $nn,X
		instructionHandlers[0xF6] = new InstructionINC(
				new AddressingModeZeroPageX(), 2, 6); // INC $nn,X
		instructionHandlers[0xF7] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xF8] = new InstructionSED(
				new AddressingModeDummy(), 1, 2); // SED
		instructionHandlers[0xF9] = new InstructionSBC(
				new AddressingModeAbsoluteY(), 3, 4); // SBC $nnnn,Y
		instructionHandlers[0xFA] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xFB] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xFC] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!
		instructionHandlers[0xFD] = new InstructionSBC(
				new AddressingModeAbsoluteX(), 3, 4); // SBC $nnnn,X
		instructionHandlers[0xFE] = new InstructionINC(
				new AddressingModeAbsoluteX(), 3, 7); // INC $nnnn,X
		instructionHandlers[0xFF] = new InstructionIllegal(
				new AddressingModeDummy(), 1, 2); // Illegal instruction!

	}

	public void executeInstruction() {
		// Seriously, if we have executed 100 illegal instructions, there's
		// something really wrong.
		if (illegalInstructions > 100)
			return;
		int address = CpuRegisters.getInstance().getPC();
		int opcode = CpuMem.getInstance().readMemQuiet(address);
		instructionHandlers[opcode].execute();
		if (instructionHandlers[opcode].getClass() == InstructionIllegal.class) {
			illegalInstructions += 1;
			System.err.println(String.format(
					"Illegal instruction: 0x%02X at: 0x%04X", opcode, address));
		}
		return;
	}

	public void executeNMI() {
		// FIXME!!! An NMI really doesn't tale zero clock cycles...
		nmiHandler.execute();
	}

}
