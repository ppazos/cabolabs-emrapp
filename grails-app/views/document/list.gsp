<%@ page import="registros.Document" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'document.label', default: 'Document')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-document" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-document" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
        <thead>
          <tr>
            <g:sortableColumn property="authorId" title="${message(code: 'document.authorId.label', default: 'Author Id')}" />
          <%--
            <g:sortableColumn property="bindData" title="${message(code: 'document.bindData.label', default: 'Bind Data')}" />
          --%>
            <g:sortableColumn property="category" title="${message(code: 'document.category.label', default: 'Category')}" />
          
            <g:sortableColumn property="compositionArchetypeId" title="${message(code: 'document.compositionArchetypeId.label', default: 'Composition Archetype Id')}" />
          
            <g:sortableColumn property="end" title="${message(code: 'document.end.label', default: 'End')}" />
          
            <g:sortableColumn property="start" title="${message(code: 'document.start.label', default: 'Start')}" />
          </tr>
        </thead>
        <tbody>
        <g:each in="${documentInstanceList}" status="i" var="documentInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td><g:link action="show" id="${documentInstance.id}">${fieldValue(bean: documentInstance, field: "authorId")}</g:link></td>
          <%--
            <td>${fieldValue(bean: documentInstance, field: "bindData")}</td>
            --%>
            <td>${fieldValue(bean: documentInstance, field: "category")}</td>
            <td>${fieldValue(bean: documentInstance, field: "compositionArchetypeId")}</td>
            <td><g:formatDate date="${documentInstance.end}" /></td>
            <td><g:formatDate date="${documentInstance.start}" /></td>
          </tr>
        </g:each>
        </tbody>
      </table>
      <div class="pagination">
        <g:paginate total="${documentInstanceTotal}" />
      </div>
    </div>
  </body>
</html>
