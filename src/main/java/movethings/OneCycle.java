package movethings;

import toplevel.Grid;

public class OneCycle
{

    private static int rows = 0;

    public static boolean moveCells(Grid grid, int injectionColumn) throws Exception
    {
        boolean result = false;
        rows = grid.getRows();
        int row = rows - 1;

        while (row >= 0)
        {
            advanceRow(grid, row);
            row--;
        }
        result = grid.cell(0, injectionColumn).isEmpty();
        return result;
    }

    private static void advanceRow(Grid grid, int row) throws Exception
    {
        for (int column = 0; column < grid.getColumns(); column++)
        {
            AdvanceCell.advanceCellOneRow(grid, row, column);
        }
    }
}
