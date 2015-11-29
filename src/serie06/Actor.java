package serie06;

import serie06.sentence.SentenceListener;

/**
 * Un acteur est un objet manipulant une boite.
 * Il peut faire au plus <code>getMaxIterNb()</code> fois quelquechose sur
 *  cette boîte, après il s'arrête.
 * On démarre l'acteur avec <code>start()</code>, on peut stopper l'acteur
 *  avant qu'il n'ait terminé sa tâche avec <code>stop()</code>.
 * Un acteur est un objet qui « tourne » tout seul, c'est-à-dire animé par
 *  un thread interne, inaccessible de l'extérieur, seulement pilotable par
 *  utilisation des méthodes de la classe de l'acteur.
 * Un acteur parle à l'aide d'événements de type SentenceEvent.
 * @inv
 *     getMaxIterNb() > 0
 *     getBox() != null
 */
public interface Actor {
    // REQUETES
    /**
     * La boite associée à cet acteur.
     */
    Box getBox();
    /**
     * Le nombre maximal de fois que l'acteur peut faire quelque chose avant
     *  de s'arrêter.
     */
    int getMaxIterNb();
    /**
     * La liste des SentenceListeners enregistrés auprès de cet acteur.
     */
    SentenceListener[] getSentenceListeners();
    /**
     * Indique si l'acteur est en train de travailler, c'est-à-dire si le thread
     *  qui l'anime a été démarré et n'a pas encore terminé son exécution.
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
     * Démarrer un acteur, c'est-à-dire lui donner un nouveau thread
     *  et lancer son exécution.
     * Tout au long de la vie de l'acteur, des phrases vont être émises ; elles
     *  décrivent ce que fait l'acteur.
     * @pre
     *     !isActive()
     * @post
     *     l'action est démarrée
     */
    void start();
    /**
     * Arrêter l'acteur, son thread va donc mourir.
     * Force le thread courant à attendre la mort de l'acteur
     *  avant de continuer.
     * @pre
     *     isActive()
     * @post
     *     l'action est arrêtée
     */
    void stop();
}
