package org.example;

import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class WebcamAutomationTest {
    @Test
    public void testWebcamPhotoCapture() throws InterruptedException, IOException {
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        // If chromedriver is in your PATH, no need to set the property. Otherwise, set the correct path below:
        String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
        if (chromeDriverPath != null && !chromeDriverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--use-fake-ui-for-media-stream");
        // Use a .y4m video file as the fake webcam feed
        String y4mPath = System.getProperty("user.dir") + "/src/test/resources/output.y4m";
        options.addArguments("--use-file-for-fake-video-capture=" + y4mPath);

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://webcamtests.com/take-photo");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement launchCameraButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("webcam-launcher"))
            );
            launchCameraButton.click();
            Thread.sleep(3000);

            WebElement takePhotoButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-action='takePhoto']"))
            );
            takePhotoButton.click();
            // Wait for the #webcam-snapshots element to be visible (contains the captured photo)
            WebElement snapshotsElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='webcam-snapshots']"))
            );
            // Scroll the element into view so it is fully visible before taking screenshot
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'end', inline: 'nearest'});", snapshotsElement
            );
            Thread.sleep(1000); // Optional: wait for scroll animation to finish

            // Take a screenshot for validation (visible viewport only)
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = System.getProperty("user.dir") + "/src/test/resources/capture_result.png";
            Files.copy(screenshot.toPath(), new File(screenshotPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            driver.quit();
        }
    }
}
