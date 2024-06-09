if [ $# -eq 0 ]; then
    echo "No arguments provided"
    exit 1
fi

if [ $1 == "--build" ]; then
    echo "------------------------------ build production ---------------------------------------"
    ./mvnw package -Pprod  verify jib:buildTar -DskipTests=true
fi

current_branch=$(git branch --show-current)

echo  $current_branch



echo "------------------------ load docker image ---------------------------------------"
docker load --input target/jib-image.tar

docker image tag media-library 192.168.2.2:5000/vegatec/media-library:${current_branch}
if [ $2 == "--prod" ]; then
    echo "------------------------------ docker  production  release ---------------------------------------"
    docker image tag media-library 192.168.2.2:5000/vegatec/media-library:latest
fi

echo "------------------------ push  all  docker image tags to remote host ---------------------------------------"
docker image push --all-tags 192.168.2.2:5000/vegatec/media-library

echo "------------------------ copy docker compose files ---------------------------------------"





