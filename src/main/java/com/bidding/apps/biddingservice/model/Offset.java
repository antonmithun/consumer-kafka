package com.bidding.apps.biddingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "topic_offset")
public class Offset {

    @Id
    private String id;

    @Field
    private Long Offset;

}
