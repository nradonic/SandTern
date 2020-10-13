package toplevel;

import java.awt.*;

public class StuffBlock
{
    double weight = 1.0;
    Material material = Material.SAND;
    private int generation = 0;

    public StuffBlock(Material material)
    {
        this.material = material;
        generation = TopLevel.generation;
    }

    public boolean isEmpty()
    {
        return material == Material.EMPTY;
    }

    public StuffBlock clone()
    {
        StuffBlock k = new StuffBlock(material);
        return k;
    }

    public int getGeneration()
    {
        return generation;
    }

    public void setGeneration(int generation)
    {
        this.generation = generation;
    }

    public enum Material
    {
        EMPTY(Color.GRAY, 0.0, Integer.MAX_VALUE),
        SAND(Color.yellow, 2.0, 1),
        FIXEDCELL(Color.BLACK, 0.0, Integer.MAX_VALUE);

        private final Color blockColor;
        private final double density;
        private final int stackingLimit;

        Material(Color blockColor, double density, int stackingLimit)
        {
            this.blockColor = blockColor;
            this.density = density;
            this.stackingLimit = stackingLimit;
        }

        public Color getBlockColor()
        {
            return this.blockColor;
        }

        public double getDensity()
        {
            return this.density;
        }

        public int getStackingLimit()
        {
            return this.stackingLimit;
        }
    }

    public Material material()
    {
        return this.material;
    }

}
