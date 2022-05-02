package com.CS6650.CentralManagementService.controller;

import com.CS6650.CentralManagementService.ServerLogger;
import com.CS6650.CentralManagementService.ServerManager;
import com.CS6650.CentralManagementService.service.RestService;
import com.CS6650.CentralManagementService.utility.ServerCreation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@RestController
public class ServerController {

  @Autowired
  RestService restService;

  private ServerManager serverManager;

  @Autowired
  public ServerController(ServerManager serverManager) {
    this.serverManager = serverManager;
  }

  @GetMapping("/servers")
  public Set<Integer> getAllServers() {
    ServerLogger.log("Received request to get all servers");
    return serverManager.getAllActiveServerPorts();
  }

  @PostMapping("/createServer/{serverPort}")
  public String createServer(@PathVariable int serverPort) throws IOException {
    ServerLogger.log("Received request to create a new server");
    Process serverProcess = ServerCreation.createServer(serverPort);
    if (serverProcess != null && serverProcess.isAlive()) {
      if (restService.getHealth(serverPort).equals("OK")){
        serverManager.addNewServerProcess(serverPort, serverProcess);
        ServerLogger.log("Successfully created a new server at port " + serverPort);
        return "Return: Successfully created a new server at port " + serverPort;
      }
    }

    ServerLogger.log("Error creating a server at port " + serverPort + ". Check CMS logs for more info");
    return "Error creating a server at port " + serverPort + " Check CMS logs for more info";
  }

  @DeleteMapping("/deleteServer/{serverPort}")
  public String deleteServer(@PathVariable int serverPort) {
    ServerLogger.log("Received request to deleter  servers at port " + serverPort);
    return serverManager.destroyServer(serverPort);
  }
}
