package com.CS6650.CentralManagementService.controller;

import com.CS6650.CentralManagementService.ServerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@RestController
public class ServerController {
  private static String SERVER_JAR_PATH = "/Users/prajakta/Desktop/DSjars/Server-0.0.1-SNAPSHOT.jar";

  private ServerManager serverManager;

  @Autowired
  public ServerController(ServerManager serverManager) {
    this.serverManager = serverManager;
  }

  @GetMapping("/servers")
  public Set<Integer> getAllServers() {
    return serverManager.getAllActiveServerPorts();
  }

  @PostMapping("/createServer/{serverPort}")
  public String createServer(@PathVariable int serverPort) throws IOException {
    try{
      Process proc = Runtime.getRuntime().exec("java -jar -Dserver.port=" +serverPort+ " " + SERVER_JAR_PATH);
      Thread.sleep(12000); //wait for server jar to start running

      InputStream in = proc.getInputStream();
      InputStream err = proc.getErrorStream();

      byte logs[]=new byte[in.available()];
      in.read(logs,0,logs.length);
      String serverJarLogs = new String(logs);

      byte error[]=new byte[err.available()];
      err.read(error,0,error.length);
      String errorMsg = new String(error);

      if(errorMsg.isEmpty() && !serverJarLogs.isEmpty()){ //server started successfully
        serverManager.addNewServerProcess(serverPort, proc);
        return "Successfully created a new server at port " + serverPort;
      } else { //Error creating server
        return "Error creating a server at port " + serverPort + " error: " + serverJarLogs + "/n/n" + errorMsg;
      }
    } catch (IOException | InterruptedException e){
      return "Error creating a server at port " + serverPort + " error: " + e.getMessage();
    }
  }

  @DeleteMapping("/deleteServer/{serverPort}")
  public String deleteServer(@PathVariable int serverPort) {
    return serverManager.destroyServer(serverPort);
  }
}
