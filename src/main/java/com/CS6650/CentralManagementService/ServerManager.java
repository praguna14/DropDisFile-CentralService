package com.CS6650.CentralManagementService;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager {
  private static Integer TIMEOUT_FOR_SERVER_HEALTH_SEC = 3;

  private Map<Integer, Process> servers;

  public ServerManager() {
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

  public void refreshActiveServers() {
    ServerLogger.log("Initializing health check for all servers " + servers.keySet());
    for (Integer serverPort : servers.keySet()) {
      Thread trackServerFailure = new Thread("" + serverPort) {
        public synchronized void run() {
          try {
            Thread.sleep(new Random().nextInt(4000));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          //API call to server and check health
          //restService.health(serverPort);
        }
      };

      Timer timer = new Timer();
      timer.schedule(new ServerTimeOutTask(trackServerFailure, timer), TIMEOUT_FOR_SERVER_HEALTH_SEC * 1000);
      trackServerFailure.start();
    }
  }

  class ServerTimeOutTask extends TimerTask {
    private Thread t;
    private Timer timer;

    ServerTimeOutTask(Thread t, Timer timer) {
      this.t = t;
      this.timer = timer;
    }

    public void run() {
      if (t != null && t.isAlive()) {
        t.interrupt();
        timer.cancel();

        int failedServerPort = Integer.parseInt(t.getName());
        handleFailedServers(failedServerPort);
      }
    }
  }

  private void handleFailedServers(int port){
      // add logic to destroy failed server and add a new replacement server
    ServerLogger.log("No response received from server at port " + port + ", server failed. ");
  }
}
