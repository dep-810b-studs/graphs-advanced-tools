package ru.mai.dep810

import org.apache.spark.graphx.Graph
import ru.mai.dep810.GraphSpark.VerticeInfo

object Main extends App{
  val sparkSession = GraphSpark.InitSession()
  val verticesRdd = GraphSpark.ReadVerticesFromFile(sparkSession,"instagram_data")
  val edgesRdd = GraphSpark.ReadEdgesFromFile(sparkSession, "edges")

  val graph: Graph[VerticeInfo, Double] = Graph(verticesRdd, edgesRdd)
  graph.inDegrees.take(10).foreach(println)
}