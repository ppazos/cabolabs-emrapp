<%@ page import="sesion.ClinicalSession" %>
<g:if test="${flash.message}">
  <div class="message" role="status">${flash.message}</div>
</g:if>

<!--
TODO: busqueda de pacientes por nombre
-->

<table>
  <tr>
    <th>uid</th>
    <th>first name</th>
    <th>last name</th>
    <th>dob</th>
    <th>sex</th>
    <th>id</th>
    <th>id type</th>
    <th></th>
  </tr>
  <g:each in="${patientList}" var="pat">
    <g:form controller="registros" action="openSession">
      <input type="hidden" name="patientUid" value="${pat.uid}" />
	   <tr>
	     <td>${pat.uid}<input type="hidden" name="datosPaciente.uid" value="${pat.uid}" /></td>
	     <td>${pat.firstName}<input type="hidden" name="datosPaciente.firstName" value="${pat.firstName}" /></td>
	     <td>${pat.lastName}<input type="hidden" name="datosPaciente.lastName" value="${pat.lastName}" /></td>
	     <td>${pat.dob}<input type="hidden" name="datosPaciente.dob" value="${pat.dob}" /></td>
	     <td>${pat.sex}<input type="hidden" name="datosPaciente.sex" value="${pat.sex}" /></td>
	     <td>${pat.idCode}<input type="hidden" name="datosPaciente.idCode" value="${pat.idCode}" /></td>
	     <td>${pat.idType}<input type="hidden" name="datosPaciente.idType" value="${pat.idType}" /></td>
	     <td>
	       <g:set var="cses" value="${ClinicalSession.findByPatientUidAndOpen(pat.uid, true)}" />
	       <g:if test="${cses}">
	         <g:link controller="registros" action="continueSession" id="${cses.id}">continuar</g:link>
	       </g:if>
	       <g:else>
	         <g:submitButton value="nueva sesión" name="doit" />
	       </g:else>
	     </td>
	   </tr>
	 </g:form>
  </g:each>
</table>

<!--
TODO: paginacion
-->