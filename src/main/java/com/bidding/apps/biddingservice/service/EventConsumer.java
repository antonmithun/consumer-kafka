package com.bidding.apps.biddingservice.service;

import com.bidding.apps.biddingservice.dto.BidEvent;
import org.springframework.messaging.Message;

public interface EventConsumer {

    void processBiddingEvent(Message<BidEvent> message);
}