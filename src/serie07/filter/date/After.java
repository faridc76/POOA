package serie07.filter.date;

import java.util.Date;

import serie07.filter.AbstractFilter;
import serie07.filter.Filtrable;

/**
 * Implantation du filtre (basé sur des valeurs de type Date) :
 *     "getValue() est une date antérieure à la valeur filtrable associée à e".
 * @inv <pre>
 *     forall e:E :
 *         validates(e) == e.filtrableValue().after(getValue()) </pre>
 */
public class After<E extends Filtrable<Date>>
        extends AbstractFilter<E, Date> {
    public After() {
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
        return d2 == null ? true : d1.after(d2);
    }
    @Override
    public String toString() {
        return "Après";
    }
}
