[![Maven Central](https://img.shields.io/maven-central/v/io.github.wmaarts/pitest-mutation-testing-elements-plugin.svg?color=brightgreen&label=Maven%20Central)](https://search.maven.org/artifact/io.github.wmaarts/pitest-mutation-testing-elements-plugin)
[![Build status](https://github.com/wmaarts/pitest-mutation-testing-elements-plugin/workflows/CI/badge.svg)](https://github.com/wmaarts/pitest-mutation-testing-elements-plugin/actions)

# pitest-mutation-testing-elements-plugin
A pitest plugin that maps [pitest](https://github.com/hcoles/pitest) results to [stryker's mutation-testing-elements](https://github.com/stryker-mutator/mutation-testing-elements/tree/master/packages/mutation-testing-elements#mutation-testing-elements).

### How to use 💁
* Add the dependency to the pitest maven plugin 
* Configure the outputFormat to be `HTML2` 

Like so:
  ```xml
    <plugin>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-maven</artifactId>
        <version>1.4.10</version>
        <dependencies>
            <dependency>
                <groupId>io.github.wmaarts</groupId>
                <artifactId>pitest-mutation-testing-elements-plugin</artifactId>
                <version>${pitest-mutation-testing-elements-plugin.version}</version>
            </dependency>
        </dependencies>
        <configuration>
            <outputFormats>
                <format>HTML2</format>
            </outputFormats>
        </configuration>
    </plugin>
```

Run [like you would normally run pitest](http://pitest.org/quickstart/maven/): ▶

```shell
mvn org.pitest:pitest-maven:mutationCoverage
```

## Preview 🔮

This is a result of pitest run on this plugin. (More previews [here](https://github.com/stryker-mutator/mutation-testing-elements/tree/master/packages/mutation-testing-elements#mutation-testing-elements))
![preview](https://i.imgur.com/tKp346S.png)
