package serie10;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class GenTree extends JTree {
	// ATTRIBUTS
	private static final int AFTER = 0;
	private static final int BEFORE = 1;
	private DefaultTreeModel model;
	private Map<Menu, JMenuItem> map;
	enum Menu {
		ROOT("create root") {
			public void execute(GenTree tree) {
				tree.createRoot();
			}
		},
		BROTHER_BEF("create brother before") {
			public void execute(GenTree tree) {
				tree.createBrother(BEFORE);
			}
		},
		BROTHER_AFT("create brother after") {
			public void execute(GenTree tree) {
				tree.createBrother(AFTER);
			}
		},
		SON("create son") {
			public void execute(GenTree tree) {
				tree.createSon();
			}
		},
		DEL("delete selection") {
			public void execute(GenTree tree) {
				tree.delete();
			}
		};
		private String label;
		Menu(String l) {
			label = l;
		}
		public String getLabel() {
			return label;
		}
		public abstract void execute(GenTree gen);
	}

	// CONSTRUCTEUR	
	public GenTree() {
		super(new DefaultTreeModel(null));
		model = new DefaultTreeModel(null);
		setModel(model);
		setCellRenderer(new GenCellRenderer());
		DefaultTreeCellEditor editor = new DefaultTreeCellEditor(
				this, new GenCellRenderer(), 
				new GenCellEditor(this, new JTextField())) {
			public Component getTreeCellEditorComponent(JTree tree,	
					Object value, boolean sel, boolean expanded,
					boolean leaf, int row) {
				ImageIcon icon = ((GenNode) value).getGender().getIcon();
				renderer.setLeafIcon(icon);
				renderer.setOpenIcon(icon);
				renderer.setClosedIcon(icon);
				return super.getTreeCellEditorComponent(
					tree, value, sel, expanded, leaf, row
				);
			}
		};
		setCellEditor(editor);
		setEditable(true);
		
		map = new LinkedHashMap<Menu, JMenuItem>(); {
			for (Menu m : Menu.values()) {
				map.put(m, new JMenuItem(m.getLabel()));
			}
			map.get(Menu.BROTHER_AFT).setEnabled(false);
			map.get(Menu.BROTHER_BEF).setEnabled(false);
			map.get(Menu.SON).setEnabled(false);
			map.get(Menu.DEL).setEnabled(false);
		}
		
		JPopupMenu menu = new JPopupMenu();
		for (Menu m : map.keySet()) {
			menu.add(map.get(m));
		}
		setComponentPopupMenu(menu);
		
		addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if (getSelectionPath() == null) {
					map.get(Menu.BROTHER_AFT).setEnabled(false);
					map.get(Menu.BROTHER_BEF).setEnabled(false);
					map.get(Menu.SON).setEnabled(false);
					map.get(Menu.DEL).setEnabled(false);
					return;
				}
				if (getSelectionPath().getLastPathComponent()
						== model.getRoot()) {
					map.get(Menu.BROTHER_AFT).setEnabled(false);
					map.get(Menu.BROTHER_BEF).setEnabled(false);
				} else {
					map.get(Menu.BROTHER_AFT).setEnabled(true);
					map.get(Menu.BROTHER_BEF).setEnabled(true);
					map.get(Menu.DEL).setEnabled(true);	
				}
				GenNode g = (GenNode) getSelectionPath().getLastPathComponent();
				map.get(Menu.SON).setEnabled(g.isLeaf());
			}
		});
		createController();
	}
	
	// REQUETES
	public GenTree getComponent() {
		return this;
	}

	// COMMANDES
	public void createRoot() {
		model.setRoot(new GenNode(Gender.MALE, "Toto"));
	}

	public void createBrother(int i) {
		GenNode brother = (GenNode) getSelectionPath().getLastPathComponent();
		GenNode parent = (GenNode) brother.getParent();
		if (parent == null) {
			throw new IllegalStateException();
		}
		int index = parent.getIndex(brother);
		if (i == AFTER)	{
			index++;
		}
		GenNode newNode = new GenNode(Gender.MALE, "Toto");
		model.insertNodeInto(newNode, parent, index);
		setSelectionPath(
			getSelectionPath().getParentPath().pathByAddingChild(newNode)
		);
	}

	public void createSon() {
		GenNode newNode = new GenNode(Gender.MALE, "Toto");
		model.insertNodeInto(
			newNode, ((GenNode) getSelectionPath().getLastPathComponent()), 0
		);
		setSelectionPath(getSelectionPath().pathByAddingChild(newNode));
	}

	public void delete() {
		GenNode node = (GenNode) getSelectionPath().getLastPathComponent();
		if (node == model.getRoot()) {
			model.setRoot(null);
		} else {
			model.removeNodeFromParent(node);
		}
	}

	public void setRoot(GenNode root) {
		model.setRoot(root);
	}
	
	// OUTILS
	private void createController() {
		for (final Menu m : map.keySet()) {
			map.get(m).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m.execute(getComponent());
				}
			});
		}
	}
}
