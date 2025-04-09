package org.example.bookingservice.controller;

import com.example.saga.PaymentClientSecretEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "payment.client.secret", groupId = "booking-group")
    public void handleClientSecret(PaymentClientSecretEvent event) {
        log.info("📨 Получен PaymentClientSecretEvent: userId={}, clientSecret={}",
                event.getUserId(), event.getClientSecret());

        // Отправка clientSecret на фронт через WebSocket по адресу /topic/payment/{userId}
        messagingTemplate.convertAndSend("/topic/payment/" + event.getUserId(), event.getClientSecret());
    }
}
