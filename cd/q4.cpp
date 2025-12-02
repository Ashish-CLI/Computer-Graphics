#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int main() {
    string file;
    cout << "Enter file name: ";
    cin >> file;

    ifstream inputFile(file);

    if (!inputFile.is_open()) {
        cout << "Error: Could not open file." << endl;
        return 1;
    }

    int characterCounts[256] = {0};
    char ch;

    while (inputFile.get(ch)) {
        characterCounts[(unsigned char)ch]++;
    }

    inputFile.close();

    cout << "\n--- Character Counts ---\n";
    for (int i = 0; i < 256; i++) {
        if (characterCounts[i] > 0) {
            cout << "Character: '" << (char)i << "' | Count: " << characterCounts[i] << endl;
        }
    }

    return 0;
}