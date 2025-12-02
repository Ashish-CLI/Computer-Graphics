#include <stdio.h>
#include <string.h>
#include <stdlib.h>


struct Symbol {
    char name[50];
    char type[50];
    int address;
};

struct Symbol symbol_table[100];
int symbol_count = 0;

int search_symbol(char name[]) {
    int i;
    for (i = 0; i < symbol_count; i++) {
        if (strcmp(symbol_table[i].name, name) == 0) {
            return i;
        }
    }
    return -1;
}

void insert_symbol() {
    char name[50], type[50];
    int address;

    if (symbol_count >= MAX_SYMBOLS) {
        printf("Symbol Table is full. Cannot insert.\n");
        return;
    }

    printf("Enter symbol name: ");
    scanf("%s", name);

    if (search_symbol(name) != -1) {
        printf("Error: Symbol '%s' already exists.\n", name);
        return;
    }

    printf("Enter symbol type: ");
    scanf("%s", type);
    printf("Enter symbol address: ");
    scanf("%d", &address);

    strcpy(symbol_table[symbol_count].name, name);
    strcpy(symbol_table[symbol_count].type, type);
    symbol_table[symbol_count].address = address;
    symbol_count++;

    printf("Symbol inserted successfully.\n");
}

void delete_symbol() {
    char name[50];
    int i, index;

    if (symbol_count == 0) {
        printf("Symbol Table is empty. Cannot delete.\n");
        return;
    }

    printf("Enter name of the symbol to delete: ");
    scanf("%s", name);

    index = search_symbol(name);

    if (index == -1) {
        printf("Error: Symbol '%s' not found.\n", name);
        return;
    }

    for (i = index; i < symbol_count - 1; i++) {
        symbol_table[i] = symbol_table[i + 1];
    }

    symbol_count--;
    printf("Symbol '%s' deleted successfully.\n", name);
}

void display_table() {
    int i;

    if (symbol_count == 0) {
        printf("\nSymbol Table is Empty.\n");
        return;
    }

    printf("\n--- Symbol Table ---\n");
    printf("Name\t\tType\t\tAddress\n");
    printf("----------------------------------------\n");
    for (i = 0; i < symbol_count; i++) {
        printf("%-15s\t%-15s\t%d\n", symbol_table[i].name, symbol_table[i].type, symbol_table[i].address);
    }
    printf("----------------------------------------\n");
}

int main() {
    int choice;

    while (1) {
        printf("\nSymbol Table Operations:\n");
        printf("1. Insert a symbol\n");
        printf("2. Delete a symbol\n");
        printf("3. Display symbol table\n");
        printf("4. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                insert_symbol();
                break;
            case 2:
                delete_symbol();
                break;
            case 3:
                display_table();
                break;
            case 4:
                printf("Exiting...\n");
                exit(0);
            default:
                printf("Invalid choice. Please try again.\n");
        }
    }

    return 0;
}