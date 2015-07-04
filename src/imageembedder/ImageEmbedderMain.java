package imageembedder;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import resources.ResourceHelper;

public class ImageEmbedderMain {

    ImageEmbedderWindow imageEmbedderWindow;
    FileSynchronizer fileSynchronizer;
    SystemTray systemTray;
    TrayIcon trayIcon;
    MenuItem lastUpdated = new MenuItem("Last Updated : ");
    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM YY");
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:m:s a");

    public ImageEmbedderMain() {
        ResourceHelper.setResource("embedderlastopened", new Date().getTime());

        fileSynchronizer = new FileSynchronizer() {
            @Override
            protected void imagesUpdated() {
                ImageEmbedderMain.this.imagesUpdated(new Date());
            }
        };
        try {
            fileSynchronizer.reset();
            EmbedderInfo ei = fileSynchronizer.loadEmbedderInfo();
            imageEmbedderWindow = new ImageEmbedderWindow(ei) {
                @Override
                void applyChanges() {
                    ImageEmbedderMain.this.applyChanges();
                }

                @Override
                void updateWallpapers() {
                    ImageEmbedderMain.this.update();
                }
            };
            setTrayIcons();
        } catch (WinSettings.RegistryException ex) {
            ResourceHelper.errLog("ImageEmbedderMain -> Constructor -> Error : " + ex);
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            ResourceHelper.errLog("ImageEmbedderMain -> Constructor -> Error : " + ex);
            try {
                fileSynchronizer.deleteEmbedderInfo();
                fileSynchronizer.reset();
                EmbedderInfo ei = fileSynchronizer.loadEmbedderInfo();
                imageEmbedderWindow = new ImageEmbedderWindow(ei) {
                    @Override
                    void applyChanges() {
                        ImageEmbedderMain.this.applyChanges();
                    }

                    @Override
                    void updateWallpapers() {
                        ImageEmbedderMain.this.update();
                    }
                };
                setTrayIcons();
            } catch (IOException | WinSettings.RegistryException | ParserConfigurationException | SAXException ex1) {
                ResourceHelper.errLog("ImageEmbedderMain -> Constructor -> Error : " + ex);
            }
        }
        imagesUpdated(fileSynchronizer.getLastUpdated());
    }

    private void imagesUpdated(Date time) {
        lastUpdated.setLabel("Last Updated : " + timeFormat.format(time));
        ResourceHelper.setResource("embedderlastupdated", time.getTime());
    }

    private void applyChanges() {
        try {
            EmbedderInfo embedderInfo = imageEmbedderWindow.getEmbedderInfo();
            fileSynchronizer.storeEmbedderInfo(embedderInfo);
        } catch (XMLStreamException | IOException ex) {
            ResourceHelper.errLog("ImageEmbedderMain -> applyChanges() -> Error : " + ex);
        }
    }

    private void setTrayIcons() {
        try {
            Image icon = ResourceHelper.getImageObject("icon.png");
            systemTray = SystemTray.getSystemTray();
            Image trayIconImage = icon.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            ResourceHelper.setResource("icon_18x18", trayIconImage);

            PopupMenu popup = new PopupMenu("Image Embedder");

            lastUpdated.setEnabled(false);
            popup.add(lastUpdated);

            MenuItem updateMenu = new MenuItem("Update Wallpaper");
            updateMenu.addActionListener(e -> update());
            popup.add(updateMenu);

            MenuItem settingsMenu = new MenuItem("Settings");
            settingsMenu.addActionListener(e -> showSettings());
            popup.add(settingsMenu);

            MenuItem exitMenu = new MenuItem("Exit");
            exitMenu.addActionListener(e -> close());
            popup.add(exitMenu);

            TrayIcon trayIcon = new TrayIcon(trayIconImage, "Image Embedder", popup);
            trayIcon.addActionListener(e -> showSettings());
            systemTray.add(trayIcon);
        } catch (Exception ex) {
            ResourceHelper.errLog("ImageEmbedderMain > setTrayIcons() > Cannot set notification icons");
        }
    }

    private void close() {
        systemTray.remove(trayIcon);
        if (imageEmbedderWindow.isVisible()) {
            imageEmbedderWindow.setVisible(false);
        }
        imageEmbedderWindow.dispose();
        System.exit(0);
    }

    private void showSettings() {
        imageEmbedderWindow.setVisible(true);
    }

    private void update() {
        EmbedderInfo embedderInfo = imageEmbedderWindow.getEmbedderInfo();
        fileSynchronizer.processImages(embedderInfo);
    }

    public static void main(String... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ImageEmbedderMain iem = new ImageEmbedderMain();
    }
}
