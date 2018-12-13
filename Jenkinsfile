pipeline {
    agent any
    tools {
        jdk 'Java11'
    }
    environment {
        JAVA_HOME = "${jdk}"
    }
    stages {
        stage("Pre-build") {
            steps {
                checkout scm
                sh './gradlew clean'
            }
        }
        stage("Compile project") {
            steps {
                sh './gradlew compileJava'
            }
        }
        stage("Test fast") {
            steps {
                sh './gradlew test'
            }
        }
        stage("Test slow") {
            when {
                branch 'master'
            }
            steps {
                sh './gradlew slowTest'
            }
        }
        stage("Assemble") {
            steps {
                sh './gradlew assemble'
            }
        }
        stage('Publish artifacts and test results') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                sh './gradlew publish'
                junit '**/build/test-results/test/TEST-*.xml'
            }
        }
//        stage("Clean local branch 'artifacts'") {
//            steps {
//                sh 'git branch -D artifacts &>/dev/null'
//                sh 'git fetch origin'
//                sh 'git checkout -b artifacts origin/artifacts'
//            }
//        }
      stage('Push artifacts') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'uliana_github',
                        passwordVariable: 'GIT_PASSWORD',
                        usernameVariable: 'GIT_USERNAME')]) {

                    sh 'rm -rf tmp/ && mv repos/ tmp/'

                    sh 'git branch -a'
                    sh 'git checkout artifacts'
                    sh 'git pull origin artifacts'

                    sh 'rm -rf repos/ && mv tmp/ repos/ && rm -rf tmp/'

                    sh 'git add repos/'
                    sh 'git commit -m "Jenkins ${BUILD_ID}"'
                    sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/PixarV/jrecord.git artifacts'
                }
            }
        }
    }
}