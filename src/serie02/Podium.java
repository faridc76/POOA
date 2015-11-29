package serie02;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @inv <pre> getModel() != null </pre>
 */
public class Podium<E extends Drawable> extends JComponent {
	private static final long serialVersionUID = 2546250256910910783L;
	// ATTRIBUTS
    private static final Color NO_ANIMAL = Color.LIGHT_GRAY;
    private static final Color PODIUM_COLOR = Color.BLACK;
    private static final Color NO_PODIUM_COLOR = Color.WHITE;
    private static final int BASE_HEIGHT = 5;
    private static final int PODIUM_HEIGHT = 2 * BASE_HEIGHT;
    private static final int LEG_WIDTH = 7;
    private static final int MARGIN = 2;
    
    private ChangeListener cl;
    private PodiumModel<E> model;

    // CONSTRUCTEURS
    /**
     * Un podium de modèle pm.
     * @pre <pre>
     *     pm != null </pre>
     * @post <pre>
     *     getModel() == pm </pre>
     */
    public Podium(PodiumModel<E> pm) {
        if (pm == null) {
        	throw new IllegalArgumentException();
        }
        cl = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				repaint();
			}        
        };
        setModel(pm);
        setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            )
        );
    }

    // REQUETES
    /**
     * Le modèle de ce Podium.
     */
    public PodiumModel<E> getModel() {
        return model;
    }

    // COMMANDES
    /**
     * Fixe un nouveau modèle pour ce Podium.
     * @pre <pre>
     *     m != null </pre>
     * @post <pre>
     *     getModel() == m </pre>
     */
    public void setModel(PodiumModel<E> m) {
    	if (m == null) {
    		throw new IllegalArgumentException();
    	}
    	if (model != null) {
    		model.removeChangeListener(cl);
    	}
        model = m;
        model.addChangeListener(cl);
        setPreferredSize(new Dimension(
        		Drawable.ELEM_WIDTH + 2 * MARGIN, 
        		PODIUM_HEIGHT + Drawable.ELEM_HEIGHT * model.capacity())
        );
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int capacity = model.capacity();
        int size = model.size();
        int x = (getWidth() - Drawable.ELEM_WIDTH) / 2;
        int y = getHeight() - PODIUM_HEIGHT;
        g.setColor(PODIUM_COLOR);
        g.fillRect(x, y, Drawable.ELEM_WIDTH, PODIUM_HEIGHT);
        g.setColor(NO_PODIUM_COLOR);
        g.fillRect(
            x + LEG_WIDTH,
            y + PODIUM_HEIGHT - BASE_HEIGHT,
            Drawable.ELEM_WIDTH - 2 * LEG_WIDTH,
            BASE_HEIGHT
        );
        for (int i = 0; i < size; i++) {
            y -= Drawable.ELEM_HEIGHT;
            model.elementAt(i).draw(g, x, y);
        }
        g.setColor(NO_ANIMAL);
        int h = Drawable.ELEM_HEIGHT * (capacity - size);
        g.fillRect(x, y - h, Drawable.ELEM_WIDTH, h);
    }
}
