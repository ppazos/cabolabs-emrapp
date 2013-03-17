<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <title>Pacientes</title>
    
    <g:javascript src="jquery-1.8.2.min.js" />
    <g:javascript src="jquery.form-3.18.js" />
    <g:javascript>
      
    </g:javascript>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
  </head>
  <body>
    
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="clinicalSession" action="list">Sesiones</g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    
    <div id="list-document" class="content scaffold-list" role="main">
    
      <h1>Lista de pacientes</h1>

      <!-- La tabla y el paginador se renderean usando un template -->
      <g:include action="patientList" />
    </div>
  </body>
</html>