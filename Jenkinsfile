pipeline {
    agent any
    environment {
        registryCredential = "github.pixarv.jrecord"
        artifacts_branch = "artifacts_test"
    }
    stages {
        stage("Build project") {
            agent {
                docker {
                    image 'openjdk'
                }
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
                        stash includes: 'tmp/**', name: 'JRecord.tmp'
                        junit '**/build/test-results/test/TEST-*.xml'
                    }
                }
            }
        }
        stage('Push artifacts') {
            steps {
                withCredentials([sshUserPrivateKey(
                        credentialsId: "${registryCredential}",
                        keyFileVariable: 'keyfile')]) {

                    sh 'git config user.email "jenkins"'
                    sh 'git config user.name "jenkins"'

                    unstash 'JRecord.tmp'

                    sh "git ls-remote --exit-code artifactory || git remote add artifactory git@github.com:PixarV/artifactory.git"
                    sh "git fetch artifactory"
                    sh "git branch -a"

                    script {
                        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master") {
                            artifacts_branch = "artifacts"
                        }
                    }

                    sh "git checkout ${artifacts_branch} || git checkout -b ${artifacts_branch} artifactory/${artifacts_branch}"
                    sh "git pull artifactory ${artifacts_branch}"

                    sh "cp -r tmp/* repos/"

                    sh "git add repos/"
                    sh "git commit -m \"Jenkins build ${BUILD_ID} by branch ${BRANCH_NAME}\""
                    sh "git push git@github.com:PixarV/artifactory.git ${artifacts_branch}"
                }
            }
        }
    }
}
