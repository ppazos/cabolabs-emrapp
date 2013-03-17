<table>
  <tr>
    <!-- <th>ehrId</th> -->
    <th>uid</th>
    <th>archetypeId</th>
    <!-- <th>category</th> -->
    <th>startTime</th>
    <th>subjectId</th>
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
	       <g:link action="showComposition" params="[uid: c.uid]" class="showCompo">ver registro</g:link>
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
      //console.log( modal.children()[0] );
      //console.log( this.href );
      
      modal.children()[0].src = this.href;
      
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
</g:javascript>

<!--
TODO: paginacion
-->