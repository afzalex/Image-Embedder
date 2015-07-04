package imageembedder;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import resources.ResourceHelper;

public class WinSettings {

    private Rectangle desktopBounds;
    private String wallpaperSource;
    private String userProfileLocation;

    static {
        try {
            System.load(new File("lib\\native\\winCommunicator.dll").getCanonicalPath());
            //System.load("E:\\documents\\Afzal\\NetBeans\\ImageEmbedder\\src\\extra\\winCommunicator.dll");
        } catch (Exception | UnsatisfiedLinkError ex) {
            ResourceHelper.logIntoSystem("WinSettings > static block > Cannot link native library > Severe Error : " + ex);
        }
    }

    public native void broadcastChange();

    public void nativeBroadcastChange() {
        try {
            broadcastChange();
        } catch (UnsatisfiedLinkError ule) {
            ResourceHelper.errLog("WinSettings > nativeBroadcastChange() > Error : " + ule);
        }
    }

    public WinSettings() {
        refresh();
    }

    public final void refresh() {
        desktopBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        userProfileLocation = System.getProperty("user.home");
    }

    public final void refreshRegistries() throws RegistryException {
        try {
            wallpaperSource = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
                    "Software\\Microsoft\\Internet Explorer\\Desktop\\General", "WallpaperSource");
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            throw new RegistryException(ex);
        }
    }

    /**
     * @return the desktopBounds
     */
    public Rectangle getDesktopBounds() {
        return desktopBounds;
    }

    /**
     * @return the wallpaperSource
     */
    public String getWallpaperSource() {
        return wallpaperSource;
    }

    /**
     * @return the userProfileLocation
     */
    public String getUserProfileLocation() {
        return userProfileLocation;
    }

    public class RegistryException extends Exception {

        public RegistryException(Exception ex) {
            super(ex.getMessage());
        }
    }
}
