#4. Sort all files given as command line arguments descending by size.

#!/bin/bash

var=""
for word in $@; do
	cnt=$(du -b $word)
	var="$var$cnt $word"$'\n'
done
echo "$var"
echo "$var" | sort -rn
