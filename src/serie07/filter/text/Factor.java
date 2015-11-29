package serie07.filter.text;

import serie07.filter.AbstractFilter;
import serie07.filter.Filtrable;

public class Factor<E extends Filtrable<String>> extends
		AbstractFilter<E, String> {

	public Factor() {
		super("");
	}

	public boolean acceptNullValue() {
		return false;
	}

	public boolean validates(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		return e.filtrableValue().contains(getValue());
	}
}
