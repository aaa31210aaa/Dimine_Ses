package utils;


import android.content.Context;

/**
 * 端口ip地址
 */
public class PortIpAddress {
    public static String JsonArrName = "data";
//    public static String myUrl = "http://192.168.5.228:9688/WCYH/";
    //    public static String myUrl = "http://218.76.35.118:8888/WCYH/";
//    public static String myUrl="http://192.168.6.57:8080/DMSPS_WCYH/";
    //内网端口地址
//    public static String host = "http://192.168.0.13:8888/WCYH/mobile/";
//    public static String host = "http://192.168.5.228:9688/WCYH/mobile/";

//    public static String host="http://192.168.6.57:8080/WCYH/mobile/";

//    public static String host = "http://192.168.6.57:8080/DMSPS_WCYH/mobile/";

    //外网端口
//    public static String host = "http://218.76.35.118:8888/WCYH/mobile/";

    //企业端口
    public static String host = "http://222.247.60.74:8083/mobile/";
    public static String myUrl = "http://222.247.60.74:8083/";


//    public static String myUrl = "http://192.168.6.135:8080/DMSPS_WCYH/";
//    public static String host = "http://192.168.6.135:8080/DMSPS_WCYH/mobile/";

    //军
//    public static String myUrl = "http://192.168.5.79/WCYH/";
//    public static String host = "http://192.168.5.79/WCYH/mobile/";

    //智
//    public static String myUrl = "http://192.168.5.34:8080/DMSPS_WCYH/";
//    public static String host = "http://192.168.5.34:8080/DMSPS_WCYH/mobile/";

    //飞
//    public static String myUrl = "http://192.168.5.186:8080/WCYH/";
//    public static String host = "http://192.168.5.186:8080/WCYH/mobile/";
    //依
//    public static String myUrl = "http://192.168.5.87:8080/DMSPS_WCYH/";
//    public static String host = "http://192.168.5.87:8080/DMSPS_WCYH/mobile/";


    //登陆
    public static String LoginAddress() {
        return host + "login.action";
    }

