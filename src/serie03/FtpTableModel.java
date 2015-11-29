package serie03;

import javax.swing.table.AbstractTableModel;

import java.util.Date;
import java.io.File;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class FtpTableModel extends AbstractTableModel {
    
    public static final int ICON_HEIGHT = 16;
    public static final int ICON_WIDTH = 16;
    
    private static final Class<?>[] CLASSES = new Class<?>[] {
        ImageIcon.class, String.class, Date.class, Number.class
    };
    private static final String[] COL_NAMES = new String[] {
        "^", "Name", "Date", "Size"
    };
    private static final int ICON = 0;
    private static final int NAME = 1;
    private static final int DATE = 2;
    private static final int SIZE = 3;
    private static final ImageIcon FILE_ICON =
        createImageIcon("images/file.png");
    private static final ImageIcon DIRECTORY_ICON =
        createImageIcon("images/directory.png");


    private Object[][] data;
    
    public FtpTableModel(String rep) {
        createData(rep);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CLASSES[columnIndex];
    }
    public int getColumnCount() {
        return CLASSES.length;
    }
    @Override
    public String getColumnName(int i) {
        return COL_NAMES[i];
    }
    public int getRowCount() {
        return (data == null) ? 0 : data.length;
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FtpTableModel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(
                    Toolkit.getDefaultToolkit().getImage(imgURL)
                        .getScaledInstance(
                            ICON_WIDTH,
                            ICON_HEIGHT,
                            Image.SCALE_SMOOTH
                        )
            );
        } else {
            System.out.println("Ressource non trouvée : " + path);
            return null;
        }
    }
    private void createData(String rep) {
        File r = null;
        if (rep == null) {
            r = new File("");  // r = répertoire local
        } else {
            r = new File(rep); // r = répertoire ou fichier
        }
        File[] fileList = r.listFiles();
        // À ce stade :
        //  fileList est un tableau de fichiers/réps si r est un répertoire
        //  fileList vaut null si r est un fichier
        if (fileList == null) {
            fileList = new File[] {r};
        }
        // À ce stade, fileList est bien un tableau.
        // data contient les données à afficher dans la table
        data = new Object[fileList.length][COL_NAMES.length];
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                data[i][ICON] = DIRECTORY_ICON;
            } else if (fileList[i].isFile()) {
                data[i][ICON] = FILE_ICON;
            }
            data[i][NAME] = fileList[i].getName();
            data[i][DATE] = new Date(fileList[i].lastModified());
            data[i][SIZE] = new Long(fileList[i].length());
        }
    }
}
