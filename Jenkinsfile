pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                sh 'cd authentication-service && mvn clean package -DskipTests'
                sh 'cd note-service && mvn clean package -DskipTests'
                sh 'cd gateway && mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker compose build'
            }
        }

        stage('Copy JARs') {
            steps {
                sh 'cp $WORKSPACE/authentication-service/target/*.jar /home/ubuntu/note-app-services/authentication-service/target/'
                sh 'cp $WORKSPACE/note-service/target/*.jar /home/ubuntu/note-app-services/note-service/target/'
                sh 'cp $WORKSPACE/api-gateway/target/*.jar /home/ubuntu/note-app-services/gateway/target/'
            }
        }

        stage('Deploy') {
            steps {
                sh 'cp /var/lib/jenkins/workspace/Notes-App-Pipeline/authentication-service/target/*.jar '
                sh 'docker compose up -d'
            }
        }

    }
}