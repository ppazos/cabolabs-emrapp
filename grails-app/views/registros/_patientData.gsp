<%@ page import="util.DateDifference" %>
<div id="datosPaciente" class="hidden_uid sex_${sex}">
  ${firstName} ${lastName}
  (${DateDifference.numberOfYears(Date.parse(grailsApplication.config.app.l10n.date_format, dob), new Date())})
  <span class="uid">uid: ${uid}</span>
</div>