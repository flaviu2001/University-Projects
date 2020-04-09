#include <stdio.h>
#include <string.h>

int main() {
    char date[11], days_in_month[13];
    days_in_month[1] = 31;
    days_in_month[3] = 31;
    days_in_month[5] = 31;
    days_in_month[7] = 31;
    days_in_month[8] = 31;
    days_in_month[10] = 31;
    days_in_month[12] = 31;
    days_in_month[2] = 28;
    days_in_month[4] = 30;
    days_in_month[6] = 30;
    days_in_month[9] = 30;
    days_in_month[11] = 30;
    while (1){
        scanf("%s", date);
        if (strcmp(date, "exit") == 0)
            break;
        int day = (date[0]-'0')*10 + date[1]-'0';
        int month = (date[3]-'0')*10 + date[4]-'0';
        int year = (date[6]-'0')*1000 + (date[7]-'0')*100 + (date[8]-'0')*10 + date[9]-'0';
        int answer = day;
        for (int i = 1; i < month; ++i)
            answer += days_in_month[i];
        if (month > 2 && (year%4 == 0 && (year%100 != 0 || year%400 == 0))) //leap years
            ++answer;
        printf("%d ", answer);
    }
    return 0;
}
