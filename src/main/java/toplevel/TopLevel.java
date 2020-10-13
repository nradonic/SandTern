package toplevel;

import movethings.OneCycle;

import java.util.concurrent.TimeUnit;

public class TopLevel
{

    static Aquarium aquarium;
    static Grid grid;
    final static long generationTime = 100;
    public static int generation = 1;
    static final int gridSize = 25;
    static int injectionColumn = 0;
    static int injectionRow = 0;

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
            if (OneCycle.moveCells(grid, injectionColumn) && generation % 10 == 0)
            {
                StuffBlock stuffBlock = new StuffBlock(StuffBlock.Material.SAND);
                injectCells(grid, injectionRow, injectionColumn, stuffBlock);
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
        injectCells(grid, row, injectionColumn, stuffBlock);
        injectCells(grid, row+3, injectionColumn+1, stuffBlock);
        injectCells(grid, row+2, injectionColumn-1, stuffBlock);
        for (int col = (int)(gridSize*.3);col < (int)(gridSize*.7);col++){
            injectCells(grid, row+4, col, stuffBlock);

        }
    }
}
