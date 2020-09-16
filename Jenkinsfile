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
                echo 'Building branch ${env.BRANCH_NAME}'
            }
        }

        stage('Build') {
            steps {
                gradlew('assemble')
            }
        }

        stage('Run-Unit-Tests') {
            steps {
                gradlew('test')
            }
        }

        stage('Build-Docker-Image') {
            steps {
                gradlew('bootBuildImage', 'vambita/status${env.BUILD_ID}')
            }
        }
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
