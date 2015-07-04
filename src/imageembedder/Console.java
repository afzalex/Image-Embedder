package imageembedder;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import resources.ResourceHelper;
import types.EmbedType;
import types.UnitType;

public abstract class Console extends JLayeredPane {

    private final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final Image consoleBackground = ResourceHelper.getImageObject("consoleBackground.jpg");
    private Image currentBackground = consoleBackground;
    private final EmbedderInfo embedderInfo;
    private final ConcurrentHashMap<EmbedType, CardPane> map = new ConcurrentHashMap<>();
    private CardPane selectedCard;
    private VirtualArea virtualArea = new VirtualArea(1212, 680, 305, 170);

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        private Point down = null;

        @Override
        public void mousePressed(MouseEvent e) {
            down = e.getPoint();
            setSelectedCard((CardPane) e.getSource());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Object source = e.getSource();
            if (source != null && source instanceof CardPane) {
                CardPane pan = (CardPane) e.getSource();
                Point loc = pan.getLocation();
                Point drag = e.getPoint();
                pan.setLocation(loc.x + drag.x - down.x, loc.y + drag.y - down.y);
                pan.resetLocationToEmbedType();
                locationChanged(pan.getEmbedtype().getPosx(), pan.getEmbedtype().getPosy());
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Object source = e.getSource();
            if (source != null && source instanceof CardPane) {
                CardPane pan = (CardPane) e.getSource();
                Point loc = pan.getLocation();
                Point drag = e.getPoint();
                pan.setLocation(loc.x + drag.x - down.x, loc.y + drag.y - down.y);
                pan.resetLocationToEmbedType();
                locationChanged(pan.getEmbedtype().getPosx(), pan.getEmbedtype().getPosy());
            }
            repaint();
        }
    };

