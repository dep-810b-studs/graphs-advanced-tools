LOAD CSV WITH HEADERS FROM 'file:///musae_facebook_target.csv' AS line
CREATE (:Page {id: toInteger(line.id), facebookId: toInteger(line.facebook_id), pageName: line.page_name, pageType: line.page_type});
MATCH (p:Page) RETURN p;

LOAD CSV WITH HEADERS FROM 'file:///musae_facebook_edges.csv' AS line
MATCH (page:Page {id: toInteger(line.id_1)}), (repost_page:Page {id: toInteger(line.id_2)})
MERGE (page)-[:REPOST]->(repost_page);