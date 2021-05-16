package ru.mai.dep810

import org.apache.spark.graphx.Edge
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}

object CsvDataReader {
  private val DATASET_PATH = "./import/dataset/"

  private val ID_FIELD_NAME = "id"
  private val COUNT_POSTS_FIELD_NAME = "pos"
  private val COUNT_FOLLOWERS_FIELD_NAME = "flr"
  private val COUNT_FOLLOWINGS_FIELD_NAME = "flg"
  private val ENGAGEMENT_GRADE_FIELD_NAME = "eg"
  private val ENGAGEMENT_RATE_FIELD_NAME = "er"
  private val FOLLOWERS_GROWTH_PERCENTAGE_FIELD_NAME = "fg"
  private val OUTSIDERS_PERCENTAGE_FIELD_NAME = "op"

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
       .map(row => (row.getAs[Int](ID_FIELD_NAME).toLong, VertexInfo(row.getAs[Int](COUNT_POSTS_FIELD_NAME),
         row.getAs[Int](COUNT_FOLLOWERS_FIELD_NAME),
         row.getAs[Int](COUNT_FOLLOWINGS_FIELD_NAME),
         row.getAs[Double](ENGAGEMENT_GRADE_FIELD_NAME),
         row.getAs[Double](ENGAGEMENT_RATE_FIELD_NAME),
         row.getAs[Double](FOLLOWERS_GROWTH_PERCENTAGE_FIELD_NAME),
         row.getAs[Double](OUTSIDERS_PERCENTAGE_FIELD_NAME))))

    def loadEdgesDataset(datasetName: String) =
      (session loadDatasetFromCSV datasetName)
        .map(row => Edge(row.getAs[Int](0),row.getAs[Int](1), row.getAs[Double](2)))
  }
}
