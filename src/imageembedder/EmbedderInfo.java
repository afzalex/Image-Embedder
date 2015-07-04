package imageembedder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import resources.ResourceHelper;
import types.EmbedType;

public class EmbedderInfo {

    private final ArrayList<EmbedType> embeds = new ArrayList<>();
    private boolean doRepeat = true;
    private long period = 1500000L; // 25 seconds

    public EmbedderInfo() {
    }

    public void addEmbed(EmbedType embed) {
        embeds.add(embed);
    }

    public void removeEmbed(EmbedType embed) {
        embeds.remove(embed);
    }

    public ArrayList<EmbedType> getEmbeds() {
        return embeds;
    }

    public void writeXMLData(OutputStream stream) throws XMLStreamException {
        XMLOutputFactory xof = ResourceHelper.getResource("XMLOutputFactory");
        XMLStreamWriter xsw = xof.createXMLStreamWriter(stream);
        xsw.writeStartDocument();

        xsw.writeStartElement("embedderinfo");             //<embedderinfo>

        xsw.writeAttribute("embedderlastopened", Long.toString(ResourceHelper.getResource("embedderlastopened")));
        xsw.writeAttribute("embedderlastupdated", Long.toString(ResourceHelper.getResource("embedderlastupdated")));

        for (EmbedType et : embeds) {
            et.writeXMLElement(xsw);
        }
        xsw.writeEndElement();                             //</embedderinfo>

        xsw.writeEndDocument();
    }

    public void readXMLData(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = ResourceHelper.getResource("DocumentBuilderFactory");
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(stream);
        Element element = doc.getDocumentElement();
        if (element.getNodeName().equals("embedderinfo")) {
            String lastopened = element.getAttribute("embedderlastopened");
            String lastupdated = element.getAttribute("embedderlastupdated");
            lastOpenedRecieved(lastopened);
            lastUpdatedRecieved(lastupdated);
            NodeList nodes = element.getChildNodes();
            int l = nodes.getLength();
            Node node;
            EmbedType et;
            for (int i = 0; i < l; i++) {
                node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if ((et = EmbedType.createEmbedTypeFromXMLNode(node)) != null) {
                        embeds.add(et);
                    }
                }
            }
        }
    }

    protected void lastOpenedRecieved(String lastOpened) {
    }

    protected void lastUpdatedRecieved(String lastUpdated) {
    }
}
