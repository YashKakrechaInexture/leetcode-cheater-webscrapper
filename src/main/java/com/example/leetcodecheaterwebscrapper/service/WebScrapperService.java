package com.example.leetcodecheaterwebscrapper.service;

import org.openqa.selenium.WebDriver;

public interface WebScrapperService {

    WebDriver startBrowser();

    WebDriver startBrowser(String url);

    void closeBrowser(WebDriver driver);
}
