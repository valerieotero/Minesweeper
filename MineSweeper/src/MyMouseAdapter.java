import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	
	private Random generator = new Random();
	
	
	public void mousePressed(MouseEvent e) {		
		switch (e.getButton()) {
			case 1:		//Left mouse button
				Component c = e.getComponent();
				while (!(c instanceof JFrame)) {
					c = c.getParent();
					if (c == null) {
						return;
					}
				}
				JFrame myFrame = (JFrame) c;
				MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
				Insets myInsets = myFrame.getInsets();
				int x1 = myInsets.left;
				int y1 = myInsets.top;
				e.translatePoint(-x1, -y1);
				int x = e.getX();
				int y = e.getY();
				myPanel.x = x;
				myPanel.y = y;
				myPanel.mouseDownGridX = myPanel.getGridX(x, y);
				myPanel.mouseDownGridY = myPanel.getGridY(x, y);
				myPanel.repaint();
				break;
				
			case 3:		//Right mouse button
				Component flag = e.getComponent();
				while (!(flag instanceof JFrame)) {
					flag = flag.getParent();
					if (flag == null) {
						return;
					}
				}
				JFrame myNewFrame = (JFrame) flag;
				MyPanel myNewPanel = (MyPanel) myNewFrame.getContentPane().getComponent(0);
				Insets myNewInsets = myNewFrame.getInsets();
				int x10 = myNewInsets.left;
				int y10 = myNewInsets.top;
				e.translatePoint(-x10, -y10);
				int x11 = e.getX();
				int y11 = e.getY();
				myNewPanel.x = x11;
				myNewPanel.y = y11;
				myNewPanel.mouseDownGridX = myNewPanel.getGridX(x11, y11);
				myNewPanel.mouseDownGridY = myNewPanel.getGridY(x11, y11);
				myNewPanel.repaint();
				break;
				
			default:    //Some other button (2 = Middle mouse button, etc.)
	//			Do nothing
				break;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
			case 1:		//Left mouse button
				Component c = e.getComponent();
				while (!(c instanceof JFrame)) {
					c = c.getParent();
					if (c == null) {
						return;
					}
				}
				JFrame myFrame = (JFrame)c;
				MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
				Insets myInsets = myFrame.getInsets();
				int x1 = myInsets.left;
				int y1 = myInsets.top;
				e.translatePoint(-x1, -y1);
				int x = e.getX();
				int y = e.getY();
				myPanel.x = x;
				myPanel.y = y;
				int gridX = myPanel.getGridX(x, y);
				int gridY = myPanel.getGridY(x, y);
				if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
					//Had pressed outside
					//Do nothing
				} else {
					if ((gridX == -1) || (gridY == -1)) {
						//Is releasing outside
						//Do nothing
					} else {
						if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
							//Released the mouse button on a different cell where it was pressed
							//Do nothing
						} else {
							//Released the mouse button on the same cell where it was pressed
							if ((gridX == 0) || (gridY == 0)) {
								//On the left column and on the top row... do nothing
							} else {
								//On the grid other than on the left column and on the top row:
								Color newColor = null;
								switch (generator.nextInt(5)) {
									case 0:
										newColor = Color.LIGHT_GRAY;
										break;
									case 1:
										newColor = Color.MAGENTA;
										break;
									case 2:
										newColor = Color.BLACK;
										break;
									case 3:
										newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
										break;
									case 4:
										newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
										break;
								}
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
								myPanel.repaint();
							}
						}
					}
				}
				myPanel.repaint();
				break;
				
			case 3:		//Right mouse button
				Component flag = e.getComponent();
				while (!(flag instanceof JFrame)) {
					flag = flag.getParent();
					if (flag == null) {
						return;
					}
				}
				JFrame myNewFrame = (JFrame)flag;
				MyPanel myNewPanel = (MyPanel) myNewFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
				Insets myNewInsets = myNewFrame.getInsets();
				int x10 = myNewInsets.left;
				int y10 = myNewInsets.top;
				e.translatePoint(-x10, -y10);
				int x11 = e.getX();
				int y11 = e.getY();
				myNewPanel.x = x11;
				myNewPanel.y = y11;
				int gridoX = myNewPanel.getGridX(x11, y11);
				int gridoY = myNewPanel.getGridY(x11, y11);
				if ((myNewPanel.mouseDownGridX == -1) || (myNewPanel.mouseDownGridY == -1)) {
					//Had pressed outside
					//Do nothing
				} else {
					if ((gridoX == -1) || (gridoY == -1)) {
						//Is releasing outside
						//Do nothing
					} else {
						if ((myNewPanel.mouseDownGridX != gridoX) || (myNewPanel.mouseDownGridY != gridoY)) {
							//Released the mouse button on a different cell where it was pressed
							//Do nothing
						} else {
							//Released the mouse button on the same cell where it was pressed
							if ((gridoX == 0) || (gridoY == 0)) {
								//On the left column and on the top row... do nothing
							} else {
								//On the grid other than on the left column and on the top row:
								Color newColor = null;
								switch (generator.nextInt(2)) {
									case 0:
										newColor = Color.RED;
										break;
									case 1:
										newColor = Color.WHITE;
										break;
										}
								
								myNewPanel.colorArray[myNewPanel.mouseDownGridX][myNewPanel.mouseDownGridY] = newColor;
								myNewPanel.repaint();
							}
						}
					}
				}
				myNewPanel.repaint();
				break;
								
			default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;
		}
	}
}