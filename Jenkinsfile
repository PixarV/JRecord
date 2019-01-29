pipeline {
    agent {
        docker {
            image 'openjdk'
        }
    }
    environment {
        registryCredential = "github.pixarv"
    }
    stages {
        stage("Preparation") {
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
                sh './gradlew check -x compileJava'
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
                sh './gradlew assemble -x compileJava'
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
                /*withCredentials([usernamePassword(credentialsId: 'brainydamage',
                        passwordVariable: 'GIT_PASSWORD',
                        usernameVariable: 'GIT_USERNAME')])*/


/*                withCredentials([usernamePassword(credentialsId: "${registryCredential}",
                        usernameVariable: 'USERNAME',
                        passwordVariable: 'PASSWORD')])*/

                withCredentials([sshUserPrivateKey(
                        credentialsId: "${registryCredential}",
                        keyFileVariable: 'keyfile')]) {

//                    sh 'git config user.email "${GIT_USERNAME}@gmail.com"'
//                    sh 'git config user.name "${GIT_USERNAME}"'
//
//                    sh 'git config credential.${origin}.username ${GIT_USERNAME}'
//                    sh 'git config credential.${origin}.password ${GIT_PASSWORD}'

                    sh 'git ls-remote --exit-code artifactory || git remote add artifactory git@github.com/PixarV/artifactory.git'
                    sh 'git fetch artifactory'
                    sh 'git branch -a'

                    sh 'git checkout artifacts || git checkout -b artifacts artifactory/artifacts'
                    sh 'git pull artifactory artifacts'

                    sh 'cp -r tmp/* repos/'

                    sh 'git add repos/'
                    sh 'git commit -m "Jenkins build ${BUILD_ID} by branch ${BRANCH_NAME}"'
                    sh 'git push git@github.com/PixarV/artifactory.git artifacts'
                }
            }
        }
    }
}