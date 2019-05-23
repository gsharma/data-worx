package com.github.dataworx.kafka;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.NettyServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

/**
 * This class serves as the driver for running a single-node embedded zookeeper for quick testing.
 * 
 * @author gaurav
 */
public final class ZookeeperServer {
  private static final Logger logger = LogManager.getLogger(ZookeeperServer.class.getSimpleName());
  private final AtomicBoolean running = new AtomicBoolean();
  private final String host;
  private final int port;
  private final int maxConnections = 4;
  // private final int tickTime = 200;
  private final String logDirectoryName = "zkLogs";
  private final String snapshotDirectoryName = "zkSnapshots";
  private ServerCnxnFactory factory;
  private File snapshotDirectory;
  private File logDirectory;

  public ZookeeperServer(final String host, final int port) {
    this.host = host;
    this.port = port;
  }

  public void init() {
    if (running.compareAndSet(false, true)) {
      try {
        snapshotDirectory = java.nio.file.Files.createTempDirectory(snapshotDirectoryName).toFile();
        logDirectory = java.nio.file.Files.createTempDirectory(logDirectoryName).toFile();
      } catch (IOException problem) {
      }
      try {
        final ZooKeeperServer zkServer =
            new ZooKeeperServer(snapshotDirectory, logDirectory, ZooKeeperServer.DEFAULT_TICK_TIME);
        factory = NettyServerCnxnFactory.createFactory();
        // factory = NIOServerCnxnFactory.createFactory();
        factory.configure(new InetSocketAddress(host, port), maxConnections);
        factory.startup(zkServer);
        running.set(true);
        logger.info("Started ZookeeperServer at {}:{}", host, port);
      } catch (Exception problem) {
        logger.error(String.format("Failed to start ZookeeperServer at {}:{}", host, port),
            problem);
      }
    }
  }

  public void tini() {
    if (running.compareAndSet(true, false)) {
      running.set(false);
      factory.shutdown();
      snapshotDirectory.delete();
      logDirectory.delete();
      logger.info("Stopped ZookeeperServer at {}:{}", host, port);
    }
  }

  public boolean isRunning() {
    return running.get();
  }

}
