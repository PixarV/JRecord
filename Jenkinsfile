import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

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
//        stage("Test fast") {
//            steps {
//                sh './gradlew test'
//            }
//        }
//        stage("Test slow") {
//            when {
//                branch 'master'
//            }
//            steps {
//                sh './gradlew slowTest'
//            }
//        }
        stage("Assemble") {
            steps {
                sh './gradlew assemble'
            }
        }
        stage('Publish artifacts and test results') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                sh './gradlew publish'
//                junit '**/build/test-results/test/TEST-*.xml'
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
                    sh 'git pull origin artifacts_test'

                    script {
                        putArtifacts()
                    }
                    sh 'ls'

                    sh 'git add repos/'
                    sh 'git commit -m "Jenkins build ${BUILD_ID} by branch ${BRANCH_NAME}"'
                    sh 'git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/PixarV/jrecord.git artifacts_test'
                }
            }
        }
    }
}

static void putArtifacts() {
    Path sourceDir = Paths.get("/var/jenkins_home/workspace/HRWD-497_Publish_with_versioning/tmp/net/sf/JRecord")

    for (File file : sourceDir.toFile().listFiles()) {
        println file.toString()

        new File(getTargetPath(file.toString()))
                .mkdirs()

        for (File artifact : file.listFiles()) {
            println artifact.toString()

            Path targetArtifact = Paths.get(getTargetPath(artifact.toString()))

            File targetArtifactFile = targetArtifact.toFile()
            println targetArtifactFile.exists()
            if (targetArtifactFile.exists()) {
                if (targetArtifactFile.isDirectory()) {
                    targetArtifactFile.deleteDir()
                } else {
                    targetArtifactFile.delete()
                }
            }
            println "before copy method"
            if(artifact.isDirectory()) {
                new File(getTargetPath(artifact.toString()))
                        .mkdirs()

                List<Path> artifacts = Files.walk(artifact.toPath())
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())
                for (int i = 0; i < artifacts.size(); i++) {
                    def source = artifacts.get(i)
                    if(!source.toFile().isDirectory()) {
                        Files.copy(source, Paths.get(
                                getTargetPath(source.toString())
                        ))
                    }
                }
            } else {
                Files.copy(artifact.toPath(), targetArtifact)
            }
            println "after copy method"
        }
    }

    sourceDir.toFile().deleteDir()
}

static String getTargetPath(String source) {
    return source
            .replace("tmp", "repos")
}