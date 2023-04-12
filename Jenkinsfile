// pipeline {
//     agent any
//     environment {
//         PATH = "/opt/maven/bin:$PATH"
//     }
//     stages{
//         stage('code checkout') {
//             steps {
//                 checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Kartheek39/hello-world.git']]])
//             }
//         }

//         stage('build') {
//             steps {
//                    dir("/var/lib/jenkins/workspace/2nd_job_pipe") {
//                    sh 'mvn clean package'
//                 }
//             }
//         }

//         stage('Deploy') {
//             steps {
//                 deploy adapters: [tomcat9(credentialsId: 'tomcat_cred', path: '', url: 'http://174.129.175.179:8080/')], contextPath: 'JavaWebapp', war: '**/*.war'
//             }
//         }
//     }

// }

pipeline {
    agent any

    stages {
        stage('code checkout') {
            steps {
                echo "checkout the code is done"
            }
        }
        stage('code build') {
            steps {
                echo "build the code is done"
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
	

		

