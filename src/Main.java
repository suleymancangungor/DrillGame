import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the JavaFX application.
 * It extends the Application class and launches the game by creating an instance of the Game class.
 */
public class Main extends Application{

    /**
     * The start method is called when the JavaFX application is launched.
     * It initializes the game by creating a Game object and starting it.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage stage) throws Exception{
        Game game = new Game(stage);
        game.start();
    }

    /**
     * Main method to launch the JavaFX application.
     * @param args Command line arguments.
     */
    public static void main(String[] args){
        launch(args);
    }
}