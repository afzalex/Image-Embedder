package imageembedder;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import resources.ResourceHelper;

public abstract class ImageEmbedderWindow extends JFrame {

    final ImageEmbedderConsole imageEmbedderConsole;

    public ImageEmbedderWindow(EmbedderInfo embedderInfo) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
            ResourceHelper.errLog("ImageEmbedderConsole > main(...) > Error : " + ex);
        }
        Rectangle frameBounds = new Rectangle();
        frameBounds.setSize(830, 600);
        Rectangle desktopBounds = ResourceHelper.getResource("DesktopBounds");
        frameBounds.setLocation((desktopBounds.width - frameBounds.width) / 2,
                (desktopBounds.height - frameBounds.height) / 2);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Image Embedder");
        setSize(frameBounds.getSize());
        setLocation(frameBounds.getLocation());
        setResizable(false);
        imageEmbedderConsole = new ImageEmbedderConsole(this, embedderInfo) {
            @Override
            void applyChanges() {
                ImageEmbedderWindow.this.applyChanges();
            }

            @Override
            void updateWallpapers() {
                ImageEmbedderWindow.this.updateWallpapers();
            }
        };
        add(imageEmbedderConsole);
        try {
            Image icon = ResourceHelper.getImageObject("icon.png");
            ResourceHelper.setResource("icon", icon);
            setIconImage(icon);
        } catch (Exception ex) {
            ResourceHelper.errLog("ImageEmbedderWindow > Cunstructor () > Cannot set Icon > Error : " + ex);
        }
    }

    public EmbedderInfo getEmbedderInfo() {
        return imageEmbedderConsole.getEmbedderInfo();
    }

    abstract void applyChanges();

    abstract void updateWallpapers();
}
