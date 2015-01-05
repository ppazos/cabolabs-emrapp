<html>
  <head>
    <meta name="layout" content="main" />
    <title>Registro de orden de estudio imagenologico</title>
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
    
    var sts_map = [
	  {value:'TC de la cabeza',         code:'24725-4', method:'CT',  atcode:'at0012', loc_anat:'cabeza',            loc_anat_code:'Head'},
	  {value:'TC tobillo izquierdo',    code:'36426-5', method:'CT',  atcode:'at0012', loc_anat:'tobillo izquierdo', loc_anat_code:'Ankle.left'},
	  {value:'TC tobillo derecho',      code:'36428-1', method:'CT',  atcode:'at0012', loc_anat:'tobillo derecho',   loc_anat_code:'Ankle.right'},
	  {value:'TC de torax',             code:'29252-4', method:'CT',  atcode:'at0012', loc_anat:'tórax',             loc_anat_code:'Chest'},
	  {value:'TC rodilla izquierda',    code:'36225-1', method:'CT',  atcode:'at0012', loc_anat:'rodilla izquierda', loc_anat_code:'Knee.left'},
	  {value:'TC rodilla derecha',      code:'36227-7', method:'CT',  atcode:'at0012', loc_anat:'rodilla derecha',   loc_anat_code:'Knee.right'},
	  {value:'TC de cuello',            code:'36235-0', method:'CT',  atcode:'at0012', loc_anat:'cuello',            loc_anat_code:'Neck'},
	  {value:'TC de femur',             code:'36338-2', method:'CT',  atcode:'at0012', loc_anat:'femur',             loc_anat_code:'Femur'},
	  {value:'RM de pecho',             code:'36283-0', method:'MRI', atcode:'at0017', loc_anat:'pecho',             loc_anat_code:'Chest'},
	  {value:'RM cerebral',             code:'24590-2', method:'MRI', atcode:'at0017', loc_anat:'cerebro',           loc_anat_code:'Brain'},
	  {value:'RM abdominal',            code:'24556-3', method:'MRI', atcode:'at0017', loc_anat:'abdominal',         loc_anat_code:'Abdomen'},
	  {value:'RM de cuello',            code:'24839-3', method:'MRI', atcode:'at0017', loc_anat:'cuello',            loc_anat_code:'Neck'},
	  {value:'RM de rodilla izquierda', code:'26200-6', method:'MRI', atcode:'at0017', loc_anat:'rodilla izquierda', loc_anat_code:'Knee.left'},
	  {value:'RM de rodilla derecha',   code:'26201-4', method:'MRI', atcode:'at0017', loc_anat:'rodilla derecha',   loc_anat_code:'Knee.right'},
	  {value:'ECO de senos',            code:'24601-7', method:'US',  atcode:'at0020', loc_anat:'senos',             loc_anat_code:'Breast'},
	  {value:'ECO de pecho',            code:'24630-6', method:'US',  atcode:'at0020', loc_anat:'pecho',             loc_anat_code:'Chest'},
	  {value:'ECO de muñeca izquierda', code:'26280-8', method:'US',  atcode:'at0020', loc_anat:'muñeca izquierda',  loc_anat_code:'Wrist.left'},
	  {value:'ECO de muñeca derecha',   code:'26282-4', method:'US',  atcode:'at0020', loc_anat:'muñeca derecha',    loc_anat_code:'Wrist.right'},
	  {value:'ECO de cuello',           code:'37918-0', method:'US',  atcode:'at0020', loc_anat:'cuello',            loc_anat_code:'Neck'},
	  {value:'ECO de ovarios',          code:'69390-3', method:'US',  atcode:'at0020', loc_anat:'ovarios',           loc_anat_code:'Ovary'},
	  {value:'RX de muñeca izquierda',  code:'26170-1', method:'XR',  atcode:'at0022', loc_anat:'muñeca izquierda',  loc_anat_code:'Wrist.left'},
	  {value:'RX de muñeca derecha',    code:'26171-9', method:'XR',  atcode:'at0022', loc_anat:'muñeca derecha',    loc_anat_code:'Wrist.right'},
	  {value:'RX de columna dorsal',    code:'24940-9', method:'XR',  atcode:'at0022', loc_anat:'columna cervical',  loc_anat_code:'Spine.cervical'},
	  {value:'RX columna vertebral',    code:'24984-7', method:'XR',  atcode:'at0022', loc_anat:'columna toracica y lumbar', loc_anat_code:'Spine.thoracic+Spine.lumbar'}
	 ];
    
    $(document).ready(function() {
	  
	  // http://stackoverflow.com/questions/2064651/how-do-i-get-the-id-of-the-control-calling-the-jquery-autocomplete-function
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
             //return matcher.test( value ) || matcher.test( normalize( value ) );
          }) );
          

          //http://jqueryui.com/demos/autocomplete/#remote-jsonp
          //http://api.jquery.com/jQuery.map/
          /*
          response( $.map( sts_map, function( item ) {
            return {
              //label: item.name + (item.adminName1 ? ", " + item.adminName1 : "") + ", " + item.countryName,
              //value: item.name
              label: item.value,
              value: item.value
            }
          }));
          */
        },
        
        select: function (event, obj) {
        
          //console.log(event);
          //console.log(obj.item); // label, value, code, method
          
          // La label se pone automaticamente (si en el map hay label y value,
          // label es la que aparece en el popup y el value es el que se pone
          // en el input al seleccionar).
          
          // Pongo el valor en el campo hidden
          $("#tipo_estudio").val(obj.item.code);
          
          $("input[name='localizacion_anatomica_texto']").val(obj.item.loc_anat);
          $("input[name='localizacion_anatomica']").val(obj.item.loc_anat_code);
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
    
    <h1>Orden de estudio imagenologico</h1>
    <!-- TODO: poder ordenar multiples estudios. -->
    
    <div class="content">
      <g:form action="save">
        <input type="hidden" name="archetypeId" value="${archetype.archetypeId.value}"/>
        
        <table>
          <g:set var="node" value="${archetype.node( bindings['categoria_estudio'])}" />
          <tr>
            <td>Categoria:</td>
            <td>
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
            <td><!-- Este campo se va a conectar a los servicios terminologicos para buscar tipos de estudios de lab -->
              Localización anatómica:
            </td>
            <td>
              <!-- este campo depende de las localizaciones disponibles
                   para eel tipo de estudio seleccionado -->
              <input type="text" name="localizacion_anatomica_texto" readonly="readonly" />
              <input type="hidden" name="localizacion_anatomica" />
            </td>
          </tr>
          <tr>
            <td>¿Es urgente?:</td>
            <td>
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