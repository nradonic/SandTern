package movethings;

import toplevel.Grid;
import toplevel.StuffBlock;
import toplevel.TopLevel;

public class AdvanceCell
{


    public static void advanceCellOneRow(Grid grid, int row, int column) throws Exception
    {
        if (isNonMovableBlock(grid, row, column))
        {
            collapse(grid, row, column);
            return;
        }

        if (isLastRow(grid, row))
        {
            grid.injectCell(row, column, new StuffBlock(StuffBlock.Material.EMPTY));
            return;
        }

        grid.injectCell(row + 1, column, grid.cell(row, column).clone());
        grid.injectCell(row, column, new StuffBlock(StuffBlock.Material.EMPTY));
    }


    private static boolean isNonMovableBlock(Grid grid, int row, int column) throws Exception
    {
        return row < 0 ||
                row >= grid.getRows() ||
                (row < grid.getRows() - 1) && (grid.cell(row + 1, column).material() != StuffBlock.Material.EMPTY) ||
                grid.cell(row, column).material() == StuffBlock.Material.FIXEDCELL ||
                grid.cell(row, column).material() == StuffBlock.Material.EMPTY ||
                grid.cell(row, column).getGeneration() == TopLevel.generation
                ;
    }

    private static boolean isLastRow(Grid grid, int row)
    {
        return row == grid.getRows() - 1;
    }

    private static void collapse(Grid grid, int row, int column) throws Exception
    {
        StuffBlock.Material material = grid.cell(row, column).material();
        if (material == StuffBlock.Material.EMPTY ||
                material == StuffBlock.Material.FIXEDCELL)
        {
            return;
        }

        int k = countLoad(grid, row, column);
        int direction = freespaceDirection(grid, row, column);
        if (k >= grid.cell(row, column).material().getStackingLimit() && direction != 0)
        {
            dropCells(grid, row, column, direction);
        }
    }

    private static int countLoad(Grid grid, int row, int column) throws Exception
    {
        int k = row - 1;
        int result = 0;
        StuffBlock.Material material = grid.cell(row, column).material();

        while (k >= 0 && grid.cell(k, column).material() == material)
        {
            result += 1;
            k--;
        }
        return result;
    }

    private static int freespaceDirection(Grid grid, int row, int column) throws Exception
    {
        int result = 0;
        boolean left = column > 0 && grid.cell(row, column - 1).material() == StuffBlock.Material.EMPTY;
        boolean right = column < grid.getColumns() - 1 && grid.cell(row, column + 1).material() == StuffBlock.Material.EMPTY;
        if (left && right)
        {
            result = 2;
        }
        if (left && !right)
        {
            result = -1;
        }
        if (!left && right)
        {
            result = 1;
        }
        return result;
    }

    private static void dropCells(Grid grid, int row, int column, int direction) throws Exception
    {
        if (direction == -1)
        {
            shiftAside(grid, row-1, column, row, column - 1);
        }
        if (direction == 1)
        {
            shiftAside(grid, row-1, column, row, column + 1);
        }
        if (direction == 2)
        {
            shiftAside(grid, row, column, row, column - 1);
            shiftAside(grid, row - 1, column, row, column + 1);
        }
    }

    private static void shiftAside(Grid grid, int sourceRow, int sourceColumn, int targetRow, int targetColumn) throws Exception
    {
        StuffBlock stuffBlock = grid.cell(sourceRow, sourceColumn).clone();
        grid.injectCell(targetRow, targetColumn, stuffBlock);
        grid.injectCell(sourceRow, sourceColumn, new StuffBlock(StuffBlock.Material.EMPTY));
    }
}
