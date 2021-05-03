LOAD CSV WITH HEADERS FROM 'file:///musae_facebook_target.csv' AS line
CREATE (:Page {id: toInteger(line.id), facebookId: toInteger(line.facebook_id), pageName: line.page_name, pageType: line.page_type});
MATCH (p:Page) RETURN p;
LOAD CSV WITH HEADERS FROM 'file:///musae_facebook_edges.csv' AS line
MATCH (a:Page), (b:Page)
WHERE a.id = line.id_1 AND b.id = line.id_2
CREATE (a)-[r:RELTYPE {name: a.pageName + '->' + b.pageName}]->(b);