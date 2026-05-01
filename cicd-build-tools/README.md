## Jenkins - Pipelines, Stages, Agents

<details>
  <summary>Jenkins Pipeline Basics</summary>
  <br/>

  **Jenkins Pipeline** is a suite of plugins for implementing continuous delivery pipelines as code.

  **Pipeline Types:**

  **1. Declarative Pipeline (Recommended):**

  ```groovy
  pipeline {
      agent any
      
      stages {
          stage('Build') {
              steps {
                  echo 'Building...'
                  sh 'mvn clean package'
              }
          }
          
          stage('Test') {
              steps {
                  echo 'Testing...'
                  sh 'mvn test'
              }
          }
          
          stage('Deploy') {
              steps {
                  echo 'Deploying...'
                  sh './deploy.sh'
              }
          }
      }
  }
  ```

  **2. Scripted Pipeline (More flexible):**

  ```groovy
  node {
      stage('Build') {
          echo 'Building...'
          sh 'mvn clean package'
      }
      
      stage('Test') {
          echo 'Testing...'
          sh 'mvn test'
      }
      
      stage('Deploy') {
          echo 'Deploying...'
          sh './deploy.sh'
      }
  }
  ```

  **Pipeline Structure:**

  ```
  Pipeline
  │
  ├── Agent (where to run)
  │
  ├── Environment (variables)
  │
  ├── Stages
  │   ├── Stage 1: Checkout
  │   ├── Stage 2: Build
  │   ├── Stage 3: Test
  │   ├── Stage 4: Quality Gate
  │   └── Stage 5: Deploy
  │
  ├── Post Actions
  │   ├── Always
  │   ├── Success
  │   └── Failure
  ```

  **Complete Example:**

  ```groovy
  pipeline {
      agent any
      
      environment {
          MAVEN_HOME = tool 'Maven-3.8'
          JAVA_HOME = tool 'JDK-17'
          PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
      }
      
      stages {
          stage('Checkout') {
              steps {
                  git branch: 'main',
                      url: 'https://github.com/company/app.git',
                      credentialsId: 'github-credentials'
              }
          }
          
          stage('Build') {
              steps {
                  sh 'mvn clean compile'
              }
          }
          
          stage('Test') {
              steps {
                  sh 'mvn test'
              }
              post {
                  always {
                      junit '**/target/surefire-reports/*.xml'
                  }
              }
          }
          
          stage('Package') {
              steps {
                  sh 'mvn package -DskipTests'
              }
          }
          
          stage('Deploy') {
              steps {
                  sh './deploy.sh'
              }
          }
      }
      
      post {
          success {
              echo 'Pipeline succeeded!'
              emailext to: 'team@company.com',
                       subject: "Build Success: ${env.JOB_NAME}",
                       body: "Build ${env.BUILD_NUMBER} succeeded"
          }
          failure {
              echo 'Pipeline failed!'
              emailext to: 'team@company.com',
                       subject: "Build Failed: ${env.JOB_NAME}",
                       body: "Build ${env.BUILD_NUMBER} failed"
          }
      }
  }
  ```

</details>

