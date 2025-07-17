# Webcam Photo Capture Automation (Java + Selenium)

## Scenario Overview
This project demonstrates how to automate the process of taking a photo on [https://webcamtests.com/take-photo](https://webcamtests.com/take-photo) using Java and Selenium WebDriver in headless mode. The automation is designed to run on a server or CI environment where no physical camera hardware is available.

## Problem Statement
- **Goal:** Automate the following steps:
  1. Open the webcam test page.
  2. Allow camera permissions.
  3. Click the button to take a photo.
  4. Capture a screenshot after the photo is taken, ensuring the result is visible.
- **Challenge:** The server does not have a webcam device, and the test must run in headless mode (no GUI).

## Solution Approach
- **Selenium WebDriver** is used for browser automation.
- **Headless Chrome** is configured to run without a GUI.
- **Fake Webcam Feed:** Chrome is started with special arguments to simulate a webcam using a pre-recorded video file (`output.y4m`). This allows the page to "see" a webcam even when no hardware is present.
- **Camera Permissions:** Chrome is started with arguments to auto-allow camera access, so no manual permission is needed.
- **Screenshot:** After taking the photo, the script waits for the result to appear and scrolls it into view before capturing a screenshot.

## How It Works
1. **Start Chrome in Headless Mode:**
   - Uses `--headless=new` for headless operation.
   - Uses `--use-fake-device-for-media-stream` to simulate a webcam.
   - Uses `--use-file-for-fake-video-capture=output.y4m` to provide a video file as the webcam feed.
   - Uses `--use-fake-ui-for-media-stream` to auto-allow camera permissions.
2. **Navigate and Interact:**
   - Opens the webcam test page.
   - Clicks the button to launch the camera and then to take a photo.
   - Waits for the photo result to appear.
   - Scrolls the result into view and takes a screenshot.

## Prerequisites
- Java 17+
- Maven
- Chrome browser and ChromeDriver (ensure compatible versions)

## Project Structure
- `src/test/java/org/example/WebcamAutomationTest.java`: Main test automation code.
- `src/test/resources/output.y4m`: Fake webcam video file.
- `src/test/resources/capture_result.png`: Screenshot output after photo capture.

## How to Run
1. **Install dependencies:**
   ```sh
   mvn clean install
   ```
2. **Run the test:**
   ```sh
   mvn test
   ```
3. **View the screenshot:**
   - Check `src/test/resources/capture_result.png` for the result.

## Why Use .y4m Format Video?
The `.y4m` (YUV4MPEG2) format is required by Chrome's `--use-file-for-fake-video-capture` flag to simulate a webcam input. Chrome only accepts `.y4m` files for this purpose because it is a raw, uncompressed video format that is easy for the browser to decode and use as a fake camera stream. Other formats like `.mp4` or `.avi` are not supported for this flag.

## How to Download or Create a .y4m Video File

### Download Sample .y4m Video
You can download a sample `.y4m` video from the following sources:
- [Sample Y4M Video 1](https://media.xiph.org/video/derf/y4m/)
- [Big Buck Bunny Y4M](https://test-videos.co.uk/bigbuckbunny-y4m)

Or, you can create your own `.y4m` file as described below.

### Convert a Video to .y4m Format
You can use `ffmpeg` to convert any video or image to `.y4m` format. Install ffmpeg if you don't have it:

#### Windows
1. Download ffmpeg from [https://ffmpeg.org/download.html](https://ffmpeg.org/download.html)
2. Extract and add the `bin` folder to your PATH.
3. Open Command Prompt and use the commands below.

#### Linux (Debian/Ubuntu)
```sh
sudo apt-get update
sudo apt-get install ffmpeg
```

#### macOS
```sh
brew install ffmpeg
```

## Generate .y4m Video from a Single Photo
To create a `.y4m` video from a single image (e.g., `photo.jpg`):

```sh
ffmpeg -loop 1 -i photo.jpg -c:v yuv4mpegpipe -t 5 -vf scale=640:480 output.y4m
```
- `-loop 1` repeats the image
- `-t 5` sets the duration to 5 seconds
- `-vf scale=640:480` resizes to webcam resolution (adjust as needed)

## Generate .y4m Video from Multiple Photos (Slideshow)
To create a `.y4m` video from multiple images (e.g., `photo1.jpg`, `photo2.jpg`, ...):

1. Place all images in a folder and name them in order (e.g., `img001.jpg`, `img002.jpg`, ...).
2. Run:
```sh
ffmpeg -framerate 1 -i img%03d.jpg -c:v yuv4mpegpipe -vf scale=640:480 output.y4m
```
- `-framerate 1` shows each image for 1 second (adjust as needed)
- `img%03d.jpg` matches files like `img001.jpg`, `img002.jpg`, etc.

## Notes
- The generated `output.y4m` file can be used directly with Chrome's `--use-file-for-fake-video-capture` flag.
- Adjust the scale and duration as needed for your scenario.

## References
- [Selenium WebDriver](https://www.selenium.dev/)
- [Chrome Headless Mode](https://developers.google.com/web/updates/2017/04/headless-chrome)
- [WebcamTests.com](https://webcamtests.com/take-photo)

---
Feel free to modify the test or the fake video file as needed for your use case.
