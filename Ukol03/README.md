0) Vytvoření kontejneru s mavenem imagem
docker run -it -d -v "${PWD}:/maven" --name maven maven:3.9 /bin/bash

1) vygenerování jednoduchého programu Hello World
mvn archetype:generate -DgroupId=com.mvnlrn -DartifactId=mvnlrn -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

2) provedení kompilace a spuštění testů
mvn compile; mvn test

3) vytvoření kompilovaného package jar
mvn package

4) přidám plugin s exec a nastavím cestu na main
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <mainClass>com.mvnlrn.App</mainClass>
    </configuration>
</plugin>

5) spustím projekt
mvn exec:java