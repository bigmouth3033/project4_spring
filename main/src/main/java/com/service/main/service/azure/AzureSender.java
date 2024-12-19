package com.service.main.service.azure;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.service.main.service.azure.models.MailPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class AzureSender {
    private final ObjectMapper objectMapper = new ObjectMapper();
    static String queueName = "queue001";

    @Value("${azure.connectionString}")
    private String connectionString;

    public void sendMessage(MailPayload payload) {
         try {


        ServiceBusSenderClient serviceBusSenderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        String jsonString = objectMapper.writeValueAsString(payload);

        serviceBusSenderClient.sendMessage(new ServiceBusMessage(jsonString));
        System.out.println("Message sent");
         } catch (Exception e) {
             throw new RuntimeException("Some thing wrong in azure sender");
         }
    }
}
