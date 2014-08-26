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

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * Serializador del modelo de datos a XML openEHR valido.
 * 
 * @author Pablo Pazos Gutierrez <pablo.pazos@cabolabs.com>
 *
 */
class XmlSerializer {

   //static def formatter = new SimpleDateFormat("yyyyMMdd'T'hhmmss.SSSSZ")
   def formatter = new SimpleDateFormat( ApplicationHolder.application.config.app.l10n.datetime_format )
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
      
      templateId = doc.templateId
      
      /**
       * Se incluye version para hacer commit al servidor
       */
      if (includeVersion)
      {
         builder.version(xmlns:'http://schemas.openehr.org/v1',
                         'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance') {
            commit_audit() {
               system_id('CABOLABS_EHR') // TODO: should be configurable
               
               time_committed(formatter.format( cses.dateClosed ))
               
               change_type() {
                  value('creation') // por ahora solo soporta creation
                  defining_code() {
                     terminology_id() {
                        value('openehr')
                     }
                     code_string(249)
                  }
               }
               
               /*
               Identity and optional reference into identity
               management service, of user who committed the item.
               */
               committer() {
                  // T0003
                  // FIXME: Si este no es el id del sistema que comitea, donde va ese id?
                  //name('ISIS_EMR_APP') // id de esta aplicaicon TODO: sacarlo de config
                  name(cses.composer.name)
                  // TODO: poner id para PartyProxy en el server
               }
            } // commit_audit
            
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
         
         // Sino le paso una ClinicalSession (ej. testing)
         if (!cses)
         {
            name('Dr. Pablo Pazos')
         }
         else
         {
            // FIXME: podria tener mas datos en User, ej. algun id unico de uso interno.
            name(cses.composer.name)
         }
      }
      
      
      builder.context() {
         start_time() {
            value( formatter.format( doc.start ) )
         }
         setting() {
            value('Hospital Montevideo')
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
     
     /*
     if (item.class == 'registros.Structure')
	  {
	     compositionContentRecursive( (Structure)struct, builder, tag)
	  }
	  else if (item.class == 'registros.Element')
	  {
	     compositionContentRecursive( (Element)struct, builder, tag)
	  }
	  else
	  {
	     // Clase no soportada class registros.Item_$$_javassist_13
	     println "Clase no soportada "+ item.class
	  }
     */
   }
   
   private void compositionContentRecursive(Structure struct, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':struct.type, archetype_node_id:struct.nodeId) {
         name() {
            //value('TODO: lookup del nombre en el arquetipo')
            value( getName(this.templateId, struct.archetypeId, struct.nodeId))
         }
         // language
         // encoding
         // subject para ENTRIES!!!
         
         //println " <<<< struct.type: "+ struct.type +", struct.attributes " + struct.attributes
         
         struct.attributes.each { attrName, dv ->
            
            //println "   >>>> " + attrName + " " + dv
            serializeDv( dv, builder, attrName )
         }
         
         // TODO: los atributos dependen del tipo del RM (struct.type)
         struct.items.each { item ->
            compositionContentRecursive(item, builder, item.attr)
         }
      }
   }
   
   private void compositionContentRecursive(Element element, MarkupBuilder builder, String tag)
   {
      builder."$tag"('xsi:type':element.type, archetype_node_id:element.nodeId) {
         
         name() {
            value( getName(this.templateId, element.archetypeId, element.nodeId))
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
      }
   }
   private void serializeDv(DvDateTime dv, MarkupBuilder builder, String tag)
   {
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