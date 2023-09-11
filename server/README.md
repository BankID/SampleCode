# BankID Demo Server

This is the server part of the BankID Demo.

## Important classes

### [RpApi](src/main/java/com/bankid/codefront/bankid/relyingparty/RpApi.java)
Implementation of communication with the BankID relying party api.

### [BankIDService](src/main/java/com/bankid/codefront/service/BankIDService.java)
The business logic for using the BankID integration.

### [TransactionController](src/main/java/com/bankid/codefront/rest/controller/TransactionController.java)
Client api for BankID authentication and sign.

## Spring profiles

| name        | description                                       |
|-------------|---------------------------------------------------|
| default     | https and redis as session store.                 |
| dev         | BankID test and uses in-memory store for sessions |
| prod        | BankID production, placeholder                    |
| redis       | enables redis for sessions.                       |
| dev-log     | log as text instead of json-format                |
| tlsdisabled | disable tls, used for tests                       |

## Directories

| name                                               | description                                                                             |
|----------------------------------------------------|-----------------------------------------------------------------------------------------|
| [certificates/production](certificates/production) | contains truststore with the public key for BankID in production.                       |
| [certificates/test](certificates/test)             | contains truststore with the public key for BankID in test and the test FP certificate. |

