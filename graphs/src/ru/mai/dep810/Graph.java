package ru.mai.dep810;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private ArrayList<Edge> _edges;

    public Graph() {
        _edges = new ArrayList<Edge>();
    }

    public Graph AddEdge(Edge edge){
        _edges.add(edge);
        return this;
    }

    public void breadthFirstSearch(Vertex startVertex){

        var vertexQueue = new ArrayDeque<Vertex>();
        vertexQueue.offer(startVertex);

        var visitedEdges = new ArrayList<Edge>();

        while (!vertexQueue.isEmpty()){
            var currentVertex = vertexQueue.poll();

            for (var edge: getRelatedEdges(currentVertex)){
                if(!visitedEdges.contains(edge)){
                    vertexQueue.offer(edge.getSecondVertex());
                    visitedEdges.add(edge);
                    System.out.println(edge);
                }
            }
        }

    }

    private List<Edge> getRelatedEdges(Vertex startVertex){
        return  _edges
                .stream()
                .filter(edge-> edge.getFirstVertex().equals(startVertex))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        var graphStringRepresentation = new StringBuilder();

        graphStringRepresentation.append("[");

        for (var edge: _edges){
            graphStringRepresentation.append(edge);
        }
        graphStringRepresentation.append("]");

        return graphStringRepresentation.toString();
    }
}
