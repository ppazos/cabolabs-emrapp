package registros

class Document {

   Date start = new Date()
   Date end
   
   String category = "event"
   
   String authorId
   String templateId
   String compositionArchetypeId
   
   // When updating an existing document on the EHRServer,
   // this field holds the UID of the version checked out
   // from the server to be modified / updated / fixed.
   // When creating a new doc this is null.
   String versionUid
   
   // Datos tal cual fueron ingresados por el usuario en la ui
   // Map<String, String>
   Map bindData
   
   List content
   
   static hasMany = [content: Item]
   
   static constraints = {
      authorId (nullable: true)
      compositionArchetypeId (nullable: true)
      templateId(nullable: false)
      end (nullable: true)
      versionUid(nullable: true)
   }
   
   static mapping = {
      content cascade: 'save-update'
   }
   
   def beforeInsert() {
      
      // Retorna una nueva coleccion con los valores cambiados con toString
      bindData = bindData.collectEntries { key, value ->
         [key, value.toString()]
      }
   }
   
   
   static transients = ['changeType']
   
   /**
    * Returns the change type to be used for the commit audit.change_type
    * when creating the XML serialization for committal.
    * @return
    */
   def getChangeType()
   {
      if (versionUid) return "modification"
      
      return "creation"
   }
}
