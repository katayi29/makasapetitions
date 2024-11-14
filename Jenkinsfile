pipeline {
    agent any

    environment {
        // Define environment variables with project-specific paths and details
        MAVEN_HOME = '/opt/maven' 
        JAVA_HOME = '/opt/java' 
        DEPLOY_DIR = '/home/makasapetitions' 
        WAR_FILE = 'target/makasapetitions.war' 
        APPLICATION_NAME = 'makasapetitions'
        EC2_HOST = '13.51.163.206' 
        EC2_USER = 'ec2-user' 
        SSH_KEY_PATH = 'C:/Users/ADMIN/Downloads/makasakey.pem' 
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the repository
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    // Clean and build the project using Maven
                    sh "${MAVEN_HOME}/bin/mvn clean install"
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Run tests with Maven
                    sh "${MAVEN_HOME}/bin/mvn test"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Copy the WAR file to the EC2 instance
                    sh """
                    scp -i ${SSH_KEY_PATH} ${WAR_FILE} ${EC2_USER}@${EC2_HOST}:${DEPLOY_DIR}
                    """
                    
                    // SSH into the EC2 instance to deploy the WAR file
                    sh """
                    ssh -i ${SSH_KEY_PATH} ${EC2_USER}@${EC2_HOST} << EOF
                    # Stop the existing Tomcat service (if applicable)
                    sudo systemctl stop tomcat

                    # Backup existing WAR (optional)
                    mv ${DEPLOY_DIR}/makasapetitions.war ${DEPLOY_DIR}/makasapetitions.war.bak

                    # Move the new WAR file to the Tomcat webapps directory
                    sudo cp ${DEPLOY_DIR}/makasapetitions.war /opt/tomcat/webapps/

                    # Start the Tomcat service
                    sudo systemctl start tomcat
                    EOF
                    """
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    // Clean up the workspace
                    cleanWs()
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'There was an issue during the pipeline execution.'
        }
    }
}
