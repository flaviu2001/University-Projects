# Display all the mounted file systems who are either smaller than than 1GB or have less than 20% free space.

bkp=$IFS
IFS=$'\n'
first=0
for x in $(cat df.fake); do
    if [ $first -eq 0 ]; then
	first=1
	continue
    fi
    sz=$(echo $x | awk '{print $2}' | sed 's#\(.*\).$#\1#')
    use=$(echo $x | awk '{print $5}' | sed 's#\(.*\).$#\1#')
    name=$(echo $x | awk '{print $6}')
    if [ $sz -lt 1024 ] || [ $use -gt 80 ]; then
	echo $name
    fi
done
IFS=$bkp
