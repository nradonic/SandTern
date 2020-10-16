package toplevel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Grid extends JPanel
{
    private int rows = 1;
    private int columns = 1;

    StuffBlock[][] grid;
    final int jpanelBorder = 3;
    final int cellBorder = 0;
    final int padded_window = 1000 + 2 * jpanelBorder;
    double cellWidth;
    double cellHeight;

    Grid(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        grid = new StuffBlock[columns][rows];
        emptyGrid();
        setupJPanel();
    }

    private void setupJPanel()
    {
        this.setLayout(new BorderLayout());
        Border s = BorderFactory.createLineBorder(Color.GREEN, jpanelBorder);
        this.setBorder(s);
        Dimension d = new Dimension(padded_window, padded_window);
        setMinimumSize(d);
        recalculateDimensions();
    }

    private void emptyGrid()
    {
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                grid[row][column] = new StuffBlock(StuffBlock.Material.EMPTY);
            }
        }
    }

    private void recalculateDimensions()
    {
        cellWidth = (this.getWidth() - 2 * jpanelBorder) / rows;
        cellHeight = (this.getHeight() - 2 * jpanelBorder) / columns;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        recalculateDimensions();
        paintCells(g);
    }

    public void paintCells(Graphics g)
    {
        g.clearRect(jpanelBorder, jpanelBorder, this.getBounds().width - 2 * jpanelBorder,
                this.getBounds().height - 2 * jpanelBorder);
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, this.getBounds().width, this.getBounds().height);
        drawCellsRC(g);
    }

    private void drawCellsRC(Graphics g)
    {
        for (int row = 0; row < rows; row++)
        {
            drawCellsC(g, row);
        }
    }

    private void drawCellsC(Graphics g, int row)
    {
        for (int column = 0; column < columns; column++)
        {
            drawCell(g, row, column);
        }
    }

    private void drawCell(Graphics g, int row, int column)
    {
        g.setColor(Color.black);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        int leftX = (int) Math.floor(column * cellWidth) + jpanelBorder;
        int topY = (int) Math.floor(row * cellHeight) + jpanelBorder;

        g2.setColor(Color.black);
        g2.drawRect(leftX, topY,
                (int) Math.floor(cellWidth) - cellBorder,
                (int) Math.floor(cellHeight) - cellBorder);

        g2.setColor(grid[row][column].material.getBlockColor());
        g2.fillRect(leftX + cellBorder, topY + cellBorder,
                (int) Math.floor(cellWidth - 2 * cellBorder),
                (int) Math.floor(cellHeight - 2 * cellBorder));
    }

    public StuffBlock cell(int row, int column) throws Exception
    {
        if (row < 0 || row >= rows || column < 0 || column >= columns)
        {
            throw new Exception("grid index out of bounds:" + row + ":" + column);
        }
        return grid[row][column];
    }

    public void injectCell(int row, int column, StuffBlock stuffBlock)
    {
        if (row >= 0 && row < rows && column >= 0 && column < columns)
        {
            grid[row][column] = stuffBlock;
        }
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }
}
