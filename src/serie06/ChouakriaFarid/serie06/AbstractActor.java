package serie06;

import serie06.sentence.SentenceListener;
import serie06.sentence.SentenceSupport;

public abstract class AbstractActor implements Actor {
	// ATTRIBUTS
	private static final int MAX_ITER = 1;
	private Box box;
	private Thread thread;
	private boolean wait;
	private SentenceSupport support;
	private Formatter format;
	private static final Object LOCK = new Object();
	
	public AbstractActor(String s, Box b) {
		box = b;
		format = new Formatter(s);
		support = new SentenceSupport(this);
	}

	// REQUETES	
	public Box getBox() {
		return box;
	}

	public int getMaxIterNb() {
		return MAX_ITER;
	}

	public SentenceListener[] getSentenceListeners() {
		return support.getSentenceListeners();
	}

	public boolean isActive() {
		return thread != null && thread.isAlive();
	}

	public boolean isWaitingOnBox() {
		return wait;
	}

	// COMMANDES	
	public void addSentenceListener(SentenceListener listener) {
		support.addSentenceListener(listener);
	}

	public void removeSentenceListener(SentenceListener listener) {
		support.removeSentenceListener(listener);
	}
	
	protected void sentence(String s) {
		synchronized (LOCK) {
			support.fireSentenceSaid(format.format(s)); 
		}
	}

	public void start() {
		if (isActive()) {
			throw new IllegalStateException();
		}
		thread = new Thread(new Runnable() {
			public void run() {
				sentence("Naissance");
				for (int i = 0; i < getMaxIterNb(); i++) {
					sentence("Début de l'étape " + i);
					synchronized (box) {
						while (!canUseBox()) {
							wait = true;
							sentence("Suspendu");
							try {
								box.wait();
								sentence("Réactivé");
								//wait = false;
							} catch (InterruptedException e) {
								//wait = false;
								sentence("Mort Subite");
								// Un return pour sortir du run.
								return;
							}
						}
						// Section Critique
						useBox();
						// Section restante
						box.notifyAll();
					}
				}
				sentence("Mort naturelle");
			}	
		});
		Formatter.resetTime();
		thread.start();
	}

	public void stop() {
		if (!isActive()) {
			throw new IllegalStateException();
		}
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			thread = null;
		}
	}
	
	protected abstract boolean canUseBox();
	
	protected abstract void useBox();

}
