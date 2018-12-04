pipeline {

    environment {
        region = "${regionName}"
        //projectName = 'strong-eon-217812'
        applicationName = 'flight-booking'
        registry = "${region}/${projectName}/${applicationName}"
        commitId = env.GIT_COMMIT.substring(0,7)
        stageString = "Build and push ${applicationName} docker image to ${region}/${projectName}"
    }

    agent {
        label 'image-builder'
    }

    stages {       
        stage("Building and pushing docker image to GCR") {
            
            steps {
                withCredentials([file(credentialsId: 'gcr-secrets-file', variable: 'GC_KEY'), 
                                 file(credentialsId: 'maven-settings-xml-secret', variable: 'MVN_SETTINGS_XML')])  {
                    container('application-image-builder') {
                        sh '''
                            mvn versions:set -DnewVersion=${commitId} -s ${MVN_SETTINGS_XML}
                            mvn clean package docker:build -s ${MVN_SETTINGS_XML}
                            cat ${GC_KEY} | docker login -u _json_key --password-stdin https://${region}
                            docker push ${registry}:${commitId}
                            mvn scm:tag -s ${MVN_SETTINGS_XML}
                        '''    
                    }
                }
            }            
        }
    }
}
