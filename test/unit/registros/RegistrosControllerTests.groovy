package registros

/*
import static org.junit.Assert.*
import grails.test.mixin.support.*
import org.junit.*
*/

import auth.User
import grails.test.mixin.*

// Binder
import registros.Document
import registros.Element
import registros.Structure
import registros.valores.DataValue
import registros.valores.DvBoolean
import registros.valores.DvCodedText
import registros.valores.DvDateTime
import registros.valores.DvQuantity
import registros.valores.DvText

// Test if there's any template loaded
import opt_repository.OperationalTemplateManager


// XML
import sesion.ClinicalSession
import xml.XmlSerializer

// controller session
import sesion.ClinicalSession

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
//@TestMixin(GrailsUnitTestMixin)
@TestFor(RegistrosController)
@Mock([Document, Element, Structure, DvBoolean, DvCodedText, DvDateTime, DvQuantity, DvText, ClinicalSession])
class RegistrosControllerTests {

    def cses
   
    void setUp() {
        // Setup logic here
       def optm = OperationalTemplateManager.getInstance()
       optm.loadAll("opts")
       
       cses = new ClinicalSession(
          patientUid: 'e6b2dc3a-6a4e-413f-82f4-70a3425ca2f1',
          dateClosed: new Date(),
          composer: new User(name:'Carlos'))

       /* cses.datosPaciente = params.datosPaciente // map
        * <input type="hidden" name="datosPaciente.uid" value="${params.datosPaciente.uid}" />
           <input type="hidden" name="datosPaciente.firstName" value="${params.datosPaciente.firstName}" />
           <input type="hidden" name="datosPaciente.lastName" value="${params.datosPaciente.lastName}" />
           <input type="hidden" name="datosPaciente.dob" value="${params.datosPaciente.dob}" />
           <input type="hidden" name="datosPaciente.sex" value="${params.datosPaciente.sex}" />
           <input type="hidden" name="datosPaciente.idCode" value="${params.datosPaciente.idCode}" />
           <input type="hidden" name="datosPaciente.idType" value="${params.datosPaciente.idType}" />
           
        */
       if (!cses.save(flush:true))
       {
          println cses.errors
       }
    }

    void tearDown() {
        // Tear down logic here
    }

    void testSomething() {
       
        //fail "Implement me"
       
       // Test to see if I have any templates loaded
       //println OperationalTemplateManager.getInstance().getCache().keySet()
       
       
       // RegistrosController.save
       params.templateId = 'Signos'
       params.presion_sistolica_mag = 12
       params.presion_sistolica_units = 'mm[Hg]'
       params.presion_diastolica_mag = 23
       params.presion_diastolica_units = 'mm[Hg]'
       params.temperatura_mag = 24
       params.temperatura_units = '°C'
       params.frecuencia_cardiaca_mag = 36
       params.frecuencia_cardiaca_units = '/min'
       params.frecuencia_respiratoria_mag = 457
       params.frecuencia_respiratoria_units = '/min'
       params.peso_mag = 67
       params.peso_units = 'kg'
       params.estatura_mag = 87
       params.estatura_units = 'cm'
       controller.session.clinicalSession = cses
       controller.save("Signos")
       
       
       if (Document.count() == 0) fail "No documents saved!"
       
       
       // Emulates the Commitjob but just to serialize
       def serializer
       def serializedDocs
       def csess = ClinicalSession.list()
       Random random = new Random()
       csess.each { cses ->
          
          // Serializa a XML los documentos que estan en la sesion clinica
          serializer = new XmlSerializer(cses)
          serializedDocs = serializer.serializeSessionDocs()
          
          // Arma parametros para el request HTTP
          serializedDocs.eachWithIndex { serDoc, i ->
          
             // logging
             def compoFile = new File('committed' + File.separator +'test_composition_'+  random.nextInt(10000) +'_'+ i +'.xml')
             compoFile << serDoc
             // /logging
          
             println serDoc
          }
       }
    }
}
