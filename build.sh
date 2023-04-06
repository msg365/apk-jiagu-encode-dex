cd ../
apktool b -o src.build.apk src.dec/
unzip -d src.build src.build.apk
cd src.build
mkdir src
mv classes* src/