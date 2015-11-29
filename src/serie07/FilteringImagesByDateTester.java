package serie07;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import serie07.filter.Filter;
import serie07.filter.Filtrable;
import serie07.filter.date.After;
import serie07.filter.date.Before;

public final class FilteringImagesByDateTester {
    private static final File INPUT = new File(
            FilteringImagesByDateTester.class.getResource("data/image")
                .getPath());
    private static final List<Filter<Data, Date>> FILTERS;
    static {
        FILTERS = new ArrayList<Filter<Data, Date>>();
        FILTERS.add(new After<Data>());
        FILTERS.add(new Before<Data>());
    }
    
    private JFrame frame;
    private FiltrablePane<Data, Date> filtrablePane;
    
    public FilteringImagesByDateTester() {
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
        filtrablePane = new FiltrablePane<Data, Date>(new Translator());
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
                    new FilteringImagesByDateTester().display();
                }
            }
        );
    }

    private static final class Data extends ImageIcon
            implements Filtrable<Date> {
        private final File image;
        Data(File i) {
            super(i.getAbsolutePath());
            image = i;
        }
        public Date filtrableValue() {
            return new Date(image.lastModified());
        }
        @Override
        public String toString() {
            return image.getAbsolutePath();
        }
    }
    private static final class Translator implements ValueTranslator<Date> {
        private static final int DEFAULT_YEAR = 1970;
        private static final int DEFAULT_MONTH = 0;
        private static final int DEFAULT_DAY = 1;
        private final Calendar calendar = Calendar.getInstance();
        public Date translateText(String text) {
            String[] parts = text.split("/");
            int year = DEFAULT_YEAR;
            int month = DEFAULT_MONTH;
            int day = DEFAULT_DAY;
            try {
                day = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]) - 1;
                year = Integer.parseInt(parts[2]);
            } catch (Exception ex) {
                year = DEFAULT_YEAR;
                month = DEFAULT_MONTH;
                day = DEFAULT_DAY;
            }
            calendar.set(year, month, day);
            return calendar.getTime();
        }
        public String translateValue(Date value) {
            calendar.setTime(value);
            return "" + calendar.get(Calendar.DAY_OF_MONTH)
                    + "/" + (calendar.get(Calendar.MONTH) + 1)
                    + "/" + calendar.get(Calendar.YEAR);
        }
    }
}
