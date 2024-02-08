def call() {
    stage('Push Image to Docker Hub') {
        when {
            expression { params.PUSH_DOCKER_IMAGE }
        }
        steps {
            sh "docker push ${env.DOCKER_IMAGE_TAG}"
            echo 'Push Image Completed'
        }
    }
}