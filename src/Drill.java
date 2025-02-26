import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The Drill class represents the drill machine controlled by the player in the game.
 * It handles properties and behaviors of drill machine, such as fuel level, position, drawing on the canvas, and calculating money and haul.
 */
public class Drill {
    public double fuel=10000; //Initial fuel value of machine.
    public int haul; //Storage
    public int money;
    public double x,y; //Position of machine.
    public Image[] image; //The array of images representing different states of the drill machine.

    /**
     * Constructs a new Drill object with default properties and initial position.
     */
    public Drill(){
        this.image = new Image[]{new Image("/assets/drill/drill_01.png"),new Image("/assets/drill/drill_60.png"),
                new Image("/assets/drill/drill_41.png"),new Image("/assets/drill/drill_26.png")};
        this.haul = 0;
        this.money = 0;
        this.x = 500;
        this.y = 50;
    }

    /**
     * Sets the position of the drill machine based on the provided changeInXPos and changeInYPos values.
     * Also ensures that the drill machine remains within the game bounds.
     *
     * @param changeInXPos The change in x position.
     * @param changeInYPos The change in y position.
     */
    public void setPosition(int changeInXPos, int changeInYPos){
        this.x += changeInXPos;
        this.y += changeInYPos;

        //Ensures the machine remains within the game bounds.
        this.y = Math.max(0,Math.min(700,this.y));
        this.x = Math.max(0,Math.min(750,this.x));
    }

    /**
     * Draws the drill machine on the canvas based on its current position and direction.
     *
     * @param gc        The GraphicsContext used for drawing.
     * @param direction The direction of the drill machine (1=left, 2=right, 3=downward, 4=upward).
     */
    public void draw(GraphicsContext gc,int direction){ //I am drawing the drill machine depending on its move direction.

        //direction 1 = left,  direction 2 = right,  direction 3 = downward,  direction 4 = upward.

        if (direction == 1){
            gc.drawImage(image[0],x,y+5,50,50);
        } else if (direction == 2) {
            gc.drawImage(image[1],x,y+9,50,50);
        } else if (direction == 3) {
            gc.drawImage(image[2],x,y+5,50,50);
        } else if (direction == 4) {
            gc.drawImage(image[3],x,y+7,50,50);
        }

    }

    /**
     * Calculates the money earned when we mine the block.
     *
     * @param block The collected block.
     */
    public void calculateMoney(Block block){
        this.money += block.money;
    }

    /**
     * Calculates the amount of haul collected from a block.
     *
     * @param block The collected block.
     */
    public void calculateHaul(Block block){
        this.haul += block.haul;
    }
}