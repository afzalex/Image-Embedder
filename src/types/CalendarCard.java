package types;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JColorChooser;

public class CalendarCard extends CardItem {

    private static final String CHANGE_BACKGROUND_COLOR_CODE = "cbcc";
    private static final String CHANGE_MONTH_BACKGROUND_COLOR_CODE = "cmbcc";
    private static final String CHANGE_MONTH_BACKGROUND_COLOR_2_CODE = "cmbc2c";
    private static final String CHANGE_MONTH_FOREGROUND_COLOR_CODE = "cmfcc";
    private static final String CHANGE_DAYS_BACKGROUND_COLOR_CODE = "cdbcc";
    private static final String CHANGE_DAYS_BACKGROUND_COLOR_2_CODE = "cdbc2c";
    private static final String CHANGE_DAYS_FOREGROUND_COLOR_CODE = "cdfcc";
    private static final String CHANGE_TODAY_BACKGROUND_COLOR_CODE = "ctbcc";
    private static final String CHANGE_TODAY_FOREGROUND_COLOR_CODE = "ctfcc";

    public CalendarCard() {
        etc.setWidth(90);
        etc.setHeight(90);
        etc.setOpacity(100);
        etc.setSizeUnitType(UnitType.percent);
        initComponents();
    }

    EmbedTypeCalendar etc = new EmbedTypeCalendar();

