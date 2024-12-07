// Cell.java
package model;

public class Cell {
    private int stones;
    private boolean isBigStoneCell;

    public Cell(int stones, boolean isBigStoneCell) {
        this.stones = stones;
        this.isBigStoneCell = isBigStoneCell;
    }

    public int getStones() {
        return stones;
    }

    public void addStone() {
        stones++;
    }
   
    public boolean isBigStoneCell() {
        return isBigStoneCell;
    }
    
    public int removeAllStones() {
        int removedStones = stones;
        stones = 0;
        return removedStones;
    }

    public void removeBigStones() {
        this.isBigStoneCell = false;
    }
}