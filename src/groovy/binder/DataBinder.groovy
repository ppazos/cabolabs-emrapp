package binder

import com.cabolabs.openehr.opt.model.AttributeNode
import com.cabolabs.openehr.opt.model.ObjectNode
import com.cabolabs.openehr.opt.model.OperationalTemplate
import groovy.util.slurpersupport.GPathResult
import java.util.List
import java.util.Map
//import opt_repository.OperationalTemplate
import org.apache.log4j.Logger

/*
import org.openehr.am.archetype.Archetype
import org.openehr.am.archetype.constraintmodel.CAttribute
import org.openehr.am.archetype.constraintmodel.C_COMPLEX_OBJECT
import org.openehr.am.archetype.constraintmodel.CMultipleAttribute
import org.openehr.am.archetype.constraintmodel.CPrimitiveObject
import org.openehr.am.archetype.constraintmodel.CSingleAttribute
import org.openehr.am.archetype.constraintmodel.ConstraintRef
import org.openehr.am.openehrprofile.datatypes.quantity.CDvQuantity
import org.openehr.am.openehrprofile.datatypes.text.CCodePhrase
*/
import registros.Document
import registros.Element
import registros.Structure
import registros.valores.DataValue
import registros.valores.DvBoolean
import registros.valores.DvCodedText
import registros.valores.DvDateTime
import registros.valores.DvQuantity
import registros.valores.DvText

/**
 * http://www.openehr.org/wsvn/ref_impl_java/TRUNK/openehr-ap/src/main/java/org/openehr/am/openehrprofile/datatypes/quantity/#path_TRUNK_openehr-ap_src_main_java_org_openehr_am_openehrprofile_datatypes_quantity_
 * http://www.openehr.org/wsvn/ref_impl_java/TRUNK/openehr-aom/src/main/java/org/openehr/am/archetype/constraintmodel/#path_TRUNK_openehr-aom_src_main_java_org_openehr_am_archetype_constraintmodel_
 * http://www.openehr.org/wsvn/ref_impl_java/TRUNK/openehr-aom/src/main/java/org/openehr/am/archetype/constraintmodel/primitive/#path_TRUNK_openehr-aom_src_main_java_org_openehr_am_archetype_constraintmodel_primitive_
 * 
 * @author Pablo Pazos Gutierrez
 *
 * ========================================================================
 * Practico Clase 5:
 * TODO: los alumnos deben implementar algunas validaciones de datos
 *       basadas en las paths recibidas y las restricciones a las que
 *       hacen referencia en el AOM (los distintos tipos de restricciones
 *       deben documentarse para referencia).
 * ========================================================================
 *
 */
class DataBinder {

   private Logger log = Logger.getLogger(getClass())
   
   
   OperationalTemplate template
   
   // Current archetypeId for the node being binded, used for context
   String archetypeId
   
   DataBinder(OperationalTemplate template)
   {
      this.template = template
      this.archetypeId = template.definition.archetypeId
   }
   
