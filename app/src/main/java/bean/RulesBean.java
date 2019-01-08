package bean;

public class RulesBean {
    //规章制度名称
    private String RulesName;
    //规章制度ID
    private String RulesID;
    //生效时间
    private String RulesEffTime;
    //规章制度类型
    private String RulesType;
    //创建人
    private String RulesCreateName;
    //创建时间
    private String CreateDate;
    //备注
    private String Memo;

    //企业名称
    private String qyname;

    public String getQyname() {
        return qyname;
    }

    public void setQyname(String qyname) {
        this.qyname = qyname;
    }

    public String getRulesName() {
        return RulesName;
    }

    public void setRulesName(String rulesName) {
        RulesName = rulesName;
    }

    public String getRulesID() {
        return RulesID;
    }

    public void setRulesID(String rulesID) {
        RulesID = rulesID;
    }

    public String getRulesEffTime() {
        return RulesEffTime;
    }

    public void setRulesEffTime(String rulesEffTime) {
        RulesEffTime = rulesEffTime;
    }

    public String getRulesType() {
        return RulesType;
    }

    public void setRulesType(String rulesType) {
        RulesType = rulesType;
    }

    public String getRulesCreateName() {
        return RulesCreateName;
    }

    public void setRulesCreateName(String rulesCreateName) {
        RulesCreateName = rulesCreateName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
