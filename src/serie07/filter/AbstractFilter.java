package serie07.filter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractFilter<E extends Filtrable<V>, V> implements
		Filter<E, V> {

	// ATTRIBUTS
	private V value;
	private PropertyChangeSupport support;
	
	//CONSTRUCTEUR 
	
	public AbstractFilter(V val) {
		value = val;
	}
	
	// REQUETES
	public abstract boolean acceptNullValue(); 

	public List<E> filter(List<E> list) {
		if (list == null) {
			throw new IllegalArgumentException();
		}
		List<E> filterList = new LinkedList<E>();
		for (E e : list) {
			if (validates(e)) {
				filterList.add(e);
			}
		}	
		return filterList;
	}

	public V getValue() {
		return value;
	}

	public PropertyChangeListener[] getValueChangeListeners() {
		return support.getPropertyChangeListeners();
	}

	public abstract boolean validates(E e);

	// COMMANDES
	
	public void addValueChangeListener(PropertyChangeListener lst) {
		if (lst == null) {
			throw new IllegalArgumentException();
		}
		if (support == null) {
			support = new PropertyChangeSupport(this);
		}
		support.addPropertyChangeListener(lst);
	}


	public void removeValueChangeListener(PropertyChangeListener lst) {
		if (lst == null) {
			throw new IllegalArgumentException();
		}
		support.removePropertyChangeListener(lst);
	}

	public void setValue(V value) {
		if (!acceptNullValue() && value == null) {
			throw new IllegalArgumentException();
		}
		this.value = value;
		support.firePropertyChange("value", null, value);
	}
}
