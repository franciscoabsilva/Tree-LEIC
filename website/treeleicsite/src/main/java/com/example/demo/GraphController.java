package com.example.demo;

import core.ArvoreLEIC;
import core.Manager;
import core.Pessoa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Map;

@RestController
public class GraphController {

    // Instanciamos o teu Manager para carregar os dados
    private final Manager manager;

    public GraphController() {
        this.manager = new Manager();
        // Certifica-te que o ficheiro arvoreLEIC.dat está na raiz do projeto ao correr
        this.manager.load(); 
    }

    @GetMapping("/api/grafo")
    public GraphDTO getGraphData() {
        ArvoreLEIC arvore = manager.getArvore();
        Map<Integer, Pessoa> pessoas = arvore.getPessoas();

        GraphDTO graph = new GraphDTO();

        // 1. Criar os Nós (Pessoas)
        for (Pessoa p : pessoas.values()) {
            String displayInfo = "<b>" + p.getNome() + "</b><br>" +
                                 "Alcunha: " + p.getAlcunha() + "<br>" +
                                 "ID: " + p.getFenixId() + "<br>" +
                                 "Matrículas: " + p.getMatriculas();
            
            // 1. Separar o nome completo por espaços
            String[] nomes = p.getNome().split(" ");
            String primeiroNome = nomes[0];
            // Se tiver mais que um nome, pega no último. Se for só um nome (raro), fica vazio.
            String ultimoNome = (nomes.length > 1) ? nomes[nomes.length - 1] : "";

            String label;

            // 2. Construir o formato pedido
            if (!p.getAlcunha().isEmpty()) {
                // Se TEM alcunha: Nome "Alcunha" Apelido
                // (O \" serve para escrever as aspas visíveis no ecrã)
                label = primeiroNome + " \"" + p.getAlcunha() + "\" " + ultimoNome;
            } else {
                // Se NÃO tem alcunha: Nome Apelido
                label = primeiroNome + " " + ultimoNome;
            }
            
            GraphDTO.Node node = new GraphDTO.Node(p.getFenixId(), label, displayInfo);
            
            // ADICIONA ESTA LINHA:
            // Agrupa por número de matrículas. O Vis.js vai dar uma cor diferente a cada ano.
            node.group = "Ano " + p.getMatriculas(); 
            
            graph.nodes.add(node);
            // 2. Criar as Arestas (Relações)
            // Vamos iterar apenas os afilhados para criar a seta Padrinho -> Afilhado
            // Isso evita duplicar arestas
            for (Integer afilhadoId : p.getAfilhados().keySet()) {
                // 'p' é o Padrinho, 'afilhadoId' é o Afilhado
                graph.edges.add(new GraphDTO.Edge(p.getFenixId(), afilhadoId));
            }
        }

        return graph;
    }
}