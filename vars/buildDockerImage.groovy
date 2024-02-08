def call() {
    stage('Build Docker Image') {
        steps {
            sh "docker build -t ${env.DOCKER_IMAGE_TAG} ."
            echo "Build docker image ${env.DOCKER_IMAGE_TAG} Completed"
        }
    }
}