package serie09;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class GenCellRenderer extends DefaultTreeCellRenderer {	
	// ATTRIBUTS
	private static final int SIZE = 20;

	// CONSTRUCTEUR
    public GenCellRenderer() {
    	setFont(new Font("Verdana", Font.PLAIN, SIZE));
    }
    
    // REQUETES    
    public Component getTreeCellRendererComponent(JTree tree, Object value,
    		boolean sel, boolean expanded, boolean leaf, int row, 
    		boolean hasFocus) {
    	Gender g = ((GenNode) value).getGender();
    	ImageIcon icon = g.getImage();
    	if (leaf) {
    		setLeafIcon(icon);
    	} else if (expanded) {
			setOpenIcon(icon);
		} else {
			setClosedIcon(icon);
		}
    	setBackgroundSelectionColor(g.getBackgroundSelectionColor());
    	if (hasFocus) {
    		setBorderSelectionColor(g.getBorderSelectionColor());
    	}    	
    	return super.getTreeCellRendererComponent(
    		tree, value, sel, expanded, leaf, row, hasFocus
    	);
    }
}
