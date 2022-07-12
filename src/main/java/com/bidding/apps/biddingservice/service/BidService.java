package com.bidding.apps.biddingservice.service;

import com.bidding.apps.biddingservice.dto.BidEvent;
import com.bidding.apps.biddingservice.model.Bid;
import com.bidding.apps.biddingservice.repository.BidRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidService.class);
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public Bid saveBid(Long offset) {
        Bid bid = new Bid();
        bid.setProductId(UUID.randomUUID().toString());
        bid.setUserId(UUID.randomUUID().toString());
        bid.setOffset(offset);
        return bidRepository.save(bid);
    }

    public Bid get(String id) {
        return bidRepository.findById(id)
                .orElse(null);
    }

    public void process(BidEvent bidEvent, Long offset, Integer partitionId) {
        Bid bid = bidRepository.save(new Bid(offset, partitionId, bidEvent.getProductId(), bidEvent.getUserId()));
        LOGGER.info("Bid for Product: {} saved. id : {}", bid.getProductId(), bid.getId());
    }

    public Long getLatestBidEventOffset(int partitionId) {
        return elasticsearchOperations.search(
                buildQueryForFindingLatestMessageOffset(partitionId), Bid.class).getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .map(Bid::getOffset)
                .findAny()
                .orElse(null);
    }

    private Long convertOffset(String offset) {
        try {
            return Long.valueOf(offset);
        } catch (Exception ex) {
            LOGGER.error("Error converting offset to Long : {}", offset);
        }
        return null;
    }

    private Query buildQueryForFindingLatestMessageOffset(int partitionId) {
        return new NativeSearchQueryBuilder()
                .withSorts(SortBuilders.fieldSort("offset").order(SortOrder.DESC))
                .withPageable(Pageable.ofSize(1))
                .withQuery(QueryBuilders.matchAllQuery())
                .withQuery(QueryBuilders.matchQuery("partitionId", partitionId))
                .build();
    }
}
