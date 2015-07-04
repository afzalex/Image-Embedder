package types;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.ResourceHelper;

public class EmbedTypeCalendar extends EmbedType {
    
    private Color background;
    private Color daysBack1;
    private Color daysBack2;
    private Color monthBack;
    private Color monthBack2;
    private Color monthFore;
    private Color daysFore;
    private Color todayBack;
    private Color todayFore;
    private boolean backgroundVisible;
    private boolean monthBackVisible;
    private boolean daysBackVisible;
    
    private static final Color DEFAULT_COLOR_BACKGROUND = new Color(153, 153, 153, 153);
    private static final Color DEFAULT_COLOR_DAYS_BACK_1 = Color.BLACK;
    private static final Color DEFAULT_COLOR_DAYS_BACK_2 = Color.WHITE;
    private static final Color DEFAULT_COLOR_MONTH_BACK = new Color(22, 153, 153);
    private static final Color DEFAULT_COLOR_MONTH_BACK_2 = Color.WHITE;
    private static final Color DEFAULT_COLOR_DAYS_FORE = Color.WHITE;
    private static final Color DEFAULT_COLOR_MONTH_FORE = Color.BLACK;
    private static final Color DEFAULT_COLOR_TODAY_BACK = DEFAULT_COLOR_MONTH_BACK;
    private static final Color DEFAULT_COLOR_TODAY_FORE = Color.BLACK;
    
    public EmbedTypeCalendar() {
        super(EmbedType.Name.Calendar);
        reset();
    }
    
