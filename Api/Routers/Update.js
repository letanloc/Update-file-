var mongo =require("../manager/Gallery");
// add app and muler muler  user import images
var fs =require('fs');
// get size forder
var getSize = require('get-folder-size');

// var base64 = require('node-base64-image');
// var storage = multer.memoryStorage();
 function JsRES(code,msg,data){
 var   j  ={
 			code :code,
 			message : msg,
 			Data : data
 };

 return j;
 }

module.exports =function(app,BaseSixFour){
		// call  storage
		app.post('/update',function(req,res){
			if( typeof req.body.images  != 'undefined' ){
				var options = {filename:'./Images/'+req.body.titles};
			var imageData = new Buffer(req.body.images, 'base64');
		BaseSixFour.base64decoder(imageData, options, function (err, saved) {
    		if 	(err) { console.log(err); 
    } else {
    console.log(req.body.images);    	

    }
});  
	}else res.json(JsRES(1,"undefined",null));
	});

		app.get('/sizeforder',function(req,res){
		getSize('./Images', function(err, size) {
  		if (err) { throw err; }
  		// console.log(size + ' bytes');
  		res.json( (size / 1024 / 1024).toFixed(2) + ' Mb',200);
  		console.log((size / 1024 / 1024).toFixed(2) + ' Mb');
			});

		});
};