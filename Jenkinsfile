pipeline {

    agent any

    triggers {
        pollSCM('*/2 * * * *')
    }

    options {
      disableConcurrentBuilds()
   }

    stages {
        stage ("Prepare-For-Build") {
            tools {
               jdk "jdk-14.0.2"
            }
            steps {
                echo "jdk path: ${jdk}"
                sh 'java -version'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew assemble'
            }
        }

        stage('Run-Unit-Tests') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Build-Docker-Image') {
            steps {
                sh './gradlew bootBuildImage vambita/${}${env.BUILD_ID}'
            }
        }
    }
}
