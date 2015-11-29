package serie07.filter;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * D�finit les filtres pour les listes d'�l�ments filtrables de type E,
 *  bas�s sur des valeurs de type V.
 * @inv <pre>
 *     !acceptNullValue() ==> getValue() != null
 *     filter(list) != null && filter(list) != list
 *     forall e:E :
 *         list.contains(e) && validates(e) <==> filter(list).contains(e) </pre>
 */
public interface Filter<E extends Filtrable<V>, V> {
    // REQUETES
    /**
     * Indique si ce filtre accepte la valeur null.
     */
    boolean acceptNullValue();
    /**
     * Retourne, � partir de list, une nouvelle liste constitu�e des valeurs
     *  filtr�es selon ce filtre lorsqu'il est bas� sur la valeur getValue().
     * @pre <pre>
     *     list != null </pre>
     */
    List<E> filter(List<E> list);
    /**
     * La valeur associ�e � ce filtre.
     */
    V getValue();
    /**
     * Un tableau de tous les �couteurs de changement de la propri�t� value.
     */
    PropertyChangeListener[] getValueChangeListeners();
    /**
     * Indique si l'�l�ment e est accept� par ce filtre lorsqu'il est bas�
     *  sur la valeur getValue().
     * @pre <pre>
     *     e != null
     *     !acceptNullValue() ==> e.filtrableValue() != null </pre>
     */
    boolean validates(E e);
    
    // COMMANDES
    /**
     * Enregistre un �couteur de changement de la propri�t� value.
     */
    void addValueChangeListener(PropertyChangeListener lst);
    /**
     * Retire un �couteur de changement de la propri�t� value.
     */
    void removeValueChangeListener(PropertyChangeListener lst);
    /**
     * Change la valeur associ�e � ce filtre.
     * @pre <pre>
     *     !acceptNullValue() ==> value != null </pre>
     * @post <pre>
     *     getValue() == value </pre>
     */
    void setValue(V value);
}