    //token值
    public static String GetToken(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "user_token", "");
    }

    //userType
    public static boolean GetUserType(Context context) {
        if (SharedPrefsUtil.getValue(context, "userInfo", "usertype", "").equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public static String updateApp() {
        return host + "update/updateapp.action";
    }

    //所有企业信息
    public static String Companyinfo() {
        return host + "mobilecompanybase/getCompanyinfo.action";
    }

    //有隐患企业信息
    public static String YhCompanyinfo() {
        return host + "mobilecompanybase/getCompanyinfobyrisk.action";
    }

    //企业详细信息
    public static String CompanyDetailInfo() {
        return host + "mobilecompanybase/getCompanydetailinfo.action";
    }

    //事故快报
    public static String Accidentinfo() {
        return host + "mobileaccidentinfo/getAccidentinfo.action";
    }

    //事故详情
    public static String Accidentdetailinfo() {
        return host + "mobileaccidentinfo/getAccidentdetailinfo.action";
    }

    //事故填报接口
    public static String AddAccidentdetailinfo() {
        return host + "mobileaccidentinfo/addAccidentdetailinfo.action";
    }

    //通知公告
    public static String MessageList() {
        return host + "message/messageList.action";
    }

    //公告详情
    public static String MessageDetail() {
        return host + "message/messageInfo.action";
    }

    //隐患登记
    public static String Hidden() {
        return host + "riskinfo/saveInfo.action";
    }

    //重大隐患复查列表
    public static String RiskList() {
        return host + "riskinfo/riskList.action";
    }

    //重大隐患复查详情
    public static String RiskDetail() {
        return host + "riskinfo/riskInfo.action";
    }

    //重大隐患复查详情提交
    public static String RiskSaveInfo() {
        return host + "zdriskaccept/saveInfo.action";
    }

    //执法检查
    public static String Riskzcbzdetail() {
        return host + "riskzcbzdetail/list.action";
    }

    //危险源
    public static String GetCompanyRisk() {
        return host + "mobilecompanybase/getCompanyRisk.action";
    }

    //危险源详情
    public static String GetCompanyRiskdetail() {
        return host + "mobilecompanybase/getCompanyRiskdetail.action";

    }

    public static String GetCompanyRiskQy() {
        return host + "mobilecompanyinfo/getCompanyrisk.action";
    }

    public static String GetCompanyRiskdetailQy() {
        return host + "mobilecompanyinfo/getCompanyriskdetail.action";
    }


    //企业隐患信息
    public static String GetRiskinfo() {
        return host + "mobilecompanybase/getRiskinfo.action";
    }

    //企业隐患信息详情
    public static String GetRiskinfodetail() {
        return host + "mobilecompanybase/getRiskinfodetail.action";
    }

    public static String GetRiskinfoQy() {
        return host + "mobilecompanyinfo/getRiskinfo.action";
    }

    public static String GetRiskinfodetailQy() {
        return host + "mobilecompanyinfo/getRiskinfodetail.action";
    }


    //企业安全生产信息
    public static String GetSafechecklist() {
        return host + "mobilecompanybase/getSafechecklist.action";
    }

    //企业安全生产详情信息
    public static String GetSafechecklistdetail() {
        return host + "mobilecompanybase/getSafechecklistdetail.action";
    }


    //企业规章制度列表
    public static String GetCompanybylaw() {
        return host + "mobilecompanybase/getCompanybylaw.action";
    }

    //企业规章制度详情
    public static String GetCompanybylawdetail() {
        return host + "mobilecompanybase/getCompanybylawdetail.action";
    }

    public static String GetCompanybylawQy() {
        return host + "mobilecompanyinfo/getCompanybylaw.action";
    }

    public static String GetCompanybylawdetailQy() {
        return host + "mobilecompanyinfo/getCompanybylawdetail.action";
    }


    //法律法规列表
    public static String GetLawregulations() {
        return host + "mobilelawregulations/getLawregulations.action";
    }

    //法律法规详情
    public static String GetLawregulationsdetail() {
        return host + "mobilelawregulations/getLawregulationsdetail.action";
    }

    //分级查询风险监控
    public static String Companyreg() {
        return host + "companyreg/complist.action";
    }

    //职业危害
    public static String Contactlimit() {
        return host + "contactlimit/list.action";
    }

    public static String ContactlimitDetail() {
        return host + "contactlimit/info.action";
    }


    //高毒列表
    public static String Htoxicmatter() {
        return host + "htoxicmatter/list.action";
    }

    //高毒详情
    public static String HtoxicmatterDetail() {
        return host + "htoxicmatter/info.action";
    }


    //危化品接口
    public static String GetChemical() {
        return host + "mobilechemical/getChemical.action";
    }

    //危险性概述
    public static String GetChemicaldetailPartOne() {
        return host + "mobilechemical/getChemicaldetailPartOne.action";
    }

    //化学反应
    public static String GetChemicaldetailPartTwo() {
        return host + "mobilechemical/getChemicaldetailPartTwo.action";
    }

    //理化特性
    public static String GetChemicaldetailPartThree() {
        return host + "mobilechemical/getChemicaldetailPartThree.action";
    }

    //接触控制
    public static String GetChemicaldetailPartFour() {
        return host + "mobilechemical/getChemicaldetailPartFour.action";
    }

    //运输/废弃
    public static String GetChemicaldetailPartFive() {
        return host + "mobilechemical/getChemicaldetailPartFive.action";
    }

    //应急处理
    public static String GetChemicaldetailPartSix() {
        return host + "mobilechemical/getChemicaldetailPartSix.action";
    }

    //危险源临界值
    public static String GetHazardmatter() {
        return host + "mobilehazardmatter/getHazardmatter.action";
    }

    //危险源临界值详情
    public static String GetHazardmatterdetail() {
        return host + "mobilehazardmatter/getHazardmatterdetail.action";
    }

    //统计数据
    public static String GetTjData() {
        return host + "riskinfo/getTjData.action";
    }

    //职业卫生信息
    public static String GetOccuhealth() {
        return host + "mobilecompanybase/getOccuhealth.action";
    }

    //职业卫生详情
    public static String GetOccuhealthDetail() {
        return host + "mobilecompanybase/getOccuhealthdetailqy.action";
    }

    public static String GetOccuhealthQy() {
        return host + "mobilecompanyinfo/getOccuhealth.action";
    }

    public static String GetOccuhealthDetailQy() {
        return host + "mobilecompanyinfo/getOccuhealthdetail.action";
    }


    //应急预案信息
    public static String GetEmergencyreserve() {
        return host + "mobilecompanybase/getEmergencyreserve.action";
    }

    //应急预案详情
    public static String GetEmergencyreserveDetail() {
        return host + "mobilecompanybase/getEmergencyreservedetailqy.action";
    }

    public static String GetEmergencyreserveQy() {
        return host + "mobilecompanyinfo/getEmergencyreserve.action";
    }

    public static String GetEmergencyreserveDetailQy() {
        return host + "mobilecompanyinfo/getEmergencyreservedetail.action";
    }


    //企业安全培训
    public static String GetSafetytraining() {
        return host + "mobilecompanybase/getSafetytraining.action";
    }

    //企业安全培训详情
    public static String GetSafetytrainingDetail() {
        return host + "mobilecompanybase/getSafetytrainingdetailqy.action";
    }

    public static String GetSafetytrainingQy() {
        return host + "mobilecompanyinfo/getSafetytraining.action";
    }

    public static String GetSafetytrainingDetailQy() {
        return host + "mobilecompanyinfo/getSafetytrainingdetail.action";
    }


    //企业安全生产投入
    public static String GetSafeinvestment() {
        return host + "mobilecompanybase/getSafeinvestment.action";
    }


    //企业安全生产投入详情
    public static String GetSafeinvestmentDetail() {
        return host + "mobilecompanybase/getSafeinvestmentdetailqy.action";
    }

    public static String GetSafeinvestmentQy() {
        return host + "mobilecompanyinfo/getSafeinvestment.action";
    }

    public static String GetSafeinvestmentDetailQy() {
        return host + "mobilecompanyinfo/getSafeinvestmentdetail.action";
    }


    //行业列表
    public static String GetIndustry() {
        return host + "";
    }

    //安全机构部门管理
    public static String GetCompanydept() {
        return host + "mobilecompanyinfo/getCompanydept.action";
    }

    //安全机构部门管理详情接口
    public static String GetCompanydeptdetail() {
        return host + "mobilecompanyinfo/getCompanydeptdetail.action";
    }

    //安全人员管理
    public static String GetSafepeopleQy() {
        return host + "mobilecompanyinfo/getSafepeople.action";
    }

    //首页人员管理
    public static String GetSafepeople() {
        return host + "mobilecompanybase/getSafepeople.action";
    }


    //安全人员管理详情
    public static String GetSafepeopledetail() {
        return host + "mobilecompanyinfo/getSafepeopledetail.action";
    }

    //特种设备设施
    public static String GetSafefacilitiesQy() {
        return host + "mobilecompanyinfo/getSafefacilities.action";
    }

    //首页特种设备
    public static String GetSafefacilities() {
        return host + "mobilecompanybase/getSafefacilities.action";
    }


    //特种设备设施详情
    public static String GetSafefacilitiesdetail() {
        return host + "mobilecompanyinfo/getSafefacilitiesdetail.action";
    }

    //应急物资信息
    public static String GetEmergencymaterials() {
        return host + "mobilecompanyinfo/getEmergencymaterials.action";
    }

    //应急物资信息详情
    public static String GetEmergencymaterialsdetail() {
        return host + "mobilecompanyinfo/getEmergencymaterialsdetail.action";
    }

    //应急演练信息
    public static String GetEmergencydrill() {
        return host + "mobilecompanyinfo/getEmergencydrill.action";
    }

    //应急演练信息详情
    public static String GetEmergencydrilldetail() {
        return host + "mobilecompanyinfo/getEmergencydrilldetail.action";
    }

    //下载文件
    public static String DownLoadFiles() {
        return host + "riskinfo/downLoadFile.action";
    }


    //企业证照
    public static String GetCertificatelistQy() {
        return host + "mobilecompanyinfo/getCertificatelist.action";
    }

    public static String GetCertificatelist() {
        return host + "mobilecompanybase/getCertificatelist.action";
    }

    //企业证照详情
    public static String GetCertificateDetail() {
        return host + "mobilecompanyinfo/getCertificatelistdetail.action";
    }


}
