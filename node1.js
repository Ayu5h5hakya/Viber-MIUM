var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
app.get('/',function(request,response){console.log('A GET request was recieved')});
io.on('connection',function(socket){
  console.log('one user connected'+socket.id);

  socket.on('message',function(data){
    console.log(data);
    var sockets = io.sockets.sockets;
    sockets.forEach(function(sock){
      if(sock.id!=socket.id){
        sock.emit("message",{message:data});
      }
    });
  });

  socket.on('disconnect',function(){
    console.log('One user disconnected');
  });

});
http.listen(8888,function(){console.log('server listening at port 8888')});
