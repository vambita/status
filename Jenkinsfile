pipeline {

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
                echo "jdk path: ${env.JAVA_HOME}"
                echo 'Building branch ${env.BRANCH_NAME}'
                sh 'java -version'
            }
        }

        stage('Compile') {
            steps {
                gradlew('clean', 'classes')
            }
        }

        stage('Assemble') {
            steps {
                gradlew('assemble')
                stash includes: '**/build/libs/*.war', name: 'vambita-status'
            }
        }

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

        stage('Verification') {
            environment {
                TEST_ITEM = '123'
            }
            parallel {
                stage('Integration Tests') {
                    steps {
                        gradlew('integrationTest')
                    }
                    post {
                        always {
                            junit '**/build/test-results/integrationTest/TEST-*.xml'
                        }
                    }
                }
                stage('Code Analysis') {
                    steps {
                        gradlew('sonarqube')
                    }
                }
            }
        }

        stage('Build-Docker-Image') {
            steps {
                gradlew('bootBuildImage', 'vambita/status${env.BUILD_ID}')
            }
        }
    }

    post {
        failure {
            mail to: 'sipatha@vambita.com', subject: 'Build failed', body: 'Please fix!'
        }
    }
}

def gradlew(String... args) {
    sh './gradlew ${args.join(' ')} -s'
}
