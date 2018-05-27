#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int fatorial(int num) {
    int fat;

    if (num < 2) {
        return 1 ;
    } else{
        fat = num * fatorial(num - 1);
        return fat ;
    }

}

void main() {
    char * comeco;
    comeco = (char *) malloc(sizeof(char) * 18);
    strcpy(comeco,  " Insira um numero: ");

    char * fim;
    fim = (char *) malloc(sizeof(char) * 13);
    strcpy(fim,  " O fatorial e ");

    int num, f;

    printf("%s", comeco);
    scanf("%d", &num);
    f = fatorial(num);
    printf("%s", fim);
    printf("%d", f);

}

