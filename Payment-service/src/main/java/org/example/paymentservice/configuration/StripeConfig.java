package org.example.paymentservice.configuration;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.api.secretKey}")
    public void setApiKey(String secretKey){
        Stripe.apiKey = secretKey;
    }
}
