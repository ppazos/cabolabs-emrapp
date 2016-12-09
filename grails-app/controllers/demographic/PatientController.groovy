package demographic

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2')
import groovyx.net.http.*
import sesion.ClinicalSession
import grails.util.Holders
import ehr.EhrService
import demographic.Patient

class PatientController {

   def config = Holders.config.app
   
   def ehrService
   
   def index()
   {
      // Ir al listado de pacientes significa salir de la sesion clinica (un paciente seleccionado)
      session.clinicalSession = null
      
      [patientList: Patient.list()]
   }
   
   def create()
   {
      [patientInstance: new Patient()]
   }
   
   def save(Patient patientInstance)
   {
      if (patientInstance.validate())
      {
         def ehr = ehrService.createEhr(session.token, patientInstance.uid)
         patientInstance.ehrUid = ehr.uid
      }
      
      if (!patientInstance.save(flush: true))
      {
         flash.message = "An error occurred saving the patient"
         render(view: "create", model: [patientInstance: patientInstance])
         return
      }
      
      redirect action: 'show', id: patientInstance.id
   }

   def show(Patient patientInstance)
   {
      [patientInstance: patientInstance]
   }
}
