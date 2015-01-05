<table>
  <tr>
    <!-- <th>ehrId</th> -->
    <th><g:message code="registros.compositionList.field.uid" /></th>
    <th><g:message code="registros.compositionList.field.archetypeId" /></th>
    <!-- <th>category</th> -->
    <th><g:message code="registros.compositionList.field.startTime" /></th>
    <th><g:message code="registros.compositionList.field.subjectId" /></th>
    <th></th>
  </tr>
  <%--
  cIdxList es NodeChild (XML Parseado) que devuelve EHRServer.findCompositions()
  el nombre del nodo cIdxList es 'list' y los hijos son compositionIndex.
  --%>
  <g:each in="${compositionIdxList.children()}" var="c">
	   <tr>
	     <!-- <td>${c.ehrId}</td> -->
	     <td>${c.uid}</td>
	     <td>${c.archetypeId}</td>
	     <!-- <td>${c.category}</td> -->
	     <td>${c.startTime}</td>
	     <td>${c.subjectId}</td>
	     <td>
	       <g:link action="showComposition" params="[uid: c.uid]" class="showCompo"><g:message code="registros.compositionList.action.showRecord" /></g:link>
	       <g:link action="checkoutComposition" params="[uid: c.uid, patientUid: params.patientUid]"><g:message code="registros.compositionList.action.checkout" /></g:link>
	     </td>
	   </tr>
  </g:each>
</table>

<div id="composition_modal" style="width:960px; height:600px; display:none;"><iframe src="" style="padding:0; margin:0; width:960px; height:600px; border:0;"></iframe></div>

<g:javascript>
  $(document).ready(function() {
  
    // Muestra modal con el contenido de la composition tal cual fue devuelto por el servidor EHR
    $('.showCompo').click(function(e) {
    
      e.preventDefault();
      
      modal = $('#composition_modal');

      var iframe = modal.children()[0];
      iframe.src = this.href;
      
      
      $(iframe).load(function() {
        console.log('carga modal');
        
        // Pretty print the version XML just for testing
        /*
        var content = (this.contentWindow || this.contentDocument);
        if (content.document) content = content.document;
        
        var versionXML = content.body.children[0];
        content.body.removeChild( content.body.children[0] );
        
        pre = document.createElement('pre');
        xml = document.createTextNode( xmlToString(versionXML) );
        pre.appendChild( xml );
        content.body.appendChild( pre );
        */
      });
      
      $.blockUI({
        message: modal,
        css: {
          width: '960px',
          height: '600px',
          top: '10px',
          left:'auto',
          padding: '10px'
        },
        onOverlayClick: $.unblockUI
      });
      
      //$('.blockOverlay').attr('title', 'salir').click($.unblockUI);
    });
    
  });
  
  // http://stackoverflow.com/questions/6507293/convert-xml-to-string-with-jquery
  function xmlToString(xmlData) { 

     var xmlString;
     //IE
     if (window.ActiveXObject){
         xmlString = xmlData.xml;
     }
     // code for Mozilla, Firefox, Opera, etc.
     else{
         xmlString = (new XMLSerializer()).serializeToString(xmlData);
     }
     return xmlString;
  }
</g:javascript>

<!--
TODO: paginacion
-->