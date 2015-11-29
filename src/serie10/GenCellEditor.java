package serie10;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;

@SuppressWarnings("serial")
public class GenCellEditor extends DefaultCellEditor {	
	// ATTRIBUTS	
	private JTree tree;

	// CONSTRUCTEUR	
	public GenCellEditor(JTree t, JTextField f) {
		super(f);
		tree = t;
	}

	// REQUETES	
	public boolean stopCellEditing() {
		String[] table = ((String) getCellEditorValue()).split(",");
		String aux = table[0];
		GenNode gen = (GenNode) tree.getSelectionPath().getLastPathComponent();
		int n;
		if (table.length == 2) {
			try {
				n = Integer.parseInt(table[1]);
				if ((n >= 0) && (n <= 1)) {
					gen.setGender(Gender.values()[n]);
				}					
			} catch (NumberFormatException e) {
				// Rien.
			}
		} else if (table.length > 2) {
			for (int i = 1; i < table.length - 1; i++) {
				aux += "," + table[i];
			}
			try {
				n = Integer.parseInt(table[table.length - 1]);
				if ((n >= 0) && (n <= 1)) {
					gen.setGender(Gender.values()[n]);
				}				
			} catch (NumberFormatException e) {
				// Rien.
			}
		}
		gen.setName(aux);
		return delegate.stopCellEditing();
	}
}
