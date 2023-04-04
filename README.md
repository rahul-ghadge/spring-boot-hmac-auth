
# Spring boot Hmac Authentication

Hmac (Hash-based Message Authentication Code) authentication is the technique used to simultaneously verify both the data integrity and authenticity of a message.
It consists of the **Secret Key** and the **Hash Function** which guarantees the integrity of the message between two parties.

HMAC uses cryptographic hash functions such as **MD5** and **SHA-***_(MD5, SHA-1, SHA-224, SHA-256, SHA-384, and SHA-512_)

Java provides built-in support for Mac class. We need to add only dependencies for parsing data and ease of code.


## Prerequisites
- Java
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/guides/index.html)
- Basic Cryptographic knowledge
- [Lombok](https://objectcomputing.com/resources/publications/sett/january-2010-reducing-boilerplate-code-with-project-lombok)


## Tools
- Eclipse or IntelliJ IDEA (or any preferred IDE) with embedded Maven
- Maven (version >= 3.6.0)
- Postman (or any RESTful API testing tool)


<br/>


###  Build and Run application
_GOTO >_ **~/absolute-path-to-directory/spring-boot-hmac-auth**  
and try below command in terminal
> **```mvn spring-boot:run```** it will run application as spring boot application

or
> **```mvn clean install```** it will build application and create **jar** file under target directory

Run jar file from below path with given command
> **```java -jar ~/path-to-spring-boot-hmac-auth/target/spring-boot-hmac-auth-0.0.1-SNAPSHOT.jar```**

Or
> run **main method** from `SpringBootHmacAuthApplication.java` as spring boot application.





### Code Snippets
1. #### Maven Dependencies
   Need to add below dependencies to enable web support related config in **pom.xml**. Lombok's dependency is to get rid of boiler-plate code.
    ```
    <dependency>
       <goupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <optional>true</optional>
    </dependency>

    <dependency>
       <groupId>javax.servlet</groupId>
       <artifactId>javax.servlet-api</artifactId>
       <version>4.0.1</version>
    </dependency>
    ```



2. #### Properties file
   Reading H2 DB related properties from **application.properties** file and configuring JPA connection factory for H2 database.

   **src/main/resources/application.properties**
   ```
   hmac-auth:
     header:
       nonce: nonce
       access-key: accesskey
       authorization: authorization
   
     access-key: 8c2ea66e-abfc-4394-8adb-fa52890bdce3
     secret-key: 982064c6-c265-11ed-afa1-0242ac120002
     expires: 60    # seconds
   ```


3. #### Model class
   Below model class which we will use to perform CRUD operations if authentication is successful.  
   **SuperHero.java**
    ```
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class SuperHero implements Serializable {
    
        @Id
        @GeneratedValue
        private int id;
    
        private String name;
        private String superName;
        private String profession;
        private int age;
        private boolean canFly;
    
        // Constructor, Getter and Setter
    }
    ```


4. #### Hmac Authentication
   All classes from config package are used to authenticate users with Hmac.
   - _config_
     - _filter_
       - **HmacInterceptor** - Which will intercept each request and validate the auth token is valid and not expired
     - _properties_
       - **HmacProperties** - Read properties from application.properties and configure Hmac objects.
     - _verifier_
       - **CredentialsVerifier** - Will verify the access kay and nonce (token is expired or not).
       - **HmacAuthVerifier** - This class is the core of Hmac authentication. It will validate all the things regarding request metadata 
                and body under **verify method**. Will verify the cryptographic _algorithm, headers and token_ as well for coming requests.
     - _web_
       - **HmacVerifyConfig** - Here we'll register HmacInterceptor interceptor in the spring interceptor registry.
   - **CredentialsProvider** - Plain interface with some abstract methods for credentials provider.
   - **HmacAuthBeanConfig** - Hmac auth related beans like **HmacInterceptor** and **CredentialsProvider** for spring application by reading **HmacProperties**.
   - **HmacHelper** - Helper class to read Http request and extract data from it generate signature request of Hmac authentication.
   - **HmacSignature** - Class which contains signature methods to return Hmac token by using cryptographic algorithm, http request and secret.





5. #### Get token operation for Super Heroes

   In **HmacKeyController.java** class,
   we have exposed 5 endpoints for basic CRUD operations which are under SuperHeroController. This controller will generate token for each request which we'll be validating under
     superHeroController.
    - GET token for All Super Heroes
    - GET token for ID
    - POST token to store Super Hero in DB
    - PUT token for to update Super Hero
    - DELETE token for ID

    ```
    @RestController
    @RequestMapping("/hmac-key/super-heroes")
    public class HmacKeyController {

        @GetMapping("/{id}")
        public ResponseEntity<?> findById(HttpServletRequest httpServletRequest) throws IOException;

        @GetMapping
        public ResponseEntity<List<?>> findAll(HttpServletRequest httpServletRequest) throws IOException;

        @PostMapping
        public ResponseEntity<?> save(HttpServletRequest httpServletRequest) throws IOException;

        @PutMapping("/{id}")
        public ResponseEntity<?> update(HttpServletRequest httpServletRequest) throws IOException;
    
        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(HttpServletRequest httpServletRequest) throws IOException;
    }
    ```


6. #### CRUD operation for Super Heroes

   In **SuperHeroController.java** class,
   we have exposed 5 endpoints for basic CRUD operations
    - GET All Super Heroes
    - GET by ID
    - POST to store Super Hero in DB
    - PUT to update Super Hero
    - DELETE by ID

   But here we'll validate request before executing the actual logic and check the token is valid or not under HmacInterceptor class. 
    This will intercept each request and allow for only valid tokens.

    ```
    @RestController
    @RequestMapping("/super-hero")
    public class SuperHeroController {

        @GetMapping
        public ResponseEntity<List<?>> findAll();

        @GetMapping("/{id}")
        public ResponseEntity<?> findById(@PathVariable String id);

        @PostMapping
        public ResponseEntity<?> save(@RequestBody SuperHero superHero);

        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable int id, @RequestBody SuperHero superHero);
    
        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable String id);
    }
    ```


7. ### API Endpoints

    > **GET** http://localhost:8080/hmac-key/super-heroes # /hmac-key is important here

    #### Response
    ```
    {
        authorization: "HmacSHA256:3+na/n6Htt2MnRQzEtYYISy5l7O/GzHDtVyisDhNT/Q=",
        nonce: "1678793259142"
    }
    ```

    #### Try above authorization code and nonce in below request as header

    > **GET** http://localhost:8080/v1/super-heroes

    ### Request headers
    ```
    accessKey: 8c2ea66e-abfc-4394-8adb-fa52890bdce3
    authorization: HmacSHA256:3+na/n6Htt2MnRQzEtYYISy5l7O/GzHDtVyisDhNT/Q=
    nonce: 1678793259142
    ```

    ---
   
    > **GET** http://localhost:8080/hmac-key/super-heroes/1
   
    ### Response
    ```
    {
        authorization: "HmacSHA256:mHQeVAoGpDv7aSGLtDQ664gr7t47JL71NHNktj4w6hQ='
        nonce: "1678791858860"
    }
    ```
   #### Try above authorization code and nonce in below request as header

    > **GET** http://localhost:8080/v1/super-heroes/1

    ### Request headers   
    ```
    accessKey: 8c2ea66e-abfc-4394-8adb-fa52890bdce3
    authorization: HmacSHA256:mHQeVAoGpDv7aSGLtDQ664gr7t47JL71NHNktj4w6hQ=
    nonce: 1678791858860
    ```

    ---

    > **POST** http://localhost:8080/hmac-key/super-heroes

    ### Request

    ```
    curl --location --request POST 'http://localhost:8080/hmac-key/super-heroes' \
    --header 'Content-Type: application/json' \
    --data-raw ' {
            "id": 10,
           "name": "Tony",
           "superName": "Iron Man",
           "profession": "Business",
           "age": 50,
           "canFly": true
       }'
    ```

    ### Response
    ```
    {
        "authorization": "HmacSHA256:GGNhJT44kpnAozc/wf+a5I6IS+TyVV/nBTvrFh6Miwc=",
        "nonce": "1678796605841"
    }
    ```

    #### Try above authorization code and nonce in below request as header

    > **POST** http://localhost:8080/v1/super-heroes

    ### Request
    ```
    curl --location --request POST 'http://localhost:8080/v1/super-heroes' \
    --header 'accessKey: 8c2ea66e-abfc-4394-8adb-fa52890bdce3' \
    --header 'authorization: HmacSHA256:GGNhJT44kpnAozc/wf+a5I6IS+TyVV/nBTvrFh6Miwc=' \
    --header 'nonce: 1678796605841' \
    --header 'Content-Type: application/json' \
    --data-raw '{
            "id": 10,
           "name": "Tony",
           "superName": "Iron Man",
           "profession": "Business",
           "age": 50,
           "canFly": true
       }'
    ```

---

