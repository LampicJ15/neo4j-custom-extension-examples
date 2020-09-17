package com.github.lampicj15.function;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.TestServerBuilders;

class CustomFunctionIT {

  private GraphDatabaseService db;

  @BeforeEach
  public void setupGraphDatabase() {
    db = TestServerBuilders.newInProcessBuilder()
        .withFunction(CustomFunction.class)
        .newServer()
        .graph();
  }

  @AfterEach
  public void tearDownGraphDatabase() {
    db.shutdown();
  }

  @Test
  public void shouldReturnTrue_allNodesHaveCommonRelationship() {
    db.execute("CREATE (:Developer)-[:LIKES]->(:GraphDatabase)");
    db.execute("CREATE (:Student)-[:LIKES]->(:School)");
    db.execute("CREATE (:Person)-[:LIKES]->(:IceCream)");

    Map<String, Object> result = db.execute("MATCH (n)"
            + "WITH collect(n) AS nodes "
            + "RETURN example.nodes.haveRelationship($relationshipName, nodes) AS value",
        Collections.singletonMap("relationshipName", "LIKES")).next();

    Assertions.assertTrue((Boolean) result.get("value"));
  }

  @Test
  public void shouldReturnFalse_nodesDoNotHaveCommonRelationship() {
    db.execute("CREATE (:Developer)-[:LIKES]->(:GraphDatabase)");
    db.execute("CREATE (:Student)-[:LIKES]->(:School)");
    db.execute("CREATE (:Person)-[:LIKES]->(:IceCream)");
    db.execute("CREATE (:Person)-[:DISLIKES]->(:Year {value:2020})");

    Map<String, Object> result = db.execute("MATCH (n)"
            + "WITH collect(n) AS nodes "
            + "RETURN example.nodes.haveRelationship($relationshipName, nodes) AS value",
        Collections.singletonMap("relationshipName", "LIKES")).next();

    Assertions.assertEquals(false, result.get("value"));
  }
}