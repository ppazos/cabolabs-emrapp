
<%@ page import="demographic.Patient" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-patient" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-patient" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list patient">
			
				<g:if test="${patientInstance?.dob}">
				<li class="fieldcontain">
					<span id="dob-label" class="property-label"><g:message code="patient.dob.label" default="Dob" /></span>
					
						<span class="property-value" aria-labelledby="dob-label"><g:formatDate date="${patientInstance?.dob}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientInstance?.ehrUid}">
				<li class="fieldcontain">
					<span id="ehrUid-label" class="property-label"><g:message code="patient.ehrUid.label" default="Ehr Uid" /></span>
					
						<span class="property-value" aria-labelledby="ehrUid-label"><g:fieldValue bean="${patientInstance}" field="ehrUid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientInstance?.firstname}">
				<li class="fieldcontain">
					<span id="firstname-label" class="property-label"><g:message code="patient.firstname.label" default="Firstname" /></span>
					
						<span class="property-value" aria-labelledby="firstname-label"><g:fieldValue bean="${patientInstance}" field="firstname"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientInstance?.lastname}">
				<li class="fieldcontain">
					<span id="lastname-label" class="property-label"><g:message code="patient.lastname.label" default="Lastname" /></span>
					
						<span class="property-value" aria-labelledby="lastname-label"><g:fieldValue bean="${patientInstance}" field="lastname"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientInstance?.sex}">
				<li class="fieldcontain">
					<span id="sex-label" class="property-label"><g:message code="patient.sex.label" default="Sex" /></span>
					
						<span class="property-value" aria-labelledby="sex-label"><g:fieldValue bean="${patientInstance}" field="sex"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientInstance?.uid}">
				<li class="fieldcontain">
					<span id="uid-label" class="property-label"><g:message code="patient.uid.label" default="Uid" /></span>
					
						<span class="property-value" aria-labelledby="uid-label"><g:fieldValue bean="${patientInstance}" field="uid"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:patientInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${patientInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
