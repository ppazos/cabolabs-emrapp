<%@ page import="sesion.ClinicalSession" %><!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="patient.list.label" /></title>
    <g:javascript src="jquery-1.8.2.min.js" />
    <g:javascript src="jquery.form-3.18.js" />
    <g:javascript>
    </g:javascript>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="clinicalSession" action="list"><g:message code="patient.list.action.sessions" /></g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    <div id="list-document" class="content scaffold-list" role="main">
      <h1><g:message code="patient.list.label" /></h1>
      
      <g:if test="${flash.message}">
		  <div class="message" role="status">${flash.message}</div>
		</g:if>
		
		<!--
		TODO: busqueda de pacientes por nombre
		-->
		
		<g:link controller="patient" action="create">Add patient</g:link>
		
		<table>
		  <tr>
		    <th><g:message code="patient.list_table.field.uid" /></th>
		    <th><g:message code="patient.list_table.field.firstName" /></th>
		    <th><g:message code="patient.list_table.field.lastName" /></th>
		    <th><g:message code="patient.list_table.field.dob" /></th>
		    <th><g:message code="patient.list_table.field.sex" /></th>
		    <th></th>
		  </tr>
		  <g:each in="${patientList}" var="pat">
		   <tr>
		     <td>${pat.uid}</td>
		     <td>${pat.firstname}</td>
		     <td>${pat.lastname}</td>
		     <td>${pat.dob}</td>
		     <td>${pat.sex}</td>
		     <td>
		       <g:set var="cses" value="${ClinicalSession.findByPatientAndOpen(pat, true)}" />
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
    </div>
  </body>
</html>
