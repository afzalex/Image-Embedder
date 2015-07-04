package imageembedder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
import resources.ResourceHelper;
import static resources.ResourceHelper.winSettings;
import types.EmbedType;
import types.UnitType;

public class FileSynchronizer {

    private String userWallpaperSource;
    private Rectangle desktopBounds;
    private final TreeMap<String, File> backupFiles = new TreeMap<>();
    private final TreeMap<String, Image> loadedBackupFiles = new TreeMap<>();
    private final TreeMap<String, File> originalFiles = new TreeMap<>();
    private Date lastOpened = new Date();
    private Date lastUpdated = new Date();

    public FileSynchronizer() {
        resetSyncFile(null);
    }

    public final void reset() throws WinSettings.RegistryException, IOException {
        ResourceHelper.resetSettings();
        userWallpaperSource = winSettings.getWallpaperSource();
        loadedBackupFiles.clear();
        desktopBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        ResourceHelper.setResource("DesktopBounds", desktopBounds);
        backup();
    }

    public final void resetSyncFile(File xmlfile) {
        ResourceHelper.SYNC_FILE = xmlfile == null
                ? new File(ResourceHelper.DEFAULT_INFO_FILE_NAME) : xmlfile;
    }

    public final void backup() throws IOException {
        resetOriginalFiles();
        resetBackupFiles();

        String orgnlFileName;
        File orgnlFile;
        for (Entry<String, File> e : originalFiles.entrySet()) {
            orgnlFileName = e.getKey();
            orgnlFile = e.getValue();
            if (!backupFiles.containsKey(orgnlFileName)) {
                Path from = Paths.get(orgnlFile.getPath());
                Path to = Paths.get(ResourceHelper.BACKUP_DIRECTORY.getPath(),
                        orgnlFileName + ResourceHelper.BACKUP_FILE_EXTENSION);
                Files.copy(from, to, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }

        resetBackupFiles();
    }

    public final void restore() throws IOException {

        resetOriginalFiles();
        resetBackupFiles();

        String s;
        File backupFile;
        for (Entry<String, File> e : backupFiles.entrySet()) {
            s = e.getKey();
            backupFile = e.getValue();
            if (originalFiles.containsKey(s)) {
                Path from = Paths.get(backupFile.getPath());
                Path to = Paths.get(originalFiles.get(s).getPath());
                Files.copy(from, to, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void storeEmbedderInfo(EmbedderInfo embedderInfo) throws XMLStreamException, FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(ResourceHelper.SYNC_FILE);
        embedderInfo.writeXMLData(fos);
        fos.close();
    }

    public void storeEmbedderInfo() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
        EmbedderInfo ei = loadEmbedderInfo();
        storeEmbedderInfo(ei);
    }

    public EmbedderInfo loadEmbedderInfo() throws ParserConfigurationException, SAXException, IOException {
        EmbedderInfo embedderInfo;
        try (FileInputStream fis = new FileInputStream(ResourceHelper.SYNC_FILE)) {
            embedderInfo = new EmbedderInfo() {

                @Override
                protected void lastOpenedRecieved(String dat) {
                    try {
                        lastOpened = new Date(Long.parseLong(dat));
                    } catch (NumberFormatException | NullPointerException ex) {
                        ResourceHelper.errLog("FileSynchronizer > loadEmbedderInfo() > embedderlastopened element not found");
                        lastOpened = new Date(0);
                    }
                }

                @Override
                protected void lastUpdatedRecieved(String dat) {
                    try {
                        lastUpdated = new Date(Long.parseLong(dat));
                    } catch (NumberFormatException | NullPointerException ex) {
                        ResourceHelper.errLog("FileSynchoronizer > loadEmbedderInfo() > embedderlastupdated element not found");
                        lastUpdated = new Date(0);
                    }
                }
            };
            embedderInfo.readXMLData(fis);
        } catch (FileNotFoundException ex) {
            embedderInfo = new EmbedderInfo();
        }
        return embedderInfo;
    }

    public void processImages(EmbedderInfo embedderInfo) {
        synchronized (processor) {
            processor.setEmbedderInfo(embedderInfo);
            processor.wait = false;
            processor.notify();
        }
    }

    public void refreshDesktop() {
        winSettings.nativeBroadcastChange();
    }

    private BufferedImage createEmptyWallImage(Image wallImage, CoverStyle style, int emptyWidth, int emptyHeight) {
        BufferedImage emptyImage = new BufferedImage(emptyWidth, emptyHeight, BufferedImage.TYPE_INT_RGB);
        try {
            Graphics2D g2d = (Graphics2D) emptyImage.getGraphics();
            Image toDraw = wallImage;
            int wallWidth = toDraw.getWidth(null);
            int wallHeight = toDraw.getHeight(null);
            switch (style) {
                case FILL: {
                    float s1 = (float) emptyWidth / wallWidth;
                    float s2 = (float) emptyHeight / wallHeight;
                    float s = s1 > s2 ? s1 : s2;
                    int newWidth = (int) (s * wallWidth);
                    toDraw = toDraw.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                }
                break;
                case FIT: {
                    float s1 = (float) emptyWidth / wallWidth;
                    float s2 = (float) emptyHeight / wallHeight;
                    float s = s1 < s2 ? s1 : s2;
                    int newWidth = (int) (s * wallWidth);
                    toDraw = toDraw.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                }
                break;
                default: {
                    toDraw = toDraw.getScaledInstance(emptyWidth, emptyHeight, Image.SCALE_SMOOTH);
                }
            }
            wallWidth = toDraw.getWidth(null);
            wallHeight = toDraw.getHeight(null);
            int locx, locy;
            locx = (emptyWidth - wallWidth) / 2;
            locy = (emptyHeight - wallHeight) / 2;
            g2d.drawImage(toDraw, locx, locy, null);
            g2d.dispose();
        } catch (NullPointerException npe) {
            ResourceHelper.errLog("FileSynchronizer > createEmptyWallImage(Image "
                    + wallImage+", CoverStyle style, int width, int height) > Error : " + npe);
        }
        return emptyImage;
    }

    private Image createEmbedImageMask(ArrayList<EmbedType> embeds, int parentWidth, int parentHeight) {
        int posx, posy, imgw, imgh;
        Image image;

        Image embedImage = new BufferedImage(parentWidth, parentHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D embedImageGraphics = (Graphics2D) embedImage.getGraphics();

        for (EmbedType et : embeds) {
            try {
                image = et.createNewImage(parentWidth, parentHeight);
                imgw = image.getWidth(null);
                imgh = image.getHeight(null);

                posx = et.getPosx();
                posy = et.getPosy();

                if (et.getPosUnitType() == UnitType.percent) {
                    posx = parentWidth * posx / 100;
                    posy = parentHeight * posy / 100;
                }

                posx = posx - imgw * et.getAnchorX() / 100;
                posy = posy - imgh * et.getAnchorY() / 100;

                embedImageGraphics.drawImage(image, posx, posy, null);
            } catch (Exception ex) {
                ResourceHelper.errLog("FileSynchronizer -> processImages(...) -> Error : " + ex);
            }
        }
        embedImageGraphics.dispose();
        return embedImage;
    }

    private void resetBackupFiles() {
        backupFiles.clear();
        Arrays.stream(ResourceHelper.BACKUP_DIRECTORY.listFiles((f, s) -> {
            if (s.endsWith(ResourceHelper.BACKUP_FILE_EXTENSION)) {
                return originalFiles.containsKey(s.substring(0, s.lastIndexOf(ResourceHelper.BACKUP_FILE_EXTENSION)));
            }
            return false;
        })).forEach(f -> {
            String n = f.getName();
            backupFiles.put(n.substring(0, n.lastIndexOf(ResourceHelper.BACKUP_FILE_EXTENSION)), f);
        });
    }

    private void resetOriginalFiles() {
        ResourceHelper.ORIGINAL_DIRECTORY = new File(userWallpaperSource);
        ResourceHelper.ORIGINAL_DIRECTORY = ResourceHelper.ORIGINAL_DIRECTORY.getParentFile();
        originalFiles.clear();
        Arrays.stream(ResourceHelper.ORIGINAL_DIRECTORY
                .listFiles((f, s) -> s.matches("(?i).+\\.((jp(e?)g)|(png))$")))
                .forEach(f -> originalFiles.put(f.getName(), f));
    }

    public void deleteEmbedderInfo() throws IOException {
        ResourceHelper.SYNC_FILE.delete();
    }

    protected void imagesUpdated() {
    }

    Processor processor = new Processor();

    /**
     * @return the lastOpened
     */
    public Date getLastOpened() {
        return lastOpened;
    }

    /**
     * @return the lastUpdated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    private class Processor implements Runnable {

        Processor() {
            new Thread(this).start();
        }

        transient boolean cont = true;
        transient boolean wait = true;

        EmbedderInfo embedderInfo;

        public void setEmbedderInfo(EmbedderInfo embedderInfo) {
            this.embedderInfo = embedderInfo;
        }

        @Override
        public void run() {
            while (cont) {
                synchronized (this) {
                    while (wait) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FileSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                wait = true;
                int parentWidth = desktopBounds.width, parentHeight = desktopBounds.height;
                ArrayList<EmbedType> embeds = embedderInfo.getEmbeds();

                String k;
                for (Entry<String, File> e : backupFiles.entrySet()) {
                    k = e.getKey();
                    if (originalFiles.containsKey(k)) {
                        if (!loadedBackupFiles.containsKey(k)) {
                            Image image = ResourceHelper.getImageObject(e.getValue());
                            loadedBackupFiles.put(k, image);
                        }
                    }
                }

                Image embedImageMask = createEmbedImageMask(embeds, parentWidth, parentHeight);

                CoverStyle style = CoverStyle.FILL;
                for (Entry<String, Image> e : loadedBackupFiles.entrySet()) {
                    Image imageToWrite = createEmptyWallImage(e.getValue(), style, parentWidth, parentHeight);
                    Graphics g = imageToWrite.getGraphics();
                    g.drawImage(embedImageMask, 0, 0, null);
                    g.dispose();

                    String name = e.getKey();
                    File file = originalFiles.get(e.getKey());
                    try {
                        switch (name.substring(name.lastIndexOf('.'))) {
                            case ".jpeg":
                                ImageIO.write((RenderedImage) imageToWrite, "jpeg", file);
                                break;
                            case ".png":
                                ImageIO.write((RenderedImage) imageToWrite, "png", file);
                                break;
                            default:
                                ImageIO.write((RenderedImage) imageToWrite, "jpg", file);
                        }
                    } catch (IOException ex) {
                        ResourceHelper.errLog("FileSynchronizer > Processor > run() > Error : " + ex);
                    }
                }
                winSettings.nativeBroadcastChange();
                imagesUpdated();
            }
        }
    }
}
