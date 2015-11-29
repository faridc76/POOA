package serie08;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class NoteSheet extends JPanel {
	// ATTRIBUTS
	private static final long serialVersionUID = 700020882161383402L;
	private JTable table;
	private NoteTableModel tableModel;
	private JPopupMenu popUp1, popUp2;
	private JMenuItem insertBeforeLine, insertAfterLine, insertToEnd, 
		deleteLine, deleteAll, insertLine; 
	private ChangeEvent changeEvent;
	private JScrollPane scroll;
	private int selectedLine;
	private static final String NEW_LINE = NoteTableModel.FIELD_SEP + "0"
		+ NoteTableModel.FIELD_SEP + "0";
	
	// CONSTRUCTEURS
	public NoteSheet() {
		super();
		createView();
		placeComponents();
		createController();
	}
	
	// REQUETES
	public NoteTableModel getModel() {
		return tableModel;
	}
	
	public double getMean() {
		return tableModel.getMean();
	}
	
	public double getPoints() {
		return tableModel.getPoints();
	}
	
	// COMMANDES
	public void loadFile(File f) throws IOException {
		tableModel.loadFile(f);
		fireStateChanged();
	}
	
	public void saveFile(File f) throws IOException {
		tableModel.saveFile(f);
	}
	
	public void addChangeListener(ChangeListener cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		listenerList.add(ChangeListener.class, cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		if (cl == null) {
			throw new IllegalArgumentException();
		}
		listenerList.remove(ChangeListener.class, cl);
	}
	
	// OUTILS
	private void createView() {
		tableModel = new DefaultNoteTableModel();
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroll = new JScrollPane(table);
		popUp1 = new JPopupMenu();
		popUp2 = new JPopupMenu();
		
		insertLine = new JMenuItem("Insérer une nouvelle ligne.");
		insertBeforeLine = new JMenuItem("Insérer avant cette ligne");
		insertAfterLine = new JMenuItem("Insérer après cette ligne");
		insertToEnd = new JMenuItem("Insérer à la fin");
		deleteLine = new JMenuItem("Supprimer cette ligne");
		deleteAll = new JMenuItem("Supprimer toutes les lignes");
	}
	
	private void placeComponents() {
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);	
		placePopUpMenu();
	}
	
	private void createController() {	
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(final TableModelEvent e) {
				if (table.getRowCount() == 0) {
					scroll.setComponentPopupMenu(popUp2);
				} else {
					scroll.setComponentPopupMenu(null);
				}
				if (e.getType() == TableModelEvent.INSERT) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							table.scrollRectToVisible(table.getCellRect(
									e.getLastRow(), 0, true));
						}
					});				
				}
				table.scrollRectToVisible(table.getBounds());
				if (e.getColumn() != ColumnFeatures.SUBJECT.ordinal() 
						|| e.getType() != TableModelEvent.UPDATE) {
					fireStateChanged();
				}
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedLine = table.rowAtPoint(e.getPoint());
				if (SwingUtilities.isRightMouseButton(e)) {
					popUp1.setLocation(e.getPoint());	
					popUp1.setVisible(true);
				} 
			}
		});
		
		insertLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(NEW_LINE); 
			}
		});
		
		insertBeforeLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.insertRow(NEW_LINE, selectedLine);
			}
		});
		
		insertAfterLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.insertRow(NEW_LINE, selectedLine + 1);
			}
		});
		
		insertToEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(NEW_LINE);
			}
		});
		
		deleteLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.removeRow(selectedLine);
			}
		});
		
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.clearRows();
			}
		});
	}
	
	private void placePopUpMenu() {
		popUp1.add(insertBeforeLine);
		popUp1.add(insertAfterLine);
		popUp1.add(insertToEnd);
		popUp1.addSeparator();
		popUp1.add(deleteLine);
		popUp1.add(deleteAll);		

		table.setComponentPopupMenu(popUp1);
		scroll.setComponentPopupMenu(popUp2);
		
		popUp2.add(insertLine);
	}
	
	private void fireStateChanged() {
		for (ChangeListener cl 
				: listenerList.getListeners(ChangeListener.class)) {
			if (changeEvent == null) {
				changeEvent = new ChangeEvent(this);
			}
			cl.stateChanged(changeEvent);
		}
	}
}
