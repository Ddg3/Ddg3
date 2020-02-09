package com.zach.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by Zach on 4/14/2018.
 */
public class Window
{
    public JFrame getFrame() {
        return frame;
    }

    private JFrame frame;
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private Canvas canvas;
    private Graphics graphics;
    private BufferStrategy buffStrat;

    public Window(Main main)
    {
        image = new BufferedImage(main.getWidth(), main.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        Dimension dim = new Dimension((int)(main.getWidth() * main.getScale()), (int)(main.getHeight() * main.getScale()));
        canvas.setPreferredSize(dim);
        canvas.setMaximumSize(dim);
        canvas.setMinimumSize(dim);

        frame = new JFrame(main.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        buffStrat = canvas.getBufferStrategy();
        graphics = buffStrat.getDrawGraphics();
    }

    public void update()
    {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        buffStrat.show();
    }
}
