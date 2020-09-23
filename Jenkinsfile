#!groovy

pipeline {

    //-- create a build number
    agent any

    triggers {
        pollSCM('*/2 * * * *')
    }

    options {
      disableConcurrentBuilds()
    }

    environment {
        JAVA_HOME="${ tool 'jdk-14.0.2' }"
        DOCKER_HOME="${ tool 'docker-latest' }"
        PATH = "$PATH:$JAVA_HOME/bin/:$DOCKER_HOME/bin"
    }

    stages {
        stage ('Prepare-For-Build') {
            steps {
                echo "Path      : $PATH"
                echo "Java      : $JAVA_NAME"
                echo "Building  : $BRANCH_NAME"
                sh "$JAVA_HOME/bin/java  -version"

            }
        }

        stage('Assemble') {
            steps {
                gradlew('clean', 'classes', 'assemble')
                stash includes: '**/build/libs/*.jar', name: 'vambita-status'
            }
        }

        stage('Verification') {
            environment {
                TEST_ITEM = '123'
            }
            parallel {
                stage('Run-Unit-Tests') {
                    steps {
                        gradlew('test')
                    }

                    post {
                        always {
                            junit '**/build/test-results/test/TEST-*.xml'
                        }
                    }
                }

                stage('Code Analysis') {
                    steps {
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'TOKEN')]) {
                            gradlew('-Dsonar.host.url=http://sonar-qube:9000', '-Dsonar.login=${TOKEN}', 'sonarqube')
                        }
                    }
                }
            }
        }

        stage('Build-Docker-Image') {
            steps {
                gradlew('docker')
            }
        }

        stage('Publish') {
            steps {
                gradlew('dockerPush')
            }
        }
    }

}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')}"
}
