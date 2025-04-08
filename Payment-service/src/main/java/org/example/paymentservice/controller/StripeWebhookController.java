package org.example.paymentservice.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.example.paymentservice.service.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {
    private final StripeService stripeService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        try {
            String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            String sigHeader = request.getHeader("Stripe-Signature");

            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (intent != null) {
                    stripeService.onPaymentSucceeded(intent.getId());
                }
            }

            return ResponseEntity.ok("✅ Webhook обработан");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Ошибка webhook: " + e.getMessage());
        }
    }
}
