package serie07;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import serie07.filter.Filter;
import serie07.filter.Filtrable;
import serie07.filter.text.Factor;
import serie07.filter.text.Suffix;
import serie07.filter.text.PatternMatching;
import serie07.filter.text.Prefix;

public final class FilteringLinesByTextTester {
	private static final InputStream INPUT =
		FilteringLinesByTextTester.class
			.getResourceAsStream("data/text/contenu.txt");
    private static final Map<String, Filter<Data, String>> FILTERS;
    static {
        FILTERS = new HashMap<String, Filter<Data, String>>();
        FILTERS.put("Préfixe", new Prefix<Data>());
        FILTERS.put("Facteur", new Factor<Data>());
        FILTERS.put("Suffixe", new Suffix<Data>());
        FILTERS.put("ExpRat", new PatternMatching<Data>());
    }
    
    private JFrame frame;
    private FiltrablePane<Data, String> filtrablePane;
    

    class Data implements Filtrable<String> {
        private String data;
        Data(String s) { 
        	data = s; 
        }
        public String filtrableValue() { 
        	return data; 
        }
        public String toString() { 
        	return data; 
        }
    }
    
    // CONSTRUCTEURS
    public FilteringLinesByTextTester() {
        createView();
        placeComponents();
        createController();
    }
    
    // COMMANDES
    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    // OUTILS
    private void createView() {
    	frame = new JFrame("Filtrable List");
        filtrablePane = new FiltrablePane<Data, String>(
    		new ValueTranslator<String>() {
		        public String translateText(String text) {
		            return text;
		        }
		        public String translateValue(String value) {
		            return value;
		        }
    		}
        );
        filtrablePane.setFilterMap(FILTERS);
        try {
			populateList();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    private void placeComponents() {
        frame.add(filtrablePane, BorderLayout.CENTER);
    }
    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void populateList() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(INPUT));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.equals("")) {
                filtrablePane.addElement(new Data(line));
            }
        }
        if (br != null) {
        	br.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    new FilteringLinesByTextTester().display();
                }
            }
        );
    }
}
	