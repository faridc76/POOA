package serie07;

import java.util.Collection;

import javax.swing.ListModel;

import serie07.filter.Filter;
import serie07.filter.Filtrable;

/**
 * Mod�les de listes filtrables.
 * Un tel mod�le est constitu� de deux listes :
 * <ol>
 *   <li>Une liste initiale comprenant tous les �l�ments.</li>
 *   <li>Une liste filtr�e comprenant parmi les �l�ments initiaux ceux qui
 *       sont accept�s par le filtre.</li>
 * </ol>
 * Le mod�le, tel qu'il est pr�sent� par ListModel, donne acc�s � la liste
 *  filtr�e ; on rajoute ici les m�thodes n�cessaires pour acc�der � la liste
 *  initiale.
 * E est le type des �l�ments du mod�le ; le type des valeurs associ�es
 *  au filtre du mod�le est V.
 * @inv <pre>
 *     0 <= getSize() <= getUnfilteredSize()
 *     getFilter() == null ==> getSize() == getUnfilteredSize()
 *     forall i:[0..getUnfilteredSize()[ :
 *         getFilter().validates(getUnfilteredElement(i))
 *             <==> exists j:[0..getSize()[ :
 *                      getElementAt(j) == getUnfilteredElement(i) </pre>
 */
public interface FiltrableListModel<E extends Filtrable<V>, V>
        extends ListModel {
    // REQUETES
    /**
     * Le filtre actuel de ce mod�le filtrable.
     */
    Filter<E, V> getFilter();
    /**
     * Le i�me �l�ment de la liste filtr�e.
     * @pre <pre>
     *     0 <= i < getSize() </pre>
     */
    E getElementAt(int i);
    /**
     * Le nombre total d'�l�ments de la liste filtr�e.
     */
    int getSize();
    /**
     * Le i�me �l�ment de la liste initiale.
     * @pre <pre>
     *     0 <= i < getUnfilteredSize() </pre>
     */
    E getUnfilteredElement(int i);
    /**
     * Le nombre total d'�l�ments de la liste initiale.
     */
    int getUnfilteredSize();

    // COMMANDES
    /**
     * Ajoute un �l�ment � la fin de la liste initiale.
     * @pre <pre>
     *     element != null </pre>
     * @post <pre>
     *     getUnfilteredSize() == old getUnfilteredSize() + 1
     *     getUnfilteredElement(getUnfilteredSize() - 1) == element </pre>
     */
    void addElement(E element);
    /**
     * Fixe le filtre de ce mod�le.
     * @post <pre>
     *     getFilter() == filter </pre>
     */
    void setFilter(Filter<E, V> filter);
    /**
     * R�initialise le mod�le avec tous les �l�ments de c.
     * @pre <pre>
     *     c != null </pre>
     * @post <pre>
     *     getUnfilteredSize() == c.getSize()
     *     forall e:E :
     *         c.contains(e)
     *             <==> exists i:[0..getUnfilteredSize()[ :
     *                     getUnfilteredElement(i) == e </pre>
     */
    void setElements(Collection<E> c);
}
