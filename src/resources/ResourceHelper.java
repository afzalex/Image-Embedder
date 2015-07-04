package resources;

import imageembedder.WinSettings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ResourceHelper {

    public static WinSettings winSettings;
    public static final String APPDATA_DIRECTORY = "data";

    public static final String BACKUP_FILE_EXTENSION = ".original";
    public static final String DEFAULT_INFO_FILE_NAME = "default.xml";
    public static final String LOG_FILE_NAME = "log.dat";
    public static File SYNC_FILE = new File(DEFAULT_INFO_FILE_NAME);
    public static File LOG_FILE = new File(LOG_FILE_NAME);
    public static File BACKUP_DIRECTORY, ORIGINAL_DIRECTORY, IMAGEEMBEDDER_DIRECTORY;
    private static final TreeMap<String, URL> urlMap = new TreeMap<>();
    private static final HashMap<URL, Object> objectMap = new HashMap<>();
    private static final HashMap<String, Object> resPool = new HashMap<>();
    private static final TreeMap<String, ResourceBundle> resBundlePool = new TreeMap<>();

    static {
        resPool.put("XMLOutputFactory", javax.xml.stream.XMLOutputFactory.newInstance());
        resPool.put("DocumentBuilderFactory", javax.xml.parsers.DocumentBuilderFactory.newInstance());
        resetSettings();
        
        System.out.println("ImageEmbedder Directory : " + IMAGEEMBEDDER_DIRECTORY.getAbsolutePath());
    }

    public static void resetSettings() {
        String postLoc, locName;
        winSettings = new WinSettings();
        try {
            winSettings.refreshRegistries();
        } catch (WinSettings.RegistryException ex) {
            logIntoSystem("ResourceHelper > resetSettings > Error : " + ex);
        }
        ResourceBundle bundle = ResourceHelper.getResourceBundle("res");
        postLoc = bundle.getString("PostLocation");
        locName = bundle.getString("LocationName");
        String home = winSettings.getUserProfileLocation();
        File file = new File(home, postLoc);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(file, locName);
        if (!file.exists()) {
            file.mkdir();
        }

        File appdataDirectory = new File(file, APPDATA_DIRECTORY);
        if (!appdataDirectory.exists()) {
            appdataDirectory.mkdir();
        }
        ResourceHelper.setResource("APPDATA_DIRECTORY", appdataDirectory);

        BACKUP_DIRECTORY = new File(file, "BackupImages");
        if (!BACKUP_DIRECTORY.exists()) {
            BACKUP_DIRECTORY.mkdir();
        }

        IMAGEEMBEDDER_DIRECTORY = file;
        SYNC_FILE = new File(file, SYNC_FILE.getName());
        LOG_FILE = new File(file, LOG_FILE.getName());
    }

    public static URL getResourceURL(String resourceName) {
        if (urlMap.containsKey(resourceName)) {
            return urlMap.get(resourceName);
        }
        urlMap.put(resourceName, ResourceHelper.class.getResource(resourceName));
        return urlMap.get(resourceName);
    }

    public static Image getImageObject(URL url) {
        Image img;
        try {
            if (objectMap.containsKey(url)) {
                return (Image) objectMap.get(url);
            }
            objectMap.put(url, (Object) javax.imageio.ImageIO.read(url));
            img = (Image) objectMap.get(url);
        } catch (Exception ex) {
            ResourceHelper.errLog("ResourceHelper > getImageObject(URL " + url + ") > Error in reading image : " + ex);
            img = null;
        }
        return img;
    }

    public static Image getImageObject(File file) {
        try {
            return getImageObject(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            ResourceHelper.errLog("ResourceHelper > getImageObject(URL) > Error in reading image : " + ex);
            return null;
        }
    }

    public static Image getImageObject(String resourceName) {
        URL url = getResourceURL(resourceName);
        return getImageObject(url);
    }

    public static <T> void setResource(String key, T obj) {
        resPool.put(key, obj);
    }

    public static <T> T getResource(String key) {
        return (T) resPool.get(key);
    }

    public static Image getCroppedImage(Image img, Component comp, Dimension size) {
        return getCroppedImage(img, comp, size.width, size.height);
    }

    public static Image getCroppedImage(Image img, Component comp, int width, int height) {
        CropImageFilter cif = new CropImageFilter(0, 0, width, height);
        FilteredImageSource fis = new FilteredImageSource(img.getSource(), cif);
        return comp.createImage(fis);
    }

    public static ResourceBundle getResourceBundle(String name) {
        ResourceBundle res;
        if (resBundlePool.containsKey(name)) {
            res = resBundlePool.get(name);
        } else {
            try {
                res = ResourceBundle.getBundle("resources/" + name);
                resBundlePool.put(name, res);
            } catch (MissingResourceException | NullPointerException ex) {
                ResourceHelper.errLog("ResourceHelper > Error in retrieving ResourceBundle : " + ex);
                res = null;
            }
        }
        return res;
    }
    private static final DateFormat logDateFormat = new SimpleDateFormat("dd/MM/YY hh:mm:ss.SSS");

    public static void errLog(String str) {
        logIntoSystem(str);
        logIntoFile(str);
    }

    public static void logIntoSystem(String str) {
        System.err.println(str);
    }

    public static void logIntoFile(String str) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(LOG_FILE, true))) {
            pw.printf("%s > %s\n", logDateFormat.format(new Date()), str);
        } catch (FileNotFoundException ex) {
            System.err.println("ResourceHelper > log(String) > Error : " + ex);
        }
    }
}
