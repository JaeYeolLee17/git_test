pipeline {
  agent any
  environment {
      CI = false
  }
  stages {
    stage('git update') {
      steps {
        git url: 'https://sjkim_emotion@bitbucket.org/emgglobal/smart-city-challenge.git', branch: 'jenkins-test', credentialsId: '25f1d5f2-2d0e-4afb-86c4-3a496b71a3fb'
      }
    }
    stage ('frontend build') {
      tools { nodejs "NodeJS" }
      steps {
        sh 'cd monitor/frontend && npm install && npm run build:nextdev'
      }
    }
    stage ('backend build') {
      steps {
        sh 'cd backend && ./gradlew clean build'
      }
    }
    stage('docker build and push'){
      steps {
        sh '''
        export TAG=0.0.1
        export REGISTRY=192.168.0.240:5000

        docker build -t $REGISTRY/challenge-api:$TAG backend/challenge-api
        docker build -t $REGISTRY/challenge-data-collector:$TAG backend/challenge-data-collector
        docker build -t $REGISTRY/challenge-data-provider:$TAG backend/challenge-data-provider
        docker build -t $REGISTRY/challenge-monitor:$TAG monitor/frontend

        docker rmi $(docker images -f "dangling=true" -q)

        docker push $REGISTRY/challenge-api:$TAG
        docker push $REGISTRY/challenge-data-collector:$TAG
        docker push $REGISTRY/challenge-data-provider:$TAG
        docker push $REGISTRY/challenge-monitor:$TAG
        '''
      }
    }
    stage('kubernetes deploy over ssh') {
      steps([$class: 'BapSshPromotionPublisherPlugin']) {
        sshPublisher(
          continueOnError: false,
          failOnError: true,
          publishers: [
            sshPublisherDesc(
              configName: 'k8s HA lb',
              verbose: true,
              transfers: [
                sshTransfer(
                    sourceFiles: 'deploy/challenge-k8s-deploy/**',
                    removePrefix: 'deploy/challenge-k8s-deploy/',
                    remoteDirectory: 'smart-city-challenge/deploy',
                    execCommand: 'kubectl delete -k smart-city-challenge/deploy; kubectl apply -k smart-city-challenge/deploy'
                )
              ]
            )
          ]
        )
      }
    }
  }
}    
