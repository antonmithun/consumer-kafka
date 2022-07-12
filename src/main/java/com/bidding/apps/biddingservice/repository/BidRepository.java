package com.bidding.apps.biddingservice.repository;

import com.bidding.apps.biddingservice.model.Bid;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BidRepository extends ElasticsearchRepository<Bid, String> {

}
