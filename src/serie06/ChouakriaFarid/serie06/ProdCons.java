package serie06;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import serie06.sentence.SentenceEvent;
import serie06.sentence.SentenceListener;

public class ProdCons {
	// ATTRIBUTS
	// Temps d'attente du Croque-Mort.
	private static final long SLEEP_TIME = 200;
	// Nombre de consommateurs et producteurs.
	private static final int CONSUMERS_NBS = 1, PRODUCERS_NBS = 3;
	// Tailles préférées de la fenêtre
	private static final int FRAME_WIDTH = 850, FRAME_HEIGHT = 700;
	// Taille du texte de la JTextArea.
	private static final int FONT_SIZE = 12;
	// Verrou du Croque-Mort.
	private static Object dodolock = new Object();
	// La fenêtre principale.
	private JFrame frame;
	// La zone d'affichage.
	private JTextArea area;
	// Le bouton 'Démarrer'.
	private JButton start;
	// Le Croque-Mort.
	private JCheckBox check;
	// Les consommateurs et producteurs.
	private List<Actor> consumers, producers;
	// Indique si le croque-mort est endormi.
	private boolean dodo;
	
	// CONSTRUCTEURS
	public ProdCons() {
		// Le croque-mort dort au début de l'application.
		dodo = true;
		createModel();
		createView();
		placeComponents();
		createController();
	}

	// COMMANDES
	/**
	 * Placement et visibilité de la fenêtre.
	 */
	public void display() {
       frame.pack();
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
    }

	// OUTILS
	// Créations des Modèles.
	private void createModel() {
		Box box = new UnsafeBox();
		consumers = new LinkedList<Actor>(); {
			for (int i = 0; i < CONSUMERS_NBS; i++) {
				consumers.add(new StdConsumer("C" + i, box));
			}
		}
		producers = new LinkedList<Actor>(); {
			for (int i = 0; i < PRODUCERS_NBS; i++) {
				producers.add(new StdProducer("P" + i, box));
			}
		}
	}
	
	// Création de la vue.
	private void createView() {
		frame = new JFrame("Producteurs & Consommateurs"); {
			frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		}
		start = new JButton("Démarrer");
		area = new JTextArea(); {
			area.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE));
			area.setEditable(false);
		}
		check = new JCheckBox("avec croque-mort");
	}
	
	// Placement des Composants.
	private void placeComponents() {
		JPanel p = new JPanel(); {
			p.add(start);
			p.add(check);
		}
		frame.add(p, BorderLayout.NORTH);
		p = new JPanel(new BorderLayout()); {
			p.add(new JScrollPane(area));
		}
		frame.add(p, BorderLayout.CENTER);
	}
	
	// Création des Contrôleurs.
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for (Actor p : producers) {
			p.addSentenceListener(new SentenceListener() {
				public void sentenceSaid(SentenceEvent e) {
					area.append(e.getSentence());
				}
			});
		}
		
		for (Actor c : consumers) {
			c.addSentenceListener(new SentenceListener() {
				public void sentenceSaid(SentenceEvent e) {
					area.append(e.getSentence());
				}
			});
		}
		
		// Bouton démarrer
		// Efface le texte entre chaque opération.
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				Thread t = new Thread(new Runnable() {
					public void run() {
						for (Actor c : consumers) {
							if (c.isActive()) {
								c.stop();
							}
						}
						for (Actor p : producers) {
							if (p.isActive()) {
								p.stop();
							}
						}
						area.setText(
							(check.isSelected()) ? "" 
							: area.getText() + "----------------\n"
						);
						for (Actor p : producers) {
							p.start();
						}
						for (Actor c : consumers) {
							c.start();
						}
						if (!check.isSelected()) {
							start.setEnabled(true);
						}
					}
				});
				t.start();
			}
		});
		
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (check.isSelected()) {
					dodo = false;
					synchronized (dodolock) {
						dodolock.notifyAll();
					}
				} else {
					dodo = true;
				}
			}
		});

		// Lancement du Croque-Mort.
		croqueMort();
	}
	
	// Méthode utilisée dans croqueMort.
	private void killActors(List<Actor> a1, List<Actor> a2) {
		// Tous Morts.
		boolean allDeath = true;
		// En attente.
		boolean waiting = true;
		List<Actor> onWait = new LinkedList<Actor>();
		// On vérifie si tout les producteurs sont morts.
		for (Actor p : a1) {
			if (p.isActive()) {
				allDeath = false;
			}
		}
		if (allDeath) { // Si ils le sont tous.
			// On vérifie si tout les consommateurs vivants sont en attente.
			for (Actor c : a2) {
				if (c.isActive()) {
					if (!c.isWaitingOnBox()) {
						waiting = false;
					}
					onWait.add(c);
				}
			}
			if (waiting) { // Si ils le sont tous.
				// On arrête tous les acteurs en attentes.
				for (Actor a : onWait) {
					a.stop();
				}
				// On réactive le bouton.
				start.setEnabled(true);
			}
		}
	}
	
	// Méthode du Croque-Mort.
	// Crée un thread qui s'assurera de la mort des acteurs bloqués.
	private void croqueMort() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					synchronized (dodolock) {
						while (dodo) {
							try {
								dodolock.wait();
							} catch (InterruptedException e) {
								return;
							}
						}
						// Section critique.
						killActors(producers, consumers);
						killActors(consumers, producers);
						// Section restante.
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							return;
						}
					}
				}
			}
		});
		t.start();
	}
	
	/**
	 * Point d'entrée de l'application.
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProdCons().display();
            }
        });
    }
}
