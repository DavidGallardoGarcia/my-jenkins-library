def call(String dockerImageTag) {
    sh "docker build -t ${dockerImageTag} ."
    echo "Build docker image ${dockerImageTag} Completed"
}