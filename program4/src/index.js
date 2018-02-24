var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var http = require('http');

var api = require('./api');
var app = express();
var PORT = process.env.PORT || 3000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);
app.use(express.static(path.join(__dirname + '/public')));

app.get('/', (req, res) => {
	res.sendFile(path.join(__dirname + '/index.html'));
});

var server = http.createServer(app);
server.listen(PORT, () => {
	console.log(`App listening on port ${PORT}`);
});
