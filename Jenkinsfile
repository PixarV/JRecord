node {
    jdk = tool name: 'Java11'
    env.JAVA_HOME = "${jdk}"
    stage("Pre-build") {
        checkout scm
        sh './gradlew clean'
    }
    stage("Compile project") {
        sh './gradlew compileJava'
    }
    stage("Test fast") {
        sh './gradlew test'
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
        sh './gradlew assemble'
    }
    stage('Result') {
         archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
         junit '**/build/test-results/test/TEST-*.xml'
    }
}