    public void changeColor(java.awt.event.ActionEvent evt) {
        Color clr = null;
        Color prev = null;
        String title = null;
        java.util.function.Consumer<Color> cons = null;
        switch (evt.getActionCommand()) {
            case CHANGE_BACKGROUND_COLOR_CODE:
                title = "Change background color";
                prev = etc.getBackground();
                cons = c -> etc.setBackground(c);
                break;
            case CHANGE_MONTH_BACKGROUND_COLOR_CODE:
                title = "Change month background color";
                prev = etc.getMonthBack();
                cons = c -> etc.setMonthBack(c);
                break;
            case CHANGE_MONTH_BACKGROUND_COLOR_2_CODE:
                title = "Change month background color 2";
                prev = etc.getMonthBack2();
                cons = c -> etc.setMonthBack2(c);
                break;
            case CHANGE_MONTH_FOREGROUND_COLOR_CODE:
                title = "Change month foreground color";
                prev = etc.getMonthFore();
                cons = c -> etc.setMonthFore(c);
                break;
            case CHANGE_DAYS_BACKGROUND_COLOR_CODE:
                title = "Change days background color";
                prev = etc.getDaysBack1();
                cons = c -> etc.setDaysBack1(c);
                break;
            case CHANGE_DAYS_BACKGROUND_COLOR_2_CODE:
                title = "Change days background color 2";
                prev = etc.getDaysBack2();
                cons = c -> etc.setDaysBack2(c);
                break;
            case CHANGE_DAYS_FOREGROUND_COLOR_CODE:
                title = "Change days foreground color";
                prev = etc.getDaysFore();
                cons = c -> etc.setDaysFore(c);
                break;
            case CHANGE_TODAY_BACKGROUND_COLOR_CODE:
                title = "Change today's background color";
                prev = etc.getTodayBack();
                cons = c -> etc.setTodayBack(c);
                break;
            case CHANGE_TODAY_FOREGROUND_COLOR_CODE:
                title = "Change today's foreground color";
                prev = etc.getTodayFore();
                cons = c -> etc.setTodayFore(c);
        }

        if (title != null) {
            clr = JColorChooser.showDialog(this, "Change Background Color",
                    prev);
            if (clr != null) {
                cons.accept(clr);
                calendarDisp.repaint();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        calBgVisibleChk = new javax.swing.JCheckBox();
        monthBgVisibleChk = new javax.swing.JCheckBox();
        daysBgVisibleChk = new javax.swing.JCheckBox();
        calendarDisp = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                Image img = etc.createNewImage(w, h);
                int ew = img.getWidth(null);
                int eh = img.getHeight(null);
                g.drawImage(img, (w - ew) / 2, (h - eh) / 2, null);
            }
        };
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        bgColorBtn = new javax.swing.JButton();
        monthBackColorBtn = new javax.swing.JButton();
        monthForeColorBtn = new javax.swing.JButton();
        daysBgColorBtn = new javax.swing.JButton();
        daysBgColor2Btn = new javax.swing.JButton();
        daysForeColorBtn = new javax.swing.JButton();
        todayForeColorBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        todayBackColorBtn = new javax.swing.JButton();
        bgColorBtn1 = new javax.swing.JButton();

        calBgVisibleChk.setSelected(true);
        calBgVisibleChk.setText("Calendar Background visible");
        calBgVisibleChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calBgVisibleChkActionPerformed(evt);
            }
        });

        monthBgVisibleChk.setSelected(true);
        monthBgVisibleChk.setText("Month Background visible");
        monthBgVisibleChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthBgVisibleChkActionPerformed(evt);
            }
        });

        daysBgVisibleChk.setSelected(true);
        daysBgVisibleChk.setText("Days Background visible");
        daysBgVisibleChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daysBgVisibleChkActionPerformed(evt);
            }
        });

        calendarDisp.setBackground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout calendarDispLayout = new javax.swing.GroupLayout(calendarDisp);
        calendarDisp.setLayout(calendarDispLayout);
        calendarDispLayout.setHorizontalGroup(
            calendarDispLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        calendarDispLayout.setVerticalGroup(
            calendarDispLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Color Settings");

        bgColorBtn.setText("Calendar");
        bgColorBtn.setActionCommand(CHANGE_BACKGROUND_COLOR_CODE);
        bgColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        monthBackColorBtn.setText("Month");
        monthBackColorBtn.setActionCommand(CHANGE_MONTH_BACKGROUND_COLOR_CODE);
        monthBackColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        monthForeColorBtn.setText("Month");
        monthForeColorBtn.setActionCommand(CHANGE_MONTH_FOREGROUND_COLOR_CODE);
        monthForeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        daysBgColorBtn.setText("Days");
        daysBgColorBtn.setActionCommand(CHANGE_DAYS_BACKGROUND_COLOR_CODE);
        daysBgColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        daysBgColor2Btn.setText("Days 2");
        daysBgColor2Btn.setActionCommand(CHANGE_DAYS_BACKGROUND_COLOR_2_CODE);
        daysBgColor2Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        daysForeColorBtn.setText("Days");
        daysForeColorBtn.setActionCommand(CHANGE_DAYS_FOREGROUND_COLOR_CODE);
        daysForeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        todayForeColorBtn.setText("Today");
        todayForeColorBtn.setActionCommand(CHANGE_TODAY_FOREGROUND_COLOR_CODE);
        todayForeColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Background");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Foreground");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Background");

        todayBackColorBtn.setText("Today");
        todayBackColorBtn.setActionCommand(CHANGE_TODAY_BACKGROUND_COLOR_CODE);
        todayBackColorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
            }
        });

        bgColorBtn1.setText("Month 2");
        bgColorBtn1.setActionCommand(CHANGE_MONTH_BACKGROUND_COLOR_2_CODE);
        bgColorBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChangeActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(monthBackColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(daysBgColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bgColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(daysBgColor2Btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bgColorBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(todayForeColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(daysForeColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(monthForeColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(todayBackColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(todayBackColorBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(monthForeColorBtn)
                                .addGap(2, 2, 2)
                                .addComponent(daysForeColorBtn)
                                .addGap(2, 2, 2)
                                .addComponent(todayForeColorBtn))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(bgColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(monthBackColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(bgColorBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(daysBgColorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(daysBgColor2Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(calBgVisibleChk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(daysBgVisibleChk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(monthBgVisibleChk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(calendarDisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(calendarDisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calBgVisibleChk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthBgVisibleChk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(daysBgVisibleChk)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void calBgVisibleChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calBgVisibleChkActionPerformed
        etc.setBackgroundVisible(calBgVisibleChk.isSelected());
        calendarDisp.repaint();
    }//GEN-LAST:event_calBgVisibleChkActionPerformed

    private void monthBgVisibleChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthBgVisibleChkActionPerformed
        etc.setMonthBackVisible(monthBgVisibleChk.isSelected());
        calendarDisp.repaint();
    }//GEN-LAST:event_monthBgVisibleChkActionPerformed

    private void daysBgVisibleChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daysBgVisibleChkActionPerformed
        etc.setDaysBackVisible(daysBgVisibleChk.isSelected());
        calendarDisp.repaint();
    }//GEN-LAST:event_daysBgVisibleChkActionPerformed

    private void colorChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorChangeActionPerformed
        changeColor(evt);
    }//GEN-LAST:event_colorChangeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bgColorBtn;
    private javax.swing.JButton bgColorBtn1;
    private javax.swing.JCheckBox calBgVisibleChk;
    private javax.swing.JPanel calendarDisp;
    private javax.swing.JButton daysBgColor2Btn;
    private javax.swing.JButton daysBgColorBtn;
    private javax.swing.JCheckBox daysBgVisibleChk;
    private javax.swing.JButton daysForeColorBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton monthBackColorBtn;
    private javax.swing.JCheckBox monthBgVisibleChk;
    private javax.swing.JButton monthForeColorBtn;
    private javax.swing.JButton todayBackColorBtn;
    private javax.swing.JButton todayForeColorBtn;
    // End of variables declaration//GEN-END:variables

    @Override
    void reset() {
    }

    @Override
    void resetFromEmbedType(EmbedType embedType) {
        etc.resetOnlyMainProperties(embedType);
        calendarDisp.repaint();
        calBgVisibleChk.setSelected(etc.isBackgroundVisible());
        monthBgVisibleChk.setSelected(etc.isMonthBackVisible());
        daysBgVisibleChk.setSelected(etc.isDaysBackVisible());
    }

    @Override
    void loadIntoEmbedType(EmbedType embedType) {
        embedType.resetOnlyMainProperties(etc);
    }

    @Override
    EmbedType.Name getEmbedTypeName() {
        return EmbedType.Name.Calendar;
    }
}
