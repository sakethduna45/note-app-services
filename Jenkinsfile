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

    }
}