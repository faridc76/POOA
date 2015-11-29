package serie07;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import serie07.filter.Filter;
import serie07.filter.Filtrable;
import serie07.filter.text.Factor;
import serie07.filter.text.PatternMatching;
import serie07.filter.text.Prefix;
import serie07.filter.text.Suffix;

public final class FilteringImagesByNameTester {
    private static final File INPUT = new File(
            FilteringImagesByNameTester.class.getResource("data/image")
                .getPath());
    private static final List<Filter<Data, String>> FILTERS;
    static {
        FILTERS = new ArrayList<Filter<Data, String>>();
        FILTERS.add(new Prefix<Data>());
        FILTERS.add(new Factor<Data>());
        FILTERS.add(new Suffix<Data>());
        FILTERS.add(new PatternMatching<Data>());
    }
    
    private JFrame frame;
    private FiltrablePane<Data, String> filtrablePane;
    
    public FilteringImagesByNameTester() {
        createView();
        placeComponents();
        createController();
    }
    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        populateList();
    }
    private void createView() {
        frame = new JFrame("Filtrable List");
        ValueTranslator<String> t = new ValueTranslator<String>() {
            public String translateText(String text) {
                return text;
            }
            public String translateValue(String value) {
                return value;
            }
        };
        filtrablePane = new FiltrablePane<Data, String>(t);
        filtrablePane.setFilters(FILTERS);
    }
    private void placeComponents() {
        frame.add(filtrablePane, BorderLayout.CENTER);
    }
    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * Cette méthode peut prendre beaucoup de temps, par conséquent elle est
     *  exécutée en tâche de fond.
     * Elle interagit avec EDT chaque fois qu'il faut publier un nouveau
     *  fichier.
     * Pour être certain qu'elle prendra du temps, on retarde un peu la
     *  boucle...
     */
    private void populateList() {
        final int delay = 100;
        SwingWorker<Void, Data> worker = new SwingWorker<Void, Data>() {
            @Override
            protected Void doInBackground() throws Exception {
                File[] files = INPUT.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".png");
                    }
                });
                if (files != null) {
                    for (File f : files) {
                        Thread.sleep(delay);
                        publish(new Data(f));
                    }
                }
                return null;
            }
            @Override
            public void process(List<Data> chunks) {
                for (Data d : chunks) {
                    filtrablePane.addElement(d);
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    new FilteringImagesByNameTester().display();
                }
            }
        );
    }

    private static final class Data extends ImageIcon
            implements Filtrable<String> {
        private final File image;
        Data(File i) {
            super(i.getAbsolutePath());
            image = i;
        }
        public String filtrableValue() {
            return image.getAbsolutePath();
        }
        @Override
        public String toString() {
            return image.getAbsolutePath();
        }
    }
}
