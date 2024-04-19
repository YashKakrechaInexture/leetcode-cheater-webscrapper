package com.example.leetcodecheaterwebscrapper.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
public class WebScrapperServiceImpl implements WebScrapperService {

    @Override
    public WebDriver startBrowser() {
        return new ChromeDriver();
    }

    @Override
    public WebDriver startBrowser(String url) {
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        return driver;
    }

    @Override
    public void closeBrowser(WebDriver driver) {
        driver.quit();
    }
}
