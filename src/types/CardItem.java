package types;

import javax.swing.JPanel;
import types.EmbedType.Name;

public abstract class CardItem extends JPanel {

    public CardItem() {
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    }

    abstract void reset();

    abstract void resetFromEmbedType(EmbedType embedType);

    abstract void loadIntoEmbedType(EmbedType embedType);

    abstract Name getEmbedTypeName();
}
