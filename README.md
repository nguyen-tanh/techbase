### Environment
* Java 8+
* Docker

### Getting started

* Start docker:
    ```
    $ docker-compose up
    ```

* Start App:
    ```
    $ ./gradlew bootJar
    $ java -jar build/libs/techbase-0.0.1-SNAPSHOT.jar
    ```

* Call API:


    * Get token: [POST] http://localhost:8080/api/auth
        
        Header: 
        ```
        Content-Type: application/json
        ```
        
        Body:
        ```json
          {
              "userName": "admin",
              "password": "admin"
          }
        ```
        Response:
        ```json
          {
          "statusMessage": "000",
          "data":{
              "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciO....",
              "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOi..."
            }
          }
        ```
    * Get Employee:  [GET] http://localhost:8080/api/user/employee
    
        Header (get from /api/auth): 
        ```
        Authorization: Bearer eyJ0e.....
        ```
        
        Response:
        ```json
          {
          "statusMessage": "000",
          "data": {
            "content": [
              {
                "name": "xyz",
                "role": "DIRECTOR",
                "teams": "",
                "departments": ""
              },{
              
              }
              
            ],
            "totalElements": 5,
            "page": 1,
            "size": 1500
          }
        }
      ```
        
