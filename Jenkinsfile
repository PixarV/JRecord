pipeline {
    agent any
    tools {
        jdk "${params.JDK_TOOL}"
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
                    branch 'develop'
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
        }
    }
}