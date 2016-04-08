var mongo =require("../manager/Gallery");

// var 
		var url ="http://api.tumblr.com/v2/blog/xkcn.info/posts?api_key"
			   +"=Cky5y4wmgeKzzDXQ7e0PR1Uk7cPYAGPbO1HPXEAmuDVAQ1CNFA&limit=20&offset=";
	  var number =0;
		
	    function getPost(res){
			var  value = (JSON.parse(res["body"]))["response"]["posts"];
  			return value;
	    }

	     function insertvalue(res){
	     }
	     // parser valuejson
		 function Result(id,photos){
		 		var parse = {
	 				id: id,
		 			document : photos[0]["alt_sizes"]
		 		}
		 		return  parse;
		 }
		   function Content(id,conent){
		   }
		   //20
		  
module.exports =function(request,app){

		app.get("/image",function(req,res){	
			 data(res,0);	
		});

	var  demo =  [
{
ma_dia_Diem: '284TSXHNTLP12QBTHCM',
ten_dia_diem: 'Tiệm sửa xe Hồng',
so_nha: '284',
duong: 'Nơ Trang Long',
phuong: 'Phường 12',
quan: 'Quận Bình Thạnh',
khu_vuc: 'TP.Hồ Chí Minh',
so_dien_thoai_dd: '01265757909',
kinh_do: '10.813471',
vi_do: '10.813471',
ma_loai_hinh: 'RX1'
}	
];
		app.get("/location",function(req,res){
			res.json(demo,200);
		});
function data(res,number){	
	request(url+number,function(err,link){	
			var arr = getPost(link);
			var posts  = (JSON.parse(link["body"]))["response"]["blog"]["posts"];			
				for(var i = 0 ; i< arr.length; i++){
						if( typeof  arr[i]["photos"] != 'undefined'){
							mongo.Insert(arr[i]["id"], 
								arr[i]["photos"][0]["alt_sizes"],function(err,doc){
									if(err)res.json(err,null);			
							});
						}else{	
							console.log("vt " + i + " undefined");
						}
				}
					if(number < posts){
						number = number + 20;				
						console.log(number);
						data(res,number);			
					}else{
						res.json("thuc hien thanh cong",200);
				}			
		});
 } 


  app.post('/update/')
};