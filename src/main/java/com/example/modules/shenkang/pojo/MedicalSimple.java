package com.example.modules.shenkang.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MedicalSimple {
    /**
     *
     */
    private Integer id;

    /**
     * 患者编号
     */
    private String patient_id="";

    /**
     * 患者主索引
     */
    private String patient_master_id="";

    /**
     * 患者类型
     */
    private String patient_class="";

    /**
     *
     */
    private String wdbz="";

    /**
     * 病历号（加密）
     */
    private String med_rec_no="";

    /**
     * 门诊号（加密）
     */
    private String out_patient_no="";

    /**
     * 住院号（加密）
     */
    private String in_patient_no="";

    /**
     *
     */
    private String JZLSH="";

    /**
     * 关联医嘱号
     */
    private String order_uid="";

    /**
     * 检查机构代码
     */
    private String organization_id="";

    /**
     * 检查机构名称
     */
    private String organization_name="";

    /**
     * 电子申请单号（加密）
     */
    private String placer_order_no="";

    /**
     * 病理号
     */
    private String pathological_number="";

    /**
     * 检查项目ID
     */
    private String project_id="";

    /**
     * 检查项目名称
     */
    private String project_text="";

    /**
     * 检查部位ID
     */
    private String procedure_id="";

    /**
     * 检查部位名称
     */
    private String procedure_name="";

    /**
     * 送检医生ID
     */
    private String send_doctor_id="";

    /**
     * 送检医生姓名
     */
    private String send_doctor_name="";

    /**
     * 送检医生电话
     */
    private String send_doctor_phone="";

    /**
     * 送检科室ID
     */
    private String send_dept_id="";

    /**
     * 送检科室名称
     */
    private String send_dept_name="";

    /**
     * 送检时间
     */
    private String send_date="";

    /**
     * 送检机构代码
     */
    private String send_organization_id="";

    /**
     * 送检机构名称
     */
    private String send_organization_name="";

    /**
     * 患者症状体征
     */
    private String symptom="";

    /**
     * 过敏及不良反应
     */
    private String adverse_reaction="";

    /**
     * 临床诊断
     */
    private String clinic_diagnosis="";

    /**
     * 手术所见
     */
    private String surgery_info="";

    /**
     * 其它相关的临床信息
     */
    private String relevant_clinical_info="";

    /**
     * 标本采集者ID
     */
    private String collector_id="";

    /**
     * 标本采集者姓名
     */
    private String collector_name="";

    /**
     * 标本采集科室
     */
    private String collection_dept="";

    /**
     * 标本采集方法
     */
    private String collection_method="";

    /**
     * 标本采集容量
     */
    private String collection_volume="";

    /**
     * 标本采集时间
     */
    private String collection_date="";

    /**
     * 标本接收时间
     */
    private String specimen_received_date="";

    /**
     * 标本接收人ID
     */
    private String specimen_received_id="";

    /**
     * 标本接收姓名
     */
    private String specimen_received_name="";

    /**
     * 检查状态名称
     */
    private String result_status="";

    /**
     * 检查状态代码
     */
    private String result_status_code="";

    /**
     * 登记时间
     */
    private String reg_time="";

    /**
     * 登记员ID
     */
    private String register_id="";

    /**
     * 登记员姓名
     */
    private String register_name="";

    /**
     * 取材时间
     */
    private String based_time="";

    /**
     * 取材医生ID
     */
    private String based_id="";

    /**
     * 取材医生姓名
     */
    private String based_name="";

    /**
     * 取材记录人ID
     */
    private String based_record_id="";

    /**
     * 取材记录人姓名
     */
    private String based_record_name="";

    /**
     * 巨检所见
     */
    private String autopsy="";

    /**
     * 制片时间
     */
    private String production_time="";

    /**
     * 制片人ID
     */
    private String production_id="";

    /**
     * 制片人姓名
     */
    private String production_name="";

    /**
     * 报告时间
     */
    private String result_assistant_time="";

    /**
     * 报告医生ID
     */
    private String result_assistant_id="";

    /**
     * 报告医生姓名
     */
    private String result_assistant_name="";

    /**
     * 审核时间
     */
    private String result_principal_time="";


    public String getResult_principal_time() {
        return result_principal_time;
    }

    public void setResult_principal_time(String result_principal_time) {
        this.result_principal_time = result_principal_time;
    }

    /**
     * 审核医生ID
     */
    private String result_principal_id="";

    /**
     * 审核医生姓名
     */
    private String result_principal_name="";

    /**
     * 阴阳性
     */
    private String abnormal_flags="";

    /**
     * 危急值
     */
    private String critical_value="";

    /**
     * 镜下所见
     */
    private String scopically_seen="";

    /**
     * 病理诊断
     */
    private String pathological_diagnosis="";

    /**
     * 关联标本ID
     */
    private Integer specimen_id;

    /**
     * 检查唯一号
     */
    private String check_no="";

    /**
     * 病种Id
     */
    private Integer medical_id;

    /**
     * 病种名称
     */
    private String medical_name="";

    /**
     * 肿瘤分类种类
     */
    private String tumor_classification="";

    /**
     * 癌症分级种类
     */
    private String cancer_classification="";

    /**
     * 癌症分期种类
     */
    private String cancer_staging="";

    /**
     *
     */
    private String createTime="";

    /**
     *
     */
    private String modifyTime="";
    private String recontent="";
    private String checkNo="";
    private List<SpecimenInfo> Specimen_list;
    private String pathNo;

    public String getPathNo() {
        return pathNo;
    }

    public void setPathNo(String pathNo) {
        this.pathNo = pathNo;
    }
    @JsonProperty("Specimen_list")
    public List<SpecimenInfo> getSpecimen_list() {
        return Specimen_list;
    }

    public void setSpecimen_list(List<SpecimenInfo> specimen_list) {
        Specimen_list = specimen_list;
    }

    public String getRecontent() {
        return recontent;
    }

    public void setRecontent(String recontent) {
        this.recontent = recontent;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_master_id() {
        return patient_master_id;
    }

    public void setPatient_master_id(String patient_master_id) {
        this.patient_master_id = patient_master_id;
    }

    public String getPatient_class() {
        return patient_class;
    }

    public void setPatient_class(String patient_class) {
        this.patient_class = patient_class;
    }

    public String getWdbz() {
        return wdbz;
    }

    public void setWdbz(String wdbz) {
        this.wdbz = wdbz;
    }

    public String getMed_rec_no() {
        return med_rec_no;
    }

    public void setMed_rec_no(String med_rec_no) {
        this.med_rec_no = med_rec_no;
    }

    public String getOut_patient_no() {
        return out_patient_no;
    }

    public void setOut_patient_no(String out_patient_no) {
        this.out_patient_no = out_patient_no;
    }

    public String getIn_patient_no() {
        return in_patient_no;
    }

    public void setIn_patient_no(String in_patient_no) {
        this.in_patient_no = in_patient_no;
    }

    @JsonProperty("JZLSH")
    public String getJZLSH() {
        return JZLSH;
    }

    public void setJZLSH(String JZLSH) {
        this.JZLSH = JZLSH;
    }

    public String getOrder_uid() {
        return order_uid;
    }

    public void setOrder_uid(String order_uid) {
        this.order_uid = order_uid;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getPlacer_order_no() {
        return placer_order_no;
    }

    public void setPlacer_order_no(String placer_order_no) {
        this.placer_order_no = placer_order_no;
    }

    public String getPathological_number() {
        return pathological_number;
    }

    public void setPathological_number(String pathological_number) {
        this.pathological_number = pathological_number;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_text() {
        return project_text;
    }

    public void setProject_text(String project_text) {
        this.project_text = project_text;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public String getProcedure_name() {
        return procedure_name;
    }

    public void setProcedure_name(String procedure_name) {
        this.procedure_name = procedure_name;
    }

    public String getSend_doctor_id() {
        return send_doctor_id;
    }

    public void setSend_doctor_id(String send_doctor_id) {
        this.send_doctor_id = send_doctor_id;
    }

    public String getSend_doctor_name() {
        return send_doctor_name;
    }

    public void setSend_doctor_name(String send_doctor_name) {
        this.send_doctor_name = send_doctor_name;
    }

    public String getSend_doctor_phone() {
        return send_doctor_phone;
    }

    public void setSend_doctor_phone(String send_doctor_phone) {
        this.send_doctor_phone = send_doctor_phone;
    }

    public String getSend_dept_id() {
        return send_dept_id;
    }

    public void setSend_dept_id(String send_dept_id) {
        this.send_dept_id = send_dept_id;
    }

    public String getSend_dept_name() {
        return send_dept_name;
    }

    public void setSend_dept_name(String send_dept_name) {
        this.send_dept_name = send_dept_name;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getSend_organization_id() {
        return send_organization_id;
    }

    public void setSend_organization_id(String send_organization_id) {
        this.send_organization_id = send_organization_id;
    }

    public String getSend_organization_name() {
        return send_organization_name;
    }

    public void setSend_organization_name(String send_organization_name) {
        this.send_organization_name = send_organization_name;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getAdverse_reaction() {
        return adverse_reaction;
    }

    public void setAdverse_reaction(String adverse_reaction) {
        this.adverse_reaction = adverse_reaction;
    }

    public String getClinic_diagnosis() {
        return clinic_diagnosis;
    }

    public void setClinic_diagnosis(String clinic_diagnosis) {
        this.clinic_diagnosis = clinic_diagnosis;
    }

    public String getSurgery_info() {
        return surgery_info;
    }

    public void setSurgery_info(String surgery_info) {
        this.surgery_info = surgery_info;
    }

    public String getRelevant_clinical_info() {
        return relevant_clinical_info;
    }

    public void setRelevant_clinical_info(String relevant_clinical_info) {
        this.relevant_clinical_info = relevant_clinical_info;
    }

    public String getCollector_id() {
        return collector_id;
    }

    public void setCollector_id(String collector_id) {
        this.collector_id = collector_id;
    }

    public String getCollector_name() {
        return collector_name;
    }

    public void setCollector_name(String collector_name) {
        this.collector_name = collector_name;
    }

    public String getCollection_dept() {
        return collection_dept;
    }

    public void setCollection_dept(String collection_dept) {
        this.collection_dept = collection_dept;
    }

    public String getCollection_method() {
        return collection_method;
    }

    public void setCollection_method(String collection_method) {
        this.collection_method = collection_method;
    }

    public String getCollection_volume() {
        return collection_volume;
    }

    public void setCollection_volume(String collection_volume) {
        this.collection_volume = collection_volume;
    }

    public String getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(String collection_date) {
        this.collection_date = collection_date;
    }

    public String getSpecimen_received_date() {
        return specimen_received_date;
    }

    public void setSpecimen_received_date(String specimen_received_date) {
        this.specimen_received_date = specimen_received_date;
    }

    public String getSpecimen_received_id() {
        return specimen_received_id;
    }

    public void setSpecimen_received_id(String specimen_received_id) {
        this.specimen_received_id = specimen_received_id;
    }

    public String getSpecimen_received_name() {
        return specimen_received_name;
    }

    public void setSpecimen_received_name(String specimen_received_name) {
        this.specimen_received_name = specimen_received_name;
    }

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getResult_status_code() {
        return result_status_code;
    }

    public void setResult_status_code(String result_status_code) {
        this.result_status_code = result_status_code;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getRegister_id() {
        return register_id;
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getRegister_name() {
        return register_name;
    }

    public void setRegister_name(String register_name) {
        this.register_name = register_name;
    }

    public String getBased_time() {
        return based_time;
    }

    public void setBased_time(String based_time) {
        this.based_time = based_time;
    }

    public String getBased_id() {
        return based_id;
    }

    public void setBased_id(String based_id) {
        this.based_id = based_id;
    }

    public String getBased_name() {
        return based_name;
    }

    public void setBased_name(String based_name) {
        this.based_name = based_name;
    }

    public String getBased_record_id() {
        return based_record_id;
    }

    public void setBased_record_id(String based_record_id) {
        this.based_record_id = based_record_id;
    }

    public String getBased_record_name() {
        return based_record_name;
    }

    public void setBased_record_name(String based_record_name) {
        this.based_record_name = based_record_name;
    }

    public String getAutopsy() {
        return autopsy;
    }

    public void setAutopsy(String autopsy) {
        this.autopsy = autopsy;
    }

    public String getProduction_time() {
        return production_time;
    }

    public void setProduction_time(String production_time) {
        this.production_time = production_time;
    }

    public String getProduction_id() {
        return production_id;
    }

    public void setProduction_id(String production_id) {
        this.production_id = production_id;
    }

    public String getProduction_name() {
        return production_name;
    }

    public void setProduction_name(String production_name) {
        this.production_name = production_name;
    }

    public String getResult_assistant_time() {
        return result_assistant_time;
    }

    public void setResult_assistant_time(String result_assistant_time) {
        this.result_assistant_time = result_assistant_time;
    }

    public String getResult_assistant_id() {
        return result_assistant_id;
    }

    public void setResult_assistant_id(String result_assistant_id) {
        this.result_assistant_id = result_assistant_id;
    }

    public String getResult_assistant_name() {
        return result_assistant_name;
    }

    public void setResult_assistant_name(String result_assistant_name) {
        this.result_assistant_name = result_assistant_name;
    }


    public String getResult_principal_id() {
        return result_principal_id;
    }

    public void setResult_principal_id(String result_principal_id) {
        this.result_principal_id = result_principal_id;
    }

    public String getResult_principal_name() {
        return result_principal_name;
    }

    public void setResult_principal_name(String result_principal_name) {
        this.result_principal_name = result_principal_name;
    }

    public String getAbnormal_flags() {
        return abnormal_flags;
    }

    public void setAbnormal_flags(String abnormal_flags) {
        this.abnormal_flags = abnormal_flags;
    }

    public String getCritical_value() {
        return critical_value;
    }

    public void setCritical_value(String critical_value) {
        this.critical_value = critical_value;
    }

    public String getScopically_seen() {
        return scopically_seen;
    }

    public void setScopically_seen(String scopically_seen) {
        this.scopically_seen = scopically_seen;
    }

    public String getPathological_diagnosis() {
        return pathological_diagnosis;
    }

    public void setPathological_diagnosis(String pathological_diagnosis) {
        this.pathological_diagnosis = pathological_diagnosis;
    }

    public Integer getSpecimen_id() {
        return specimen_id;
    }

    public void setSpecimen_id(Integer specimen_id) {
        this.specimen_id = specimen_id;
    }

    public String getCheck_no() {
        return check_no;
    }

    public void setCheck_no(String check_no) {
        this.check_no = check_no;
    }

    public Integer getMedical_id() {
        return medical_id;
    }

    public void setMedical_id(Integer medical_id) {
        this.medical_id = medical_id;
    }

    public String getMedical_name() {
        return medical_name;
    }

    public void setMedical_name(String medical_name) {
        this.medical_name = medical_name;
    }

    public String getTumor_classification() {
        return tumor_classification;
    }

    public void setTumor_classification(String tumor_classification) {
        this.tumor_classification = tumor_classification;
    }

    public String getCancer_classification() {
        return cancer_classification;
    }

    public void setCancer_classification(String cancer_classification) {
        this.cancer_classification = cancer_classification;
    }

    public String getCancer_staging() {
        return cancer_staging;
    }

    public void setCancer_staging(String cancer_staging) {
        this.cancer_staging = cancer_staging;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

}
