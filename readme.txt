This is a takeout service which provide ordering at least one coffee with many kind of toppings

H2 access link:
    http://localhost:8085/coffeePlace

Access link:
    http://localhost:8085/swagger-ui/index.html

Docker commands:
    mvn clean package
    docker build -t coffeePlace:latest .
    docker run -p8085:8085 coffeePlace:latest

Test coverage:
    Using JaCoCo maven plugin.
    After building project coverage report will be available here:
        /target/site/jacoco/index.html