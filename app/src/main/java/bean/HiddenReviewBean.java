package bean;

public class HiddenReviewBean {
    //隐患名称
    private String HiddenName;
    //隐患编号
    private String HiddenNumber;
    //整改责任人
    private String RectificationPersonLiable;
    //隐患描述
    private String Describe;
    //隐患id
    private String yhid;



    public String getHiddenName() {
        return HiddenName;
    }

    public void setHiddenName(String hiddenName) {
        HiddenName = hiddenName;
    }

    public String getHiddenNumber() {
        return HiddenNumber;
    }

    public void setHiddenNumber(String hiddenNumber) {
        HiddenNumber = hiddenNumber;
    }

    public String getRectificationPersonLiable() {
        return RectificationPersonLiable;
    }

    public void setRectificationPersonLiable(String rectificationPersonLiable) {
        RectificationPersonLiable = rectificationPersonLiable;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }
}
