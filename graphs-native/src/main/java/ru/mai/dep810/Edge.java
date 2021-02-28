package ru.mai.dep810;

import java.util.Objects;

public class Edge {

    private final Vertex _firstVertex;
    private final Vertex _secondVertex;

    public Edge(Vertex firstVertex, Vertex secondVertex) {
        _firstVertex = firstVertex;
        _secondVertex = secondVertex;
    }

    public Vertex getFirstVertex() {
        return _firstVertex;
    }

    public Vertex getSecondVertex() {
        return _secondVertex;
    }

    @Override
    public String toString() {
        StringBuilder edgeStringRepresentation = new StringBuilder();

        edgeStringRepresentation.append("(");
        edgeStringRepresentation.append(_firstVertex.getName());
        edgeStringRepresentation.append("->");
        edgeStringRepresentation.append(_secondVertex.getName());
        edgeStringRepresentation.append(")");

        return edgeStringRepresentation.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(_firstVertex, edge._firstVertex) && Objects.equals(_secondVertex, edge._secondVertex);
    }
}
