BEGIN{
	printf "%-10s %s\n", "NAME", "AVERAGE"
	printf "%-10s %s\n", "---", "-------"
}

NR > 1 {
	sum = 0
	cnt = 0
	for (i=3; i <= NF; ++i)
		if ($i >= 0){
			sum = sum+$i
			cnt = cnt+1
			test[i-2] = test[i-2]+$i
			testcnt[i-2] = testcnt[i-2]+1
			testteam[$2] = testteam[$2]+$i
			testteamcnt[$2] = testteamcnt[$2]+1
		}
	v[$1] = sum/cnt
}

END{
	for (i in v)
		printf "%-10s %7.2f\n", i, v[i]
	print "------------------"
	for (i in test)
		print "Average for Test", i, ": ", test[i]/testcnt[i]
	print "------------------"
	for (i in testteam)
		print "Average for ", i, "Team: ", testteam[i]/testteamcnt[i]
}
