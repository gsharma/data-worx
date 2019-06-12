package com.github.dataworx.zk;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private final int maxConnections;
  // private final int tickTime = 200;
  private final String logDirectoryName = "zkLogs";
  private final String snapshotDirectoryName = "zkSnapshots";
  private ServerCnxnFactory connectionFactory;
  private File snapshotDirectory;
  private File logDirectory;

  public ZookeeperServer(final String host, final int port, final int maxConnections) {
    this.host = host;
    this.port = port;
    this.maxConnections = maxConnections;
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
        connectionFactory = NettyServerCnxnFactory.createFactory();
        connectionFactory.configure(new InetSocketAddress(host, port), maxConnections);
        connectionFactory.startup(zkServer);
        running.set(true);
        logger.info("Setup Zk snapshotDir:{}, files:{}", snapshotDirectory.getPath(),
            Arrays.deepToString(snapshotDirectory.list()));
        logger.info("Setup Zk logDir:{}, files:{}", logDirectory.getPath(),
            Arrays.deepToString(logDirectory.list()));
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
      // logger.debug(connectionFactory.getAllConnectionInfo(false));
      connectionFactory.shutdown();
      logger.debug("Zk snapshotDir:{}, files:{}", snapshotDirectory.getPath(),
          Arrays.deepToString(snapshotDirectory.list()));
      logger.debug("Zk logDir:{}, files:{}", logDirectory.getPath(),
          Arrays.deepToString(logDirectory.list()));
      final boolean snapshotDirDeleted = deleteDirectory(snapshotDirectory);
      final boolean logDirDeleted = deleteDirectory(logDirectory);
      logger.info("Stopped ZookeeperServer at {}:{}, snapshotDirDeleted:{}, logDirDeleted:{}", host,
          port, snapshotDirDeleted, logDirDeleted);
    }
  }

  private static boolean deleteDirectory(final File directory) {
    final File[] allFiles = directory.listFiles();
    if (allFiles != null) {
      for (final File file : allFiles) {
        deleteDirectory(file);
      }
    }
    return directory.delete();
  }

  public boolean isRunning() {
    return running.get();
  }

}
