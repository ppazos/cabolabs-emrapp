import auth.User

class BootStrap {

    def init = { servletContext ->
       
       // OPT support
       def optm = opt_repository.OperationalTemplateManager.getInstance()
       optm.loadAll("opts")
       
       def userData = [
          [name:'Dr. Pablo Pazos', user:'a', pass:'a'],
          [name:'Dra. Barbara Cardozo', user:'b', pass:'b'],
          [name:'Dr. Troy Darose', user:'c', pass:'c'],
          [name:'Dra. Luciana Pazos', user:'d', pass:'d'],
          [name:'Dr. Ricardo Cardozo', user:'e', pass:'e'],
       ]
       
       userData.each { udt ->
          
          def u = new User(udt)
          if (!u.save())
          {
             println u.errors
          }
       }
    }
    def destroy = {
    }
}
