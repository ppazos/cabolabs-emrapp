<html>
  <head>
    <meta name="layout" content="main" />
    <title>Registro de signos</title>
    <style>
      body {
        padding: 10px;
        margin-top: 10px;
      }
      h1 {
        border-bottom: 1px solid #DFDFDF;
        padding-bottom: 5px;
      }
      .content {
        padding: 10px;
        margin: 0 22%; /* centra la tabla */
      }
      table {
        border: 0;
      }
      tr > td:first-child {
        text-align: right;
      }
      tr:last-child td {
        /*text-align: center;*/
      }
      input[type=radio] {
        margin: 0 5px 0 0;
      }
      input[readonly=readonly] {
      border: 0;
      background: none;
      }
      label {
      margin: 0 5px 0 5px;
      }
      .units_constraint {
      display: none;
      }
    </style>
    <g:javascript src="jquery-1.8.2.min.js" />
    <g:javascript>
    
    $(document).ready(function() {
      
    });
    
    </g:javascript>
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><g:link class="list" controller="registros" action="currentSession">Registros</g:link></li>
      </ul>
    </div>
  
    <h1>Registro de signos</h1>

    <div class="content">

      <%--
      ${doc.bindData}
      {/content[at0008]/data[at0009]/events[at0010]/data[at0011]/items[at0012]/value/magnitude=32, /content[at0018]/data[at0019]/events[at0020]/data[at0021]/items[at0022]/value/magnitude=84, /content[at0028]/data[at0029]/events[at0030]/data[at0031]/items[at0032]/value/magnitude=182, /content[at0013]/data[at0014]/events[at0015]/data[at0016]/items[at0017]/value/magnitude=67, /content[at0018]/data[at0019]/events[at0020]/data[at0021]/items[at0022]/value/units=/min, /content[at0006]/data[at0007]/events[at0002]/data[at0003]/items[at0005]/value/magnitude=78, /content[at0008]/data[at0009]/events[at0010]/data[at0011]/items[at0012]/value/units=ºC, /content[at0028]/data[at0029]/events[at0030]/data[at0031]/items[at0032]/value/units=cm, /content[at0006]/data[at0007]/events[at0002]/data[at0003]/items[at0004]/value/units=mm[Hg], /content[at0013]/data[at0014]/events[at0015]/data[at0016]/items[at0017]/value/units=/min, /content[at0006]/data[at0007]/events[at0002]/data[at0003]/items[at0004]/value/magnitude=123, /content[at0023]/data[at0024]/events[at0025]/data[at0026]/items[at0027]/value/units=kg, /content[at0023]/data[at0024]/events[at0025]/data[at0026]/items[at0027]/value/magnitude=87, /content[at0006]/data[at0007]/events[at0002]/data[at0003]/items[at0005]/value/units=mm[Hg]} 
      <br/>
      
      ${bindings['presion_sistolica_units']}
      // /content[at0006]/data[at0007]/events[at0002]/data[at0003]/items[at0004]/value/units 
      <br/>
      
      ${doc.bindData[ bindings['presion_sistolica_units'] ]}
      // mm[Hg] 
      <br/>
      --%>
      
      <table>
        <g:set var="node" value="${template.getNode( bindings['presion_sistolica'])}" />
        <tr>
          <td>
            Presión sistólica:
          
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['presion_sistolica_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['presion_sistolica_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['presion_sistolica_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['presion_diastolica'])}" />
        <tr>
          <td>
            Presión diastólica:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['presion_diastolica_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['presion_diastolica_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['presion_diastolica_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['temperatura'])}" />
        <tr>
          <td>
            Temperatura:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['temperatura_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['temperatura_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['temperatura_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['frecuencia_cardiaca'])}" />
        <g:set var="name" value="${node.xmlNode.attributes.find{ it.rm_attribute_name.text() == "name" }.children[0]}" />
          <g:set var="value" value="${node.xmlNode.attributes.find{ it.rm_attribute_name.text() == "value" }.children[0]}" />
        <tr>
          <td>
            Frecuencia cardíaca:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${value.list.find{ it.units == doc.bindData[ bindings['frecuencia_cardiaca_units'] ] }}" />
            
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${template.getTerm('openEHR-EHR-OBSERVATION.pulse.v1', doc.bindData[ bindings['frecuencia_cardiaca_name'] ])}
            <br/>
            ${doc.bindData[ bindings['frecuencia_cardiaca_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['frecuencia_cardiaca_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['frecuencia_respiratoria'])}" />
        <tr>
          <td>
            Frecuencia respiratoria:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['frecuencia_respiratoria_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['frecuencia_respiratoria_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['frecuencia_respiratoria_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['peso'])}" />
        <tr>
          <td>
            Peso:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['peso_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['peso_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['peso_units'] ]}</label>
          </td>
        </tr>
        
        <g:set var="node" value="${template.getNode( bindings['estatura'] )}" />
        <tr>
          <td>
            Estatura:
            
            <%-- restriccion para las unidades ingresadas, quiero mostrar el rango para esa restriccion --%>
            <g:set var="cdvq_item" value="${node.xmlNode.list.find{ it.units == doc.bindData[ bindings['estatura_units'] ] }}" />
            <g:if test="${cdvq_item?.magnitude}">
            (${cdvq_item.magnitude.lower}..${cdvq_item.magnitude.upper})
            </g:if>
          </td>
          <td>
            ${doc.bindData[ bindings['estatura_mag'] ]}
          </td>
          <td>
            <label>${doc.bindData[ bindings['estatura_units'] ]}</label>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>