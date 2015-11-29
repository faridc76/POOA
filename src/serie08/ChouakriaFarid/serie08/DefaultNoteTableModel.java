package serie08;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DefaultNoteTableModel extends AbstractTableModel implements
		NoteTableModel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6657280536827984082L;
	// ATTRIBUTS
	// Une ligne contient : un String, Deux Double (List<Object>) et un Double.
	private List<List<Object>> listofList;
		
	// CONSTRUCTEURS
	public DefaultNoteTableModel() {
		super();
		listofList = new LinkedList<List<Object>>();
		fireTableDataChanged();
	}
		
	// REQUETES
	public Class<?> getColumnClass(int index) {
		return ColumnFeatures.values()[index].type();
	}
	
	public String getColumnName(int index) {
		return ColumnFeatures.values()[index].name();
	}
	
	public int getRowCount() {
		return listofList.size();
	}

	public int getColumnCount() {
		return ColumnFeatures.values().length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= getRowCount() 
				|| columnIndex < 0 || columnIndex >= getColumnCount()) {
			throw new IllegalArgumentException();
		}
		return listofList.get(rowIndex).get(columnIndex);
	}
	
	public boolean isCellEditable(int row, int column) {
		if (row < 0 || row >= getRowCount()
				|| column < 0 || column >= getColumnCount()) {
			throw new IllegalArgumentException();
		}
		return column != ColumnFeatures.POINTS.ordinal();
	}

	public double getMean() {
		if (getRowCount() == 0) {
			return Double.NaN;
		}
		double sum = 0;
		for (List<Object> o : listofList) {
			sum += (Double) o.get(ColumnFeatures.COEF.ordinal());
		}
		return getPoints() / sum;
	}

	public double getPoints() {
		double sum = 0;
		for (List<Object> o : listofList) {
			sum += (Double) o.get(ColumnFeatures.POINTS.ordinal());
		}
		return sum;
	}

	// COMMANDES	
	public void addRow(String line) {
		insertRow(line, getRowCount());
	}

	public void clearRows() {
		listofList.clear();
		fireTableRowsDeleted(0, getRowCount());
	}

	public void insertRow(String line, int index) {
		if (line == null || !LINE_PAT.matcher(line).matches()
				|| index < 0 || index > getRowCount()) {
			System.out.println(index);
			throw new IllegalArgumentException();
		}
		List<Object> list = new LinkedList<Object>();
		String[] tab = line.split(FIELD_SEP);
		list.add(tab[0]);
		list.add(Double.parseDouble(tab[1]));
		list.add(Double.parseDouble(tab[2]));
		list.add(Double.parseDouble(tab[1]) * Double.parseDouble(tab[2]));
		listofList.add(index, list);
		fireTableRowsInserted(index, index);
	}

	public void removeRow(int index) {
		if (index < 0 || index > getRowCount()) {
			throw new IllegalArgumentException();
		}
		listofList.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public void setValueAt(Object value, int row, int column) {
		if ((value != null && value.getClass() != getColumnClass(column)) 
				|| row < 0 || row >= getRowCount() 
				|| column < 0 || column >= getColumnCount() - 1) {
			throw new IllegalArgumentException();
		}
		List<Object> list = listofList.get(row);
		if (value == null) {
			if (column == ColumnFeatures.SUBJECT.ordinal()) {
				list.set(column, "");
			} else {
				list.set(column, new Double(0));
				list.set(ColumnFeatures.POINTS.ordinal(), new Double(0));
			}
		} else {
			list.set(column, value);
			if (column != ColumnFeatures.SUBJECT.ordinal()) {
				list.set(
					ColumnFeatures.POINTS.ordinal(), (Double) list.get(ColumnFeatures.COEF.ordinal())
					* (Double) list.get(ColumnFeatures.MARK.ordinal())
				);
			}
		}
		if (column == ColumnFeatures.SUBJECT.ordinal()) {
			fireTableCellUpdated(row, column);
		} else {
			fireTableRowsUpdated(row, row);
		}
	}
	
	public void loadFile(File f) throws IOException {
		if (f == null || !f.canRead()) {
			throw new IllegalArgumentException(
				"Fichier inexistant, ou impossible à lire"
			);
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String verif;
		String begin = COMMENT_CHAR + " " + getColumnName(ColumnFeatures.SUBJECT.ordinal()) + FIELD_SEP 
		+ getColumnName(ColumnFeatures.COEF.ordinal()) + FIELD_SEP + getColumnName(ColumnFeatures.MARK.ordinal());
		verif = br.readLine();
		if (verif.compareTo(begin) != 0) {
			throw new IllegalArgumentException("Début incorrect");
		}
		verif = br.readLine();
		if (verif.compareTo("") != 0) {
			throw new IllegalArgumentException(
				"Il manque un saut de ligne"
			);
		}
		clearRows();
		while ((verif = br.readLine()) != null) {
			if (!LINE_PAT.matcher(verif).matches()) {
				throw new IllegalArgumentException("Mauvaise syntaxe de ligne");
			}
			addRow(verif);
		}
		br.close();
	}

	public void saveFile(File f) throws IOException {
		if (f == null) {
			throw new IllegalArgumentException();
		}
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
		// Le début (en commentaires).
		bw.write(
			COMMENT_CHAR + " " + getColumnName(ColumnFeatures.SUBJECT.ordinal()) + FIELD_SEP 
			+ getColumnName(ColumnFeatures.COEF.ordinal()) + FIELD_SEP 
			+ getColumnName(ColumnFeatures.MARK.ordinal())
		);
		// Le saut de ligne
		bw.newLine();
		// Les lignes reconnaissables par LINE_PAT
		for (List<Object> o : listofList) {
			bw.newLine();
			// La matière (String ne contenant pas de séparateur)
			bw.write((String) o.get(ColumnFeatures.SUBJECT.ordinal()));
			// Un séparateur
			bw.write(FIELD_SEP);
			// Le coefficient (double convertit en String)
			bw.write(String.valueOf((Double) o.get(ColumnFeatures.COEF.ordinal())));
			// Un autre séparateur
			bw.write(FIELD_SEP);
			// La note (double convertit en String)
			bw.write(String.valueOf((Double) o.get(ColumnFeatures.MARK.ordinal())));
		}
		bw.close();
	}
}
