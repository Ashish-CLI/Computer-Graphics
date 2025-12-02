#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

int main() {
    vector<string> keywords = {
        "int", "char", "float", "double", "bool", "void", "auto", "const",
        "if", "else", "for", "while", "do", "switch", "case", "break",
        "continue", "return", "class", "struct", "new", "delete", "this",
        "true", "false", "namespace", "using", "try", "catch", "throw"
    };

    string filename;
    cout << "Enter file name: ";
    cin >> filename;

    ifstream inputFile(filename);

    if (!inputFile) {
        cout << "Error opening file." << endl;
        return 1;
    }

    string word;
    int count = 0;

    while (inputFile >> word) {
        for (const string& k : keywords) {
            if (word == k) {
                count++;
                break;
            }
        }
    }

    inputFile.close();

    cout << "Total keywords found: " << count << endl;

    return 0;
}