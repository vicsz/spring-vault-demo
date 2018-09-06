# HashiCorp Vault Demo with Spring Cloud Vault (local Vault and PCF Vault Service)

Demo Application demonstrating values from a Hashicorp Vault instance using Spring Cloud Vault

# Vault Setup

## Local Vault Setup

*For local development testing purposes*

### Download Vault

Download from : https://www.vaultproject.io/downloads.html

### Run Vault Locally (in Dev Mode)

```sh
./vault server -dev -dev-root-token-id=00000000-0000-0000-0000-000000000000
```

> Note - storage is *in-memory* , and will need to be re-populated / re-configured every time you re-start vault.

> Note - you can access the local Vault GUI at http://localhost:8200

### Add a Secret value to Vault

#### Initial Config

```sh
export VAULT_ADDR='http://127.0.0.1:8200'
./vault secrets disable secret
./vault secrets enable -version=1 -path=secret kv
```

> Note - the requirement of setting the VAULT_ADDR variable, running Vault commands needs this to know which vault instance to execute against

> Note - re-creation of the secrets engine store with v1 instead of v2 of KV engine. ( v 0.10.0 introduced some breaking API changes, this is why this is required)


#### Add the actual value

```sh
./vault write secret/vault-demo vaultSecret=hiddenValue
```

> Note - *secret* is a specific directory used by Spring Cloud Vault (always needed) , *vault-demo* is your application name definied in bootstrap.properties.

### Run the application locally

```sh
mvn spring-boot:run
```

### Hit the root endpoint

```sh
curl http://localhost:8080
```

> You should see the correct value of *hiddenValue* loaded.

## PCF Vault Setup

### Create and Bind the Service

> TODO

### Adding Values to a PCF Vault Instance

> TODO

# Notes

**1. Vault Config settings are in bootstrap.properties file and not application.properties.**

**2. The setting of *spring.application.name* in the bootstrap.properties file, this value will be used when accessing the Vault Storage.**

**3. Loading of the Vault secret is performed using *Value* annotation in the *WebController* - same way as loading Values from a config server.**

**4. Spring Cloud Vault Config reads config properties from Vaults using the application name and active profiles:**

```
/secret/{application}/{profile}
/secret/{application}
/secret/{default-context}/{profile}
/secret/{default-context}
```

**5. Addition of default secret loading *:#{null}}* to ensure application loads even if vaultSecret value isn't found.**

```java
@Value("${vaultSecret:#{null}}")
private String vaultSecret;
```

Alternatives:

Stop application if vaultSecret is not found on initial load.
```java
@Value("${vaultSecret}")
private String vaultSecret;
```

Set valueSecret to a default text string *defaultValue* , if vaultSecret is not found.
```java
@Value("${vaultSecret:defaultValue}")
private String vaultSecret;
```

**6. Reloading Values from Vault**

A restart will always cause to reload of values from Vault.

For re-loading values **without** a restart, you can use the *refresh actuator* endpoint.

This will require the Actuator Spring Boot Dependency, and the refresh endpoint exposed (see application.properties file).

Also note the required addition of the RefreshScope annotation in the WebController.

To force a reload of values, the *refresh* endpoint needs to be hit:

```sh
curl -X POST http://localhost:8080/actuator/refresh
```




