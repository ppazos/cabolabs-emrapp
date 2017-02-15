package ehr

import groovyx.net.http.*
import grails.util.Holders

class EhrService {

   // TODO: operation here and job to free the cache
   def cache = [:]

   def config = Holders.config
   def server_url = config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path
   
   def login(String username, String password, String orgnumber)
   {
      // service login
      // set token on session
      def ehr = new RESTClient(server_url)
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.post(
            path:'api/v1/login',
            requestContentType: ContentType.URLENC,
            body: [username: username, password: password, organization: orgnumber]
         )
         
         // token
         return res.responseData.token
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "except login:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   /**
    * helps to get the uid of the current org to create ehrs for that org.
    * @param username
    * @return
    */
   def profile(String token, String username)
   {
      def ehr = new RESTClient(server_url)
      ehr.headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
      ehr.headers.'Authorization' = 'Bearer '+ token
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.get(
            path: 'api/v1/users/'+username,
            query: [format: 'json']
         )
         
         println "profile res:"+ res.responseData.toString()
         //println res.responseData.getClass()
         
         // username, email, organizations
         return res.responseData
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "except profile:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   def createEhr(String token, String subjectUid)
   {
      // service login
      // set token on session
      def ehr = new RESTClient(server_url)
      ehr.headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
      ehr.headers.'Authorization' = 'Bearer '+ token
      try
      {
         // Sin URLENC da error null pointer exception sin mas datos... no se porque es. PREGUNTAR!
         def res = ehr.post(
            path:'api/v1/ehrs',
            requestContentType: ContentType.URLENC,
            body: [subjectUid: subjectUid]
         )
         
         // ehr
         return res.responseData
      }
      catch (Exception e)
      {
         // FIXME: log a disco
         println "post ehrs:" + e.message
         e.printStackTrace(System.out)
      }
   }
   
   def getCompositions(String token, String ehrUid)
   {
      def res = []
      try
      {
         def ehr = new RESTClient(server_url)
         
         // custom handlers to access the raw response from the reader
         ehr.handler.failure = { resp, reader ->
            println "failure handler"
            [response:resp, reader:reader]
         }
         ehr.handler.success = { resp, reader ->
            println "success handler"
            [response:resp, reader:reader]
         }
         
         def data = ehr.get( path: 'api/v1/compositions',
                        query: [ehrUid: ehrUid, format: 'json'],
                        headers: ['Authorization': 'Bearer '+ token] )
         
         res = data.reader.result
         
         // JSON
         //println res.reader.toString()
         /*
          * {
               "result": [{
                  "id": 1,
                  "archetypeId": "openEHR-EHR-COMPOSITION.signos.v1",
                  "category": "event",
                  "dataIndexed": true,
                  "ehrUid": "11111111-1111-1111-1111-111111111111",
                  "lastVersion": true,
                  "organizationUid": "d6b7bc9b-4909-4b80-ad8b-4f2c8ffa985c",
                  "startTime": "2016-05-24 01:00:13",
                  "subjectId": "11111111-1111-1111-1111-111111111111",
                  "templateId": "Signos",
                  "uid": "e20e3cc9-5def-47e6-a0d2-96cbf6675698"
               }],
               "pagination": {
                  "max": 30,
                  "offset": 0,
                  "nextOffset": 30,
                  "prevOffset": 0
               }
            }
          */
         
         // XML
         //println groovy.xml.XmlUtil.serialize(res.reader)
         /* for reader class NodeChild (format = xml)
          * <?xml version="1.0" encoding="UTF-8"?>
          * <map>
              <entry key="result">
                <compositionIndex id="1">
                  <archetypeId>openEHR-EHR-COMPOSITION.signos.v1</archetypeId>
                  <category>event</category>
                  <dataIndexed>true</dataIndexed>
                  <ehrUid>11111111-1111-1111-1111-111111111111</ehrUid>
                  <lastVersion>true</lastVersion>
                  <organizationUid>d6b7bc9b-4909-4b80-ad8b-4f2c8ffa985c</organizationUid>
                  <startTime>2016-05-24 01:00:13</startTime>
                  <subjectId>11111111-1111-1111-1111-111111111111</subjectId>
                  <templateId>Signos</templateId>
                  <uid>e20e3cc9-5def-47e6-a0d2-96cbf6675698</uid>
                </compositionIndex>
              </entry>
              <entry key="pagination">
                <entry key="max">30</entry>
                <entry key="offset">0</entry>
                <entry key="nextOffset">30</entry>
                <entry key="prevOffset">0</entry>
              </entry>
            </map>
          */
         
         //println res.response.data.name() // map
      }
      catch (Exception e)
      {
         // TODO: log a disco
         render "Ocurrio un error al obtener los registros del paciente "+ e.message
         return res
      }
      
      return res
   }
   
   
   
   def String getEhrIdByPatientId(String patientUid, String token)
   {
      def res
      def ehrUid
      
      // Pide datos al EHR Server
      def ehr = new RESTClient(config.server.protocol + config.server.ip +':'+ config.server.port + config.server.path)
      
      
      // Lookup de ehrId por subjectId
      // FIXME: esto se puede evitar si viene el dato con el paciente
      try
      {
         // Si ocurre un error (status >399), tira una exception porque el defaultFailureHandler asi lo hace.
         // Para obtener la respuesta del XML que devuelve el servidor, se accede al campo "response" en la exception.
         res = ehr.get( path: 'api/v1/ehrForSubject',
                        query: [subjectUid:patientUid, format:'json'],
                        headers: ['Authorization': 'Bearer '+ token] )
         
         // FIXME: el paciente puede existir y no tener EHR, verificar si devuelve el EHR u otro error, ej. paciente no existe...
         // WONTFIX: siempre tira una excepcion en cada caso de error porque el servidor tira error 500 not found en esos casos.
         ehrUid = res.data.uid
      }
      catch (org.apache.http.conn.HttpHostConnectException e) // no hay conectividad
      {
         log.error( e.message )
         return
      }
      catch (groovyx.net.http.HttpResponseException e)
      {
         // puedo acceder al response usando la excepci�n!
         // 500 class groovyx.net.http.HttpResponseDecorator
         println e.response.status.toString() +" "+ e.response.class.toString()
         
         // errorEHR no encontrado para el paciente $subjectId, se debe crear un EHR para el paciente
         println e.response.data
         
         // WARNING: es el XML parseado, no el texto en bruto!
         // class groovy.util.slurpersupport.NodeChild
         println e.response.data.getClass()
         
         // Procesando el XML
         println e.response.data.code.text() // error
         println e.response.data.message.text() // el texto
         
         // text/xml
         println e.response.contentType
         
         // TODO: log a disco
         // no debe serguir si falla el lookup
         //render "Ocurrio un error al obtener el ehr del paciente "+ e.message
         log.error( e.response.data.message.text() )
         return
      }
      
      return ehrUid
      
   } // getEhrIdByPatientId
}
