package com.CS6650.CentralManagementService.controller;

import com.CS6650.CentralManagementService.model.Server;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@RestController
public class ServerController {
  private static String SERVER_JAR_PATH = "/Users/prajakta/Desktop/DSjars/Server-0.0.1-SNAPSHOT.jar";

  @GetMapping("/servers")
  public Set<Server> getAllServers() {
    // TO DO: find all active servers that client will be able to access
    return new HashSet<>();
  }

  @PostMapping("/createServer/{serverPort}")
  String createServer(@PathVariable int serverPort) throws IOException {
    try{
      Process proc = Runtime.getRuntime().exec("java -jar -Dserver.port=" +serverPort+ " " + SERVER_JAR_PATH);
      InputStream in = proc.getInputStream();
      InputStream err = proc.getErrorStream();

      byte logs[]=new byte[in.available()];
      in.read(logs,0,logs.length);
      String serverJarLogs = new String(logs);

      byte error[]=new byte[err.available()];
      err.read(error,0,error.length);
      String errorMsg = new String(error);

      if(errorMsg.isEmpty()){ //server started successfully
        return "Successfully created a new server at port " + serverPort;
      } else { //Error creating server
        return "Error creating a server at port " + serverPort + " error: " + serverJarLogs + "/n/n" + errorMsg;
      }
    } catch (IOException e){
      return "Error creating a server at port " + serverPort + " error: " + e.getMessage();
    }
  }

  @DeleteMapping("/deleteServer/{serverPort}")
  boolean deleteServer(@PathVariable int serverPort) {
    //checks whether process for this server is still alive
    //if alive, destroy
    //else send message
    return true;
  }
}