    @Override
    public Image createNewImage(int parentWidth, int parentHeight) {
        Image image = createEmptyImage(parentWidth, parentHeight);
        try {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            float opacity = (float) getOpacity() / 100;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            // Create image
            {
                Dimension dimt = new Dimension(700, 600);
                Image imaget = new BufferedImage(dimt.width, dimt.height,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2dt = (Graphics2D) imaget.getGraphics();
                g2dt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2dt.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_NORMALIZE);
                
                GregorianCalendar cal = new GregorianCalendar();
                int todayDate = cal.get(Calendar.DAY_OF_MONTH);
                int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                int dayStart = cal.get(Calendar.DAY_OF_WEEK);
                
                float wunit = (float) (dimt.getWidth() / 7);
                float hunit = (float) (dimt.getHeight() / 8);
                int gap = 3;
                Color invisible = new Color(0, 0, 0, 0);
                FontMetrics fm;
                String str;
                Rectangle2D strbnds;
                Rectangle arbnds;
                
                g2dt.setColor(invisible);
                if (isBackgroundVisible()) {
                    g2dt.setColor(getBackground());
                    g2dt.fillRect(0, 0, dimt.width, dimt.height);
                }
                
                arbnds = new Rectangle(gap, gap, dimt.width - 2 * gap, (int) (hunit - 2 * gap));
                if (isMonthBackVisible()) {
                    g2dt.setPaint(new GradientPaint(0, arbnds.y, getMonthBack(),
                            0, arbnds.y + arbnds.height, getMonthBack2()));
                    g2dt.fillRect(arbnds.x, arbnds.y, arbnds.width, arbnds.height);
                }
                
                g2dt.setColor(getMonthFore());
                g2dt.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
                fm = g2dt.getFontMetrics();
                str = cal.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.ENGLISH);
                strbnds = fm.getStringBounds(str, g2dt);
                g2dt.drawString(str, (float) (arbnds.x + (arbnds.width - strbnds.getWidth()) / 2), (float) (arbnds.y + arbnds.height * 3 / 4));
                
                g2dt.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
                fm = g2dt.getFontMetrics();
                arbnds = new Rectangle((int) wunit - 2 * gap, (int) hunit - 2 * gap);
                arbnds.y = (int) (hunit + gap);
                g2dt.setColor(getDaysFore());
                Paint np = g2dt.getPaint();
                Paint gp = new GradientPaint(0, arbnds.y, getDaysBack1(),
                        0, arbnds.y + arbnds.height, getDaysBack2());
                for (int j = 0; j < 7; j++) {
                    cal.set(Calendar.DAY_OF_WEEK, j + 1);
                    arbnds.x = (int) (j * wunit + gap);
                    str = cal.getDisplayName(Calendar.DAY_OF_WEEK,
                            Calendar.SHORT_FORMAT, Locale.ENGLISH);
                    strbnds = fm.getStringBounds(str, g2dt);
                    if (isDaysBackVisible()) {
                        g2dt.setPaint(gp);
                        g2dt.fillRect(arbnds.x, arbnds.y, arbnds.width, arbnds.height);
                    }
                    
                    g2dt.setPaint(np);
                    g2dt.drawString(str, (float) (arbnds.x + (arbnds.width - strbnds.getWidth()) / 2),
                            (float) (arbnds.y + (arbnds.height + strbnds.getHeight() / 2) / 2));
                }
                cal.set(Calendar.DAY_OF_MONTH, 1);
                boolean istoday = false;
                Color temp = null;
                main:
                for (int i = 2; i < 8; i++) {
                    arbnds.y = (int) (i * hunit + gap);
                    gp = new GradientPaint(0, arbnds.y, getDaysBack1(),
                            0, arbnds.y + arbnds.height, getDaysBack2());
                    for (int j = 0; j < 7; j++) {
                        if (cal.get(Calendar.MONTH) != month) {
                            break main;
                        }
                        if (j + 1 == cal.get(Calendar.DAY_OF_WEEK)) {
                            istoday = cal.get(Calendar.DAY_OF_MONTH) == todayDate;
                            arbnds.x = (int) (j * wunit + gap);
                            str = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
                            strbnds = fm.getStringBounds(str, g2dt);
                            if (isDaysBackVisible()) {
                                if (istoday) {
                                    g2dt.setPaint(new GradientPaint(0, arbnds.y, getTodayBack(),
                                            0, arbnds.y + arbnds.height, getDaysBack2()));
                                } else {
                                    g2dt.setPaint(gp);
                                }
                                g2dt.fillRect(arbnds.x, arbnds.y, arbnds.width, arbnds.height);
                            }
                            g2dt.setPaint(np);
                            if (istoday) {
                                Color t = g2dt.getColor();
                                g2dt.setColor(getTodayFore());
                                g2dt.drawString(str, (float) (arbnds.x + (arbnds.width - strbnds.getWidth()) / 2),
                                        (float) (arbnds.y + (arbnds.height + strbnds.getHeight() / 2) / 2));
                                g2dt.setColor(t);
                            } else {
                                g2dt.drawString(str, (float) (arbnds.x + (arbnds.width - strbnds.getWidth()) / 2),
                                        (float) (arbnds.y + (arbnds.height + strbnds.getHeight() / 2) / 2));
                            }
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                }
                g2dt.dispose();
                
                float s1 = (float) width / dimt.width;
                float s2 = (float) height / dimt.height;
                float s = s1 < s2 ? s1 : s2;
                int newWidth = (int) (s * dimt.width);
                imaget = imaget.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                dimt.width = imaget.getWidth(null);
                dimt.height = imaget.getHeight(null);
                int locx, locy;
                locx = (width - dimt.width) / 2;
                locy = (height - dimt.height) / 2;
                g2d.drawImage(imaget, locx, locy, null);
            }
            
            g2d.dispose();
        } catch (Exception ex) {
            ResourceHelper.errLog("EmbedTypeCalendar > createNewImage(int width, int height) "
                    + "> Error : " + ex);
        }
        embedTypeImage = image;
        return image;
    }
    
    @Override
    public Image createSampleImage(int parentWidth, int parentHeight) {
        if (embedTypeImage == null) {
            createNewImage(parentWidth, parentHeight);
        }
        return embedTypeImage;
    }
    
    @Override
    public final void reset() {
        super.resetCommonProperties();
        background = DEFAULT_COLOR_BACKGROUND;
        daysBack1 = DEFAULT_COLOR_DAYS_BACK_1;
        daysBack2 = DEFAULT_COLOR_DAYS_BACK_2;
        monthBack = DEFAULT_COLOR_MONTH_BACK;
        monthBack2 = DEFAULT_COLOR_MONTH_BACK_2;
        monthFore = DEFAULT_COLOR_MONTH_FORE;
        daysFore = DEFAULT_COLOR_DAYS_FORE;
        todayBack = DEFAULT_COLOR_TODAY_BACK;
        todayFore = DEFAULT_COLOR_TODAY_FORE;
        backgroundVisible = true;
        monthBackVisible = true;
        daysBackVisible = true;
    }
    
    @Override
    public void resetOnlyMainProperties(EmbedType embedType) {
        if (embedType.getName().equals(getName())) {
            EmbedTypeCalendar etci = (EmbedTypeCalendar) embedType;
            background = etci.background;
            daysBack1 = etci.daysBack1;
            daysBack2 = etci.daysBack2;
            monthBack = etci.monthBack;
            monthBack2 = etci.monthBack2;
            monthFore = etci.monthFore;
            daysFore = etci.daysFore;
            todayBack = etci.todayBack;
            todayFore = etci.todayFore;
            backgroundVisible = etci.backgroundVisible;
            monthBackVisible = etci.monthBackVisible;
            daysBackVisible = etci.daysBackVisible;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (obj instanceof EmbedTypeCustomImage && obj != null) {
                EmbedTypeCalendar embed = (EmbedTypeCalendar) obj;
                return embed.background.equals(background) && embed.daysBack1.equals(daysBack1)
                        && embed.daysBack2.equals(daysBack2) && embed.monthBack.equals(monthBack)
                        && embed.monthFore.equals(monthFore) && embed.daysFore.equals(daysFore)
                        && embed.backgroundVisible == backgroundVisible
                        && embed.monthBackVisible == monthBackVisible
                        && embed.daysBackVisible == daysBackVisible;
            }
        }
        return false;
    }
    
    @Override
    protected void writeXMLElementInnerData(XMLStreamWriter xsw) throws XMLStreamException {
        
        xsw.writeEmptyElement("background");                                    //<background ... />
        xsw.writeAttribute("value", Integer.toString(background.getRGB()));
        
        xsw.writeEmptyElement("backvisibility");                                //<backvisibility ... />
        xsw.writeAttribute("value", Boolean.toString(backgroundVisible));
        
        xsw.writeEmptyElement("monthback");                                     //<monthback ... />
        xsw.writeAttribute("value", Integer.toString(monthBack.getRGB()));
        
        xsw.writeEmptyElement("monthback2");                                    //<monthback2 ... />
        xsw.writeAttribute("value", Integer.toString(monthBack2.getRGB()));
        
        xsw.writeEmptyElement("monthvisibility");                               //<monthvisibility ... />
        xsw.writeAttribute("value", Boolean.toString(monthBackVisible));
        
        xsw.writeEmptyElement("monthfore");                                     //<monthfore ... />
        xsw.writeAttribute("value", Integer.toString(monthFore.getRGB()));
        
        xsw.writeEmptyElement("daysback1");                                     //<daysback1 ... />
        xsw.writeAttribute("value", Integer.toString(daysBack1.getRGB()));
        
        xsw.writeEmptyElement("daysback2");                                     //<daysback2 ... />
        xsw.writeAttribute("value", Integer.toString(daysBack2.getRGB()));
        
        xsw.writeEmptyElement("daysvisibility");                                //<daysvisibility ... />
        xsw.writeAttribute("value", Boolean.toString(daysBackVisible));
        
        xsw.writeEmptyElement("daysfore");                                      //<daysfore ... />
        xsw.writeAttribute("value", Integer.toString(daysFore.getRGB()));
        
        xsw.writeEmptyElement("todayback");                                     //<todayback ... />
        xsw.writeAttribute("value", Integer.toString(todayBack.getRGB()));
        
        xsw.writeEmptyElement("todayfore");                                     //<todayfore ... />
        xsw.writeAttribute("value", Integer.toString(todayFore.getRGB()));
    }
    
    @Override
    protected void readXMLElementInnerData(Node node) {
        Node n;
        NamedNodeMap nnm;
        String nm;
        
        NodeList nodes = node.getChildNodes();
        int l = nodes.getLength();
        
        for (int i = 0; i < l; i++) {
            n = nodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    nm = n.getNodeName();
                    nnm = n.getAttributes();
                    switch (nm) {
                        case "background":
                            background = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "backvisibility":
                            backgroundVisible = Boolean.parseBoolean(nnm.getNamedItem("value").getNodeValue());
                            break;
                        case "monthback":
                            monthBack = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "monthback2":
                            monthBack2 = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "monthvisibility":
                            monthBackVisible = Boolean.parseBoolean(nnm.getNamedItem("value").getNodeValue());
                            break;
                        case "monthfore":
                            monthFore = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "daysback1":
                            daysBack1 = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "daysback2":
                            daysBack2 = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "daysvisibility":
                            daysBackVisible = Boolean.parseBoolean(nnm.getNamedItem("value").getNodeValue());
                            break;
                        case "daysfore":
                            daysFore = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "todayback":
                            todayBack = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "todayfore":
                            todayFore = new Color(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                    }
                } catch (Exception ex) {
                    ResourceHelper.errLog("EmbedTypeCalendar > readXMLElementInnerData(Node) "
                            + "> Error : " + ex);
                }
            }
        }
    }
    
