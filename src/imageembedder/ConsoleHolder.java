package imageembedder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import resources.ResourceHelper;

public abstract class ConsoleHolder extends JPanel {

    private Image deskImage = ResourceHelper.getImageObject("deskImage.png");
    private final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private float aspectRatio;
    private Rectangle consoleArea;
    private final Console console;

    public abstract void locationChanged(int x, int y);

    private Dimension holderSize;

    public ConsoleHolder(EmbedderInfo embedderInfo) {
        console = new Console(embedderInfo) {
            @Override
            public void locationChanged(int x, int y) {
                ConsoleHolder.this.locationChanged(x, y);
            }
        };
        preInitComponent();
    }

    private void preInitComponent() {
        setLayout(null);
        setBackground(TRANSPARENT);
        int imageWidth = 700;
        deskImage = deskImage.getScaledInstance(imageWidth, -1, Image.SCALE_SMOOTH);
        aspectRatio = (float) imageWidth / deskImage.getHeight(null);
        holderSize = new Dimension(imageWidth, (int) (imageWidth / aspectRatio));
        setSize(holderSize);
        setPreferredSize(holderSize);

        // Setting console area
        {
            ResourceBundle bundle = ResourceHelper.getResourceBundle("res");
            Dimension imgSize = new Dimension(
                    Integer.parseInt(bundle.getString("DeskImageWidth")),
                    Integer.parseInt(bundle.getString("DeskImageHeight")));

            Point start1 = new Point(
                    Integer.parseInt(bundle.getString("DeskImageConsoleStartX")),
                    Integer.parseInt(bundle.getString("DeskImageConsoleStartY")));

            Point end1 = new Point(
                    Integer.parseInt(bundle.getString("DeskImageConsoleEndX")),
                    Integer.parseInt(bundle.getString("DeskImageConsoleEndY")));

            Point start2 = new Point(holderSize.width * start1.x / imgSize.width,
                    holderSize.height * start1.y / imgSize.height);
            Point end2 = new Point(holderSize.width * end1.x / imgSize.width,
                    holderSize.height * end1.y / imgSize.height);
            consoleArea = new Rectangle(start2.x, start2.y, end2.x - start2.x, end2.y - start2.y);
        }
        add(getConsole());
        getConsole().setBounds(consoleArea);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (deskImage != null) {
            g.drawImage(deskImage, 0, 0, null);
        }
        super.paintComponent(g);
    }

    /**
     * @return the console
     */
    public Console getConsole() {
        return console;
    }
}
