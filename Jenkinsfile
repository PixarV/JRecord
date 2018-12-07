node {
    jdk = tool name: 'Java11'
    env.JAVA_HOME = "${jdk}"
    stage("checkout") {
        checkout scm
    }
    stage("Compile project") {
        sh './gradlew compileJava'
    }
    stage("Test fast") {
        sh './gradlew test'
    }
    stage("Assemble") {
        sh './gradlew assemble'
    }
    stage('Result') {
         archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
         junit '**/build/test-results/test/TEST-*.xml'
    }
}
