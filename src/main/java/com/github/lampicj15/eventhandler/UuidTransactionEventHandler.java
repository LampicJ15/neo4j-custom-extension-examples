package com.github.lampicj15.eventhandler;

import java.util.UUID;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.configuration.Config;

public class UuidTransactionEventHandler implements TransactionEventHandler {

  private final String uuidProperty;

  public UuidTransactionEventHandler(Config configuration) {
    uuidProperty = configuration.getRaw().getOrDefault("example.uuid.property", "uuid");
  }

  @Override
  public Object beforeCommit(TransactionData transactionData) throws Exception {
    transactionData.createdNodes().forEach(this::addUuid);
    transactionData.createdRelationships().forEach(this::addUuid);
    return null;
  }

  @Override
  public void afterCommit(TransactionData transactionData, Object o) {

  }

  @Override
  public void afterRollback(TransactionData transactionData, Object o) {

  }

  private void addUuid(Entity entity) {
    if (!(entity.hasProperty(uuidProperty))) {
      entity.setProperty(uuidProperty, UUID.randomUUID().toString());
    }
  }

}
