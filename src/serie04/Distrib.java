package serie04;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

abstract class Distrib {
    protected enum LKey {
        BACK("Cet appareil rend la monnaie"),
        CREDIT("Vous disposez d'un crédit de 0 cents"),
        DRINK("Boisson :"),
        MONEY("Monnaie :");
        private String label;
        LKey(String lbl) { label = lbl; }
    }
    
    protected enum FKey { INS, DRINK, MONEY }

    public static final int DRINKS_NB = 3;
    protected enum BKey {
        ORANGE("Jus d'orange"),
        CHOCO("Chocolat"),
        COFFEE("Café"),
        INS("Insérer"),
        CANCEL("Annuler"),
        TAKE("Prenez votre boisson et/ou votre monnaie");
        private String label;
        BKey(String lbl) { label = lbl; }
    }
    protected enum PKey {
        ORANGE("110 cents"),
        CHOCO("45 cents"),
        COFFEE("30 cents"),
        INS("cents"),
        MONEY("cents");
        private String label;
        PKey(String lbl) { label = lbl; }
    }

    private static final int MIN_FIELD_SIZE = 10;

    private JFrame frame;
    private Map<LKey, JLabel> labels;
    private Map<BKey, JButton> buttons;
    private Map<FKey, JTextField> fields;
    private Map<PKey, JLabel> posts;
    
    public Distrib(String title) {
        createView(title);
        placeComponents();
        createController();
    }
    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    protected void createView(String title) {
        frame = new JFrame(title);

        labels = new EnumMap<LKey, JLabel>(LKey.class);
        for (LKey k : LKey.values()) {
            labels.put(k, new JLabel(k.label));
        }
        
        buttons = new EnumMap<BKey, JButton>(BKey.class);
        for (BKey k : BKey.values()) {
            buttons.put(k, new JButton(k.label));
        }
        
        fields = new EnumMap<FKey, JTextField>(FKey.class);
        for (FKey k : FKey.values()) {
            fields.put(k, new JTextField(MIN_FIELD_SIZE));
        }
        
        posts = new EnumMap<PKey, JLabel>(PKey.class);
        for (PKey k : PKey.values()) {
            posts.put(k, new JLabel(k.label));
        }
    }
    protected abstract void placeComponents();
    protected void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    protected JFrame getFrame() {
        return frame;
    }
    protected JLabel getLabel(LKey k) {
        return labels.get(k);
    }
    protected JButton getButton(BKey k) {
        return buttons.get(k);
    }
    protected JTextField getField(FKey k) {
        return fields.get(k);
    }
    protected JLabel getPost(PKey k) {
        return posts.get(k);
    }
}
