# Mini Stock Manager System

## Content
- [Project Overview](#project-overview)
- [Microservices Overview](#microservices-overview)
- [AWS 2-tier Architecture & Deployment](#aws-2-tier-architecture--deployment)

---

## Project Overview

This project is a microservices system built with Java, Maven, and Spring Boot. It includes several components: Eureka Server for service discovery; microservices such as msvc-products and msvc-items; msvc-oauth, which functions as an authentication server; a config-server that dynamically sets our profile environment (dev & prod); and finally, msvc-gateway, which serves as the entry point for our microservices requests (similar to an API gateway, but native to Spring). The project employs Docker and Docker Compose for containerization and orchestration, enabling the services to communicate via Eureka and connect to an RDS MySQL database configured with dynamic environment variables. AWS integration leverages ECS with Fargate for serverless deployment, Route 53 for DNS management, and a load balancer for efficient traffic distribution.

## Microservices Overview

![Marchy](https://i.imghippo.com/files/IFGv8841OM.png)

1. **ConfigServer**
    - **Description:** Acts as a centralized configuration server for managing external properties for applications across all environments. It allows the microservices to fetch their configuration from a central location.
    - **Key Features:**
        - Centralized configuration management
        - Environment-specific configurations
        - Version control integration

2. **EurekaServer**
    - **Description:** A service registry using Netflix Eureka. It allows microservices to register themselves at runtime and provides a lookup mechanism to find other services for inter-service communication.
    - **Key Features:**
        - Service registration and discovery
        - Load balancing
        - Failover support

3. **msvc-gatewayServer**
    - **Description:** Serves as an API Gateway, providing a single entry point for all client requests. It routes requests to the appropriate microservices and handles cross-cutting concerns such as security, monitoring, and rate limiting.
    - **Key Features:**
        - Request routing
        - Security
        - Rate limiting
        - Monitoring

4. **msvcItems**
    - **Description:** Manages items in the system. It provides CRUD operations for items and interacts with other microservices to fetch related data.
    - **Key Features:**
        - Item management
        - CRUD operations
        - Inter-service communication

5. **msvcLibsCommons**
    - **Description:** Contains common libraries and utilities that are shared across multiple microservices. It helps reduce code duplication and maintain consistency.
    - **Key Features:**
        - Shared utilities
        - Common libraries
        - Reusable components

6. **msvcOauth**
    - **Description:** Handles authentication and authorization using OAuth2. It provides secure access to the microservices by issuing and validating tokens.
    - **Key Features:**
        - OAuth2 authentication
        - Token issuance
        - Token validation

7. **msvcProducts**
    - **Description:** Manages the products in the system. It provides CRUD operations for products and interacts with other microservices to fetch related data.
    - **Key Features:**
        - Product management
        - CRUD operations
        - Inter-service communication

8. **msvcUsers**
    - **Description:** Manages users in the system. It provides CRUD operations for user data and handles user-related functionalities.
    - **Key Features:**
        - User management
        - CRUD operations
        - User authentication

## Additional Components

- **config**
    - **Description:** Contains configuration files and resources used by the microservices. It may include environment-specific configurations and other settings.
    - **Key Features:**
        - Configuration management
        - Environment-specific settings

- **pom.xml**
    - **Description:** The Maven Project Object Model file that contains information about the project and configuration details used by Maven to build the project.
    - **Key Features:**
        - Project dependencies
        - Build configuration
        - Plugin management

- **compose.yml**
    - **Description:** The Docker Compose file that defines the services, networks, and volumes for the Docker containers used in the project.
    - **Key Features:**
        - Service orchestration
        - Container configuration
        - Network setup

## AWS 2-tier Architecture & Deployment

This section outlines our AWS 2-tier architecture and deployment strategy, designed to provide a secure, scalable, and highly available infrastructure for hosting our microservices APIs. The architecture leverages a suite of AWS services, combined with external resources, to meet both operational and security best practices as defined in the AWS Well-Architected Framework.

### Architecture Diagram

![2-tier Architecture Diagram](https://i.imghippo.com/files/dvx7877xQ.png)

### AWS Resources Utilized

- **Route 53 (Public Hosted Zones):**  
  Manages DNS records with a public hosted zone, enabling domain resolution for our internet-facing services.

- **ACM (AWS Certificate Manager):**  
  Provisions and manages SSL/TLS certificates to secure communications within the public hosted zone.

- **VPC & Subnets (Public and Private):**  
  Isolates and controls network traffic within a custom virtual network, distributing resources between public and private subnets.

- **Internet Gateway & NAT Gateway:**
    - **Internet Gateway:** Provides connectivity for resources in the public subnet.
    - **NAT Gateway:** Allows resources in private subnets to securely access the internet (e.g., for pulling container images from DockerHub).

- **ECS & Fargate:**  
  Runs containerized microservices on a serverless compute engine, with tasks dynamically registered to a target group managed by the Application Load Balancer.

- **Application Load Balancer (ALB):**  
  Acts as the single entry point for internet requests, terminating SSL/TLS encryption and forwarding decrypted HTTP traffic to ECS tasks.

- **RDS (MySQL):**  
  Hosts a managed MySQL database for persisting application data and handling transactions.

- **Secrets Manager:**  
  Securely stores and manages database credentials, leveraging automatic rotation and encryption at rest.

- **S3:**  
  Stores encrypted snapshots of the RDS database, ensuring data backups are secure and readily retrievable.

### External Resources

- **Cloudflare:**  
  Although our domain is managed via Cloudflare, we integrate with AWS by creating a public hosted zone in Route 53 to route traffic efficiently.

- **DockerHub:**  
  Stores container images, which are pulled during deployment for running our ECS Fargate tasks.

### Application Flow

1. **Domain Resolution & SSL Termination:**
    - The domain, originally managed by Cloudflare, is integrated with AWS by creating a public hosted zone in Route 53.
    - An alias record points to the Application Load Balancer (ALB), which handles all incoming internet-facing requests.
    - An SSL certificate is provisioned via AWS ACM to ensure that all communications with the ALB are encrypted (HTTPS).

2. **Traffic Routing & Container Deployment:**
    - The ALB listens exclusively for HTTPS traffic. Upon receiving a request, it terminates the SSL/TLS encryption and forwards the request as HTTP to a target group.
    - ECS Fargate services register themselves with the target group during deployment, ensuring seamless routing of traffic to the appropriate containers.

3. **Private Subnet Connectivity & Image Pulling:**
    - Since ECS tasks run within private subnets, a NAT Gateway is configured to allow these containers to securely pull images from DockerHub.

4. **Database and Data Security:**
    - A MySQL database is deployed using RDS to persist application data and manage transactions.
    - AWS Secrets Manager securely stores the database credentials, supporting automatic rotation and encryption at rest.
    - Encrypted snapshots of the RDS instance are stored in an S3 bucket for backup and disaster recovery.

5. **Adherence to Best Practices:**
    - Throughout the deployment process, we follow the AWS Well-Architected Framework to ensure that our infrastructure meets best practices for security, performance, reliability, and cost optimization.
