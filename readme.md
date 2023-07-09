<h2>How to build?</h2>
1. Docker should be installed before build
2. mvn clean install
<p>or quick build: mvn clean install -DskipTests

<h2>How to run tests?</h2>
1. Docker should be installed before.
2. mvn test

<h2>How to run?</h2>
First build project then run 
"docker-compose up -d"

<h2> How to stop? </h2>
docker-compose down

<h2>Accessing app</h2>
Swagger url - localhost:8080/swagger-ui/
<p>
Login: user
Password: user

<h2>How to check logs in Docker </h2>
docker container logs tiara-challenge-backend-1

 

