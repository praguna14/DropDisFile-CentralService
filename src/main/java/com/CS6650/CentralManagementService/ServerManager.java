package com.CS6650.CentralManagementService;

import com.CS6650.CentralManagementService.service.RestService;
import com.CS6650.CentralManagementService.utility.ServerCreation;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager {

  @Autowired
  RestService restService;

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
          //API call to server and check health
          restService.getHealth(serverPort);
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
    ServerLogger.log("No health check response received from server at port " + port + ", destroying process for server. ");
    String result = destroyServer(port);

    if(result.startsWith("Successfully")){
      int newServerPort = 10000 + new Random().nextInt(10000);
      ServerLogger.log("Creating a new server at port " + newServerPort + " as a replacement for destroyed server " + port);

      Process serverProcess = ServerCreation.createServer(newServerPort);

      if(serverProcess != null && serverProcess.isAlive()){
        if (restService.getHealth(newServerPort).equals("OK")){
          addNewServerProcess(newServerPort, serverProcess);
          ServerLogger.log("Log: Successfully created a new server at port " + newServerPort);
        } else {
          ServerLogger.log("Error creating a server at port " + newServerPort);
        }
      } else {
        ServerLogger.log("Error creating a server at port " + newServerPort + ". Check logs");
      }
    }
  }
}
