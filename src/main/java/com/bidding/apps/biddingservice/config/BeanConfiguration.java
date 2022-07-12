package com.bidding.apps.biddingservice.config;

import com.bidding.apps.biddingservice.dto.BidEvent;
import com.bidding.apps.biddingservice.service.EventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class BeanConfiguration {

    private final EventConsumer eventConsumer;

    @Autowired
    public BeanConfiguration(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Bean
    public Consumer<Message<BidEvent>> receive() {
        return eventConsumer::processBiddingEvent;
    }
}
