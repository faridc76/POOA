package serie07;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import serie07.filter.Filter;
import serie07.filter.Filtrable;


@SuppressWarnings("serial")
public class StdFiltrableListModel<E extends Filtrable<V>, V> 
extends AbstractListModel implements FiltrableListModel<E, V> {
	
	// ATTRIBUTS
	private Filter<E, V> filter;
	private List<E> initList;
	private List<E> filterList;
	private PropertyChangeListener propertyChange;
	
	//CONSTRUCTEUR
	public StdFiltrableListModel() {
		initList = new LinkedList<E>();
		filterList = new LinkedList<E>();
		propertyChange = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				filtering();
			}
		};
	}
	
	// REQUETES
	public Filter<E, V> getFilter() {
		return filter;
	}
	
	public E getElementAt(int i) {
		return filterList.get(i);
	}

	public int getSize() {
		return filterList.size();
	}

	public E getUnfilteredElement(int i) {
		return initList.get(i);
	}

	public int getUnfilteredSize() {
		return initList.size();
	}

	// COMMANDES
	public void addElement(E element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		initList.add(element);
		if (filter.validates(element)) {
			filterList.add(element);
		}
	}

	public void setFilter(Filter<E, V> filter) {
		if (this.filter != null) {
			this.filter.removeValueChangeListener(propertyChange);
		}
		this.filter = filter;
		filter.addValueChangeListener(propertyChange);
		filtering();
	}

	public void setElements(Collection<E> c) {
		if (c == null) {
			throw new IllegalArgumentException();
		}
		initList.clear();
		for (E e : c) {
			initList.add(e);
		}
		filtering();
	}
	
	// OUTILS
		private void filtering() {
			filterList = filter.filter(initList);
			fireContentsChanged(this, 0, getSize());
		}
	
}