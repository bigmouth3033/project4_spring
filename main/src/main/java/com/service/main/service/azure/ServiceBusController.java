package com.service.main.service.azure;

import com.service.main.service.azure.models.MailPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("testServiceBus")
public class ServiceBusController {
    @Autowired
    private AzureSender _azureSender;

    @PostMapping("send")
    public void sendMessage() {
        MailPayload mailPayload = new MailPayload();
        mailPayload.setFile(null);
        mailPayload.setTo("dumplings031102@gmail.com");
        String[] cc = {"dumplings031102@gmail.com"};
        mailPayload.setCc(cc);
        mailPayload.setSubject("Test Model");
        mailPayload.setBody("Hello");
        _azureSender.sendMessage(mailPayload);
    }
}
