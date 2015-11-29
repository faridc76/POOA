package serie07.filter;

/**
 * Lorsqu'on souhaite filtrer des éléments par rapport à un filtre portant
 *  sur des valeurs d'un type V, on type ces éléments par
 *  <code>Filtrable<V></code>.
 * Ainsi un élément filtrable est un élément dont on peut extraire une valeur
 *  du type V au moyen de <code>filtrableValue</code>.
 */
public interface Filtrable<V> {
    /**
     * La valeur par rapport à laquelle on filtre.
     */
    V filtrableValue();
}
