# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2: Server Design

[Server Design Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnl3kuwowGKEpujKcplu8So3gFDpJr6zpdhu7raDA0AwD2faYCqwYam6mUQMwABmvgShlak7EYEBqGgADkzDHGAIDxNu7kWf6zLTJe0BIAAXigHBRjGcaFFpCXwMgqYwOmACMBE5qoebzNBRYlvUPjdXqvUDbsdFNm5grDvy9SHnIKDPvE56Xtex2LpUy4BmuAY3Vuh0TTppYOeKGSqABmCfSB1S6YR5bEaR3wUVR9YkahDbjcmU3YTAuH4aMoPReDcOwZe0PIbDtGNgxnjeH4-heCg6AxHEiQU1TDm+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDFDiHoOh8KVJ99Qi0hANmRhFlWTZ9iM-ZQmM05aguW1R23idMCMmAl3XfBotoP5vKBQ9wWhU+r3yLK8oy2Ld0ChNbn1JdMoZVl-a5eqxq42bhUlWVDHvR1pZdRRO2DcNKCxgp4uYUjYBpk483o4ty0FmMa3QBtW3xLHe1EzrHmW-rHAoNwx6XiblFmxbC5u9b9QZDMEA0C9L5vUl7ZSwJ6unr9-2A+7wNgZpk8pyUaco3hWb7cTTFkyi67+Ng4oavxaIwAA4kqGjM5HbP79zfP2EqwtB7L42S-LpbO4U4+JZ29TIDkh85vZaLf2omsSTlypHrekBsmTG2fs3fc94bbijtr3B2kVn6xUrouRWLoe5XjSj7XsftVQamfiHGApVNrh37hPCSUci4l3joneMCNKiiXTpnbM-Ic6rWLAXBUNCoD9UGg2eiwCZCgNHNZP+R8MTQKtkKeottKwIAPkqD0rsgbv3ETkdKxUlFzFfBQj8j96i7y-kfUeCBAKPzUTUF4Ywr45gLA0FwTjOjTyobPaaqMCJ2LUA4pxLgXFlxJsxfwHAADsbgnAoCcDECMwQ4BcQAGzwAnIYf+RRU4s0nmzVoHRL7XyLnjLM3iABySpXGJgfrCf0z81gjBKWUuWVSJYdkspgs66J-4YjgCk-+gDtbvRAXFMBhtIG33QNI+KbcQrwKwRFJ2YzCiqMoa05KXscGwF9jlAhgdTZIWIaQg6+i37WMlLw-hQ1owJ1GsnCazCZoZwWuw-MnD1o8Jjnw3agjDmdl1kMsR7SUCdPqXMCZ91ZFPXXP-FRoirFWW8QASWkOXAxTT6jdKPICpUZiLFNKsSDBF0gCyzXCMEQI5SFaIznjhRe6MCVEpJWSwJq8AiWBrjZTY1MkAJDAKyvsEAOUACkIDih0YYfwyRQBqnSXPTJVDsnMhkj0bxN9dnoCzNgBAwBWVQDgBAGyUA1gEvJc0weNSYAjA1Vqygur9WGqVIi0yuKMHJQAFbCrQJ0oV4pekoEJFrVyFDBloLESM+uUDUEt1gXImZaykHzNVYsmFyyrKxuAOlDZeCtl5R2Y3PZaAiokLDsi45nUzm7Todcxhk0qX3NYWMbOzzCxcNLJtd55yvnkJ+RXFu3kwBAvtdIUFrdwXyKhWlSKBKI0wOTZgsdDtcHZX9hqbx+yi0RxLaWAAQiGPpFak5VruemNGbDcyNrzs2+oeh1woj9TkDtWaA43ucoNGAIA9UZRNGaGs4YxpHJaf6QMX6wxIT3QwmetzU7p0zFnJ5K0m2vM-cGc0MBaz1mXsI12G1sBaA6UqDE3jbpJqmcybD51RUyh1HqKKRp9AwEtdq4tDxUUwC9R6rF-5zGNK-HiqeNzKUeJpSMdDQSyZeC1Zy7lYn5SIGDLAYA2ANWEDyPGE+G62Ycy5jzPmxgbmVK-KdbgeAYBPq1lx8yxyrIgEM1AKRGHREGdk26VQtmllTI7l3QwJpFHhT7l2oNPbX3Wf-s5odUaYDue7l58jvmVm-ODQ5vAl0QtTpkY9CLnm+yzJi4OFF+nJqyexWZilanzXGvccjTxox0NAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
