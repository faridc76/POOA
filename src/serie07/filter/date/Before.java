package serie07.filter.date;

import java.util.Date;

import serie07.filter.AbstractFilter;
import serie07.filter.Filtrable;

/**
 * Implantation du filtre (basé sur des valeurs de type Date) :
 *     "getValue() est une date postérieure à la valeur filtrable associée à e".
 * @inv <pre>
 *     forall e:E :
 *         validates(e) == e.filtrableValue().before(getValue()) </pre>
 */
public class Before<E extends Filtrable<Date>>
        extends AbstractFilter<E, Date> {
    public Before() {
        super(null);
    }
    public boolean acceptNullValue() {
        return true;
    }
    public boolean validates(E e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }
        Date d1 = e.filtrableValue();
        Date d2 = getValue();
        return d2 == null ? true : d1.before(d2);
    }
    @Override
    public String toString() {
        return "Avant";
    }
}
