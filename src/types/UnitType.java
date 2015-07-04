package types;

public enum UnitType {

    pixels("px"),
    percent("%");
    public final String sign;
    UnitType(String sign){
        this.sign = sign;
    }
    
    public static UnitType parseSign(String sign){
        if(sign.equalsIgnoreCase(pixels.sign)){
            return pixels;
        } else {
            return percent;
        }
    }
}
