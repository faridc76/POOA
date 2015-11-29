package serie08;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.table.TableModel;

/**
 * Le type des mod�les de donn�es pour les JTables de l'application.
 * @inv
 *     getColumnCount() == ColumnFeatures.values().length
 *     forall f:ColumnFeatures :
 *         getColumnName(f.ordinal()).equals(f.header())
 *         getColumnClass(f.ordinal()).equals(f.type())
 *     getRowCount() >= 0
 *     forall i:[0..getRowCount()[, j:[0..getColumnCount()[ :
 *         isCellEditable(i, j) == (j != getColumnCount() - 1)
 *         getValueAt(i, j) != null
 *     forall i:[0..getRowCount()[ :
 *         getValueAt(i, ColumnFeatures.POINTS.ordinal()).equals(
 *             (Double) getValueAt(i, ColumnFeatures.COEF.ordinal())
 *             * (Double) getValueAt(i, ColumnFeatures.MARK.ordinal())
 *         )
 */
public interface NoteTableModel extends TableModel {
    char COMMENT_CHAR = '#';
    String FIELD_SEP = "\t";
    Double ZERO = new Double(0);
    Pattern LINE_PAT = Pattern.compile(
            "^[^" + FIELD_SEP + "]*(" + FIELD_SEP + "\\d+\\.?\\d*){2}("
            + FIELD_SEP + ".*)?$"
    );
    
    // REQUETES
    /**
     * La classe des �l�ments situ�s dans les cellules de la colonne index.
     * @pre
     *     0 <= index < getColumnCount()
     */
    Class<?> getColumnClass(int index);
    /**
     * Le nombre de colonnes.
     */
    int getColumnCount();
    /**
     * L'ent�te de la colonne index.
     * @pre
     *     0 <= index < getColumnCount()
     */
    String getColumnName(int index);
    /**
     * La moyenne des notes du mod�le, tenant compte des coefficients.
     * @post
     *     getRowCount() > 0
     *         ==> result ==
     *                 getPoints()
     *                 / sum(i:[0..getRowCount()[,
     *                     getValueAt(i, ColumnFeatures.COEF.ordinal()))
     *     getRowCount() == 0
     *         ==> result == Double.NaN
     */
    double getMean();
    /**
     * Le nombre de points correspondants aux notes stock�es dans le mod�le,
     *  calcul� comme la somme des (coef * note).
     * @post
     *     result == sum(i:[0..getRowCount()[,
     *         getValueAt(i, ColumnFeatures.POINTS.ordinal()))
     */
    double getPoints();
    /**
     * Le nombre de lignes.
     */
    int getRowCount();
    /**
     * La valeur de la cellule en (row, column).
     * @pre
     *     0 <= row < getRowCount()
     *     0 <= column < getColumnCount()
     */
    Object getValueAt(int row, int column);
    /**
     * Indique si cette cellule est �ditable.
     * @pre
     *     0 <= row < getRowCount()
     *     0 <= column < getColumnCount()
     */
    boolean isCellEditable(int row, int column);

    // COMMANDES
    /**
     * Ajoute une nouvelle ligne � la fin du mod�le.
     * @pre
     *     line != null
     *     line est reconnue par LINE_PAT
     * @post
     *     getRowCount() == old getRowCount() + 1
     *     les lignes du mod�le entre 0 et old getRowCount()-1 n'ont pas chang�
     *     line repr�sente la ligne du mod�le en getRowCount() - 1
     */
    void addRow(String line);
    /**
     * Supprime toutes les lignes dans le mod�le.
     * @post
     *     getRowCount() == 0
     */
    void clearRows();
    /**
     * Ins�re une nouvelle ligne dans le mod�le.
     * @pre
     *     line != null
     *     line est reconnue par LINE_PAT
     *     0 <= index && index <= getRowCount()
     * @post
     *     getRowCount() == old getRowCount() + 1
     *     les lignes du mod�le entre 0 et index - 1 n'ont pas chang�
     *     line repr�sente la ligne du mod�le en position index
     *     forall i:[index+1..getRowCount()[ :
     *         la ligne du mod�le en position i correspond � l'ancienne
     *          ligne du mod�le en position i - 1
     */
    void insertRow(String line, int index);
    /**
     * Retire la ligne en position index dans le mod�le.
     * @pre
     *     0 <= index && index < getRowCount()
     * @post
     *     getRowCount() == old getRowCount() - 1
     *     les lignes du mod�le entre 0 et index - 1 n'ont pas chang�
     *     forall i:[index..getRowCount()[ :
     *         la ligne du mod�le en position i correspond � l'ancienne
     *          ligne du mod�le en position i + 1
     */
    void removeRow(int index);
    /**
     * @pre
     *     value == null || value.getClass() == getColumnClass(column)
     *     0 <= row < getRowCount()
     *     0 <= column < getColumnCount() - 1
     * @post
     *     value == null
     *         ==> getValueAt(row, column).equals(
     *                 ColumnFeatures.values()[column].defaultValue())
     *     value != null
     *         ==> getValueAt(row, column).equals(value)
     */
    void setValueAt(Object value, int row, int column);
    /**
     * Charge un fichier de notes.
     * @pre
     *     f != null
     * @post
     *     le mod�le contient les donn�es (lignes de f reconnues par LINE_PAT)
     * @throws FileNotFoundException
     *     si f ne peut pas �tre ouvert en lecture
     * @throws IOException
     *     si les lignes ne sont ni des lignes vides ni des commentaires
     *     ni des lignes reconnues par LINE_PAT
     * @throws IOException
     *     si une erreur d'entr�e/sortie survient durant le traitement de f
     */
    void loadFile(File f) throws IOException;
    /**
     * Enregistre un fichier de notes.
     * @pre
     *     f != null
     * @post
     *     f est un fichier texte contenant toutes les donn�es du mod�le 
     *     f commence par :
     *         COMMENT_CHAR + " "
     *          + ColumnFeatures.SUBJECT.header() + FIELD_SEP
     *          + ColumnFeatures.COEF.header() + FIELD_SEP
     *          + ColumnFeatures.MARK.header()
     *     suivi d'une ligne vierge
     *     suivi de getRowCount() lignes reconnues par LINE_PAT
     * @throws FileNotFoundException
     *     si f ne peut pas �tre ouvert en �criture
     * @throws IOException
     *     si une erreur d'entr�e/sortie survient durant le traitement de f
     */
    void saveFile(File f) throws IOException;
}
