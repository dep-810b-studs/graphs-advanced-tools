package ru.mai.dep810;

public class Main {

    public static void main(String[] args) {

        var aVertex = new Vertex("A");
        var bVertex = new Vertex("B");
        var cVertex = new Vertex("C");
        var dVertex = new Vertex("D");
        var eVertex = new Vertex("E");
        var fVertex = new Vertex("F");
        var gVertex = new Vertex("G");
        var hVertex = new Vertex("H");

	    var graph = new Graph();

	    graph
                .AddEdge(new Edge(aVertex, bVertex))
                .AddEdge(new Edge(aVertex, eVertex))
                .AddEdge(new Edge(bVertex, cVertex))
                .AddEdge(new Edge(bVertex, dVertex))
                .AddEdge(new Edge(dVertex, eVertex))
                .AddEdge(new Edge(eVertex, fVertex))
                .AddEdge(new Edge(cVertex, gVertex))
                .AddEdge(new Edge(cVertex, hVertex));


	    graph.breadthFirstSearch(aVertex);
	    graph.depthFirstSearch(aVertex);
    }
}
