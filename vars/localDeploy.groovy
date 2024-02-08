def call() {
    stage('Local Deploy') {
        when {
            expression { params.LOCAL_DEPLOY }
        }
        steps {
            script {
                input 'Deploy locally?'
                def containerName = 'my-nodejs-app'
                def containerExists = sh(script: "docker ps -a --format '{{.Names}}' | grep -q '^${containerName}\$'", returnStatus: true)

                if (containerExists == 0) {
                    echo "Container '${containerName}' exists"
                    sh "docker rm -f '${containerName}'"
                    echo "Remove container '${containerName}'"
                }
                
                sh "docker run -p 3000:3000 -d --name '${containerName}' --rm ${env.DOCKER_IMAGE_TAG}"
                echo 'Deploy Image Completed'
            }
        }
    }
}