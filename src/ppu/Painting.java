package ppu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;

public class Painting extends JPanel {
	Image image;
	int pixels[];
	int width = 300;
	int height = 300;


	public Painting() {
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pixels = new int[width * height]; 
		for(int i = 0; i<width * height; i ++) {
			pixels[i] = 0xffffffff;
		}
		
		image = createImage(new MemoryImageSource(width, height, pixels, 0, width));		
		g.setColor(Color.WHITE);
		g.drawImage(image, 0, 0, this);
		
	}
}
