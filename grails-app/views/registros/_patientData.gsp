<%@ page import="util.DateDifference" %><%@ page import="java.util.Date" %>
<div id="datosPaciente" class="hidden_uid sex_${sex}">
  ${firstName} ${lastName}
  (${DateDifference.numberOfYears(Date.parse(grailsApplication.config.app.l10n.date_format, dob), new Date())})
  <span class="uid"><g:message code="registros.patientData.label.uid" />: ${uid}</span>
</div>