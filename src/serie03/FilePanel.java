package serie03;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class FilePanel extends JPanel {

    private enum Key {
        CHG_DIR("ChgDir"),
        MK_DIR("MkDir"),
        VIEW("View"),
        EXEC("Exec"),
        REN("Rename"),
        DEL("Delete"),
        FRESH("Refresh"),
        INFO("DirInfo");
        private String label;
        Key(String lbl) { label = lbl; }
    }

    // ATTRIBUTS

    private static final int[] SIZES = {
        FtpTableModel.ICON_WIDTH + 10, 130, 75, 50
    };
    private static final int TABLE_HEIGHT = 300;

    private JComboBox dir;
    private JTable content;
    private JTextField filter;
    private Map<Key, JButton> buttons;

    // CONSTRUCTEURS

    public FilePanel(String title, String path) {
        super(null);
        createView(path);
        placeComponents(title);
    }

    // OUTILS
    
    private void createView(String path) {
        // Ici, on affiche des listes de fichiers dans les tables,
        // mais c'est juste pour remplir un vide affreux...
        String rep = new File(path).getAbsolutePath();
        dir = new JComboBox(new Object[] {rep});
        content = createTable(rep);
        filter = new JTextField("*.*");
        buttons = new EnumMap<Key, JButton>(Key.class);
        for (Key k : Key.values()) {
            buttons.put(k, new JButton(k.label));
        }
    }
    
    private JTable createTable(String rep) {
        JTable table = new JTable(new FtpTableModel(rep));
        table.setShowGrid(false);
        table.setRowHeight(FtpTableModel.ICON_HEIGHT + 1);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        int width = 0;
        for (int w : SIZES) {
            width += w;
        }
        table.setPreferredScrollableViewportSize(
                new Dimension(width, TABLE_HEIGHT)
        );
        for (int i = 0; i < SIZES.length; i++) {
            TableColumn c = table.getColumnModel().getColumn(i);
            c.setPreferredWidth(SIZES[i]);
        }
        return table;
    }
    
    private void placeComponents(String title) {
    	// Disposition sous forme de GridBagLayout.
    	setLayout(new GridBagLayout());
    	// Les GridBagConstraints associées.
    	GBC[] constraints = new GBC[] {
    			// La JComboBox.
    			new GBC(0 , 0, 2, 1).fill(GBC.BOTH).insets(5).weight(1, 0),
    			// La JTable.
    			new GBC(0 , 1, 1, 10).fill(GBC.BOTH).insets(0, 5, 0, 0)
    			.weight(1, 1),
    			// Bouton : ChgDir
    			constraints(1),
    			// Bouton : MkDir
    			constraints(2),
    			// Le JTextField
    			new GBC(1 , 3, 1, 1).fill(GBC.BOTH).insets(1, 5, 1, 5),
    			// Bouton : View
    			constraints(4),
    			// Bouton : Exec
    			constraints(5),
    			// Bouton : Rename
    			constraints(6),
    			// Bouton : Delete
    			constraints(7),
    			// Bouton : Refresh
    			constraints(8),
    			// Bouton : DirInfo
    			constraints(9),
    	};
    	// Les Placements des composants (juste pour la vue, sans écouteurs)
    	add(dir, constraints[0]);
    	add(new JScrollPane(content), constraints[1]);
    	add(new JButton("ChgDir"), constraints[2]);
    	add(new JButton("MkDir"), constraints[3]);
    	add(filter, constraints[4]);
    	add(new JButton("View"), constraints[5]);
    	add(new JButton("Exec"), constraints[6]);
    	add(new JButton("Rename"), constraints[7]);
    	add(new JButton("Delete"), constraints[8]);
    	add(new JButton("Refresh"), constraints[9]);
    	add(new JButton("DirInfo"), constraints[10]);
    	// La bordure de titre.
    	setBorder(BorderFactory.createTitledBorder(title));
    }
    
    private GBC constraints(int i) {
    	return new GBC(1 , i, 1, 1).fill(GBC.BOTH).insets(1, 5, 1, 5);
    }
}
