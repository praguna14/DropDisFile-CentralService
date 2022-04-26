package com.CS6650.CentralManagementService;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServersInfo {
  private Map<Integer, Process> servers;

  public ServersInfo() {
    this.servers = Collections.synchronizedMap(new HashMap<>());
  }

  public void addNewServerProcess(int serverPort, Process proc) {
    servers.put(serverPort, proc);
  }

  public Set<Integer> getAllActiveServerPorts() {
    return new HashSet<>(servers.keySet());
  }

  public String destroyServer(int serverPort) {
    if(servers.get(serverPort) == null){
      return "Error: Server on port " + serverPort + " does not exist";
    } else {
      Process proc = servers.get(serverPort);
      proc.destroy();
      servers.remove(serverPort);
      return "Successfully destroyed server on port " + serverPort;
    }
  }
}
