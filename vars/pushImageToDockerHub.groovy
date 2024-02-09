def call(String dockerImageTag) {
    sh "docker push ${dockerImageTag}"
    echo 'Push Image Completed'
}