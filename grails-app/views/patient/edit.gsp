<%@ page import="demographic.Patient" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <title><g:message code="default.edit.label" /></title>
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" action="index"><g:message code="clinicalSession.list.nav.patients" /></g:link></li>
      </ul>
    </div>
    <div id="edit-patient" class="content scaffold-edit" role="main">
      <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${patientInstance}">
      <ul class="errors" role="alert">
        <g:eachError bean="${patientInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form url="[resource:patientInstance, action:'update']" method="PUT" >
        <g:hiddenField name="version" value="${patientInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
          <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