    public Console(EmbedderInfo embedderInfo) {
        this.embedderInfo = embedderInfo;
        setBackground(TRANSPARENT);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                virtualArea.setVirtualSize(getWidth(), getHeight());
                resetCards();
            }
        });
    }

    public void resetRealSize(int realWidth, int realHeight) {
        virtualArea.setRealSize(realWidth, realHeight);
    }

    private void setSelectedCard(CardPane pane) {
        if (selectedCard != null) {
            selectedCard.setIsSelected(false);
        }
        selectedCard = pane;
        selectedCard.setIsSelected(true);
        if (map.containsValue(pane)) {
            Optional<EmbedType> op = map.keySet().stream().filter(et -> {
                if (map.get(et) == pane) {
                    return true;
                }
                return false;
            }).findFirst();
            if (op.isPresent()) {
                selectedCardChanged(op.get());
            }
        }
        selectedCard.resetLocationFromEmbedType();
        locationChanged(selectedCard.embedtype.getPosx(), selectedCard.embedtype.getPosy());
        repaint();
    }

    public void selectedCardChanged(EmbedType embed) {
    }

    public boolean canSelectMoreCard() {
        Set<EmbedType> keySet = map.keySet();
        return !keySet.isEmpty() && keySet.size() > 1;
    }

    public void selectNextCard() {
        ArrayList<CardPane> al = new ArrayList<>(map.values());
        for (int i = 0; i < al.size(); i++) {
            CardPane cp = al.get(i);
            if (selectedCard == cp) {
                if (i == al.size() - 1) {
                    setSelectedCard(al.get(0));
                } else {
                    setSelectedCard(al.get(i + 1));
                }
                break;
            }
        }
    }

    public void selectPrevCard() {
        ArrayList<CardPane> al = new ArrayList<>(map.values());
        for (int i = 0; i < al.size(); i++) {
            CardPane cp = al.get(i);
            if (selectedCard == cp) {
                if (i == 0) {
                    setSelectedCard(al.get(al.size() - 1));
                } else {
                    setSelectedCard(al.get(i - 1));
                }
                break;
            }
        }
    }

    public final void resetCards() {
        ArrayList<EmbedType> embeds = getEmbedderInfo().getEmbeds();

        // checking if old EmbedType's are removed. And remove them from display.
        for (EmbedType et : map.keySet()) {
            if (!embeds.contains(et)) {
                CardPane val = map.get(et);
                remove(val);
                map.remove(et);
            }
        }

        // checking if new EmbedInfo are available to display. And display them.
        for (int i = 0; i < embeds.size(); i++) {
            EmbedType key = embeds.get(i);
            if (map.containsKey(key)) {
                CardPane val = map.get(key);
                setLayer(val, i);
            } else {
                CardPane val = new CardPane(this, key);
                val.resetLocationFromEmbedType();
                val.repaint();
                map.put(key, val);
                add(val, i);
            }
        }
        repaint();
    }

    public void resetCard(EmbedType et) {
        CardPane cp = map.get(et);
        if (cp != null) {
            cp.resetImage();
        }
    }

    public void resetLocationUnitType(UnitType unitype) {
        selectedCard.getEmbedtype().setPosUnitType(unitype);
        selectedCard.resetLocationFromEmbedType();
    }

    public void resetLocationX(int x) {
        selectedCard.getEmbedtype().setPosx(x);
        selectedCard.resetLocationFromEmbedType();
    }

    public void resetLocationY(int y) {
        selectedCard.getEmbedtype().setPosy(y);
        selectedCard.resetLocationFromEmbedType();
    }

    public abstract void locationChanged(int x, int y);

    @Override
    public void paint(Graphics g) {
        if (consoleBackground != null) {
            if (currentBackground.getWidth(null) != getWidth()
                    || currentBackground.getHeight(null) != getHeight()) {
                currentBackground = consoleBackground
                        .getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
            }
        }
        g.drawImage(currentBackground, 0, 0, null);
        super.paint(g);
    }

    public EmbedderInfo getEmbedderInfo() {
        return embedderInfo;
    }

    public EmbedType getSelectedEmbedType() {

        return selectedCard == null ? null : selectedCard.getEmbedtype();
    }

    private class CardPane extends JPanel {

        private boolean isSelected = false;
        private Image realImage;
        private Image virtualImage;
        private final Color TRANSPARENT = new Color(0, 0, 0, 0);
        private final EmbedType embedtype;
        private final Container parent;

        CardPane(Container parent, EmbedType embedtype) {
            this.embedtype = embedtype;
            this.parent = parent;
            resetImage();
            setBackground(TRANSPARENT);
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        public void resetLocationAndSize() {

        }

        protected final void resetImage() {
            //realImage = embedtype.createSampleImage(virtualArea.getRealWidth(), virtualArea.getRealHeight());
            realImage = embedtype.createNewImage(virtualArea.getRealWidth(), virtualArea.getRealHeight());
            if (realImage == null) {
                realImage = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
            }
            int riw = realImage.getWidth(null);
            int rih = realImage.getHeight(null);
            virtualImage = realImage.getScaledInstance(virtualArea.realToVirtualX(riw),
                    virtualArea.realToVirtualY(rih), Image.SCALE_SMOOTH);
            Dimension size = new Dimension(virtualImage.getWidth(null), virtualImage.getHeight(null));
            setSize(size);
            setPreferredSize(size);
            repaint();
        }

        public void resetLocationFromEmbedType() {
            int posx = embedtype.getPosx();
            int posy = embedtype.getPosy();
            UnitType posunit = embedtype.getPosUnitType();
            int anchorXinPX = embedtype.getAnchorX() * virtualArea.virtualToRealX(getWidth()) / 100;
            int anchorYinPX = embedtype.getAnchorY() * virtualArea.virtualToRealY(getHeight()) / 100;
            int realX, realY;
            if (posunit.equals(UnitType.pixels)) {
                realX = posx - anchorXinPX;
                realY = posy - anchorYinPX;
            } else {
                realX = virtualArea.getRealWidth() * posx / 100 - anchorXinPX;
                realY = virtualArea.getRealHeight() * posy / 100 - anchorYinPX;
            }
            realX = virtualArea.realToVirtualX(realX);
            realY = virtualArea.realToVirtualY(realY);
            setLocation(realX, realY);
        }

        public void resetLocationToEmbedType() {
            UnitType posunit = embedtype.getPosUnitType();
            int anchorXinPX = embedtype.getAnchorX() * virtualArea.virtualToRealX(getWidth()) / 100;
            int anchorYinPX = embedtype.getAnchorY() * virtualArea.virtualToRealY(getHeight()) / 100;
            int posx, posy;
            int locx = virtualArea.virtualToRealX(getX());
            int locy = virtualArea.virtualToRealY(getY());
            if (posunit.equals(UnitType.pixels)) {
                posx = locx + anchorXinPX;
                posy = locy + anchorYinPX;
            } else {
                posx = (locx + anchorXinPX) * 100 / virtualArea.getRealWidth();
                posy = (locy + anchorYinPX) * 100 / virtualArea.getRealHeight();
            }
            embedtype.setPosx(posx);
            embedtype.setPosy(posy);
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(virtualImage, 0, 0, null);
            if (isIsSelected()) {
                g.setColor(Color.WHITE);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            super.paint(g);
        }

        /**
         * @return the realImage
         */
        public Image getRealImage() {
            return realImage;
        }

        public Image getVirtualImage() {
            return virtualImage;
        }

        /**
         * @return the embedtype
         */
        public EmbedType getEmbedtype() {
            return embedtype;
        }

        /**
         * @return the isSelected
         */
        public boolean isIsSelected() {
            return isSelected;
        }

        /**
         * @param isSelected the isSelected to set
         */
        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }

}
