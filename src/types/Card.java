package types;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import resources.ResourceHelper;
import types.EmbedType.Name;

public abstract class Card extends javax.swing.JPanel {

    private EmbedType embedType;
    private CardItem cardtype;
    private DefaultFormatterFactory percentFormatterFactory;
    private final HashMap<EmbedType.Name, CardItem> cardOptions = new HashMap<>();
    public final static Dimension CARD_SIZE = new Dimension(545, 293);

    private int info_posx, info_posy;
    private UnitType info_posUnit;

    public final boolean CREATE = true;
    public final boolean EDIT = false;

    public boolean createOrEdit = CREATE;

    private final AnchorPane anchorPane = new AnchorPane() {
        @Override
        public void anchorMoved(int anchorx, int anchory) {
            anchorXField.setText(anchorx + "");
            anchorYField.setText(anchory + "");
        }
    };

    /**
     * Creates new form Type
     */
    public Card() {
        this(null);
    }

    public Card(EmbedType embedtype) {
        // setting all possible embedTypes. i.e. setting gui for all features.
        {
            cardOptions.put(EmbedType.Name.CustomImage, new CustomImageCard());
            cardOptions.put(EmbedType.Name.WeatherInfo, new WeatherInfoCard());
            cardOptions.put(EmbedType.Name.Calendar, new CalendarCard());
        }

        if (embedtype == null) {
            cardtype = cardOptions.get(EmbedType.Name.CustomImage);
        } else {
            cardtype = cardOptions.get(embedtype.getName());
            cardtype.resetFromEmbedType(embedtype);
        }
        preInitComponents();
        initComponents();
        postInitComponents();
        resetFromEmbedType(embedtype);
    }

    private void preInitComponents() {
        anchorPane.setAnchorx(EMPTY_EMBEDTYPE.getAnchorX());
        anchorPane.setAnchory(EMPTY_EMBEDTYPE.getAnchorY());
        NumberFormatter f = new NumberFormatter(new DecimalFormat("#"));
        f.setMinimum(0);
        f.setMaximum(100);
        percentFormatterFactory = new javax.swing.text.DefaultFormatterFactory(f);
    }

    private void postInitComponents() {
        resetTypePanePlatform();
    }

    private abstract class AnchorPane extends JPanel {

        private Rectangle area;
        private final int gap = 10;
        private final Image crosshair = ResourceHelper.getImageObject("crosshair.png")
                .getScaledInstance(20, -1, Image.SCALE_SMOOTH);
        private int anchorx = 0;
        private int anchory = 0;
        private final MouseAdapter mouseAdapter = new MouseAdapter() {
            boolean isPressed = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (area.contains(e.getPoint())) {
                    isPressed = true;
                } else {
                    isPressed = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPressed) {
                    Point point = e.getPoint();
                    if (point.x < area.x) {
                        point.x = area.x;
                    } else if (point.x > area.getMaxX()) {
                        point.x = (int) area.getMaxX();
                    }
                    if (point.y < area.y) {
                        point.y = area.y;
                    } else if (point.y > area.getMaxY()) {
                        point.y = (int) area.getMaxY();
                    }
                    int anchorx = (int) ((point.x - gap) * 100.0 / area.width);
                    int anchory = (int) ((point.y - gap) * 100.0 / area.height);
                    reset(anchorx, anchory);
                }
            }
        };

        AnchorPane() {
            setBackground(Color.WHITE);
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            int innerw = w - 2 * gap;
            int innerh = h - 2 * gap;
            area = new Rectangle(gap, gap, innerw, innerh);
            Color temp = g.getColor();
            super.paintComponent(g);
            g.setColor(Color.GRAY);
            g.fillRect(gap, gap, innerw, innerh);
            int imgw = crosshair.getWidth(null);
            int imgh = crosshair.getHeight(null);
            int x = (int) (anchorx * innerw / 100.0 - imgw / 2.0 + gap);
            int y = (int) (anchory * innerh / 100.0 - imgh / 2.0 + gap);
            g.drawImage(crosshair, x, y, null);
            g.setColor(temp);
        }

        private void reset(int anchorx, int anchory) {
            this.anchorx = anchorx;
            this.anchory = anchory;
            anchorMoved(anchorx, anchory);
            repaint();
        }

