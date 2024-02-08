def call(String dockerImageTag) {
    stage('Build Docker Image') {
        steps {
            sh "docker build -t ${dockerImageTag} ."
            echo "Build docker image ${dockerImageTag} Completed"
        }
    }
}