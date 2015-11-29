package serie02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import serie02.PodiumManager.Order;
import serie02.PodiumManager.Rank;

public class CrazyCircus {
	// ATTRIBUTS
	private static final int LEVEL = 3;
	private JFrame frame;
	private JButton lo, ki, ma, ni, so, newGame;
	private JTextArea area;
	private JCheckBox authorize;
	private PodiumManager<AnimalColor> podiums;
	private Set<AnimalColor> animals;
	
	// CONSTRUCTEUR
	public CrazyCircus() {
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
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// OUTILS
	/**
	 * Création de la vue.
	 */
	private void createView() {
		frame = new JFrame("Crazy Circus");
		lo = new JButton("Lo : G>D");
		ki = new JButton("Ki : G<D");
		ma = new JButton("Ma : ^G");
		ni = new JButton("Ni : ^D");
		so = new JButton("So : <->");
		newGame = new JButton("Nouvelle Partie");
		authorize = new JCheckBox("Autoriser SO");
		area = new JTextArea(3, 8); {
			area.setLineWrap(true);
			area.setEditable(false);
		}
		animals = new HashSet<AnimalColor>();
		for (int i = 0; i < LEVEL; i++) {
			animals.add(AnimalColor.values()[i]);
		}
		podiums = new StdPodiumManager<AnimalColor>(animals);
	}
	
	/**
	 * Placement des composants, en accord avec l'application fourni.
	 */
	private void placeComponent() {
		// Les boutons à droite.
		JPanel p = new JPanel(new BorderLayout()); {
			JPanel q = new JPanel(new GridLayout(0, 1)); {
				q.add(lo);
				q.add(ki);
				q.add(ma);
				q.add(ni);
				q.add(so);
				JPanel r = new JPanel(); {
					r.add(authorize);
					r.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				q.add(r);
				q.add(newGame);
			}
			p.add(q, BorderLayout.NORTH);
		}
		frame.add(p, BorderLayout.EAST);
		// Les podiums à gauche.
		p = new JPanel(new BorderLayout()); {
			JPanel q = new JPanel(new GridLayout(1, 0)); {
				int size = podiums.getPodiums().size();
				for (int i = 0; i < size / 2; i++) {
					q.add(podiums.getPodiums().get(Rank.values()[i]));
				}
				q.add(new JLabel());
				for (int i = size / 2; i < size; i++) {
					q.add(podiums.getPodiums().get(Rank.values()[i]));
				}						
			}
			p.add(q, BorderLayout.CENTER);
			q = new JPanel(new BorderLayout()); {
				q.add(new JLabel("    Départ"), BorderLayout.WEST);
				q.add(new JLabel("Objectif   "), BorderLayout.EAST);
			}
			p.add(q, BorderLayout.SOUTH);
		}		
		frame.add(p, BorderLayout.WEST);
		// Le texte en bas.
		p = new JPanel(new BorderLayout()); {
			p.add(new JScrollPane(area), BorderLayout.CENTER);
		}
		frame.add(p, BorderLayout.SOUTH);
	}
	
	/**
	 *  Création des contrôleurs.
	 */
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Écouteur sur la propriété lastOrder.
		podiums.addPropertyChangeListener("lastOrder", 
				new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				area.append(podiums.getLastOrder().name() + " ");
			}
		});
		
		// Écouteur sur la propriété finished.
		podiums.addPropertyChangeListener("finished", 
				new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				area.append("\ngagné en " + podiums.getShotsNb() + " coups et "
						+ podiums.getTimeDelta() + " s.");
				setEnabled(false);
			}
		});
		
		// Écouteur sur les veto.
		podiums.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				if (!authorize.isSelected()) {
					throw new PropertyVetoException("Non autorisé", evt);
				}
			}
		});
		
		// Les Actions.
		lo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.executeOrder(Order.LO);
			}
		});
		
		ki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.executeOrder(Order.KI);
			}
		});
		
		ma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.executeOrder(Order.MA);
			}
		});
		
		ni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.executeOrder(Order.NI);
			}
		});
		
		so.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.executeOrder(Order.SO);
			}
		});
		
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				podiums.reinit();
				setEnabled(true);
				area.setText("");
			}
		});
	}

	/**
	 * Commande d'activation des boutons.
	 */
	private void setEnabled(boolean b) {
		lo.setEnabled(b);
		ki.setEnabled(b);
		ma.setEnabled(b);
		ni.setEnabled(b);
		so.setEnabled(b);
	}
	
	// POINT D'ENTREE
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CrazyCircus().display();
            }
        });
    }
}
