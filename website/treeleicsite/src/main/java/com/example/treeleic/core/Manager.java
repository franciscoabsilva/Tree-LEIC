package core; // O package que adicionámos

import java.io.*;

public class Manager {

    private String _filename = "../arvoreLEIC.dat"; // MUDAR AQUI A LOCALIZACAO DO ARVORELEIC.DAT, neste momento está na pasta website
    private ArvoreLEIC _arvore = new ArvoreLEIC();

    public void load() {
        try {
            FileInputStream fis = new FileInputStream(_filename);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // Criamos um ObjectInputStream personalizado que "traduz" os nomes antigos
            ObjectInputStream ois = new ObjectInputStream(bis) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    
                    // Se o ficheiro disser "Pessoa", nós dizemos "core.Pessoa"
                    if (name.equals("Pessoa")) {
                        return core.Pessoa.class;
                    }
                    // Se o ficheiro disser "ArvoreLEIC", nós dizemos "core.ArvoreLEIC"
                    if (name.equals("ArvoreLEIC")) {
                        return core.ArvoreLEIC.class;
                    }
                    
                    return super.resolveClass(desc);
                }
            };

            _arvore = (ArvoreLEIC) ois.readObject();
            ois.close();
            System.out.println("SUCESSO: Ficheiro carregado com " + _arvore.getPessoas().size() + " pessoas.");

        } catch (FileNotFoundException e) {
            System.out.println("Aviso: Ficheiro não encontrado, a iniciar árvore vazia.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRO ao carregar: " + e.getMessage());
            e.printStackTrace(); // Isto vai ajudar a ver se há outro erro
        }
    }

    public void save() {
        // O método save mantém-se igual, mas agora vai gravar com o formato novo (core.*)
        String tempFilename = _filename + ".tmp";
        String backupFilename = "backups/" + _filename.replace(".dat", System.currentTimeMillis() + ".dat");
        
        try {
            new File("backups").mkdirs(); // Garante que a pasta existe
            
            File originalFile = new File(_filename);
            File backupFile = new File(backupFilename);

            if (originalFile.exists() && originalFile.renameTo(backupFile)) {
                System.out.println("Backup saved as " + backupFilename);
            }
    
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFilename)));
            oos.writeObject(_arvore);
            oos.close();
            
            File tempFile = new File(tempFilename);
            if (tempFile.renameTo(originalFile)) {
                System.out.println("File saved successfully.");
            } else {
                System.out.println("Failed to rename temp file.");
            }
        } catch (IOException e) {
            System.out.println("Error during save: " + e.getMessage());
        }
    }

    public ArvoreLEIC getArvore() {
        return _arvore;
    }
}