package toplevel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Aquarium extends JFrame {
    static Logger logger = Logger.getLogger(Aquarium.class.getName());

    String emptyLabel = "Sand Game";
    String message = "";

    static boolean nextGeneration = false;
    static boolean continuous = false;
    static Double angle = 0.0;

    static Controls controls = new Controls();
    final int border_width = 3;
    final int padded_window = 1000 + 2 * border_width;

    Grid grid;

    Aquarium(Grid grid) {
        this.grid = grid;
        setLayout(new BorderLayout());
        annotateTitle("start");

        add(controls, BorderLayout.NORTH);

        Dimension d = new Dimension(padded_window, padded_window);

        add(grid, BorderLayout.CENTER);

        setMinimumSize(d);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        validate();
        pack();


        this.setVisible(true);

    }

    public void annotateTitle(String s) {
        this.setTitle(emptyLabel + " " + s);
    }

}
