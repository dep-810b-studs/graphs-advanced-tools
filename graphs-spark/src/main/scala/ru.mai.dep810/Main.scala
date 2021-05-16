package ru.mai.dep810

import org.apache.spark.graphx.{Graph, VertexRDD}
import ru.mai.dep810.CsvDataReader._

object Main extends App{
  val sparkSession = GraphSpark.InitSession()
  val verticesRdd = sparkSession loadVerticiesDataset  "instagram_data"
  val edgesRdd = sparkSession loadEdgesDataset  "edges"

  val graph: Graph[VertexInfo, Double] = Graph(verticesRdd, edgesRdd)
  println("In Degrees:")
  graph.inDegrees
    .sortBy(-_._2)
    .take(10)
    .foreach(println)
  println("Out Degrees:")
  graph.outDegrees
    .sortBy(-_._2)
    .take(10)
    .foreach(println)

  val furtherFollowees: VertexRDD[(Int, Double)] = graph.aggregateMessages[(Int, Double)](
    triplet => triplet.sendToSrc((triplet.srcId.toInt, triplet.srcAttr.calculateMeasure())),
    (a, b) => (a._1,a._2 + b._2 ))

  furtherFollowees
    .sortBy(-_._2._2)
    .take(10)
    .foreach(println)

}