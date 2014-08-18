package registros

import registros.valores.DataValue

class Item {

   // TODO
   //String name // Locatable.name
   String type // rm_type_name
   String attr // rm_attr_name
   
   String archetypeId
   String path
   String nodeId // atNNNN
   
   // debug
   String aomType
   
   // atributos simples segun el tipo de dato
   // ej. si type=HISTORY, atttributes=[origin, period, duration]
   // ej. si type=EVENT, attributes=[time]
   Map attributes
   static hasMany = [attributes: DataValue]
   
   static belongsTo = [Document, Structure]
   
   static constraints = {
      attr(nullable:true)
   }
   
   /* no me esta salvando los DvDateTime en attributes
   static mapping = {
      attributes cascade: 'save-update'
   }
   */
   
   /*
   def beforeInsert()
   {
      // agrego porque no esta salvando los attributes
      // FIX: era por un error de validacion de DvDateTime>
      //   Field error in object 'registros.valores.DvDateTime' on field 'aomType': rejected value [null];
      
      attributes.each { attr, dv ->
         
         if(!dv.save())
         {
            println dv.errors
         }
      }
   }
   */
}