<%@ page import="sesion.ClinicalSession" %>
<g:if test="${flash.message}">
  <div class="message" role="status">${flash.message}</div>
</g:if>

<!--
TODO: busqueda de pacientes por nombre
-->

<table>
  <tr>
    <th><g:message code="patient.list_table.field.uid" /></th>
    <th><g:message code="patient.list_table.field.firstName" /></th>
    <th><g:message code="patient.list_table.field.lastName" /></th>
    <th><g:message code="patient.list_table.field.dob" /></th>
    <th><g:message code="patient.list_table.field.sex" /></th>
    <th><g:message code="patient.list_table.field.id" /></th>
    <th><g:message code="patient.list_table.field.idType" /></th>
    <th></th>
  </tr>
  <g:each in="${patientList}" var="pat">
   <tr>
     <td>${pat.uid}</td>
     <td>${pat.firstName}</td>
     <td>${pat.lastName}</td>
     <td>${pat.dob}</td>
     <td>${pat.sex}</td>
     <td>${pat.idCode}</td>
     <td>${pat.idType}</td>
     <td>
       <g:set var="cses" value="${ClinicalSession.findByPatientUidAndOpen(pat.uid, true)}" />
       <g:if test="${cses}">
         <g:link controller="registros" action="continueSession" id="${cses.id}"><g:message code="patient.list_table.action.continue" /></g:link>
       </g:if>
       <g:else>
         <g:form method="post" controller="registros" action="openSession">
           <input type="hidden" name="patientUid" value="${pat.uid}" />
           <g:submitButton name="doit" value="${g.message(code:'patient.list_table.action.newSession')}" />
         </g:form>
       </g:else>
       
       <!-- necesito el submit por los datos del paciente -->
       <g:form method="post" controller="registros" action="list">
         <input type="hidden" name="patientUid" value="${pat.uid}" />
         <g:submitButton name="doit" value="${g.message(code:'patient.list_table.action.history')}" controller="registros" action="list" />
       </g:form>
     </td>
   </tr>
  </g:each>
</table>

<!--
TODO: paginacion
-->