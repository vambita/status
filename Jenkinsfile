pipeline {

    agent any

    triggers {
        pollSCM('*/2 * * * *')
    }

    tools {
        gradlew 'gradle'
    }

    options {
      disableConcurrentBuilds()
   }

    stages {
        stage('build') {
            steps {
                sh './gradlew assemble'
            }
        }

        stage('unit test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('docker-image') {
            steps {
                sh './gradlew bootBuildImage'
            }
        }
}
