pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                bat 'cd authentication-service && mvn clean package -DskipTests'
                bat 'cd note-service && mvn clean package -DskipTests'
                bat 'cd api-gateway && mvn clean package -DskipTests'
            }
        }   

    }
}