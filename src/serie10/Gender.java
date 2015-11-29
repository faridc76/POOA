package serie10;

import java.awt.Color;

import javax.swing.ImageIcon;

public enum Gender {

	MALE(
		"fils de ", 
		new Color(200, 210, 255), 
		new Color(99, 130, 191),
		new ImageIcon("src/serie10/images/utilisateur.png")
	),
	FEMALE(
		"fille de ", 
		new Color(255, 175, 175), 
		new Color(191, 88, 88),
		new ImageIcon("src/serie10/images/utilisatrice.png")
	);
	
	// ATTRIBUTS
	private String label;
	private Color background;
	private Color border;
	private ImageIcon icon;
	
	// CONSTRUCTEUR
	private Gender(String l, Color c1, Color c2, ImageIcon i) {
		label = l;
		background = c1;
		border = c2;
		icon = i;
	}
	
	// REQUETES
	public String getLabel() {
		return label;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	
	public Color getBackgroundColor() {
		return background;
	}
	
	public Color getBorderColor() {
		return border;
	}
}
