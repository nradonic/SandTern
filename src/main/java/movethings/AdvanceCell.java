package movethings;

import toplevel.Grid;
import toplevel.StuffBlock;
import toplevel.TopLevel;

import java.util.Random;

public class AdvanceCell
{
    static Random random = new Random();

    private enum Direction
    {
        LEFT,
        CENTER,
        RIGHT,
        BOTH,
        NONE;
    }

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
                grid.cell(row, column).getGeneration() == TopLevel.generation;
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

        int stackingHeight = countLoad(grid, row, column);

        int lateralBlocksLeft = countBlocking(grid, row, column, Direction.LEFT);
        boolean flowLeft = lateralBlocksLeft != column;

        int lateralBlocksRight = countBlocking(grid, row, column, Direction.RIGHT);
        boolean flowRight = lateralBlocksRight != column;


        System.out.println("Lateral= " + lateralBlocksLeft + " : " + lateralBlocksRight);

        Direction direction = freespaceDirection(grid, row, column);
        if (StuffBlock.Material.SAND == grid.cell(row, column).material() &&
                stackingHeight >= grid.cell(row, column).material().getStackingLimit() &&
                direction != Direction.CENTER)
        {
            dropCells(grid, row, column, direction);
        }
        if (StuffBlock.Material.WATER == grid.cell(row, column).material() &&
                stackingHeight >= grid.cell(row, column).material().getStackingLimit() &&
                (flowLeft || flowRight))
        {
            flowAndDropCells(grid, row, column, flowLeft, lateralBlocksLeft, flowRight, lateralBlocksRight);
        }
        if (StuffBlock.Material.WATER == grid.cell(row, column).material() &&
                stackingHeight >= grid.cell(row, column).material().getStackingLimit() &&
                (!flowLeft && !flowRight))
        {
            swapBottomWaterCell(grid, row, column);
        }
    }

    private static void swapBottomWaterCell(Grid grid, int row, int column) throws Exception
    {
        StuffBlock stuffBlockTop = grid.cell(row - 1, column).clone();
        StuffBlock stuffBlockBottom = grid.cell(row, column).clone();
        grid.injectCell(row, column, stuffBlockTop);
        grid.injectCell(row - 1, column, stuffBlockBottom);
    }

    private static void flowAndDropCells(Grid grid, int row, int column, boolean flowLeft, int lateralBlocksLeft,
                                         boolean flowRight, int lateralBlocksRight) throws Exception
    {
        boolean localLeft = flowLeft;
        boolean localRight = flowRight;

        int sourceRow = row - 1;
        if (localLeft && localRight)
        {
            int k = new Random().nextInt(2);
            if (k == 0)
            {
                localLeft = false;
            }
            localRight = !localLeft;
        }
        if (localLeft && !localRight)
        {
            flowAndEmpty(grid, row, column, lateralBlocksLeft);
            return;
        }
        if (!localLeft && localRight)
        {
            flowAndEmpty(grid, row, column, lateralBlocksRight);
            return;
        }
    }

    private static void flowAndEmpty(Grid grid, int row, int column, int targetColumn) throws Exception
    {
        StuffBlock stuffBlock = grid.cell(row, column).clone();
        grid.injectCell(row, targetColumn, stuffBlock);
        stuffBlock = grid.cell(row - 1, column).clone();
        grid.injectCell(row, column, stuffBlock);

        grid.injectCell(row - 1, column, new StuffBlock(StuffBlock.Material.EMPTY));
        return;
    }

    private static int countBlocking(Grid grid, int row, int column, Direction direction) throws Exception
    {
        int result = 1;

        int columnIncrement = 1;
        if (Direction.LEFT == direction)
        {
            columnIncrement = -1;
        }
        int lateralThreshold = grid.cell(row, column).material().getLateralBlockThreshold();

        int totalBlocks = 0;
        int columnToTest = column;
        while (columnToTest >= 0 &&
                columnToTest < grid.getColumns() &&
                pressureExtends(grid, row, column, columnToTest) &&
                totalBlocks < lateralThreshold)
        {
            columnToTest += columnIncrement;
        }
        if (StuffBlock.Material.EMPTY != grid.cell(row, columnToTest).material())
        {
            columnToTest = column;
        }

        return columnToTest;
    }

    private static boolean pressureExtends(Grid grid, int row, int column, int columnToTest) throws Exception
    {

        StuffBlock.Material referenceMaterial = grid.cell(row, column).material();
        StuffBlock.Material testMaterial = grid.cell(row, columnToTest).material();
        if (StuffBlock.Material.SAND == testMaterial ||
                StuffBlock.Material.FIXEDCELL == testMaterial ||
                StuffBlock.Material.EMPTY == testMaterial ||

                StuffBlock.Material.SAND == referenceMaterial ||
                StuffBlock.Material.FIXEDCELL == referenceMaterial ||
                StuffBlock.Material.EMPTY == referenceMaterial)
        {
            return false;
        }
        if (StuffBlock.Material.WATER == referenceMaterial &&
                StuffBlock.Material.WATER == testMaterial)
        {
            return true;
        }
        return false;
    }

    private static int countLoad(Grid grid, int row, int column) throws Exception
    {
        int k = row - 1;
        int result = 0;
        StuffBlock.Material material = grid.cell(row, column).material();

        while (k >= 0 &&
                (grid.cell(k, column).material() == StuffBlock.Material.SAND ||
                        grid.cell(k, column).material() == StuffBlock.Material.WATER))
        {
            result += 1;
            k--;
        }
        return result;
    }

    private static Direction freespaceDirection(Grid grid, int row, int column) throws Exception
    {
        Direction result = Direction.CENTER;
        boolean left = column > 0 && grid.cell(row, column - 1).material() == StuffBlock.Material.EMPTY;
        boolean right = column < grid.getColumns() - 1 && grid.cell(row, column + 1).material() == StuffBlock.Material.EMPTY;
        if (left && right)
        {
            result = Direction.BOTH;
        }
        if (left && !right)
        {
            result = Direction.LEFT;
        }
        if (!left && right)
        {
            result = Direction.RIGHT;
        }
        if (!left && !right)
        {
            result = Direction.NONE;
        }
        return result;
    }

    private static void dropCells(Grid grid, int row, int column, Direction direction) throws Exception
    {
        if (direction == Direction.LEFT)
        {
            shiftAside(grid, row - 1, column, row, column - 1);
        }
        if (direction == Direction.RIGHT)
        {
            shiftAside(grid, row - 1, column, row, column + 1);
        }
        if (direction == Direction.BOTH)
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
