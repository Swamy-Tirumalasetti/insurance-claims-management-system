# Insurance Claims Management System

A Java 17 and Spring Boot REST API for managing insurance customers, policies, and claims. This README has two purposes: a chronological build journal to help me remember what I did, and a concise interview summary of the work.

## Project status — November 28, 2026

**Phase 1: Core application — complete**  
**Phase 2: Amazon RDS integration — complete**  
**Phase 3: Amazon S3 — planned**

## Part 1: Personal build journal

Read this section from top to bottom to recall how the project was built. When continuing the project, add the actual actions and verification results under the relevant phase; keep planned work marked as planned until it succeeds.

### Phase 1 — Build the core application

#### Step 1 — Create the Spring Boot project

**What I did:** Generated `Java_Aws_Aflac_Application` with Java 17 and the Spring Web, Spring Data JPA, MySQL Connector/J, and Lombok dependencies. Opened it in IntelliJ and aligned the project SDK/compiler with JDK 17.

**Result:** A Maven-based Spring Boot project that builds against Java 17.

#### Step 2 — Set up Git and GitHub

**What I did:** Initialized and used the `main` branch, committed the project in milestones, and pushed it to the GitHub repository. Kept build output and IDE files out of version control with `.gitignore`.

**Result:** The project and its progress are tracked in GitHub as separate setup and API milestones.

#### Step 3 — Connect the application to local MySQL

**What I did:** Configured the application to use the local MySQL database `insurance_claims_db`, with Hibernate set to update the schema. Kept the database password outside the committed configuration in an environment variable.

**Result:** The application can use MySQL for local persistence, and Hibernate can create/update the mapped schema.

#### Step 4 — Model the insurance data

**What I did:** Created JPA entities and `JpaRepository` interfaces for `Customer`, `Policy`, `Claim`, and `ClaimDocument`. Connected policies to customers, claims to policies, and claim documents to claims with `@ManyToOne` relationships and foreign keys. Used `BigDecimal` for monetary amounts.

**Result:** A relational persistence model for the customer → policy → claim → document flow. `ClaimDocument` currently represents metadata such as file name, S3 bucket, and object key; it does not upload files to S3 yet.

#### Step 5 — Define API request and response data

**What I did:** Created Java record DTOs for customer, policy, and claim requests and responses. Requests refer to existing records using IDs such as `customerId` and `policyId`.

**Result:** The API has explicit data contracts and returns DTOs rather than serializing JPA entities directly.

#### Step 6 — Implement business services

**What I did:** Added customer, policy, and claim services to map DTOs to entities, call repositories, and map saved entities back to response DTOs. Policy creation checks that the customer exists. Claim submission checks that the policy exists, rejects a claimed amount above policy coverage, and sets the initial status to `SUBMITTED`. Policy and claim write/retrieval methods use transactions.

**Result:** Core business rules are handled in the service layer, separate from HTTP request handling.

#### Step 7 — Expose REST endpoints and errors

**What I did:** Added REST controllers for customers, policies, and claims. Create endpoints return `201 Created`; retrieval endpoints return `200 OK`. Added centralized exception handling for bad requests (`400`) and missing records (`404`).

