package com.service.main.service.azure;

import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.main.service.MailService;
import com.service.main.service.azure.models.MailPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class AzureReceiver {
    private static final String connectionString = "Endpoint=sb://eproject4.servicebus.windows.net/;SharedAccessKeyName=queue001;SharedAccessKey=aJ+5rh+8R5bjLteqvXf+FCZnj3xhEFSX8+ASbGxRGUw=;EntityPath=queue001";
    private static final String queueName = "queue001";


    private static MailService _mailService;

    public AzureReceiver(MailService mailService) {
        this._mailService = mailService;
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void ReceiveMessages() throws InterruptedException {
        CountDownLatch countdownLatch = new CountDownLatch(1);
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(AzureReceiver::processMessage)
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        System.out.println("Starting the processor");
        processorClient.start();

        try {
            countdownLatch.await(); // Chờ cho đến khi countdownLatch.countDown() được gọi
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        } finally {
            System.out.println("Stopping and closing the processor");
            processorClient.close(); // Đóng processorClient
        }
    }

    private static void processMessage(ServiceBusReceivedMessageContext context) {
        try {
            ServiceBusReceivedMessage message = context.getMessage();
            System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                    message.getSequenceNumber(), message.getBody());
            MailPayload mailPayload = objectMapper.readValue(message.getBody().toString(), MailPayload.class);
            _mailService.sendMail(mailPayload.getFile(),mailPayload.getTo(),mailPayload.getCc(),mailPayload.getSubject(),mailPayload.getBody());
        } catch (JsonProcessingException e) {
             // Xử lý lỗi JsonProcessingException
            System.err.println("Something went wrong in processMessage");
        }
    }

    private static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {

    }
}
