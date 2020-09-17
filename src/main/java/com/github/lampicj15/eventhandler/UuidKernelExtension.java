package com.github.lampicj15.eventhandler;

import com.github.lampicj15.eventhandler.UuidKernelExtensionFactory.Dependencies;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;


public class UuidKernelExtension extends LifecycleAdapter {

  private final GraphDatabaseAPI db;
  private final Config config;
  private UuidTransactionEventHandler uuidTransactionEventHandler;

  public UuidKernelExtension(Dependencies dependencies) {
    this.db = dependencies.graphDatabaseService();
    this.config = dependencies.config();
  }

  @Override
  public void start() throws Throwable {
    uuidTransactionEventHandler = new UuidTransactionEventHandler(config);
    db.registerTransactionEventHandler(uuidTransactionEventHandler);
  }

  @Override
  public void shutdown() throws Throwable {
    db.unregisterTransactionEventHandler(uuidTransactionEventHandler);
  }
}
