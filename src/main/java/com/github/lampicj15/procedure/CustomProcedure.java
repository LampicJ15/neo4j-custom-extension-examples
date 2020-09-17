package com.github.lampicj15.procedure;

import static org.neo4j.procedure.Mode.WRITE;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class CustomProcedure {

  @Procedure(mode = WRITE, name = "example.nodes.doublyLinkedList")
  @Description("example.nodes.doublyLinkedList([node1, node2, ...], previousRelationship, nextRelationship)"
      + "- creates a doubly connected linked list")
  public Stream<NodeResult> createDoublyLinkedList(
      @Name("nodes") List<Node> nodes,
      @Name(value = "previousRelationship", defaultValue = "PREV") String previousRelationship,
      @Name(value = "nextRelationship", defaultValue = "NEXT") String nextRelationship) {

    createLinkedList(nodes, RelationshipType.withName(nextRelationship));
    Collections.reverse(nodes);
    createLinkedList(nodes, RelationshipType.withName(previousRelationship));

    return nodes.stream().map(NodeResult::new);
  }

  private void createLinkedList(List<Node> nodes, RelationshipType relationshipType) {
    Iterator<Node> it = nodes.iterator();
    if (it.hasNext()) {
      Node node = it.next();
      while (it.hasNext()) {
        Node next = it.next();
        node.createRelationshipTo(next, relationshipType);
        node = next;
      }
    }
  }
}
