
pipeline {
    agent any
    
    stages {
        stage('Logging into AWS ECR') {
            steps {
                withCredentials([
                    string(credentialsId: 'Access-key-123', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'Secret_key123', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'Repo_url', variable: 'REPOSITORY_URI')


                ]) {
                    script {
                        sh """
                        aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                        aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                        aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 330716407122.dkr.ecr.eu-west-3.amazonaws.com
                        """
                    }
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


