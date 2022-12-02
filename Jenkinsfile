pipeline {
    agent any

    tools {
        maven 'maven_3.8.6'
    }

    stages{
        stage('code checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Kartheek39/javaloginapp.git']]])
            }
        }

        stage('build') {
            steps {
                   dir("/var/lib/jenkins/workspace/2nd_job_pipe") {
                    withMaven{
                        sh 'mvn clean'
                    }
                   
                }
            }
        }

        /*stage('code build'){
            steps{
                sh 'mvn -B -DskipTests clean package'
            }
        }*/
        
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
	

		

