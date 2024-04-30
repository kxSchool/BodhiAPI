package com.example.modules.simulation.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SimulationDataService {
    public Map<String,String> setPatientsMap(String organization_id, String patient_id, String insurance_id, String insurance_type, String name
            , String name_spell, String mother_name, String sex, String birth_date, String birth_place
            , String nation, String citizenship, String marital_status, String IdentityType
            , String id_card_no, String IdentityID, String health_card_no
            , String contact_phone_no, String email, String address_province, String address_city
            , String address_district, String address_street, String address_road, String address_detail
            , String postalcode, String occupation, String work_unit, String language
            , String updateDateTime, String pathNo
    ){
        Map<String,String> patientInfo1=new HashMap<>();
        patientInfo1.put("organization_id",organization_id);
        patientInfo1.put("patient_id",patient_id);
        patientInfo1.put("insurance_id",insurance_id);
        patientInfo1.put("insurance_type",insurance_type);
        patientInfo1.put("name",name);
        patientInfo1.put("name_spell",name_spell);
        patientInfo1.put("mother_name",mother_name);
        patientInfo1.put("sex",sex);
        patientInfo1.put("birth_date",birth_date);
        patientInfo1.put("birth_place",birth_place);
        patientInfo1.put("nation",nation);
        patientInfo1.put("citizenship",citizenship);
        patientInfo1.put("marital_status",marital_status);
        patientInfo1.put("IdentityType",IdentityType);
        patientInfo1.put("id_card_no",id_card_no);
        patientInfo1.put("IdentityID",IdentityID);
        patientInfo1.put("health_card_no",health_card_no);
        patientInfo1.put("contact_phone_no",contact_phone_no);
        patientInfo1.put("email",email);
        patientInfo1.put("address_province",address_province);
        patientInfo1.put("address_city",address_city);
        patientInfo1.put("address_district",address_district);
        patientInfo1.put("address_street",address_street);
        patientInfo1.put("address_road",address_road);
        patientInfo1.put("address_detail",address_detail);
        patientInfo1.put("postalcode",postalcode);
        patientInfo1.put("occupation",occupation);
        patientInfo1.put("work_unit",work_unit);
        patientInfo1.put("language",language);
        patientInfo1.put("updateDateTime",updateDateTime);
        patientInfo1.put("pathNo",pathNo);
        return patientInfo1;
    }


    public Map<String,Object> setRegisterMap(String patient_id, String patient_master_id, String patient_class, String wdbz,
                                             String med_rec_no, String out_patient_no, String in_patient_no, String JZLSH,
                                             String order_uid, String organization_id, String organization_name, String placer_order_no,
                                             String pathological_number, String project_id, String project_text, String procedure_id,
                                             String procedure_name,
                                             String send_doctor_id, String send_doctor_name, String send_doctor_phone, String send_dept_id,
                                             String send_dept_name, String send_date, String send_organization_id, String send_organization_name,
                                             String symptom, String adverse_reaction, String clinic_diagnosis, String surgery_info,
                                             String relevant_clinical_info, String collector_id, String collector_name, String collection_dept,
                                             String collection_method, String collection_volume, String collection_date, String specimen_received_date,
                                             String specimen_received_id, String specimen_received_name, String result_status, String result_status_code,
                                             String reg_time, String register_id, String register_name, String based_time,
                                             String based_id, String based_name, String based_record_id, String based_record_name,
                                             String autopsy, String production_time, String production_id, String production_name,
                                             String result_assistant_time, String result_assistant_id, String result_assistant_name, String result_principal_id,
                                             String result_principal_name, String result_principal_time, String abnormal_flags, String critical_value,
                                             String scopically_seen, String pathological_diagnosis, String tumor_classification, String cancer_classification,
                                             String cancer_staging, String updateDateTime, String pathNo, Map<String,String> Specimen_list){
        Map<String,Object> registerInfo1=new HashMap<>();
        registerInfo1.put("patient_id",patient_id);
        registerInfo1.put("patient_master_id",patient_master_id);
        registerInfo1.put("patient_class",patient_class);
        registerInfo1.put("wdbz",wdbz);
        registerInfo1.put("med_rec_no",med_rec_no);
        registerInfo1.put("out_patient_no",out_patient_no);
        registerInfo1.put("in_patient_no",in_patient_no);
        registerInfo1.put("JZLSH",JZLSH);
        registerInfo1.put("order_uid",order_uid);
        registerInfo1.put("organization_id",organization_id);
        registerInfo1.put("organization_name",organization_name);
        registerInfo1.put("placer_order_no",placer_order_no);
        registerInfo1.put("pathological_number",pathological_number);
        registerInfo1.put("project_id",project_id);
        registerInfo1.put("project_text",project_text);
        registerInfo1.put("procedure_id",procedure_id);
        registerInfo1.put("procedure_name",procedure_name);
        registerInfo1.put("send_doctor_id",send_doctor_id);
        registerInfo1.put("send_doctor_name",send_doctor_name);
        registerInfo1.put("send_doctor_phone",send_doctor_phone);
        registerInfo1.put("send_dept_id",send_dept_id);
        registerInfo1.put("send_dept_name",send_dept_name);
        registerInfo1.put("send_date",send_date);
        registerInfo1.put("send_organization_id",send_organization_id);
        registerInfo1.put("send_organization_name",send_organization_name);
        registerInfo1.put("symptom",symptom);
        registerInfo1.put("adverse_reaction",adverse_reaction);
        registerInfo1.put("clinic_diagnosis",clinic_diagnosis);
        registerInfo1.put("surgery_info",surgery_info);
        registerInfo1.put("relevant_clinical_info",relevant_clinical_info);
        registerInfo1.put("collector_id",collector_id);
        registerInfo1.put("collector_name",collector_name);
        registerInfo1.put("collection_dept",collection_dept);
        registerInfo1.put("collection_method",collection_method);
        registerInfo1.put("collection_volume",collection_volume);
        registerInfo1.put("collection_date",collection_date);
        registerInfo1.put("specimen_received_date",specimen_received_date);
        registerInfo1.put("specimen_received_id",specimen_received_id);
        registerInfo1.put("specimen_received_name",specimen_received_name);
        registerInfo1.put("result_status",result_status);
        registerInfo1.put("result_status_code",result_status_code);
        registerInfo1.put("reg_time",reg_time);
        registerInfo1.put("register_id",register_id);
        registerInfo1.put("register_name",register_name);
        registerInfo1.put("based_time",based_time);
        registerInfo1.put("based_id",based_id);
        registerInfo1.put("based_name",based_name);
        registerInfo1.put("based_record_id",based_record_id);
        registerInfo1.put("based_record_name",based_record_name);
        registerInfo1.put("autopsy",autopsy);
        registerInfo1.put("production_time",production_time);
        registerInfo1.put("production_id",production_id);
        registerInfo1.put("production_name",production_name);
        registerInfo1.put("result_assistant_time",result_assistant_time);
        registerInfo1.put("result_assistant_id",result_assistant_id);
        registerInfo1.put("result_assistant_name",result_assistant_name);
        registerInfo1.put("result_principal_id",result_principal_id);
        registerInfo1.put("result_principal_name",result_principal_name);
        registerInfo1.put("result_principal_time",result_principal_time);
        registerInfo1.put("abnormal_flags",abnormal_flags);
        registerInfo1.put("critical_value",critical_value);
        registerInfo1.put("scopically_seen",scopically_seen);
        registerInfo1.put("pathological_diagnosis",pathological_diagnosis);
        registerInfo1.put("tumor_classification",tumor_classification);
        registerInfo1.put("cancer_classification",cancer_classification);
        registerInfo1.put("cancer_staging",cancer_staging);
        registerInfo1.put("updateDateTime",updateDateTime);
        registerInfo1.put("pathNo",pathNo);
        registerInfo1.put("Specimen_list",Specimen_list);
        return registerInfo1;
    }

}
