#3. Write a script that receives as command line arguments pairs consisting of a filename and a word. For each pair, check if the given word appears at least 3 times in the file and print a corresponding message.

#!/bin/bash

while [ $# -gt 0 ]; do
	filename=$1
	word=$2
	cnt=0
	words=$(cat $filename)
	for w in $words; do
		if [ $w == $word ]; then
			cnt=$((cnt+1))
		fi
	done
	if [ $cnt -gt 2 ]; then
		echo "yeaa"
	fi
	shift 2
done
