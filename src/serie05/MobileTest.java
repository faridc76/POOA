package serie05;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Classe de test du mobile.
 */
public class MobileTest {
    private enum BKey {
        ACTIVATE("Activate"),
        PAUSE("Pause"),
        FREEZE("Freeze"),
        RESUME("Resume"),
        TERMINATE("Terminate");
        private String label;
        BKey(String lbl) { label = lbl; }
    }
    private static final int
        WIDTH = 419,
        HEIGHT = 211,
        RAY = 10,
        DX = 5,
        DY = 5;

    private ThreadedMobileComponent mobile;
    private JFrame frame;
    private Map<BKey, JButton> buttons;

    /**
     * Constructeur.
     * @param title : titre de la frame.
     * @param width : largeur du composant graphique.
     * @param height : hauteur du composant graphique
     * @param ray : rayon de la balle.
     */
    public MobileTest(String title, int width, int height,
            int ray, int dx, int dy) {
        createView(title, dx, dy, width, height, ray);
        placeComponents();
        createController();
    }
    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void createView(String title, int dx, int dy,
            int width, int height, int ray) {
        frame = new JFrame(title);
        mobile = new ThreadedMobileComponent(width, height, ray);
        mobile.setDiscShift(dx, dy);
        buttons = new EnumMap<BKey, JButton>(BKey.class);
        for (BKey k : BKey.values()) {
            buttons.put(k, new JButton(k.label));
        }
        updateButtonsFromMobile();
    }
    private void placeComponents() {
        JPanel p = new JPanel(); {
            p.add(mobile);
        }
        frame.add(p, BorderLayout.CENTER);
        
        p = new JPanel(); {
            for (BKey k : BKey.values()) {
                p.add(buttons.get(k));
            }
        }
        frame.add(p, BorderLayout.NORTH);
    }
    private void createController() {
        // FENETRE
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // BOUTONS
        buttons.get(BKey.ACTIVATE).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mobile.start();
            }
        });
        buttons.get(BKey.PAUSE).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mobile.pause();
            }
        });
        buttons.get(BKey.FREEZE).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mobile.freeze();
            }
        });
        buttons.get(BKey.RESUME).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mobile.resume();
            }
        });
        buttons.get(BKey.TERMINATE).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mobile.stop();
            }
        });

        // MOBILE
        mobile.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateButtonsFromMobile();
            }
        });
    }
    
    private void updateButtonsFromMobile() {
        buttons.get(BKey.ACTIVATE).setEnabled(!mobile.hasStarted());
        buttons.get(BKey.PAUSE).setEnabled(mobile.isRunning());
        buttons.get(BKey.FREEZE).setEnabled(mobile.isRunning());
        buttons.get(BKey.RESUME).setEnabled(mobile.isFreezed());
        buttons.get(BKey.TERMINATE).setEnabled(mobile.isActive());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MobileTest(
                    "Essai sur les Threads", WIDTH, HEIGHT, RAY, DX, DY
                ).display();
            }
        });
    }
}
