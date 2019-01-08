package bean;


public class TemporaryBean {
    //保存信息的id
    private String saveId;
    //隐患名称
    private String CommonlyName;
    //整改责任人
    private String CommonlyLiable;
    //隐患类型分类
    private String CommonlyTypeClassification;
    //整改类型
    private String Rectification;
    //排查日期
    private String InvestigationDate;
    //治理截止日期
    private String ClosingDate;
    //隐患具体位置
    private String CommonlyLocation;
    //隐患描述
    private String CommonlyDescribe;
    //主要治理方案
    private String CommonlyScheme;
    //伤亡人数
    private String CasualtiesNum;
    //经济损失
    private String Loss;
    //时间和范围
    private String TimeAndRange;
    //隐患级别
    private String Level;

    //保存信息的时间
    private String saveTime;
    //图片地址
    private String ImagesAddress;
    //选中框展示状态
    private boolean isShow;
    //选中状态
    private boolean isCheck;


    public String getSaveId() {
        return saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public String getCommonlyName() {
        return CommonlyName;
    }

    public void setCommonlyName(String commonlyName) {
        CommonlyName = commonlyName;
    }

    public String getCommonlyLiable() {
        return CommonlyLiable;
    }

    public void setCommonlyLiable(String commonlyLiable) {
        CommonlyLiable = commonlyLiable;
    }

    public String getCommonlyTypeClassification() {
        return CommonlyTypeClassification;
    }

    public void setCommonlyTypeClassification(String commonlyTypeClassification) {
        CommonlyTypeClassification = commonlyTypeClassification;
    }

    public String getRectification() {
        return Rectification;
    }

    public void setRectification(String rectification) {
        Rectification = rectification;
    }

    public String getInvestigationDate() {
        return InvestigationDate;
    }

    public void setInvestigationDate(String investigationDate) {
        InvestigationDate = investigationDate;
    }

    public String getClosingDate() {
        return ClosingDate;
    }

    public void setClosingDate(String closingDate) {
        ClosingDate = closingDate;
    }

    public String getCommonlyLocation() {
        return CommonlyLocation;
    }

    public void setCommonlyLocation(String commonlyLocation) {
        CommonlyLocation = commonlyLocation;
    }

    public String getCommonlyDescribe() {
        return CommonlyDescribe;
    }

    public void setCommonlyDescribe(String commonlyDescribe) {
        CommonlyDescribe = commonlyDescribe;
    }

    public String getCommonlyScheme() {
        return CommonlyScheme;
    }

    public void setCommonlyScheme(String commonlyScheme) {
        CommonlyScheme = commonlyScheme;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getImagesAddress() {
        return ImagesAddress;
    }

    public void setImagesAddress(String imagesAddress) {
        ImagesAddress = imagesAddress;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCasualtiesNum() {
        return CasualtiesNum;
    }

    public void setCasualtiesNum(String casualtiesNum) {
        CasualtiesNum = casualtiesNum;
    }

    public String getLoss() {
        return Loss;
    }

    public void setLoss(String loss) {
        Loss = loss;
    }

    public String getTimeAndRange() {
        return TimeAndRange;
    }

    public void setTimeAndRange(String timeAndRange) {
        TimeAndRange = timeAndRange;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }
}
