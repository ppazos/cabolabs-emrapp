grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org" // prueba para ver si encara htt builder...
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
       // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

       // runtime 'mysql:mysql-connector-java:5.1.20'
       
       // ===== PARA QUE AGARRE HttpBuilder ===== //
       //
       runtime('org.apache.httpcomponents:httpclient:4.0.3') { // org/apache/http/client/ClientProtocolException
       }
      
       // ========================  net.sf.json.JSON  ===========================
       // NO RESUELVE ESTA DEPENDENCIA, PONGO EL JAR EN LIB DERECHO... (funka!) <<<<<<<
       //
       //  (pongo: commons-collections, commons-beanutils, ezmorph, commons-logging, json-lib, commons-lang)
       //
       // =======================================================================
       //
       // VER: http://bartling.blogspot.com/2011/03/grails-jar-dependencies-with.html
       // BUG GRAILS: http://jira.grails.org/browse/GRAILS-6147
       // Me da:
       //  - Specified dependency definition runtime(net.sf.json-lib:json-lib:2.3:jdk15) is invalid! Skipping..
       //runtime('net.sf.json-lib:json-lib:2.3:jdk15') { // net.sf.json.JSON
       //  Me da:
       //  - Failed to resolve dependencies (Set log level to 'warn' in BuildConfig.groovy for more information)
       /*
        runtime('net.sf.json-lib:json-lib:2.3') { // net.sf.json.JSON
        //este depende de
        //  jakarta commons-lang 2.5
         //jakarta commons-beanutils 1.8.0
         //jakarta commons-collections 3.2.1
         //jakarta commons-logging 1.1.1
         //ezmorph 1.0.6
        excludes "jruby", "xmlunit", "xom" // se excluyen aca https://github.com/jgritman/httpbuilder/blob/master/pom.xml
        }
       */
      
       runtime('net.sourceforge.nekohtml:nekohtml:1.9.9') {
       }
       runtime('xml-resolver:xml-resolver:1.2') { // org.apache.xml.resolver.CatalogManager
       }
       runtime('xerces:xercesImpl:2.6.2') {
       }
       runtime('org.codehaus.groovy.modules.http-builder:http-builder:0.5.2') {
         excludes "commons-logging", "groovy" // "xml-apis", 
       }
       //
       // ===== PARA QUE AGARRE HttpBuilder ===== //
       
       runtime('com.thoughtworks.xstream:xstream:1.4.3') {
       }
       

       test "org.grails:grails-datastore-test-support:1.0-grails-2.4"
    }

    plugins {
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        
        build ":tomcat:7.0.55.2"

        // plugins for the compile step
        compile ':scaffolding:2.1.2'
        compile ':cache:1.1.8'
        compile ':asset-pipeline:2.1.5'

        // plugins needed at runtime but not for compilation
        runtime ':hibernate4:4.3.8.1' // or ':hibernate:3.6.10.14'
        runtime ':database-migration:1.4.0'
        runtime ':jquery:1.11.1'
        
        compile ':quartz:1.0.2'
    }
}
