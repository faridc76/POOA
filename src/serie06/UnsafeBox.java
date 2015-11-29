package serie06;

/**
 * Implantation non « thread safe » des boîtes.
 */
public class UnsafeBox implements Box {
    // ATTRIBUTS
    private Integer value;
    private String desc;

    // CONSTRUCTEURS
    public UnsafeBox() {
        value = null;
        desc = "";
    }

    // REQUETES
    public int getValue() {
        return value;
    }
    public boolean isEmpty() {
        return value == null;
    }
    public String lastDescription() {
        return desc;
    }
    
    // COMMANDES
    public void clean() {
        desc = "box --> " + value;
        value = null;
    }
    public void fill(int v) {
        if (!isEmpty()) {
            throw new IllegalStateException();
        }
        value = v;
        desc = "box <-- " + v;
    }
}
