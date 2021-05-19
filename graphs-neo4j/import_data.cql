-- Import users statistic
CALL apoc.periodic.iterate('
CALL apoc.load.csv("dataset/instagram_data.csv", {header:true})
yield map as row return row
','
CREATE (ir:InstagramUserInfo) SET ir = {Id: toInteger(row.id), CountPosts: toInteger(row.pos), CountFollowers: toInteger(row.flr), CountFollowing: toInteger(row.flg), EngagementGrade: toFloat(row.eg), EngagementRate: toFloat(row.er), FollowersGrowth: toFloat(row.fg), OutsidersPercentage: toFloat(row.op)}
', {batchSize:10000, iterateList:true, parallel:true});

-- Import following info
CALL apoc.periodic.iterate('
CALL apoc.load.csv("dataset/edges.csv", {header:true})
yield map as row return row ','
CREATE (flw: InstagramUserFollowingInfo) SET flw = {UserId: toInteger(row.user_id), Follower: toInteger(row.follower_id), Weight: toFloat(row.weight) }
', {batchSize:10000, iterateList:true, parallel:true});

CREATE CONSTRAINT UniqueUser ON (iUser:InstagramUser) ASSERT iUser.Id IS UNIQUE;

CREATE INDEX instagram_user_index IF NOT EXISTS
FOR (instUser: InstagramUser)
ON (instUser.Id);


MATCH (ir:InstagramUserFollowingInfo)
MATCH (instUser:InstagramUserInfo {Id: ir.UserId})
MATCH (instFollower:InstagramUserInfo {id: ir.Follower})
CREATE (instFollower)-[rel:FOLLOW {ir.Weight}]->(instUser)
RETURN count(rel);

CALL gds.graph.create(  
  'InstagramUsersFollowingsGraph',
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

CALL gds.pageRank.stream('  ', {
  maxIterations: 20,
  dampingFactor: 0.85
})
YIELD nodeId, score
RETURN gds.util.asNode(nodeId).id AS Id, score
ORDER BY score DESC, name ASC LIMIT 10

MATCH (instUser: InstagramUser {Id: 29422})
CALL gds.alpha.spanningTree.minimum.write({
  nodeProjection: 'InstagramUsersFollowingsGraph',
  relationshipProjection: {
    LINK: {
      type: 'FOLLOW',
      properties: 'weight'
    }
  },
  startNodeId: id(instUser),
  writeProperty: 'MINST',
  weightWriteProperty: 'writeCost',
  relationshipWeightProperty: 'weight',
})
YIELD createMillis, computeMillis, writeMillis, effectiveNodeCount
RETURN createMillis, computeMillis, writeMillis, effectiveNodeCount;

MATCH path = (instUser: InstagramUser {Id: 29422.0})-[:MINST*]-()
WITH relationships(path) AS rels
UNWIND rels AS rel
WITH DISTINCT rel AS rel
RETURN startNode(rel).Id AS source, endNode(rel).Id AS destination, rel.writeCost AS cost;

MATCH (instUser: InstagramUserInfo {Id: 29422})
CALL gds.alpha.spanningTree.minimum.write({
  nodeProjection: 'InstagramUserInfo',
  relationshipProjection: {
    LINK: {
      type: 'FOLLOW',
      properties: 'weight'
    }
  },
  startNodeId: id(instUser),
  writeProperty: 'MINST',
  weightWriteProperty: 'writeCost',
  relationshipWeightProperty: 'weight'
})
YIELD createMillis, computeMillis, writeMillis, effectiveNodeCount
RETURN createMillis, computeMillis, writeMillis, effectiveNodeCount
LIMIT 10;

MATCH path = (userInfo:InstagramUserInfo {Id: 29422})-[:MINST*]-()
WITH relationships(path) AS rels
UNWIND rels AS rel
WITH DISTINCT rel AS rel
YIELD startNode(rel).Id AS source, endNode(rel).Id AS destination, rel.writeCost AS cost
LIMIT 1000;

MATCH (a:InstagramUserInfo {Id: 29422})
MATCH (d:InstagramUserInfo {Id: 25590})
WITH id(a) AS startNode, id(d) AS targetNode
CALL gds.alpha.bfs.stream('InstagramUsersFollowingsGraph', {startNode: startNode, targetNodes: [targetNode] })
YIELD path
UNWIND [ n in nodes(path) | (n.CountFollowers * ( (1 / n.CountFollowing) +
      n.EngagementGrade + (n.EngagementRate / 12)  + (n.FollowersGrowth / 100) - (n.OutsidersPercentage / 100)) )] AS measures
RETURN sum(measures) as TotalMeasure