package serie07;

/**
 * Mod�lise les traducteurs de valeurs en texte.
 * FiltrablePane utilise un JTextField dans lequel l'utilisateur peut taper du
 *  texte repr�sentant une valeur de type V.
 * FiltrablePane utilise un ValueTranslator pour passer du texte de son
 *  JTextField � la valeur qui lui correspond.
 * Par exemple, pour une Date, le texte pourra �tre compos� sous la forme j/m/a
 *  et un traducteur cr�era la Date correspondante.
 */
public interface ValueTranslator<V> {
    /**
     * Donne la valeur d�crite par l'argument text.
     */
    V translateText(String text);
    /**
     * Donne la repr�sentation textuelle de value.
     */
    String translateValue(V value);
}
