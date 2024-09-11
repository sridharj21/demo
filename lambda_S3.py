##Make sure the IAM role is attached to the lambda.(passrole).
import boto3

def lambda_handler(event, context):
    s3 = boto3.client('s3')
    
    try:
        # Set up replication configuration on the source bucket
        response = s3.put_bucket_replication(
            Bucket='8kmlies12312',
            ReplicationConfiguration={
                'Role': 'arn:aws:iam::330716407122:role/service-role/Normal-role-fxdol3c9',
                'Rules': [
                    {
                        'ID': 'ReplicateTo8krocheaws32',  # Unique identifier for the rule
                        'Priority': 1,  
                        'Status': 'Enabled',
                        'Filter': {
                            'Prefix': ''  # Empty means replicate all objects
                        },
                        'DeleteMarkerReplication': {  # Specify delete marker replication
                            'Status': 'Disabled'  # You can also set this to 'Enabled' if needed
                        },
                        'Destination': {
                            'Bucket': 'arn:aws:s3:::8krocheaws-32',  # Correct destination bucket
                            'StorageClass': 'STANDARD'
                        }
                    }
                ]
            }
        )
        
        return {
            'statusCode': 200,
            'body': f"Replication configuration set successfully: {response}"
        }
    
    except Exception as e:
        return {
            'statusCode': 400,
            'body': f"An error occurred: {str(e)}"
        }
