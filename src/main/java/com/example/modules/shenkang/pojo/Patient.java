package com.example.modules.shenkang.pojo;

public class Patient {
    //
    private Integer id;
    private String organization_id="";
    private String patient_id="";
    //无
    private String patient_master_id="";
    private String name="";
    private String name_spell="";
    private String mother_name="";
    //无
    private String age="";
    //无
    private String agepart="";
    private String sex="";
    private String birth_date="";
    private String birth_place="";
    private String nation="";
    private String citizenship="";
    private String marital_status="";
    private String IdentityType="";
    private String IdentityID="";
    private String id_card_no="";
    private String health_card_no="";
    private String contact_phone_no="";
    private String email="";
    private String address_province="";
    private String address_city="";
    private String address_district="";
    private String address_street="";
    private String address_road="";
    private String address_detail="";
    private String postalcode="";
    private String occupation="";
    private String work_unit="";
    private String language="";
    private String pathNo;

    public String getPathNo() {
        return pathNo;
    }

    public void setPathNo(String pathNo) {
        this.pathNo = pathNo;
    }
    //无
    private String insurance_type="";
    //无
    private String insurance_id="";
    //无
    private String createTime="";
    //无
    private String modifyTime="";
    //无
    private String YLY1="";
    //无
    private String YLY2="";

    public Patient(String patient_id, String name, String name_spell, String mother_name, String sex, String birth_date, String birth_place, String nation, String citizenship, String marital_status, String id_card_no, String health_card_no) {
        this.patient_id = patient_id;
        this.name = name;
        this.name_spell = name_spell;
        this.mother_name = mother_name;
        this.sex = sex;
        this.birth_date = birth_date;
        this.birth_place = birth_place;
        this.nation = nation;
        this.citizenship = citizenship;
        this.marital_status = marital_status;
        this.id_card_no = id_card_no;
        this.health_card_no = health_card_no;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatient_master_id() {
        return patient_master_id;
    }

    public void setPatient_master_id(String patient_master_id) {
        this.patient_master_id = patient_master_id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgepart() {
        return agepart;
    }

    public void setAgepart(String agepart) {
        this.agepart = agepart;
    }

    public String getIdentityType() {
        return IdentityType;
    }

    public void setIdentityType(String identityType) {
        IdentityType = identityType;
    }

    public String getIdentityID() {
        return IdentityID;
    }

    public void setIdentityID(String identityID) {
        IdentityID = identityID;
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

    public String getYLY1() {
        return YLY1;
    }

    public void setYLY1(String YLY1) {
        this.YLY1 = YLY1;
    }

    public String getYLY2() {
        return YLY2;
    }

    public void setYLY2(String YLY2) {
        this.YLY2 = YLY2;
    }

    public Patient() {
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_spell() {
        return name_spell;
    }

    public void setName_spell(String name_spell) {
        this.name_spell = name_spell;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getHealth_card_no() {
        return health_card_no;
    }

    public void setHealth_card_no(String health_card_no) {
        this.health_card_no = health_card_no;
    }

    public String getContact_phone_no() {
        return contact_phone_no;
    }

    public void setContact_phone_no(String contact_phone_no) {
        this.contact_phone_no = contact_phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress_province() {
        return address_province;
    }

    public void setAddress_province(String address_province) {
        this.address_province = address_province;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_district() {
        return address_district;
    }

    public void setAddress_district(String address_district) {
        this.address_district = address_district;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_road() {
        return address_road;
    }

    public void setAddress_road(String address_road) {
        this.address_road = address_road;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWork_unit() {
        return work_unit;
    }

    public void setWork_unit(String work_unit) {
        this.work_unit = work_unit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getInsurance_type() {
        return insurance_type;
    }

    public void setInsurance_type(String insurance_type) {
        this.insurance_type = insurance_type;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }
}
