package serie07.filter.text;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import serie07.filter.AbstractFilter;
import serie07.filter.Filtrable;

/**
 * Implantation du filtre (basé sur des valeurs de type String) :
 *   "getValue() est une regexp reconnaissant la valeur filtrable associée à e".
 * @inv <pre>
 *     forall e:E :
 *         validates(e) ==
 *             l'automate associé à getValue()
 *             reconnaît e.filtrableValue() </pre>
 */
public class PatternMatching<E extends Filtrable<String>>
        extends AbstractFilter<E, String> {
    private static final Pattern EMPTY = Pattern.compile("");
    private Pattern pattern;
    public PatternMatching() {
        super("");
        pattern = EMPTY;
    }
    public boolean acceptNullValue() {
        return false;
    }
    public boolean validates(E e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }
        if (e.filtrableValue() == null) {
            throw new IllegalArgumentException();
        }
        String v = getValue();
        if ("".equals(v)) {
            pattern = EMPTY;
        } else if (!pattern.pattern().equals(v)) {
            try {
                pattern = Pattern.compile(v);
            } catch (PatternSyntaxException pse) {
                pattern = EMPTY;
            }
        }
        return pattern.matcher(e.filtrableValue()).matches();
    }
    @Override
    public String toString() {
        return "RegExp";
    }
}
