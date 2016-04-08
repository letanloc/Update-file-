var connection =require("../connection/mongoconnection");
var images  =connection.images;
 module.exports.Insert=function(id,doc,callback){	
							images.insert({"_id" : id,
							"images" : doc
							}
								,function(err,val){
								if(err){
									console.log(" insert loi"+err);
									callback(err,null);
								}else{
									callback(null,val);
									console.log(" insert thanh cong"+val);
								}
							});
  					};		