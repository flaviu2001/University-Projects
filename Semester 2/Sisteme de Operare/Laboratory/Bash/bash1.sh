#1. Write a bash script that calculates the sum of the sizes of all text files in a given folder.

#!/bin/bash

if [ $# -ne 1 ]; then
	echo "Please provide exactly one argument"
	exit
fi

sum=0
for i in `ls "$1"`; do
	if [ -f "$1/$i" ]; then
		isfile=`file "$1/$i" | grep -c "text"`
		if [ $isfile -eq 1 ]; then
			fsize=`du -b "$1/$i" | cut -f1`
			sum=$((sum+fsize))
		fi
	fi
done

echo "Total size: $sum"