<details>
  <summary>Jenkins Agents</summary>
  <br/>

  **Agents** define where the pipeline or stage executes.

  **Agent Types:**

  **1. Any Agent:**

  ```groovy
  pipeline {
      agent any  // Run on any available agent
      
      stages {
          stage('Build') {
              steps {
                  sh 'mvn clean package'
              }
          }
      }
  }
  ```

  **2. None (Define per stage):**

  ```groovy
  pipeline {
      agent none  // No global agent
      
      stages {
          stage('Build') {
              agent {
                  label 'linux'  // Run on Linux agent
              }
              steps {
                  sh 'mvn clean package'
              }
          }
          
          stage('Test') {
              agent {
                  label 'windows'  // Run on Windows agent
              }
              steps {
                  bat 'mvn test'
              }
          }
      }
  }
  ```

  **3. Label (Specific agent):**

  ```groovy
  pipeline {
      agent {
          label 'linux && docker'  // Agent with both labels
      }
      
      stages {
          stage('Build') {
              steps {
                  sh 'docker build -t myapp .'
              }
          }
      }
  }
  ```

  **4. Docker Agent:**

  ```groovy
  pipeline {
      agent {
          docker {
              image 'maven:3.8-openjdk-17'
              args '-v $HOME/.m2:/root/.m2'
          }
      }
      
      stages {
          stage('Build') {
              steps {
                  sh 'mvn clean package'
              }
          }
      }
  }
  ```

  **5. Multiple Agents (Different stages):**

  ```groovy
  pipeline {
      agent none
      
      stages {
          stage('Build on Linux') {
              agent {
                  label 'linux'
              }
              steps {
                  sh 'mvn clean package'
              }
          }
          
          stage('Test on Windows') {
              agent {
                  label 'windows'
              }
              steps {
                  bat 'mvn test'
              }
          }
          
          stage('Build Docker') {
              agent {
                  docker {
                      image 'docker:latest'
                  }
              }
              steps {
                  sh 'docker build -t myapp .'
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Pipeline Stages</summary>
  <br/>

  **Stages** organize pipeline work into logical phases.

  **Common Stage Pattern:**

  ```groovy
  pipeline {
      agent any
      
      stages {
          // 1. Source Control
          stage('Checkout') {
              steps {
                  git branch: 'main',
                      url: 'https://github.com/company/app.git'
              }
          }
          
          // 2. Build
          stage('Build') {
              steps {
                  sh 'mvn clean compile'
              }
          }
          
          // 3. Unit Tests
          stage('Unit Tests') {
              steps {
                  sh 'mvn test'
              }
              post {
                  always {
                      junit '**/target/surefire-reports/*.xml'
                      jacoco execPattern: '**/target/jacoco.exec'
                  }
              }
          }
          
          // 4. Code Quality
          stage('SonarQube Analysis') {
              steps {
                  withSonarQubeEnv('SonarQube') {
                      sh 'mvn sonar:sonar'
                  }
              }
          }
          
          stage('Quality Gate') {
              steps {
                  timeout(time: 5, unit: 'MINUTES') {
                      waitForQualityGate abortPipeline: true
                  }
              }
          }
          
          // 5. Package
          stage('Package') {
              steps {
                  sh 'mvn package -DskipTests'
              }
          }
          
          // 6. Build Docker Image
          stage('Docker Build') {
              steps {
                  script {
                      docker.build("myapp:${env.BUILD_NUMBER}")
                  }
              }
          }
          
          // 7. Push to Registry
          stage('Push to Registry') {
              steps {
                  script {
                      docker.withRegistry('https://registry.company.com', 'docker-credentials') {
                          docker.image("myapp:${env.BUILD_NUMBER}").push()
                          docker.image("myapp:${env.BUILD_NUMBER}").push('latest')
                      }
                  }
              }
          }
          
          // 8. Deploy to Dev
          stage('Deploy to Dev') {
              steps {
                  sh 'kubectl set image deployment/myapp myapp=registry.company.com/myapp:${BUILD_NUMBER} -n dev'
              }
          }
          
          // 9. Integration Tests
          stage('Integration Tests') {
              steps {
                  sh 'mvn verify -Pintegration-tests'
              }
          }
          
          // 10. Deploy to Staging
          stage('Deploy to Staging') {
              when {
                  branch 'main'
              }
              steps {
                  sh 'kubectl set image deployment/myapp myapp=registry.company.com/myapp:${BUILD_NUMBER} -n staging'
              }
          }
          
          // 11. Manual Approval
          stage('Approve Production') {
              when {
                  branch 'main'
              }
              steps {
                  input message: 'Deploy to production?',
                        ok: 'Deploy',
                        submitter: 'admin,release-manager'
              }
          }
          
          // 12. Deploy to Production
          stage('Deploy to Production') {
              when {
                  branch 'main'
              }
              steps {
                  sh 'kubectl set image deployment/myapp myapp=registry.company.com/myapp:${BUILD_NUMBER} -n production'
              }
          }
      }
  }
  ```

  **Parallel Stages:**

  ```groovy
  pipeline {
      agent any
      
      stages {
          stage('Build') {
              steps {
                  sh 'mvn clean package'
              }
          }
          
          stage('Parallel Tests') {
              parallel {
                  stage('Unit Tests') {
                      steps {
                          sh 'mvn test'
                      }
                  }
                  
                  stage('Integration Tests') {
                      steps {
                          sh 'mvn verify -Pintegration'
                      }
                  }
                  
                  stage('Security Scan') {
                      steps {
                          sh 'dependency-check.sh'
                      }
                  }
              }
          }
          
          stage('Deploy') {
              steps {
                  sh './deploy.sh'
              }
          }
      }
  }
  ```

  **Conditional Stages:**

  ```groovy
  pipeline {
      agent any
      
      stages {
          stage('Build') {
              steps {
                  sh 'mvn clean package'
              }
          }
          
          // Only on main branch
          stage('Deploy to Staging') {
              when {
                  branch 'main'
              }
              steps {
                  sh './deploy-staging.sh'
              }
          }
          
          // Only on release branches
          stage('Deploy to Production') {
              when {
                  branch 'release/*'
              }
              steps {
                  sh './deploy-production.sh'
              }
          }
          
          // Only if environment variable set
          stage('Performance Tests') {
              when {
                  environment name: 'RUN_PERF_TESTS', value: 'true'
              }
              steps {
                  sh 'mvn verify -Pperformance'
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Environment Variables and Credentials</summary>
  <br/>

  **Environment Variables:**

  ```groovy
  pipeline {
      agent any
      
      environment {
          // Global variables
          APP_NAME = 'myapp'
          VERSION = '1.0.0'
          REGISTRY = 'registry.company.com'
          
          // Tool paths
          MAVEN_HOME = tool 'Maven-3.8'
          JAVA_HOME = tool 'JDK-17'
          
          // Credentials
          DOCKER_CREDENTIALS = credentials('docker-registry-credentials')
          AWS_CREDENTIALS = credentials('aws-credentials')
      }
      
      stages {
          stage('Build') {
              environment {
                  // Stage-specific variable
                  BUILD_ENV = 'production'
              }
              steps {
                  echo "Building ${APP_NAME} version ${VERSION}"
                  echo "Build environment: ${BUILD_ENV}"
                  sh 'mvn clean package'
              }
          }
      }
  }
  ```

  **Built-in Environment Variables:**

  ```groovy
  pipeline {
      agent any
      
      stages {
          stage('Info') {
              steps {
                  echo "Job Name: ${env.JOB_NAME}"
                  echo "Build Number: ${env.BUILD_NUMBER}"
                  echo "Build ID: ${env.BUILD_ID}"
                  echo "Build URL: ${env.BUILD_URL}"
                  echo "Workspace: ${env.WORKSPACE}"
                  echo "Git Branch: ${env.GIT_BRANCH}"
                  echo "Git Commit: ${env.GIT_COMMIT}"
                  echo "Node Name: ${env.NODE_NAME}"
              }
          }
      }
  }
  ```

  **Credentials Management:**

  ```groovy
  pipeline {
      agent any
      
      stages {
          // Username/Password
          stage('Docker Login') {
              steps {
                  withCredentials([usernamePassword(
                      credentialsId: 'docker-credentials',
                      usernameVariable: 'DOCKER_USER',
                      passwordVariable: 'DOCKER_PASS'
                  )]) {
                      sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                  }
              }
          }
          
          // Secret Text
          stage('API Call') {
              steps {
                  withCredentials([string(
                      credentialsId: 'api-token',
                      variable: 'API_TOKEN'
                  )]) {
                      sh 'curl -H "Authorization: Bearer $API_TOKEN" https://api.company.com'
                  }
              }
          }
          
          // SSH Key
          stage('Deploy via SSH') {
              steps {
                  withCredentials([sshUserPrivateKey(
                      credentialsId: 'ssh-key',
                      keyFileVariable: 'SSH_KEY',
                      usernameVariable: 'SSH_USER'
                  )]) {
                      sh 'ssh -i $SSH_KEY $SSH_USER@server.com ./deploy.sh'
                  }
              }
          }
      }
  }
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```groovy
  // ✅ DO: Use Declarative Pipeline
  pipeline {
      agent any
      stages {
          stage('Build') {
              steps {
                  sh 'mvn clean package'
              }
          }
      }
  }
  
  // ✅ DO: Define agent per stage for efficiency
  pipeline {
      agent none
      stages {
          stage('Build') {
              agent { label 'linux' }
              steps {
                  sh 'mvn clean package'
              }
          }
      }
  }
  
  // ✅ DO: Use environment variables
  pipeline {
      agent any
      environment {
          APP_NAME = 'myapp'
          VERSION = '1.0.0'
      }
      stages {
          stage('Build') {
              steps {
                  echo "Building ${APP_NAME} ${VERSION}"
              }
          }
      }
  }
  
  // ✅ DO: Use credentials() for secrets
  pipeline {
      agent any
      environment {
          DOCKER_CREDS = credentials('docker-credentials')
      }
      stages {
          stage('Push') {
              steps {
                  sh 'docker login -u $DOCKER_CREDS_USR -p $DOCKER_CREDS_PSW'
              }
          }
      }
  }
  
  // ✅ DO: Use parallel stages for speed
  stage('Tests') {
      parallel {
          stage('Unit') {
              steps { sh 'mvn test' }
          }
          stage('Integration') {
              steps { sh 'mvn verify' }
          }
      }
  }
  
  // ✅ DO: Add timeouts
  stage('Deploy') {
      steps {
          timeout(time: 10, unit: 'MINUTES') {
              sh './deploy.sh'
          }
      }
  }
  
  // ✅ DO: Use retry for flaky operations
  stage('Deploy') {
      steps {
          retry(3) {
              sh './deploy.sh'
          }
      }
  }
  
  // ✅ DO: Archive artifacts and test results
  post {
      always {
          junit '**/target/surefire-reports/*.xml'
          archiveArtifacts 'target/*.jar'
      }
  }
  
  // ✅ DO: Clean workspace
  post {
      always {
          cleanWs()
      }
  }
  
  // ❌ DON'T: Hardcode credentials
  // Use credentials() instead
  
  // ❌ DON'T: Use agent any for all stages
  // Define specific agents per stage
  
  // ❌ DON'T: Run long operations without timeout
  // Always add timeout for external calls
  
  // ❌ DON'T: Ignore test failures
  // Always publish test results
  ```

  **Summary:**
  + **Declarative Pipeline** - Structured, easier to read and maintain
  + **Agents** - Define where pipeline executes (any, label, docker, kubernetes)
  + **Stages** - Organize work into logical phases
  + **Environment** - Manage variables and credentials securely
  + **Parallel** - Run stages concurrently for speed
  + **Retry/Timeout** - Handle failures and prevent hanging
  + **Post Actions** - Always, success, failure notifications
  + **Credentials** - Never hardcode, use credentials()
  + **Clean Workspace** - Always clean up after build
  + **Archive Artifacts** - Save build outputs and test results

</details>

## Docker - Containerization

<details>
  <summary>Docker Basics</summary>
  <br/>

  **Docker** packages applications and dependencies into containers.

  **Key Concepts:**

  ```
  ┌─────────────────────────────────────┐
  │         Docker Architecture         │
  ├─────────────────────────────────────┤
  │                                     │
  │  Dockerfile ──build──▶ Image       │
  │                          │          │
  │                          │ run      │
  │                          ▼          │
  │                      Container      │
  │                                     │
  │  Image = Template (read-only)      │
  │  Container = Running instance      │
  └─────────────────────────────────────┘
  ```

  **Basic Commands:**

  ```bash
  # Images
  docker images                    # List images
  docker pull nginx:latest         # Download image
  docker build -t myapp:1.0 .     # Build image
  docker rmi myapp:1.0            # Remove image
  docker tag myapp:1.0 myapp:latest  # Tag image
  
  # Containers
  docker ps                        # List running containers
  docker ps -a                     # List all containers
  docker run -d -p 8080:8080 myapp:1.0  # Run container
  docker stop container_id         # Stop container
  docker start container_id        # Start container
  docker restart container_id      # Restart container
  docker rm container_id           # Remove container
  docker logs container_id         # View logs
  docker exec -it container_id bash  # Enter container
  
  # System
  docker system df                 # Show disk usage
  docker system prune              # Clean up unused resources
  docker system prune -a           # Remove all unused images
  ```

  **Docker Run Options:**

  ```bash
  # Basic run
  docker run nginx
  
  # Detached mode (background)
  docker run -d nginx
  
  # Port mapping
  docker run -p 8080:80 nginx
  
  # Name container
  docker run --name my-nginx nginx
  
  # Environment variables
  docker run -e DB_HOST=localhost -e DB_PORT=5432 myapp
  
  # Volume mount
  docker run -v /host/path:/container/path myapp
  
  # Network
  docker run --network my-network myapp
  
  # Resource limits
  docker run --memory="512m" --cpus="1.0" myapp
  
  # Auto-remove after stop
  docker run --rm myapp
  
  # Interactive terminal
  docker run -it ubuntu bash
  
  # Complete example
  docker run -d \
    --name myapp \
    -p 8080:8080 \
    -e SPRING_PROFILES_ACTIVE=prod \
    -e DB_HOST=postgres \
    -v /data:/app/data \
    --network app-network \
    --memory="1g" \
    --cpus="2.0" \
    --restart unless-stopped \
    myapp:1.0
  ```

</details>

<details>
  <summary>Dockerfile</summary>
  <br/>

  **Dockerfile** defines how to build a Docker image.

  **Basic Dockerfile:**

  ```dockerfile
  # Base image
  FROM openjdk:17-jdk-slim
  
  # Metadata
  LABEL maintainer="team@company.com"
  LABEL version="1.0"
  
  # Working directory
  WORKDIR /app
  
  # Copy files
  COPY target/myapp.jar app.jar
  
  # Expose port
  EXPOSE 8080
  
  # Run command
  CMD ["java", "-jar", "app.jar"]
  ```

  **Complete Spring Boot Dockerfile:**

  ```dockerfile
  FROM openjdk:17-jdk-slim
  
  # Install dependencies
  RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
  
  # Create app user (security)
  RUN groupadd -r appuser && useradd -r -g appuser appuser
  
  # Set working directory
  WORKDIR /app
  
  # Copy JAR
  COPY target/myapp-1.0.0.jar app.jar
  
  # Change ownership
  RUN chown -R appuser:appuser /app
  
  # Switch to non-root user
  USER appuser
  
  # Health check
  HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
  
  # Expose port
  EXPOSE 8080
  
  # JVM options
  ENV JAVA_OPTS="-Xmx512m -Xms256m"
  
  # Run application
  ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
  ```

  **Multi-Stage Build (Recommended):**

  ```dockerfile
  # Stage 1: Build
  FROM maven:3.8-openjdk-17 AS build
  
  WORKDIR /app
  
  # Copy pom.xml first (layer caching)
  COPY pom.xml .
  RUN mvn dependency:go-offline
  
  # Copy source code
  COPY src ./src
  
  # Build application
  RUN mvn clean package -DskipTests
  
  # Stage 2: Runtime
  FROM openjdk:17-jdk-slim
  
  # Create non-root user
  RUN groupadd -r appuser && useradd -r -g appuser appuser
  
  WORKDIR /app
  
  # Copy only the JAR from build stage
  COPY --from=build /app/target/myapp-*.jar app.jar
  
  RUN chown -R appuser:appuser /app
  USER appuser
  
  EXPOSE 8080
  
  HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
  
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```

  **Dockerfile Instructions:**

  ```dockerfile
  # FROM - Base image
  FROM openjdk:17-jdk-slim
  
  # LABEL - Metadata
  LABEL maintainer="team@company.com"
  LABEL version="1.0"
  
  # ENV - Environment variables
  ENV APP_HOME=/app
  ENV JAVA_OPTS="-Xmx512m"
  
  # ARG - Build-time variables
  ARG VERSION=1.0.0
  ARG BUILD_DATE
  
  # WORKDIR - Set working directory
  WORKDIR /app
  
  # COPY - Copy files from host to image
  COPY target/app.jar app.jar
  COPY config/ /app/config/
  
  # ADD - Copy and extract (use COPY instead)
  ADD archive.tar.gz /app/
  
  # RUN - Execute commands during build
  RUN apt-get update && apt-get install -y curl
  RUN mkdir -p /app/logs
  
  # EXPOSE - Document port (doesn't publish)
  EXPOSE 8080
  EXPOSE 8443
  
  # VOLUME - Create mount point
  VOLUME ["/app/data", "/app/logs"]
  
  # USER - Switch user
  USER appuser
  
  # HEALTHCHECK - Container health check
  HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/health || exit 1
  
  # CMD - Default command (can be overridden)
  CMD ["java", "-jar", "app.jar"]
  
  # ENTRYPOINT - Main command (not easily overridden)
  ENTRYPOINT ["java", "-jar", "app.jar"]
  
  # ENTRYPOINT + CMD (CMD becomes default args)
  ENTRYPOINT ["java"]
  CMD ["-jar", "app.jar"]
  ```

  **Build Arguments:**

  ```dockerfile
  FROM openjdk:17-jdk-slim
  
  # Build arguments
  ARG VERSION=1.0.0
  ARG BUILD_DATE
  ARG GIT_COMMIT
  
  # Use in labels
  LABEL version="${VERSION}"
  LABEL build-date="${BUILD_DATE}"
  LABEL git-commit="${GIT_COMMIT}"
  
  WORKDIR /app
  
  # Use in file names
  COPY target/myapp-${VERSION}.jar app.jar
  
  EXPOSE 8080
  
  CMD ["java", "-jar", "app.jar"]
  ```

  ```bash
  # Build with arguments
  docker build \
    --build-arg VERSION=1.2.3 \
    --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ') \
    --build-arg GIT_COMMIT=$(git rev-parse HEAD) \
    -t myapp:1.2.3 .
  ```

</details>

<details>
  <summary>Docker Volumes</summary>
  <br/>

  **Volumes** persist data outside containers.

  **Volume Types:**

  ```
  1. Named Volumes (Managed by Docker)
  ┌─────────────────────────────────┐
  │  Container                      │
  │  /app/data ──▶ my-data-volume  │
  └─────────────────────────────────┘
  
  2. Bind Mounts (Host directory)
  ┌─────────────────────────────────┐
  │  Container                      │
  │  /app/data ──▶ /host/path      │
  └─────────────────────────────────┘
  
  3. tmpfs (Memory only)
  ┌─────────────────────────────────┐
  │  Container                      │
  │  /app/temp ──▶ RAM             │
  └─────────────────────────────────┘
  ```

  **Named Volumes:**

  ```bash
  # Create volume
  docker volume create my-data
  
  # List volumes
  docker volume ls
  
  # Inspect volume
  docker volume inspect my-data
  
  # Remove volume
  docker volume rm my-data
  
  # Remove unused volumes
  docker volume prune
  
  # Use volume in container
  docker run -v my-data:/app/data myapp
  
  # Read-only volume
  docker run -v my-data:/app/data:ro myapp
  ```

  **Bind Mounts:**

  ```bash
  # Mount host directory
  docker run -v /host/path:/container/path myapp
  
  # Current directory
  docker run -v $(pwd):/app myapp
  
  # Read-only
  docker run -v /host/path:/container/path:ro myapp
  
  # Example: Development with hot reload
  docker run -d \
    -p 8080:8080 \
    -v $(pwd)/src:/app/src \
    -v $(pwd)/target:/app/target \
    myapp-dev
  ```

  **Docker Compose Volumes:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      volumes:
        # Named volume
        - app-data:/app/data
        
        # Bind mount
        - ./config:/app/config:ro
        
        # Anonymous volume
        - /app/temp
    
    postgres:
      image: postgres:15
      volumes:
        # Named volume for database
        - postgres-data:/var/lib/postgresql/data
        
        # Init scripts
        - ./init.sql:/docker-entrypoint-initdb.d/init.sql:ro
  
  volumes:
    app-data:
      driver: local
    postgres-data:
      driver: local
  ```

  **Volume in Dockerfile:**

  ```dockerfile
  FROM openjdk:17-jdk-slim
  
  WORKDIR /app
  
  # Declare volume mount points
  VOLUME ["/app/data", "/app/logs"]
  
  COPY target/app.jar app.jar
  
  CMD ["java", "-jar", "app.jar"]
  ```

  **Backup and Restore:**

  ```bash
  # Backup volume
  docker run --rm \
    -v my-data:/data \
    -v $(pwd):/backup \
    ubuntu tar czf /backup/backup.tar.gz /data
  
  # Restore volume
  docker run --rm \
    -v my-data:/data \
    -v $(pwd):/backup \
    ubuntu tar xzf /backup/backup.tar.gz -C /
  
  # Copy files from container
  docker cp container_id:/app/data ./backup/
  
  # Copy files to container
  docker cp ./config container_id:/app/config/
  ```

</details>

<details>
  <summary>Docker Networks</summary>
  <br/>

  **Networks** enable container communication.

  **Network Types:**

  ```
  1. Bridge (Default)
  ┌─────────────────────────────────────┐
  │  Docker Host                        │
  │  ┌──────────┐      ┌──────────┐   │
  │  │Container1│◀────▶│Container2│   │
  │  └──────────┘      └──────────┘   │
  │         │              │           │
  │         └──────┬───────┘           │
  │             Bridge                 │
  └─────────────────────────────────────┘
  
  2. Host (Share host network)
  ┌─────────────────────────────────────┐
  │  Container uses host network stack  │
  └─────────────────────────────────────┘
  
  3. None (No network)
  ┌─────────────────────────────────────┐
  │  Container isolated                 │
  └─────────────────────────────────────┘
  ```

  **Network Commands:**

  ```bash
  # List networks
  docker network ls
  
  # Create network
  docker network create my-network
  
  # Create network with subnet
  docker network create --subnet=172.18.0.0/16 my-network
  
  # Inspect network
  docker network inspect my-network
  
  # Connect container to network
  docker network connect my-network container_id
  
  # Disconnect container from network
  docker network disconnect my-network container_id
  
  # Remove network
  docker network rm my-network
  
  # Remove unused networks
  docker network prune
  ```

  **Using Networks:**

  ```bash
  # Create network
  docker network create app-network
  
  # Run containers on same network
  docker run -d --name postgres --network app-network postgres:15
  docker run -d --name app --network app-network -e DB_HOST=postgres myapp
  
  # Containers can communicate using container names as hostnames
  # app can connect to postgres using hostname "postgres"
  ```

  **Network in Docker Compose:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      networks:
        - frontend
        - backend
      environment:
        DB_HOST: postgres
        REDIS_HOST: redis
    
    postgres:
      image: postgres:15
      networks:
        - backend
      environment:
        POSTGRES_PASSWORD: secret
    
    redis:
      image: redis:7
      networks:
        - backend
    
    nginx:
      image: nginx:latest
      networks:
        - frontend
      ports:
        - "80:80"
  
  networks:
    frontend:
      driver: bridge
    backend:
      driver: bridge
      internal: true  # No external access
  ```

  **Network Aliases:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      networks:
        backend:
          aliases:
            - myapp
            - api
    
    postgres:
      image: postgres:15
      networks:
        backend:
          aliases:
            - db
            - database
  
  networks:
    backend:
  ```

  **Custom Network Configuration:**

  ```bash
  # Create network with custom settings
  docker network create \
    --driver bridge \
    --subnet 172.20.0.0/16 \
    --gateway 172.20.0.1 \
    --ip-range 172.20.240.0/20 \
    my-custom-network
  
  # Run container with static IP
  docker run -d \
    --name app \
    --network my-custom-network \
    --ip 172.20.0.10 \
    myapp
  ```

</details>

<details>
  <summary>Docker Compose</summary>
  <br/>

  **Docker Compose** manages multi-container applications.

  **Basic docker-compose.yml:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      ports:
        - "8080:8080"
      environment:
        - SPRING_PROFILES_ACTIVE=prod
        - DB_HOST=postgres
      depends_on:
        - postgres
        - redis
    
    postgres:
      image: postgres:15
      environment:
        POSTGRES_DB: mydb
        POSTGRES_USER: user
        POSTGRES_PASSWORD: password
      volumes:
        - postgres-data:/var/lib/postgresql/data
    
    redis:
      image: redis:7
      volumes:
        - redis-data:/data
  
  volumes:
    postgres-data:
    redis-data:
  ```

  **Complete Example:**

  ```yaml
  version: '3.8'
  
  services:
    # Application
    app:
      build:
        context: .
        dockerfile: Dockerfile
        args:
          VERSION: 1.0.0
      image: myapp:1.0
      container_name: myapp
      ports:
        - "8080:8080"
      environment:
        SPRING_PROFILES_ACTIVE: prod
        DB_HOST: postgres
        DB_PORT: 5432
        DB_NAME: mydb
        DB_USER: user
        DB_PASSWORD: password
        REDIS_HOST: redis
        REDIS_PORT: 6379
      volumes:
        - ./logs:/app/logs
        - app-data:/app/data
      networks:
        - backend
        - frontend
      depends_on:
        postgres:
          condition: service_healthy
        redis:
          condition: service_started
      restart: unless-stopped
      healthcheck:
        test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
        interval: 30s
        timeout: 3s
        retries: 3
        start_period: 40s
      deploy:
        resources:
          limits:
            cpus: '2.0'
            memory: 1G
          reservations:
            cpus: '1.0'
            memory: 512M
    
    # Database
    postgres:
      image: postgres:15-alpine
      container_name: postgres
      environment:
        POSTGRES_DB: mydb
        POSTGRES_USER: user
        POSTGRES_PASSWORD: password
        POSTGRES_INITDB_ARGS: "--encoding=UTF8"
      volumes:
        - postgres-data:/var/lib/postgresql/data
        - ./init.sql:/docker-entrypoint-initdb.d/init.sql:ro
      networks:
        - backend
      restart: unless-stopped
      healthcheck:
        test: ["CMD-SHELL", "pg_isready -U user -d mydb"]
        interval: 10s
        timeout: 5s
        retries: 5
      deploy:
        resources:
          limits:
            memory: 512M
    
    # Cache
    redis:
      image: redis:7-alpine
      container_name: redis
      command: redis-server --appendonly yes
      volumes:
        - redis-data:/data
      networks:
        - backend
      restart: unless-stopped
      healthcheck:
        test: ["CMD", "redis-cli", "ping"]
        interval: 10s
        timeout: 3s
        retries: 3
    
    # Reverse Proxy
    nginx:
      image: nginx:alpine
      container_name: nginx
      ports:
        - "80:80"
        - "443:443"
      volumes:
        - ./nginx.conf:/etc/nginx/nginx.conf:ro
        - ./ssl:/etc/nginx/ssl:ro
      networks:
        - frontend
      depends_on:
        - app
      restart: unless-stopped
  
  volumes:
    postgres-data:
      driver: local
    redis-data:
      driver: local
    app-data:
      driver: local
  
  networks:
    frontend:
      driver: bridge
    backend:
      driver: bridge
      internal: true
  ```

  **Docker Compose Commands:**

  ```bash
  # Start services
  docker-compose up
  
  # Start in background
  docker-compose up -d
  
  # Build and start
  docker-compose up --build
  
  # Stop services
  docker-compose stop
  
  # Stop and remove containers
  docker-compose down
  
  # Stop and remove containers, volumes
  docker-compose down -v
  
  # View logs
  docker-compose logs
  docker-compose logs -f app
  
  # List services
  docker-compose ps
  
  # Execute command in service
  docker-compose exec app bash
  
  # Scale service
  docker-compose up -d --scale app=3
  
  # Restart service
  docker-compose restart app
  
  # Pull images
  docker-compose pull
  
  # Build images
  docker-compose build
  
  # Validate compose file
  docker-compose config
  ```

  **Environment Files:**

  ```yaml
  # docker-compose.yml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      env_file:
        - .env
        - .env.prod
      environment:
        # Override specific variables
        LOG_LEVEL: INFO
  ```

  ```bash
  # .env
  DB_HOST=postgres
  DB_PORT=5432
  DB_NAME=mydb
  DB_USER=user
  DB_PASSWORD=secret
  REDIS_HOST=redis
  REDIS_PORT=6379
  ```

  **Profiles:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      image: myapp:1.0
      profiles: ["prod"]
    
    app-dev:
      build: .
      profiles: ["dev"]
      volumes:
        - ./src:/app/src
    
    postgres:
      image: postgres:15
      # Always runs
    
    pgadmin:
      image: dpage/pgadmin4
      profiles: ["dev"]
      ports:
        - "5050:80"
  ```

  ```bash
  # Run with profile
  docker-compose --profile dev up
  docker-compose --profile prod up
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  **1. Multi-Stage Builds:**

  ```dockerfile
  # ✅ DO: Use multi-stage builds
  # Stage 1: Build
  FROM maven:3.8-openjdk-17 AS build
  WORKDIR /app
  COPY pom.xml .
  RUN mvn dependency:go-offline
  COPY src ./src
  RUN mvn clean package -DskipTests
  
  # Stage 2: Runtime (smaller image)
  FROM openjdk:17-jdk-slim
  WORKDIR /app
  COPY --from=build /app/target/*.jar app.jar
  CMD ["java", "-jar", "app.jar"]
  
  # Result: Build tools not in final image (smaller, more secure)
  ```

  **2. Layer Caching:**

  ```dockerfile
  # ✅ DO: Order instructions for better caching
  FROM openjdk:17-jdk-slim
  
  WORKDIR /app
  
  # 1. Copy dependency file first (changes rarely)
  COPY pom.xml .
  RUN mvn dependency:go-offline
  
  # 2. Copy source code (changes frequently)
  COPY src ./src
  
  # 3. Build
  RUN mvn clean package
  
  # ❌ DON'T: Copy everything at once
  # COPY . .  # Any file change invalidates all layers
  ```

  **3. Minimize Image Size:**

  ```dockerfile
  # ✅ DO: Use slim/alpine images
  FROM openjdk:17-jdk-slim  # ~400MB
  # or
  FROM openjdk:17-jdk-alpine  # ~150MB
  
  # ❌ DON'T: Use full images
  # FROM openjdk:17-jdk  # ~800MB
  
  # ✅ DO: Clean up in same layer
  RUN apt-get update && \
      apt-get install -y curl && \
      rm -rf /var/lib/apt/lists/*
  
  # ❌ DON'T: Clean up in separate layer
  # RUN apt-get update
  # RUN apt-get install -y curl
  # RUN rm -rf /var/lib/apt/lists/*  # Doesn't reduce image size
  
  # ✅ DO: Use .dockerignore
  # .dockerignore file:
  # target/
  # .git/
  # .idea/
  # *.md
  # .env
  ```

  **4. Security:**

  ```dockerfile
  # ✅ DO: Run as non-root user
  FROM openjdk:17-jdk-slim
  
  # Create user
  RUN groupadd -r appuser && useradd -r -g appuser appuser
  
  WORKDIR /app
  COPY target/app.jar app.jar
  
  # Change ownership
  RUN chown -R appuser:appuser /app
  
  # Switch to non-root
  USER appuser
  
  CMD ["java", "-jar", "app.jar"]
  
  # ❌ DON'T: Run as root
  # USER root  # Default, insecure
  
  # ✅ DO: Scan for vulnerabilities
  # docker scan myapp:1.0
  # trivy image myapp:1.0
  
  # ✅ DO: Use specific versions
  FROM openjdk:17.0.8-jdk-slim
  
  # ❌ DON'T: Use latest tag
  # FROM openjdk:latest  # Unpredictable
  ```

  **5. Health Checks:**

  ```dockerfile
  # ✅ DO: Add health checks
  FROM openjdk:17-jdk-slim
  
  WORKDIR /app
  COPY target/app.jar app.jar
  
  # Health check
  HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
  
  CMD ["java", "-jar", "app.jar"]
  ```

  **6. Environment Variables:**

  ```dockerfile
  # ✅ DO: Use ENV for configuration
  FROM openjdk:17-jdk-slim
  
  ENV JAVA_OPTS="-Xmx512m -Xms256m" \
      APP_PORT=8080 \
      LOG_LEVEL=INFO
  
  WORKDIR /app
  COPY target/app.jar app.jar
  
  EXPOSE ${APP_PORT}
  
  CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
  
  # ❌ DON'T: Hardcode secrets
  # ENV DB_PASSWORD=secret123  # Never do this!
  
  # ✅ DO: Pass secrets at runtime
  # docker run -e DB_PASSWORD=secret myapp
  ```

  **7. Metadata:**

  ```dockerfile
  # ✅ DO: Add labels
  FROM openjdk:17-jdk-slim
  
  LABEL maintainer="team@company.com" \
        version="1.0.0" \
        description="My Spring Boot Application" \
        org.opencontainers.image.source="https://github.com/company/app" \
        org.opencontainers.image.licenses="MIT"
  
  WORKDIR /app
  COPY target/app.jar app.jar
  
  CMD ["java", "-jar", "app.jar"]
  ```

  **8. Efficient Copying:**

  ```dockerfile
  # ✅ DO: Copy only what's needed
  FROM openjdk:17-jdk-slim
  
  WORKDIR /app
  
  # Copy specific files
  COPY target/app.jar app.jar
  COPY config/ /app/config/
  
  # ❌ DON'T: Copy everything
  # COPY . .  # Includes unnecessary files
  
  # ✅ DO: Use .dockerignore
  # .dockerignore:
  # .git
  # .idea
  # target/
  # *.md
  # Dockerfile
  ```

  **9. Build Arguments:**

  ```dockerfile
  # ✅ DO: Use build args for flexibility
  FROM openjdk:17-jdk-slim
  
  ARG VERSION=1.0.0
  ARG BUILD_DATE
  ARG GIT_COMMIT
  
  LABEL version="${VERSION}" \
        build-date="${BUILD_DATE}" \
        git-commit="${GIT_COMMIT}"
  
  WORKDIR /app
  COPY target/myapp-${VERSION}.jar app.jar
  
  CMD ["java", "-jar", "app.jar"]
  ```

  ```bash
  # Build with arguments
  docker build \
    --build-arg VERSION=1.2.3 \
    --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ') \
    --build-arg GIT_COMMIT=$(git rev-parse HEAD) \
    -t myapp:1.2.3 .
  ```

  **10. Complete Best Practice Example:**

  ```dockerfile
  # Multi-stage build
  FROM maven:3.8-openjdk-17-slim AS build
  
  WORKDIR /build
  
  # Copy pom.xml first for layer caching
  COPY pom.xml .
  RUN mvn dependency:go-offline
  
  # Copy source and build
  COPY src ./src
  RUN mvn clean package -DskipTests
  
  # Runtime stage
  FROM openjdk:17-jdk-slim
  
  # Metadata
  LABEL maintainer="team@company.com" \
        version="1.0.0"
  
  # Install curl for health check
  RUN apt-get update && \
      apt-get install -y --no-install-recommends curl && \
      rm -rf /var/lib/apt/lists/*
  
  # Create non-root user
  RUN groupadd -r appuser && \
      useradd -r -g appuser appuser
  
  # Set working directory
  WORKDIR /app
  
  # Copy JAR from build stage
  COPY --from=build /build/target/*.jar app.jar
  
  # Change ownership
  RUN chown -R appuser:appuser /app
  
  # Switch to non-root user
  USER appuser
  
  # Expose port
  EXPOSE 8080
  
  # Health check
  HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
  
  # Environment variables
  ENV JAVA_OPTS="-Xmx512m -Xms256m"
  
  # Run application
  ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
  ```

  **Docker Compose Best Practices:**

  ```yaml
  version: '3.8'
  
  services:
    app:
      # ✅ DO: Use specific versions
      image: myapp:1.0.0
      
      # ✅ DO: Set container name
      container_name: myapp
      
      # ✅ DO: Use health checks
      healthcheck:
        test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
        interval: 30s
        timeout: 3s
        retries: 3
      
      # ✅ DO: Set restart policy
      restart: unless-stopped
      
      # ✅ DO: Set resource limits
      deploy:
        resources:
          limits:
            cpus: '2.0'
            memory: 1G
          reservations:
            cpus: '1.0'
            memory: 512M
      
      # ✅ DO: Use depends_on with conditions
      depends_on:
        postgres:
          condition: service_healthy
      
      # ✅ DO: Use named volumes
      volumes:
        - app-data:/app/data
      
      # ✅ DO: Use custom networks
      networks:
        - backend
      
      # ✅ DO: Use env files
      env_file:
        - .env
    
    postgres:
      image: postgres:15-alpine
      
      # ✅ DO: Add health check
      healthcheck:
        test: ["CMD-SHELL", "pg_isready -U user"]
        interval: 10s
        timeout: 5s
        retries: 5
      
      # ✅ DO: Use volumes for data
      volumes:
        - postgres-data:/var/lib/postgresql/data
      
      # ✅ DO: Use secrets for passwords
      environment:
        POSTGRES_PASSWORD_FILE: /run/secrets/db_password
      
      secrets:
        - db_password
  
  volumes:
    app-data:
    postgres-data:
  
  networks:
    backend:
      driver: bridge
  
  secrets:
    db_password:
      file: ./secrets/db_password.txt
  ```

  **Summary:**
  + **Multi-stage builds** - Smaller, more secure images
  + **Layer caching** - Order instructions for faster builds
  + **Minimize size** - Use slim/alpine images, clean up in same layer
  + **Security** - Run as non-root, scan for vulnerabilities, use specific versions
  + **Health checks** - Monitor container health
  + **Environment variables** - Configuration, never hardcode secrets
  + **Metadata** - Add labels for documentation
  + **.dockerignore** - Exclude unnecessary files
  + **Build arguments** - Flexible builds
  + **Named volumes** - Persist data
  + **Custom networks** - Isolate services
  + **Resource limits** - Prevent resource exhaustion
  + **Restart policies** - Automatic recovery

</details>

## Kubernetes - Orchestration

<details>
  <summary>Kubernetes Basics</summary>
  <br/>

  **Kubernetes (K8s)** orchestrates containerized applications across clusters.

  **Architecture:**

  ```
  ┌─────────────────────────────────────────────────────────┐
  │                   Kubernetes Cluster                    │
  ├─────────────────────────────────────────────────────────┤
  │                                                         │
  │  Control Plane (Master)                                 │
  │  ┌──────────────────────────────────────────────┐     │
  │  │ API Server │ Scheduler │ Controller Manager  │     │
  │  │            │ etcd (state storage)            │     │
  │  └──────────────────────────────────────────────┘     │
  │                        │                               │
  │  ──────────────────────┼───────────────────────────   │
  │                        │                               │
  │  Worker Nodes                                          │
  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
  │  │   Node 1    │  │   Node 2    │  │   Node 3    │  │
  │  │             │  │             │  │             │  │
  │  │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │  │
  │  │ │  Pod 1  │ │  │ │  Pod 2  │ │  │ │  Pod 3  │ │  │
  │  │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │  │
  │  │ ┌─────────┐ │  │ ┌─────────┐ │  │             │  │
  │  │ │  Pod 4  │ │  │ │  Pod 5  │ │  │             │  │
  │  │ └─────────┘ │  │ └─────────┘ │  │             │  │
  │  └─────────────┘  └─────────────┘  └─────────────┘  │
  └─────────────────────────────────────────────────────────┘
  ```

  **Key Concepts:**

  - **Pod**: Smallest deployable unit (one or more containers)
  - **Service**: Stable network endpoint for pods
  - **Deployment**: Manages pod replicas and updates
  - **ConfigMap**: Configuration data
  - **Secret**: Sensitive data (passwords, tokens)
  - **Namespace**: Virtual cluster for resource isolation

  **Basic Commands:**

  ```bash
  # Cluster info
  kubectl cluster-info
  kubectl get nodes
  
  # Pods
  kubectl get pods
  kubectl get pods -n namespace
  kubectl describe pod pod-name
  kubectl logs pod-name
  kubectl logs -f pod-name  # Follow logs
  kubectl exec -it pod-name -- bash
  
  # Deployments
  kubectl get deployments
  kubectl describe deployment deployment-name
  kubectl scale deployment deployment-name --replicas=3
  kubectl rollout status deployment/deployment-name
  kubectl rollout history deployment/deployment-name
  kubectl rollout undo deployment/deployment-name
  
  # Services
  kubectl get services
  kubectl describe service service-name
  
  # ConfigMaps & Secrets
  kubectl get configmaps
  kubectl get secrets
  kubectl describe configmap configmap-name
  
  # Apply/Delete resources
  kubectl apply -f file.yaml
  kubectl delete -f file.yaml
  kubectl delete pod pod-name
  
  # Namespaces
  kubectl get namespaces
  kubectl create namespace my-namespace
  kubectl config set-context --current --namespace=my-namespace
  
  # Debug
  kubectl get events
  kubectl top nodes
  kubectl top pods
  ```

</details>

<details>
  <summary>Pods</summary>
  <br/>

  **Pod** is the smallest deployable unit in Kubernetes.

  **Basic Pod:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
    labels:
      app: myapp
      environment: production
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      ports:
      - containerPort: 8080
      env:
      - name: SPRING_PROFILES_ACTIVE
        value: "prod"
  ```

  **Pod with Resources:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      ports:
      - containerPort: 8080
      
      # Resource requests and limits
      resources:
        requests:
          memory: "256Mi"
          cpu: "500m"
        limits:
          memory: "512Mi"
          cpu: "1000m"
      
      # Environment variables
      env:
      - name: DB_HOST
        value: "postgres"
      - name: DB_PORT
        value: "5432"
      
      # Liveness probe (restart if fails)
      livenessProbe:
        httpGet:
          path: /actuator/health/liveness
          port: 8080
        initialDelaySeconds: 30
        periodSeconds: 10
        timeoutSeconds: 3
        failureThreshold: 3
      
      # Readiness probe (remove from service if fails)
      readinessProbe:
        httpGet:
          path: /actuator/health/readiness
          port: 8080
        initialDelaySeconds: 20
        periodSeconds: 5
        timeoutSeconds: 3
        failureThreshold: 3
  ```

  **Multi-Container Pod:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    # Main application
    - name: myapp
      image: myapp:1.0
      ports:
      - containerPort: 8080
      volumeMounts:
      - name: shared-logs
        mountPath: /app/logs
    
    # Sidecar: Log shipper
    - name: log-shipper
      image: fluent-bit:latest
      volumeMounts:
      - name: shared-logs
        mountPath: /logs
      env:
      - name: ELASTICSEARCH_HOST
        value: "elasticsearch:9200"
    
    # Shared volume
    volumes:
    - name: shared-logs
      emptyDir: {}
  ```

  **Init Containers:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    # Init containers run before main containers
    initContainers:
    - name: wait-for-db
      image: busybox:latest
      command: ['sh', '-c', 'until nc -z postgres 5432; do echo waiting for db; sleep 2; done']
    
    - name: migration
      image: myapp:1.0
      command: ['java', '-jar', 'app.jar', '--migrate']
      env:
      - name: DB_HOST
        value: "postgres"
    
    containers:
    - name: myapp
      image: myapp:1.0
      ports:
      - containerPort: 8080
  ```

</details>

<details>
  <summary>Services</summary>
  <br/>

  **Service** provides stable network endpoint for pods.

  **Service Types:**

  ```
  1. ClusterIP (Default)
  ┌─────────────────────────────────┐
  │  Internal IP (cluster only)     │
  │  Service: 10.0.0.1:80          │
  │     │                           │
  │     └──▶ Pod 1, Pod 2, Pod 3   │
  └─────────────────────────────────┘
  
  2. NodePort
  ┌─────────────────────────────────┐
  │  External access via node IP    │
  │  NodeIP:30080 ──▶ Service      │
  │                    │            │
  │                    └──▶ Pods    │
  └─────────────────────────────────┘
  
  3. LoadBalancer
  ┌─────────────────────────────────┐
  │  Cloud load balancer            │
  │  External IP ──▶ Service        │
  │                   │             │
  │                   └──▶ Pods     │
  └─────────────────────────────────┘
  ```

  **ClusterIP Service (Internal):**

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: myapp-service
  spec:
    type: ClusterIP  # Default
    selector:
      app: myapp
    ports:
    - protocol: TCP
      port: 80        # Service port
      targetPort: 8080  # Container port
  ```

  **NodePort Service (External):**

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: myapp-service
  spec:
    type: NodePort
    selector:
      app: myapp
    ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
      nodePort: 30080  # External port (30000-32767)
  ```

  **LoadBalancer Service (Cloud):**

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: myapp-service
  spec:
    type: LoadBalancer
    selector:
      app: myapp
    ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  ```

  **Headless Service (Direct pod access):**

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: myapp-headless
  spec:
    clusterIP: None  # Headless
    selector:
      app: myapp
    ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  ```

  **Service with Multiple Ports:**

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: myapp-service
  spec:
    selector:
      app: myapp
    ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: 8080
    - name: https
      protocol: TCP
      port: 443
      targetPort: 8443
    - name: metrics
      protocol: TCP
      port: 9090
      targetPort: 9090
  ```

</details>

<details>
  <summary>Deployments</summary>
  <br/>

  **Deployment** manages pod replicas and rolling updates.

  **Basic Deployment:**

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: myapp-deployment
    labels:
      app: myapp
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: myapp
    template:
      metadata:
        labels:
          app: myapp
      spec:
        containers:
        - name: myapp
          image: myapp:1.0
          ports:
          - containerPort: 8080
  ```

  **Complete Deployment:**

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: myapp-deployment
    namespace: production
    labels:
      app: myapp
      version: v1
  spec:
    # Number of pod replicas
    replicas: 3
    
    # Rolling update strategy
    strategy:
      type: RollingUpdate
      rollingUpdate:
        maxSurge: 1        # Max pods above desired count
        maxUnavailable: 1  # Max pods unavailable during update
    
    # Minimum time pod should be ready
    minReadySeconds: 10
    
    # Revision history limit
    revisionHistoryLimit: 10
    
    # Pod selector
    selector:
      matchLabels:
        app: myapp
    
    # Pod template
    template:
      metadata:
        labels:
          app: myapp
          version: v1
      spec:
        containers:
        - name: myapp
          image: myapp:1.0
          imagePullPolicy: IfNotPresent
          
          ports:
          - name: http
            containerPort: 8080
            protocol: TCP
          
          # Environment variables
          env:
          - name: SPRING_PROFILES_ACTIVE
            value: "prod"
          - name: DB_HOST
            valueFrom:
              configMapKeyRef:
                name: myapp-config
                key: db.host
          - name: DB_PASSWORD
            valueFrom:
              secretKeyRef:
                name: myapp-secret
                key: db.password
          
          # Resource limits
          resources:
            requests:
              memory: "256Mi"
              cpu: "500m"
            limits:
              memory: "512Mi"
              cpu: "1000m"
          
          # Liveness probe
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
            timeoutSeconds: 3
            failureThreshold: 3
          
          # Readiness probe
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 20
            periodSeconds: 5
            timeoutSeconds: 3
            failureThreshold: 3
          
          # Volume mounts
          volumeMounts:
          - name: config
            mountPath: /app/config
            readOnly: true
          - name: data
            mountPath: /app/data
        
        # Volumes
        volumes:
        - name: config
          configMap:
            name: myapp-config
        - name: data
          persistentVolumeClaim:
            claimName: myapp-pvc
  ```

  **Deployment Commands:**

  ```bash
  # Create deployment
  kubectl apply -f deployment.yaml
  
  # Get deployments
  kubectl get deployments
  kubectl get deployment myapp-deployment
  
  # Describe deployment
  kubectl describe deployment myapp-deployment
  
  # Scale deployment
  kubectl scale deployment myapp-deployment --replicas=5
  
  # Update image
  kubectl set image deployment/myapp-deployment myapp=myapp:2.0
  
  # Rollout status
  kubectl rollout status deployment/myapp-deployment
  
  # Rollout history
  kubectl rollout history deployment/myapp-deployment
  
  # Rollback to previous version
  kubectl rollout undo deployment/myapp-deployment
  
  # Rollback to specific revision
  kubectl rollout undo deployment/myapp-deployment --to-revision=2
  
  # Pause rollout
  kubectl rollout pause deployment/myapp-deployment
  
  # Resume rollout
  kubectl rollout resume deployment/myapp-deployment
  
  # Delete deployment
  kubectl delete deployment myapp-deployment
  ```

  **Update Strategies:**

  ```yaml
  # Rolling Update (Default)
  spec:
    strategy:
      type: RollingUpdate
      rollingUpdate:
        maxSurge: 1
        maxUnavailable: 1
  
  # Recreate (All pods killed before new ones created)
  spec:
    strategy:
      type: Recreate
  ```

</details>

<details>
  <summary>ConfigMaps</summary>
  <br/>

  **ConfigMap** stores non-sensitive configuration data.

  **Create ConfigMap from Literal:**

  ```bash
  kubectl create configmap myapp-config \
    --from-literal=db.host=postgres \
    --from-literal=db.port=5432 \
    --from-literal=app.name=myapp
  ```

  **Create ConfigMap from File:**

  ```bash
  # From properties file
  kubectl create configmap myapp-config \
    --from-file=application.properties
  
  # From multiple files
  kubectl create configmap myapp-config \
    --from-file=config/
  ```

  **ConfigMap YAML:**

  ```yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: myapp-config
    namespace: production
  data:
    # Key-value pairs
    db.host: "postgres"
    db.port: "5432"
    app.name: "myapp"
    log.level: "INFO"
    
    # File content
    application.properties: |
      spring.datasource.url=jdbc:postgresql://postgres:5432/mydb
      spring.datasource.username=user
      spring.jpa.hibernate.ddl-auto=validate
      logging.level.root=INFO
    
    nginx.conf: |
      server {
        listen 80;
        server_name myapp.com;
        location / {
          proxy_pass http://myapp:8080;
        }
      }
  ```

  **Use ConfigMap in Pod (Environment Variables):**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      
      # Single environment variable
      env:
      - name: DB_HOST
        valueFrom:
          configMapKeyRef:
            name: myapp-config
            key: db.host
      
      # All keys as environment variables
      envFrom:
      - configMapRef:
          name: myapp-config
  ```

  **Use ConfigMap as Volume:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      volumeMounts:
      - name: config
        mountPath: /app/config
        readOnly: true
    
    volumes:
    - name: config
      configMap:
        name: myapp-config
        items:
        - key: application.properties
          path: application.properties
  ```

  **Update ConfigMap:**

  ```bash
  # Edit ConfigMap
  kubectl edit configmap myapp-config
  
  # Replace ConfigMap
  kubectl replace -f configmap.yaml
  
  # Delete and recreate
  kubectl delete configmap myapp-config
  kubectl apply -f configmap.yaml
  
  # Note: Pods need restart to pick up changes
  kubectl rollout restart deployment/myapp-deployment
  ```

</details>

<details>
  <summary>Secrets</summary>
  <br/>

  **Secret** stores sensitive data (passwords, tokens, keys).

  **Create Secret from Literal:**

  ```bash
  kubectl create secret generic myapp-secret \
    --from-literal=db.password=secret123 \
    --from-literal=api.key=abc123xyz
  ```

  **Create Secret from File:**

  ```bash
  # From file
  kubectl create secret generic myapp-secret \
    --from-file=ssh-privatekey=~/.ssh/id_rsa \
    --from-file=ssh-publickey=~/.ssh/id_rsa.pub
  
  # TLS secret
  kubectl create secret tls myapp-tls \
    --cert=path/to/cert.crt \
    --key=path/to/key.key
  
  # Docker registry secret
  kubectl create secret docker-registry regcred \
    --docker-server=registry.company.com \
    --docker-username=user \
    --docker-password=password \
    --docker-email=user@company.com
  ```

  **Secret YAML:**

  ```yaml
  apiVersion: v1
  kind: Secret
  metadata:
    name: myapp-secret
    namespace: production
  type: Opaque
  data:
    # Base64 encoded values
    db.password: c2VjcmV0MTIz  # echo -n 'secret123' | base64
    api.key: YWJjMTIzeHl6      # echo -n 'abc123xyz' | base64
  
  # Or use stringData (automatically encoded)
  stringData:
    db.password: secret123
    api.key: abc123xyz
  ```

  **Use Secret in Pod (Environment Variables):**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      
      # Single environment variable
      env:
      - name: DB_PASSWORD
        valueFrom:
          secretKeyRef:
            name: myapp-secret
            key: db.password
      
      # All keys as environment variables
      envFrom:
      - secretRef:
          name: myapp-secret
  ```

  **Use Secret as Volume:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: myapp:1.0
      volumeMounts:
      - name: secret
        mountPath: /app/secrets
        readOnly: true
    
    volumes:
    - name: secret
      secret:
        secretName: myapp-secret
        items:
        - key: db.password
          path: db-password.txt
  ```

  **Use Docker Registry Secret:**

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: myapp-pod
  spec:
    containers:
    - name: myapp
      image: registry.company.com/myapp:1.0
    
    # Pull image using secret
    imagePullSecrets:
    - name: regcred
  ```

  **TLS Secret for Ingress:**

  ```yaml
  apiVersion: networking.k8s.io/v1
  kind: Ingress
  metadata:
    name: myapp-ingress
  spec:
    tls:
    - hosts:
      - myapp.com
      secretName: myapp-tls
    rules:
    - host: myapp.com
      http:
        paths:
        - path: /
          pathType: Prefix
          backend:
            service:
              name: myapp-service
              port:
                number: 80
  ```

</details>

<details>
  <summary>Helm - Package Management</summary>
  <br/>

  **Helm** is a package manager for Kubernetes.

  **Helm Concepts:**

  ```
  Chart = Package (templates + values)
  Release = Installed instance of chart
  Repository = Collection of charts
  ```

  **Helm Commands:**

  ```bash
  # Add repository
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo add stable https://charts.helm.sh/stable
  
  # Update repositories
  helm repo update
  
  # Search charts
  helm search repo nginx
  helm search hub wordpress
  
  # Install chart
  helm install my-release bitnami/nginx
  
  # Install with custom values
  helm install my-release bitnami/nginx -f values.yaml
  helm install my-release bitnami/nginx --set replicaCount=3
  
  # List releases
  helm list
  helm list -n namespace
  
  # Get release info
  helm status my-release
  helm get values my-release
  helm get manifest my-release
  
  # Upgrade release
  helm upgrade my-release bitnami/nginx -f values.yaml
  
  # Rollback release
  helm rollback my-release
  helm rollback my-release 1  # To specific revision
  
  # Uninstall release
  helm uninstall my-release
  
  # Create new chart
  helm create mychart
  
  # Package chart
  helm package mychart/
  
  # Lint chart
  helm lint mychart/
  
  # Dry run (test without installing)
  helm install my-release mychart/ --dry-run --debug
  ```

  **Chart Structure:**

  ```
  mychart/
  ├── Chart.yaml          # Chart metadata
  ├── values.yaml         # Default values
  ├── charts/             # Dependencies
  ├── templates/          # Kubernetes manifests
  │   ├── deployment.yaml
  │   ├── service.yaml
  │   ├── ingress.yaml
  │   ├── configmap.yaml
  │   ├── secret.yaml
  │   ├── _helpers.tpl    # Template helpers
  │   └── NOTES.txt       # Post-install notes
  └── .helmignore         # Files to ignore
  ```

  **Chart.yaml:**

  ```yaml
  apiVersion: v2
  name: myapp
  description: My Spring Boot Application
  type: application
  version: 1.0.0
  appVersion: "1.0.0"
  keywords:
    - spring-boot
    - java
  maintainers:
    - name: Team
      email: team@company.com
  dependencies:
    - name: postgresql
      version: 12.x.x
      repository: https://charts.bitnami.com/bitnami
  ```

  **values.yaml:**

  ```yaml
  # Default values
  replicaCount: 3
  
  image:
    repository: myapp
    tag: "1.0.0"
    pullPolicy: IfNotPresent
  
  service:
    type: ClusterIP
    port: 80
    targetPort: 8080
  
  ingress:
    enabled: true
    className: nginx
    hosts:
      - host: myapp.com
        paths:
          - path: /
            pathType: Prefix
    tls:
      - secretName: myapp-tls
        hosts:
          - myapp.com
  
  resources:
    requests:
      memory: "256Mi"
      cpu: "500m"
    limits:
      memory: "512Mi"
      cpu: "1000m"
  
  autoscaling:
    enabled: true
    minReplicas: 2
    maxReplicas: 10
    targetCPUUtilizationPercentage: 80
  
  env:
    SPRING_PROFILES_ACTIVE: prod
    LOG_LEVEL: INFO
  
  postgresql:
    enabled: true
    auth:
      username: user
      password: password
      database: mydb
  ```

  **templates/deployment.yaml:**

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: {{ include "myapp.fullname" . }}
    labels:
      {{- include "myapp.labels" . | nindent 4 }}
  spec:
    replicas: {{ .Values.replicaCount }}
    selector:
      matchLabels:
        {{- include "myapp.selectorLabels" . | nindent 6 }}
    template:
      metadata:
        labels:
          {{- include "myapp.selectorLabels" . | nindent 8 }}
      spec:
        containers:
        - name: {{ .Chart.Name }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          ports:
          - name: http
            containerPort: {{ .Values.service.targetPort }}
            protocol: TCP
          env:
          {{- range $key, $value := .Values.env }}
          - name: {{ $key }}
            value: {{ $value | quote }}
          {{- end }}
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: http
            initialDelaySeconds: 30
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: http
            initialDelaySeconds: 20
            periodSeconds: 5
  ```

  **templates/_helpers.tpl:**

  ```yaml
  {{/*
  Expand the name of the chart.
  */}}
  {{- define "myapp.name" -}}
  {{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
  {{- end }}
  
  {{/*
  Create a default fully qualified app name.
  */}}
  {{- define "myapp.fullname" -}}
  {{- if .Values.fullnameOverride }}
  {{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
  {{- else }}
  {{- $name := default .Chart.Name .Values.nameOverride }}
  {{- if contains $name .Release.Name }}
  {{- .Release.Name | trunc 63 | trimSuffix "-" }}
  {{- else }}
  {{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
  {{- end }}
  {{- end }}
  {{- end }}
  
  {{/*
  Common labels
  */}}
  {{- define "myapp.labels" -}}
  helm.sh/chart: {{ include "myapp.chart" . }}
  {{ include "myapp.selectorLabels" . }}
  {{- if .Chart.AppVersion }}
  app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
  {{- end }}
  app.kubernetes.io/managed-by: {{ .Release.Service }}
  {{- end }}
  
  {{/*
  Selector labels
  */}}
  {{- define "myapp.selectorLabels" -}}
  app.kubernetes.io/name: {{ include "myapp.name" . }}
  app.kubernetes.io/instance: {{ .Release.Name }}
  {{- end }}
  ```

  **Custom values file (values-prod.yaml):**

  ```yaml
  replicaCount: 5
  
  image:
    tag: "2.0.0"
  
  ingress:
    hosts:
      - host: myapp.production.com
        paths:
          - path: /
            pathType: Prefix
  
  resources:
    requests:
      memory: "512Mi"
      cpu: "1000m"
    limits:
      memory: "1Gi"
      cpu: "2000m"
  
  env:
    SPRING_PROFILES_ACTIVE: production
    LOG_LEVEL: WARN
  ```

  ```bash
  # Install with custom values
  helm install myapp-prod ./mychart -f values-prod.yaml
  ```

</details>

<details>
  <summary>Istio - Service Mesh (Bonus)</summary>
  <br/>

  **Istio** provides traffic management, security, and observability.

  **Istio Architecture:**

  ```
  ┌─────────────────────────────────────────────┐
  │           Istio Control Plane               │
  │  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
  │  │  Pilot   │  │  Citadel │  │  Galley  │ │
  │  │(Traffic) │  │(Security)│  │(Config)  │ │
  │  └──────────┘  └──────────┘  └──────────┘ │
  └─────────────────────────────────────────────┘
                      │
  ────────────────────┼────────────────────────
                      │
  ┌─────────────────────────────────────────────┐
  │              Data Plane                     │
  │  ┌──────────────┐      ┌──────────────┐   │
  │  │   Pod A      │      │   Pod B      │   │
  │  │ ┌──────────┐ │      │ ┌──────────┐ │   │
  │  │ │  Envoy   │ │◀────▶│ │  Envoy   │ │   │
  │  │ │ Sidecar  │ │      │ │ Sidecar  │ │   │
  │  │ └──────────┘ │      │ └──────────┘ │   │
  │  │ ┌──────────┐ │      │ ┌──────────┐ │   │
  │  │ │   App    │ │      │ │   App    │ │   │
  │  │ └──────────┘ │      │ └──────────┘ │   │
  │  └──────────────┘      └──────────────┘   │
  └─────────────────────────────────────────────┘
  ```

  **Enable Istio Injection:**

  ```bash
  # Label namespace for automatic sidecar injection
  kubectl label namespace default istio-injection=enabled
  
  # Verify
  kubectl get namespace -L istio-injection
  ```

  **VirtualService (Traffic Routing):**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: VirtualService
  metadata:
    name: myapp
  spec:
    hosts:
    - myapp.com
    gateways:
    - myapp-gateway
    http:
    # Canary deployment (90% v1, 10% v2)
    - match:
      - headers:
          user-agent:
            regex: ".*Chrome.*"
      route:
      - destination:
          host: myapp
          subset: v2
        weight: 10
      - destination:
          host: myapp
          subset: v1
        weight: 90
    
    # Default route
    - route:
      - destination:
          host: myapp
          subset: v1
  ```

  **DestinationRule (Load Balancing):**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: DestinationRule
  metadata:
    name: myapp
  spec:
    host: myapp
    trafficPolicy:
      loadBalancer:
        simple: ROUND_ROBIN
      connectionPool:
        tcp:
          maxConnections: 100
        http:
          http1MaxPendingRequests: 50
          http2MaxRequests: 100
      outlierDetection:
        consecutiveErrors: 5
        interval: 30s
        baseEjectionTime: 30s
    subsets:
    - name: v1
      labels:
        version: v1
    - name: v2
      labels:
        version: v2
  ```

  **Gateway (Ingress):**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: Gateway
  metadata:
    name: myapp-gateway
  spec:
    selector:
      istio: ingressgateway
    servers:
    - port:
        number: 80
        name: http
        protocol: HTTP
      hosts:
      - myapp.com
    - port:
        number: 443
        name: https
        protocol: HTTPS
      tls:
        mode: SIMPLE
        credentialName: myapp-tls
      hosts:
      - myapp.com
  ```

  **Circuit Breaker:**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: DestinationRule
  metadata:
    name: myapp
  spec:
    host: myapp
    trafficPolicy:
      connectionPool:
        tcp:
          maxConnections: 100
        http:
          http1MaxPendingRequests: 10
          maxRequestsPerConnection: 2
      outlierDetection:
        consecutiveErrors: 5
        interval: 30s
        baseEjectionTime: 30s
        maxEjectionPercent: 50
  ```

  **Retry Policy:**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: VirtualService
  metadata:
    name: myapp
  spec:
    hosts:
    - myapp
    http:
    - route:
      - destination:
          host: myapp
      retries:
        attempts: 3
        perTryTimeout: 2s
        retryOn: 5xx,reset,connect-failure,refused-stream
  ```

  **Timeout:**

  ```yaml
  apiVersion: networking.istio.io/v1beta1
  kind: VirtualService
  metadata:
    name: myapp
  spec:
    hosts:
    - myapp
    http:
    - route:
      - destination:
          host: myapp
      timeout: 10s
  ```

  **Mutual TLS:**

  ```yaml
  apiVersion: security.istio.io/v1beta1
  kind: PeerAuthentication
  metadata:
    name: default
    namespace: production
  spec:
    mtls:
      mode: STRICT  # Require mTLS for all services
  ```

  **Authorization Policy:**

  ```yaml
  apiVersion: security.istio.io/v1beta1
  kind: AuthorizationPolicy
  metadata:
    name: myapp-authz
    namespace: production
  spec:
    selector:
      matchLabels:
        app: myapp
    action: ALLOW
    rules:
    - from:
      - source:
          principals: ["cluster.local/ns/production/sa/frontend"]
      to:
      - operation:
          methods: ["GET", "POST"]
          paths: ["/api/*"]
  ```

</details>

<details>
  <summary>Best Practices</summary>
  <br/>

  ```yaml
  # ✅ DO: Use namespaces for isolation
  apiVersion: v1
  kind: Namespace
  metadata:
    name: production
  
  # ✅ DO: Set resource requests and limits
  resources:
    requests:
      memory: "256Mi"
      cpu: "500m"
    limits:
      memory: "512Mi"
      cpu: "1000m"
  
  # ✅ DO: Use liveness and readiness probes
  livenessProbe:
    httpGet:
      path: /actuator/health/liveness
      port: 8080
    initialDelaySeconds: 30
    periodSeconds: 10
  
  readinessProbe:
    httpGet:
      path: /actuator/health/readiness
      port: 8080
    initialDelaySeconds: 20
    periodSeconds: 5
  
  # ✅ DO: Use ConfigMaps and Secrets
  env:
  - name: DB_HOST
    valueFrom:
      configMapKeyRef:
        name: myapp-config
        key: db.host
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: myapp-secret
        key: db.password
  
  # ✅ DO: Use rolling updates
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
  
  # ✅ DO: Use HorizontalPodAutoscaler
  apiVersion: autoscaling/v2
  kind: HorizontalPodAutoscaler
  metadata:
    name: myapp-hpa
  spec:
    scaleTargetRef:
      apiVersion: apps/v1
      kind: Deployment
      name: myapp
    minReplicas: 2
    maxReplicas: 10
    metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 80
  
  # ✅ DO: Use PodDisruptionBudget
  apiVersion: policy/v1
  kind: PodDisruptionBudget
  metadata:
    name: myapp-pdb
  spec:
    minAvailable: 2
    selector:
      matchLabels:
        app: myapp
  
  # ✅ DO: Use labels and selectors
  metadata:
    labels:
      app: myapp
      version: v1
      environment: production
  
  # ❌ DON'T: Run as root
  # Use securityContext
  securityContext:
    runAsNonRoot: true
    runAsUser: 1000
    fsGroup: 1000
  
  # ❌ DON'T: Use latest tag
  # Use specific versions
  image: myapp:1.0.0
  
  # ❌ DON'T: Hardcode values
  # Use ConfigMaps and Secrets
  ```

  **Summary:**
  + **Pods** - Smallest deployable unit, containers + volumes
  + **Services** - Stable network endpoint (ClusterIP, NodePort, LoadBalancer)
  + **Deployments** - Manage replicas and rolling updates
  + **ConfigMaps** - Non-sensitive configuration
  + **Secrets** - Sensitive data (passwords, tokens)
  + **Helm** - Package manager for Kubernetes
  + **Istio** - Service mesh (traffic, security, observability)
  + Use **namespaces** for isolation
  + Set **resource limits** to prevent resource exhaustion
  + Use **probes** for health monitoring
  + Use **HPA** for auto-scaling
  + Use **PDB** for high availability
  + **Never run as root** in containers
  + Use **specific image tags**, not latest

</details>
