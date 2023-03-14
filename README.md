
# Spring boot Hmac Authentication

### Request
> **GET** http://localhost:8080/hmac-key/super-heroes

### Response
```
{
    authorization: "HmacSHA256:3+na/n6Htt2MnRQzEtYYISy5l7O/GzHDtVyisDhNT/Q=",
    nonce: "1678793259142"
}
```

### Request URL
> **GET** http://localhost:8080/v1/super-heroes

### Request
#### Try above authorization code and nonce in below request as header
```
curl --location --request GET 'http://localhost:8080/v1/super-heroes' \
--header 'accessKey: 8c2ea66e-abfc-4394-8adb-fa52890bdce3' \
--header 'authorization: HmacSHA256:3+na/n6Htt2MnRQzEtYYISy5l7O/GzHDtVyisDhNT/Q=' \
--header 'nonce: 1678793259142'
```


---
---



### Request
> **GET** http://localhost:8080/hmac-key/super-heroes/1

### Response
```
{
    authorization: "HmacSHA256:mHQeVAoGpDv7aSGLtDQ664gr7t47JL71NHNktj4w6hQ='
    nonce: "1678791858860"
}
```

### Request URL
> **GET** http://localhost:8080/v1/super-heroes/1

### Request
#### Try above authorization code and nonce in below request
```
curl --location --request GET 'http://localhost:8080/v1/super-heroes/1' \
--header 'accessKey: 8c2ea66e-abfc-4394-8adb-fa52890bdce3' \
--header 'authorization: HmacSHA256:mHQeVAoGpDv7aSGLtDQ664gr7t47JL71NHNktj4w6hQ=' \
--header 'nonce: 1678791858860'
```


---
---


### Request URL
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


### Request URL
> **POST** http://localhost:8080/v1/super-heroes

### Request
#### Try above authorization code and nonce in below request as header
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
---

