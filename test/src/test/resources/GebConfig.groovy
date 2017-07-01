

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

waiting {
  timeout = 2
}

environments {

  // run via “./gradlew chromeTest”
  // See: http://code.google.com/p/selenium/wiki/ChromeDriver
  chrome {
    driver = { new ChromeDriver() }
  }

  // run via “./gradlew firefoxTest”
  // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
  firefox {
    atCheckWaiting = 1

    driver = { new FirefoxDriver() }
  }

}

// To run the tests with all browsers just run “./gradlew test”

baseUrl = "http:/virkr.dk"
