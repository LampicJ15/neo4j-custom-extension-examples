package com.github.lampicj15.eventhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.logging.Log;
import org.neo4j.logging.internal.LogService;

public class UuidTransactionEventHandler implements TransactionEventHandler<List<Entity>> {

  private final String uuidProperty;
  private final Log log;

  public UuidTransactionEventHandler(LogService logService, Config configuration) {
    uuidProperty = configuration.getRaw().getOrDefault("example.uuid.property", "uuid");
    log = logService.getUserLog(UuidTransactionEventHandler.class);
  }

  @Override
  public List<Entity> beforeCommit(TransactionData transactionData) throws Exception {
    List<Entity> entities = new ArrayList<>();

    transactionData.createdNodes()
        .forEach(node -> {
          if (addUuid(node)) {
            entities.add(node);
          }
        });

    transactionData.createdRelationships()
        .forEach(relationship -> {
          if (addUuid(relationship)) {
            entities.add(relationship);
          }
        });

    return entities;
  }

  @Override
  public void afterCommit(TransactionData transactionData, List<Entity> entities) {
    entities.forEach(entity ->
        log.info("Added uuid property to entity with id: " + entity.getId())
    );
  }

  @Override
  public void afterRollback(TransactionData transactionData, List<Entity> entities) {
  }

  private boolean addUuid(Entity entity) {
    if (!(entity.hasProperty(uuidProperty))) {
      entity.setProperty(uuidProperty, UUID.randomUUID().toString());
      return true;
    }
    return false;
  }

}
