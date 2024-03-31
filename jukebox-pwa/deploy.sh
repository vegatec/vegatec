echo "------------------------------ build production ---------------------------------------"
# npm run build -prod

echo "----------------------- copy node app to build folder ---------------------------------"
# cp src/main/webapp/app/index.js build/resources/main/static

echo "--------------------------- build docker image ----------------------------------------"
docker build -t jukebox-control .

echo "-------------------------- save docker image as tar  ----------------------------------"
docker save jukebox-control > build/jukebox-control.tar

echo "------------------------ copy docker  tar to remote host -------------------------------"
# scp build/jukebox-control.tar vegatec@$1:./jukebox/jukebox-control.tar
