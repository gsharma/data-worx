package com.github.dataworx.zk;

import static org.junit.Assert.assertEquals;
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
    final ZookeeperServer one = new ZookeeperServer(1, "localhost", 9001, 2);
    one.init();
    assertTrue(one.isRunning());
    assertEquals("standalone", one.getServer().getState());
    assertEquals(0, one.getServer().getZxid());

    final ZookeeperServer two = new ZookeeperServer(2, "localhost", 9002, 2);
    two.init();
    assertTrue(two.isRunning());
    assertEquals("standalone", two.getServer().getState());
    assertEquals(0, two.getServer().getZxid());

    final ZookeeperServer three = new ZookeeperServer(3, "localhost", 9003, 2);
    three.init();
    assertTrue(three.isRunning());
    assertEquals("standalone", three.getServer().getState());
    assertEquals(0, three.getServer().getZxid());

    // tear down all servers
    one.tini();
    assertFalse(one.isRunning());
    two.tini();
    assertFalse(two.isRunning());
    three.tini();
    assertFalse(three.isRunning());
  }

}
