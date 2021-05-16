package ru.mai.dep810

import org.apache.spark.graphx.Edge
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import ru.mai.dep810.Helpers._

object GraphSpark {

  case class VerticeInfo(countPosts: Int, engagementGrade: Double)

  def InitSession() = SparkSession
    .builder()
    .master("local[*]")
    .appName("graphTest")
    .getOrCreate()

  def ReadVerticesFromFile(session: SparkSession, datasetFileName: String): RDD[(Long, VerticeInfo)] =
    (session loadDatasetFromCSV datasetFileName)
    .map(row => (row.getAs[Int](0).toLong, VerticeInfo(row.getAs[Int](1),row.getAs[Double](4))))

  def ReadEdgesFromFile(session: SparkSession, datasetFileName: String): RDD[Edge[Double]] =
    (session loadDatasetFromCSV datasetFileName)
    .map(row => Edge(row.getAs[Int](0),row.getAs[Int](1), row.getAs[Double](2)))

}
