This service provide takeout coffee

Access link:
    http://localhost:8085/swagger-ui/index.html

Docker commands:
    mvn clean package
    docker build -t coffeePlace:latest .
    docker run -p8085:8085 coffeePlace:latest