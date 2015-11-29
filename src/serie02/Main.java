package serie02;

import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

public final class Main {
    
    private static int colorsNb = 3;

    private Main() {
        // rien
    }

    public static void main(final String[] args) {
        AnimalColor[] values = AnimalColor.values();
        if (args.length >= 1) {
            colorsNb = Integer.parseInt(args[0]);
        }
        if (colorsNb > values.length) {
            colorsNb = values.length;
        }
        final Set<AnimalColor> s = new HashSet<AnimalColor>();
        for (int i = 0; i < colorsNb; i++) {
            s.add(values[i]);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CrazyCircus().display();
            }
        });
    }
}
