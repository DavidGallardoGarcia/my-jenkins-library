def call(String dockerImageTag) {
    def containerName = 'my-nodejs-app'
    def containerExists = sh(script: "docker ps -a --format '{{.Names}}' | grep -q '^${containerName}\$'", returnStatus: true)

    if (containerExists == 0) {
        echo "Container '${containerName}' exists"
        sh "docker rm -f '${containerName}'"
        echo "Remove container '${containerName}'"
    }
    
    sh "docker run -p 3000:3000 -d --name '${containerName}' --rm ${dockerImageTag}"
    echo 'Deploy Image Completed'
}