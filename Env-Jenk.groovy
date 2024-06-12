# It's pass to the enviromnet variabe.


pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID = "330716407122"
        AWS_DEFAULT_REGION = "eu-west-3" 
        IMAGE_REPO_NAME = "demo12"
        IMAGE_TAG = "latest"
        REPOSITORY_URI = "330716407122.dkr.ecr.eu-west-3.amazonaws.com/demo12"
    }

    stages {
        stage('Logging into AWS ECR') {
            steps {
                script {
                    sh """
                    aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 330716407122.dkr.ecr.eu-west-3.amazonaws.com
                    """
                }
            }
        }

        stage('Checkout') {
            steps {
                // Checkout source code from the repository
                git url: "https://github.com/sridharj21/Hello.git", branch: "main"
            }
        }

        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build "demo12:latest"
                }
            }
        }

        stage('Pushing to ECR') {
            steps { 
                script {
                    sh "docker tag demo12:latest public.ecr.aws/o4x7k6y5/demo12:latest"
                    sh "docker push public.ecr.aws/o4x7k6y5/demo12:latest"
                }
            }
        }
         
        
    }

    
}
