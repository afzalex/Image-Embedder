package types;

import imageembedder.CoverStyle;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.ResourceHelper;

public class EmbedTypeCustomImage extends EmbedType {

    private CoverStyle covertype;
    private boolean doShuffle;
    private URL[] urls;

    public EmbedTypeCustomImage() {
        super(EmbedType.Name.CustomImage);
        reset();
    }

    private int counter = 0;
    private Random random = new Random();

    @Override
    public Image createNewImage(int parentWidth, int parentHeight) {
        Image image = createEmptyImage(parentWidth, parentHeight);
        try {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            float opacity = (float) getOpacity() / 100;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            URL[] urls = getUrls();
            if (urls != null && urls.length > 0) {
                Image todraw;
                if (doShuffle) {
                    todraw = ResourceHelper.getImageObject(urls[random.nextInt(urls.length)]);
                } else {
                    todraw = ResourceHelper.getImageObject(urls[counter++ % urls.length]);
                }
                if (todraw != null) {
                    int imgw = todraw.getWidth(null);
                    int imgh = todraw.getHeight(null);
                    switch (getCovertype()) {
                        case FILL: {
                            float s1 = (float) width / imgw;
                            float s2 = (float) height / imgh;
                            float s = s1 > s2 ? s1 : s2;
                            int newWidth = (int) (s * imgw);
                            todraw = todraw.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                        }
                        break;
                        case FIT: {
                            float s1 = (float) width / imgw;
                            float s2 = (float) height / imgh;
                            float s = s1 < s2 ? s1 : s2;
                            int newWidth = (int) (s * imgw);
                            todraw = todraw.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                        }
                        break;
                        default: {
                            todraw = todraw.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        }
                    }
                    imgw = todraw.getWidth(null);
                    imgh = todraw.getHeight(null);
                    int locx, locy;
                    locx = (width - imgw) / 2;
                    locy = (height - imgh) / 2;
                    g2d.drawImage(todraw, locx, locy, null);
                }
            }
            g2d.dispose();
        } catch (Exception ex) {
            ResourceHelper.errLog("EmbedTypeCustomImage > createNewImage(...) > Error : " + ex);
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
        this.covertype = CoverStyle.STRETCH;
        doShuffle = false;
        urls = null;
    }

    @Override
    public void resetOnlyMainProperties(EmbedType embedType) {
        if (embedType.getName().equals(getName())) {
            EmbedTypeCustomImage etci = (EmbedTypeCustomImage) embedType;
            covertype = etci.covertype;
            doShuffle = etci.doShuffle;
            urls = etci.urls;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (obj instanceof EmbedTypeCustomImage && obj != null) {
                EmbedTypeCustomImage embed = (EmbedTypeCustomImage) obj;
                return embed.doShuffle == doShuffle && embed.covertype == covertype
                        && embed.urls == urls;
            }
        }
        return false;
    }

    @Override
    protected void writeXMLElementInnerData(XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeEmptyElement("covertype");                     //<covertype ... />
        xsw.writeAttribute("value", covertype.toString());

        xsw.writeEmptyElement("doshuffle");                     //<doshuffle ... />
        xsw.writeAttribute("value", "" + doShuffle);

        xsw.writeStartElement("urls");                          //<urls>
        if (urls != null) {
            for (URL url : urls) {
                xsw.writeEmptyElement("url");                   //<url ... />
                xsw.writeAttribute("value", url.toString());
            }
        }
        xsw.writeEndElement();                                  //</urls>
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
                        case "covertype":
                            setCovertype(CoverStyle.valueOf(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "doshuffle":
                            setDoShuffle(Boolean.parseBoolean(nnm.getNamedItem("value").getNodeValue()));
                            break;
                        case "urls":
                            ArrayList<URL> al = new ArrayList<>();
                            NodeList uns = n.getChildNodes();
                            for (int j = 0; j < uns.getLength(); j++) {
                                Node un = uns.item(j);
                                if (un != null && un.getNodeType() == Node.ELEMENT_NODE && un.getNodeName().equals("url")) {
                                    NamedNodeMap unv = un.getAttributes();
                                    URL url = new URL(unv.getNamedItem("value").getNodeValue());
                                    al.add(url);
                                }
                            }
                            urls = new URL[al.size()];
                            urls = al.toArray(urls);
                            break;
                    }
                } catch (Exception ex) {
                    ResourceHelper.errLog("EmbedType > readXMLElementInnerData(Node) > Error : " + ex);
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

    /**
     * @return the covertype
     */
    public CoverStyle getCovertype() {
        return covertype;
    }

    /**
     * @param covertype the covertype to set
     */
    public void setCovertype(CoverStyle covertype) {
        this.covertype = covertype;
    }

    /**
     * @return the file
     */
    public URL[] getUrls() {
        return urls;
    }

    /**
     * @param file the file to set
     */
    public void setUrls(URL[] urls) {
        this.urls = urls;
    }

    /**
     * @return the doShuffle
     */
    public boolean isDoShuffle() {
        return doShuffle;
    }

    /**
     * @param doShuffle the doShuffle to set
     */
    public void setDoShuffle(boolean doShuffle) {
        this.doShuffle = doShuffle;
    }
}
