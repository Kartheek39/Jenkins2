pipeline {
    agent any

    stages{
        stage('code checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Kartheek39/javaloginapp.git']]])
            }
        }

        stage('code build') {
            steps {
                cd '/var/lib/jenkins/workspace/2nd_job_pipe'
                //sh 'mvn clean install'
            }
        }
    }

}


//pipeline{
//    agent any
//    stages{
//        stage('code checkout'){
//            steps{
//                echo "code checkout success"
//            }
//        }
//        stage('Run Build'){
//            steps{
//                echo "Build success"
//            }
//        }
//        stage('Deployment'){
//            steps{
//                echo "Deployment success"
//            }
//        }
//    }
//}

/*@Library('Testlibraries') _

log.info 'Starting'
log.warning 'Nothing to do!'*/
	

		

