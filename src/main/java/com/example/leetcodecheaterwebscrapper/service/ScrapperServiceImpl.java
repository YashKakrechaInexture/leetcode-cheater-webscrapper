package com.example.leetcodecheaterwebscrapper.service;

import com.example.leetcodecheaterwebscrapper.repository.SubmissionRepository;
import com.example.leetcodecheaterwebscrapper.repository.UserSubmissionRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ScrapperServiceImpl implements ScrapperService {

    private static final Logger logger = LoggerFactory.getLogger(ScrapperServiceImpl.class);

    @Autowired
    private WebScrapperService webScrapperService;

    @Autowired
    private UserSubmissionRepository userSubmissionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public void scrapUrl(String leetcodeUrl, int startPage, int endPage) throws InterruptedException {
        logger.info("Scraping URL: {}", leetcodeUrl);
        WebDriver driver = webScrapperService.startBrowser();
        scrapAllPage(leetcodeUrl, startPage, endPage, driver);
//        TimeUnit.SECONDS.sleep(5);
        webScrapperService.closeBrowser(driver);
    }

    private void scrapAllPage(String leetcodeUrl, int startPage, int endPage, WebDriver driver) throws InterruptedException {
        for(int page=startPage; page<=endPage; page++){
            scrapPage(leetcodeUrl+"/"+page, driver);
//            TimeUnit.SECONDS.sleep(5);
        }
    }

    private void scrapPage(String url, WebDriver driver) throws InterruptedException {
        driver.get(url);
        TimeUnit.SECONDS.sleep(1);
        WebElement table =  driver.findElement(By.tagName("table"));
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> usersRows = tbody.findElements(By.tagName("tr"));
        for(WebElement userRow : usersRows){
            Actions actions = new Actions(driver);
            actions.moveToElement(userRow);
            actions.perform();
            List<WebElement> tds = userRow.findElements(By.tagName("td"));
            int rank = Integer.parseInt(tds.get(0).getText());
            String username = tds.get(1).findElement(By.tagName("a")).getText();
            int score = Integer.parseInt(tds.get(2).getText());
            String finishTime = tds.get(3).getText();

            String q1Code = getCodeFromWebElement(tds.get(4), driver);
            String q2Code = getCodeFromWebElement(tds.get(5), driver);
            String q3Code = getCodeFromWebElement(tds.get(6), driver);
            String q4Code = getCodeFromWebElement(tds.get(7), driver);
        }
        System.out.println("-----------------------------------------------------------");
    }

    private String getCodeFromWebElement(WebElement element, WebDriver driver) throws InterruptedException {
        String code = "";
        element.findElement(By.tagName("a")).click();
        TimeUnit.SECONDS.sleep(1);
        WebElement modalContent = driver.findElement(By.className("modal-content"));
        WebElement codeElement = modalContent.findElement(By.tagName("pre"));
        code = codeElement.getText();
        WebElement modalHeader = modalContent.findElement(By.className("modal-header"));
        WebElement closeButton = modalHeader.findElement(By.tagName("button"));
        closeButton.click();
        return code;
    }
}
