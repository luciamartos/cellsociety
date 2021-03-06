package rule;

import javafx.scene.paint.Color;
import cellsociety_team13.BackgroundCell;
import cellsociety_team13.Cell;
import cellsociety_team13.CellGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * SchellingModel moves cells to a random "EMPTY" position on the grid whenever
 * they are not "satisfied" a cell is satisfied when its surrounded by at lead t
 * cells that are the same as itself
 *
 * @author Lucia Martos
 */

public class SchellingModel extends Rule {
    /* evaluate cell finds the next state for the current grid and current cell
     * @param myCell is the current cell being analysed
     */
    private List<Cell> emptyCellList;

    @Override
    public void evaluateGrid(CellGrid myGrid) {
        emptyCellList = myGrid.getCellsByType("EMPTY");
        for (int i = 0; i < myGrid.getGridHeight(); i++) {
            for (int j = 0; j < myGrid.getGridWidth(); j++) {
                if (myGrid.getCell(i, j).getNextType() == null) {
                    evaluateCell(myGrid.getCell(i, j), myGrid);
                }
            }
        }
    }

    public void evaluateCell(Cell myCell, CellGrid myGrid) {
        myNeighbours = myGrid.getNeighbours(myCell);
        nonDiagNeighbours = myGrid.getNonDiagNeighbours(myCell);
        if (!satisfiedCell(myCell)) {
            Cell nextCell = findAnEmptyValidCell();
            if (nextCell != null) {
                nextCell.setNextType(myCell.getCurrentType());
                emptyCellList.remove(nextCell);
                myCell.setNextType("EMPTY");
                emptyCellList.add(myCell);
            }
        } else {
            myCell.setNextType(myCell.getCurrentType());
        }
    }

    public void setStatesInMap(Cell myCell) {
        return;
    }

    public void setColor(Cell myCell, CellGrid myGrid) {
        if (myCell.getCurrentType().equals("EMPTY")) {
            myCell.setFill(Color.WHITE);
        } else if (myCell.getCurrentType().equals("X")) {
            myCell.setFill(Color.BLACK);
        } else if (myCell.getCurrentType().equals("O")) {
            myCell.setFill(Color.BLUE);
        }
    }

    private boolean satisfiedCell(Cell myCell) {
        int count = 0;
        if (myCell.getCurrentType().equals("EMPTY")) {
            return true;
        }
        for (Cell neighbour : myNeighbours) {
            if (neighbour.getCurrentType().equals(myCell.getCurrentType())) {
                count++;
            }
        }
        double myT = 100.0 * count / myNeighbours.size();
        return myT > getParameter("similar");
    }


    private Cell findAnEmptyValidCell() {
        if (emptyCellList == null || emptyCellList.size() == 0) {
            return null;
        }
        int choice = (int) (Math.random() * emptyCellList.size());
        return emptyCellList.get(choice);
    }

    @Override
    void setBGStatesInMap(BackgroundCell myBGCell) {
        return;
    }

    @Override
    void evaluateBackgroundCell(BackgroundCell myBackgroundCell) {
        return;
    }
}
