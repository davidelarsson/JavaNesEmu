package debugger;

import java.awt.Point;

public class Application {

	public static void main(String[] args) {
		
		MemoryPane.getInstance().getPanel().setName("Main Memory");
		MemoryPane.getInstance().getPanel().setSize(495, 400);
		StackPane.getInstance().getPanel().setName("Stack");
		StackPane.getInstance().getPanel().setSize(110, 330);
		CpuPane.getInstance().getPanel().setName("CPU");
		CpuPane.getInstance().getPanel().setSize(300, 250);
		DebugPane.getInstance().getPanel().setName("Debug");
		DebugPane.getInstance().getPanel().setSize(355, 250);
		FilePane.getInstance().getPanel().setName("Current File");
		FilePane.getInstance().getPanel().setSize(250, 200);
		ExecutionPane.getInstance().getPanel().setName("Execution");
		ExecutionPane.getInstance().getPanel().setSize(355, 445);
		GraphicsPane.getInstance().getPanel().setName("Graphics");
		GraphicsPane.getInstance().getPanel().setSize(400, 400);
		ControllerPane.getInstance().getPanel().setName("Control");
		ControllerPane.getInstance().getPanel().setSize(300, 200);
		
		// The array of initial windows
		Object[][] panels = {
				{ MemoryPane.getInstance().getPanel(), new Point(0, 0) },
				{ StackPane.getInstance().getPanel(), new Point(495, 0) },
				{ CpuPane.getInstance().getPanel(), new Point(605, 0) },
				{ DebugPane.getInstance().getPanel(), new Point(250, 400)},
				{ FilePane.getInstance().getPanel(), new Point(0, 400)},
				{ ExecutionPane.getInstance().getPanel(), new Point(605, 250)},
				{ GraphicsPane.getInstance().getPanel(), new Point(960, 0)},
				{ ControllerPane.getInstance().getPanel(), new Point(300,400)}};

		WindowCollection rootWindow = new WindowCollection("NES Emulator", panels, 1400, 750);
		rootWindow.setVisible(true);

	}
}
