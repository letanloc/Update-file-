var  mysql  =require('mysql');
// connection value
var connection = mysql.createConnection({
		host : 'localhost',
		user : 'root',
		password : 'loc',
		database :  "GaleryApi",
}); 
// conver password  to md5
function Hashpassword(req){
  var crypto = require('crypto');
  var name = 'braitsch';
  var hash = crypto.createHash('md5').update(name).digest('hex');
  return  hash;
};
// user login 
    // connection.end();
      module.exports.login =  function(req,callback){
          connection.connect();
          var query = connection.query("select * from   User  where  email like '%"
          	+req.email+"%'"+" and  pasword like	 " + "'%"
          	+req.pasword+"%'",
           function(err, raws,result) {
                if(err) callback(err,null);
                else   callback(null,raws);
              });
           // connection.end();
      };
// };
// user register
 module.exports.register =function(req,callback){
 		connection.connect();
 		var Uservalue  = {
 					email	: req.email,
 					pasword	:  Hashpassword(req.password),
 					name	 : req.name	
 		};	
 			connection.query("insert into User set ? ",Uservalue,function(error,values,fields){		
 							if(error)callback(error,null);
 							else  callback(null,"success");
 			});
// select all value  data 
      module.exports.getlistUser= function(req,callback){
          connection.connect();
          connect.query("select * from  User ",function(error,values,fields){
              if(error){  
                  callback(error,null);
              }else{  
                  callback(null,values);
              }
          });
      };
 };