    @Override
    public EmbedType createNewEmbedType() {
        EmbedTypeCustomImage embedType = new EmbedTypeCustomImage();
        embedType.resetFromEmbedType(this);
        return embedType;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setBackground(Color background) {
        this.background = background;
    }
    
    public Color getDaysBack1() {
        return daysBack1;
    }
    
    public void setDaysBack1(Color daysBack1) {
        this.daysBack1 = daysBack1;
    }
    
    public Color getDaysBack2() {
        return daysBack2;
    }
    
    public void setDaysBack2(Color daysBack2) {
        this.daysBack2 = daysBack2;
    }
    
    public Color getMonthBack() {
        return monthBack;
    }
    
    public void setMonthBack(Color monthBack) {
        this.monthBack = monthBack;
    }
    
    public Color getMonthFore() {
        return monthFore;
    }
    
    public void setMonthFore(Color monthFore) {
        this.monthFore = monthFore;
    }
    
    public Color getDaysFore() {
        return daysFore;
    }
    
    public void setDaysFore(Color daysFore) {
        this.daysFore = daysFore;
    }

    /**
     * @return the backgroundVisible
     */
    public boolean isBackgroundVisible() {
        return backgroundVisible;
    }

    /**
     * @param backgroundVisible the backgroundVisible to set
     */
    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }

