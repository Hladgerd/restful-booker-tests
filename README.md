# Integration tests of Restful-Booker API Playground

![](https://img.shields.io/badge/Code-Java%2017-informational?style=flat&color=blueviolet)
![](https://img.shields.io/badge/Framework-JUnit%205-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-REST%20Assured-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-AssertJ-informational?style=flat&&color=blueviolet)

Links : [Restful-Booker](https://restful-booker.herokuapp.com/),  [Restful-Booker API Docs](https://restful-booker.herokuapp.com/apidoc/index.html)

*Restful-Booker* is a Web API playground with authentication, CRUD operations and several bugs.

## Installation
Clone the repository:  
`git clone git@github.com:Hladgerd/restful-booker-tests.git`


### Authentication
Authentication is needed for `PUT` and `DELETE` requests.  
You find credentials info [here](https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken).  
The credentials should be entered in authentication.properties file.


### Running the tests
Run tests:  
`mvn clean test`


## Issues found

`https://restful-booker.herokuapp.com/booking` - Get Booking Ids
* Filtering by checkout date returns incorrect result

`https://restful-booker.herokuapp.com/booking` - Create Booking  
`https://restful-booker.herokuapp.com/booking` - Update Booking
* Checkin and checkout dates can be in the past
* Checkin date can be before checkout date
* Fields format not validated
* Optional field not nulled by update

`https://restful-booker.herokuapp.com/booking/1` - Delete Booking
* Delete non-existing booking returns 405 HTTP code for not found message.