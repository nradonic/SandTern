package toplevel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class Controls extends JPanel {
    static Logger logger = Logger.getLogger(Controls.class.getName());

    JButton button1;
    int count = 0;

    Controls() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        Border border = BorderFactory.createLineBorder(Color.CYAN);
        this.setBorder(border);
        this.setBackground(Color.BLUE);

        button1 = new JButton("Next");
        button1.addActionListener(actionListener);

        add(button1);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            count++;
//            logger.info("Count: " + count);
        }
    };

    public void paint(Graphics g) {
        super.paint(g);
        button1.setText("Next " + count);
    }

}