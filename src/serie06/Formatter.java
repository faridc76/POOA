package serie06;

public class Formatter {
    // ATTRIBUTS
    // num�ro d'instance des formateurs, permettant de calculer une indentation
    // unique pour chaque formateur
    private static int indentFactor = 0;
    // initialement -1, m�morise l'instant initial d'une ex�cution
    private static volatile long startTime;
    // un formateur poss�de un bloc d'indentation �quivalent �
    // indentFactor * INDENT
    private static final String INDENT = "               |"; // 15 espaces
    // ratio pour convertir les nanosecondes en millisecondes
    private static final int NANO_TO_MILLI = 1000000;

    // indentation de ce formateur
    private final String indent;
    // nom de l'acteur associ� � ce formateur
    private final String name;
    
    // CONSTRUCTEURS
    public Formatter(String n) {
        name = n;
        StringBuffer idt = new StringBuffer();
        for (int i = 0; i < indentFactor; i++) {
            idt.append(INDENT);
        }
        indent = idt.toString();
        indentFactor += 1;
    }

    // REQUETES
    public String format(String m) {
        return prefix() + m + "\n";
    }
    private String prefix() {
        return getTime() + " ms  " + indent + name + " : ";
    }
    
    // METHODES STATIQUES
    public static synchronized void resetTime() {
        startTime = System.nanoTime();
    }
    private static synchronized int getTime() {
        return (int) ((System.nanoTime() - startTime) / NANO_TO_MILLI);
    }
}
