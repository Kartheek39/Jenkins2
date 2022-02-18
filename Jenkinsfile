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
                RunBuild()
            }
        }
        stage('Deployment'){
            steps{
                Deployment()
            }
        }
    }
}