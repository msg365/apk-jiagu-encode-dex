cd ..
rm -rf src.build.apk src.build
apktool b -o src.build.apk src.dec/
unzip -d src.build src.build.apk 
cd src.build
mkdir src
mv classes* src/
zip -rn .arsc:.mp3:.mp4:.json -r acece4901ec2f0faf156df76b80f811b assets resources.arsc res
rm -rf assets/*
mv acece4901ec2f0faf156df76b80f811b.zip assets/

cd /home/u1/Git/jiagu.dfw.v4/enc
