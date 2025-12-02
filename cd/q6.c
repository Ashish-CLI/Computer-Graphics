#include<stdio.h>
#include<ctype.h>
#include<string.h>

void findFirst(char c, int result_row);

int num_productions;
char productions[20][20];
char first_set[20][20] = {0};


void addToResultSet(char result[], char val) {
    int k;
    for (k = 0; result[k] != '\0'; k++) {
        if (result[k] == val) {
            return;
        }
    }
    result[k] = val;
    result[k + 1] = '\0';
}

int main() {
    int i;

    printf("Enter the number of production rules: ");
    scanf("%d", &num_productions);
    printf("Enter %d production rules (e.g., E=TR, T=# for epsilon):\n", num_productions);

    for (i = 0; i < num_productions; i++) {
        scanf("%s", productions[i]);
    }

    int non_terminal_count = 0;
    char non_terminals[20];

    for (i = 0; i < num_productions; i++) {
        int found = 0;
        for(int j=0; j < non_terminal_count; j++){
            if(non_terminals[j] == productions[i][0]){
                found = 1;
                break;
            }
        }
        if(!found){
            non_terminals[non_terminal_count++] = productions[i][0];
        }
    }


    for (i = 0; i < non_terminal_count; i++) {
         findFirst(non_terminals[i], i);
         printf("FIRST(%c) = { ", non_terminals[i]);
         for (int j = 0; j < strlen(first_set[i]); j++) {
            printf("%c ", first_set[i][j]);
         }
         printf("}\n");
    }

    return 0;
}

void findFirst(char c, int result_row) {
    int j;

    for (j = 0; j < num_productions; j++) {
        if (productions[j][0] == c) {
            char first_symbol = productions[j][2];

            if (first_symbol == '#') {
                addToResultSet(first_set[result_row], '#');
            }
            else if (!isupper(first_symbol)) {
                addToResultSet(first_set[result_row], first_symbol);
            }
            else {
                findFirst(first_symbol, result_row);
            }
        }
    }
}
