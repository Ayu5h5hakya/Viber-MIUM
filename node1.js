var mongoose = require('mongoose');
var io = require('socket.io').listen(8888);

//Database server to connect tos
mongoose.connect('mongodb://localhost/chatAppDatabase4');

//Connection checks
var db = mongoose.connection;
db.on('error',console.error);
// var userSchema =new mongoose.Schema(
// {
//   name:String
// });
// var User = mongoose.model('userModel',userSchema);

// var record1 = new User({roll:1});
// record1.save(function(err){});
// var record2 = new User({roll:2});
// record2.save(function(err){});
// var record3 = new User({roll:3});
// record3.save(function(err){});
// var record4 = new User({roll:4});
// record4.save(function(err){});
// var record5 = new User({roll:5});
// record5.save(function(err){});

// User.count({},function(err,count){
//   if(count!=0)
//   {
//     console.log(count);
//     User.find({},function(err,data){
//       console.log(data);
//     });
//   }
//   else
//     {
//       var record1 = new User({roll:1});
//       record1.save(function(err){});
//       var record2 = new User({roll:2});
//       record2.save(function(err){});
//       var record3 = new User({roll:3});
//       record3.save(function(err){});
//       var record4 = new User({roll:4});
//       record4.save(function(err){});
//       var record5 = new User({roll:5});
//       record5.save(function(err){});
//
//     }
// });
db.once('open',function()
{
  //Schema defination
  var userSchema =new mongoose.Schema(
  {
    name:String
  });
  var User = mongoose.model('userModel',userSchema);

  var connected_sockets = [];
  io.sockets.on('connection', function (socket)
  {
    console.log("Connected : " + socket.id);
    connected_sockets.push(socket.id);

    socket.on('message',function(data)
    {
        console.log(data);
        connected_sockets.forEach(function(socket_id)
        {
            if(socket.id !=socket_id)
            {
              io.sockets.socket(socket_id).emit('message',{message:data});
            }
        });
    });

    socket.on('user_info',function(data)
    {
      User.count({},function(err,count)
      {
        if(count!=0)
        {
            console.log('User info has arrived');
            User.find({},function(err,data){
              console.log(data);
            });
            User.findOne({name:data},function(err,document){
              if(!document)
              {
                    var record = new User({name:data});
                    record.save(function(err)
                    {
                      if(err) throw err;
                    });
              }
              else
              {
                console.log('A member has appeared');
              }
            });
        }
        else
        {
            var record = new User({name:data});
            record.save(function(err){
              if(err) throw err;
            });
        }
      });

  });

});
});
