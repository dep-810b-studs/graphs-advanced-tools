package ru.mai.dep810;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private ArrayList<Edge> _edges;

    public Graph() {
        _edges = new ArrayList();
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

    public void depthFirstSearch(Vertex startVertex){

        var vertexStack = new Stack<ColoredVertex>();
        vertexStack.add(new ColoredVertex(startVertex));

        var wayEnded = false;
        var notWayEndedAfterWayEnded = true;

        while (!vertexStack.isEmpty()){
            var topVertex = vertexStack.lastElement();

            switch (topVertex.getColor()){
                case White:
                    var neighbors = getNeighbors(topVertex);
                    topVertex.setColor(VertexColor.Grey);

                    if(wayEnded){
                        wayEnded = false;
                        notWayEndedAfterWayEnded = false;
                        var firstTime = true;

                        for(var vertex: vertexStack){
                            if(!firstTime)
                                System.out.print("->");
                            else firstTime= false;
                            System.out.print(vertex.getVertex().getName());
                        }
                    }
                    else{
                        if(!notWayEndedAfterWayEnded){
                            System.out.print("->");
                            notWayEndedAfterWayEnded = true;
                        }
                        System.out.print(topVertex.getVertex().getName());
                        if(!neighbors.isEmpty())
                            System.out.print("->");
                    }
                    vertexStack.addAll(neighbors);
                    break;
                case Grey:
                    if(!wayEnded)
                        System.out.println();
                    wayEnded = true;
                    vertexStack.pop();
                    break;
            }
        }

    }

    private List<Edge> getRelatedEdges(Vertex startVertex){
        return  _edges
                .stream()
                .filter(edge-> edge.getFirstVertex().equals(startVertex))
                .collect(Collectors.toList());
    }

    private List<ColoredVertex> getNeighbors(ColoredVertex coloredVertex){
        return  _edges
                .stream()
                .filter(edge-> edge.getFirstVertex().equals(coloredVertex.getVertex()))
                .map(edge -> new ColoredVertex(edge.getSecondVertex()))
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
