#DO NOT MODIFY THIS FILE!!!

#Create Javadoc
echo "Generating javadoc documentation..." 
if [ ! -d tmp ]; then
    mkdir tmp
    if [ ! -f tmp/JavadocCreateLog.log ]; then
        touch tmp/JavadocCreateLog.log
    fi
fi
javadoc -d doc *.java -author -version -Xdoclint 

#Serve Javadoc
echo "Opening javadoc documentation in a web browser...."
echo "Access your javadoc documentation at: https://$CR_IO_EXT_DOMAIN-8080.na.app.codingrooms.com/doc/index.html"
echo ''
if [ ! -d tmp ]; then
    mkdir tmp
    if [ ! -f tmp/JavadocServerLog.log ]; then
        touch tmp/JavadocServerLog.log
    fi
fi
python3 -m  http.server 8080 &> tmp/JavadocServerLog.log &
pid=$!
