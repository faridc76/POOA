package serie04;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupDistrib  extends Distrib  {
	
	// ATTRIBUT
	private static final Alignment CENTER = GroupLayout.Alignment.CENTER;

	public GroupDistrib(String title) {
		super(title);
	}
	
	protected void placeComponents() {
		// Taille des Boutons.
		JButton[] drinks = new JButton[DRINKS_NB]; 
		for (int i = 0; i < drinks.length; i++) {
			drinks[i] = getButton(BKey.values()[i]);
		}
		setButtonsSize(drinks);
		setButtonsSize(getButton(BKey.INS), getButton(BKey.CANCEL));
		
		// Taille des TextField.
		JTextField[] tf = new JTextField[FKey.values().length];
		for (int i = 0; i < tf.length; i++) {
			tf[i] = getField(FKey.values()[i]);
		}
		setTextFieldSize(tf);
		
		// Association entre Conteneur et Gestionnaire
		JPanel p = new JPanel(null);
		GroupLayout group = new GroupLayout(p);
		p.setLayout(group);
		// Les Écarts.
		group.setAutoCreateGaps(true);
		group.setAutoCreateContainerGaps(true);
		// PG : Groupe parallèle, SG : Groupe Séquentielle.
		// Placement horizontal.
		group.setHorizontalGroup(
			// PG
			group.createParallelGroup(CENTER)
				.addComponent(getLabel(LKey.BACK))
				.addComponent(getLabel(LKey.CREDIT))
				// SG
				.addGroup(group.createSequentialGroup()
					// PG
					.addGroup(group.createParallelGroup()
						// SG
						.addGroup(group.createSequentialGroup()
							// PG
							.addGroup(group.createParallelGroup()
								.addComponent(getButton(BKey.ORANGE))
								.addComponent(getButton(BKey.CHOCO))
								.addComponent(getButton(BKey.COFFEE))
							)
							// PG
							.addGroup(group.createParallelGroup()
								.addComponent(getPost(PKey.ORANGE))
								.addComponent(getPost(PKey.CHOCO))
								.addComponent(getPost(PKey.COFFEE))
							)
						)
						// SG
						.addGroup(group.createSequentialGroup()
							.addComponent(getLabel(LKey.DRINK))
							.addComponent(getField(FKey.DRINK))
						)
					)
					// PG
					.addGroup(group.createParallelGroup()
						// SG
						.addGroup(group.createSequentialGroup()
							//PG
							.addGroup(group.createParallelGroup()
								.addComponent(getButton(BKey.INS))
								.addComponent(getButton(BKey.CANCEL))
							)
							.addComponent(getField(FKey.INS))
							.addComponent(getPost(PKey.INS))
						)
						// SG
						.addGroup(group.createSequentialGroup()
							.addComponent(getLabel(LKey.MONEY))
							.addComponent(getField(FKey.MONEY))
							.addComponent(getPost(PKey.MONEY))
						)
					)
				)
				.addComponent(getButton(BKey.TAKE))
			);
			// Placement vertical
			group.setVerticalGroup(
				// SG
				group.createSequentialGroup()
					.addComponent(getLabel(LKey.BACK))
					.addComponent(getLabel(LKey.CREDIT))
					// PG
					.addGroup(group.createParallelGroup()
						// SG
						.addGroup(group.createSequentialGroup()
							// PG
							.addGroup(group.createParallelGroup(CENTER)
								.addComponent(getButton(BKey.ORANGE))
								.addComponent(getPost(PKey.ORANGE))
							)
							// PG
							.addGroup(group.createParallelGroup(CENTER)
								.addComponent(getButton(BKey.CHOCO))
								.addComponent(getPost(PKey.CHOCO))
							)
							// PG
							.addGroup(group.createParallelGroup(CENTER)
								.addComponent(getButton(BKey.COFFEE))
								.addComponent(getPost(PKey.COFFEE))
							)
						)
						// SG
						.addGroup(group.createSequentialGroup()
							// PG
							.addGroup(group.createParallelGroup(CENTER)
								.addComponent(getButton(BKey.INS))
								.addComponent(getField(FKey.INS))
								.addComponent(getPost(PKey.INS))
							)
							.addComponent(getButton(BKey.CANCEL))
						)
					)
					// PG
					.addGroup(group.createParallelGroup()
						// PG
						.addGroup(group.createParallelGroup(CENTER)
							.addComponent(getLabel(LKey.DRINK))
							.addComponent(getField(FKey.DRINK))
						)
						// PG
						.addGroup(group.createParallelGroup(CENTER)
							.addComponent(getLabel(LKey.MONEY))
							.addComponent(getField(FKey.MONEY))
							.addComponent(getPost(PKey.MONEY))
						)
					)
					.addComponent(getButton(BKey.TAKE))
			);
		getFrame().add(p);	
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GroupDistrib("GroupDistrib de boissons").display();
			}
		});
	}
	
	//OUTILS

	// La Largeur du plus gros bouton passé en argument.
	private int getMaxButtonsWidth(JButton... button) {
		int max = 0;
		for (int i = 0; i < button.length; i++) {
			if (button[i].getPreferredSize().width > max) {
				max = button[i].getPreferredSize().width;
			}
		}
		return max;
	}
	
	// Changement de la largeur des boutons.
	private void setButtonsSize(JButton... b) {
		int width = getMaxButtonsWidth(b);
		for (int i = 0; i < b.length; i++) {
			Dimension dim = new Dimension(
				width,
				b[i].getPreferredSize().height
			);
			b[i].setPreferredSize(dim);
			b[i].setMaximumSize(dim);
		}
	}
	
	// Ajuste la taille des TextField 
	private void setTextFieldSize(JTextField... tf) {
		for (int i = 0; i < tf.length; i++) {
			tf[i].setMaximumSize(
				new Dimension(
					Short.MAX_VALUE,
					tf[i].getPreferredSize().height
			));
		}
	}
	
	
}
