<html>
  <head>
    <meta name="layout" content="main" />
    <title>Sign</title>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
    <style type="text/css">
      body {
        padding: 10px;
        margin-top: 10px;
      }
      h1 {
        border-bottom: 1px solid #DFDFDF;
        padding-bottom: 5px;
      }
      table {      
        border: 0px;
      }
      th {
        background: none;
        text-align: right;
        vertical-align: middle;
      }
      tr > th:first-child {
        padding: 0; /* sobreescribe un padding left de main.css */
      }
      
      #form1 {
        border: 2px solid #999;
        padding: 20px 20px;
		width: 290px;
		position: relative;
		left: 25%;
        background-color: #fff;
        -moz-box-shadow:    2px 3px 5px 1px #ccc;
        -webkit-box-shadow: 2px 3px 5px 1px #ccc;
        box-shadow:         2px 3px 5px 1px #ccc;
        -webkit-border-radius: 10px;
        -moz-border-radius: 10px;
        border-radius: 10px;
      }
      #form1 table {
        width: 270px;
        margin-bottom: 0px;
      }
      p {margin:1em 0}
      input {
        position:relative;
      }
      td {
        text-align: right;
      }
      .error {
        /* TODO: meter icono de error ! */
        border: 1px solid #f00;
        background-color: #f99;
        padding: 2px;
        margin-bottom: 3px;
      }
      .error ul {
        list-style:none;
        margin:0;
        padding:0;
      }
      img {
        border: 0px;
      }
      #datosPaciente {
        padding: 10px;
        margin: 10px auto;
        background-color: #99ccff;
      }
      /* para centrar el login/sign horiz */
      #sign_panel {
        width: 50%;
      }
      #sign_panel > div {
        position: relative;
        left: 50%;
        width: 50%;
      }
    </style>
    
    <g:javascript src="jquery-1.8.2.min.js" />

    <script type="text/javascript">
    $(document).ready(function() {
      $("#user").focus();
    });
    </script>
    
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="registros" action="list">Registros</g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    
    <g:render template="patientData" model="${session.clinicalSession.datosPaciente}" />
  
    <h1>Sign records</h1>
  
    <g:if test="${flash.message}">
      <div class="error"><g:message code="${flash.message}" /></div>
    </g:if>
    
    <div id="sign_panel">
      <div>
	    <g:form url="[action:'sign']" method="post" id="form1">
	
	      <table>
	        <tr>
	          <th>Usuario</th>
	          <td><input type="text" id="user" name="user" size="24" /></td>
	        </tr>
	        <tr>
	          <th>Clave</th>
	          <td><input type="password" name="pass" size="24" /></td>
	        </tr>
	        <tr>
	          <th></th>
	          <td><input type="submit" name="doit" value="Firmar" /></td>
	        </tr>
	      </table>
	    </g:form>
	   </div>
	 </div>
  </body>
</html>