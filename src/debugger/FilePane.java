package debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import other.NesFile;
import ppu.PpuRegisters;

import memory.CpuMem;

import cpu.CpuRegisters;

public class FilePane {
	private static FilePane instance;
	private JPanel filePanel;
	private JLabel PRGLabel;
	private JLabel CHRLabel;
	private JLabel mapperLabel;
	private JLabel batteryLabel;
	private JLabel mirroringLabel;

	public static FilePane getInstance() {
		if (instance == null)
			instance = new FilePane();
		return instance;
	}

	public JPanel getPanel() {
		return filePanel;
	}

	private class OpenButtonListener implements ActionListener {

		int[] fileBuffer;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String fileName;

			JFileChooser fileChooser = new JFileChooser();
			// Suggest to open a .nes file
			FileFilter inesFileFilter = new FileNameExtensionFilter(
					"iNES file", "nes");
			fileChooser.addChoosableFileFilter(inesFileFilter);
			fileChooser.setFileFilter(inesFileFilter);
			fileChooser.setCurrentDirectory(new File(System
					.getProperty("user.dir")));

			int returnVal = fileChooser.showOpenDialog(filePanel);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
				DebugPane.getInstance().outputDebugMessage(
						"Load file cancelled.");
				return;
			}

			fileName = fileChooser.getSelectedFile().getName();

