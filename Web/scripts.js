
  // Your web app's Firebase configuration
  var firebaseConfig = {
  	apiKey: "",
  	authDomain: "touchinglives-18148.firebaseapp.com",
  	databaseURL: "https://touchinglives-18148.firebaseio.com",
  	projectId: "touchinglives-18148",
  	storageBucket: "",
  	messagingSenderId: "1083204876142",
  	appId: "1:1083204876142:web:a4bc5ea5434f35de"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  // var user= firebase.auth().currentUser;
  // if(user){

  // }else{

  // }
  // Get a reference to the database service
  var database = firebase.database();
  var att={};
  var len;
  var savings={};
  var refer= firebase.database().ref("studentInfo");
  refer.on("value", function(snapshot){
  	snapshot.forEach(function(child){

  		att[child.key]=0;
  		savings[child.key]=0;
  	});

  });
  // var starCountRef = firebase.database().ref('posts/' + postId + '/starCount');
  // starCountRef.on('value', function(snapshot) {
  // updateStarCount(postElement, snapshot.val());

  var ref= firebase.database().ref("attendance");
  ref.on("value", function(snapshot){
  // 	const snap = Object.getOwnPropertyNames(snapshot);

  // 	len = snap.length;
  // console.log(len);
  len=0;
  snapshot.forEach(function(child){
  		//console.log(child.val());
  		var monthchk=child.key.split("-")[1];
  		var month="07";
  		// console.log(child.val());
  		if (month==monthchk){
  			len=len+1;
  			child.forEach(function(ch){
  				ch.forEach(function(c){
  					// console.log(c);
  					c.forEach(function(sid){
  						// console.log(sid.val());
  						if(Number(sid.val())>=0){

  							att[sid.key]=Number(att[sid.key])+1;
  							savings[sid.key]=Number(savings[sid.key])+Number(sid.val());
  						}
  						
  					});
  				});
  			});
  		}
  		//console.log(child.key+": "+child.val());
  	});
  console.log(len);
  for(var k in att){
    if (att.hasOwnProperty(k)){
     att[k]=(att[k]/len)*100;

   }
 }
 var labels = Object.keys(att);
 var data = Object.values(att);
 console.log(data);
 drawchart(labels,data);
  	// $('#btn').click(att,function(){
  	// 	var data=att;
  	// 	var csv = Papa.unparse(data);
  	// 	var data, filename, link;

  	// 	if (csv == null) return;

  	// 	filename =  'export.csv';

  	// 	if (!csv.match(/^data:text\/csv/i)) {
  	// 		csv = 'data:text/csv;charset=utf-8,' + csv;
  	// 	}
  	// 	data = encodeURI(csv);

  	// 	link = document.createElement('a');
  	// 	link.setAttribute('href', data);
  	// 	link.setAttribute('download', filename);
  	// 	link.click();

  	// });
  	// console.log(att);




  });
  
  
  // $.each(att, function(k, v){
  // 	v=v/len*100
  // 	console.log
  // });
  

  function drawchart(labels,data){
  	var ctx = document.getElementById("myChart");



  	var background=[]
  	var borderColor=[]
  	for (i=0;i<data.length;i++){
  		if(data[i]<50)
  			{color='rgba(255, 0, 0, 0.2)';}
  		else if(data[i]>50 && data[i]<75)
  			{color='rgba(255,165,0, 0.4)';}
  		else
  			{color='rgba(0, 255, 0, 0.2)';}
  		background.push(color);
  		borderColor.push(color);
  	}

  	var myChart = new Chart(ctx, { 
  		type: 'bar', 
  		data: { 
  			labels: labels, 
  			datasets: [ 
  			{ label: 'attendance %', 
  			data: data, 

  			backgroundColor : background
		//[getColor(50),getColor(77),getColor(90),getColor(20),getColor(70)
		,

		borderColor: borderColor,
		borderWidth : 1 
	} 
	] 
}, 
options: { 
	scales: { 
		yAxes: [{ 
			ticks: { 
				beginAtZero:true 
			} ,
			scaleLabel:{
				display:true,
				labelString:"Percentage"
			}
		}] ,
		xAxes: [{

			barThickness: 100,
			scaleLabel:{
				display:true,
				labelString:"student Id"
			},
		}]
	}
}
});
  }

  
  function getColor(data){

  	if(data>50)
  		{color='rgba(0, 255, 0, 0.2)';}
  	else
  		{color='rgba(255, 0, 0, 0.2)';}

  	return color;
  }

