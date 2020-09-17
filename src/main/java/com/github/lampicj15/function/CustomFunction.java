package com.github.lampicj15.function;

import java.util.List;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class CustomFunction {

  @UserFunction(name = "example.nodes.haveRelationship")
  @Description("example.nodes.haveRelationship([node1, node2, ...], relationshipName) "
      + "- returns true if all nodes have a relationship with the given name")
  public boolean haveRelationship(@Name(value = "relationshipName") String relationshipName,
      @Name(value = "nodes") List<Node> nodes) {
    RelationshipType relationshipType = RelationshipType.withName(relationshipName);

    for (Node node : nodes) {
      if (!(node.hasRelationship(relationshipType))) {
        return false;
      }
    }
    return true;
  }
}
