package com.github.lampicj15.eventhandler;

import com.github.lampicj15.eventhandler.UuidKernelExtensionFactory.Dependencies;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;
import org.neo4j.logging.Log;
import org.neo4j.logging.internal.LogService;


public class UuidKernelExtension extends LifecycleAdapter {

  private final GraphDatabaseAPI db;
  private final Config config;
  private UuidTransactionEventHandler uuidTransactionEventHandler;
  private LogService logService;
  private Log log;

  public UuidKernelExtension(Dependencies dependencies) {
    this.db = dependencies.graphDatabaseService();
    this.config = dependencies.config();
    this.logService = dependencies.logService();
    this.log = logService.getUserLog(UuidKernelExtension.class);
  }

  @Override
  public void start() throws Throwable {
    log.info("Started the Custom UUID Kernel extension");
    uuidTransactionEventHandler = new UuidTransactionEventHandler(logService, config);
    db.registerTransactionEventHandler(uuidTransactionEventHandler);
  }

  @Override
  public void shutdown() throws Throwable {
    log.info("Shutting down the Custom UUID Kernel extension");
    db.unregisterTransactionEventHandler(uuidTransactionEventHandler);
  }
}
