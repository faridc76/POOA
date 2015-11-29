package serie04;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class BoxDistrib extends Distrib {
	//ATTRIBUTS
	
	private static final float ALIGNMENT_LEFT = 0f;
	private static final float ALIGNMENT_CENTER = 0.5f;
	private static final int HORIZONTAL_SIZE = 10;
	private static final int VERTICAL_SIZE = 10;
	
	
	// CONSTRUCTEUR
	
	public BoxDistrib(String title) {
		super(title);
	}

	//COMMANDES
	
	protected void placeComponents() {
		// Taille des Boutons.
		JButton[] drinks = new JButton[DRINKS_NB]; 
		for (int i = 0; i < drinks.length; i++) {
			drinks[i] = getButton(BKey.values()[i]);
		}
		setButtonsSize(drinks);
		setButtonsSize(getButton(BKey.INS), getButton(BKey.CANCEL));
		
		// Taille des TextField.
		JTextField[] tf = new JTextField[FKey.values().length];
		for (int i = 0; i < tf.length; i++) {
			tf[i] = getField(FKey.values()[i]);
		}
		setTextFieldSize(tf);
		
		// Alignement des Labels au centre.
		setAlignmentCenter(getLabel(LKey.BACK), getLabel(LKey.CREDIT),
				getButton(BKey.TAKE));
		
		// Grosse boite verticale.
		Box p = Box.createVerticalBox(); {
			// Les deux JLabels
			p.add(Box.createVerticalStrut(VERTICAL_SIZE));
			p.add(getLabel(LKey.BACK));
			p.add(Box.createVerticalStrut(VERTICAL_SIZE));
			p.add(getLabel(LKey.CREDIT));
			p.add(Box.createVerticalStrut(VERTICAL_SIZE));
			// Grosse boite horizontale.
			Box q = Box.createHorizontalBox(); {
				q.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
				// 1e petite boite verticale.
				Box r = Box.createVerticalBox(); {
					Box s;
					// Les boites horizontales pour les boissons.
					for (int i = 0; i < DRINKS_NB; i++) {
						s = Box.createHorizontalBox(); {
							s.add(getButton(BKey.values()[i]));
							s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
							s.add(getPost(PKey.values()[i]));
						}
						s.setAlignmentX(ALIGNMENT_LEFT);
						r.add(s);
						r.add(Box.createVerticalStrut(VERTICAL_SIZE / 2));
					}
					// La boite horizontale pour la boisson sélectionnée.
					s = Box.createHorizontalBox(); {
						s.add(getLabel(LKey.DRINK));
						s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
						s.add(getField(FKey.DRINK));
					}
					s.setAlignmentX(ALIGNMENT_LEFT);
					r.add(s);
				}
				q.add(r);
				q.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
				// 2e petite boite verticale.
				r = Box.createVerticalBox(); {
					r.add(Box.createVerticalStrut(2 * VERTICAL_SIZE));
					// 1e petite boite horizontale.
					Box s = Box.createHorizontalBox(); {
						s.add(getButton(BKey.INS));
						s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
						s.add(getField(FKey.INS));
						s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
						s.add(getPost(PKey.INS));
					}
					s.setAlignmentX(ALIGNMENT_LEFT);
					r.add(s);
					// 2e petite boite horizontale.
					s = Box.createHorizontalBox(); {
						s.add(getButton(BKey.CANCEL));
					}
					s.setAlignmentX(ALIGNMENT_LEFT);
					r.add(s);
					r.add(Box.createVerticalStrut(2 * VERTICAL_SIZE));
					// 2e petite boite horizontale.
					s = Box.createHorizontalBox(); {
						s.add(getLabel(LKey.MONEY));
						s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
						s.add(getField(FKey.MONEY));
						s.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
						s.add(getPost(PKey.MONEY));
					}
					s.setAlignmentX(ALIGNMENT_LEFT);
					r.add(s);
				}
				q.add(r);
				q.add(Box.createHorizontalStrut(HORIZONTAL_SIZE));
			}
			p.add(q);
			p.add(Box.createVerticalStrut(VERTICAL_SIZE));
			p.add(getButton(BKey.TAKE));
			p.add(Box.createVerticalStrut(VERTICAL_SIZE));
			
		}
		getFrame().add(p, BorderLayout.NORTH);	
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BoxDistrib("BoxDistrib de boissons").display();
            }
        });
	}
	
	//OUTILS
		
	// La Largeur du plus gros bouton passé en argument.
	private int getMaxButtonsWidth(JButton... button) {
		int max = 0;
		for (int i = 0; i < button.length; i++) {
			if (button[i].getPreferredSize().width > max) {
				max = button[i].getPreferredSize().width;
			}
		}
		return max;
	}
	
	// Changement de la largeur des boutons.
	private void setButtonsSize(JButton... b) {
		int width = getMaxButtonsWidth(b);
		for (int i = 0; i < b.length; i++) {
			Dimension dim = new Dimension(
				width,
				b[i].getPreferredSize().height
			);
			b[i].setPreferredSize(dim);
			b[i].setMaximumSize(dim);
		}
	}
	
	// Ajuste la taille des TextField 
	private void setTextFieldSize(JTextField... tf) {
		for (int i = 0; i < tf.length; i++) {
			tf[i].setMaximumSize(
				new Dimension(
					Short.MAX_VALUE,
					tf[i].getPreferredSize().height
			));
		}
	}
	
	// Alignement au centre.
	private void setAlignmentCenter(JComponent... c) {
		for (int i = 0; i < c.length; i++) {
			c[i].setAlignmentX(ALIGNMENT_CENTER);
		}
	}
	
}
