pipeline {
    agent any
//    def repo = "https://github.com/PixarV/jrecord.git"
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
        stage('Result') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                junit '**/build/test-results/test/TEST-*.xml'
            }
        }
        stage('Push artifacts') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'uliana_github')]) {
                    sh("git config user.name")
                }
            }
        }
    }
}