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
    }

    stages {
        stage ('Prepare-For-Build') {
            steps {
                echo 'Using Java ${JAVA_NAME}'
                sh '$JAVA_HOME/bin/java  -version'
                echo 'Building branch ${BRANCH_NAME}'
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
                    withCredentials([string(credentialsId: 'sonarqube-token', variable: 'TOKEN')]) {
                        steps {
                            gradlew('-Dsonar.host.url=http://sonar-qube:9000', '-Dsonar.login=${TOKEN}', 'sonarqube')
                        }
                    }
                }
            }
        }

        stage('Build-Docker-Image') {
            steps {
                gradlew('bootBuildImage', 'vambita/status${env.BUILD_ID}') //-- send to oc registry
            }
        }

        stage('Deploy') {
            steps {
                gradlew('bootBuildImage', 'vambita/status${env.BUILD_ID}') //-- send to oc registry
            }
        }
    }

}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')}"
}
