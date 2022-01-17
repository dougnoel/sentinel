package com.dougnoel.sentinel.systems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

//TODO: Get this working or remove it.
public class OperatingSystemInteractions {

    private OperatingSystemInteractions(){}
    
    public static ExecutionResult executeCommand(String command) {
    	ExecutionResult result = new ExecutionResult();

    	ProcessBuilder pb = new ProcessBuilder(command);
    	pb.redirectErrorStream(true);
    	
    	Process process;
		try {
			process = pb.start();
	    	BufferedReader inStreamReader = new BufferedReader(
	    	    new InputStreamReader(process.getInputStream())); 
	
	    	String outputStream;
	    	while((outputStream = inStreamReader.readLine()) != null){
	    		result.appendMessage(outputStream);
	    	}
	
	    	result.success(process.waitFor(5, TimeUnit.MINUTES));
	    	if (result.getSuccess() && process.exitValue() == 1){
	    		result.success(false);
	    	}
    	
		}
		catch (IOException | InterruptedException e) {
			if(e instanceof InterruptedException)
				Thread.currentThread().interrupt();
			result.success(false);
			result.appendMessage(e.toString());
		}

    	return result;
    }
    
    public static void installChocolatey() {
		ExecutionResult result = executeCommand("@\"%SystemRoot%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe\" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command \"[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))\" && SET \"PATH=%PATH%;%ALLUSERSPROFILE%\\chocolatey\\bin\"");
		
		if (!result.getSuccess())
			throw new com.dougnoel.sentinel.exceptions.IOException(result.getMessage());
    }
}
