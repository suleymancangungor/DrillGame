import javafx.scene.image.Image;

/**
 * The Block class represents a block in the game world, which could be a valuable mineral, lava, or an obstacle.
 * It stores information about the type of block, its image, and the money and haul value associated with it.
 */
public class Block{
    public String blockType; //Type of block
    public Image image; //The image representing the block
    public int money; //Monetary value of the block
    public int haul; //Haul value of the block.


    /**
     * Constructs a new Block object with the specified properties.
     *
     * @param blockType  The type of the block.
     * @param imagePath  The path to the image file representing the block.
     * @param money      The monetary value of the block.
     * @param haul       The haul value of the block.
     */
    public Block(String blockType,String imagePath,int money,int haul){
        this.blockType = blockType;
        this.image = new Image(imagePath);
        this.money = money;
        this.haul = haul;
    }
}