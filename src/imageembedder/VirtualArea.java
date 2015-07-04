package imageembedder;

import java.awt.Point;

public class VirtualArea {

    private int realWidth;
    private int virtualWidth;
    private int realHeight;
    private int virtualHeight;
    private float widthRatio;
    private float heightRatio;
    
    public VirtualArea() {
        this(0, 0, 0, 0);
    }

    public VirtualArea(int realWidth, int realHeight, int virtualWidth, int virtualHeight) {
        setAreas(realWidth, realHeight, virtualWidth, virtualHeight);
    }

    private void setAreas(int realWidth, int realHeight, int virtualWidth, int virtualHeight) {
        this.realWidth = realWidth;
        this.virtualWidth = virtualWidth;
        this.realHeight = realHeight;
        this.virtualHeight = virtualHeight;
        resetRatios();
    }
    
    public void setVirtualSize(int virtualWidth, int virtualHeight){
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        resetRatios();
    }
    
    public void setRealSize(int realWidth, int realHeight){
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        resetRatios();
    }

    private void resetRatios() {
        widthRatio = (float) getRealWidth() / getVirtualWidth();
        heightRatio = (float) getRealHeight() / getVirtualHeight();
    }

    public int realToVirtualX(int realX) {
        return (int) (realX / getWidthRatio());
    }

    public float realToVirtualX(float realX) {
        return realX / getWidthRatio();
    }

    public int realToVirtualY(int realY) {
        return (int) (realY / getHeightRatio());
    }

    public float realToVirtualY(float realY) {
        return realY / getHeightRatio();
    }

    public Point realToVirtualPoint(Point realPoint) {
        return new Point(realToVirtualX(realPoint.x), realToVirtualY(realPoint.y));
    }

    public int virtualToRealX(int virtualX) {
        return (int) (virtualX * getWidthRatio());
    }

    public float virtualToRealX(float virtualX) {
        return virtualX * getWidthRatio();
    }

    public int virtualToRealY(int virtualY) {
        return (int) (virtualY * getHeightRatio());
    }

    public float virtualToRealY(float virtualY) {
        return virtualY * getHeightRatio();
    }

    public Point virtualToRealPoint(Point virtualPoint) {
        return new Point(virtualToRealX(virtualPoint.x), virtualToRealY(virtualPoint.y));
    }

    /**
     * @return the realWidth
     */
    public int getRealWidth() {
        return realWidth;
    }

    /**
     * @return the virtualWidth
     */
    public int getVirtualWidth() {
        return virtualWidth;
    }

    /**
     * @return the realHeight
     */
    public int getRealHeight() {
        return realHeight;
    }

    /**
     * @return the virtualHeight
     */
    public int getVirtualHeight() {
        return virtualHeight;
    }

    /**
     * @return the widthRatio
     */
    public float getWidthRatio() {
        return widthRatio;
    }

    /**
     * @return the heightRatio
     */
    public float getHeightRatio() {
        return heightRatio;
    }

}
