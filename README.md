# Coordinatix

### About
Application acts as an external coordinator / dispatcher for shared resources in a distributed environment.
It can externally throttle the requests to the resource (i.e. database, our service, etc).
Applications competing for resources should ask for permission and once it is granted,
they can start utilizing the resource.
When they finish processing, they release the permission so that it can be reused.
The other way to release the permit is to set the timeout for a permit and once a permit expires,
it is automatically released.
The configuration can be set in `application.properties` config file.

### Dependencies
Application uses Redis as an external environment to synchronize on.
This allows us to scale the application.

If you do not have Redis you can install it using docker:
`docker pull redis`

### HowTo
Here are the instructions on how you can use it

* To ask for a permission to the resource with id 'myresourceid': curl -X GET "http://localhost:8080/permission/myresourceid"

The following response should be returned of the permission is granted:
{"permitId":"ea10fa72056382664bca5fe45f03823d","resourceId":"myresourceid","maxAllowedPermits":2,"currentPermits":0,"expirationMs":5000,"granted":true}

Now we can start using the resource.

* To release the permission for the resource id 'myresourceid' and permitId obtained previously: curl -X POST "http://localhost:8080/release/myresourceid/ea10fa72056382664bca5fe45f03823d"

* In case when the permission cannot be granted you will see the response of this kind
`{"permitId":null,"resourceId":"myresourceid","maxAllowedPermits":2,"currentPermits":0,"expirationMs":5000,"granted":false}`