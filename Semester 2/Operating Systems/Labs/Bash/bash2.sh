#2. Write a script that reads filenames until the word "stop" is entered. For each filename, check if it is a text file and if it is, print the number of words on the first line.

#!/bin/bash

while [ 1 -eq 1 ]; do
        read A
        if [ -f $A ] && [ $(file "$A" | grep -c "text") -eq 1 ]; then
                head -n 1 $A | wc -w
        fi
done