   /**
    * Caso de bind:
    * 
    * [fecha_espera_month:10, 
    * fecha_espera:Mon Oct 01 20:28:00 GFT 2012, 
    * categoria_estudio:at0010, 
    * archetypeId:openEHR-EHR-COMPOSITION.orden_de_estudio_de_laboratorio.v1, 
    * fecha_espera_hour:20, tipo_estudio:tipo estudio, 
    * descripcion:desc, 
    * fecha_espera_minute:28, 
    * fecha_espera_year:2012, 
    * fecha_espera_day:1, 
    * urgente:on, 
    * action:save, 
    * controller:registros]
    * 
  * [
  * /content[at0002]/activities[at0003]/description[at0004]/items[at0009]/value:Mon Oct 01 20:28:00 GFT 2012, 
  * /content[at0002]/activities[at0003]/description[at0004]/items[at0005]/value:at0010, 
  * /content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value:tipo estudio, 
  * /content[at0002]/activities[at0003]/description[at0004]/items[at0008]/value:desc, 
  * /content[at0002]/activities[at0003]/description[at0004]/items[at0007]/value:on]

No se encuentra el arquetipo openEHR-EHR-COMPOSITION.orden_de_estudio_de_laboratorio.v1, se intenta cargarlo
Carga desde: archetypes\composition\openEHR-EHR-COMPOSITION.orden_de_estudio_de_laboratorio.v1.adl

 bind_C_COMPLEX_OBJECT_COMPOSITION
     .bind_C_COMPLEX_OBJECT_DV_CODED_TEXT [
          /content[at0002]/activities[at0003]/description[at0004]/items[at0009]/value:Mon Oct01 20:28:00 GFT 2012, 
          /content[at0002]/activities[at0003]/description[at0004]/items[at0005]/value:at0010, 
          /content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value:tipo estudio, 
          /content[at0002]/activities[at0003]/description[at0004]/items[at0008]/value:desc, 
          /content[at0002]/activities[at0003]/description[at0004]/items[at0007]/value:on]
      bind_CCodePhrase_CodePhrase [:]

 bind_C_COMPLEX_OBJECT_EVENT_CONTEXT
    bind_C_COMPLEX_OBJECT_ITEM_TREE
  bind_C_COMPLEX_OBJECT_INSTRUCTION
   bind_C_COMPLEX_OBJECT_ACTIVITY
    bind_C_COMPLEX_OBJECT_ITEM_TREE
    .bind_C_COMPLEX_OBJECT_ELEMENT
     .bind_C_COMPLEX_OBJECT_DV_CODED_TEXT [/content[at0002]/activities[at0003]/description[at0004]/items[at0005]/value:at0010]
      bind_CCodePhrase_CodePhrase [/content[at0002]/activities[at0003]/description[at0004]/items[at0005]/value:at0010]

    .bind_C_COMPLEX_OBJECT_ELEMENT
     .bind_C_COMPLEX_OBJECT_DV_CODED_TEXT [/content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value:tipo estudio]
      bind_ConstraintRef_CodePhrase [/content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value:tipo estudio]

    .bind_C_COMPLEX_OBJECT_ELEMENT
     .bind_C_COMPLEX_OBJECT_DV_BOOLEAN [/content[at0002]/activities[at0003]/description[at0004]/items[at0007]/value:on]
    ...bind_CPrimitiveObject_DvBoolean

    .bind_C_COMPLEX_OBJECT_ELEMENT
     .bind_C_COMPLEX_OBJECT_DV_TEXT [/content[at0002]/activities[at0003]/description[at0004]/items[at0008]/value:desc]
bind_C_COMPLEX_OBJECT_DV_TEXT sin restricciones: DV_TEXT [/content[at0002]/activities[at0003]/description[at0004]/items[at0008]/value:desc]
    .bind_C_COMPLEX_OBJECT_ELEMENT
     .bind_C_COMPLEX_OBJECT_DV_DATE_TIME [/content[at0002]/activities[at0003]/description[at0004]/items[at0009]/value:Mon Oct 01 20:28:00 GFT 2012]
    ...bind_CPrimitiveObject_DvDateTime
    */
   
   Document bind(Map bind_data)
   {
      // muestra: 2012-11-21 14:45:42,980 [http-bio-8080-exec-5] INFO  binder.DataBinder  - log DataBinder.bind()
      // TODO: ver como customizar los datos del mensaje en stdout
      // TODO: ver como guardar en archivo
      log.info('bind()')
      
      
      // DEBUG
      //new File("test_template.xml") << groovy.xml.XmlUtil.serialize( template )
      
      
      // bind_C_COMPLEX_OBJECT_COMPOSITION
      String method = 'bind_C_COMPLEX_OBJECT_'+ template.definition.rmTypeName

      return this."$method"(template.definition, bind_data)
   }
   
   Map filterData(Map bind_data, String filter_path)
   {
      log.info('filterData()')
      
      Map filtered_data = [:]

      // Filtra bind_data por path
      bind_data.each { path, value ->
         
         //println "filterData: if $path startsWith " + filter_path
         
         // Value puede ser multiple
         if (path.startsWith(filter_path)) filtered_data[path] = value
      }
      
      return filtered_data
   }
   
