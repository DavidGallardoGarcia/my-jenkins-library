def buildDockerImage(String dockerImageTag) {
    sh "docker build -t ${dockerImageTag} ."
    echo "Build docker image ${dockerImageTag} Completed"
}

def pushDockerImage(String dockerImageTag) {
    sh "docker push ${dockerImageTag}"
    echo 'Push Image Completed'
}

def readProperties(String configFile) {
    def props = [:] //empty map
    configFile.split('\n').each { //read line by line
        def (key, value) = it.split('=') //split key value
        props[key.trim()] = value.trim() //concat key value
    }
    return props
}

def localDeployDockerImage(String dockerImageTag) {
    def props = readProperties('app.properties')
    
    def containerName = props.getProperty('containerName')
    def ports = props.getProperty('ports')

    def containerExists = sh(script: "docker ps -a --format '{{.Names}}' | grep -q '^${containerName}\$'", returnStatus: true)

    if (containerExists == 0) {
        echo "Container '${containerName}' exists"
        sh "docker rm -f '${containerName}'"
        echo "Remove container '${containerName}'"
    }

    // def portMappings = ports.collect { "-p ${it}" }.join(' ')
    
    sh "docker run ${ports} -d --name ${containerName} --rm ${dockerImageTag}"
    echo 'Deploy Image Completed'
}

def k8sDeploy(String dockerImageTag, String kubeconfig) {
    def deploymentYaml = 'kubernetes/node-deployment.yaml'
    def serviceYaml = 'kubernetes/node-svc.yaml'

    echo "Deploying resources"
    sh "envsubst < '${deploymentYaml}' | kubectl --kubeconfig=${kubeconfig} apply -f -"
    sh "kubectl --kubeconfig=$KUBECONFIG apply -f '${serviceYaml}'"
}