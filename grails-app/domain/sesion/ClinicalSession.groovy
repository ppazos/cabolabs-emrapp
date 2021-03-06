package sesion

import auth.User
import registros.Document
import demographic.Patient

class ClinicalSession {

   Date dateCreated
   
   /* fecha en la que se firma el registro y se cierra, es el commit logico al sistema,
    * no el momento que se envia al server (ese es un log del server).
    * Este campo se mapea a auditTimeCommitted en CommitJob.
    */
   Date dateClosed
   
   boolean open = true // se cierra cuando se firma
   boolean committed = false // true cuando se commitea al server
   
   Patient patient
   
   //User composer // se setea cuando se firma el registro
   String composer
   
   String authToken
   
   static hasMany = [documents: Document]
   
   static constraints = {
      //patientUid(nullable:false)
      composer(nullable:true)
      dateClosed(nullable:true)
   }
   
   def getDocumentForTemplate(String templateId)
   {
      return this.documents.find{ it.templateId == templateId }
   }
   
   /*
   static namedQueries = {
      
   }
   */
}