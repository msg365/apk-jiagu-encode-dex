# zip -rn .arsc:.mp3:.mp4:.json -r 1.apk assets resources.arsc res

cd ../src.build/assets

mkdir 110bdcbb6c486590_
cd 110bdcbb6c486590_
mv ../110bdcbb6c486590 .
split -b 1m 110bdcbb6c486590
rm 110bdcbb6c486590

cd ..
mkdir cb90d78184cc1f6a_
cd cb90d78184cc1f6a_
mv ../cb90d78184cc1f6a .
split -b 1m cb90d78184cc1f6a
rm cb90d78184cc1f6a

cd /home/u1/Git/jiagu.xpj843.v3/enc
