var aws = require('aws-sdk');
var axios = require('axios');
var fs = require('fs');

aws.config.update({region: 'us-east-1'});

var s3 = new aws.S3();
var dynamodb = new aws.DynamoDB();
var tableName = 'program4';
var bucketName = 'css490-program4';

module.exports = {
    loadData: function (req, res, next) {
        // copy the file
        var params = {
            Bucket: bucketName,
            CopySource: 'https://s3-us-west-2.amazonaws.com/css490/input.txt',
            Key: 'temp-file.txt'
        };

        s3.copyObject(params).promise()
        .then((copyData) => {
            console.log('copied object');

            delete params.CopySource; // delete CopySource attribute for retrieving object

            s3.getObject(params).promise()
            .then((objectData) => {
                console.log('retrieved object');

                var repsonseData = objectData.Body.toString('utf8');
                
                var lines = repsonseData.split(/\n/);
                
                var batchParams = {
                    RequestItems: { }
                };

                batchParams.RequestItems[tableName] = [];

                for (var line = 0; line < lines.length; line++) {
                    if (lines[line].length > 0) {
                        var tokens = lines[line].split(' ');

                        // assuming first and second tokens are last and first names
                        var itemVars = {
                            'lastName': {
                                S: tokens[0].toLowerCase()
                            },
                            'firstName': {
                                S: tokens[1].toLowerCase()
                            }
                        };

                        for (var i = 2; i < tokens.length; i++) {
                            var attr = tokens[i].split('=');

                            itemVars[attr[0]] = {
                                S: attr[1].replace('\r', '')
                            };
                        }
                    
                        var itemRequest = {
                            PutRequest: {
                                Item: itemVars
                            }
                        };
                        batchParams.RequestItems[tableName].push(itemRequest);
                    }
                }

                //insert items into dynamodb
                dynamodb.batchWriteItem(batchParams).promise()
                .then((queryData) => {
                    console.log('inserted items');
                    res.send('done');
                    return next();
                })
                .catch((err) => {
                    console.log(err);
                });

                // delete object
                s3.deleteObject(params).promise()
                .then((data) => {
                    console.log('deleted object');
                })
                .catch((err) => {
                    console.log('error in deleting object');
                });
            })
            .catch((err) => {
                console.log(err);
                console.log('error in retrieving object');
            });
        })
        .catch((err) => {
            console.log('error in copying object');
        });
    },
    queryTable: function(req, res) {
        var attributeVal = {
            ':lN': {
                S: req.query.lastName.toLowerCase()
            }
        };

        if (req.query.firstName) {
            attributeVal[':fN'] = {
                S: req.query.firstName.toLowerCase()
            }
        }

        var queryParams;

        if ((!req.query.lastName || req.query.lastName.length === 0) && req.query.firstName) { // only first name was given
            queryParams = {
                TableName: tableName,
                FilterExpression: 'firstName = :fN',
                ExpressionAttributeValues: {
                    ':fN': {
                        S: req.query.firstName
                    }
                }
            }
            
            dynamodb.scan(queryParams).promise()
            .then((data) => {
                return res.send(data);
            })
            .catch((err) => {
                console.log(err);
                return res.send('error');
            });
        } else {
            queryParams = {
                ExpressionAttributeValues: attributeVal,
                KeyConditionExpression: (req.query.firstName ? 'lastName = :lN and firstName = :fN' : 'lastName = :lN'),
                TableName: tableName
            };

            dynamodb.query(queryParams).promise()
            .then((data) => {
                return res.send(data);
            })
            .catch((err) => {
                return res.send('error');
            });
        }
    }
};