			DebugPane.getInstance().outputDebugMessage("Opening " + fileName);
			RandomAccessFile file;
			try {
				file = new RandomAccessFile(fileName, "r");
				fileBuffer = new int[(int) file.length()];
				int i = 0;

				// Read the first four bytes and check that it's a NES file!
				for (; i < 4; i++) {
					fileBuffer[i] = file.readByte();
				}
				if (!(fileBuffer[0] == 'N' && fileBuffer[1] == 'E'
						&& fileBuffer[2] == 'S' && fileBuffer[3] == 0x1a)) {
					DebugPane.getInstance().outputDebugMessage(
							"Not a NES file!");
					file.close();
					return;
				}

				/*
				 * Oh yes, must be unsigned! I was surprised a method like this
				 * even existed in Javas' otherwise inescapably signed world!
				 */
				for (; i < file.length(); i++) {
					fileBuffer[i] = file.readUnsignedByte();
				}

				file.close();

			} catch (Exception e) {
				DebugPane.getInstance().outputDebugMessage(
						"Could not open file!");
				return;
			} finally {
			}
			DebugPane.getInstance().outputDebugMessage(fileName + " loaded");
			// That's it, the file is in the array, now interpret it!
			setupFile();
			MemoryPane.getInstance().fireStateChanged();

		}

		/*
		 * FIXME!!! There's more to do here. So much more...
		 */
		private void setupFile() {
			NesFile.getInstance().setNumOfPRG(fileBuffer[4]);
			NesFile.getInstance().setNumOfCHR(fileBuffer[5]);

			/*
			 * The mapper's lower four bits are in byte 6 and the upper four
			 * bits are in byte 7.
			 */
			int mapper = fileBuffer[6] & 0x000000f0;
			mapper = mapper >> 4;
			mapper |= fileBuffer[7] & 0x000000f0;

			if (mapper != 0) {
				DebugPane.getInstance().outputDebugMessage(
						"Mapper " + mapper + " is as of yet unsupported!");
				return;
			}
			NesFile.getInstance().setMapper(mapper);

			/*
			 * Bit 1 of Byte 6 determines whether there is a battery-backed SRAM
			 * at 0x6000 - 0x7FFF
			 */
			int battery = fileBuffer[6];
			battery &= 0x02;
			if (battery == 0)
				NesFile.getInstance().setBatteryBacked(false);
			else
				NesFile.getInstance().setBatteryBacked(true);

			/*
			 * Bit 0 of Byte 6 indicates whether we have horizontal (0) or
			 * vertical (1) mirroring
			 */
			int mirroring = fileBuffer[6];
			mirroring &= 0x01;
			if (mirroring == 0)
				NesFile.getInstance().setMirroring(NesFile.HORIZONTAL);
			else
				NesFile.getInstance().setMirroring(NesFile.VERTICAL);

			/*
			 * Now, check whether there's a 512-byte trainer in the header. If
			 * so, bit 2 is set. From now on we must read the buffer with the
			 * trainer as an offset
			 */
			int trainer = fileBuffer[6];
			trainer &= 0x04;
			if (trainer != 0)
				trainer = 512;

			/*
			 * FIXME!! Since we do not yet support mappers, just load one or two
			 * banks of PRG into memory.
			 */
			if (NesFile.getInstance().getNumOfPRG() == 1) {
				MemoryPane.getInstance().setOffset(0xC000);
				int start = 0x8000;
				int length = 0x4000;
				for (int i = 0; i < length; i++)
					CpuMem.getInstance().writeMemQuiet(start + i,
							fileBuffer[trainer + i + 0x10]);
				start = 0xC000;
				for (int i = 0; i < length; i++)
					CpuMem.getInstance().writeMemQuiet(start + i,
							fileBuffer[trainer + i + 0x10]);
			} else if (NesFile.getInstance().getNumOfPRG() == 2) {
				MemoryPane.getInstance().setOffset(0x8000);
				int start = 0x8000;
				int length = 0x8000;
				for (int i = 0; i < length; i++)
					CpuMem.getInstance().writeMemQuiet(start + i,
							fileBuffer[trainer + i + 0x10]);

			} else {
				DebugPane.getInstance().outputDebugMessage(
						"Unsupported number of PRG ROMs!");
			}

			/*
			 * FIXME!! Since we do not yet support mappers, just load one CHR
			 * ROM into PPU memory.
			 */
			if (NesFile.getInstance().getNumOfCHR() == 1) {

				int numOfPRG = NesFile.getInstance().getNumOfPRG();
				int start = numOfPRG * 0x4000;
				int length = 0x2000;
				for (int i = 0; i < length; i++)
					PpuRegisters.getInstance().writePpuMemQuiet(i,
							fileBuffer[trainer + start + i + 0x10]);
			} else {
				DebugPane.getInstance().outputDebugMessage(
						"Unsupported number of CHR ROMs!");
			}

			int resetVector = CpuMem.getInstance().getResetVector();
			CpuRegisters.getInstance().setPC(resetVector);

			fireStateChanged();
			ExecutionPane.getInstance().fireStateChanged();
			CpuPane.getInstance().fireStateChanged();
		}

	}

	private FilePane() {

		JPanel buttonPanel = new JPanel();
		JButton openButton = new JButton("Load file");
		openButton.addActionListener(new OpenButtonListener());
		buttonPanel.add(openButton);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		infoPanel.add(new JLabel("16k PRG-ROM: "), constraints);

		PRGLabel = new JLabel("0");
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		infoPanel.add(PRGLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		infoPanel.add(new JLabel("8k CHR-ROM: "), constraints);

		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		CHRLabel = new JLabel("0");
		infoPanel.add(CHRLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		infoPanel.add(new JLabel("mapper: "), constraints);

		mapperLabel = new JLabel("0");
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		infoPanel.add(mapperLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		infoPanel.add(new JLabel("Battery backup: "), constraints);

		batteryLabel = new JLabel("disabled");
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		infoPanel.add(batteryLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		infoPanel.add(new JLabel("mirroring: "), constraints);

		mirroringLabel = new JLabel("horizontal");
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		infoPanel.add(mirroringLabel, constraints);

		filePanel = new JPanel();
		filePanel.setBorder(new TitledBorder(new EtchedBorder(), "File"));

		filePanel.add(buttonPanel);
		filePanel.add(infoPanel);
	}

	public void fireStateChanged() {
		CHRLabel.setText(Integer.toString(NesFile.getInstance().getNumOfCHR()));
		PRGLabel.setText(Integer.toString(NesFile.getInstance().getNumOfPRG()));
		mapperLabel
				.setText(Integer.toString(NesFile.getInstance().getMapper()));
		batteryLabel
				.setText((NesFile.getInstance().isBatteryBacked() ? "enabled"
						: "disabled"));
		if (NesFile.getInstance().getMirroring() == NesFile.HORIZONTAL)
			mirroringLabel.setText("horizontal");
		else
			mirroringLabel.setText("vertical");

	}
}
