package types;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.ResourceHelper;

public abstract class EmbedType {

    private int width;
    private int height;
    private UnitType sizeUnitType;
    private int anchorX;
    private int anchorY;
    private int opacity;
    private final Name name;
    private int posx = 10;
    private int posy = 10;
    private UnitType posUnitType = UnitType.percent;

    protected Image embedTypeImage;

    public static final String[] DISPLAY_NAMES = {
        "Custom Image",
        "Weather Information",
        "Calendar"
    };

    public EmbedType(Name name) {
        this.name = name;
        resetCommonProperties();
    }

    public EmbedType(Name name, EmbedType embedtype) {
        this.name = name;
        resetCommonProperties(embedtype);
    }

    public EmbedType(Name name, int width, int height, UnitType sizeUnitType,
            int anchorX, int anchorY, int opacity) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.sizeUnitType = sizeUnitType;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.opacity = opacity;
    }

    protected Image createEmptyImage(int parentWidth, int parentHeight) {
        int width, height;
        if (getSizeUnitType().equals(UnitType.pixels)) {
            width = getWidth();
            height = getHeight();
        } else {
            width = parentWidth * getWidth() / 100;
            height = parentHeight * getHeight() / 100;
        }
        Image image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return image;
    }

    public final void resetCommonProperties() {
        this.width = 200;
        this.height = 150;
        this.sizeUnitType = UnitType.pixels;
        this.anchorX = 0;
        this.anchorY = 0;
        this.opacity = 75;
        this.posx = 0;
        this.posy = 0;
        this.posUnitType = UnitType.percent;
    }

    public final void resetCommonProperties(EmbedType embedType) {
        if (embedType != null) {
            this.width = embedType.width;
            this.height = embedType.height;
            this.sizeUnitType = embedType.sizeUnitType;
            this.anchorX = embedType.anchorX;
            this.anchorY = embedType.anchorY;
            this.opacity = embedType.opacity;
            this.posx = embedType.posx;
            this.posy = embedType.posy;
            this.posUnitType = embedType.posUnitType;
        } else {
            resetCommonProperties();
        }
    }

    public abstract void resetOnlyMainProperties(EmbedType embedType);

    public abstract void reset();
    
    public void resetFromEmbedType(EmbedType embedtype) {
        if (embedtype != null) {
            resetCommonProperties(embedtype);
            resetOnlyMainProperties(embedtype);
        }
    }

    public abstract EmbedType createNewEmbedType();

    public void writeXMLElement(XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement("embedtype");              //<embedtype ...>
        xsw.writeAttribute("name", name.toString());

        xsw.writeEmptyElement("position");               //<position ... />
        xsw.writeAttribute("x", "" + posx);
        xsw.writeAttribute("y", "" + posy);
        xsw.writeAttribute("unit", posUnitType.sign);

        xsw.writeEmptyElement("size");                   //<size ... />
        xsw.writeAttribute("width", "" + width);
        xsw.writeAttribute("height", "" + height);
        xsw.writeAttribute("unit", sizeUnitType.sign);

        xsw.writeEmptyElement("anchor");                 //<anchor ... />
        xsw.writeAttribute("x", "" + anchorX);
        xsw.writeAttribute("y", "" + anchorY);

        xsw.writeEmptyElement("opacity");
        xsw.writeAttribute("value", "" + opacity);

        xsw.writeStartElement("typedata");               //<typedata/>
        writeXMLElementInnerData(xsw);
        xsw.writeEndElement();                           //</typedata>

        xsw.writeEndElement();                           //</embedtype>
    }

    public static EmbedType createEmbedTypeFromXMLNode(Node node) {
        EmbedType embed = null;
        if (node.getNodeName().equals("embedtype")) {
            NamedNodeMap attrs = node.getAttributes();
            Node nameNode = attrs.getNamedItem("name");
            if (nameNode != null) {
                Name name = Name.valueOf(nameNode.getNodeValue());
                switch (name) {
                    case CustomImage:
                        embed = new EmbedTypeCustomImage();
                        break;
                    case Calendar:
                        embed = new EmbedTypeCalendar();
                        break;
                    case WeatherInfo:
                        embed = new EmbedTypeWeatherInfo();
                }
                embed.readXMLElement(node);
            }
        }
        return embed;
    }

    protected void readXMLElement(Node node) {
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
                        case "position":
                            setPosx(Integer.parseInt(nnm.getNamedItem("x").getNodeValue()));
                            setPosy(Integer.parseInt(nnm.getNamedItem("y").getNodeValue()));
                            setPosUnitType(UnitType.parseSign(nnm.getNamedItem("unit").getNodeValue()));
                            break;
                        case "size":
                            setWidth(Integer.parseInt(nnm.getNamedItem("width").getNodeValue()));
                            setHeight(Integer.parseInt(nnm.getNamedItem("height").getNodeValue()));
                            setSizeUnitType(UnitType.parseSign(nnm.getNamedItem("unit").getNodeValue()));
                            break;
                        case "anchor":
                            setAnchorX(Integer.parseInt(nnm.getNamedItem("x").getNodeValue()));
                            setAnchorY(Integer.parseInt(nnm.getNamedItem("y").getNodeValue()));
                            break;
                        case "opacity":
                            setOpacity(Integer.parseInt(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "typedata":
                            readXMLElementInnerData(n);
                            break;
                    }
                } catch (Exception ex) {
                    ResourceHelper.errLog("EmbedType > readXMLElement(Node) > Error : " + ex);
                }
            }
        }
    }

    protected abstract void writeXMLElementInnerData(XMLStreamWriter xsw) throws XMLStreamException;

    protected abstract void readXMLElementInnerData(Node node);

    public abstract Image createNewImage(int parentWidth, int parentHeight);

    public abstract Image createSampleImage(int parentWidth, int parentHeight);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmbedType) {
            if (obj != null) {
                EmbedType et = (EmbedType) obj;
                if (anchorX == et.anchorX && anchorY == et.anchorY && height == et.height
                        && width == et.width && name == et.name && posx == et.posx && posy == et.posy
                        && sizeUnitType == et.sizeUnitType) {
                    return true;
                }
            }
        }
        return super.equals(obj);
    }

    public Image getImage() {
        return embedTypeImage;
    }

    /**
     * @return the name
     */
    public Name getName() {
        return name;
    }

    /**
     * @return the posx
     */
    public int getPosx() {
        return posx;
    }

    /**
     * @param posx the posx to set
     */
    public void setPosx(int posx) {
        this.posx = posx;
    }

    /**
     * @return the posy
     */
    public int getPosy() {
        return posy;
    }

    /**
     * @param posy the posy to set
     */
    public void setPosy(int posy) {
        this.posy = posy;
    }

    /**
     * @return the posUnitType
     */
    public UnitType getPosUnitType() {
        return posUnitType;
    }

    /**
     * @param posUnitType the posUnitType to set
     */
    public void setPosUnitType(UnitType posUnitType) {
        this.posUnitType = posUnitType;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the sizeUnitType
     */
    public UnitType getSizeUnitType() {
        return sizeUnitType;
    }

    /**
     * @param sizeUnitType the sizeUnitType to set
     */
    public void setSizeUnitType(UnitType sizeUnitType) {
        this.sizeUnitType = sizeUnitType;
    }

    /**
     * @return the anchorX
     */
    public int getAnchorX() {
        return anchorX;
    }

    /**
     * @param anchorX the anchorX to set
     */
    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
    }

    /**
     * @return the anchorY
     */
    public int getAnchorY() {
        return anchorY;
    }

    /**
     * @param anchorY the anchorY to set
     */
    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
    }

    /**
     * @return the opacity
     */
    public int getOpacity() {
        return opacity;
    }

    /**
     * @param opacity the opacity to set
     */
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public enum Name {

        CustomImage(DISPLAY_NAMES[0]),
        WeatherInfo(DISPLAY_NAMES[1]),
        Calendar(DISPLAY_NAMES[2]);
        final String DISPLAY_NAME;

        Name(String value) {
            if (Arrays.stream(DISPLAY_NAMES).anyMatch(nm -> value.equals(nm))) {
                this.DISPLAY_NAME = value;
            } else {
                this.DISPLAY_NAME = DISPLAY_NAMES[0];
            }
        }

        public static Name parse(String displayName) {
            for (Name name : Name.values()) {
                if (name.DISPLAY_NAME.equalsIgnoreCase(displayName)) {
                    return name;
                }
            }
            return CustomImage;
        }
    }
}
