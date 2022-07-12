package com.bidding.apps.biddingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "bid")
@Data
public class Bid {
    @Id
    private String id;

    @Field
    private Long offset;
    @Field
    private Integer partitionId;

    @Field
    private String productId;
    @Field
    private String userId;

    public Bid() {
    }

    public Bid(Long offset, Integer partitionId, String productId, String userId) {
        this.offset = offset;
        this.partitionId = partitionId;
        this.productId = productId;
        this.userId = userId;
    }

}
