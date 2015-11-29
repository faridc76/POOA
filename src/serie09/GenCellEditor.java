package serie09;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

public class GenCellEditor extends DefaultTreeCellEditor {
	GenCellEditor(JTree t, DefaultTreeCellRenderer r) {
		super(t, r);
	}
	/**
	 * Apparemment, cette méthode ne configure pas son renderer lorsqu'elle
	 * s'exécute... Bug ?
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree t, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		renderer.getTreeCellRendererComponent(t, value, isSelected,
				expanded, leaf, row, false);
		return super.getTreeCellEditorComponent(t, value, isSelected,
				expanded, leaf, row);
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
		return this.stopCellEditing();
	}
}