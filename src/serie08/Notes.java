package serie08;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Notes {
	// ATTRIBUTS
	private JFrame frame;
	private JButton load, save;
	private JLabel mean, pts;
	private NoteSheet note;
	
	// CONSTRUCTEUR
	public Notes() {
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
		frame = new JFrame("Gestionnaire de Notes");
		load = new JButton("Load");
		save = new JButton("Save as...");
		mean = new JLabel("Moyenne : NN");
		pts = new JLabel("Total des points : NN");
		note = new NoteSheet();
	}
	
	private void placeComponents() {
		JPanel p = new JPanel(); {
			p.add(load);
			p.add(save);
		}
		frame.add(p, BorderLayout.NORTH);
		frame.add(note, BorderLayout.CENTER);
		p = new JPanel(); {
			p.add(mean);
			p.add(pts);
		}
		frame.add(p, BorderLayout.SOUTH);
	}

	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		note.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mean.setText("Moyenne : " + note.getModel().getMean());
				pts.setText(
					"Total des points : " + note.getModel().getPoints()
				);
			}
		});
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(
						System.getProperty("user.home"));
				int res = fc.showOpenDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					try {
						note.getModel().loadFile(fc.getSelectedFile());
					} catch (IOException e1) {
						showMessagDialog();
					}
				}
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(
						System.getProperty("user.home"));
				int res = fc.showSaveDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					try {
						note.getModel().saveFile(fc.getSelectedFile());
					} catch (IOException e1) {
						showMessagDialog();
					}
				}
			}
		});
	}
	
	private void showMessagDialog() {
		JOptionPane.showMessageDialog(frame,
				"Le fichier est incorrect",
				"Erreur",
				JOptionPane.ERROR_MESSAGE);		
	}

	public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    new Notes().display();
                }
            }
        );
    }
}
