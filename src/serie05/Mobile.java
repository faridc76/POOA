package serie05;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.event.ChangeListener;

/**
 * Un mobile mod�lise un petit rectangle qui se d�place � l'int�rieur d'un
 *  grand rectangle.<br />
 * Le d�placement du rectangle mobile s'effectue exclusivement � l'int�rieur du
 *  rectangle statique : lorsque il touche le bord du rectangle statique, le
 *  rectangle mobile rebondit.
 * � chaque d�placement du rectangle mobile, tous les ChangeListeners
 *  enregistr�s aupr�s du mobile sont notifi�s.
 * Le rectangle mobile peut �tre immobilis�.
 * 
 * @inv
 *     getCenter() != null
 *     getMovingRect() != null
 *     getStaticRect() != null
 *     getChangeListeners() != null
 *     getCenter().x == getMovingRect().x + getMovingRect().width / 2
 *     getCenter().y == getMovingRect().y + getMovingRect().height / 2
 *     getMovingRect().height >= 0
 *     getMovingRect().width >= 0
 *     getStaticRect().height >= 0
 *     getStaticRect().width >= 0
 *     getStaticRect().contains(getMovingRect())
 *     forall i:[0..getChangeListeners().length[ :
 *         getChangeListeners()[i] != null
 *     isValidCenterPosition(c)
 *         == c != null
 *            getStaticRect().contains(
 *                c.x - getMovingRect().width/2,
 *                c.y - getMovingRect().height/2,
 *                getMovingRect().width,
 *                getMovingRect().height
 *            )
 * @cons
 * $DESC$
 *     Un mobile de rectangle statique <code>sr</code>, de rectangle
 *      mobile <code>mr</code>, de d�placement initial null (horizontal et
 *      vertical).
 * $ARGS$ Rectangle sr, Rectangle mr
 * $PRE$
 *     mr != null
 *     mr.height > 0
 *     mr.width > 0
 *     sr != null
 *     sr.contains(mr)
 * $POST$
 *     getStaticRect().equals(sr)
 *     getMovingRect().equals(mr)
 *     getHorizontalShift() == 0
 *     getVerticalShift() == 0
 *     isMovable()
 */
public interface Mobile {

    // REQUETES
    /**
     * Le centre du rectangle mobile.
     */
    Point getCenter();
    /**
     * Un tableau des ChangeListeners associ�s � ce mobile.
     */
    ChangeListener[] getChangeListeners();
    /**
     * La composante horizontale lors du d�placement du mobile.
     * C'est le nombre de pixels horizontaux dont le rectangle mobile se
     *  d�place au cours d'un appel � <code>move()</code>.
     */
    int getHorizontalShift();
    /**
     * Une copie du rectangle mobile.
     */
    Rectangle getMovingRect();
    /**
     * Une copie du rectangle statique dans lequel le rectangle mobile
     *  se meut.
     */
    Rectangle getStaticRect();
    /**
     * La composante verticale lors du d�placement du mobile.
     * C'est le nombre de pixels verticaux dont le rectangle mobile se
     *  d�place au cours d'un appel � <code>move()</code>.
     */
    int getVerticalShift();
    /**
     * Indique si le rectangle mobile peut �tre d�plac� avec move().
     */
    boolean isMovable();
    /**
     * Indique si le point <code>p</code> est une position admissible pour
     *  le centre du rectangle mobile.
     * C'est-�-dire si, en pla�ant le centre du rectangle mobile en p,
     *  celui-ci est toujours enti�rement contenu dans le rectangle statique.
     */
    boolean isValidCenterPosition(Point p);
    
    // COMMANDES
    /**
     * Associe un ChangeListener � ce mobile.
     * @post
     *     listener != null
     *         ==> exists i:[0..getChangeListeners().length[ :
     *             listener == getChangeListeners()[i]
     */
    void addChangeListener(ChangeListener listener);
    /**
     * D�place le rectangle mobile � l'int�rieur du rectangle statique.
     * Le rectangle mobile tente de se d�placer en accord avec sa quantit�
     *  de d�placement (un couple d'entiers d�fini par
     *  <code>setHorizontalShift()</code> et <code>setVerticalShift()</code>).
     * Si, ce faisant, il rencontre le bord du rectangle statique,
     *  il se colle � celui-ci, pr�t � rebondir au prochain d�placement.
     * @pre
     *     isMovable()
     * @post
     *     Le rectangle mobile s'est d�plac� dans le rectangle statique
     *     Si le rectangle mobile n'a pas touch� le bord il s'est d�plac�
     *      de (getHorizontalShift(), getVerticalShift())
     *     Sinon il se colle au rectangle statique et change de direction pour
     *      simuler le rebond
     *     Notife tous ses ChangeListeners
     */
    void move();
    /**
     * Retire le ChangeListener sp�cifi� de ce mobile.
     * @post
     *     forall i:[0..getChangeListeners().length[ :
     *         listener != getChangeListeners()[i]
     */
    void removeChangeListener(ChangeListener listener);
    /**
     * D�finit la position du centre du rectangle mobile.
     * @pre
     *     isValidCenterPosition(c)
     * @post
     *     getMovingRect().width == old getMovingRect().width
     *     getMovingRect().height == old getMovingRect().height
     *     getMovingRect().getLocation().equals(
     *         new Point(
     *             c.x - getMovingRect().width/2,
     *             c.y - getMovingRect().height/2
     *         )
     *     )
     *     Notife tous ses ChangeListeners
     */
    void setCenter(Point c);
    /**
     * D�finit la composante horizontale (en pixels) pour le d�placement
     *  du mobile.
     * @post
     *     getHorizontalShift() == hs
     */
    void setHorizontalShift(int hs);
    /**
     * Fixe la possibilit� de d�placement par move().
     * @post
     *     isMovable() == movable
     */
    void setMovable(boolean movable);
    /**
     * D�finit la composante verticale (en pixel) pour le d�placement
     *  du mobile.
     * @post
     *     getVerticalShift() == vs
     */
    void setVerticalShift(int vs);
}
