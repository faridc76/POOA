package serie07;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import serie07.filter.Filter;
import serie07.filter.Filtrable;

public class FiltrablePane<E extends Filtrable<V>, V> extends JPanel {
	private static final long serialVersionUID = 5001674823299495803L;
	// ATTRIBUTS
	// Le Modèle.
	private MarkableFiltrableListModel<E, V> listModel;
	// La Vue.
	private JList filtrableList;
	private JComboBox filterTypes;
	private JTextField filterText;
	// Autres
	private Map<String, Filter<E, V>> filterMap;
	private ListDataListener listData;
	private ValueTranslator<V> valueTrans;
	
	// CONSTRUCTEUR
	public FiltrablePane(ValueTranslator<V> trans) {
		listData = new ListDataListener() {
			public void intervalRemoved(ListDataEvent e) {
				filtrableList.repaint();
			}
			
			public void intervalAdded(ListDataEvent e) {
				filtrableList.repaint();
			}
			
			public void contentsChanged(ListDataEvent e) {
				filtrableList.repaint();
			}
		};
		valueTrans = trans;
		createModel();
		createView();
		placeComponents();
		createController();
	}
	
	// COMMANDES
	public void setFilterMap(Map<String, Filter<E, V>> filters) {
		filterMap = filters;
		filterTypes.removeAllItems();
		for (String s : filterMap.keySet()) {
			filterTypes.addItem(s);
		}
	}

	public void addElement(E e) {
		listModel.addElement(e);
	}
	
	public void setModel(MarkableFiltrableListModel<E, V> lm) {
		if (listModel != null) {
			listModel.removeListDataListener(listData);
		}
		listModel = lm;
		listModel.addListDataListener(listData);
	}
	
	// OUTILS
	private void createModel() {
		setModel(new MarkableFiltrableListModel<E, V>());
		filtrableList = new JList(listModel);
		filtrableList.setCellRenderer(new ItalicRenderer());
		filterMap = new HashMap<String, Filter<E, V>>(); 
	}
	
	private void createView() {
		String[] tab = new String[filterMap.size()];
		int i = 0;
		for (String s : filterMap.keySet()) {
			tab[i] = s;
			i++;
		}
		filterTypes = new JComboBox(tab); 
		filterText = new JTextField();
	}
	
	private void placeComponents() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel(new BorderLayout()); {
			// La JComboBox
			p.add(filterTypes, BorderLayout.WEST);
			// Le JTextField
			p.add(filterText, BorderLayout.CENTER);
			p.setBorder(
				BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(), "Recherche"
				)
			);
		}
		add(p, BorderLayout.NORTH);
		JPanel q = new JPanel(new BorderLayout()); {
			// La JList
			JScrollPane scroll = new JScrollPane(filtrableList);
			q.add(scroll);
			q.setBorder(BorderFactory.createEtchedBorder());
		}
		add(q, BorderLayout.CENTER);
	}
	
	private void createController() {
		filterTypes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter<E, V> f = filterMap.get(filterTypes.getSelectedItem());
				listModel.setFilter(f);
				V v = valueTrans.translateText(filterText.getText());
				f.setValue(v);
			}
		});
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				documentUpdate();
			}

			public void removeUpdate(DocumentEvent e) {
				documentUpdate();
			}

			public void changedUpdate(DocumentEvent e) {
				documentUpdate();
			}
		});
		listModel.addListDataListener(listData);
		// Optionnelle.
		filtrableList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					listModel.setMarked(
						filtrableList.locationToIndex(e.getPoint())
					);
				}
			}
		});
	}
	
	private void documentUpdate() {
		Filter<E, V> f = listModel.getFilter();
		try {
			Document d = filterText.getDocument();
			String s = d.getText(0, d.getLength());
			V v = valueTrans.translateText(s);
			f.setValue(v);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
}
