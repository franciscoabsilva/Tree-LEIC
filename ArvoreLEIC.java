
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import exceptions.*;

public class ArvoreLEIC implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Pessoa> _pessoas = new TreeMap<>();
    private int _ano = 2024;

    public void adicionarPessoa(Pessoa pessoa) throws DuplicatePersonException {
        if (_pessoas.containsKey(pessoa.getFenixId())) {
            throw new DuplicatePersonException(pessoa.getFenixId());
        }
        _pessoas.put(pessoa.getFenixId(), pessoa);
    }

    public void removerPessoa(int fenixId) throws UnknownPersonException {
        Pessoa pessoa = _pessoas.get(fenixId);

        if (pessoa == null) {
            throw new UnknownPersonException(fenixId);
        }

        Map<Integer, Pessoa> afilhados = pessoa.getAfilhados();
        Map<Integer, Pessoa> padrinhos = pessoa.getPadrinhos();
        for (Pessoa afilhado : afilhados.values()) {
            afilhado.removerPadrinho(fenixId);
        }
        for (Pessoa padrinho : padrinhos.values()) {
            padrinho.removerAfilhado(fenixId);
        }
        _pessoas.remove(fenixId);
    }

    public void adicionarPadrinho(int afilhadoId, int padrinhoId) throws UnknownPersonException {
        Pessoa afilhado = _pessoas.get(afilhadoId);
        Pessoa padrinho = _pessoas.get(padrinhoId);

        if (afilhado == null) {
            throw new UnknownPersonException(afilhadoId);
        }
        if (padrinho == null) {
            throw new UnknownPersonException(padrinhoId);
        }

        afilhado.adicionarPadrinho(padrinho);
        padrinho.adicionarAfilhado(afilhado);
    }

    public void removerPadrinho(int afilhadoId, int padrinhoId) throws UnknownPersonException {
        Pessoa afilhado = _pessoas.get(afilhadoId);
        Pessoa padrinho = _pessoas.get(padrinhoId);

        if (afilhado == null) {
            throw new UnknownPersonException(afilhadoId);
        }
        if (padrinho == null) {
            throw new UnknownPersonException(padrinhoId);
        }

        afilhado.removerPadrinho(padrinhoId);
        padrinho.removerAfilhado(afilhadoId);
    }

    public Pessoa getPessoa(int fenixId) {
        return _pessoas.get(fenixId);
    }

    public void printPessoa(int fenixId) throws UnknownPersonException {
        Pessoa pessoa = getPessoa(fenixId);
        if (pessoa == null) {
            throw new UnknownPersonException(fenixId);
        } else {
            pessoa.mostrarInformacoes();
        }
    }

    public Map<Integer, Pessoa> getPessoas() {
        return _pessoas;
    }

    public void atualizarPessoaId(int fenixId, int novoFenixId) throws DuplicatePersonException {

        if (_pessoas.containsKey(novoFenixId)) {
            throw new DuplicatePersonException(novoFenixId);
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
    

    public void mostrarArvoreGenealogicaCima(int fenixId) throws UnknownPersonException {
        Pessoa pessoa = _pessoas.get(fenixId);
        if (pessoa == null) {
            throw new UnknownPersonException(fenixId);
        }
        System.out.println();
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

    public void mostrarArvoreGenealogicaBaixo(int fenixId) throws UnknownPersonException {
        Pessoa pessoa = _pessoas.get(fenixId);
        if (pessoa == null) {
            throw new UnknownPersonException(fenixId);
        }
        System.out.println();
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