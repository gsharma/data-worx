package com.github.dataworx.zk;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.junit.Test;

/**
 * Sanity tests for zookeeper server.
 * 
 * @author gaurav
 */
public final class ZookeeperServerTest {

  @Test
  public void testServerLifecycle() {
    QuorumPeerConfig.setReconfigEnabled(true);
    final ZookeeperServer one = new ZookeeperServer("localhost", 9001, 2);
    one.init();
    assertTrue(one.isRunning());
    
    final ZookeeperServer two = new ZookeeperServer("localhost", 9002, 2);
    two.init();
    assertTrue(two.isRunning());
    
    final ZookeeperServer three = new ZookeeperServer("localhost", 9003, 2);
    three.init();
    assertTrue(three.isRunning());

    // tear down all servers
    one.tini();
    assertFalse(one.isRunning());
    two.tini();
    assertFalse(two.isRunning());
    three.tini();
    assertFalse(three.isRunning());
  }

}
