pipeline{
    agent any
    stages{
        stage('code checkout'){
            steps{
                echo "code checkout success"
            }
        }
        stage('Run Build'){
            steps{
                echo "Build success"
            }
        }
        stage('Deployment'){
            steps{
                echo "Deployment success"
            }
        }
    }
}


pipeline {
    agent any
// 	agent { node { label 'slave' } }
    environment {
        // Email Recipints
        DEFAULT_RECIPIENTS= '<email>'
        ALLWAYS_RECIPIENTS= '<email>'
        remote_server = '172.31.87.123'

        // # Paths
        ss_code_dir = "$WORKSPACE/java-hello-world-with-maven"
        ss_dist_dir = "$ss_code_dir/target"
        // destination path
        ds_code_dir = '/home/www/helloworld'
        // Backup directory
        ds_backup_dir = '/home/last_build_backup/helloworld'

        git_credentialsid = 'Kartheek39'
// 		jenkins_login = 'JENKINS_STG'
        git_url = 'github.com/jabedhasan21/java-hello-world-with-maven.git'
        BRANCH_NAME = 'master'

    }

    /*parameters {
        /*gitParameter(branch: '',
            branchFilter: 'origin/(.*)',
            defaultValue: 'gms_staging',
            description: 'Default branch : "gms_staging"\nOR\nChoose the "BRANCH_NAME" from above list',
            name: 'BRANCH_NAME',
            quickFilterEnabled: false,
            selectedValue: 'NONE',
            sortMode: 'NONE',
            tagFilter: '*',
            type: 'PT_BRANCH')*/
}*/	
	
    stages {
		stage('Code Checkout') {
			steps {
			    CodeCheckout()
			    }
        }
		stage('Maven Build') { 
			steps {
			    RunBuild()
            }
		}
		stage('Deployment') { 
			steps {
			    Deploy()
            }
		}
    }
}
		
void CodeCheckout() {
    sh '''
         rm -rf $WORKSPACE/*
        '''
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/jabedhasan21/java-hello-world-with-maven.git']]])
}

void RunBuild() {
    sh '''
        cd $ss_code_dir
        mvn clean install
    '''
}		

void RunBuild() {
    sh '''
      echo
    '''
}	