   Document bind_C_COMPLEX_OBJECT_COMPOSITION(ObjectNode cobject, Map bind_data)
   {
      log.info('bind_C_COMPLEX_OBJECT_COMPOSITION() '+ cobject.path)
      
      // bind_data values DEBEN ser todos strings para poder salvarlos
      // esto se lo dejo a beforeInsert en Document
      
      Document doc = new Document(bindData: bind_data)
      
      doc.compositionArchetypeId = cobject.archetypeId
      
      String method
      Map filtered_data = [:]
      AttributeNode attr // CAttribute
      List items
      
      /*
       * Composition attributes:
       *  - category: DV_CODED_TEXT (path: /category) "persistent", "event"
       *     <Concept Language="en" ConceptID="432" Rubric="Composition category"/>      nombre del concepto
       *     <Concept Language="en" ConceptID="431" Rubric="persistent"/>                   category 20
       *     <Concept Language="en" ConceptID="433" Rubric="event"/>                        category 20
       *     <Concept Language="en" ConceptID="434" Rubric="process"/>                      category 20
       *     
       *     <Concept Language="en" ConceptID="226" Rubric="Setting"/>                   nombre del concepto
       *     <Concept Language="en" ConceptID="435" Rubric="laboratory"/>                   setting 10
       *     <Concept Language="en" ConceptID="436" Rubric="imaging"/>                      setting 10
       *     <Concept Language="en" ConceptID="225" Rubric="home"/>                         setting 10
       *     <Concept Language="en" ConceptID="227" Rubric="emergency care"/>               setting 10
       *     <Concept Language="en" ConceptID="228" Rubric="primary medical care"/>         setting 10
       *     <Concept Language="en" ConceptID="229" Rubric="primary nursing care"/>         setting 10
       *     <Concept Language="en" ConceptID="230" Rubric="primary allied health care"/>   setting 10
       *     <Concept Language="en" ConceptID="231" Rubric="midwifery care"/>               setting 10
       *     <Concept Language="en" ConceptID="232" Rubric="secondary medical care"/>       setting 10
       *     <Concept Language="en" ConceptID="233" Rubric="secondary nursing care"/>       setting 10
       *     <Concept Language="en" ConceptID="234" Rubric="secondary allied health care"/> setting 10
       *     <Concept Language="en" ConceptID="235" Rubric="complementary health care"/>    setting 10
       *     <Concept Language="en" ConceptID="236" Rubric="dental care"/>                  setting 10
       *     <Concept Language="en" ConceptID="237" Rubric="nursing home care"/>            setting 10
       *     <Concept Language="en" ConceptID="238" Rubric="other care"/>                   setting 10
       *      
       *  - context: EVENT_CONTEXT (si es eventual) (path: /context)
       *  - content: *CONTENT_ITEM (SECTION o ENTRY) (path: /content)
       * 
       * Atributos no arquetipados:
       *  - composer: PARTY_PROXY
       *  - language: CODE_PHRASE (se saca de la config)
       *  - territory: CODE_PHRASE (se saca de la config)
       */
      
      // ------------------------------------------------------------------------------
      // bind attribute: context
      /*
      attr = cobject.attributes.find { it.rmAttributeName == "context" }
      filtered_data = filterData(bind_data, "/context")
      
      method = 'bind_'+ attr.'@xsi:type'.text()
      items = this."$method"(attr, filtered_data)
      
      //println " context items: " + items
      
      items.each { item ->
         
         strct.addToItems(item) // items es uno solo
      }
      
      filtered_data = [:]
      */
      // ------------------------------------------------------------------------------
      
      // ------------------------------------------------------------------------------
      // bind attribute: context
      // FIXME: sacar la category de la config o ponerlo en el arquetipo
      /*
      attr = cobject.attributes.find { it.rmAttributeName == "category" }
      filtered_data = filterData(bind_data, "/category")
      
      method = 'bind_'+ attr.'@xsi:type'.text()
      items = this."$method"(attr, filtered_data)
      
      items.each { item ->
         
         // FIXME: esto es un DataValue no un item...
         strct.addToItems(item) // items es uno solo
      }
      
      filtered_data = [:]
      */
      // ------------------------------------------------------------------------------
      
      // ------------------------------------------------------------------------------
      // bind attribute: content
      attr = cobject.attributes.find { it.rmAttributeName == "content" } // COMSPOSITION tiene content y category
      filtered_data = filterData(bind_data, "/content")
      
      method = 'bind_'+ attr.type // bind_C_MULTIPLE_ATTRIBUTE
      items = this."$method"(attr, filtered_data)
      
      //println " content items: " + items
      // content items: [registros.Structure : null]
      
      items.each { item ->
         
         doc.addToContent(item) // items es uno solo
      }
      
      filtered_data = [:]
      // ------------------------------------------------------------------------------
   
      
      return doc
   }
   
   Structure bind_C_COMPLEX_OBJECT_EVENT_CONTEXT(ObjectNode cobject, Map bind_data)
   {
      log.info('bind_C_COMPLEX_OBJECT_EVENT_CONTEXT() '+ cobject.path.text())
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:cobject.path.text(),
         nodeId:cobject.node_id.text(),
         type:cobject.rm_type_name.text(),
         aomType:'C_COMPLEX_OBJECT')
      
      String method
      def filtered_data = [:]
      List items
      
