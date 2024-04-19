package com.example.leetcodecheaterwebscrapper.controller;

import com.example.leetcodecheaterwebscrapper.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController {

    @Autowired
    private ScrapperService scrapperService;

    @GetMapping("/scrap")
    public String scrapUrl(@RequestParam String leetcodeUrl, @RequestParam int startPage, @RequestParam int endPage) throws InterruptedException {
        scrapperService.scrapUrl(leetcodeUrl, startPage, endPage);
        return "Scrapping Started.";
    }
}