    /**
     * @return the monthBackVisible
     */
    public boolean isMonthBackVisible() {
        return monthBackVisible;
    }

    /**
     * @param monthBackVisible the monthBackVisible to set
     */
    public void setMonthBackVisible(boolean monthBackVisible) {
        this.monthBackVisible = monthBackVisible;
    }

    /**
     * @return the daysBackVisible
     */
    public boolean isDaysBackVisible() {
        return daysBackVisible;
    }

    /**
     * @param daysBackVisible the daysBackVisible to set
     */
    public void setDaysBackVisible(boolean daysBackVisible) {
        this.daysBackVisible = daysBackVisible;
    }

    /**
     * @return the todayBack
     */
    public Color getTodayBack() {
        return todayBack;
    }

    /**
     * @param todayBack the todayBack to set
     */
    public void setTodayBack(Color todayBack) {
        this.todayBack = todayBack;
    }

    /**
     * @return the todayFore
     */
    public Color getTodayFore() {
        return todayFore;
    }

    /**
     * @param todayFore the todayFore to set
     */
    public void setTodayFore(Color todayFore) {
        this.todayFore = todayFore;
    }

    /**
     * @return the monthBack2
     */
    public Color getMonthBack2() {
        return monthBack2;
    }

    /**
     * @param monthBack2 the monthBack2 to set
     */
    public void setMonthBack2(Color monthBack2) {
        this.monthBack2 = monthBack2;
    }
}
