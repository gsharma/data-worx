package com.github.dataworx.zk;

import java.io.IOException;
import java.util.List;

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

  @Override
  public List<String> getChildWatches() {
    return super.getChildWatches();
  }


  @Override
  public List<String> getDataWatches() {
    return super.getDataWatches();
  }


  @Override
  public List<String> getExistWatches() {
    return super.getExistWatches();
  }

  public long getLastZxid() {
    return cnxn.getLastZxid();
  }

  public void disconnect() {
    cnxn.disconnect();
  }

}
