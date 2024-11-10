
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Manager {

    /** The hotel manager. */
    private String _filename = "arvoreLEIC.dat";

    /** The current hotel. */
    private ArvoreLEIC _arvore = new ArvoreLEIC();

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(_filename)));
            _arvore = (ArvoreLEIC) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    public void save() {
        String tempFilename = _filename + ".tmp";
        String backupFilename = "backups/" + _filename.replace(".dat", System.currentTimeMillis() + ".dat");
        
        try {
            File originalFile = new File(_filename);
            File backupFile = new File(backupFilename);

            // Rename the original file to arvoreLEICold.dat for backup
            if (originalFile.renameTo(backupFile)) {
                System.out.println("Backup saved as " + backupFilename);
            } else {
                System.out.println("Failed to create backup file.");
                return;
            }
    
            // Step 2: Write data to a temporary file first
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFilename)));
            oos.writeObject(_arvore);
            oos.close();
            
            // Step 3: Rename the temporary file to the original filename
            File tempFile = new File(tempFilename);
            if (tempFile.renameTo(originalFile)) {
                System.out.println("File saved successfully.");
            } else {
                System.out.println("Failed to rename temp file to original file.");
            }
        } catch (IOException e) {
            System.out.println("Error during save: " + e.getMessage());
        }
    }
    

    public ArvoreLEIC getArvore() {
        return _arvore;
    }
}

