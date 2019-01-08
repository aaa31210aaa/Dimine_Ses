package bean;

public class WxyBean {
    //企业名称
    private String qyname;
    //主要风险点
    private String mainrisk;
    //风险点id
    private String fxdid;
    //主要危险
    private String maindanger;
    //风险类型
    private String risktypename;
    //备注
    private String memo;
    //是否是重大危险源


    public String getQyname() {
        return qyname;
    }

    public void setQyname(String qyname) {
        this.qyname = qyname;
    }

    public String getMainrisk() {
        return mainrisk;
    }

    public void setMainrisk(String mainrisk) {
        this.mainrisk = mainrisk;
    }

    public String getFxdid() {
        return fxdid;
    }

    public void setFxdid(String fxdid) {
        this.fxdid = fxdid;
    }

    public String getMaindanger() {
        return maindanger;
    }

    public void setMaindanger(String maindanger) {
        this.maindanger = maindanger;
    }

    public String getRisktypename() {
        return risktypename;
    }

    public void setRisktypename(String risktypename) {
        this.risktypename = risktypename;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
