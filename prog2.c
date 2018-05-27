#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void main() {
    char * pesostr;
    pesostr = (char *) malloc(sizeof(char) * 17);
    strcpy(pesostr,  " Insira seu peso: ");

    char * alturastr;
    alturastr = (char *) malloc(sizeof(char) * 19);
    strcpy(alturastr,  " Insira sua altura: ");

    char * imcalto;
    imcalto = (char *) malloc(sizeof(char) * 27);
    strcpy(imcalto,  " Seu IMC esta acima do ideal");

    char * imcbaixo;
    imcbaixo = (char *) malloc(sizeof(char) * 28);
    strcpy(imcbaixo,  " Seu IMC esta abaixo do ideal");

    char * imcideal;
    imcideal = (char *) malloc(sizeof(char) * 31);
    strcpy(imcideal,  " Seu IMC esta no padroes normais");

    int flag;
    float peso, altura, alturaquad, imc;

    flag = 0;
    printf("%s", pesostr);
    scanf("%f", &peso);
    printf("%s", alturastr);
    scanf("%f", &altura);
    alturaquad = altura * altura;
    imc = peso / alturaquad;
    if (imc > 25) {
        printf("%s", imcalto);
    } else{
        if (imc < 18) {
            printf("%s", imcbaixo);
        } else{
            printf("%s", imcideal);
        }
    }

}

