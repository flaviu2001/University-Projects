# 6. Seminar problem: Write a script that monitors the state of a given folder and prints a message when something changed.

#!/bin/bash

if [ $# -ne 1 ]; then
	echo "Needs exactly one argument"
	exit 1
fi

if [ ! -d $1 ];then
	echo "Not a directory"
	exit 1
fi

first=0

while true; do
	bkp=$IFS
	IFS=$'\n'
	args=$(find $1)
	state=""
	for fl in $args; do
		if [ -f $fl ]; then
			props=$(ls -l $fl | sha256sum)
			content=$(sha256sum $fl)
		fi
		if [ -d $fl ]; then
			props=$(ls -ld $fl | sha256sum)
			content=$(ls -l $fl | sha256sum)
		fi
		state=$state$'\n'"$props $content"
	done
	IFS=$bkp
	if [ "$oldstate" != "$state" ] && [ $first -eq 1 ]; then
		echo "Directory state changed"
	fi
	oldstate=$state
	first=1
	sleep 1
done
