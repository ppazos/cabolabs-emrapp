package xml

import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import registros.Document
import registros.Item
import registros.Element
import registros.Structure
import registros.valores.DataValue
import registros.valores.DvBoolean
import registros.valores.DvCodedText
import registros.valores.DvDateTime
import registros.valores.DvQuantity
import registros.valores.DvText
import sesion.ClinicalSession

import grails.util.Holders

/**
 * Serializador del modelo de datos a XML openEHR valido.
 * 
 * @author Pablo Pazos Gutierrez <pablo.pazos@cabolabs.com>
 *
 */
class XmlSerializer {

   def formatter = new SimpleDateFormat( Holders.config.app.l10n.datetime_format )
   static def manager = opt_repository.OperationalTemplateManager.getInstance()
   
   
   // templateId de la composition a la que se le esta haciendo toXML
   String templateId
   
   // Sesion clinica que tiene el composer de las compositions
   ClinicalSession cses
   
   
   /**
    * 
    * @param cses si estoy serializando para hacer un commit, la cses
    *             tiene el composer de las compositions que se estan
    *             commiteando, ademas del patientUid para obtener el
    *             ehr al que quiero hacer el commit.
    */
   public XmlSerializer(ClinicalSession cses)
   {
      this.cses = cses // Puede ser null ej. testing
   }
   
   /**
    * Si le pasaron una ClinicalSession al constructor, serializa
    * todos los documentos que tenga y devuelve una lista de los
    * strings XML.
    * 
    * @return
    */
   public List<String> serializeSessionDocs()
   {
      List<String> res = []
      this.cses.documents.each { doc ->
         
         res << toXml(doc, true)
      }
      
      return res
   }
   
   /**
    * El texto dentro del template, depende del arquetipo y del nodeId
    * @param templateId
    * @param archetypeId
    * @param nodeId
    * @return
    */
   private String getName(String templateId, String archetypeId, String nodeId)
   {
      def template = manager.getTemplate(templateId)
      
      if (!template)
      {
         println "getName(): No hay template en "+ templateId
         return ''
      }

      return template.getTerm(archetypeId, nodeId)
   }
   
   
   
