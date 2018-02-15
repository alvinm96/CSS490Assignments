var express = require('express');
var app = express();

var PORT = process.env.PORT || 3000;

app.get('/', (req, res) => {
	res.send('Hi');
});

app.listen(3000, () => {
	console.log(`App listening on port ${PORT}`);
});
