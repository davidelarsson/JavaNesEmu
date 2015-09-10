package ppu;

import java.awt.Container;
import java.awt.GridLayout;


import javax.swing.JFrame;


public class Application {
	
	public static void main(String[] args) {
		new Application();
	}
	
	public Application() {
		
		JFrame gui = new JFrame();
		gui.setTitle("Painting test");
		gui.setSize(400,400);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Painting panel = new Painting();
		gui.getContentPane().setLayout(new GridLayout(1,1));
		gui.getContentPane().add(panel);
		gui.setVisible(true);
	}


}
