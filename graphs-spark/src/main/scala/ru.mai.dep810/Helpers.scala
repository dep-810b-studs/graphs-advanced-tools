package ru.mai.dep810

import org.apache.spark.graphx.Edge
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import ru.mai.dep810.GraphSpark.VerticeInfo

object Helpers {
  private val DATASET_PATH = "./import/dataset/"

  implicit class SparkSessionReader(session: SparkSession){
    def loadDatasetFromCSV(datasetName: String) : RDD[Row]=
      session
        .read
        .format("com.databricks.spark.csv")
        .option("sep",",")
        .option("header","true")
        .option("inferSchema", "true")
        .load(f"${DATASET_PATH}/${datasetName}.csv")
        .rdd

    def loadVerticiesDataset(datasetName: String) =
      (session loadDatasetFromCSV datasetName)
       .map(row => (row.getAs[Int](0).toLong, VerticeInfo(row.getAs[Int](1),row.getAs[Double](4))))

    def loadEdgesDataset(datasetName: String) =
      (session loadDatasetFromCSV datasetName)
        .map(row => Edge(row.getAs[Int](0),row.getAs[Int](1), row.getAs[Double](2)))

  }
}
