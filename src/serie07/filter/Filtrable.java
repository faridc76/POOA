package serie07.filter;

/**
 * Lorsqu'on souhaite filtrer des �l�ments par rapport � un filtre portant
 *  sur des valeurs d'un type V, on type ces �l�ments par
 *  <code>Filtrable<V></code>.
 * Ainsi un �l�ment filtrable est un �l�ment dont on peut extraire une valeur
 *  du type V au moyen de <code>filtrableValue</code>.
 */
public interface Filtrable<V> {
    /**
     * La valeur par rapport � laquelle on filtre.
     */
    V filtrableValue();
}
