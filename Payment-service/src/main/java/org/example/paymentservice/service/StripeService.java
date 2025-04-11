package org.example.paymentservice.service;

import com.example.saga.PaymentClientSecretEvent;
import com.example.saga.PaymentFailedEvent;
import com.example.saga.PaymentStartedEvent;
import com.example.saga.PaymentSuccessEvent;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeService {
    @Value("${stripe.api.secretKey}")
    private String secretKey;

    private final PaymentRepository repo;
    private final KafkaTemplate<String, Object> kafka;
    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    public StripeService(PaymentRepository repo, KafkaTemplate<String, Object> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "payment.started", groupId = "payment-group")
    public void handlePaymentStarted(PaymentStartedEvent event) {
        log.info("Получено событие PaymentStartedEvent: sagaId={}, amount={}, currency={}",
                event.getSagaId(), event.getAmount(), event.getCurrency());

        try {
            Stripe.apiKey = secretKey;
            log.debug("Stripe API инициализирован");

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) event.getAmount())
                    .setCurrency(event.getCurrency())
                    .putMetadata("sagaId", event.getSagaId())
                    .putMetadata("bookingId", String.valueOf(event.getBookingId()))
                    .putMetadata("userId", String.valueOf(event.getUserId()))
                    .putMetadata("resourceId", String.valueOf(event.getResourceId()))
                    .putMetadata("resourceType", event.getResourceType())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String clientSecret = paymentIntent.getClientSecret();
            log.info("PaymentIntent создан: id={}, clientSecret={}", paymentIntent.getId(), clientSecret);

            Payment payment = new Payment();
            payment.setBookingId(event.getBookingId());
            payment.setPaymentIntent(paymentIntent.getId());
            payment.setAmount(event.getAmount() / 100.0);
            payment.setStatus("PENDING");
            repo.save(payment);
            log.info("Платеж сохранён в БД: bookingId={}, status=PENDING", event.getBookingId());

            PaymentClientSecretEvent clientSecretEvent = new PaymentClientSecretEvent(
                    event.getSagaId(), clientSecret, event.getUserId(), event.getBookingId()
            );
            kafka.send("payment.client.secret", clientSecretEvent);
            log.info("Отправлен event payment.client.secret: sagaId={}, bookingId={} ",
                    event.getSagaId(), event.getBookingId());

        } catch (Exception e) {
            log.error("Ошибка при создании платежа для sagaId={}: {}",
                    event.getSagaId(), e.getMessage(), e);
            kafka.send("payment.failed",
                    new PaymentFailedEvent(event.getSagaId(), event.getBookingId(), e.getMessage()));
        }
    }

    public void onPaymentSucceeded(String paymentIntentId) throws StripeException {
        Payment payment = repo.findByPaymentIntent(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Платеж не найден"));
        payment.setStatus("PAID");
        repo.save(payment);
        String sagaId = findSagaIdFromStripe(paymentIntentId);
        kafka.send("payment.success", new PaymentSuccessEvent(sagaId, payment.getBookingId()));
        log.info("Платеж подтверждён: paymentIntent={}, sagaId={}, bookingId={}",
                paymentIntentId, sagaId, payment.getBookingId());
    }

    private String findSagaIdFromStripe(String paymentIntentId) throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        return intent.getMetadata().get("sagaId");
    }
}