        public abstract void anchorMoved(int anchorx, int anchory);

        /**
         * @param anchorx the anchorx to set
         */
        public void setAnchorx(int anchorx) {
            this.anchorx = anchorx;
            repaint();
        }

        /**
         * @param anchory the anchory to set
         */
        public void setAnchory(int anchory) {
            this.anchory = anchory;
            repaint();
        }
    }

    private void resetTypePanePlatform() {
        Component[] components = typePanePlatform.getComponents();
        if (components == null || components.length != 1 || !(components[0] instanceof CardItem)
                || components[0] != cardtype) {
            typePanePlatform.removeAll();
            typePanePlatform.add(cardtype);
            resetTypePanePlatformSize();
            updateUI();
        }
    }

    private void resetTypePanePlatformSize() {
        cardtype.setSize(typePanePlatform.getSize());
        cardtype.setPreferredSize(typePanePlatform.getSize());
    }

    public EmbedType getEmbedtype() {
        return embedType;
    }

    public EmbedType createEmbedType() {
        EmbedType et = null;
        for (Name name : cardOptions.keySet()) {
            if (name.DISPLAY_NAME.equals(typeCombo.getSelectedItem())) {
                switch (name) {
                    case Calendar:
                        et = new EmbedTypeCalendar();
                        break;
                    case WeatherInfo:
                        et = new EmbedTypeWeatherInfo();
                        break;
                    default:
                        et = new EmbedTypeCustomImage();
                }
                break;
            }
        }

        editEmbedType(et);

        embedType = et;
        return embedType;
    }

    public EmbedType editEmbedType() {
        return editEmbedType(embedType);
    }

    public EmbedType editEmbedType(EmbedType et) {
        et.setWidth(Integer.parseInt(widthField.getText()));
        et.setHeight(Integer.parseInt(heightField.getText()));
        et.setSizeUnitType(UnitType.parseSign(getSizeUnitType()));
        et.setAnchorX(anchorPane.anchorx);
        et.setAnchorY(anchorPane.anchory);
        et.setOpacity(Integer.parseInt(opacityField.getText()));
        et.setPosx(info_posx);
        et.setPosy(info_posy);
        et.setPosUnitType(info_posUnit);

        cardtype.loadIntoEmbedType(et);

        return et;
    }

    public void reset() {
        EmbedType et = EMPTY_EMBEDTYPE;
        widthField.setText(et.getWidth() + "");
        heightField.setText(et.getHeight() + "");
        sizeUnitCombo.setSelectedItem(et.getSizeUnitType().sign);
        anchorXField.setText(et.getAnchorX() + "");
        anchorYField.setText(et.getAnchorY() + "");
        anchorPane.setAnchorx(et.getAnchorX());
        anchorPane.setAnchory(et.getAnchorY());
        opacityField.setText(et.getOpacity() + "");

        info_posx = et.getPosx();
        info_posy = et.getPosy();
        info_posUnit = et.getPosUnitType();

        for (Entry<Name, CardItem> entry : cardOptions.entrySet()) {
            CardItem ct = entry.getValue();
            if (ct != null) {
                ct.reset();
            }
        }
    }

    public final void resetFromEmbedType(EmbedType embedType) {
        if (embedType != null) {
            widthField.setText(embedType.getWidth() + "");
            heightField.setText(embedType.getHeight() + "");
            sizeUnitCombo.setSelectedItem(embedType.getSizeUnitType().sign);
            anchorXField.setText(embedType.getAnchorX() + "");
            anchorYField.setText(embedType.getAnchorY() + "");
            anchorPane.setAnchorx(embedType.getAnchorX());
            anchorPane.setAnchory(embedType.getAnchorY());
            opacityField.setText(embedType.getOpacity() + "");
            info_posx = embedType.getPosx();
            info_posy = embedType.getPosy();
            info_posUnit = embedType.getPosUnitType();

            for (Entry<Name, CardItem> entry : cardOptions.entrySet()) {
                CardItem ct = entry.getValue();
                if (ct != null) {
                    if (entry.getKey() == embedType.getName()) {
                        ct.resetFromEmbedType(embedType);
                        typeCombo.setSelectedItem(embedType.getName());
                        for (Name name : cardOptions.keySet()) {
                            if (name.DISPLAY_NAME.equals(embedType.getName().DISPLAY_NAME)) {
                                cardtype = cardOptions.get(name);
                                resetTypePanePlatform();
                                break;
                            }
                        }
                    } else {
                        ct.reset();
                    }
                }
            }

            this.embedType = embedType;
        } else {
            reset();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (createOrEdit) {
            createEditCard.setText("Create");
        } else {
            createEditCard.setText("Edit");
        }
        super.paint(g);
    }

    public abstract void createCard();

    public abstract void editCard();

    public abstract void cancelCard();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sizeFormat = new javax.swing.ButtonGroup();
        opacityLabel = new javax.swing.JLabel();
        opacityField = new javax.swing.JFormattedTextField();
        opacityUnitLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox<String>(EmbedType.DISPLAY_NAMES);
        typePanePlatform = new javax.swing.JPanel();
        createEditCard = new javax.swing.JButton();
        cancelCard = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        widthLabel = new javax.swing.JLabel();
        widthField = new javax.swing.JFormattedTextField();
        widthUnitTypeLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        heightField = new javax.swing.JFormattedTextField();
        heightUnitTypeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sizeUnitCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        anchorXLabel = new javax.swing.JLabel();
        anchorXField = new javax.swing.JFormattedTextField();
        anchorXUnitLabel = new javax.swing.JLabel();
        anchorYUnitLabel = new javax.swing.JLabel();
        anchorYField = new javax.swing.JFormattedTextField();
        anchorYLabel = new javax.swing.JLabel();
        anchorPanePlatform = anchorPane;
        jLabel3 = new javax.swing.JLabel();

        setPreferredSize(CARD_SIZE);

        opacityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        opacityLabel.setText("Opacity : ");

        opacityField.setFormatterFactory(percentFormatterFactory);

        opacityUnitLabel.setText("%");

        typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        typeLabel.setText("Type : ");

        typeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboActionPerformed(evt);
            }
        });

        typePanePlatform.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        typePanePlatform.setLayout(new GridLayout(1, 1));
        typePanePlatform.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                typePanePlatformAncestorResized(evt);
            }
        });

        javax.swing.GroupLayout typePanePlatformLayout = new javax.swing.GroupLayout(typePanePlatform);
        typePanePlatform.setLayout(typePanePlatformLayout);
        typePanePlatformLayout.setHorizontalGroup(
            typePanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        typePanePlatformLayout.setVerticalGroup(
            typePanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        createEditCard.setText("Create");
        createEditCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEditCardActionPerformed(evt);
            }
        });

        cancelCard.setText("Cancel");
        cancelCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelCardActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        widthLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        widthLabel.setText("Width : ");

        widthField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        widthUnitTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        widthUnitTypeLabel.setText(getSizeUnitType());
        widthUnitTypeLabel.setAlignmentX(0.5F);

        heightLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        heightLabel.setText("Height : ");

        heightField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        heightUnitTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heightUnitTypeLabel.setText(getSizeUnitType());
        heightUnitTypeLabel.setAlignmentX(0.5F);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Size Setting");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        sizeUnitCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "%", "px" }));
        sizeUnitCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeUnitComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(heightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(heightUnitTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(widthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(widthUnitTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sizeUnitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(widthField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(widthUnitTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeUnitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightUnitTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        anchorXLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        anchorXLabel.setText("Anchor X : ");

        anchorXField.setFormatterFactory(percentFormatterFactory);
        anchorXField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                anchorXFieldPropertyChange(evt);
            }
        });

        anchorXUnitLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        anchorXUnitLabel.setText("%");
        anchorXUnitLabel.setAlignmentX(0.5F);

        anchorYUnitLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        anchorYUnitLabel.setText("%");
        anchorYUnitLabel.setAlignmentX(0.5F);

        anchorYField.setFormatterFactory(percentFormatterFactory);
        anchorYField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                anchorYFieldPropertyChange(evt);
            }
        });

        anchorYLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        anchorYLabel.setText("Anchor Y : ");

        javax.swing.GroupLayout anchorPanePlatformLayout = new javax.swing.GroupLayout(anchorPanePlatform);
        anchorPanePlatform.setLayout(anchorPanePlatformLayout);
        anchorPanePlatformLayout.setHorizontalGroup(
            anchorPanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        anchorPanePlatformLayout.setVerticalGroup(
            anchorPanePlatformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Anchor Setting");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(anchorYLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(anchorYField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(anchorYUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(anchorXLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(anchorXField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(anchorXUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(anchorPanePlatform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(anchorXLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anchorXField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anchorXUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(anchorYLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anchorYField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(anchorYUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addComponent(anchorPanePlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(createEditCard)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelCard))
                    .addComponent(typePanePlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(opacityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(opacityField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(opacityUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opacityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opacityField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opacityUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(typePanePlatform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createEditCard)
                    .addComponent(cancelCard))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sizeUnitComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeUnitComboActionPerformed
        widthUnitTypeLabel.setText(getSizeUnitType());
        heightUnitTypeLabel.setText(getSizeUnitType());
    }//GEN-LAST:event_sizeUnitComboActionPerformed

    private void anchorXFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_anchorXFieldPropertyChange
        if (evt.getPropertyName().equals("value")) {
            anchorPane.setAnchorx(Integer.parseInt(evt.getNewValue() + ""));
        }
    }//GEN-LAST:event_anchorXFieldPropertyChange

    private void anchorYFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_anchorYFieldPropertyChange
        if (evt.getPropertyName().equals("value")) {
            anchorPane.setAnchory(Integer.parseInt(evt.getNewValue() + ""));
        }
    }//GEN-LAST:event_anchorYFieldPropertyChange

    private void typeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboActionPerformed

        for (Name name : cardOptions.keySet()) {
            if (name.DISPLAY_NAME.equals(typeCombo.getSelectedItem())) {
                cardtype = cardOptions.get(name);
                resetTypePanePlatform();
                break;
            }
        }
    }//GEN-LAST:event_typeComboActionPerformed

    private void typePanePlatformAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_typePanePlatformAncestorResized
        resetTypePanePlatformSize();
    }//GEN-LAST:event_typePanePlatformAncestorResized

    private void createEditCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createEditCardActionPerformed
        if (createOrEdit) {
            createEmbedType();
            createCard();
        } else {
            editEmbedType();
            editCard();
        }
    }//GEN-LAST:event_createEditCardActionPerformed

    private void cancelCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelCardActionPerformed
        cancelCard();
    }//GEN-LAST:event_cancelCardActionPerformed

    private String getSizeUnitType() {
        return sizeUnitCombo.getSelectedIndex() == 0 ? "%" : "px";
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel anchorPanePlatform;
    private javax.swing.JFormattedTextField anchorXField;
    private javax.swing.JLabel anchorXLabel;
    private javax.swing.JLabel anchorXUnitLabel;
    private javax.swing.JFormattedTextField anchorYField;
    private javax.swing.JLabel anchorYLabel;
    private javax.swing.JLabel anchorYUnitLabel;
    private javax.swing.JButton cancelCard;
    private javax.swing.JButton createEditCard;
    private javax.swing.JFormattedTextField heightField;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JLabel heightUnitTypeLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JFormattedTextField opacityField;
    private javax.swing.JLabel opacityLabel;
    private javax.swing.JLabel opacityUnitLabel;
    private javax.swing.ButtonGroup sizeFormat;
    public javax.swing.JComboBox sizeUnitCombo;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JPanel typePanePlatform;
    private javax.swing.JFormattedTextField widthField;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JLabel widthUnitTypeLabel;
    // End of variables declaration//GEN-END:variables

    private final EmbedType EMPTY_EMBEDTYPE = new EmbedType(Name.Calendar) {

        @Override
        public void reset() {
        }

        @Override
        public void resetFromEmbedType(EmbedType embedtype) {
        }

        @Override
        public EmbedType createNewEmbedType() {
            return null;
        }

        @Override
        protected void writeXMLElementInnerData(XMLStreamWriter xsw) throws XMLStreamException {
        }

        @Override
        public Image createNewImage(int parentWidth, int parentHeight) {
            return null;
        }

        @Override
        protected void readXMLElementInnerData(Node node) {
        }

        @Override
        public Image createSampleImage(int parentWidth, int parentHeight) {
            return null;
        }

        @Override
        public void resetOnlyMainProperties(EmbedType embedType) {
        }
    };
}
