package serie02;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Implémentation standard de PodiumModel.
 */
public class StdPodiumModel<E extends Drawable> implements PodiumModel<E> {
    // ATTRIBUTS
	// Les éléments du podium.
    private List<E> data;
    // La capacité du podium.
    private int capacity;
    // L'évènement pour le changement du modèle.
    private transient ChangeEvent changeEvent;
    // Une liste d'écouteurs.
    private EventListenerList listenerList;

    // CONSTRUCTEURS
    public StdPodiumModel(List<E> init, int capacity) {
        if (init == null) {
            throw new IllegalArgumentException();
        }
        for (E e : init) {
            if (e == null) {
                throw new IllegalArgumentException();
            }
        }
        changeEvent = null;
        listenerList = new EventListenerList();
        this.capacity = capacity;
        data = new ArrayList<E>(init);
    }
    
    public StdPodiumModel() {
        this(new ArrayList<E>(), 0);
    }

    // REQUETES
    public E bottom() {
        if (size() <= 0) {
            throw new IllegalStateException();
        }
        return data.get(0);
    }
    
    public E elementAt(int i) {
        if (i < 0 || i >= capacity()) {
            throw new IllegalArgumentException();
        }
        if (i < size()) {
            return data.get(i);
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if ((o == null) || (o.getClass() != getClass())) {
            return false;
        }
        StdPodiumModel<E> arg = (StdPodiumModel<E>) o;
        boolean result = (arg.capacity == capacity
                && data.size() == arg.data.size());
        for (int i = 0; result && (i < data.size()); i++) {
            result = data.get(i).equals(arg.data.get(i));
        }
        return result;
    }
    
    public int capacity() {
        return capacity;
    }
    
    public int size() {
        return data.size();
    }
    
    public E top() {
        if (size() <= 0) {
            throw new IllegalStateException();
        }
        return data.get(data.size() - 1);
    }
    
    public int hashCode() {
        return data.hashCode() + capacity;
    }
    
    public String toString() {
        String res = "[";
        for (int i = 0; i < size(); i++) {
            res += data.get(i) + "|";
        }
        return capacity + "/" + res + "]";
    }

    // COMMANDES
    public void addTop(E elem) {
        if (elem == null) {
            throw new IllegalArgumentException();
        }
        if (size() >= capacity()) {
            throw new IllegalStateException(
                "Ne peut ajouter en haut : la structure est pleine"
            );
        }
        data.add(elem);
        fireStateChanged();
    }
    
    public void removeBottom() {
        if (size() <= 0) {
            throw new IllegalStateException(
                "Ne peut retirer en bas : la structure est vide"
            );
        }
        data.remove(0);
        fireStateChanged();
    }
    
    public void removeTop() {
        if (size() <= 0) {
            throw new IllegalStateException(
                "Ne peut retirer en haut : la structure est vide"
            );
        }
        data.remove(size() - 1);
        fireStateChanged();
    }
    
	public void addChangeListener(ChangeListener cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		listenerList.add(ChangeListener.class, cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		listenerList.remove(ChangeListener.class, cl);
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
