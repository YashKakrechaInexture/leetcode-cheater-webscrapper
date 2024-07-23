package com.example.leetcodecheaterwebscrapper.service;

import com.example.leetcodecheaterwebscrapper.models.Submission;
import com.example.leetcodecheaterwebscrapper.models.UserSubmission;
import com.example.leetcodecheaterwebscrapper.repository.SubmissionRepository;
import com.example.leetcodecheaterwebscrapper.repository.UserSubmissionRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        scrapAllPage(leetcodeUrl, startPage, endPage);
    }

    private void scrapAllPage(String leetcodeUrl, int startPage, int endPage) throws InterruptedException {
        int numberOfAvailableThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfAvailableThreads);
        try {
            for (int page = startPage; page <= endPage; page++) {
                int currPage = page;
                executorService.submit(() -> {
                    try {
                        scrapPage(leetcodeUrl, currPage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (Exception e){
            logger.error("Error occurred while scraping all pages", e);
        } finally {
            if (!executorService.isShutdown()) {
                executorService.shutdownNow(); // Force shutdown if necessary
            }
        }
    }

    private void scrapPage(String url, int page) throws InterruptedException {
        WebDriver driver = webScrapperService.startBrowser();
        driver.get(url + "/" + page);
        TimeUnit.SECONDS.sleep(2);
        WebElement table =  driver.findElement(By.tagName("table"));
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> usersRows = tbody.findElements(By.tagName("tr"));
        for(WebElement userRow : usersRows){
            try{
                Actions actions = new Actions(driver);
                actions.moveToElement(userRow);
                actions.perform();
                TimeUnit.SECONDS.sleep(2);
                List<WebElement> tds = userRow.findElements(By.tagName("td"));
                int rank = Integer.parseInt(tds.get(0).getText());
                String username = tds.get(1).findElement(By.tagName("a")).getText();
                int score = Integer.parseInt(tds.get(2).getText());
                String finishTime = tds.get(3).getText();

                Submission submission1 = getSubmission(getCodeFromWebElement(tds.get(4), driver), "Q1");
                Submission submission2 = getSubmission(getCodeFromWebElement(tds.get(5), driver), "Q2");
                Submission submission3 = getSubmission(getCodeFromWebElement(tds.get(6), driver), "Q3");
                Submission submission4 = getSubmission(getCodeFromWebElement(tds.get(7), driver), "Q4");

                UserSubmission userSubmission = new UserSubmission();
                userSubmission.setPage(page);
                userSubmission.setUserRank(rank);
                userSubmission.setUsername(username);
                userSubmission.setScore(score);
                userSubmission.setFinishTime(finishTime);
                userSubmission.setQuestionOneSubmission(submission1);
                userSubmission.setQuestionTwoSubmission(submission2);
                userSubmission.setQuestionThreeSubmission(submission3);
                userSubmission.setQuestionFourSubmission(submission4);

                saveUserSubmission(userSubmission);
            }catch(Exception e){
                logger.error("Error while scraping page '"+url+"' : ", e);
            }
        }
        webScrapperService.closeBrowser(driver);
        System.out.println("-----------------------------------------------------------");
    }

    private String getCodeFromWebElement(WebElement element, WebDriver driver) throws InterruptedException {
        String code = "";
        try{
            TimeUnit.SECONDS.sleep(2);
            try{
                element.findElement(By.tagName("a")).click();
            }catch(NoSuchElementException e){
                // user has not submitted the code
                return code;
            }catch(Exception e){
                throw e;
            }
            TimeUnit.SECONDS.sleep(3);
            WebElement modalContent = driver.findElement(By.className("modal-content"));
            TimeUnit.SECONDS.sleep(3);
            WebElement codeElement = modalContent.findElement(By.tagName("pre"));
            code = codeElement.getText();
            WebElement modalHeader = modalContent.findElement(By.className("modal-header"));
            WebElement closeButton = modalHeader.findElement(By.tagName("button"));
            closeButton.click();
        }catch(Exception e){
            logger.error("Error occurred while getting code from web element : ", e);
        }
        return code;
    }

    private Submission getSubmission(String code, String questionNumber) {
        Submission submission = new Submission();
        submission.setCode(code);
        submission.setCodeHash(hashString(code));
        submission.setQuestionNumber(questionNumber);
        return submission;
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }
    }

    private synchronized void saveUserSubmission(UserSubmission userSubmission) {
        userSubmissionRepository.save(userSubmission);
    }
}
