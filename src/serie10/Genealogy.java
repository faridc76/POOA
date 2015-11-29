package serie10;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class Genealogy {
	// ATTRIBUTS	
	private static final Dimension FRAME_DIM = new Dimension(400, 600);
	private JFrame frame;
	private GenTree tree;
	private JButton load, save;
	
	// CONSTRUCTEUR	
	public Genealogy() {
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
	
	private void createView() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(FRAME_DIM));
		load = new JButton("Load...");
		save = new JButton("Save as...");
		tree = new GenTree();
	}
	
	private void placeComponents() {
		JPanel p = new JPanel(); {
			p.add(load);
			p.add(save);
		}
		frame.add(p, BorderLayout.NORTH);
		frame.add(new JScrollPane(tree), BorderLayout.CENTER);
	}
	
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int res = fc.showOpenDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists() && file.canRead()) {
						try {
							FileReader fr = new FileReader(file);
							BufferedReader buffer = new BufferedReader(fr);
							tree.setRoot(GenNode.createFrom(buffer));
						} catch (Exception exp) {
							showMessageDialog();
						}
					} else {
						showMessageDialog();
					}
				}
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int res = fc.showSaveDialog(frame);
				if (res == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e1) {
							showMessageDialog();
						}
					}
					if (file.canWrite()) {
						try {
							FileWriter fw = new FileWriter(file);
							BufferedWriter buffer = new BufferedWriter(fw);
							((GenNode) tree.getModel().getRoot()).save(buffer);
							buffer.close();
						} catch (IOException e1) {
							showMessageDialog();
						}
					} else {
						showMessageDialog();
					}
				}
			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath tp = tree.getSelectionPath();
				if (tp != null) {	
					Object[] nodes = tp.getPath();
					String title = ((GenNode) nodes[nodes.length - 1]).toString();
					for (int i = nodes.length - 2; i >= 0; i--) {
						title += ", " + ((GenNode) nodes[i + 1]).getGender().getLabel()
							+ ((GenNode) nodes[i]).toString();
					}
					frame.setTitle(title);
				}
			}			
		});
	}
	
	// OUTILS	
	private void showMessageDialog() {
		JOptionPane.showMessageDialog(frame, "Erreur !");
	}
	
	// POINT D'ENTREE
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Genealogy().display();
			}
		});
	}
}
