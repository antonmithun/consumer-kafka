package com.bidding.apps.biddingservice.controller;

import com.bidding.apps.biddingservice.model.Bid;
import com.bidding.apps.biddingservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/bid")
public class BidController {
    @Autowired
    BidService bidService;

    @PostMapping
    public Bid save(@RequestParam Long offset) {
        return bidService.saveBid(offset);
    }

    @GetMapping
    public Bid get(@RequestParam String id) {
        return bidService.get(id);
    }

    @GetMapping("/offset")
    public Long getLargestOffset(int partitionId) {
        return bidService.getLatestBidEventOffset(partitionId);
    }
}