   public String toXml(Document doc, boolean includeVersion)
   {
      def writer = new StringWriter()
      def builder = new MarkupBuilder(writer)
      
      builder.setDoubleQuotes(true) // Use double quotes on attributes
      
      templateId = doc.templateId
      
      /**
       * Se incluye version para hacer commit al servidor
       */
      if (includeVersion)
      {
         builder.version(xmlns:       'http://schemas.openehr.org/v1',
                         'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
                         'xsi:type':  'ORIGINAL_VERSION') {
            /**
             * required by the XSD
             */
            contribution {
              id('xsi:type':'HIER_OBJECT_ID') {
                value(java.util.UUID.randomUUID())
              }
              namespace('EHR::COMMON')
              type('CONTRIBUTION')
            }
           
            commit_audit() {
               system_id('CABOLABS_EHR') // TODO: should be configurable and the same as auditSystemId sent to the commit service from CommitJob
               
               /*
                Identity and optional reference into identity
                management service, of user who committed the item.
                */
               committer('xsi:type':"PARTY_IDENTIFIED") {
                  external_ref {
                     id('xsi:type': 'HIER_OBJECT_ID') {
                        value(cses.composer) // FIXME: now composer is the username .uid)
                     }
                     namespace('DEMOGRAPHIC')
                     type('PERSON')
                  }
                  name(cses.composer) // FIXME: now composer is the username .name)
                  // identifiers DV_IDENTIFIER
               }
               
               /*
                * This time will be overriden by the server to be copliant with this rule:
                * 
                * The time_committed attribute in both the Contribution and Version audits
                * should reflect the time of committal to an EHR server, i.e. the time of
                * availability to other users in the same system. It should therefore be
                * computed on the server in implementations where the data are created
                * in a separate client context.
                */
               time_committed {
                  value(formatter.format( cses.dateClosed ))
               }
               
               change_type() {
                  value( doc.getChangeType() ) // The doc knows if the change type is creation or modification
                  defining_code() {
                     terminology_id() {
                        value('openehr')
                     }
                     code_string(249)
                  }
               }
            } // commit_audit
            
            /**
             * version.uid is mandatory by the schema.
             */
            uid {
               
               // Versioning support: the doc will have the versionUid if it was checked out
               // from the EHRServer to be updated. Here we use the same UID as the checked
               // out version, because the EHRServer uses this to match an existing version
               // and does the update of the new version UID internally.
               if (doc.versionUid)
               {
                  value( doc.versionUid )
               }
               else
               {
                  // versioned_object_id + creating_system_id + version_tree_id
                  // FIXME: not 100% if the version tree id should be set by the client or by the server
                  value( (java.util.UUID.randomUUID() as String) +'::EMR_APP::1' )
               }
            }
           
            
            // FIXME: ver donde va el templateId
            data('xsi:type': 'COMPOSITION', archetype_node_id: doc.compositionArchetypeId) {
               
               compositionHeader(doc, builder) // name, language, territory, ...
               compositionContent(doc, builder)
            }
            
            lifecycle_state() {
               value('completed')
               defining_code() {
                  terminology_id() {
                     value('openehr')
                  }
                  code_string(532)
               }
            }
         }
      }
      else
      {
         builder.composition(xmlns:'http://schemas.openehr.org/v1',
                             'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance') {
                             
            compositionHeader(doc, builder) // name, language, territory, ...
            compositionContent(doc, builder)
         }
      }
      
      return writer.toString()
         
   } // toXml
   
   
   private void compositionHeader(Document doc, MarkupBuilder builder)
   {
      // Campos heredados de LOCATABLE
      builder.name() {
         //value('TODO: lookup al arquetipo para obtener el valor por el at0000')
         value( getName(this.templateId, doc.compositionArchetypeId, 'at0000') )
      }
      builder.archetype_details() { // ARCHETYPED
         archetype_id() { // ARCHETYPE_ID
            value(doc.compositionArchetypeId)
         }
         template_id() { // TEMPLATE_ID
            value(doc.templateId)
         }
         rm_version('1.0.2')
      }
      
      // Campos de COMPOSITION
      builder.language() {
         terminology_id() {
            value('ISO_639-1')
         }
         code_string('es') // TODO: deberia salir de una config global
      }
      builder.territory() {
         terminology_id() {
            value('ISO_3166-1')
         }
         code_string('UY') // TODO: deberia salir de una config global
      }
      builder.category() {
         value('event') // por ahora solo se soporta event
         defining_code() {
            terminology_id() {
               value('openehr')
            }
            code_string(443)
         }
      }
      
      // FIXME: el composer deberia ser el medico
      //        tengo que implementar un login para saber quien es el composer
      builder.composer('xsi:type':'PARTY_IDENTIFIED') {
         
         external_ref {
            id('xsi:type': 'HIER_OBJECT_ID') {
               value(cses.composer)// FIXME: now composer is the username .uid)
            }
            namespace('DEMOGRAPHIC')
            type('PERSON')
         }
         name(cses.composer)// FIXME: now composer is the username .name)
         // identifiers DV_IDENTIFIER
      }
      
      
      builder.context() {
         start_time() {
            value( formatter.format( doc.start ) )
         }
         setting() {
            value('Hospital Montevideo') // FIXME
            defining_code() {
               terminology_id() {
                  value('openehr')
               }
               code_string(229)
            }
         }
         // health_care_facility
      }
   }
   
   
   private void compositionContent(Document doc, MarkupBuilder builder)
   {
      doc.content.each { item ->
      
         // item aqui es siempre structure porque modela SECTION o ENTRY
         compositionContentRecursive(item, builder, "content") // serializa entries y sections de COMPO.content
      }
   }
   
   // auxiliar para poder serializar clases persistidas que tienen lazy loading y hay proxies (javassist) en lugar de las instancias de las clases correctas.
   private void compositionContentRecursive(Item item, MarkupBuilder builder, String tag)
   {
      //println item.getClass() // class registros.Item_$$_javassist_13
      // despues de imprimir la clase es la correcta porque carga las
	   // cosas de la base... FIXME: solucionar sin imprimir!!!!
	   //println item as grails.converters.XML
	  
      // Si la instancia es un proxy
      if (item.getClass().getSimpleName().contains('_$$_javassist_'))
      {
         // hacer refresh no funciona para obtener la clase real
         // cargando el item a mano si funciona!
         item = Item.get(item.id)
      }
     
      //println " ===+++--->>> "+ item.getClass().getSimpleName()
      //println " ===+++--->>> "+ item.class
      //assert ['registros.Structure', 'registros.Element'].contains( item.getClass().getSimpleName() )
     
      // Item deberia tener la clase correcta Structure o Element
      compositionContentRecursive(item, builder, tag)
   }
   
