package serie05;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class MobileComponent extends JComponent {

	// ATTRIBUTS
	private static final long serialVersionUID = -2367796860787119090L;
	// Le Mobile.
	private Mobile model;
	// Indique si cercle est attrapé.
	private boolean caught;
	
	// CONSTRUCTEUR
	public MobileComponent(Rectangle sr, Rectangle mr) {
		model = new StdMobile(sr, mr);
		caught = false;
		createController();
		setPreferredSize(new Dimension(sr.width, sr.height));
		repaint();
		}
	
	
	// REQUETES
    Point getDiscCenter() {
    	return model.getCenter();
    }
    int getHorizontalShift() {
    	return model.getHorizontalShift();
    }
    Mobile getModel() {
    	return model;
    	
    }
    int getVerticalShift() {
    	return model.getVerticalShift();
    	
    }
    boolean isDiscCaught() {
    	return caught;
    }

    // COMMANDES
    void activate(int n) {
    	if (n < 0) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < n; i++) {
			model.move();
		}		
    }
    void setDiscCenter(Point p) {
    	// Precondition dans setCenter
    	model.setCenter(p);
    }
    void setDiscShift(int dx, int dy) {
    	model.setHorizontalShift(dx);
		model.setVerticalShift(dy);
    }
    
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = model.getStaticRect();
		g.setColor(Color.BLUE);
		g.fillRect(r.x, r.y, r.width, r.height);
		r = model.getMovingRect();
		g.setColor(Color.RED);
		g.fillOval(r.x, r.y, r.width, r.height);
	}
    
 // OUTILS
    private void createController() {
		
	}

}
