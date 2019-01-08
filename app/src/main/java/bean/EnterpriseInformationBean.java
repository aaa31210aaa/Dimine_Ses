package bean;

public class EnterpriseInformationBean {
    //企业名称
    private String CompanyName;
    //监管部门
    private String SupervisionDepartment;
    //企业地址
    private String EnterpriseAddress;
    //企业ID
    private String CompanyId;
    //行业
    private String industry;
    //是否有更新
    private String addStatus;
    //更新时间
    private String tablename;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getSupervisionDepartment() {
        return SupervisionDepartment;
    }

    public void setSupervisionDepartment(String supervisionDepartment) {
        SupervisionDepartment = supervisionDepartment;
    }

    public String getEnterpriseAddress() {
        return EnterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        EnterpriseAddress = enterpriseAddress;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}
