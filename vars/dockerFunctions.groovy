def buildDockerImage(String dockerImageTag) {
    sh "docker build -t ${dockerImageTag} ."
    echo "Build docker image ${dockerImageTag} Completed"
}

def pushDockerImage(String dockerImageTag) {
    sh "docker push ${dockerImageTag}"
    echo 'Push Image Completed'
}

def localDeployDockerImage(String dockerImageTag) {
    def configFile = 'app.properties'
    def config = readProperties file: configFile

    def containerName = config.containerName
    def ports = config.ports.split(',')

    def containerExists = sh(script: "docker ps -a --format '{{.Names}}' | grep -q '^${containerName}\$'", returnStatus: true)

    if (containerExists == 0) {
        echo "Container '${containerName}' exists"
        sh "docker rm -f '${containerName}'"
        echo "Remove container '${containerName}'"
    }

    def portMappings = ports.collect { "-p ${it}" }.join(' ')
    
    sh "docker run ${portMappings} -d --name ${containerName} --rm ${dockerImageTag}"
    echo 'Deploy Image Completed'
}

def k8sDeploy(String dockerImageTag, String kubeconfig) {
    def deploymentYaml = 'kubernetes/node-deployment.yaml'
    def serviceYaml = 'kubernetes/node-svc.yaml'

    echo "Deploying resources"
    sh "envsubst < '${deploymentYaml}' | kubectl --kubeconfig=${kubeconfig} apply -f -"
    sh "kubectl --kubeconfig=$KUBECONFIG apply -f '${serviceYaml}'"
}