   // Check for abstract ENTRY super class
   private boolean isEntry(Item item)
   {
      return ['OBSERVATION', 'EVALUATION', 'INSTRUCTION', 'ACTION', 'ADMIN_ENTRY'].contains(item.type)
   }
   
   private boolean hasSpecificBuild(Item item)
   {
      return ['HISTORY'].contains(item.type)
   }
   
   // Check for abstract EVENT super class
   private boolean isEvent(Item item)
   {
      return ['POINT_EVENT', 'INTERVAL_EVENT'].contains(item.type)
   }
   
   /**
    * 
    * @param struct
    * @param builder
    * @param tag
    */
   private void compositionContentRecursive(Structure struct, MarkupBuilder builder, String tag)
   {
      def _archetype_node_id = ((struct.nodeId == 'at0000') ? struct.archetypeId : struct.nodeId)
      
      builder."$tag"('xsi:type':struct.type, archetype_node_id: _archetype_node_id) {
         
         // LOCATABLE mandatory attributes
         name() {
            //value('TODO: lookup del nombre en el arquetipo')
            value( getName(this.templateId, struct.archetypeId, struct.nodeId))
         }
         
         // The build methods are used to maintain the order of the attributes
         // in the XML to be compliant with the XSD.
         if (isEntry(struct))
         {
            this.buildENTRY(struct, builder)
         }
         else if (isEvent(struct))
         {
            this.buildEVENT(struct, builder)
         }
         else if (hasSpecificBuild(struct))
         {
            String buildMethod = 'build'+ struct.type
            this."$buildMethod"(struct, builder)
         }
         else // Generic serialization
         {
            //println " <<<< struct.type: "+ struct.type +", struct.attributes " + struct.attributes
            
            struct.attributes.each { attrName, dv ->
               
               //println "   >>>> " + attrName + " " + dv
               serializeDv( dv, builder, attrName )
            }
            
            // TODO: los atributos dependen del tipo del RM (struct.type)
            // FIXME: para que el XML sea valido se debe respetar el orden de los atributos:
            //   - OBSERVATION: name, language, encoding, subject, protocol, data
            //   - HISTORY: name, origin, events
            //   - EVENT/POINT_EVENT: name, time, data, state
            //
            // FIXME: no usar tipos abstractos
            //   - EVENT -> POINT_EVENT
            struct.items.each { item ->
               compositionContentRecursive(item, builder, item.attr)
            }
         }
      }
   }
   
   // =============================================================================
   // Methods to build tags by rm_type_name (Struct.type).
   // This is needed because each attribute from a rm_type_name has a specific
   // order in the XSD, and the generic build might not generate the attributes
   // in the right order into the XML.
   // =============================================================================
   
   private void buildENTRY(Structure struct, MarkupBuilder builder)
   {
      // ENTRY mandatory attributes in order!
      builder.language {
         terminology_id {
            value('ISO_639-1') // TODO: config
         }
         code_string('es') // TODO: config
      }
      builder.encoding {
         terminology_id {
            value('Unicode') // TODO: config
         }
         code_string('UTF-8')
      }
      /*
      builder.subject('xsi:type': 'PARTY_IDENTIFIED') {
         external_ref {
            id('xsi:type': 'HIER_OBJECT_ID') {
               value(cses.patientUid)
            }
            namespace('DEMOGRAPHIC')
            type('PERSON')
         }
      }
      */
      builder.subject('xsi:type': 'PARTY_SELF') {}
      
      // Serialize the rest of the structure for each entry
      String entryBuildMethod = 'build'+ struct.type
      this."$entryBuildMethod"(struct, builder)
   }
   
