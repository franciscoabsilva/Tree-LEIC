import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import exceptions.*;

public class Manager {

    /** The hotel manager. */
    private String _filename = "arvoreLEIC.dat";

    /** Indicates whether there were changes since last load/save. */
    private boolean _changed = false;

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

        if (!_changed) {
            return;
        }

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

     // --- MÉTODOS DE LEITURA (Não alteram _changed) ---

    public Pessoa getPessoa(int id) {
        return _arvore.getPessoa(id);
    }

    public Map<Integer, Pessoa> getPessoas() {
        return _arvore.getPessoas();
    }

    public int getAno() {
        return _arvore.getAno();
    }

    public void printPessoa(int id) throws UnknownPersonException {
        _arvore.printPessoa(id);
    }
    
    public void mostrarArvoreGenealogicaCima(int id) throws UnknownPersonException {
        _arvore.mostrarArvoreGenealogicaCima(id);
    }

    public void mostrarArvoreGenealogicaBaixo(int id) throws UnknownPersonException {
        _arvore.mostrarArvoreGenealogicaBaixo(id);
    }

    public String getPessoaInfo(int fenixId, int mode) throws UnknownPersonException {
        return _arvore.getPessoaInfo(fenixId, mode);
    }

    // Este era o erro da linha 427 e 641
    public Pessoa getPessoaExistente(int fenixId) throws UnknownPersonException {
        return _arvore.getPessoaExistente(fenixId);
    }

    // --- MÉTODOS DE ESCRITA (Alteram _changed = true) ---

    public void adicionarPessoa(Pessoa p) throws DuplicatePersonException {
        _arvore.adicionarPessoa(p);
        _changed = true;
    }

    public void removerPessoa(int id) throws UnknownPersonException {
        _arvore.removerPessoa(id);
        _changed = true;
    }

    public void adicionarPadrinho(int afilhado, int padrinho) throws UnknownPersonException {
        _arvore.adicionarPadrinho(afilhado, padrinho);
        _changed = true;
    }

    public void removerPadrinho(int afilhado, int padrinho) throws UnknownPersonException {
        _arvore.removerPadrinho(afilhado, padrinho);
        _changed = true;
    }

    public void atualizarPessoaId(int antigo, int novo) throws DuplicatePersonException {
        _arvore.atualizarPessoaId(antigo, novo);
        _changed = true;
    }

    public void avancarAno() {
        _arvore.avancarAno();
        _changed = true;
    }

    public void recuarAno() {
        _arvore.recuarAno();
        _changed = true;
    }

    // NOVOS MÉTODOS PARA O CASE 8 (Edição direta de atributos)
    // Isto evita que tenhas de fazer pessoa.setNome() na App e perderes o tracking
    public void alterarNome(int id, String novoNome) {
        Pessoa p = _arvore.getPessoa(id);
        if (p != null) {
            p.setNome(novoNome);
            _changed = true;
        }
    }

    public void alterarAlcunha(int id, String novaAlcunha) {
        Pessoa p = _arvore.getPessoa(id);
        if (p != null) {
            p.setAlcunha(novaAlcunha);
            _changed = true;
        }
    }

    public void alterarMatricula(int id, int novaMatricula) {
        Pessoa p = _arvore.getPessoa(id);
        if (p != null) {
            p.setMatriculas(novaMatricula);
            _changed = true;
        }
    }
    

    public ArvoreLEIC getArvore() {
        return _arvore;
    }
}

