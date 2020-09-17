package com.github.lampicj15.eventhandler;

import com.github.lampicj15.function.CustomFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.TestServerBuilders;

class UuidTransactionEventHandlerIT {

  private GraphDatabaseService db;

  @BeforeEach
  public void setupGraphDatabase() {
    db = TestServerBuilders.newInProcessBuilder()
        .withFunction(CustomFunction.class)
        .withConfig("example.uuid.property", "cleverName")
        .newServer()
        .graph();
  }

  @AfterEach
  public void tearDownGraphDatabase() {
    db.shutdown();
  }

  @Test
  public void shouldAddUuidToNode() {
    db.execute("CREATE (:Test)");

    try (Transaction tx = db.beginTx()) {
      Node node = db.findNodes(Label.label("Test")).next();
      Assertions.assertTrue(node.hasProperty("cleverName"));
      tx.success();
    }
  }

  @Test
  public void shouldAddUuidToRelationship() {
    db.execute("CREATE (:Developer)-[:LOVES]->(:GraphDatabase)");

    try (Transaction tx = db.beginTx()) {
      Relationship relationship = db.findNodes(Label.label("Developer"))
          .next()
          .getSingleRelationship(RelationshipType.withName("LOVES"), Direction.OUTGOING);

      Assertions.assertTrue(relationship.hasProperty("cleverName"));
      tx.success();
    }
  }

}