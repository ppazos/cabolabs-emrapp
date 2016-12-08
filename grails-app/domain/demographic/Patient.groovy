package demographic

class Patient {

   String firstname
   String lastname
   String uid = java.util.UUID.randomUUID() as String
   String ehrUid // reference to the EHRServer EHR
   String sex
   Date dob
   
   static constraints = {
      dob nullable: true
      ehrUid nullable: true
   }
}
