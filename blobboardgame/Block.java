package blobboardgame;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outermost block) is at level 0
 private int maxDepth;
 private Color color;
 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(8);

 public static void main(String[] args){

  /*Block red = new Block(0,0,8,1,2, GameColors.RED, new Block[0]);

  Block green = new Block(8,0,8,1,2, GameColors.GREEN, new Block[0]);

  Block yellow = new Block(0,8,8,1,2, GameColors.YELLOW, new Block[0]);

  Block red2 = new Block(8,8,4,2,2, GameColors.RED, new Block[0]);

  Block blue2 = new Block(12,8,4,2,2, GameColors.BLUE,new Block[0]);

  Block yellow2 = new Block(8,12,4,2,2, GameColors.YELLOW, new Block[0]);

  Block red3 = new Block(12,12,4,2,2, GameColors.RED, new Block[0]);

  Block block1 = new Block(8,8,8,1,2, null, new Block[] {blue2, red2,yellow2,red3});

  Block block0 = new Block(0,0,16,0,2, null, new Block [] {green, red, yellow, block1 });

  block0.printBlock();
*/

  Block blockDepth2 = new Block(0,2);
  blockDepth2.updateSizeAndPosition(16, 0, 0);
  blockDepth2.printColoredBlock();

//  Block blockDepth3 = new Block(0,3);
//  blockDepth3.updateSizeAndPosition(16, 0, 0);
//  Block b1 = blockDepth3.getSelectedBlock(3, 5, 2);
//  b1.printBlock();

//  blockDepth3.reflect(1);
//  blockDepth3.printBlock();

 }

 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {}

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord= x;
  this.yCoord= y;
  this.size= size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color= c;
  this.children = subBlocks;
 }



 /*
  * Creates a random block given its level and a max depth.
  *
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */

 public Block(int lvl, int maxDepth) {
  this.level = lvl;
  this.maxDepth = maxDepth;
  this.children = new Block[0];

  //if level is less than max and math then we will subdivide
  if (this.level < this.maxDepth && gen.nextDouble() < Math.exp(-0.25 * this.level)) {
    this.children = new Block[4];

    for (int i = 0; i < 4; i++) {
     this.children[i] = new Block(lvl + 1, maxDepth);
    }

    //otherwise we color the block
  } else {
   int colorIndex = gen.nextInt(GameColors.BLOCK_COLORS.length);
   this.color = GameColors.BLOCK_COLORS[colorIndex];
  }
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition(int size, int xPos, int yPos) {
//  if (size <= 0 || !isValidSize(size, maxDepth)) {
//   throw new IllegalArgumentException("Invalid size provided.");
//  }
//
//  this.size = size;
//  this.xCoord = xPos;
//  this.yCoord = yPos;
//
//  if (this.children.length > 0) {
//   int childSize = size / 2;
//
//   int [] num = {1,0,2,3};
//   int childIndex = 0;
//
//   for (int i: num) {
//
//    int childX = xPos + (i % 2) * childSize;
//    int childY = yPos + (i / 2) * childSize;
//
//    this.children[childIndex].updateSizeAndPosition(childSize, childX, childY);
//    childIndex++;
//   }
//  }

   if (size < 0) {
    throw new IllegalArgumentException("Invalid size: Size cannot be negative.");
   }

   if (size % 2 != 0 && level != maxDepth) {
    throw new IllegalArgumentException("Invalid size: Size must be evenly divisible by 2.");
   }

   if (level == maxDepth) { //instructions: divided into 2 integers until the max depth is reached
    this.size = size;
    this.xCoord = xPos;
    this.yCoord = yPos;
    return;
   }

   this.size = size;
   this.xCoord = xPos;
   this.yCoord = yPos;

   // If the block is subdivided, recursively update its children
   if (children != null && children.length > 0) {
    int halfSize = size / 2; // Calculate half of the current size

    children[0].updateSizeAndPosition(halfSize, xPos + halfSize, yPos); // UR
    children[1].updateSizeAndPosition(halfSize, xPos, yPos); // UL
    children[2].updateSizeAndPosition(halfSize, xPos, yPos + halfSize); // LL
    children[3].updateSizeAndPosition(halfSize, xPos + halfSize, yPos + halfSize); // LR

   }
  }



// private boolean isValidSize(int size, int maxDepth) {
//  int newSize = size;
//  for (int i = 0; i < maxDepth; i++) {
//   newSize /= 2;
//   if (newSize <= 0) {
//    return false;// Size cannot be divided evenly into 2 integers
//   }
//  }
//  return true;
// }



 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  ArrayList<BlockToDraw> blocksToDrawList = new ArrayList<>();

  // If the block is undivided
  if (children.length == 0) {
   // Add BlockToDraw with block's color and stroke thickness 0 (filled)
   BlockToDraw filledBlock = new BlockToDraw(color, xCoord, yCoord, size, 0);
   blocksToDrawList.add(filledBlock);

   // Add BlockToDraw with FRAME_COLOR and stroke thickness 3
   BlockToDraw frameBlock = new BlockToDraw(GameColors.FRAME_COLOR, xCoord, yCoord, size, 3);
   blocksToDrawList.add(frameBlock);
  }

  // Recursively add blocks from children
  for (Block child : children) {
   blocksToDrawList.addAll(child.getBlocksToDraw());
  }

  return blocksToDrawList;
 }

 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }



 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
