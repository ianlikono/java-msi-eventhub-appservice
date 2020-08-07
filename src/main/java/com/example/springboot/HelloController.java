package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;


import java.util.Random;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
	
	protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

	@RequestMapping("/")
	public String index() {
		
//		System.setProperty("AZURE_CLIENT_ID", "<<insert-service-principal-client-id>>");
//        System.setProperty("AZURE_CLIENT_SECRET", "<<insert-service-principal-client-application-secret>>");
//        System.setProperty("AZURE_TENANT_ID", "<<insert-service-principal-tenant-id>>");
		
		TokenCredential credential = new DefaultAzureCredentialBuilder()
	            .build();
		
		try {
			EventHubProducerClient producer = new EventHubClientBuilder()
		            .credential(
		                "eventhubjavatest.servicebus.windows.net",
		                "newone",
		                credential)
		            .buildProducerClient();
			
			EventDataBatch batch = producer.createBatch();
			for(int i = 0; i < 10000; i++) {
				batch.tryAdd(new EventData("First event" + getSaltString()));
			}

			producer.send(batch);
			producer.close();

			return "Running powered by Event Hub from Microsoft";
			
		}catch(Exception e) {
			return e.toString();
		}

		
	}

}
