cd ..
rm -rf src.build.apk src.build
apktool b -o src.build.apk src.dec/
unzip -d src.build src.build.apk 
cd src.build
mkdir src
mv classes* src/

cd /home/u1/Git/jiagu.xpj843.v3/enc
