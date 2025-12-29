package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class GraphDTO {
    public List<Node> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();

    public static class Node {
        public int id;
        public String label;
        public String title; // Tooltip (aparece ao passar o rato)
        public String group; // Para colorir por ano/curso se quiseres

        public Node(int id, String label, String title) {
            this.id = id;
            this.label = label;
            this.title = title;
        }
    }

    public static class Edge {
        public int from;
        public int to;
        public String arrows; // "to", "from", "middle"

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
            this.arrows = "to"; // Aponta do Padrinho para o Afilhado
        }
    }
}