#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <cctype> // For isalpha, isalnum

using namespace std;

bool isIdentifierChar(char c) {
    return isalnum(c) || c == '_';
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <filename>" << endl;
        return 1;
    }

    ifstream file(argv[1]);
    if (!file.is_open()) {
        cerr << "Error: Could not open file " << argv[1] << endl;
        return 1;
    }

    set<string> keywords = {"if", "else", "while", "for", "int"};
    map<string, int> keywordCounts;

    for (const string& kw : keywords) {
        keywordCounts[kw] = 0;
    }

    string currentWord;
    char c;

    while (file.get(c)) {
        if (isIdentifierChar(c)) {
            currentWord += c;
        } else {
            if (!currentWord.empty()) {
                if (keywords.count(currentWord)) {
                    keywordCounts[currentWord]++;
                }
                currentWord = "";
            }
        }
    }

    if (!currentWord.empty()) {
        if (keywords.count(currentWord)) {
            keywordCounts[currentWord]++;
        }
    }

    cout << "Keyword Counts:" << endl;
    for (const auto& pair : keywordCounts) {
        cout << "  " << pair.first << ": " << pair.second << endl;
    }

    file.close();
    return 0;
}