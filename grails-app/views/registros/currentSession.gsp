<html>
  <head>
    <meta name="layout" content="main" />
    <title>Registros</title>
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
        <li><g:link class="list" controller="patient" action="list">Pacientes</g:link></li>
        <li><g:link class="list" controller="clinicalSession" action="list">Sesiones</g:link></li>
        <li><g:link class="list" controller="registros" action="list" params="[patientUid:session.clinicalSession.patientUid]">Registros hist&oacute;ricos</g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    
    <g:render template="patientData" model="${session.clinicalSession.datosPaciente}" />
    
    <h1>Creaci&oacute;n de registros clinicos</h1>
    <div class="content">
    
      <%-- evita error de lazy load si se accede a session.clinicalSession.documents --%>
      <g:set var="cses" value="${session.clinicalSession.refresh()}" />
    
      <g:each in="${templates}" var="template">
        
        <%-- hay un doc para el arquetipo en la sesion? --%>
        <%-- <g:set var="doc" value="${cses.documents.find{ it.compositionArchetypeId == archetype.archetypeId.value }}" /> --%>
        
        <g:set var="doc" value="${cses.getDocumentForTemplate( template.templateId )}" />
        
        
        <%-- nombre y descripcion del arquetipo --%>
        <g:set var="term" value="${template.concept.text()}" />
        
        <g:if test="${doc}">
          <g:link action="show" params="[id:doc.id]">${term}</g:link> *
        </g:if>
        <g:else>
          <g:link action="create" params="[templateId: template.templateId]">${term}</g:link>
        </g:else>
        <br/>
        <%--
        <template>
          <definition> 
            <term_definitions code="at0000">
              <items id="description">unknown</items>
        --%>
        ${template.definition.term_definitions.find { it.@code == "at0000" }.items.find{ it.@id == "description" }.text()}
        <br/><br/>
      </g:each>
      
      <%-- debe haber algun registro hecho para poder ir a firmar --%>
      <g:if test="${cses.documents.size() > 0}">
        <div id="sign">
          <g:link action="sign">Firmar</g:link>
        </div>
      </g:if>
      
      
      <%-- ahora tengo una pantalla de registros historicos
      <!-- Lista de registros en el EHR Server -->
      <h1>Registros historicos</h1>
      <g:include action="compositionList" params="[patientUid: session.clinicalSession.patientUid]" />
      --%>
      <!--
      <div class="help">
        Esta aplicaci&oacute;n es para crear y enviar registros a un servidor remoto de registros cl&iacute;nicos.<br/>
        Los identificadores &uacute;nicos de cada registro (registros cl&iacute;nicos, &oacute;rdenes de estudios, etc.) son asignados por el servidor al recibir cada registro.<br/>
        La lectura de registros se realiza solicitando los mismos al servidor, seg&uacute;n alg&uacute;n criterio de b&uacute;squeda.<br/>
      </div>
      -->
    </div>
  </body>
</html>