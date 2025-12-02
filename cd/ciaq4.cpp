#include <iostream>
#include <fstream>
#include <string>
#include <set>
#include <map>

using namespace std;

// Function to check if a character can be part of an operator
bool isOperatorChar(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/' || c == '=' ||
            c == '<' || c == '>' || c == '!' || c == '&' || c == '|' ||
            c == '%' || c == '^' || c == '~');
}

// Define common C++ operators
const set<string> operators = {
    "+", "-", "*", "/", "%",
    "=", "==", "!=", "<", ">", "<=", ">=",
    "&&", "||", "!",
    "++", "--",
    "+=", "-=", "*=", "/=", "%=",
    "&", "|", "^", "~", "<<", ">>",
    "&=", "|=", "^=", "<<=", ">>="
};

/**
 * @brief Processes a string of operator characters using greedy matching.
 */
void processOperatorString(string& opStr, map<string, int>& counts, int& total) {
    if (opStr.empty()) return;

    while (!opStr.empty()) {
        // Check for 3-character operators
        if (opStr.length() >= 3 && operators.count(opStr.substr(0, 3))) {
            counts[opStr.substr(0, 3)]++;
            total++;
            opStr = opStr.substr(3);
        }
        // Check for 2-character operators
        else if (opStr.length() >= 2 && operators.count(opStr.substr(0, 2))) {
            counts[opStr.substr(0, 2)]++;
            total++;
            opStr = opStr.substr(2);
        }
        // Check for 1-character operators
        else if (opStr.length() >= 1 && operators.count(opStr.substr(0, 1))) {
            counts[opStr.substr(0, 1)]++;
            total++;
            opStr = opStr.substr(1);
        }
        else {
            // Discard unrecognized operator character
            opStr = opStr.substr(1);
        }
    }
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

    map<string, int> operatorCounts;
    int totalOperators = 0;
    char c;
    string currentOperator;

    while (file.get(c)) {
        if (isOperatorChar(c)) {
            currentOperator += c; // Keep building the operator string
        } else {
            // We hit a non-operator char. Process what we've found.
            processOperatorString(currentOperator, operatorCounts, totalOperators);
        }
    }

    // After the loop, process any trailing operator
    processOperatorString(currentOperator, operatorCounts, totalOperators);

    cout << "Total Operators: " << totalOperators << endl;
    cout << "Operator Breakdown:" << endl;
    for (const auto& pair : operatorCounts) {
        cout << "  " << pair.first << ": " << pair.second << endl;
    }

    file.close();
    return 0;
}