package serie06;

/**
 * Une boite est une ressource partag�e pouvant contenir un entier.
 * Les producteurs mettent des entiers dans une boite, et les consommateurs
 *  les y enl�vent.
 * @inv
 *     isEmpty() <==> getValue() == null
 *     lastDescritption() != null
 * @cons
 *     $POST$ isEmpty() && lastDescription().equals("")
 */
public interface Box {
    // REQUETES
    /**
     * Le contenu de la boite.
     */
    int getValue();
    /**
     * La boite est-elle vide ?
     */
    boolean isEmpty();
    /**
     * Descritpion textuelle de la derni�re action effectu�e sur la boite.
     */
    String lastDescription();
    
    // COMMANDES
    /**
     * Vide la boite.
     * @post
     *     isEmpty()
     *     lastDescription().equals("box --> " + getValue())
     */
    void clean();
    /**
     * Remplit la boite avec l'entier v.
     * @pre
     *     isEmpty()
     * @post
     *     getValue() == v
     *     lastDescription().equals("box <-- " + v)
     */
    void fill(int v);
}
