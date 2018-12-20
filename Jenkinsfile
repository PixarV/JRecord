pipeline {
    agent any
    tools {
        jdk 'Java11'
    }
    environment {
        JAVA_HOME = "${jdk}"
    }
    stages {
        stage("Fetch origin") {
            steps {
                sh 'git remote remove origin'
                sh 'git remote add origin https://github.com/PixarV/jrecord.git'
                sh 'git fetch origin'
                sh 'git branch -a'
            }
        }
        stage("Pre-build") {
            steps {
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
        stage('Push artifacts') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'brainydamage',
                        passwordVariable: 'GIT_PASSWORD',
                        usernameVariable: 'GIT_USERNAME')]) {

                    sh 'git config user.email "${GIT_USERNAME}@gmail.com"'
                    sh 'git config user.name "${GIT_USERNAME}"'

                    sh 'git config credential.${origin}.username ${GIT_USERNAME}'
                    sh 'git config credential.${origin}.password ${GIT_PASSWORD}'

                    sh 'rm -rf tmp/ && mv repos/ tmp/'

                    sh 'git branch -a'

                    sh 'git checkout artifacts_test || git checkout -b artifacts_test origin/artifacts_test'
                    sh 'git pull origin artifacts'

                    sh 'chmod 777 put_artifacts.sh'
                    sh './put_artifacts.sh'

                    sh 'git add repos/'
                    sh 'git commit -m "Jenkins build ${BUILD_ID} by branch ${BRANCH_NAME}"'
                    sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/PixarV/jrecord.git artifacts'
                }
            }
        }
    }
}
