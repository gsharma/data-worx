package com.github.dataworx.kafka;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Sanity tests for zookeeper server.
 * 
 * @author gaurav
 */
public final class ZookeeperServerTest {

  @Test
  public void testServerLifecycle() {
    final ZookeeperServer zkServer = new ZookeeperServer("localhost", 9000);
    zkServer.init();
    assertTrue(zkServer.isRunning());
    zkServer.tini();
    assertFalse(zkServer.isRunning());
  }

}
