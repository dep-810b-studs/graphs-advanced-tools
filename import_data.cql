-- Import users statistic
CALL apoc.periodic.iterate('
CALL apoc.load.csv("dataset/instagram_data.csv", {header:true})
yield map as row return row
','
CREATE (ir:InstagramUser) SET ir = {Id: toFloat(row.id), CountPosts: toFloat(row.pos), CountFollowers: toFloat(row.flr), CountFollowing: toFloat(row.flg), EngagementGrade: toFloat(row.eg), EngagementRate: toFloat(row.er), FollowersGrowth: toFloat(row.fg), OutsidersPercentage: toFloat(row.op)}
', {batchSize:10000, iterateList:true, parallel:true});

-- Import following info
CALL apoc.periodic.iterate('
CALL apoc.load.csv("dataset/edges.csv", {header:true})
yield map as row return row ','
CREATE (flw: FollowingInfo) SET flw = {UserId: toInteger(row.user_id), Follower: toInteger(row.follower_id), Weight: toFloat(row.weight) }
', {batchSize:10000, iterateList:true, parallel:true});

CREATE CONSTRAINT UniqueUser ON (iUser:InstagramUser) ASSERT iUser.Id IS UNIQUE;

CREATE INDEX instagram_user_index IF NOT EXISTS
FOR (instUser: InstagramUser)
ON (instUser.Id);



MATCH (ir:InternalReferences)
MATCH (to_paper:User {id: ir.Node1})
MATCH (from_paper:User {id: ir.Node2})
CREATE (from_paper)-[rel:FOLLOW]->(to_paper)
RETURN count(rel);

CALL gds.graph.create(
  'InstagramFollowingsGraph',
  'InstagramUser',
  'FOLLOW'
)

CALL gds.wcc.stats('InstagramFollowingsGraph')
YIELD componentCount

MATCH (source:InstagramUser {Id: 30343}), (target:InstagramUser {Id: 15821})
CALL gds.beta.shortestPath.yens.stream('InstagramFollowingsGraph', {
    sourceNode: id(source),
    targetNode: id(target),
    k: 5
})
YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs
RETURN
    index,
    gds.util.asNode(sourceNode).name AS sourceNodeName,
    gds.util.asNode(targetNode).name AS targetNodeName,
    totalCost,
    [nodeId IN nodeIds | gds.util.asNode(nodeId).name] AS nodeNames,
    costs
ORDER BY index


