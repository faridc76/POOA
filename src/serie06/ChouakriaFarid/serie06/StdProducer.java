package serie06;

public class StdProducer extends AbstractActor {
	
	public StdProducer(String s, Box b) {
		super(s, b);
	}

	protected boolean canUseBox() {
		return getBox().isEmpty();
	}

	protected void useBox() {
		int value = 1 + (int) (Math.random() * 10);
		sentence("box <-- " + value);
		getBox().fill(value);
	}
}