//  System.out.println("this.level: " + this.level);
//  System.out.println("this.maxDepth: " + this.maxDepth);


  // Input validation: check if lvl is within valid range
  if (lvl < this.level ||lvl > this.maxDepth) {
   throw new IllegalArgumentException("Invalid level specified.");
  }
  if ((xCoord <= x && (this.xCoord + size) > x) && (yCoord <= y && (yCoord + size) > y)){
   if (level == lvl || children.length == 0){
    return this;

   } else{
    for (int i =0; i<children.length; i++){
     if (children[i].getSelectedBlock(x,y,lvl) != null){

      return children[i].getSelectedBlock(x,y,lvl);
     }
    }
   }
  }
  return null;
 }

  //if level is smaller than this.level or larger than this.maxdepth
  //throw illegalArgumnet
  //they click on the box and use up and down to pick the level
  //if the level chosen is lower than the lowest block at that location then return the block with the
  //closest level
  //if a block has a location (x,y) and we subdivide this block then one of the sub blocks will have that location
  //this is why we need to know the location of






 /*
  * Swaps the child Blocks of this Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this Block has no children, do nothing. The swap
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("Direction must be 0 (x-axis) or 1 (y-axis)");
  }

  // Base case: If this block has no children, do nothing
  if (children.length == 0) {
   return;
  }

  // Reflect the children blocks based on the specified direction
  if (direction == 0) { // Reflect over the x-axis
   Block temp = children[0];
   children[0] = children[3];
   children[3] = temp;

   temp = children[1];
   children[1] = children[2];
   children[2] = temp;

   // Reflect over the y-axis
  } else {
   Block temp = children[0];
   children[0] = children[1];
   children[1] = temp;

   temp = children[2];
   children[2] = children[3];
   children[3] = temp;
  }

  // Recursively reflect sub-blocks
  for (Block child : children) {
   child.reflect(direction);
  }

  //System.out.println("Current size: " + size);


  updateSizeAndPosition(size, xCoord, yCoord);





 }

 /*
  * Rotate this Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("Direction must be 0 (counterclockwise) or 1 (clockwise).");
  }

  // If this block has no children, do nothing
  if (children.length == 0) {
   return;
  }

  // Rotate the children blocks based on the specified direction

  if (direction == 0) { // Rotate counterclockwise
   // Swap children blocks in counterclockwise direction
   Block temp = children[3];
   children[3] = children[2];
   children[2] = children[1];
   children[1] = children[0];
   children[0] = temp;
   //reorder


   updateSizeAndPosition(size, xCoord, yCoord);


  } else { // Rotate clockwise
   Block temp = children[0];
   children[0] = children[1];
   children[1] = children[2];
   children[2] = children[3];
   children[3] = temp;

   updateSizeAndPosition(size, xCoord, yCoord);

   }

  // Recursively rotate sub-blocks
  for (Block child : children) {
   child.rotate(direction);

   //System.out.println("Current size: " + size);

  }
 }




 /*
  * Smash this Block.
  *
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A Block can be smashed iff it is not the top-level Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this Block was smashed and False otherwise.
  *
  */
 public boolean smash() {
  if (level == 0 || level == maxDepth) {
   return false; // Cannot be smashed
  }

  // Generate four new random sub-blocks
  children = new Block[4];
  for (int i = 0; i < 4; i++) {
   children[i] = new Block(level + 1, maxDepth);
  }

  updateSizeAndPosition(size, xCoord, yCoord);


  return true; // Block was successfully smashed

 }


 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  int newSize = (int) Math.pow(2, maxDepth);
  Color[][] result = new Color[newSize][newSize]; // Create a 2D array to store the colors of unit cells
  int length = size / newSize;

  for (int i = 0; i < newSize; i++){
   for (int j = 0; j < newSize; j++){

    int k = maxDepth;
    Block b = getSelectedBlock(i * length, j * length, k);
    result[j][i] = b.color;
   }
  }
  return result;
 }



 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }

}
