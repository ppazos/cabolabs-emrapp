package demographic

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2')
import groovyx.net.http.*
import sesion.ClinicalSession
import grails.util.Holders
import ehr.EhrService

class PatientController {

   def config = Holders.config.app
   
   EhrService ehrService
   
   def index()
   {
      render "index!"
   }
   
   // TODO:
   //  1. llamar a esta por ajax desde la view
   //  2. el render se hace en el controller contra un template
   //  3. el response ajax es el html ya concinado (no hay procesamiento de la info en la vista = mas seguro xq el usuario no conoce el protocolo interno)
   //  4. hay que soportar paginacion
   //  5. hacer pruebas de que es mas rapido> pedir y procesar xml o json
   def list()
   {
      println "patient list"
      
      // Ir al listado de pacientes significa salir de la sesion clinica (un paciente seleccionado)
      session.clinicalSession = null
   }
   
   // se llama por ajax desde list view
   def patientList()
   {
      // TIMER
      def start = System.currentTimeMillis()
      
      def patientList = ehrService.getPatients(session.token)
      
      // FIXME: when we add support for pagination, the last page might result in an empty list and that's a valid response
      if (patientList.size() == 0) flash.message = g.message(code:'patient.patientList.error.serverError')
      
      // TIMER
      def now = System.currentTimeMillis()
      println "listPatients - execution time: " + (now - start) + " ms"

      render (template:'list_table', model:[patientList: patientList])
      
   } // patientList
}