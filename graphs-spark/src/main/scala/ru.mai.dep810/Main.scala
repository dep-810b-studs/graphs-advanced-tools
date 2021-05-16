package ru.mai.dep810

import org.apache.spark.graphx.Graph
import ru.mai.dep810.GraphSpark.VerticeInfo
import Helpers._

object Main extends App{
  val sparkSession = GraphSpark.InitSession()
  val verticesRdd = sparkSession loadVerticiesDataset  "instagram_data"
  val edgesRdd = sparkSession loadEdgesDataset  "edges"

  val graph: Graph[VerticeInfo, Double] = Graph(verticesRdd, edgesRdd)
  graph.inDegrees.take(10).foreach(println)
}