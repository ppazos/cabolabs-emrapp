<%@ page import="auth.User" %>



<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="user.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${userInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'pass', 'error')} ">
	<label for="pass">
		<g:message code="user.pass.label" default="Pass" />
		
	</label>
	<g:textField name="pass" value="${userInstance?.pass}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'user', 'error')} ">
	<label for="user">
		<g:message code="user.user.label" default="User" />
		
	</label>
	<g:textField name="user" value="${userInstance?.user}"/>
</div>

