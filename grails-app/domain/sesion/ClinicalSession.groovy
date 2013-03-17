package sesion

import auth.User
import registros.Document

class ClinicalSession {

   String patientUid
   Date dateCreated
   boolean open = true // se cierra cuando se firma
   boolean committed = false // true cuando se commitea al server
   
   Map datosPaciente = [:] // Map<String,String> cachea los datos para no tener que pedirlos de nuevo
   
   User composer // se setea cuando se firma el registro
   
   static hasMany = [documents: Document]
   
   static constraints = {
      patientUid(nullable:false)
      composer(nullable:true)
   }
   
   static transients = ['documentForArchetype']
   
   def getDocumentForArchetype(String archetypeId)
   {
      return this.documents.find{ it.compositionArchetypeId == archetypeId }
   }
   
   /*
   static namedQueries = {
      
   }
   */
}