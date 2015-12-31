<html>
  <head>
    <title>Login</title>

    <style type="text/css">
      table {      
        border: 0px;
      }
      th {
        background: none;
        text-align: right;
        vertical-align: middle;
      }      
      html,body{
        height:100%;
        background-color:#efefef;
        font-family: arial;
        background-image: -moz-linear-gradient(center top, #ddd, #efefef);
        background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #ddd), color-stop(1, #efefef));
        background-image: linear-gradient(top, #ddd, #efefef);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorStr = '#dddddd', EndColorStr = '#efefef');
        background-repeat: no-repeat;
        height: 100%;
        /* change the box model to exclude the padding from the calculation of 100% height (IE8+) */
        -webkit-box-sizing: border-box;
           -moz-box-sizing: border-box;
                box-sizing: border-box;
        
        padding-bottom: 15px;
      }
      #wrapper{
        height:100%;
        width:100%;
        display:table;
        vertical-align:middle;
      }
      #outer {
        display:table-cell;
        vertical-align:middle;
      }
      #formwrap {
        position:relative;
        left:50%;
        float:left;
      }
      #form1 {
        border: 2px solid #999;
        padding: 20px 20px;
        position: relative;
        text-align: center;
        left: -50%;
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
        margin: 20px;
        margin-bottom: 0px;
      }
      ul.userBar {
        position: relative;
        left: -50%;
        top: 1px;
      }
      ul.userBar li.active {
        position: relative;
        z-index: 9999;
      }
      ul.userBar li a {
        border-bottom: 0px;
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
      .version {
        margin-top: 5px;
        margin-bottom: 10px;
        text-align: right;
        width: 50%;
      }
    </style>
    <!--[if lt IE 8]>
      <style type="text/css">
        #formwrap {top:50%}
        #form1 {top:-50%;}
      </style>
     <![endif]-->
     <!--[if IE 7]>
       <style type="text/css">
        #wrapper{
         position:relative;
         overflow:hidden;
        }
      </style>
    <![endif]-->
    
    <g:javascript src="jquery-1.8.2.min.js" />

    <script type="text/javascript">
    $(document).ready(function() {
      $("#user").focus();
    });
    </script>
    
  </head>
  <body>     
    <div id="wrapper">
      <div id="outer">
        <div id="formwrap">
          <g:form url="[action:'login']" method="post" id="form1">
            <a href="http://cabolabs.com" target="_blank"><img src="http://cabolabs.com/images/logo_min_2.png" /></a>
            <h3 align="center">Sistema de Registro Clínico</h3>
            <g:if test="${flash.message}">
              <div class="error"><g:message code="${flash.message}" /></div>
            </g:if>
            <table>
              <tr>
                <th>Usuario</th>
                <td><input type="text" id="user" name="username" size="24" /></td>
              </tr>
              <tr>
                <th>Clave</th>
                <td><input type="password" name="password" size="24" /></td>
              </tr>
              <tr>
                <th>Organization</th>
                <td><input type="text" name="orgnumber" size="24" /></td>
              </tr>
              <tr>
                <th></th>
                <td><input type="submit" name="doit" value="Ingresar" /></td>
              </tr>
            </table>
          </g:form>
        </div>
      </div>
    </div>
  </body>
</html>