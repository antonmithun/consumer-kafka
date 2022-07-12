package com.bidding.apps.biddingservice.service.impl;

import com.bidding.apps.biddingservice.dto.BidEvent;
import com.bidding.apps.biddingservice.service.BidService;
import com.bidding.apps.biddingservice.service.EventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class KafkaEventConsumer implements EventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventConsumer.class);
    private static final String KAFKA_OFFSET_KEY = "kafka_offset";
    private static final String KAFKA_PARTITION_ID = "kafka_receivedPartitionId";

    private final BidService bidService;

    @Autowired
    public KafkaEventConsumer(BidService bidService) {
        this.bidService = bidService;
    }

    @Override
    public void processBiddingEvent(Message<BidEvent> message) {
        LOGGER.info("Received in Func Consumer : {}", message.getPayload());
        LOGGER.info("Kafka offset :: {}",  message.getHeaders().get(KAFKA_OFFSET_KEY));

        final Long offset = Objects.nonNull(message.getHeaders().get(KAFKA_OFFSET_KEY)) ?
                message.getHeaders().get(KAFKA_OFFSET_KEY, Long.class) : null;

        final Integer partitionId = Objects.nonNull(message.getHeaders().get(KAFKA_PARTITION_ID)) ?
                message.getHeaders().get(KAFKA_PARTITION_ID, Integer.class) : null;

        bidService.process(message.getPayload(), offset, partitionId);

        message.getHeaders().forEach((key, value) -> LOGGER.info("Key : {}, value : {}", key , value));

        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        LOGGER.info("Acknowledgement : {}",  acknowledgment);
        if (acknowledgment != null) {
            LOGGER.info("Acknowledgment provided");
            acknowledgment.acknowledge();
        }
    }
}
