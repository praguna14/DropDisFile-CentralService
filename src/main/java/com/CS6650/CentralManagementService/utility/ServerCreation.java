package com.CS6650.CentralManagementService.utility;

import com.CS6650.CentralManagementService.ServerLogger;
import com.CS6650.CentralManagementService.service.RestService;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

public class ServerCreation {
  private static String SERVER_JAR_PATH = "/Users/prajakta/Desktop/DSjars/Server-0.0.1-SNAPSHOT.jar";

  public static Process createServer(int serverPort){
    Process proc = null;
    try{
      proc = Runtime.getRuntime().exec("java -jar -Dserver.port=" +serverPort+ " " + SERVER_JAR_PATH);
      Thread.sleep(12000); //wait for server jar to start running

      String errorMsg = getErrorInProcess(proc);

      if(!errorMsg.isEmpty()){ // error starting server
        ServerLogger.log("Error creating a server at port " + serverPort + " error: " + errorMsg);
        proc.destroy();
        return null;
      } else{
        return proc;
      }
    } catch (IOException | InterruptedException e){
      if(proc != null){
        proc.destroy();
      }
      ServerLogger.log("Error creating a server at port " + serverPort + " error: " + e.getMessage());
      return null;
    }
  }

  private static String getErrorInProcess(Process proc) throws IOException {
//    InputStream in = proc.getInputStream();
//    byte logs[]=new byte[in.available()];
//    in.read(logs,0,logs.length);
//    String serverJarLogs = new String(logs);

    InputStream err = proc.getErrorStream();

    byte[] error =new byte[err.available()];
    err.read(error,0,error.length);
    return new String(error);
  }
}
