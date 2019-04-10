# pitest-stryker-report-plugin
A pitest plugin that maps pitest results to stryker's mutation-testing-elements

This is a WIP that is not yet published. 

### How to use
Download the repo and install locally with `mvn install`.

Then add the jar as dependency to the pitest plugin in pom.xml like so:
  ```
  <plugin>
      <groupId>org.pitest</groupId>
      <artifactId>pitest-maven</artifactId>
      <version>1.4.7</version>
      <!-- This stryker plugin -->
      <dependencies>
          <dependency>
              <groupId>org.pitest.plugins</groupId>
              <artifactId>stryker-plugin</artifactId>
              <version>0.1.0</version>
          </dependency>
      </dependencies>
      <!-- Tell pitest to use this plugin as output -->
      <configuration>
          <outputFormats>
              <format>STRYKER</format>
          </outputFormats>
      </configuration>
  </plugin>
```
