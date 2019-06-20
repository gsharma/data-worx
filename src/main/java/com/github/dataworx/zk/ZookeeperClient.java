package com.github.dataworx.zk;

import java.io.IOException;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.admin.ZooKeeperAdmin;
import org.apache.zookeeper.client.ZKClientConfig;

/**
 * TODO
 * 
 * @author gaurav
 */
public final class ZookeeperClient extends ZooKeeperAdmin {

  // TODO
  public ZookeeperClient(String connectString, int sessionTimeout, Watcher watcher,
      ZKClientConfig conf) throws IOException {
    super(connectString, sessionTimeout, watcher, conf);
  }

}
