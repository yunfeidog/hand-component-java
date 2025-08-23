package plus.yunfei;

public enum PayType {
    WX,
    ALI;

    public static PayType typeOf(String payType) {
        return PayType.valueOf(payType);
    }
}
