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
//        stage("Compile project") {
//            steps {
//                sh './gradlew compileJava'
//            }
//        }
//        stage("Test fast") {
//            steps {
//                sh './gradlew test'
//            }
//        }
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
        stage('Result') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                sh './gradlew publish'
//                junit '**/build/test-results/test/TEST-*.xml'
            }
        }
        stage('Push artifacts') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'uliana_github',
                        passwordVariable: 'GIT_PASSWORD',
                        usernameVariable: 'GIT_USERNAME')]) {

                    sh 'git config user.email "${GIT_USERNAME}@gmail.com"'
                    sh 'git config user.name "{GIT_USERNAME}"'
                    sh 'git config user.password "{GIT_PASSWORD}"'

                    sh 'git branch -D artifacts'
                    sh 'git branch -a'
                    sh 'git checkout -b artifacts origin/artifacts'
//                            '|| git checkout -b artifacts origin/artifacts'
                    sh 'git add repos/'
                    sh 'git status'
                    sh 'git pull origin/artifacts'
                    sh 'git commit -m "Jenkins ${BUILD_ID}" -- repos/'
                    sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/PixarV/jrecord.git'

//                    sh 'git branch -D artifacts'
                }

            }
        }
    }
}