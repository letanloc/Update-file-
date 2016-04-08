var  mng =resquire('../manager/UserMng');
var api_key="92f1e69894ded8b877c2bd3e60ebc37b";
var app_id="3 94a45da5f5331fc39d528d0632df5d6";
function  JsonParse(c,msg,res){

	var JSN  = {
		code : c,
		message : msg,
		data : res
	};
	return  JSN;
}
module.exports =function(app){
app.post("/hello",function(req,res){
				 if( req.body.api_key == api_key && req.body.app_id == app_id){
				res.json(JsonParse(0,null,"xin chao tan map"),200);
			}else{
				res.json(JsonParse(1,"Api key",null),200);
		}
	});
  app.post("/login",function(req,res){
  		mng.login(req.body,function(error,doc){
  				console.log(req.body);
  			if(error)res.json(JsonParse(1,error,null),200);
  			else{
  				if(typeof req.body.email == "undefined"||req.pasword == "undefined"){
  							res.json(1,"du lieu khong hcinh xac",200);
  				}else{
  						res.json(JsonParse(0,null,doc),200);
  				}
  			}
  		});
  });

 app.post("/register",function(req,res){
    mng.register(req.body,function(error,doc){
            if(error)res.json(JsonParse(1,error,null),200);
            else res.json(JsonParse(0,null,doc),200);
    });
});
 /*----*/
 app.get("/listuser",function(req,res){
    mng.getlistUser(req,function(error,doc){
       if(error)res.json(JsonParse(1,error,null),200);
        else res.json(JsonParse(0,null,doc),200);
    });
 });
};
