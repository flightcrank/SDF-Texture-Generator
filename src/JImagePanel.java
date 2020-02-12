
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author karma
 */
public class JImagePanel extends JComponent {

	Image img;

	public JImagePanel() {
		
	}
	
	public void setImage(Image i) {
		
		img = i;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (img != null) {
			
			g.drawImage(img, 0, 0, this);
		}
	}	
}
