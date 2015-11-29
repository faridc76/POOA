package serie06;

public class StdConsumer extends AbstractActor {
	
	public StdConsumer(String s, Box b) {
		super(s, b);
	}

	protected boolean canUseBox() {
		return !getBox().isEmpty();
	}

	protected void useBox() {
		sentence("box --> " + getBox().getValue());
		getBox().clean();
	}
}
