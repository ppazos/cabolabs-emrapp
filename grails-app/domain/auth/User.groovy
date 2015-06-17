package auth

class User {

   String name
   
   String user
   String pass
   
   String uid = java.util.UUID.randomUUID() as String
}