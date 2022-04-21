package com.CS6650.CentralManagementService.controller;

import com.CS6650.CentralManagementService.model.Server;
import com.CS6650.CentralManagementService.model.UserMetadata;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class ServerController {

  @GetMapping("/servers")
  public Set<Server> getAllServers() {
    // TO DO: find all active servers that client will be able to access
    return new HashSet<>();
  }

  @PostMapping("/createServer")
  Server createServer(@RequestBody Server newServer) {
    // TO DO: Create a new server with up do date information
    return new Server();
  }

  @DeleteMapping("/deleteServer/{serverPort}")
  boolean registerUser(@PathVariable int serverPort) {
    return true;
  }
}
