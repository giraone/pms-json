# Testing PostgreSQL JSONB/JSON, Spring Data Rest and QueryDSL

**TODO**
- QueryDSL
- UI

**Features/scope of this project**

- Java 9 source level (needed for using `Map.of()`)
- JUnit 5 with AssertJ
- Spring Data Rest
  . basic usage
  - paging and sorting enabled
  - HAL explorer (?)
- Flexible storage of additional attributes (`Map<Object, String>`  Serialization/Deserialization to JSON/JSONB)
- Docker compose setup with PostgreSQL and PgAdmin4 is available
- OWASP maven dependency check configured in `pom.xml`. Call using `mvn -DskipTests=true verify`. Result in `dependency-check-report.html`.

## Build and Run

Build:
```
export JAVA_HOME=<path-to-jdk9-or-higher>
mvn package
```

Run:
```
docker-compose up -d
java -jar target/pms-json.jar
```

## Spring Data Rest

See also https://docs.spring.io/spring-data/rest/docs/current/reference/html/

### Paging and Sorting

Paging and Sorting enabled by using `PagingAndSortingRepository`:

```java
@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID> {
}
```

Configuration in `application.yml e.g.
- page size set to 20
- URL base path  ["localhost:8080/**api**/companies"](http://localhost:8080/api/companies")

```
spring:
  data:
    rest:
      base-path: api
      default-page-size: 20
```

## HAL explorer
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-rest-hal-explorer</artifactId>
</dependency>
```

## Flexible storage of additional attributes

Here I want to show, how one can map arbitrary hierarchical properties (JSON tree with sub elements and arrays)
to JSON/JSONB columns in PostgreSQL (and H2 for testing). The shown *Company* entity has a *companyAddress*
object, which may differ in its content.

### Solution 1: Using `HashMap<String, Object>` and an `javax.persistence.AttributeConverter` 

```java
import com.giraone.pms.repository.conversion.HashMapConverter;
...
@Entity
public class Company implements Serializable {

    @Id
    private UUID id;

    ...
    
    @Column(name = "company_address")
    @Convert(converter = HashMapConverter.class)
    private HashMap<String, Object> companyAddress;
}
```

### Solution 2 using `om.vladmihalcea.hibernate.type.json.JsonStringType`
```java
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Company implements Serializable {

    @Id
    private UUID id;
 
    ...

    @Type(type = "json")
    @Column(name = "company_address")
    private HashMap<String, Object> companyAddress;
}
```

### Solution 3 using `com.vladmihalcea.hibernate.type.json.JsonBinaryType`

```java
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
...
@Entity
public class Company implements Serializable {

    @Id
    private UUID id;
    ...

    @Column(name = "company_address")
    @Type(type = "jsonb")
    private HashMap<String, Object> companyAddress;
}
```

## QUERY DSL

https://www.baeldung.com/rest-api-search-language-spring-data-querydsl

## Tests with SQL based schema creation and SQL test data:

```
@Sql(value = {"/schema-h2-create.sql" "/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-h2-drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
class SpringDataRestResourcesIntTest {
}
```

### Fetch type LAZY

```
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
...
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
public class Company {
    ...
    @Type(type = "jsonb")
    @Column(columnDefinition = "companyAddress")
    @Basic(fetch = FetchType.LAZY)
    private CompanyAddress companyAddress;
}
```

For using the fetch type LAZY, `hibernate-enhance-maven-plugin` is also needed.

### Entities with PostgreSQL UUID

```
@Entity
public class Company {
    @Id
    @Type(type = "pg-uuid")
    private UUID id;
    ...
}
```