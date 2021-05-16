package ru.mai.dep810

case class VertexInfo(CountPosts: Int,
                      CountFollowers: Int,
                      CountFollowings: Int,
                      EngagementGrade: Double,
                      EngagementRate: Double,
                      FollowersGrowth: Double,
                      OutsidersPercentage: Double) {
  def calculateMeasure():Double =
    CountFollowers * ( (1 / (CountFollowers + CountFollowings)) +
      EngagementGrade + (EngagementRate / 12)  + (FollowersGrowth / 100) - (OutsidersPercentage / 100))
}


