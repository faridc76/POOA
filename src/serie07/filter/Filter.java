package serie07.filter;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Définit les filtres pour les listes d'éléments filtrables de type E,
 *  basés sur des valeurs de type V.
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
     * Retourne, à partir de list, une nouvelle liste constituée des valeurs
     *  filtrées selon ce filtre lorsqu'il est basé sur la valeur getValue().
     * @pre <pre>
     *     list != null </pre>
     */
    List<E> filter(List<E> list);
    /**
     * La valeur associée à ce filtre.
     */
    V getValue();
    /**
     * Un tableau de tous les écouteurs de changement de la propriété value.
     */
    PropertyChangeListener[] getValueChangeListeners();
    /**
     * Indique si l'élément e est accepté par ce filtre lorsqu'il est basé
     *  sur la valeur getValue().
     * @pre <pre>
     *     e != null
     *     !acceptNullValue() ==> e.filtrableValue() != null </pre>
     */
    boolean validates(E e);
    
    // COMMANDES
    /**
     * Enregistre un écouteur de changement de la propriété value.
     */
    void addValueChangeListener(PropertyChangeListener lst);
    /**
     * Retire un écouteur de changement de la propriété value.
     */
    void removeValueChangeListener(PropertyChangeListener lst);
    /**
     * Change la valeur associée à ce filtre.
     * @pre <pre>
     *     !acceptNullValue() ==> value != null </pre>
     * @post <pre>
     *     getValue() == value </pre>
     */
    void setValue(V value);
}
