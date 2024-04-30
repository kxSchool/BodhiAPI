package com.example.modules.wnhis.pojo;

/**
* @author  作者 : 小布
* @version 创建时间：2019年3月13日 上午10:07:03
* @explain 类说明 : 用户表
*/
public class SysUser{

    private Integer userId;
    //@NotNull(message = "账号不能为空")
    //@Length(min = 2, max = 10, message = "fullName 长度必须在 {min} - {max} 之间")
    private String fullName;
    private String jobnum;
    private String tel;
    private String password;
    private String salt;
    private Integer state;
    private String sex;
    private Integer hospitalDistrict;
    private String hospitalDistrictName;
    private Integer department;
    private String departmentName;
    private String email;
    private String idCard;
    private Integer professional;
    private String professionalName;
    private String entryYear;
    private String question;
    private String answer;
    private String jobcode;

    private Integer roleId;
    private String roleName;

	public String getJobcode() {
		return jobcode;
	}
	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}
	public String getHospitalDistrictName() {
		return hospitalDistrictName;
	}
	public void setHospitalDistrictName(String hospitalDistrictName) {
		this.hospitalDistrictName = hospitalDistrictName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getProfessionalName() {
		return professionalName;
	}
	public void setProfessionalName(String professionalName) {
		this.professionalName = professionalName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getHospitalDistrict() {
		return hospitalDistrict;
	}
	public void setHospitalDistrict(Integer hospitalDistrict) {
		this.hospitalDistrict = hospitalDistrict;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getProfessional() {
		return professional;
	}
	public void setProfessional(Integer professional) {
		this.professional = professional;
	}
	public String getEntryYear() {
		return entryYear;
	}
	public void setEntryYear(String entryYear) {
		this.entryYear = entryYear;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}




}
