<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <title><g:message code="clinicalSession.list.title" /></title>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="patient" action="list"><g:message code="clinicalSession.list.nav.patients" /></g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    <div id="list-document" class="content scaffold-list" role="main">
      <h1><g:message code="clinicalSession.list.title" /></h1>
      
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      
      <table>
        <thead>
          <tr>
            <th><g:message code="clinicalSession.list.field.id" /></th>
            <th><g:message code="clinicalSession.list.field.patient" /></th>
            <th><g:message code="clinicalSession.list.field.date" /></th>
            <th><g:message code="clinicalSession.list.field.open" /></th>
            <th><g:message code="clinicalSession.list.field.committed" /></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${list}" status="i" var="cses">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
              <td>${cses.id}</td>
              <td>
                <g:link class="list" controller="registros" action="list" params="[patientUid:cses.patientUid]">${cses.datosPaciente.firstName} ${cses.datosPaciente.lastName}</g:link>
              </td>
              <td>${cses.dateCreated}</td>
              <td>${cses.open}</td>
              <td>${cses.committed}</td>
              <td>
                <g:if test="${cses.open}">
                  <g:link controller="registros" action="continueSession" id="${cses.id}"><g:message code="clinicalSession.list.action.continue" /></g:link>
                </g:if>
              </td>
            </tr>
          </g:each>
        </tbody>
      </table>
      <div class="pagination">
        <g:paginate total="${total}" />
      </div>
    </div>
  </body>
</html>