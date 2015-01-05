<%@ page import="sesion.ClinicalSession" %>
<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="registros.list.label" /></title>
    <style>
      body {
        padding: 10px;
        margin-top: 10px;
      }
      .content {
        padding: 10px;
      }
      .help {
        background-color: #eee;
        font-size: 11px;
        padding: 5px;
      }
      #sign {
        text-align: right;
        padding: 15px;
      }
      #sign a {
        background-color: #ddd;
        padding: 5px 10px;
      }
    </style>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
    <g:javascript src="jquery-1.8.2.min.js" />
    <g:javascript src="jquery.blockUI.js" />
    <g:javascript>
      $(document).ready(function() {
      });
    </g:javascript>
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="patient" action="list"><g:message code="registros.currentSession.action.patients" /></g:link></li>
        <li><g:link class="list" controller="clinicalSession" action="list"><g:message code="registros.currentSession.action.sessions" /></g:link></li>
        <li>
          <g:set var="cses" value="${ClinicalSession.findByPatientUidAndOpen(params.patientUid, true)}" />
          <g:if test="${cses}">
            <g:link class="create" controller="registros" action="continueSession" id="${cses.id}"><g:message code="registros.list.action.continueSession" /></g:link>
          </g:if>
          <g:else>
            <g:form method="post" controller="registros" action="openSession">
              <input type="hidden" name="patientUid" value="${params.patientUid}" />
              <g:submitButton name="doit" value="${g.message(code:'registros.list.action.newSession')}" />
            </g:form>
          </g:else>
        </li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    
    <g:render template="patientData" model="${params.datosPaciente}" />
    
    <h1><g:message code="registros.currentSession.action.history" /></h1>
    <div class="content">
      <!-- Lista de registros en el EHR Server -->
      <g:include action="compositionList" params="[patientUid: params.patientUid]" />
    </div>
  </body>
</html>