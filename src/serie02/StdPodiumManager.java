package serie02;

import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class StdPodiumManager<E extends Drawable> 
		implements PodiumManager<E> {
	// ATTRIBUTS
	// Division entière pour récupéré le temps écoulé en secondes.
	private static final int FOR_SECONDES = 1000;
	// Le dernier ordre exécuté.
	private Order lastOrder;
	// Les podiums.
	private Map<Rank, Podium<E>> podiums;
	// Le nombre de coups utilisé.
	private int shotsNb;
	// Le temps écoulé entre le début et la fin d'une partie.
	private int timeDelta;
	// Le temps de départ pour un début de partie.
	private long begin;
	// Annonce la fin de partie.
	private boolean finished;
	// Les dessinables présents sur les podiums
	private Set<E> drawables;
	// Le support sur les changements du veto.
	private VetoableChangeSupport vetoableSupport;
	// Le support sur les changements des propriétés.
	private PropertyChangeSupport propertySupport;
	
	// CONSTRUCTEUR
	public StdPodiumManager(Set<E> e) {
		podiums = new HashMap<Rank, Podium<E>>();
		List<E> list = new LinkedList<E>();
		list.addAll(e);
		for (Rank r : Rank.values()) {
			podiums.put(
				r, new Podium<E>(new StdPodiumModel<E>(list, list.size()))
			);
		}
		drawables = e;
		vetoableSupport = new VetoableChangeSupport(this);
		propertySupport = new PropertyChangeSupport(this);
		reinit();
	}

	// REQUETES
	public Order getLastOrder() {
		return lastOrder;
	}

	public Map<Rank, Podium<E>> getPodiums() {
		return podiums;
	}

	public int getShotsNb() {
		return shotsNb;
	}

	public long getTimeDelta() {
		return timeDelta;
	}

	public boolean isFinished() {
		return finished;
	}

	// COMMANDES	
	public void executeOrder(Order o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
		PodiumModel<E> pL = podiums.get(Rank.WRK_LEFT).getModel();
		PodiumModel<E> pR = podiums.get(Rank.WRK_RIGHT).getModel();
		// Modifications du modèle selon l'ordre demandé.
		switch (o) {
		case LO :
			moveTop(pL, pR);
			break;
		case KI : 
			moveTop(pR, pL);
			break;
		case MA : 
			mountBottom(pL);
			break;
		case NI: 
			mountBottom(pR);
			break;
		default :
			// Ordre spécial : SO
			try {
				vetoableSupport.fireVetoableChange(
						"lastOrder", null, lastOrder
						);
			} catch (PropertyVetoException e) {
				break;
			}
			exchangeTop(pL, pR);
		}
		// MAJ de la propriété simple shotsNb.
		shotsNb++;
		// MAJ de la propriété liée lastOrder.
		lastOrder = o;
		propertySupport.firePropertyChange("lastOrder", null, getLastOrder());
		// MAJ de la propriété simple timeDelta.
		boolean b = goalAchieved();
		int result = (int) (System.currentTimeMillis() / FOR_SECONDES - begin);
		timeDelta =  (b) ? result : 0;
		// MAJ de la propriété liée finished.
		boolean old = finished;
		finished = b;
		propertySupport.firePropertyChange("finished", old, isFinished());
	
	}

	public void reinit() {
		changePodiumModels();
		shotsNb = 0;
		timeDelta = 0;
		begin = System.currentTimeMillis() / FOR_SECONDES;
		finished = false;
		lastOrder = null;
	}

	public synchronized void addPropertyChangeListener(String propName,
			PropertyChangeListener lst) {
		if (lst == null || propName == null) {
			throw new IllegalArgumentException();
		}
		if (propertySupport == null) {
			propertySupport = new PropertyChangeSupport(this);
		}
		propertySupport.addPropertyChangeListener(propName, lst);
	}

	public synchronized void addVetoableChangeListener(
				VetoableChangeListener lst) {
		if (lst == null) {
			throw new IllegalArgumentException();
		}
		if (vetoableSupport == null) {
			vetoableSupport = new VetoableChangeSupport(this);
		}
		vetoableSupport.addVetoableChangeListener(lst);
	}
	
	public synchronized void removePropertyChangeListener(
				PropertyChangeListener lst) {
		if (lst == null) {
			throw new IllegalArgumentException();
		}
		propertySupport.removePropertyChangeListener(lst);
	}

	public synchronized void removeVetoableChangeListener(
				VetoableChangeListener lst) {
		if (lst == null) {
			throw new IllegalArgumentException();
		}
		vetoableSupport.removeVetoableChangeListener(lst);
	}

	// OUTILS
	/**
	 *  Monte le bas de la pile du PodiumModel<E> pm au sommet.
	 */
	private void mountBottom(PodiumModel<E> pm) {
		if (pm.size() > 0) {
			E d = pm.bottom();
			pm.removeBottom();
			pm.addTop(d);
		}
	}
	
	/**
	 *  Dépose le sommet de la pile du PodiumModel<E> pm1 sur
	 *  celui du PodiumModel<E> pm2.
	 */
	private void moveTop(PodiumModel<E> pm1, PodiumModel<E> pm2) {
		if (pm1.size() > 0) {
			E d = pm1.top();
			pm1.removeTop();
			pm2.addTop(d);
		}
	}
	
	/**
	 * Échange les deux sommets des piles des PodiumModel<E> pm1 et pm2.
	 */
	private void exchangeTop(PodiumModel<E> pm1, PodiumModel<E> pm2) {
		if (pm1.size() > 0 && pm2.size() > 0) {	
			E d = pm1.top();
			pm1.removeTop();
			pm1.addTop(pm2.top());
			pm2.removeTop();
			pm2.addTop(d);
		}
	}
	
	/**
	 * Changement des podiums.
	 */
	private void changePodiumModels() {
	    List<List<E>> lst = createRandomElements();
	    lst.addAll(createRandomElements());
	    for (Rank r : Rank.values()) {
	        podiums.get(r).setModel(
	                new StdPodiumModel<E>(
	                        lst.get(r.ordinal()),
	                        drawables.size()
	                )
	        );
	    }
	}
	
	/**
	 * Création d'éléments aléatoires sur une paire de podiums.
	 */
	private List<List<E>> createRandomElements() {
	    final double ratio = 0.5;
	    List<E> list = new ArrayList<E>(drawables.size());
	    List<E> elements = new LinkedList<E>(drawables);
	    for (int i = drawables.size(); i > 0; i--) {
	        int k = ((int) (Math.random() * i));
	        list.add(elements.get(k));
	        elements.remove(k);
	    }
	    List<E> elemsL = new ArrayList<E>(drawables.size());
	    List<E> elemsR = new ArrayList<E>(drawables.size());
	    for (E e : list) {
	        if (Math.random() < ratio) {
	            elemsL.add(e);
	        } else {
	            elemsR.add(e);
	        }
	    }
	    ArrayList<List<E>> result = new ArrayList<List<E>>(2);
	    result.add(elemsL);
	    result.add(elemsR);
	    return result;
	}
	
	/**
	 * Annonce la fin de la partie.
	 */
	private boolean goalAchieved() {
		Rank[] v = Rank.values();
		for (int i = 0, j = v.length / 2; 
			i < v.length / 2 && j < v.length; i++, j++) {
			if (size(v[i]) == size(v[j])) {
				for (int k = 0; k < size(v[i]); k++) {
					if (elementAt(v[i], k) != elementAt(v[j], k)) {
						return false;
					}
				}
			} else {
				return false;
			}
		}		
		return true;
	}
	
	private int size(Rank r) {
		return podiums.get(r).getModel().size();
	}
	
	private E elementAt(Rank r, int i) {
		return podiums.get(r).getModel().elementAt(i);
	}
}
