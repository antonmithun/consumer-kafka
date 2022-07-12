package com.bidding.apps.biddingservice.config;

import com.bidding.apps.biddingservice.service.BidService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.KafkaBindingRebalanceListener;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Objects;

@Configuration
public class KafkaBindingListener implements KafkaBindingRebalanceListener {

    private final BidService bidService;

    @Autowired
    public KafkaBindingListener(BidService bidService) {
        this.bidService = bidService;
    }

    @Override
    public void onPartitionsAssigned(
            String bindingName, Consumer<?, ?> consumer,
            Collection<TopicPartition> partitions,
            boolean initial) {

        partitions.forEach(topicPartition -> {
            final Long offset = bidService.getLatestBidEventOffset(topicPartition.partition());
            if (Objects.nonNull(offset)) {
                consumer.seek(topicPartition, offset + 1);
            }
        });
    }
}
