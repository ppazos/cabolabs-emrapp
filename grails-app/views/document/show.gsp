<%@ page import="registros.Document" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'document.label', default: 'Document')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-document" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-document" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list document">
      
        <g:if test="${documentInstance?.authorId}">
          <li class="fieldcontain">
            <span id="authorId-label" class="property-label"><g:message code="document.authorId.label" default="Author Id" /></span>
            <span class="property-value" aria-labelledby="authorId-label"><g:fieldValue bean="${documentInstance}" field="authorId"/></span>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.bindData}">
          <li class="fieldcontain">
            <span id="bindData-label" class="property-label"><g:message code="document.bindData.label" default="Bind Data" /></span>
            <span class="property-value" aria-labelledby="bindData-label"><g:fieldValue bean="${documentInstance}" field="bindData"/></span>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.category}">
          <li class="fieldcontain">
            <span id="category-label" class="property-label"><g:message code="document.category.label" default="Category" /></span>
            <span class="property-value" aria-labelledby="category-label"><g:fieldValue bean="${documentInstance}" field="category"/></span>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.compositionArchetypeId}">
          <li class="fieldcontain">
            <span id="compositionArchetypeId-label" class="property-label"><g:message code="document.compositionArchetypeId.label" default="Archetype Id" /></span>
            <span class="property-value" aria-labelledby="compositionArchetypeId-label"><g:fieldValue bean="${documentInstance}" field="compositionArchetypeId"/></span>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.content}">
          <li class="fieldcontain">
          <span id="content-label" class="property-label"><g:message code="document.content.label" default="Content" /></span>
            <g:each in="${documentInstance.content}" var="c">
              <span class="property-value" aria-labelledby="content-label"><g:link controller="item" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></span>
            </g:each>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.end}">
          <li class="fieldcontain">
            <span id="end-label" class="property-label"><g:message code="document.end.label" default="End" /></span>
            <span class="property-value" aria-labelledby="end-label"><g:formatDate date="${documentInstance?.end}" /></span>
          </li>
        </g:if>
      
        <g:if test="${documentInstance?.start}">
          <li class="fieldcontain">
            <span id="start-label" class="property-label"><g:message code="document.start.label" default="Start" /></span>
            <span class="property-value" aria-labelledby="start-label"><g:formatDate date="${documentInstance?.start}" /></span>
          </li>
        </g:if>
      
      </ol>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${documentInstance?.id}" />
          <g:link class="edit" action="edit" id="${documentInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
