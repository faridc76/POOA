package serie05;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class StdMobile implements Mobile {
	
	// ATTRIBUTS
	// Les deux Rectangles.
	private Rectangle staticRect, movingRect;
	// Les pas de déplacements.
	private int hShift, vShift;
	// Indique si le rectangle mobile est en mouvement
	private boolean movable;
	// La liste des ChangeListeners.
    private transient ChangeEvent changeEvent;
	private EventListenerList listenerList;
	
	
	// CONSTRUCTEUR	
	public StdMobile(Rectangle sr, Rectangle mr) {
		if ((mr == null) || (sr == null) || (mr.height <= 0) || (mr.width <= 0) || !sr.contains(mr)) {
			throw new IllegalArgumentException();
		}
		staticRect = sr;
		movingRect = mr;
		hShift = 0;
		vShift = 0;
		movable = true;
	}
	
	
	// REQUETES
	public Point getCenter() {
		return new Point(
				getMovingRect().x + getMovingRect().width / 2,
				getMovingRect().y + getMovingRect().height / 2
			);
	}

	
	public ChangeListener[] getChangeListeners() {
		return (ChangeListener[]) listenerList.getListenerList();
	}

	public int getHorizontalShift() {
		return hShift;
	}

	public Rectangle getMovingRect() {
		return movingRect;
	}

	public Rectangle getStaticRect() {
		return staticRect;
	}

	public int getVerticalShift() {
		return vShift;
	}

	public boolean isMovable() {
		return movable;
	}


	public boolean isValidCenterPosition(Point p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return getStaticRect().contains(
			p.x - getMovingRect().width / 2,
			p.y - getMovingRect().height / 2,
			getMovingRect().width,
			getMovingRect().height
	    );
	}

	public void addChangeListener(ChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		listenerList.add(ChangeListener.class, listener);
	}


	public void move() {
		if (!isMovable()) {
            throw new IllegalStateException();
        }
        Point oldTlc = movingRect.getLocation();
        Point newTlc = new Point(oldTlc.x + hShift, oldTlc.y + vShift);
        Point newBrc = new Point(
            newTlc.x + movingRect.width,
            newTlc.y + movingRect.height
        );
        if (newTlc.y <= staticRect.y) {
            newTlc.y = staticRect.y;
            vShift = -vShift;
        } else if (newBrc.y >= staticRect.y + staticRect.height) {
            newTlc.y = staticRect.y + staticRect.height - movingRect.height;
            vShift = -vShift;
        }
        if (newTlc.x <= staticRect.x) {
            newTlc.x = staticRect.x;
            hShift = -hShift;
        } else if (newBrc.x >= staticRect.x + staticRect.width) {
            newTlc.x = staticRect.x + staticRect.width - movingRect.width;
            hShift = -hShift;
        }
        getMovingRect().setLocation(newTlc);
		fireStateChanged();
    }

	public void removeChangeListener(ChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		listenerList.remove(ChangeListener.class, listener);
	}

	public void setCenter(Point c) {
		if (!isValidCenterPosition(c)) {
			throw new IllegalArgumentException();
		}
		getMovingRect().setLocation(
			new Point(
				c.x - getMovingRect().width / 2,
	            c.y - getMovingRect().height / 2
	        )
	    );
		fireStateChanged();
	}

	public void setHorizontalShift(int hs) {
		hShift = hs;
		fireStateChanged();
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
		fireStateChanged();
	}

	public void setVerticalShift(int vs) {
		vShift = vs;
		fireStateChanged();
	}
	
	public void fireStateChanged() {
		for (ChangeListener cl 
				: listenerList.getListeners(ChangeListener.class)) {
			if (changeEvent == null) {
				changeEvent = new ChangeEvent(this);
			}
			cl.stateChanged(changeEvent);
		}
	}


}
