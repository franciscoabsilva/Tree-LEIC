
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int _fenixId; // meter id do fenix
    private String _nome;
    private String _alcunha;
    private int _matriculas;
    private Map<Integer, Pessoa> _padrinhos = new TreeMap<>();
    private Map<Integer, Pessoa> _afilhados = new TreeMap<>();
    
    // Construtor da classe Pessoa
    public Pessoa(int fenixId, String nome, String alcunha, int matriculas) {
        _fenixId = fenixId;
        _nome = nome;
        _alcunha = alcunha;
        _matriculas = matriculas;
    }

    public int getFenixId() {
        return _fenixId;
    }

    public void setFenixId(int fenixId) {
        _fenixId = fenixId;
    }

    // Métodos Getters e Setters
    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public String getAlcunha() {
        return _alcunha;
    }

    public void setAlcunha(String alcunha) {
        _alcunha = alcunha;
    }

    public int getMatriculas() {
        return _matriculas;
    }

    public void setMatriculas(int matriculas) {
        _matriculas = matriculas;
    }

    public Map<Integer, Pessoa> getPadrinhos() {
        return _padrinhos;
    }

    public Map<Integer, Pessoa> getAfilhados() {
        return _afilhados;
    }

    // Método para adicionar um padrinho
    public void adicionarPadrinho(Pessoa padrinho) {
        if (!_padrinhos.containsKey(padrinho.getFenixId())) {
            _padrinhos.put(padrinho.getFenixId(), padrinho);
            padrinho.adicionarAfilhado(this);
        }
    }

    // Método para adicionar um afilhado
    public void adicionarAfilhado(Pessoa afilhado) {
        if (!_afilhados.containsKey(afilhado.getFenixId())) {
            _afilhados.put(afilhado.getFenixId(), afilhado);
            afilhado.adicionarPadrinho(this);
        }
    }

    public void removerPadrinho(int fenixId) {
        _padrinhos.remove(fenixId);
    }

    public void removerAfilhado(int fenixId) {
        _afilhados.remove(fenixId);
    }

    // Método para exibir informações da pessoa
    public void mostrarInformacoes() {
        System.out.println("Fenix ID: " + _fenixId);
        System.out.println("Nome: " + _nome);
        System.out.println("Alcunha: " + _alcunha);
        System.out.println("Matrícula: " + _matriculas);

        System.out.print("Padrinhos: ");
        if (_padrinhos.isEmpty()) {
            System.out.println("Nenhum");
        } else {
            for (Pessoa padrinho : _padrinhos.values()) {
                if (padrinho.getAlcunha() != "") {
                    System.out.print(padrinho.nomeComAlcunha() + ", ");
                } else {
                    System.out.print(padrinho.getNome() + ", ");
                }
            }
            System.out.println();
        }

        System.out.print("Afilhados: ");
        if (_afilhados.isEmpty()) {
            System.out.println("Nenhum");
        } else {
            for (Pessoa afilhado : _afilhados.values()) {
                if (afilhado.getAlcunha() != "") {
                    System.out.print(afilhado.nomeComAlcunha() + ", ");
                } else {
                    System.out.print(afilhado.getNome() + ", ");
                }
            }
            System.out.println();
        }
    }

    public String nomeComAlcunha() {
        if (_alcunha.isEmpty()) {
            return _nome;
        }
        // Dividir o nome em palavras usando o espaço como delimitador
        String[] partes = _nome.split(" ", 2);
        
        // Se o nome é composto de uma palavra, apenas adiciona a alcunha ao final
        if (partes.length == 1) {
            return partes[0] + " \"" + _alcunha + "\"";
        }
        
        // Insere a alcunha após a primeira palavra
        return partes[0] + " \"" + _alcunha + "\" " + partes[1];
    }

    public void printPessoaId() {
        if (_alcunha != "") {
            System.out.println(_fenixId + " " + nomeComAlcunha());
        } else {
            System.out.println(_fenixId + " " + _nome);
        }
    }

    public void avancarAno() {
        _matriculas++;
    }

    public void recuarAno() {
        _matriculas--;
    }


    public String getNomesPadrinhos() {
        StringBuilder nomes = new StringBuilder();
        for (Pessoa padrinho : getPadrinhos().values()) {
            nomes.append(padrinho.getNome()).append(", ");
        }
        if (nomes.length() > 0) nomes.setLength(nomes.length() - 2); // Remove a última vírgula
        return nomes.toString();
    }

    public String getNomesAfilhados() {
        StringBuilder nomes = new StringBuilder();
        for (Pessoa afilhado : getAfilhados().values()) {
            nomes.append(afilhado.getNome()).append(", ");
        }
        if (nomes.length() > 0) nomes.setLength(nomes.length() - 2); // Remove a última vírgula
        return nomes.toString();
    }
}
