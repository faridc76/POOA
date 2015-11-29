package serie01;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class EventsTester {
	// ATTRIBUTS
	private static final int MAIN_FRAME_HEIGHT = 650;
	private static final int MAIN_FRAME_WIDTH = 650;
	private static final int TEST_FRAME_HEIGHT = 200;
	private static final int TEST_FRAME_WIDTH = 100;
	// La fenêtre de contrôle.
	private JFrame mainFrame;
	// La fenêtre de tests.
	private JFrame testFrame;
	// Le bouton "Nouvelle Fenêtre.
	private JButton newWindow;
	// Le bouton RAZ
	private JButton raz;
	// Les zones d'affichages.
	private JTextArea[] areas;
	// Les ascenseurs des zones d'affichages.
	private JScrollPane[] scroll;
	// L'association "ID d'événements - Zones d'Affichage".
	private Map<Integer, JTextArea> board;
	// L'association "Titre - Tableau d'ID d'événements".
	private Map<String, int[]> data;
	private int order, razNbs;
	
	
	// CONSTRUCTEUR
	public EventsTester() {
		order = 1;
		razNbs = 1;
		createView();
		placeComponents();
		createMainFrameController();
	}
	
	// COMMANDES
	/**
	 * Placement de la fenêtre d'application.
	 */
	public void display() {
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	// OUTILS
	/**
	 * Création de la vue.
	 */
	private void createView() {
		mainFrame = new JFrame("Tests sur les événements - Zone d'AFFICHAGE");
		mainFrame.setPreferredSize(
				new Dimension(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT)
				);
		testFrame = new JFrame();
		
		newWindow = new JButton("Nouvelle Fenêtre");
		raz = new JButton("RAZ compteur");
		
		data = new HashMap<String, int[]>(); {
			data.put("MouseListener", new int[] {
					MouseEvent.MOUSE_CLICKED, 
					MouseEvent.MOUSE_PRESSED,
					MouseEvent.MOUSE_RELEASED,
					MouseEvent.MOUSE_ENTERED,
					MouseEvent.MOUSE_EXITED
			});
			data.put("WindowFocusListener", new int[] {
					WindowEvent.WINDOW_GAINED_FOCUS,
					WindowEvent.WINDOW_LOST_FOCUS
			});
			data.put("WindowListener", new int[] {
					WindowEvent.WINDOW_ACTIVATED,
					WindowEvent.WINDOW_OPENED,
					WindowEvent.WINDOW_DEACTIVATED,
					WindowEvent.WINDOW_CLOSED,
					WindowEvent.WINDOW_CLOSING,
					WindowEvent.WINDOW_ICONIFIED,
					WindowEvent.WINDOW_DEICONIFIED
			});
			data.put("KeyListener", new int[] {
					KeyEvent.KEY_PRESSED,
					KeyEvent.KEY_TYPED,
					KeyEvent.KEY_RELEASED
			});
			data.put("WindowStateListener", new int[] {
					WindowEvent.WINDOW_STATE_CHANGED
			});
			data.put("MouseWheelListener", new int[] {
					MouseWheelEvent.MOUSE_WHEEL
			});
			data.put("MouseMotionListener", new int[] {
					MouseEvent.MOUSE_MOVED,
					MouseEvent.MOUSE_DRAGGED
			});
		}		
		
		areas = new JTextArea[data.size()];
		scroll = new JScrollPane[data.size()];
		
		board = new HashMap<Integer, JTextArea>();
		int i = 0;
		for (String s : data.keySet()) {
			scroll[i] = new JScrollPane();
			areas[i] = new JTextArea();
			scroll[i].add(areas[i]);
			scroll[i].setViewportView(areas[i]);
			scroll[i].setBorder(
					BorderFactory.createTitledBorder(
							BorderFactory.createLineBorder(Color.BLACK), s
							)
						);
			for (Integer j : data.get(s)) {
				board.put(j, areas[i]);
			}
			i++;
		}
	}
	
	/**
	 * Placements des composants graphiques de l'application.
	 */
	private void placeComponents() {
		JPanel panel = new JPanel(new BorderLayout()); {
			JPanel p = new JPanel(); {
				p.add(newWindow);
				p.add(raz);
			}
			panel.add(p, BorderLayout.NORTH);
			JPanel q = new JPanel(new GridLayout(3, 3)); {
				for (JScrollPane s : scroll) {
					q.add(s);
				}
				q.setBorder(BorderFactory.createEtchedBorder());
			}
			panel.add(q, BorderLayout.CENTER);
		}
		mainFrame.add(panel);
	}
	
	/**
	 * La fenêtre de confirmation.
	 */
	private int showConfirmDialog() {
		return JOptionPane.showConfirmDialog(
				null, 
				"Attention! Vous allez perdre l'affichage en cours,"
				+ " voulez-vous quand même continuer?", 
				"Avertissement", 
				JOptionPane.YES_NO_OPTION              
			    );
	}
	
	/**
	 * Les contrôleurs de la fenêtre principale.
	 */
	private void createMainFrameController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		newWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = (order > 1) ? showConfirmDialog() : 0;
				if (confirm == 0) {
					testFrame.dispose();
					for (JTextArea a : areas) {
						a.setText("");
					}
					order = 1;
					razNbs = 1;
					createNewTestFrame();
					createTestFrameController();
				}
			}
		});
		
		raz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (JTextArea a : areas) {
					a.append("--- RAZ " + razNbs + " ---\n");
				}
				razNbs++;
				order = 1;
			}
		});
	}
	
	/**
	 * Création de la fenêtre de test.
	 */
	private void createNewTestFrame() {
		testFrame = new JFrame("Zone de Test");
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setPreferredSize(
				new Dimension(TEST_FRAME_WIDTH, TEST_FRAME_HEIGHT)
				);
		testFrame.setVisible(true);
		testFrame.pack();
		testFrame.setLocation(
				mainFrame.getX() + mainFrame.getWidth() - testFrame.getWidth(), 
				mainFrame.getY() + mainFrame.getHeight() - testFrame.getHeight()
				);
	}
	
	/**
	 * Les effets de la zone de tests sur la fenêtre de tests.
	 */
	private void testZone(AWTEvent e) {
		board.get(e.getID()).append(
				order + " " + e.paramString().split(",")[0] + "\n"
				);
		order++;
	}
	
	/**
	 * Les contrôleurs de la fenêtre de test.
	 */
	private void createTestFrameController() {
		testFrame.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				testZone(e);
			}

			public void mouseEntered(MouseEvent e) {
				testZone(e);
			}

			public void mouseExited(MouseEvent e) {
				testZone(e);
			}

			public void mousePressed(MouseEvent e) {
				testZone(e);
			}

			public void mouseReleased(MouseEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addWindowFocusListener(new WindowFocusListener() {			
			public void windowGainedFocus(WindowEvent e) {
				testZone(e);
			}

			public void windowLostFocus(WindowEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {
				testZone(e);
			}

			public void windowClosed(WindowEvent e) {
				testZone(e);
			}

			public void windowClosing(WindowEvent e) {
				testZone(e);
			}

			public void windowDeactivated(WindowEvent e) {
				testZone(e);
			}

			public void windowDeiconified(WindowEvent e) {
				testZone(e);
			}

			public void windowIconified(WindowEvent e) {
				testZone(e);
			}

			public void windowOpened(WindowEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				testZone(e);
			}

			public void keyReleased(KeyEvent e) {
				testZone(e);
			}

			public void keyTyped(KeyEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				testZone(e);
			}
		});
		
		testFrame.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				testZone(e);
			}

			public void mouseMoved(MouseEvent e) {
				testZone(e);
			}
		});
	}
	
	
	// POINT D'ENTREE
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EventsTester().display();
            }
        });
    }
}