      /**
       * Atributos de EVENT_CONTEXT:
       *  - start_time: DV_DATE_TIME
       *  - end_time: DV_DATE_TIME
       *  - location: String (se saca del contexto de la session)
       *  - setting: DV_CODED_TEXT (se saca del contexto de la session)
       *  - other_context: ITEM_STRUCTURE
       *  - health_care_facility: PARTY_IDENTIFIED
       *  - participations: *PARTICIPATION
       */
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path.text())

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.'@xsi:type'.text()
         
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
   }
   
   Structure bind_C_COMPLEX_OBJECT_OBSERVATION(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_OBSERVATION() '+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:    cobject.path,
         nodeId:  cobject.nodeId,
         type:    cobject.rmTypeName,
         aomType: 'C_COMPLEX_OBJECT',
         attr:    attrName)
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
      
   } // bind_C_COMPLEX_OBJECT_OBSERVATION
   
   Structure bind_C_COMPLEX_OBJECT_HISTORY(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_HISTORY() '+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:     cobject.path, 
         nodeId:   cobject.nodeId, 
         type:     cobject.rmTypeName, 
         aomType:  'C_COMPLEX_OBJECT', 
         attr:     attrName,
         attributes: ['origin': new DvDateTime(value:new Date())]
      )
      
      //strct.attributes = ['origin': new DvDateTime(value:new Date())]
      
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
      
   } // bind_C_COMPLEX_OBJECT_HISTORY
   
   // ==============================================================================
   // EVENTS
   // ==============================================================================
   
   // The template should not use the abstract type EVENT, just added this for testing.
   Structure bind_C_COMPLEX_OBJECT_EVENT(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_EVENT()'+ cobject.path)
      return bindGenericEvent(cobject, bind_data, attrName)
   }
   
   Structure bind_C_COMPLEX_OBJECT_POINT_EVENT(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_POINT_EVENT()'+ cobject.path)
      return bindGenericEvent(cobject, bind_data, attrName)
   }
   
   Structure bind_C_COMPLEX_OBJECT_INTERVAL_EVENT(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_INTERVAL_EVENT()'+ cobject.path)
      return bindGenericEvent(cobject, bind_data, attrName)
   }
   
   private Structure bindGenericEvent(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bindGenericEvent()'+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:        cobject.path,
         nodeId:      cobject.nodeId,
         type:        ((cobject.rmTypeName == 'EVENT')?'POINT_EVENT':cobject.rmTypeName), // Use concrete type POINT_EVENT if abstract type EVENT is detected.
         aomType:     'C_COMPLEX_OBJECT',
         attr:        attrName,
         attributes:  ['time': new DvDateTime(value:new Date())]
      )
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
      
   } // bindGenericEvent
   
   // ==============================================================================
   // /EVENTS
   // ==============================================================================
      
   DataValue bind_C_DV_QUANTITY_DV_QUANTITY(ObjectNode cdv, Map bind_data, String attrName)
   {
      // Crear Boolean
      //println "    ...bind_CDvQuantity_DvQuantity "+ cdv.path.text() +" "+ bind_data +" "+ attrName
      //println cdv.list
      //println ""
      log.info('bind_C_DV_QUANTITY_DV_QUANTITY()')
      
      println "+++++++++++++++++++++++++++++++"
      
      //println template.paths
      //println cdv
      println cdv.path
      
      println "===================="
      println bind_data[cdv.path + "/magnitude"]
      println "===================="
      
      /*
       * cdv.property: CodePhrase
       * cdv.list: List<CDvQuantityItem>
       * 
       * cdvqitem.magnitude: Interval<Real>
       * cdvqitem.precision: Inteval<Integer>
       * cdvqitem.units: String
       * 
       * ...bind_CDvQuantity_DvQuantity
       *  
       * /content[at0028]/data[at0029]/events[at0030]/data[at0031]/items[at0032]/value
       *  [
       *  /content[at0028]/data[at0029]/events[at0030]/data[at0031]/items[at0032]/value/units:cm, 
       *  /content[at0028]/data[at0029]/events[at0030]/data[at0031]/items[at0032]/value/magnitude:181
       *  ]
       *  
       *  [org.openehr.am.openehrprofile.datatypes.quantity.CDvQuantityItem@88db432c]
       */
      
      return new DvQuantity( magnitude: bind_data[cdv.path + "/magnitude"],
                             units:     bind_data[cdv.path + "/units"],
                             aomType:   'C_DV_QUANTITY' )
   }
   
   
   
   Structure bind_C_COMPLEX_OBJECT_INSTRUCTION(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_INSTRUCTION()'+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:        cobject.path,
         nodeId:      cobject.nodeId,
         type:        cobject.rmTypeName,
         aomType:     'C_COMPLEX_OBJECT',
         attr:        attrName,
         attributes:  ['narrative': new DvText(value: 'TODO: instruction narrative')]
      )
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
   }
   
   Structure bind_C_COMPLEX_OBJECT_ACTIVITY(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_ACTIVITY()'+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path: cobject.path,
         nodeId: cobject.nodeId, 
         type: cobject.rmTypeName, 
         aomType: 'C_COMPLEX_OBJECT', 
         attr: attrName)
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)
         
         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
   }
   
   Structure bind_C_COMPLEX_OBJECT_ITEM_TREE(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_ITEM_TREE()'+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:    cobject.path,
         nodeId:  cobject.nodeId,
         type:    cobject.rmTypeName,
         aomType: 'C_COMPLEX_OBJECT',
         attr:    attrName)
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
   }
   
   Structure bind_C_COMPLEX_OBJECT_CLUSTER(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_CLUSTER()'+ cobject.path)
      
      Structure strct = new Structure(
         archetypeId: archetypeId,
         path:    cobject.path,
         nodeId:  cobject.nodeId,
         type:    cobject.rmTypeName,
         aomType: 'C_COMPLEX_OBJECT', 
         attr:attrName)
      
      String method
      def filtered_data = [:]
      List items
            
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         items = this."$method"(attr, filtered_data)
         
         items.each { item ->
            
            strct.addToItems(item)
         }
         
         filtered_data = [:]
      }
      
      return strct
   }
   
   Element bind_C_COMPLEX_OBJECT_ELEMENT(ObjectNode cobject, Map bind_data, String attrName)
   {
      //println "     bind_C_COMPLEX_OBJECT_ELEMENT " + cobject.path.text() +" "+ attrName
      //println "     - cobject.attrs: "+ cobject.attributes.rmAttributeName
      log.info('bind_C_COMPLEX_OBJECT_ELEMENT()'+ cobject.path)
      
      String method
      def filtered_data = [:]
      Map items = [:]
      
      // FIXME: ir a la restriccion de value directamente
      // TODO: soportar nullFlavour (en el arquetipo deberia venir especificado que se va a usar, sino no se le da bola)
      // Tiene restriccion solo para value (podria tener para nullFlavour)
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
//         println "______________________"
//         println "ELEMENT.value: " + method
//         println "______________________"
//         
         items[attr.rmAttributeName] = this."$method"(attr, filtered_data)
         
         filtered_data = [:]
      }
      
      println ""
      println "ELEMENT value binded: "+ items
      println ""
      
      
      def element = null
      def value = items['value']
      def name = items['name']
      
      // Prueba para cuando no se envian todos los datos necesarios para 
      // crear los datavalues.
      
      
      element = new Element(
         value:   value,
         name:    name,
         archetypeId: archetypeId,
         path:    cobject.path,
         nodeId:  cobject.nodeId,
         type:    cobject.rmTypeName,
         aomType: 'C_COMPLEX_OBJECT',
         attr:    attrName)
      
      // elem.value no siempre tiene .value, depende del tipo de DataValue,
      // esto era para ver si el valor del DvCodedText es null o vacio
      //
      //if (element.value && element.value.value)
      //   println "       tipo del valor de ELEMENT: " + element.value.class + " " + element.value.value.class
      //else
         
