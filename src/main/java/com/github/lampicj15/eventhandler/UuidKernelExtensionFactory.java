package com.github.lampicj15.eventhandler;

import com.github.lampicj15.eventhandler.UuidKernelExtensionFactory.Dependencies;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.ExtensionType;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.logging.internal.LogService;

public class UuidKernelExtensionFactory extends KernelExtensionFactory<Dependencies> {

  public UuidKernelExtensionFactory() {
    super(ExtensionType.DATABASE, "UUID_KERNEL_EXTENSION_FACTORY");
  }


  @Override
  public Lifecycle newInstance(KernelContext kernelContext, Dependencies dependencies) {
    return new UuidKernelExtension(dependencies);
  }

  public interface Dependencies {

    GraphDatabaseAPI graphDatabaseService();

    Config config();

    LogService logService();
  }
}
