# Border Checker Service

Border checker is a simple Spring Boot service to find the shortest route between countries.

The services uses json data file to load the countries and its borders.
It also exposes an API endpoint where user can specify the origin and destination as a country codes. 
As a response the endpoint returns a route being an array of country codes which need to be crossed.


# Build

In order to build the service you need to download the source code and save it in the folder of your choice. Then go to that folder and run the following command to build the jar file.

    mvn clean install

As a prerequisite you need to have `Maven` and `Java 17` already installed.

# Run

Once the service is build you can run it by executing the command below.
    
    java -jar target/border-checker-0.0.1-SNAPSHOT.jar

By default, the service uses internal json data file to load the countries. You can also load the data from a remote location.
Default remote location is
https://raw.githubusercontent.com/mledoze/countries/master/countries.json
In order to load data from remote location you need to specify the following parameter

    java -jar target/border-checker-0.0.1-SNAPSHOT.jar --data-loader.mode=remote

Remote data location is configurable. Please look at the configuration file `application.yaml` to see the details.

# Usage

Assuming the service is up and running, you can utilize the `/routing` endpoint to find out the route from origin to destination country.
The URL is in the following format:
`/routing/{origin_code}/{destination_code}`

For example, in order to find out which countries you need to cross going from Czechia to Italy use the following URL

    $ curl http://localhost:8088/routing/CZE/ITA

The service should return the following json response:

    {"route":["CZE","AUT","ITA"]}

Please notice that the default service port is `8088`. This value is also configurable in the `application.yaml` file or by specifying the corresponding runtime parameter.