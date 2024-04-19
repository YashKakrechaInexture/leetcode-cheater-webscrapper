package com.example.leetcodecheaterwebscrapper.service;

public interface ScrapperService {
    void scrapUrl(String leetcodeUrl, int startPage, int endPage) throws InterruptedException;
}
