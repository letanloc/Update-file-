var express  =require('express');
var conslidate =require('consolidate') ;
var http =require('http');
var BaseSixFour = require('node-base64-image');
// create muler
// var multer	  = require('multer');
// var upload = multer({ dest: 'uploads/' })
var  request  =require('request');


var app  = express();
  app.configure(function(){
    app.set('port', (process.env.PORT || 5010));
    app.use(express.bodyParser());
  });
var server = http.createServer(app);
process.on('uncaughtException',function(err){
  	  console.log('Caught exception: ' + err);
});
// require('./Routers/userouter')(app);	
require('./Routers/galleryrouter')(request,app);
require('./Routers/Update')(app,BaseSixFour);
server.listen(app.get('port'),function(){
	console.log('thuc hien lien ket');
});
