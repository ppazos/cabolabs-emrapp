<html>
  <head>
    <meta name="layout" content="main" />
    <title>Registro de orden de laboratorio</title>
    <style>
      body {
        padding: 10px;
        margin-top: 10px;
      }
      h1 {
        border-bottom: 1px solid #DFDFDF;
        padding-bottom: 5px;
      }
      form {
        margin: 0 17%;
      }
      form > table {
        border: 0; /* saca el border top de la table que lo puse como border bottom del h1 */
      }
      .content {
        /*padding: 10px;*/
      }
      tr > td:first-child {
        text-align: right;
      }
      tr:last-child td {
        /*text-align: center;*/
      }
    </style>
    
    <g:javascript src="jquery-1.8.2.min.js" />
    <g:javascript src="jquery-ui-1.8.24.autocomplete.min.js" />
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css/ui-lightness', file: 'jquery-ui-1.8.24.custom.css')}" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'emr.css')}" />
    <g:javascript>
    
    // TODO: se debe implementar la busqueda en los STs
    // Ver ejemplo: http://search.loinc.org/search.zul?query=blood
    
    /*
    http://s.details.loinc.org/LOINC/13535-0.html?sections=Simple
    Sucrose hemolysis (hemólisis en sacarosa)
    http://www.nlm.nih.gov/medlineplus/spanish/ency/article/003673.htm
    
    http://s.details.loinc.org/LOINC/789-8.html?sections=Simple
    Erythrocytes (eritrocitos, glóbulos rojos, hematíes)
    http://www.nlm.nih.gov/medlineplus/spanish/ency/article/003644.htm
    
    http://s.details.loinc.org/LOINC/65634-8.html?sections=Simple
    Creatinine (creatinina en la sangre)
    http://www.nlm.nih.gov/medlineplus/spanish/ency/article/003475.htm
    */
    
    /*
    Categorias:
     - orina:         at0010
     - sangre:        at0011
     - citologia:     at0012
     - histologia:    at0013
     - microbiologia: at0014
    */
    var sts_map = [{label: 'glucosa en orina',        code: '50555-2',   atcode: 'at0010', class:'UA'},
                   {label: 'proteína en orina',       code: '50561-0',   atcode: 'at0010', class:'UA'},
                   {label: 'pH de la orina',          code: '50560-2',   atcode: 'at0010', class:'UA'},
                   
                   {label: 'hemólisis en sacarosa',   code: '13535-0',   atcode: 'at0011', class:'HEM/BC'},
                   {label: 'glóbulos rojos',          code: '789-8',     atcode: 'at0011', class:'HEM/BC'},
                   {label: 'ácido úrico en sangre',   code: '5816-4',    atcode: 'at0011', class:'HEM/BC'},
                   
                   {label: 'citología vaginal',       code: '19762-4',   atcode: 'at0012', class:'CYTO'},
                   {label: 'citología bronquial',     code: '42210-5',   atcode: 'at0012', class:'CYTO'},
                   
                   {label: 'histología',              code: '22036-8',   atcode: 'at0013', class:'TUMRRGT'},
                   
                   {label: 'bacterias en esputo',         code: '622-1', atcode: 'at0014', class:'MICRO'},
                   {label: 'cultivo de hongos en sangre', code: '601-5', atcode: 'at0014', class:'MICRO'}
                   ];
    
    $(document).ready(function() {
	  
	   // http://www.jensbits.com/2011/08/24/using-jquery-autocomplete-when-remote-source-json-does-not-contain-label-or-value-fields/
	   // http://stackoverflow.com/questions/4532505/jquery-ui-autocomplete-syntax
	   // http://jqueryui.com/demos/autocomplete/#remote-jsonp
      $("#tipo_estudio_texto").autocomplete({
        
        //source: sts_map, // se puede poner una URL: http://blog.zeion.net/2011/12/15/ejemplo-practico-de-autocompletar-con-jquery/
        
        source: function(request, response) {

          // El tipo de estudio depende de la categoria
          if ( $("input[name='categoria_estudio']:checked").val() == undefined )
          {
             alert("Debe seleccionar la categoría del estudio");
             $("#tipo_estudio_texto").val('');
             return false;
          }

          // http://jqueryui.com/demos/autocomplete/#folding
          var matcher = new RegExp( $.ui.autocomplete.escapeRegex( request.term ), "i" ); // Matchea por el texto ingresado
          response( $.grep( sts_map, function( value ) {
          
             // Filra tipos de estudio que no están en la categoría seleccionada
             if (value.atcode != $("input[name='categoria_estudio']:checked").val()) return false;
          
             value = value.label || value.value || value;
             return matcher.test( value );
          }) );
        },

        select: function (event, obj) {
        
          //console.log(event);
          //console.log(obj.item); // label, value, code
          
          // La label se pone automaticamente (si en el map hay label y value,
          // label es la que aparece en el popup y el value es el que se pone
          // en el input al seleccionar).
          
          // Pongo el valor en el campo hidden
          $("#tipo_estudio").val(obj.item.code);
        }
      });
      
      // Pone el nombre de la categoria de estudio en el input hidden al seleccionar el codigo
      $('input[name=categoria_estudio]').click(function(){
        
        //console.log( $('.text', $(this).parent()).text() );
        $('input[name=categoria_estudio_texto]').val( $('.text', $(this).parent()).text() );
      });
	});
	</g:javascript>
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="registros" action="currentSession">Registros</g:link></li>
      </ul>
      <g:render template="/user/loggedUser" />
    </div>
    
    <g:render template="patientData" model="${session.clinicalSession.datosPaciente}" />
    
    <h1>Orden de laboratorio</h1>
    <!-- TODO: poder ordenar multiples estudios. -->
    <div class="content">
      <g:form action="save">
        <input type="hidden" name="archetypeId" value="${archetype.archetypeId.value}"/>
        
        <table>
          <g:set var="node" value="${archetype.node( bindings['categoria_estudio'])}" />
          <tr>
            <td>Categoria:</td>
            <td>
              <%--
              ${node} CCodePhrase
              --%>
              <g:each in="${node.codeList}" var="code">
                <label>
                  <input type="radio" value="${code}" name="categoria_estudio" />
                  <span class="text">${archetype.ontology.termDefinition("es", code).getText()}</span>
                </label><br/>
              </g:each>
              
              <!-- nombre del codigo seleccionado, se pone por javascript -->
              <input type="hidden" name="categoria_estudio_texto" id="categoria_estudio_texto" />
            </td>
          </tr>
          <tr>
            <td><!-- Este campo se va a conectar a los servicios terminologicos para buscar tipos de estudios de lab -->
              Tipo de estudio:
            </td>
            <td>
              <!-- este campo es DvCodedText con restriccion ConstraintRef acNNNN para buscar en ST -->
            
              <!-- este es el campo en el binding, el codigo se pone por javascript -->
              <input type="hidden" name="tipo_estudio" id="tipo_estudio" />
              
              <!-- este es el campo que se usa para poner el texto y buscarlo en el ST -->
              <!-- TODO: ver en el modelo si hay lugar para "original text" -->
              <input type="text" name="tipo_estudio_texto" id="tipo_estudio_texto" />
            </td>
          </tr>
          <tr>
            <td>¿Es urgente?:</td>
            <td>
              <%-- FIXME: render de labels para boolean debe ser i18n --%>
              <!-- <label><input type="radio" name="urgente" value="" /> NR</label> -->
              <label><input type="radio" name="urgente" value="true" /> Si</label>
              <label><input type="radio" name="urgente" value="false" checked="checked" /> No</label>
            </td>
          </tr>
          <tr>
            <td>Descripcion:</td>
            <td><textarea name="descripcion"></textarea></td>
          </tr>
          <tr>
            <td>Fecha esperada del resultado:</td>
            <td><g:datePicker name="fecha_espera" /></td>
          </tr>
          <tr>
            <td></td>
            <td><input type="submit" value="Guardar" /></td>
          </tr>
        </table>
	  </g:form>
    </div>
  </body>
</html>