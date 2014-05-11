cabolabs-emrapp
===============

openEHR Electronic Medical Record Application for testing commits and queries to the EHRServer.

Workflow:

  * user/login
  * patient/list -> patient/list_table
  * registros/list(patientUid) -> registros/compositionList
    * registros/continueSession
      * registros/currentSession
    * registros/openSession
      * registros/currentSession

  * registros/currentSession
    * registros/create(archetypeId) // TODO: cambiar a templateId
    * registros/show(id)