package serie06;

import serie06.sentence.SentenceListener;

/**
 * Un acteur est un objet manipulant une boite.
 * Il peut faire au plus <code>getMaxIterNb()</code> fois quelquechose sur
 *  cette bo�te, apr�s il s'arr�te.
 * On d�marre l'acteur avec <code>start()</code>, on peut stopper l'acteur
 *  avant qu'il n'ait termin� sa t�che avec <code>stop()</code>.
 * Un acteur est un objet qui ��tourne�� tout seul, c'est-�-dire anim� par
 *  un thread interne, inaccessible de l'ext�rieur, seulement pilotable par
 *  utilisation des m�thodes de la classe de l'acteur.
 * Un acteur parle � l'aide d'�v�nements de type SentenceEvent.
 * @inv
 *     getMaxIterNb() > 0
 *     getBox() != null
 */
public interface Actor {
    // REQUETES
    /**
     * La boite associ�e � cet acteur.
     */
    Box getBox();
    /**
     * Le nombre maximal de fois que l'acteur peut faire quelque chose avant
     *  de s'arr�ter.
     */
    int getMaxIterNb();
    /**
     * La liste des SentenceListeners enregistr�s aupr�s de cet acteur.
     */
    SentenceListener[] getSentenceListeners();
    /**
     * Indique si l'acteur est en train de travailler, c'est-�-dire si le thread
     *  qui l'anime a �t� d�marr� et n'a pas encore termin� son ex�cution.
     */
    boolean isActive();
    /**
     * Indique si l'acteur est en attente sur la boite au cours de son action.
     */
    boolean isWaitingOnBox();

    // COMMANDES
    /**
     * Enregistre un SentenceListener sur cet acteur.
     */
    void addSentenceListener(SentenceListener listener);
    /**
     * Retire un SentenceListener de cet acteur.
     */
    void removeSentenceListener(SentenceListener listener);
    /**
     * D�marrer un acteur, c'est-�-dire lui donner un nouveau thread
     *  et lancer son ex�cution.
     * Tout au long de la vie de l'acteur, des phrases vont �tre �mises ; elles
     *  d�crivent ce que fait l'acteur.
     * @pre
     *     !isActive()
     * @post
     *     l'action est d�marr�e
     */
    void start();
    /**
     * Arr�ter l'acteur, son thread va donc mourir.
     * Force le thread courant � attendre la mort de l'acteur
     *  avant de continuer.
     * @pre
     *     isActive()
     * @post
     *     l'action est arr�t�e
     */
    void stop();
}
