package com.CS6650.CentralManagementService;

import com.CS6650.CentralManagementService.controller.ServerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
  static int[] serverPortsOnStartup = {5051,5052,5053};
  long healthCheckPeriod = 1000L * 5;
  long healthCheckInitialDelay = 1000L * 15;

  @Autowired
  ServerController serverController;

  @Autowired
  ServerManager serverManager;

  public void onApplicationEvent(ContextRefreshedEvent event) {
    ServerLogger.log("Starting default servers on ports " + Arrays.toString(serverPortsOnStartup));
    for(Integer serverPort : serverPortsOnStartup){
      startDefaultServer(serverPort);
    }

    selfStabilizeUponServerFailure();
  }

  private void startDefaultServer(Integer serverPort) {
    Thread startServerThread = new Thread("Start thread for server on port " + serverPort) {
      public synchronized void run(){
        try {
          serverController.createServer(serverPort);
        } catch (IOException e) {
          ServerLogger.log(String.format("Start Server %d failed due to : %s", serverPort, e.getMessage()));
        }
      }
    };
    startServerThread.start();
  }

  private void selfStabilizeUponServerFailure() {
    TimerTask repeatedHealthCheckTask = new TimerTask() {
      public void run() {
        serverManager.refreshActiveServers();
      }
    };
    Timer timer = new Timer("Timer");
    timer.scheduleAtFixedRate(repeatedHealthCheckTask, healthCheckInitialDelay, healthCheckPeriod);
  }
}
