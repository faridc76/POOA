package serie09;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class GenNode extends DefaultMutableTreeNode {	
	// ATTRIBUTS	
	private String name;
	private Gender gender;
	
	private static final char FIELD_SEP = ',';
	private static final char COMMENT = '#';
	private static final String PUSH = "+";
	private static final String POP = "-";
	private static final String INDENT_PAT = "[\\+]|[\\-]*";
	private static final Pattern LINE_PAT = Pattern.compile(
			"^\\s*(" + INDENT_PAT + ")\\s*" + FIELD_SEP
			+ "\\s*([^" + FIELD_SEP + "]+)\\s*" + FIELD_SEP
			+ "\\s*([01]).*$"
	);
	
	// CONSTRUCTEUR	
	public GenNode(Gender g, String n) {
		if (g == null || n == null) {
			throw new IllegalArgumentException();
		}
		gender = g;
		name = n;
	}

	public GenNode(Gender g) {
		this(g, g.toString());
	}
	
	// REQUETES	
	public Gender getGender() {
		return gender;
	}

	public String toString() {
		return name;
	}
	
	// COMMANDES
	public void setGender(Gender g) {
		if (g == null) {
			throw new IllegalArgumentException();
		}
		gender = g;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public void save(BufferedWriter b) throws IOException {
		save(this, b);
	}

	private void save(GenNode n, BufferedWriter b) throws IOException {
		write(n, b);
		for (int i = 0; i < n.getChildCount(); i++) {
			save((GenNode) n.getChildAt(i), b);
		}
	}
	
	public static GenNode createFrom(BufferedReader r) throws IOException {
		GenNode root = null, node = null, parent = null;
		String s = r.readLine();
		String[] table;
		Matcher m = LINE_PAT.matcher("");
		while (s != null) {
			m.reset(s);
			if (!m.matches()) {
				throw new IOException("Syntaxe incorrecte");
			}
			if (s.charAt(0) != COMMENT) {
				if (root != null) {
					if (s.charAt(0) == PUSH.charAt(0)) {
						parent = node;
					} else {
						if (s.charAt(0) == POP.charAt(0)) {
							int nb = 0, x;
							for (int i = 0; i < s.length(); i++) {
								if (s.charAt(i) != POP.charAt(0)) {
									x = nb;	break;
								}
								nb++;
							}
							x = nb;
							GenNode g = node;
							for (int i = 0; i <= x; i++) {
								g = (GenNode) g.getParent();
							}
							parent = g;
						}
					}
				}
				table = s.split(FIELD_SEP + "");
				node = new GenNode(
					Gender.values()[Integer.parseInt(table[2])], table[1]
				);
				if (root == null) {
					root = node;
				} else {
					parent.insert(node, parent.getChildCount());
				}
			}
			s = r.readLine();
		}
		return root;
	}

	private void write(GenNode n, BufferedWriter b) throws IOException {
		String line = FIELD_SEP + n.toString() + FIELD_SEP
			+ n.getGender().ordinal() + "\n";
		if (n.isRoot()) {
			b.write(line);
		} else {
			TreeNode p = n.getParent();
			if (p.getIndex(n) == 0) {
				b.write(PUSH + line);
			} else {
				for (int i = 1; i <= nodesNbs((GenNode) 
						p.getChildAt(p.getIndex(n) - 1)); i++) {
					b.write(POP);
				}
				b.write(line);
			}
		}
	}

	// OUTILS	
	private int nodesNbs(GenNode n) {
		if (n.isLeaf()) {
			return 0;
		} else {
			return nodesNbs((GenNode) n.getChildAt(n.getChildCount() - 1)) + 1;
		}
	}
}