**Result:** The core workflows are available over HTTP:

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/customers` | Create a customer |
| `GET` | `/customers/{id}` | Retrieve a customer |
| `POST` | `/policies` | Create a policy |
| `GET` | `/policies/{id}` | Retrieve a policy |
| `POST` | `/claims` | Submit a claim |
| `GET` | `/claims/{id}` | Retrieve a claim |
| `GET` | `/claims` | List claims |

#### Step 8 — Verify the end-to-end flow

**What I did:** Used Postman to send API requests and MySQL Workbench to inspect persisted data and relationships.

**Result:** Verified the request-to-service-to-repository-to-MySQL flow for the core create/retrieve scenarios.

### Phase 2 — Connect persistence to Amazon RDS (complete)

#### Step 1 — Provision the managed MySQL database

**What I did:** Provisioned the AWS RDS MySQL 8.0 instance `insurance-claims-rds` with the initial database `insurance_claims_db`.

**Result:** The application has a managed MySQL database in AWS instead of relying only on local MySQL.

#### Step 2 — Configure network access for local development

**What I did:** Enabled public access temporarily for local testing and configured the RDS VPC security group to allow inbound TCP port `3306` from my IP address.

**Result:** My local application and MySQL Workbench could reach the RDS database. This is a development setup; for production, disable public access and allow database traffic only from the application tier's security group.

#### Step 3 — Add a Spring `rds` profile

**What I did:** Added `application-rds.properties` with the RDS endpoint, username, and password supplied through `RDS_ENDPOINT`, `RDS_USERNAME`, and `RDS_PASSWORD` environment variables in the IntelliJ run configuration. Started the application using the `rds` profile.

**Result:** The same Java application can target local MySQL or AWS RDS through configuration, without changing its business logic. Credentials remain outside the committed configuration.

#### Step 4 — Verify the cloud database and application flow

**What I did:** Connected MySQL Workbench to the RDS endpoint on port `3306`. Sent `POST` and `GET` requests for customers, policies, and claims through Postman while the application was running with the `rds` profile. Checked the resulting records and foreign-key relationships in the RDS database.

**Result:** Verified the application-to-RDS JDBC connection, Hibernate schema creation, API persistence, and successful reads/writes against the cloud database.

#### Phase 2 request flow

`Postman (POST/GET)` → `Spring Boot (rds profile)` → `AWS RDS MySQL (port 3306)` → `verified in MySQL Workbench`

**Security note:** The RDS security group currently allows access from my IP for local testing. Keep this restricted, and remove public access/use application-tier-only database access for a production deployment.

#### Phase 2 — What each step demonstrates

| Layer / tool | Hands-on work | Interview purpose |
|---|---|---|
| AWS RDS | Provisioned a managed MySQL 8.0 instance and initialized `insurance_claims_db`. | Uses a managed database service rather than operating the database server directly. |
| VPC security group | Allowed inbound TCP `3306` from my IP for temporary local development access. | Demonstrates network-level access control; production access should come only from the application tier. |
| MySQL Workbench | Connected to the RDS endpoint and inspected generated tables and persisted records. | Independently verifies the cloud database schema and data. |
| Spring Boot `rds` profile | Added RDS-specific configuration and selected it when running the application; supplied connection settings through IntelliJ environment variables. | Separates environment configuration from Java business logic and keeps credentials out of Git. |
| Postman | Sent customer, policy, and claim POST/GET requests to the running application. | Confirms API operations persist and retrieve related data through the cloud database. |

### Phase 3 — Store claim documents in Amazon S3 (planned)

**Goal:** Add real S3 document upload and retrieval for claims, using the existing document metadata model where appropriate.

**When completed, record:** Bucket and access setup, application changes, how the S3 object key is associated with a claim, upload/download checks, and successful verification. Do not mark this phase complete until file operations have been tested.

### Further AWS deployment and operations (planned)

EC2, an Application Load Balancer (ALB), Lambda, and CloudWatch are part of the broader project goal. Their integration/deployment is not recorded as completed yet. Add a dated build-journal step and the actual verification result for each service when work begins and succeeds.

## Part 2: Interview-ready summary

### 30-second pitch

> I built the backend for an Insurance Claims Management System using Java 17, Spring Boot, Spring Data JPA, and MySQL. I modeled customers, policies, claims, and claim-document metadata as relational entities, then added record-based DTOs, repository-backed services, REST endpoints, and centralized exception handling. The claim workflow validates its policy and prevents claims from exceeding coverage. I first verified the application locally, then connected it to AWS RDS MySQL through a dedicated Spring profile with environment-based configuration—without changing business logic. I tested customer, policy, and claim API flows with Postman and verified the cloud records in MySQL Workbench. S3 document storage is the next phase.

### Phase 2 — 30-second RDS pitch

> After building and testing the application locally, I connected its persistence layer to an AWS RDS MySQL 8.0 instance without changing the Java business logic. I added a dedicated Spring `rds` profile and supplied the RDS endpoint and credentials through environment variables. For local testing, I temporarily enabled public access and restricted the VPC security group on port 3306 to my IP. I verified the connection and schema through MySQL Workbench, then tested customer, policy, and claim POST/GET requests with Postman and confirmed the persisted records in RDS.

### Interview flow

**Phase 1 — setup → persistence → API → verification:** Spring Boot project with Java 17 and Maven → local MySQL, JPA entities, relationships, and repositories → record DTOs, transactional services, REST controllers, and exception handling → Postman requests and MySQL Workbench checks.

**Phase 2 — provision → secure → configure → verify:** RDS MySQL instance → security-group ingress restricted to my IP for local testing → Spring `rds` profile with environment-based configuration → Postman API requests → verify schema and records in MySQL Workbench.

**Why this design:** DTOs keep API contracts separate from database entities; repositories reduce persistence boilerplate; service-layer checks enforce domain rules; transactions protect database operations; and end-to-end testing confirms the request reaches and persists in MySQL.
