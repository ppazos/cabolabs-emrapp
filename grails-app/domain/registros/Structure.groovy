package registros

class Structure extends Item {

   static hasMany = [items: Item]
   
   static belongsTo = [Document]
   
   static mapping = {
      items cascade: 'all' //'save-update'
   }
}