   private void buildOBSERVATION(Structure struct, MarkupBuilder builder)
   {
      // Attribute order: protocol (CARE_ENTRY, optional), guideline_id (CARE_ENTRY, optional), data (OBSERVATION), state (OBSERVATION, optional)
      
      // protocol is optional, so might be null
      def protocol = struct.items.find { it.attr == 'protocol' }
      if (protocol) compositionContentRecursive(protocol, builder, 'protocol')
      
      // data is mandatory, can't be null
      def data = struct.items.find { it.attr == 'data' }
      compositionContentRecursive(data, builder, 'data')
   }
   
   private void buildEVALUATION(Structure struct, MarkupBuilder builder)
   {
      // Attribute order: protocol (CARE_ENTRY, optional), guideline_id (CARE_ENTRY, optional),
      // data (EVALUATION).
      
      // protocol is optional, so might be null
      def protocol = struct.items.find { it.attr == 'protocol' }
      if (protocol) compositionContentRecursive(protocol, builder, 'protocol')
      
      // data is mandatory, can't be null
      def data = struct.items.find { it.attr == 'data' }
      compositionContentRecursive(data, builder, 'data')
   }
   
   private void buildINSTRUCTION(Structure struct, MarkupBuilder builder)
   {
      // Attribute order: protocol (CARE_ENTRY, optional), guideline_id (CARE_ENTRY, optional),
      // narrative (INSTRUCTION), expiry_time (INSTRUCTION, optional),
      // wf_definition (INSTRUCTION, optional), activities (INSTRUCTION, optional).
      
      // protocol is optional, so might be null
      def protocol = struct.items.find { it.attr == 'protocol' }
      if (protocol) compositionContentRecursive(protocol, builder, 'protocol')
      
      // narrative (DV_TEXT) is mandatory
      def narrative = struct.attributes.find { it.key == 'narrative' }.value // attributes is a map name->dv
      serializeDv(narrative, builder, 'narrative')
      
      // activities are optional, so might be 0 activities
      // FIXME: activities also needs order in the attributes so need to create a buildACTIVITY method.
      def activities = struct.items.findAll { it.attr == 'activities' }
      activities.each { activity ->
         compositionContentRecursive(activity, builder, 'activities')
      }
   }
   
   private void buildACTION(Structure struct, MarkupBuilder builder)
   {
      // Attribute order: protocol (CARE_ENTRY, optional), guideline_id (CARE_ENTRY, optional),
      // time (ACTION), description (ACTION),
      // ism_transition (ACTION), instruction_details (ACTION, optional).
      
      // protocol is optional, so might be null
      def protocol = struct.items.find { it.attr == 'protocol' }
      if (protocol) compositionContentRecursive(protocol, builder, 'protocol')
      
      // time (DV_DATE_TIME) is mandatory
      def time = struct.attributes.find { it.key == 'time' }.value // attributes is a map name->dv
      serializeDv(time, builder, 'time')
      
      // description (ITEM_STRUCTURE), is mandatory
      def description = struct.items.find { it.attr == 'description' }
      compositionContentRecursive(description, builder, 'description')
      
      // ism_transition (ISM_TRANSITION), is mandatory
      // FIXME: ism_transition also needs order in the attributes so need to create a buildISM_TRANSITION method.
      def ism_transition = struct.items.find { it.attr == 'ism_transition' }
      compositionContentRecursive(ism_transition, builder, 'ism_transition')
   }
   
   private void buildADMIN_ENTRY(Structure struct, MarkupBuilder builder)
   {
      // Attribute order: protocol (CARE_ENTRY, optional), guideline_id (CARE_ENTRY, optional),
      // data (ADMIN_ENTRY).
      
      // data (ADMIN_ENTRY), is mandatory
      def data = struct.items.find { it.attr == 'data' }
      compositionContentRecursive(data, builder, 'data')
   }
   
