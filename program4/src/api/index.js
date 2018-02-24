var express = require('express');
var router = express.Router();
var aws = require('./aws.js');

router.post('/load-data', aws.loadData);
router.get('/user', aws.queryTable);

module.exports = router;