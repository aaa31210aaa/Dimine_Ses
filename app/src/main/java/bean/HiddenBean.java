package bean;

public class HiddenBean {
    private String Date;  //日期
    private String InvestigationType;  //排查类型
    private String Checker; //检查人

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getInvestigationType() {
        return InvestigationType;
    }

    public void setInvestigationType(String investigationType) {
        InvestigationType = investigationType;
    }

    public String getChecker() {
        return Checker;
    }

    public void setChecker(String checker) {
        Checker = checker;
    }
}
