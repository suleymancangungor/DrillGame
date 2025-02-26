import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.canvas.*;

/**
 * This class represents a simple mining game implemented using JavaFX.
 * The player controls a drill machine to dig through blocks and collect resources.
 */
public class Game{

    public Stage stage;
    public static AnimationTimer timer; //for run program until end.
    public static Block[][] blocksInMap; //for storing block objects and making calculations.
    public static Block[] mineralBlocks; //blocks list.
    public Drill drill;
    public Text textFuel,textHaul,textMoney; //for score texts.
    public String updatedFuel,updatedHaul,updatedMoney; //for score texts.
    public int gravityTimer = 0; //gravityTimer and gravityDelay is for falling down slowly if there is no block under the machine.
    public int gravityDelay = 100;
    public boolean gameOverLose,gameOverWin= false; //to check how the game ends.
    public Text gameOverText,gameOverMoney; //for game over texts.
    public int direction = 1; //for drawing machine according to its direction.


    /**
     * Constructs a new Game object.
     *
     * @param stage The primary stage for the game.
     */
    public Game(Stage stage){
        this.stage = stage;
    }


    /**
     * Starts the game by initializing the game world, setting up the UI, and handling user input.
     */
    public void start(){
        //Create the UI.
        StackPane stackPane = new StackPane();
        Canvas canvas = new Canvas(800,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stackPane.getChildren().add(canvas);
        Scene scene = new Scene(stackPane,800,800);

        blocksInMap = new Block[16][16]; //Creates list for adding block objects.

        //All valuable blocks including lava and obstacle blocks.
        mineralBlocks = new Block[]{new Block("Ironium","/assets/underground/valuable_ironium.png",30,10), new Block("Bronzium","/assets/underground/valuable_bronzium.png",60,10),
                new Block("Silverium","/assets/underground/valuable_silverium.png",100,10), new Block("Goldium","/assets/underground/valuable_goldium.png",250,20),
                new Block("Platinum","/assets/underground/valuable_platinum.png",750,30), new Block("Einsteinium","/assets/underground/valuable_einsteinium.png",2000,40),
                new Block("Emerald","/assets/underground/valuable_emerald.png",5000,60), new Block("Ruby","/assets/underground/valuable_ruby.png",20000,80),
                new Block("Diamond","/assets/underground/valuable_diamond.png",100000,100), new Block("Amazonite","/assets/underground/valuable_amazonite.png",500000,120),
                new Block("Lava","/assets/underground/lava_01.png",0,0), new Block("Lava","/assets/underground/lava_02.png",0,0),
                new Block("Lava","/assets/underground/lava_03.png",0,0), new Block("Obstacle","/assets/underground/obstacle_01.png",0,0),
                new Block("Obstacle","/assets/underground/obstacle_02.png",0,0), new Block("Obstacle","/assets/underground/obstacle_03.png",0,0)};

        drill = new Drill(); //Creating drill machine object.
        drawBackground(gc); //Draws background.
        drawDrill(gc,direction); //Draws drill machine.

        //This part is for adding fuel, haul, money text on screen.
        VBox scoreBox = new VBox(10);
        scoreBox.setAlignment(Pos.TOP_LEFT);

        textFuel = new Text();
        textFuel.setFill(Color.WHITE);
        textFuel.setFont(Font.font("Arial", 24));

        textHaul = new Text();
        textHaul.setFill(Color.WHITE);
        textHaul.setFont(Font.font("Arial", 24));

        textMoney = new Text();
        textMoney.setFill(Color.WHITE);
        textMoney.setFont(Font.font("Arial", 24));

        scoreBox.getChildren().addAll(textFuel,textHaul,textMoney);
        stackPane.getChildren().add(scoreBox);

        //Handle user input.
        scene.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());
            gc.clearRect(drill.x+2, drill.y+4,48,46);  //Clears current block(before calculation).
            int changeInXPos = 0; //Change in the x position.
            int changeInYPos = 0; //Change in the y position.

            drill.fuel -= 100;//Fuel runs out faster when moving or trying to move.

            if (event.getCode() == KeyCode.UP){
                direction = 4;
                if (drill.y != 0 && blocksInMap[(int)(drill.y/50)-1][(int)(drill.x/50)]==null) { //Can't drill upward.
                    drill.fuel -= 10; //Fuel runs out faster when flying against gravity.
                    changeInYPos -= 50;
                    gravityTimer = 0;
                }
            } else if (event.getCode() == KeyCode.DOWN) {
                if ((drill.x==750 || drill.x==0) && drill.y==100){
                    changeInYPos += 0;
                }else{
                    changeInYPos += 50;
                }
                direction = 3;
            } else if (event.getCode() == KeyCode.LEFT) {
                direction = 1;
                changeInXPos -= 50;
            } else if (event.getCode() == KeyCode.RIGHT) {
                direction = 2;
                changeInXPos += 50;
            }

            if (drill.y < 100){  //Draws background according to its position. This position is the position of current block(before calculating, moving).
                gc.setFill(Color.rgb(25,25,113));
                gc.fillRect(drill.x-1, drill.y-1,51,51);
            }else {
                gc.setFill(Color.rgb(161,81,46));
                gc.fillRect(drill.x-1, drill.y-1,51,51);
            }

            if (!((int)(drill.x+changeInXPos)/50 >= 0  && (int)(drill.x+changeInXPos)/50 < 16 && blocksInMap[(int)(drill.y+changeInYPos)/50][(int)(drill.x+changeInXPos)/50]!=null && blocksInMap[(int)(drill.y+changeInYPos)/50][(int)(drill.x+changeInXPos)/50].blockType.equals("Obstacle"))){
                //We can't drill obstacles.

                drill.setPosition(changeInXPos,changeInYPos); //Calculates next blocks position and moves there.

                //It calculates move and then if the new position has lava block, game will over. And also machine can't drill upward.

                int x = (int) drill.x/50;
                int y = (int) drill.y/50;

                if (blocksInMap[y][x]!=null && blocksInMap[y][x].blockType.equals("Lava")){
                    gameOverLose = true;
                }

                if (blocksInMap[y][x]!=null){ //If there is block, calculates new money and haul values.
                    drill.calculateMoney(blocksInMap[y][x]);
                    drill.calculateHaul(blocksInMap[y][x]);
                }

                if ((x<16) && (y<16) && blocksInMap[y][x]!=null){  //Removes block and draws background(next block, after calculation).
                    blocksInMap[y][x]=null;
                }
                if (drill.y < 100){
                    gc.setFill(Color.rgb(25,25,113));
                    gc.fillRect(drill.x-1, drill.y-1,51,51);
                }else {
                    gc.setFill(Color.rgb(161,81,46));
                    gc.fillRect(drill.x-1, drill.y-1,51,51);
                }
            }
        });

        //It checks whether game is over. If not, updates values, makes some delay calculations for gravity.
        //And also draws drill every single time.
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOverLose){
                    finishGame(gc,stackPane,canvas,scene);
                } else if (gameOverWin) {
                    finishGame(gc,stackPane,canvas,scene);
                } else{
                    update();
                    gravityTimer++;

                    textFuel = (Text) scoreBox.getChildren().get(0);
                    textFuel.setText(updatedFuel);

                    textHaul = (Text) scoreBox.getChildren().get(1);
                    textHaul.setText(updatedHaul);

                    textMoney = (Text) scoreBox.getChildren().get(2);
                    textMoney.setText(updatedMoney);

                    if (gravityTimer >= gravityDelay){
                        gravity(gc);
                    }
                    drawDrill(gc,direction);
                }
            }
        };
        timer.start();

        //Set up the stage and show the scene.
        stage.setResizable(false); //Screen is not resizable.
        stage.setTitle("HU-Load");
        stage.setScene(scene);
        stage.show();

    }


    /**
     * Updates the fuel, haul, and money values. If the fuel runs out, sets gameOverWin variable to true.
     */
    public void update(){
        drill.fuel -=0.1111; //Fuel is decreasing.

        if (drill.fuel <= 0){ //Checks remaining fuel.
            gameOverWin = true;
        }else {
            updatedFuel = "fuel:"+String.format("%.4f",drill.fuel);
            updatedHaul = "haul:"+drill.haul;
            updatedMoney = "money:"+drill.money;
        }
    }

    /**
     * Draws the background of the game and places blocks in the map.
     *
     * @param gc The GraphicsContext used for drawing.
     */
    public void drawBackground(GraphicsContext gc){
        gc.clearRect(0,0,800,800);
        gc.setFill(Color.rgb(161,81,46));
        gc.fillRect(0,104,800,800);
        gc.setFill(Color.rgb(25,25,113));
        gc.fillRect(0,0,800,104);

        //Starts in row 3 to place block because there will be sky in first 2 row (null).Block placement will be randomly in earth.
        for (int row=2;row<16;row++){
            for (int col=0;col<16;col++){
                if (row==2){
                    int randomIndex = (int) (Math.random() * 2) + 1;
                    blocksInMap[row][col] = new Block("Top","/assets/underground/top_0"+randomIndex+".png",0,0);
                } else if (col==0 || col==15 || row==15) {
                    int randomIndex = (int) (Math.random() * 3) + 1;
                    blocksInMap[row][col] = new Block("Obstacle","/assets/underground/obstacle_0"+randomIndex+".png",0,0);
                } else if (Math.random()<0.10) { //Chance to place mineral blocks. Includes lava and obstacle blocks.
                    int randomIndex = (int) (Math.random() * mineralBlocks.length);
                    blocksInMap[row][col] = mineralBlocks[randomIndex];
                }else {
                    int randomIndex = (int) (Math.random() * 5) + 1;
                    blocksInMap[row][col] = new Block("Soil","/assets/underground/soil_0"+randomIndex+".png",0,0);
                }
                gc.drawImage(blocksInMap[row][col].image,col*50,row*50,50,50); //Draws image of block.
            }
        }
    }

    /**
     * Draws the drill machine according to its direction.
     *
     * @param gc        The GraphicsContext used for drawing.
     * @param direction The direction of the drill machine (1=left, 2=right, 3=down, 4=up).
     */
    public void drawDrill(GraphicsContext gc,int direction){
        drill.draw(gc,direction);
    }

    /**
     * Finishes the game and displays the game over screen.
     *
     * @param gc        The GraphicsContext used for drawing.
     * @param stackPane The StackPane containing the canvas and other UI elements.
     * @param canvas    The Canvas used for drawing.
     * @param scene     The Scene of the game.
     */
    public void finishGame(GraphicsContext gc, StackPane stackPane, Canvas canvas, Scene scene){ //Finishes game.
        scene.setOnKeyPressed(null); //We can't move
        stackPane.getChildren().clear();
        stackPane.getChildren().add(canvas);
        gc.clearRect(0,0,800,800);

        if (gameOverWin){ //Green game over screen will show up.
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(0,0,800,800);

            VBox gameOverBox = new VBox(10);
            gameOverBox.setAlignment(Pos.CENTER);

            gameOverText = new Text("GAME OVER");
            gameOverText.setFill(Color.WHITE);
            gameOverText.setFont(Font.font("Arial", 40));

            gameOverMoney = new Text("Collected Money: "+drill.money);
            gameOverMoney.setFill(Color.WHITE);
            gameOverMoney.setFont(Font.font("Ariel",40));

            gameOverBox.getChildren().addAll(gameOverText,gameOverMoney);
            stackPane.getChildren().add(gameOverBox);

        }else if (gameOverLose){ //Red game over screen will show up.
            gc.setFill(Color.DARKRED);
            gc.fillRect(0,0,800,800);

            VBox gameOverBox = new VBox();
            gameOverBox.setAlignment(Pos.CENTER);

            gameOverText = new Text("GAME OVER");
            gameOverText.setFill(Color.WHITE);
            gameOverText.setFont(Font.font("Arial", 40));
            gameOverBox.getChildren().add(gameOverText);
            stackPane.getChildren().add(gameOverBox);
        }
    }

    /**
     * Applies gravity to the drill machine, making it move downward if there is no block below.
     *
     * @param gc The GraphicsContext used for drawing.
     */
    public void gravity(GraphicsContext gc){
        int changeInYPos = 0;
        //If drill machine doesn't have any block under of it, machine will move downward(gravity).
        if (blocksInMap[(int)(drill.y/50)+1][(int)(drill.x/50)]==null) {
            direction = 4;
            changeInYPos +=50;
        }

        //Creates background, depends on position of machine.
        gc.clearRect(drill.x+2, drill.y+4,48,46); //First clears then draws.
        if (drill.y < 100){
            gc.setFill(Color.rgb(25,25,113));
            gc.fillRect(drill.x-1, drill.y-1,51,51);
        }else {
            gc.setFill(Color.rgb(161,81,46));
            gc.fillRect(drill.x-1, drill.y-1,51,51);
        }

        drill.setPosition(0,changeInYPos); //Moves to next block.
        gravityTimer = 0;
    }
}