//         if (element.value)
//            println "       tipo del valor de ELEMENT: " + element.value.class + " valor="+ element.value
//         else
//            println "       dice que ELEMENT.value es null para "+ cobject.path
            
      
      return element
   }
   
   DataValue bind_C_COMPLEX_OBJECT_DV_TEXT(ObjectNode cobject, Map bind_data, String attrName)
   {
      //println "     .bind_C_COMPLEX_OBJECT_DV_TEXT "+ cobject.path.text() +" "+ bind_data +" "+ attrName
      log.info('bind_C_COMPLEX_OBJECT_DV_TEXT()'+ cobject.path)
      
      
      if (!cobject.attributes || cobject.attributes.size() == 0)
      {
         //println "-- bind_C_COMPLEX_OBJECT_DV_TEXT sin restricciones: " + cobject.rm_type_name.text() + " "+ bind_data
         //println ""
         
         return new DvText(value:bind_data[cobject.path], aomType: 'C_COMPLEX_OBJECT')
      }
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         items = this."$method"(attr, filtered_data)
         
         println " dv_text items: "+ items
         
         filtered_data = [:]
      }
      
      log.debug("DvText bind value is not set for multiple occurrences")
      
      return new DvText(value:"todo", aomType: 'C_COMPLEX_OBJECT')
   }
   
   
   DataValue bind_C_COMPLEX_OBJECT_DV_CODED_TEXT(ObjectNode cobject, Map bind_data, String attrName)
   {
      println ""
      log.info('bind_C_COMPLEX_OBJECT_DV_CODED_TEXT()'+ cobject.path)
      println " >>>> bind_data: "+ bind_data
      
      if (!cobject.attributes || cobject.attributes.size() == 0)
      {
         println " >>>> -- bind_C_COMPLEX_OBJECT_DV_CODED_TEXT sin restricciones: " + cobject.rm_type_name.text() + " "+ bind_data
      }
      
      String method
      def filtered_data = [:]
      List items
      
      cobject.attributes.each { attr ->
         
         filtered_data = filterData(bind_data, cobject.path)

         // bind_CMultipleAttribute
         // bind_CSingleName
         method = 'bind_'+ attr.type
         
         println " >>>> "+ attr +" "+ method
         
         // [[code_string:todo, terminology_id_name:todo, terminology_id_version:todo]] viene de bind_xxxx_CodePhrase
         items = this."$method"(attr, filtered_data)
         
         //println " dv_coded_text items: " + items
         // dv_coded_text items: [[codeString:at0014, terminologyIdName:local, terminologyIdVersion:null]]
         // dv_coded_text items: [[codeString:todo, terminologyIdName:todo, terminologyIdVersion:todo]]
         filtered_data = [:]
      }
      
      println " >>>> -+++++ DV_CODED_TEXT +++ "
      println " >>>> items: "+ items // items: [[codeString:null, terminologyIdName:, terminologyIdVersion:]]
      
      // FIXME: para bind_ConstraintRef_CodePhrase esto es el texto: OK
      //        para bind_CCodePhrase_CodePhrase esto es el codeString: CORREGIR
      //
      //        Se deberia diferenciar por la path: si termina en /value/defining_code es el codigo
      //        Si termina en /value/value es el valor del DvCodedText
      //
      //items[0]["value"] = bind_data[cobject.path.text()] // Si la terminologia es local, el value lo saco del arquetipo, de la ontologia.
      
      // FIXME: hardcoded locale
      // Sino pregunto esto, el codeString es TODO y devuelve un termino null
      if (items[0]["terminologyIdName"] == "local")
      {
         //items[0]["value"] = archetype.ontology.termDefinition("es", items[0]["codeString"]).getText()
         println " >>>> archetype: "+ cobject.archetypeId +" term: "+ items[0]["codeString"]
         items[0]["value"] = template.getTerm(cobject.archetypeId, items[0]["codeString"])
      }
      else // se deberia sacar el codigo del ST
      {
         // Usa los STs y genera esto:
         /* <registros.Element>
              <type>ELEMENT</type>
              <path>/content[at0002]/activities[at0003]/description[at0004]/items[at0006]</path>
              <nodeId>at0006</nodeId>
              <aomType>C_COMPLEX_OBJECT</aomType>
              <value class="registros.valores.DvCodedText">
                <value>hemólisis en sacarosa</value>
                <codeString>13535-0</codeString>
                <terminologyIdName>LOINC</terminologyIdName>
                <terminologyIdVersion>todo</terminologyIdVersion>
              </value>
            </registros.Element> */
         
         // [/content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value/defining_code:13535-0,
         //  /content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value/value:hem├│lisis en sacarosa] <<< este es el value
         //println "DATA: " + bind_data[cobject.path.text() + "/value"]
         items[0]["value"] = bind_data[cobject.path + "/value"] // Debe venir el value de la ui como path()/value (.../value/value)
      }
      
      // DV_CODED_TEXT items: [[codeString:at0014, terminologyIdName:local, terminologyIdVersion:null, value:microbiologia]]
      // DV_CODED_TEXT items: [[codeString:todo, terminologyIdName:todo, terminologyIdVersion:todo]]
      items[0]["aomType"] = 'C_COMPLEX_OBJECT'
      
      
      // [[value: ..., codeString: ..., terminologyIdName:..., terminologyIdVersion: ...]]
      // bind_data:
      /*
       * [/content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/value/units:/min,
       * /content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/value/magnitude:45,
       * /content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/name/defining_code/code_string:at1026]
       */
      // FIXME: data is null
      // 
      println " >>>> ++++++ DV_CODED_TEXT: "+ items[0]
      println "  "+ new DvCodedText(items[0]).validate() // false
      
      def d = new DvCodedText(items[0])
      if (!d.validate())
      {
         d.errors.each {
            println it
         }
      }
      
      println " >>>> /+++++ DV_CODED_TEXT +++ "
      println ""
      
      return new DvCodedText(items[0])
   }
   
   DataValue bind_C_COMPLEX_OBJECT_DV_BOOLEAN(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_DV_BOOLEAN()'+ cobject.path)
      
      /*
       * .bind_C_COMPLEX_OBJECT_DV_BOOLEAN
       * /content[at0002]/activities[at0003]/description[at0004]/items[at0007]/value
       * [/content[at0002]/activities[at0003]/description[at0004]/items[at0007]/value:on]
       */
      //println "     .bind_C_COMPLEX_OBJECT_DV_BOOLEAN "+ cobject.path.text() +" " + bind_data +" "+ attrName
      //println ""
      
      String method
      def filtered_data = [:]
      List items
      
      // En bind_data[cobject.path.text()] hay un string true o false
      // TODO: podria NO venir valor
      // TODO: podrian venir multiples valores
      return new DvBoolean(value: Boolean.parseBoolean( bind_data[cobject.path] ),
                           aomType: 'C_COMPLEX_OBJECT')
   }
   
   DataValue bind_C_COMPLEX_OBJECT_DV_DATE_TIME(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_C_COMPLEX_OBJECT_DV_DATE_TIME()'+ cobject.path)
      
      /*
       * .bind_C_COMPLEX_OBJECT_DV_DATE_TIME
       * /content[at0002]/activities[at0003]/description[at0004]/items[at0009]/value
       * [/content[at0002]/activities[at0003]/description[at0004]/items[at0009]/value:Mon Oct 01 20:28:00 GFT 2012]
       */
      //println "     .bind_C_COMPLEX_OBJECT_DV_DATE_TIME "+ cobject.path.text() +" "+ bind_data +" "+ attrName
      //println ""

      String method
      def filtered_data = [:]
      List items
      
      // java.util.date OK
      //println "valor deberia ser date y es: " + bind_data[cobject.path.text()].class
      
      // Ya bindea si el valor es unido
      // FIXME: valores multiples
      
      // FIXME: es al reves, lo que hay que castear es a String el Date en Document.bindData
      // clona la fecha desde los datos porque se usa para Document.bindData donde
      // se castea a String y este hace referencia a al mismo objeto que luego es
      // un String y tira una except al salvar porque espera un Date (Grails ya
      // bindea el java.util.Date)
      return new DvDateTime(value: bind_data[cobject.path], //.clone(),
                            aomType: 'C_COMPLEX_OBJECT')
   }
   
   /**
    * Viene de bind_C_COMPLEX_OBJECT_DV_BOOLEAN
    * 
    * @param cobject
    * @param bind_data
    * @return
    */
   Map bind_CPrimitiveObject_DvBoolean(ObjectNode cobject, Map bind_data)
   {
      log.info('bind_CPrimitiveObject_DvBoolean()'+ cobject.path)
      
      // Crear Boolean
      //println "    ...bind_CPrimitiveObject_DvBoolean "+ cobject.path.text() +" "+ bind_data
      //println ""
      
      // TODO: verificar si llega a ejecutar esta operacion.
      
      log.debug("DV_BOOLEAN bind is not implemented")
      return [:]
   }
   
   /**
    * Viene de bind_C_COMPLEX_OBJECT_DV_DATE_TIME
    * 
    * @param cobject
    * @param bind_data
    * @return
    */
   Map bind_CPrimitiveObject_DvDateTime(ObjectNode cobject, Map bind_data)
   {
      log.info('bind_CPrimitiveObject_DvDateTime()'+ cobject.path)
      
      // Crear DateTime
      //println "    ...bind_CPrimitiveObject_DvDateTime "+ cobject.path.text() +" "+ bind_data
      //println ""
      
      // TODO: verificar si llega a ejecutar esta operacion.
      
      log.debug("DV_DATE_TIME bind is not implemented")
      
      return [:]
   }
   
   /**
    * Viene de bind_C_COMPLEX_OBJECT_DV_CODED_TEXT con restriccion ConstraintRef a acNNNN:
    * 
    * ELEMENT[at0006] occurrences matches {0..1} matches {
         value matches {
            DV_CODED_TEXT matches {
               defining_code matches {[ac0001]} <<<<< Para procesar esto (CodePhrase)
            }
         }
      }
    * 
    * @param cobject
    * @param bind_data
    * @return
    */
   Map bind_ConstraintRef_CodePhrase(ObjectNode cobject, Map bind_data, String attrName)
   {
      log.info('bind_ConstraintRef_CodePhrase()'+ cobject.path)
      
      //println "      bind_ConstraintRef_CodePhrase "+ cobject.path.text() +" "+ bind_data +" "+ attrName
      //println ""
      
      /*
      println cobject
      org.openehr.am.archetype.constraintmodel.ConstraintRef@3ebd4[
        reference=ac0001
        rm_type_name.text()=CodePhrase
        occurrences=org.openehr.rm.support.basic.Interval@16fb175[lower=1,lowerIncluded=true,upper=1,upperIncluded=true]
        node_id.text()=<null>
        parent=<null>
        anyAllowed=false
        path=/content[at0002]/activities[at0003]/description[at0004]/items[at0006]/value/defining_code
      ]
      */
      
      /*
       * DV_CODED_TEXT matches {
       *   defining_code matches {[ac0001]}    -- Estudios de laboratorio LOINC
       * }
       * 
       * constraint_definitions = <
            ["es"] = <
               items = <
                  ["ac0001"] = <
                     text = <"Estudios de laboratorio LOINC">
                     description = <"*">
                  >

         constraint_bindings = <
            ["LOINC"] = <
               items = <
                  ["ac0001"] = <terminology:LOINC>
               >
       */
      // Estudios de laboratorio LOINC
      //println "._. " + archetype.ontology.constraintDefinition("es", cobject.reference).getText()
      
      /*
       [org.openehr.am.archetype.ontology.OntologyBinding@1536eb4[
         terminology=LOINC
         bindingList=[org.openehr.am.archetype.ontology.QueryBindingItem@6c0370[
           query=org.openehr.am.archetype.ontology.Query@9db94d[
             url=terminology:LOINC
           ]
           code=ac0001
         ]]
       ]]
      */
      //def bindings = archetype.ontology.getConstraintBindingList()
      
      // FIXME: verificar si la terminologia es la correcta para el CODE_PHRASE
      // FIXME: que uso si tengo mas de una terminologia en bindings
      def bindings = this.template.getTermBindings()
      
      //println bindings[0].terminology.class.getSimpleName() // String
      
      log.debug("CODE_PHRASE bind terminology version is empty")
      
      // Crea el equivalente al CodePhrase
      // TODO: terminology y version deben ir en el mismo campo codificado
      return [codeString: bind_data[cobject.path], terminologyIdName: bindings[0].@terminology /*bindings[0].terminology*/, terminologyIdVersion: "todo"]
   }
   
   /**
    * Viene de bind_C_COMPLEX_OBJECT_DV_CODED_TEXT con restriccion de CodePhrase:
    * 
    * ELEMENT[at0005] occurrences matches {0..1} matches {  -- categoria
         value matches {
            DV_CODED_TEXT matches {
               defining_code matches { <<<<<< Esta restriccion
                  [local::
                  at0010,  -- orina
                  at0011,  -- sangre
                  at0012,  -- citologia
                  at0013,  -- histologia
                  at0014]  -- microbiologia
    */
   Map bind_C_CODE_PHRASE_CODE_PHRASE(ObjectNode cobject, Map bind_data, String attrName)
   {
      // /content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/name/defining_code
      log.info('bind_C_CODE_PHRASE_CODE_PHRASE()'+ cobject.path +" "+ attrName)
      
      // bind_CCodePhrase_CodePhrase /content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/name/defining_code
      // [/content[archetype_id=openEHR-EHR-OBSERVATION.pulse.v1]/data[at0002]/events[at0003]/data[at0001]/items[at0004]/name/defining_code/code_string:at1026]
      // defining_code
      //println "      bind_CCodePhrase_CodePhrase "+ cobject.path +" " + bind_data +" "+ attrName
      //
      // cobject.xmlNode = <children xsi:type="C_CODE_PHRASE">
      
      // Crea el equivalente al CodePhrase
      // FIXME: ver de donde sacar la version (si es local, no hay version)
      return [ codeString: bind_data[cobject.path + "/code_string"], 
               terminologyIdName: cobject.xmlNode.terminology_id.value.text()
               //, terminologyIdVersion: cobject.xmlNode.terminology_id.version.text()
               ]
   }
   
   
   /**
    * @link http://www.openehr.org/wsvn/ref_impl_java/TRUNK/openehr-aom/src/main/java/org/openehr/am/archetype/constraintmodel/CSingleAttribute.java
    * @param cattr
    * @param bind_data
    * @return
    */
   List bind_C_SINGLE_ATTRIBUTE(AttributeNode cattr, Map bind_data)
   {
      //log.info('bind_C_SINGLE_ATTRIBUTE()'+ cattr.rmAttributeName)
      
      String method
      def attrs = []
      def attr
      def adl_class
      cattr.children.each { cobject -> // cattr.alternatives en ADL
         
         //println "  attr obj: "+ cobject.rm_type_name.text()
         
         // Si es una referencia a otro arquetipo, el tipo de ADL es complex object
         adl_class = cobject.type
         if (adl_class == "ARCHETYPE_SLOT") return // avoid SLOTS see https://github.com/ppazos/cabolabs-emrapp/issues/12
         if (adl_class == "C_ARCHETYPE_ROOT")
         {
            adl_class = "C_COMPLEX_OBJECT"
            this.archetypeId = cobject.archetypeId // if object is root it has a different archetypeId
         }

         method = 'bind_'+ adl_class +'_'+ cobject.rmTypeName
         attr = this."$method"(cobject, bind_data, cattr.rmAttributeName)
         
         // Si no vienen valores para bindear un datavalue, el binder de element retorna null
         // Aqui se verifica si lo que se devuelve es null, y si es, no se considera para enviarselo al nodo padre
         if (attr) attrs << attr
      }
      return attrs
   }
   
   /**
    * @link http://www.openehr.org/wsvn/ref_impl_java/TRUNK/openehr-aom/src/main/java/org/openehr/am/archetype/constraintmodel/CMultipleAttribute.java
    * @param cattr
    * @param bind_data
    * @return
    */
   List bind_C_MULTIPLE_ATTRIBUTE(AttributeNode cattr, Map bind_data)
   {
      //log.info('bind_C_MULTIPLE_ATTRIBUTE()'+ cattr.rmAttributeName)
      
      String method
      def attrs = []
      def attr
      def adl_class
      cattr.children.each { cobject -> // cattr.members en ADL
         
         //println "  attr obj: "+ cobject.rm_type_name.text()
         
         // Si es una referencia a otro arquetipo, el tipo de ADL es complex object
         adl_class = cobject.type
         if (adl_class == "ARCHETYPE_SLOT") return // avoid SLOTS see https://github.com/ppazos/cabolabs-emrapp/issues/12
         if (adl_class == "C_ARCHETYPE_ROOT")
         {
            adl_class = "C_COMPLEX_OBJECT"
            this.archetypeId = cobject.archetypeId // if object is root it has a different archetypeId
         }
         
         method = 'bind_'+ adl_class +'_'+ cobject.rmTypeName
         attr = this."$method"(cobject, bind_data, cattr.rmAttributeName)
         
         // Si no vienen valores para bindear un datavalue, el binder de element retorna null
         // Aqui se verifica si lo que se devuelve es null, y si es, no se considera para enviarselo al nodo padre
         if (attr) attrs << attr
      }
      return attrs
   }
}