   private void buildHISTORY(Structure struct, MarkupBuilder builder)
   {
      // HISTORY attributes in order:
      // - origin (DV_DATE_TIME, mandatory),
      // - period (DV_DURATION, optional),
      // - duration (DV_DURATION, optional)
      // - events (EVENT, 0..N)
      // - summary (ITEM_STRUCTURE, optional)
      
      def origin = struct.attributes.find { it.key == 'origin' }.value // attributes is a map name->dv
      serializeDv(origin, builder, 'origin')
      
      def period = struct.attributes.find { it.key == 'period' } // attributes is a map name->dv
      if (period) serializeDv(period.value, builder, 'period')
      
      def duration = struct.attributes.find { it.key == 'duration' } // attributes is a map name->dv
      if (duration) serializeDv(duration.value, builder, 'duration')
      
      // TODO: verify!
      def events = struct.items.find { it.attr == 'events' }
      events.each { event ->
         compositionContentRecursive(event, builder, 'events')
      }
      
      def summary = struct.items.find { it.attr == 'summary' }
      if (summary) compositionContentRecursive(summary, builder, 'summary')
   }
   
   private void buildEVENT(Structure struct, MarkupBuilder builder)
   {
      // EVENT attributes in order:
      // - time (DV_DATE_TIME, mandatory),
      // - data (ITEM_STRUCTURE, mandatory),
      // - state (ITEM_STRUCTURE, optional)
      
      def time = struct.attributes.find { it.key == 'time' }.value // attributes is a map name->dv
      serializeDv(time, builder, 'time')
      
      def data = struct.items.find { it.attr == 'data' }
      compositionContentRecursive(data, builder, 'data')
      
      def state = struct.items.find { it.attr == 'state' }
      if (state) compositionContentRecursive(state, builder, 'state')
      
      // Serialize the rest of the structure for each entry
      String entryBuildMethod = 'build'+ struct.type
      this."$entryBuildMethod"(struct, builder)
   }
   
   private void buildPOINT_EVENT(Structure struct, MarkupBuilder builder)
   {
      return // Doesn't add attributes to EVENT
   }
   
   private void buildINTERVAL_EVENT(Structure struct, MarkupBuilder builder)
   {
      // INTERVAL_EVENT attributes in order:
      // - width (DV_DURATION, mandatory),
      // - sample_count (int, optional),
      // - math_function (DV_CODED_TEXT, mandatory)
      
      throw new Exception("INTERVAL_EVENT is not supported yet")
   }
   
   // =============================================================================
   // =============================================================================
   // =============================================================================
   
   /**
    * 
    * @param element
    * @param builder
    * @param tag
    */
   private void compositionContentRecursive(Element element, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':element.type, archetype_node_id:element.nodeId) {
         
         if (element.name)
         {
            serializeDv( element.name, builder, 'name' )
         }
         else
         {
            name() {
               value( getName(this.templateId, element.archetypeId, element.nodeId))
            }
         }
         
         serializeDv( element.value, builder, 'value' )
      }
   }
   
   /**
    * Operacion auxiliar para resolver proxies de la bd ($$_jaassit)
    * Idem a compositionContentRecursive( Item, ... )
    * @param dv
    * @param builder
    * @param tag
    */
   private void serializeDv(DataValue dv, MarkupBuilder builder, String tag)
   {
      // Si la instancia es un proxy
      if (dv.getClass().getSimpleName().contains('_$$_javassist_'))
      {
         // hacer refresh no funciona para obtener la clase real
         // cargando el item a mano si funciona!
         dv = DataValue.get(dv.id)
         
         // Now dv should have the specific type and not the abstract DataValue
         serializeDv(dv, builder, tag)
      }
      else
         throw new Exception("Type "+ dv.getClass().getSimpleName() +" is not supported yet")
   }
   
   private void serializeDv(DvDateTime dv, MarkupBuilder builder, String tag)
   {
      println "serializeDv DvDateTime"
      
      builder."$tag"('xsi:type':'DV_DATE_TIME') {
         value( formatter.format( dv.value ) )
      }
   }
   private void serializeDv(DvQuantity dv, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':'DV_QUANTITY') {
         magnitude(dv.magnitude)
         units(dv.units)
      }
   }
   private void serializeDv(DvText dv, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':'DV_TEXT') {
         value(dv.value)
      }
   }
   private void serializeDv(DvCodedText dv, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':'DV_CODED_TEXT') {
         value(dv.value)
         defining_code() {
            terminology_id() {
               value(dv.terminologyIdName) // TODO: version
            }
            code_string(dv.codeString)
         }
         
      }
   }
   private void serializeDv(DvBoolean dv, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':'DV_BOOLEAN') {
         value(dv.value)
      }
   }
}
