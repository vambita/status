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
        PATH = "$PATH:$JAVA_HOME/bin/"
        CUSTOM_DOCKER_REGISTRY = sh (
            script: 'docker inspect -f \'{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}\' local-nexus',
            returnStdout: true
        ).trim()
    }

    stages {
        stage ('Prepare-For-Build') {
            steps {
                echo "Path      : $PATH"
                echo "Java      : $JAVA_HOME"
                echo "Building  : $BRANCH_NAME"
                sh "$JAVA_HOME/bin/java  -version"
                sh "whoami"
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

        stage('Build & Publish') {
            when{
                branch 'master'
            }
            steps {
                script {
                    docker.withRegistry('http://${CUSTOM_DOCKER_REGISTRY}:8184', 'docker-registry-credentials') {
                        def theImage = docker.build("vambita/status", '--no-cache=true dockerbuild')
                        theImage.push("${env.BUILD_NUMBER}")
                        theImage.push("latest")
                    }
                }
            }
        }
    }

}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')}"
}
