package registros

class Document {

   Date start = new Date()
   Date end
   
   String category = "event"
   
   String authorId
   String compositionArchetypeId
   
   // Datos tal cual fueron ingresados por el usuario en la ui
   // Map<String, String>
   Map bindData
   
   List content
   
   static hasMany = [content: Item]
   
   static constraints = {
      authorId (nullable: true)
      compositionArchetypeId (nullable: true)
      end (nullable: true)
   }
   
   static mapping = {
      content cascade: 'save-update'
   }
   
   def beforeInsert() {
      
      // Retorna una nueva coleccion con los valores cambiados
      bindData = bindData.collectEntries { key, value ->
         [key, value.toString()]
      }
   }
}