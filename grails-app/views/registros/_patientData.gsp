<%@ page import="util.DateDifference" %><%@ page import="java.util.Date" %><%@ page import="grails.util.Holders" %>

<div id="datosPaciente" class="hidden_uid sex_${patientInstance.sex}">
  ${patientInstance.firstname} ${patientInstance.lastname}
  <g:if test="${dob}">
    (${DateDifference.numberOfYears(Date.parse(Holders.config.app.l10n.date_format, patientInstance.dob), new Date())})
  </g:if>
  <span class="uid"><g:message code="registros.patientData.label.uid" />: ${patientInstance.uid}</span>
</div>