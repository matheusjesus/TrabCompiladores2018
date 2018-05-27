#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void fib(int num) {
    char * coma;
    coma = (char *) malloc(sizeof(char) * 2);
    strcpy(coma,  " , ");

    int aux, i, a, b;

    a = 0;
    b = 1;
    printf("%d", b);
    printf("%s", coma);
    for( i = 0; i < num; i = i + 1) {
        aux = a + b;
        a = b;
        b = aux;
        printf("%d", aux);
        if (i > num - 1) {
            printf("%s", coma);
        }
        if (i < num - 1) {
            printf("%s", coma);
        }
    }
}

void main() {
    char * comeco;
    comeco = (char *) malloc(sizeof(char) * 59);
    strcpy(comeco,  " Escolha uma opcao: -1. Media de dois numeros -2. Fibonacci ");

    char * error;
    error = (char *) malloc(sizeof(char) * 14);
    strcpy(error,  " Opcao invalida");

    char * numero1;
    numero1 = (char *) malloc(sizeof(char) * 26);
    strcpy(numero1,  " Digite o primeiro numero: ");

    char * numero2;
    numero2 = (char *) malloc(sizeof(char) * 25);
    strcpy(numero2,  " Digite o segundo numero: ");

    int num, opc;
    float num2, num3, result;

    printf("%s", comeco);
    scanf("%d", &opc);
    if (opc == 1) {
        printf("%s", numero1);
        scanf("%f", &num2);
        printf("%s", numero2);
        scanf("%f", &num3);
        result = (num2 + num3) / 2;
        printf("%f", result);
    } else{
        if (opc == 2) {
            scanf("%d", &num);
            fib(num);
        } else{
            printf("%s", error);
        }
    }

}

