# Display a report showing the full name of all the users currently connected, and the number of processes belonging to each of them.

arr=()
app=()
nr=0
for x in $(cat who.fake | awk '{print $1}'); do
    nr=$((nr+1))
    arr[$nr]=$x
    app[$nr]=0
done
bkp=$IFS
IFS=$'\n'
for x in $(cat ps.fake); do
    for y in $(seq 1 $nr); do
	if [ ${arr[$y]} == $(echo $x | cut -d' ' -f1) ]; then
	    app[$y]=$((app[$y] + 1))
	    break
	fi
    done
done

for x in $(seq 1 $nr); do
    echo ${arr[$x]} ${app[$x]}
done

IFS=$bkp

#Use grep -c you half wit