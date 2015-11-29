package serie03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;

public class ClientFTP {
	// ATTRIBUTS
	// Le niveau de clignotement du pointeur de texte.
	private static final int CARET_BLINK = 800;
	// LA couleur de bordure de titre
	private static final int RED = 6;
	private static final int GREEN = 75;
	private static final int BLUE = 144;
	// La fenêtre principale.
	private JFrame frame;
	// Le bouton flèche vers la droite
	private JButton right;
	// Le bouton flèche vers la gauche.
	private JButton left;
	// Bouton ASCII
	private JRadioButton ascii;
	// Bouton Binary
	private JRadioButton binary;
	// Bouton Auto
	private JCheckBox auto;
	// La zone de texte
	private JTextArea area;
	// Les Boutons du bas, associés à leurs contraintes.
	private Map<JButton, GBC> buttons;
	// Les deux FilePanel
	private FilePanel local;
	private FilePanel remote;

	// CONSTRUCTEUR
	public ClientFTP() {
		lookAndFeel();
		createView();
		placeComponent();
		createController();
	}
	
	// COMMANDES
	/**
	 * Disposition de la fenêtre.
	 */
	public void display() {
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	// OUTILS
	/**
	 * Création de la vue.
	 */
	private void createView() {
		frame = new JFrame("Vue de WS_FTP Light pour window$");
		left = new JButton("<-");
		right = new JButton("->");
		ascii = new JRadioButton("ASCII");
		binary = new JRadioButton("Binary");
		ButtonGroup g = new ButtonGroup(); {
			g.add(ascii);
			g.add(binary);
		}
		auto = new JCheckBox("Auto");
		area = new JTextArea("Ceci est la console," 
				+ "\non y voit passer\ntoutes les commandes");
		area.setEditable(false);
		buttons = new HashMap<JButton, GBC>(); {
			buttons.put(new JButton("Connect"), constraints(0));
			buttons.put(new JButton("Cancel"), constraints(1));
			buttons.put(new JButton("LogWnd"), constraints(2));
			buttons.put(new JButton("Help"), constraints(3));
			buttons.put(new JButton("Options"), constraints(4));
			buttons.put(new JButton("About"), constraints(5));
			buttons.put(new JButton("Exit"), constraints(6));
		}
		local = new FilePanel("Local System", "D:");
		remote = new FilePanel("Remote System", "D:");
	}
	
	private GBC constraints(int i) {
		return new GBC(i, 0).insets(4, 2, 2, 2).fill(GBC.BOTH).weight(1, 1);
	}
	
	/**
	 * Le look and feel de l'application.
	 */
	private void lookAndFeel() {
		// Changement du look and feel
		// Pour un meilleur rendu on peut utiliser
		// com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
		// Pour un rendu identique à celui de l'application
		// On utilisera plutôt: WindowsLookAndFeel
		// Ici, j'ai choisi de faire correspondre le lookAndFeel
		// avec le système d'exploitation utilisé.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		// Le clignotement du pointeur de texte dans les TextArea.
		UIManager.put(
			"TextArea.caretBlinkRate", 
			CARET_BLINK
		);
		// Les bordures de types "Titres" sont de couleurs bleu
		UIManager.put(
			"TitledBorder.titleColor",
			new ColorUIResource(new Color(RED, GREEN, BLUE))
		);
		// Entouré d'une bordure "Gravure".
		UIManager.put(
			"TitledBorder.border",
			new BorderUIResource(BorderFactory.createEtchedBorder())
		);
		
		// Bordure des JScrollPane.
		UIManager.put(
			"ScrollPane.border",
			new BorderUIResource(
				BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
					BorderFactory.createEmptyBorder(3, 5, 3, 5))
				)
		);
	}
	
	/**
	 * Placement des composants.
	 */
	private void placeComponent() {
		// Un tableau de contraintes pour le Panel placé au Nord.
		GBC[] constraints = new GBC[] {
				// Local System
				new GBC(0, 0, 1, 2).fill(GBC.BOTH).weight(1, 1),
				// Bouton 1 : Flèche vers la gauche
				new GBC(1, 0, 1, 1).anchor(GBC.PAGE_END).weight(0, 1),
				// Bouton 2 : Flèche vers la droite
				new GBC(1, 1, 1, 1).anchor(GBC.PAGE_START).weight(0, 1),
				// Remote System
				new GBC(2, 0, 1, 2).fill(GBC.BOTH).weight(1, 1),
		};
		
		// Centre de la fenêtre.
		JPanel p = new JPanel(new GridBagLayout()); {
			p.add(local, constraints[0]);
			p.add(new JPanel().add(left), constraints[1]);
			p.add(new JPanel().add(right), constraints[2]);
			p.add(remote, constraints[3]);
		}
		frame.add(p, BorderLayout.CENTER);
		
		// Sud de la fenêtre.
		p = new JPanel(new BorderLayout()); {
			// En Haut du Panel.
			JPanel q = new JPanel(); {
				q.add(ascii);
				q.add(binary);
				q.add(auto);
			}
			p.add(q, BorderLayout.NORTH);
			// Au Milieu du Panel.
			q = new JPanel(new BorderLayout()); {
				q.add(new JScrollPane(area), BorderLayout.CENTER);
			}
			// En bas du Panel.
			p.add(q, BorderLayout.CENTER);
			q = new JPanel(new BorderLayout()); {
				JPanel r = new JPanel(new GridBagLayout()); {
					for (JButton b : buttons.keySet()) {
						r.add(b, buttons.get(b));
					}
				}
				q.add(r, BorderLayout.CENTER);
			}
			p.add(q, BorderLayout.SOUTH);
		}
		frame.add(p, BorderLayout.SOUTH);
	}
	
	/**
	 *  Création des contrôleurs.
	 */
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Aucun écouteurs.
	}
	
	// POINT D'ENTREE
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientFTP().display();
            }
        });
    }
}
