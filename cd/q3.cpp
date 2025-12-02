#include <iostream>
#include <fstream>
#include <string>
#include <set>

using namespace std;

int main() {
    set<char> operators = {
        '+', '-', '*', '/', '%',
        '=', '!', '<', '>', '&',
        '|', '^', '~', '?'
    };

    string file;
    cout << "Enter file name: ";
    cin >> file;

    ifstream inputFile(file);

    if (!inputFile.is_open()) {
        cout << "Error: Could not open file '" << file << "'" << endl;
        return 1;
    }

    char ch;
    int count = 0;

    while (inputFile.get(ch)) {
        if (operators.count(ch)) {
            count++;
        }
    }

    inputFile.close();

    cout << "Total operators found: " << count << endl;

    return 0;
}