AWS_PROPERTIES="~/AwsCredentials.properties"
BUCKET="s3.choncms.com"
BUCKET_FOLDER="the-bucket-folder"
TARGET_SITE_DIR="/home/ubuntu/chon-trunk/svn/trunk/tools/chon-p2/Chon Site/target/site"

appassembler/bin/AwsToolsApp -a $AWS_PROPERTIES -b $BUCKET -d $TARGET_SITE_DIR -n $BUCKET_FOLDER