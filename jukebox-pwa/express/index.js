
const path = require('path');
var http = require('http'),
    https= require('https'),
    httpProxy = require('http-proxy'),
    express = require('express');

// var request = require('request').defaults({
//       strictSSL: false,
//       rejectUnauthorized: false
//    });

 process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

// create a server
var app = express();
var proxy = httpProxy.createProxyServer({ target: 'http://jukebox-app:8080/', ws: true });
var server = require('http').createServer(app);


app.all('/api/*', function(req, res) {
  console.log("proxying GET request", req.url);
  proxy.web(req, res, {});
});

app.all('/media/*', function(req, res) {
  console.log("proxying GET request", req.url);
  proxy.web(req, res, {});

});

app.all('/library/media/*', function(req, res) {
  console.log("proxying GET request", req.url);
  proxy.web(req, res, {});
});

app.all('/management/*', function(req, res) {
  console.log("proxying GET request", req.url);
  proxy.web(req, res, {});
});


/* Point static path to client */
//app.use(express.static(path.join(__dirname, '')));
app.use(express.static(path.join(__dirname, '..', 'angular', 'build')));


/* Catch all other routes and return the index file */
// app.get('*', (req, res) => {
//   res.sendFile(path.join(__dirname, 'index.html'));
// });
app.get('/', function (req, res) {
  res.sendFile(path.join(__dirname, '..', 'angular', 'build', 'index.html'));
});

// Proxy websockets
server.on('upgrade', function (req, socket, head) {
  console.log("proxying upgrade request", req.url);
  proxy.ws(req, socket, head);
});

/**
 * Get port from environment and store in Express.
 */
const port = process.env.PORT || '3000';
app.set('port', port);

// serve static content
app.use('/', express.static(__dirname + "/public"));

server.listen(port, () => console.log(`API running on localhost:${port}`));
