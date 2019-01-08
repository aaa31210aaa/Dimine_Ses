package bean;

public class EnterpriseBean {
    //排查名称
    private String InvestigationName;
    //排查类型
    private String InvestigationType;
    //排查人
    private String Investigators;

    public String getInvestigationName() {
        return InvestigationName;
    }

    public void setInvestigationName(String investigationName) {
        InvestigationName = investigationName;
    }

    public String getInvestigationType() {
        return InvestigationType;
    }

    public void setInvestigationType(String investigationType) {
        InvestigationType = investigationType;
    }

    public String getInvestigators() {
        return Investigators;
    }

    public void setInvestigators(String investigators) {
        Investigators = investigators;
    }
}
