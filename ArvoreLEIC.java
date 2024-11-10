
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ArvoreLEIC implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Pessoa> _pessoas = new TreeMap<>();
    private int _ano = 2024;

    public void adicionarPessoa(Pessoa pessoa) {
        if (_pessoas.containsKey(pessoa.getFenixId())) {
            System.out.println("Já existe uma pessoa com este Fenix ID");
            return;
        }
        _pessoas.put(pessoa.getFenixId(), pessoa);
        System.out.println("Pessoa adicionada com sucesso!");
    }

    public void removerPessoa(int fenixId) {
        Pessoa pessoa = _pessoas.get(fenixId);
        Map<Integer, Pessoa> afilhados = pessoa.getAfilhados();
        Map<Integer, Pessoa> padrinhos = pessoa.getPadrinhos();
        for (Pessoa afilhado : afilhados.values()) {
            afilhado.removerPadrinho(fenixId);
        }
        for (Pessoa padrinho : padrinhos.values()) {
            padrinho.removerAfilhado(fenixId);
        }
        _pessoas.remove(fenixId);
        System.out.println("Pessoa removida com sucesso!");
    }

    public void adicionarPadrinho(int afilhadoId, int padrinhoId) {
        Pessoa afilhado = _pessoas.get(afilhadoId);
        Pessoa padrinho = _pessoas.get(padrinhoId);

        if (afilhado == null) {
            System.out.println("Pessoa " + afilhadoId + " não encontrado");
        }
        if (padrinho == null) {
            System.out.println("Pessoa " + padrinhoId + " não encontrado");
        }
        if (afilhado != null && padrinho != null) {
            afilhado.adicionarPadrinho(padrinho);
            padrinho.adicionarAfilhado(afilhado);
            System.out.println("Padrinho adicionado com sucesso! " + afilhado.getNome() + " é afilhado/a de " + padrinho.getNome());
        }
    }

    public void removerPadrinho(int afilhadoId, int padrinhoId) {
        Pessoa afilhado = _pessoas.get(afilhadoId);
        Pessoa padrinho = _pessoas.get(padrinhoId);

        if (afilhado == null) {
            System.out.println("Pessoa " + afilhadoId + " não encontrado");
        }
        if (padrinho == null) {
            System.out.println("Pessoa " + padrinhoId + " não encontrado");
        }
        if (afilhado != null && padrinho != null) {
            afilhado.removerPadrinho(padrinhoId);
            padrinho.removerAfilhado(afilhadoId);
            System.out.println("Padrinho removido com sucesso!");
        }
    }

    public Pessoa getPessoa(int fenixId) {
        return _pessoas.get(fenixId);
    }

    public void printPessoa(Pessoa pessoa) {
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada");
        } else {
            pessoa.mostrarInformacoes();
        }
    }

    public Map<Integer, Pessoa> getPessoas() {
        return _pessoas;
    }

    public void atualizarPessoaId(int fenixId, int novoFenixId) {

        if (_pessoas.containsKey(novoFenixId)) {
            System.out.println("Já existe uma pessoa com este Fenix ID");
            return;
        }

        Pessoa pessoa = _pessoas.get(fenixId);

        for (Pessoa afilhado : pessoa.getAfilhados().values()) {
            afilhado.removerPadrinho(fenixId);
            afilhado.adicionarPadrinho(pessoa);
        }
        for (Pessoa padrinho : pessoa.getPadrinhos().values()) {
                padrinho.removerAfilhado(fenixId);
                padrinho.adicionarAfilhado(pessoa);
        }

        _pessoas.remove(fenixId);
        pessoa.setFenixId(novoFenixId);
        _pessoas.put(novoFenixId, pessoa);
    }

    public void avancarAno() {
        for (Pessoa pessoa : _pessoas.values()) {
            pessoa.avancarAno();
        }
        _ano++;
    }

    public void recuarAno() {
        for (Pessoa pessoa : _pessoas.values()) {
            pessoa.recuarAno();
        }
        _ano--;
    }

    public int getAno() {
        return _ano;
    }
    

    public void mostrarArvoreGenealogicaCima(int fenixId) {
        Pessoa pessoa = _pessoas.get(fenixId);
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }
        if (pessoa.getAlcunha() != "") {
            System.out.println(pessoa.nomeComAlcunha().toUpperCase() + ":");
        } else {
            System.out.println(pessoa.getNome().toUpperCase() + ":");
        }
        Set<Integer> visitados = new HashSet<>();
        visitados.add(fenixId);  // Marca a pessoa inicial como visitada
        exibirPadrinhosRecursivo(pessoa, 1, visitados);
    }

    // Função recursiva para exibir padrinhos e padrinhos dos padrinhos, com indentação
    private void exibirPadrinhosRecursivo(Pessoa pessoa, int nivel, Set<Integer> visitados) {
        for (Pessoa padrinho : pessoa.getPadrinhos().values()) {
            // Exibe o nome do padrinho com o nível de indentação apropriado
            for (int i = 0; i < nivel; i++) System.out.print("-");
            if (padrinho.getAlcunha().equals("")) {
                System.out.print(padrinho.getNome());
            } else {
                System.out.print(padrinho.nomeComAlcunha());
            }
            // Se o padrinho já foi visitado, marca como "(acima)" e não continua a recursão
            if (visitados.contains(padrinho.getFenixId())) {
                System.out.println(" (acima)");
            } else {
                System.out.println();  // Nova linha após o nome do padrinho
                visitados.add(padrinho.getFenixId());  // Marca o padrinho como visitado
                exibirPadrinhosRecursivo(padrinho, nivel + 1, visitados);  // Recursão com aumento do nível
            }
        }
    }

    public void mostrarArvoreGenealogicaBaixo(int fenixId) {
        Pessoa pessoa = _pessoas.get(fenixId);
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }
        if (pessoa.getAlcunha() != "") {
            System.out.println(pessoa.nomeComAlcunha().toUpperCase() + ":");
        } else {
            System.out.println(pessoa.getNome().toUpperCase() + ":");
        }
        Set<Integer> visitados = new HashSet<>();
        visitados.add(fenixId);  // Marca a pessoa inicial como visitada
        exibirAfilhadosRecursivo(pessoa, 1, visitados);
    }

    // Função recursiva para exibir afilhados e afilhados dos afilhados, com indentação
    private void exibirAfilhadosRecursivo(Pessoa pessoa, int nivel, Set<Integer> visitados) {
        for (Pessoa afilhado : pessoa.getAfilhados().values()) {
            // Exibe o nome do afilhado com o nível de indentação apropriado
            for (int i = 0; i < nivel; i++) System.out.print("-");
            if (afilhado.getAlcunha().equals("")) {
                System.out.print(afilhado.getNome());
            } else {
                System.out.print(afilhado.nomeComAlcunha());
            }
            
            // Se o afilhado já foi visitado, marca como "(abaixo)" e não continua a recursão
            if (visitados.contains(afilhado.getFenixId())) {
                System.out.println(" (acima)");
            } else {
                System.out.println();  // Nova linha após o nome do afilhado
                visitados.add(afilhado.getFenixId());  // Marca o afilhado como visitado
                exibirAfilhadosRecursivo(afilhado, nivel + 1, visitados);  // Recursão com aumento do nível
            }
        }
    }
}