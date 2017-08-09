<%@ page import="demographic.Patient" %>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'firstname', 'error')} required">
   <label for="firstname">
      <g:message code="patient.firstname.label" default="Firstname" />
      <span class="required-indicator">*</span>
   </label>
   <g:textField name="firstname" required="" value="${patientInstance?.firstname}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'lastname', 'error')} required">
   <label for="lastname">
      <g:message code="patient.lastname.label" default="Lastname" />
      <span class="required-indicator">*</span>
   </label>
   <g:textField name="lastname" required="" value="${patientInstance?.lastname}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'dob', 'error')} ">
  <label for="dob">
    <g:message code="patient.dob.label" default="Dob" />
  </label>
  <g:datePicker name="dob" precision="day"  value="${patientInstance?.dob}" default="none" noSelection="['': '']" />

</div>

<%-- ehrUId is set by the server
<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'ehrUid', 'error')} ">
  <label for="ehrUid">
    <g:message code="patient.ehrUid.label" default="Ehr Uid" />
  </label>
  <g:textField name="ehrUid" value="${patientInstance?.ehrUid}"/>
</div>
--%>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'sex', 'error')} required">
  <label for="sex">
    <g:message code="patient.sex.label" default="Sex" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="sex" required="" value="${patientInstance?.sex}"/>
</div>
