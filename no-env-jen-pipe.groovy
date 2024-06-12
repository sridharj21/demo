Jenkins pipeline scripetd for ECR push to docker image.
#This Scripted in No pass to the enviroment variable .
#............


pipeline {
    agent any

    stages {
        stage('Logging into AWS ECR') {
            steps {
                withCredentials([
                    string(credentialsId: 'Access-key-123', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'Secret_key123', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'Repo_url', variable: 'REPOSITORY_URI'),
                    string(credentialsId: 'accountid', variable: 'AWS_ACCOUNT_ID')
                ]) {
                    script {
                        sh '''
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-3.amazonaws.com
                        '''
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                // Checkout source code from the repository
                git url: 'https://github.com/sridharj21/Hello.git', branch: 'main'
            }
        }

        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build("demo12:latest")
                }
            }
        }

        stage('Pushing to ECR') {
            steps {
                withCredentials([
                    string(credentialsId: 'Access-key-123', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'Secret_key123', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'Repo_url', variable: 'REPOSITORY_URI'),
                    string(credentialsId: 'accountid', variable: 'AWS_ACCOUNT_ID')
                ]) {
                    script {
                        sh '''
                        # Re-authenticate before pushing the image to avoid token expiration
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.eu-west-3.amazonaws.com
                        docker tag demo12:latest public.ecr.aws/o4x7k6y5/demo12:latest
                        docker push public.ecr.aws/o4x7k6y5/demo12:latest
                    }
                }
            }
        }
    }
}
