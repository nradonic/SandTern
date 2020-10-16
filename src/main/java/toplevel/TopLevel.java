package toplevel;

import movethings.OneCycle;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TopLevel
{

    static Aquarium aquarium;
    static Grid grid;
    final static long generationTime = 100;
    public static int generation = 1;
    static final int gridSize = 30;
    static int injectionColumn = 0;
    static int injectionRow = 0;
    static Random random = new Random();

    public static void main(String[] args) throws Exception
    {
        grid = new Grid(gridSize, gridSize);
        aquarium = new Aquarium(grid);

        injectionColumn = gridSize / 2;
        injectionRow = 0;

        initializeGrid(grid);

        boolean spaceToDrop = true;
        do
        {
            TimeUnit.MILLISECONDS.sleep(generationTime);
            if (OneCycle.moveCells(grid, injectionColumn) && generation % 5 == 0)
            {
                StuffBlock stuffBlock = new StuffBlock(StuffBlock.Material.WATER);
                int k = gridSize / 2;
                injectCells(grid, injectionRow, k, stuffBlock);
            }
            aquarium.repaint();

            generation++;
            System.out.println("Generation: " + generation);
        } while (spaceToDrop);
    }

    private static void injectCells(Grid grid, int injectionRow, int injectionColumn, StuffBlock stuffBlock) throws Exception
    {
        if (grid.cell(injectionRow, injectionColumn).material == StuffBlock.Material.FIXEDCELL)
        {
            return;
        }
        grid.injectCell(injectionRow, injectionColumn, stuffBlock);
    }

    private static void initializeGrid(Grid grid) throws Exception
    {
        StuffBlock stuffBlock = new StuffBlock(StuffBlock.Material.FIXEDCELL);
        int row = (int) (gridSize * 0.7);
        for (int i = -3; i <= 3; i++)
        {
            injectCells(grid, row, injectionColumn + i, stuffBlock);
        }

//        stuffBlock = new StuffBlock(StuffBlock.Material.WATER);
//        injectCells(grid, row-1, injectionColumn - 1, stuffBlock);
//        stuffBlock = new StuffBlock(StuffBlock.Material.WATER);
//        injectCells(grid, row-1, injectionColumn, stuffBlock);
//        stuffBlock = new StuffBlock(StuffBlock.Material.WATER);
//        injectCells(grid, row-1, injectionColumn + 1, stuffBlock);
//        stuffBlock = new StuffBlock(StuffBlock.Material.WATER);
//        injectCells(grid, row-2, injectionColumn, stuffBlock);


//        injectCells(grid, row, injectionColumn + 1, stuffBlock);
//        injectCells(grid, row, injectionColumn - 1, stuffBlock);
        for (int col = (int) (gridSize * .2); col < (int) (gridSize * .8); col++)
        {
            injectCells(grid, (int) row, col, stuffBlock);
        }
    }
}
