package com.hveiga.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

/*
 * AWSKillInstance Class
 * @author Hector Veiga - hveiga@hawk.iit.edu
 * 
 * I assume you have AWS SDK for Java and credentials configured in AwsCredentials.properties.
 */
public class AWSKillInstance {
	
    private static String instanceId = "";
    static AmazonEC2      ec2;
    
    
    private static void init() throws Exception {
        AWSCredentials credentials = new PropertiesCredentials(
                AwsConsoleApp.class.getResourceAsStream("AwsCredentials.properties"));

        ec2 = new AmazonEC2Client(credentials);
    }

	public static void main(String[] args) throws Exception {
		// This code should be executed inside an Amazon EC2 instance
		init();
		killMePlease();
	}
	
    public static void killMePlease() {
        try {
        	String wget = "sudo wget -q -O - http://169.254.169.254/latest/meta-data/instance-id";
            Process p = Runtime.getRuntime().exec(wget);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            instanceId = br.readLine();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(instanceId.equals("")) return;
        
        List<String> instancesToTerminate = new ArrayList<String>();
        instancesToTerminate.add(instanceId);   
        TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest();
        terminateRequest.setInstanceIds(instancesToTerminate);
        ec2.terminateInstances(terminateRequest);
    }

}
