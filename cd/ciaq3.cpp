#include <iostream>
#include <fstream>
#include <string>
#include <cctype> // For isspace

using namespace std;

int main(int argc, char* argv[]) {
    if (argc != 2) {
        cout << "Filename not given or more than one given" << endl;
        return 1;
    }

    ifstream file(argv[1]);
    if (!file.is_open()) {
        cout << "Error file not opened" << endl;
        return 1;
    }

    int blankSpaces = 0;
    int words = 0;
    int lines = 0;
    bool inWord = false;
    char c;
    char last_char = '\n'; 

    while (file.get(c)) {
        if (c == '\n') {
            lines++;
        }

        if (isspace(c)) {
            blankSpaces++;
            inWord = false;
        } else {
            if (!inWord) {
                words++;
                inWord = true;
            }
        }
        last_char = c;
    }

    if (words > 0 && last_char != '\n') {
        lines++;
    }

    cout << "Blank Spaces: " << blankSpaces << endl;
    cout << "Words: " << words << endl;
    cout << "Lines: " << lines << endl;

    file.close();
    return 0;
}