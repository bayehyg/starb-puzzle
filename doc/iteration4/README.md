# Star Battle: Iteration 4
## Starting Our Application

To start our application, follow these steps:

**Clone this repository:**
    ```bash
    git clone <repository-url>
    ```

**Run the following command in your terminal:**
    ```bash
    docker compose up -d
    ```


## Server

To start the server:  `./gradlew bootRun` or use Tasks -> application -> bootRun in the
IntelliJ Gradle window.

## DATABASE
Starting the Server automatically populates the database if it is empty.

### Java Client

To start the Java client: `./gradlew run` or use Tasks -> Application -> Run in the IntelliJ Gradle window

Code located in: `src/main/java/starb/client`

## Instructions for Game

1. Run the client and the server
2. When ran properly, it take you to a UI
3. Play the game

### How to play the Game?
* The game is created by using a board. This board is your main game space.
* In the board, there's a grid. The grid contains multiple regions.
* In order to finish the level, you have to place 2 stars in each of the region. However,
more than 2 stars cant exist in the same row and column.
* Each star also can not have another star next to it or neighboring it.
* You can place the stars using the left click on your mouse.
* If placing a star in a row with 