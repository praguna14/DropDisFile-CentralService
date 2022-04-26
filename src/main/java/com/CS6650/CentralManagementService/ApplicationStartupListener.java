package com.CS6650.CentralManagementService;

import com.CS6650.CentralManagementService.controller.ServerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
  static int[] serverPortsOnStartup = {9091,9092,9093};

  @Autowired
  ServerController serverController;

  public void onApplicationEvent(ContextRefreshedEvent event) {
    ServerLogger.init(String.valueOf(8081), "CentralManagementServerLog");

    for(Integer serverPort : serverPortsOnStartup){

      Thread startServerThread = new Thread("Start thread for server on port " + serverPort) {
        public synchronized void run(){
          try {
            String resultLog = serverController.createServer(serverPort);
            ServerLogger.log(resultLog);
          } catch (IOException e) {
            ServerLogger.log(String.format("Start Server %d failed due to : %s", serverPort, e.getMessage()));
          }
        }
      };
      startServerThread.start();
    }
  }
}
