package com.github.lampicj15.procedure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.TestServerBuilders;

class CustomProcedureIT {

  private GraphDatabaseService db;

  @BeforeEach
  public void setupGraphDatabase() {
    db = TestServerBuilders.newInProcessBuilder()
        .withProcedure(CustomProcedure.class)
        .newServer()
        .graph();
  }

  @AfterEach
  public void tearDownGraphDatabase() {
    db.shutdown();
  }

  @Test
  public void shouldCreateDoublyLinkedList() {
    db.execute("CREATE (:Node {number:1})");
    db.execute("CREATE (:Node {number:2})");
    db.execute("CREATE (:Node {number:3})");

    db.execute("MATCH (n:Node) "
        + "WITH n "
        + "ORDER BY n.number ASCENDING "
        + "WITH collect(n) AS nodes "
        + "CALL example.nodes.doublyLinkedList(nodes) "
        + "YIELD node RETURN node");

    long numberOfRelationships = (long) db
        .execute("MATCH ()-[rel]->() RETURN count(rel) AS relationshipsCount").next()
        .get("relationshipsCount");

    Assertions.assertEquals(4, numberOfRelationships);
  }
}