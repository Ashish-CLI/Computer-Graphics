#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <sstream> // For string splitting in tokenizer
#include <iomanip> // For pretty-printing

// Use standard C++ namespaces
using namespace std;

// --- 1. The Grammar ---
// The parser is built for the classic expression grammar:
//
// (0) S' -> E         (Augmented rule)
// (1) E  -> E + T
// (2) E  -> T
// (3) T  -> T * F
// (4) T  -> F
// (5) F  -> ( E )
// (6) F  -> id
//
// Terminals: +, *, (, ), id, $
// Non-Terminals: E, T, F

// --- 2. The SLR(1) Parse Tables ---
// We use std::map to represent the nested dictionaries from Python.

// ACTION Table: map<state, map<terminal, action>>
map<int, map<string, string>> action_table = {
    {0, {{"id", "s5"}, {"(", "s4"}}},
    {1, {{"+", "s6"}, {"$", "acc"}}},
    {2, {{"+", "r2"}, {"*", "s7"}, {")", "r2"}, {"$", "r2"}}},
    {3, {{"+", "r4"}, {"*", "r4"}, {")", "r4"}, {"$", "r4"}}},
    {4, {{"id", "s5"}, {"(", "s4"}}},
    {5, {{"+", "r6"}, {"*", "r6"}, {")", "r6"}, {"$", "r6"}}},
    {6, {{"id", "s5"}, {"(", "s4"}}},
    {7, {{"id", "s5"}, {"(", "s4"}}},
    {8, {{"+", "s6"}, {")", "s11"}}},
    {9, {{"+", "r1"}, {"*", "s7"}, {")", "r1"}, {"$", "r1"}}},
    {10, {{"+", "r3"}, {"*", "r3"}, {")", "r3"}, {"$", "r3"}}},
    {11, {{"+", "r5"}, {"*", "r5"}, {")", "r5"}, {"$", "r5"}}}
};

// GOTO Table: map<state, map<non_terminal, state>>
map<int, map<string, int>> goto_table = {
    {0, {{"E", 1}, {"T", 2}, {"F", 3}}},
    {1, {}},
    {2, {}},
    {3, {}},
    {4, {{"E", 8}, {"T", 2}, {"F", 3}}},
    {5, {}},
    {6, {{"T", 9}, {"F", 3}}},
    {7, {{"F", 10}}},
    {8, {}},
    {9, {}},
    {10, {}},
    {11, {}}
};

// Productions: (Head, Body Length)
// We use std::vector of std::pair<string, int>
vector<pair<string, int>> productions = {
    {"E", 1},  // 0: S' -> E (Using E as S')
    {"E", 3},  // 1: E  -> E + T
    {"E", 1},  // 2: E  -> T
    {"T", 3},  // 3: T  -> T * F
    {"T", 1},  // 4: T  -> F
    {"F", 3},  // 5: F  -> ( E )
    {"F", 1}   // 6: F  -> id
};

// --- 3. The Parser Engine ---

// Helper function to print the stack
void print_stack(const vector<int>& stack) {
    stringstream ss;
    for (int state : stack) {
        ss << state << " ";
    }
    cout << left << setw(30) << ss.str();
}

// Helper function to print the remaining input
void print_input(const vector<string>& tokens, int cursor) {
    stringstream ss;
    for (int i = cursor; i < tokens.size(); ++i) {
        ss << tokens[i] << " ";
    }
    cout << "| " << left << setw(30) << ss.str();
}

bool parse(const vector<string>& tokens) {
    if (tokens.empty()) {
        cout << "Error: No tokens to parse." << endl;
        return false;
    }

    // The stack holds states (integers)
    vector<int> stack;
    stack.push_back(0); // Initial state
    int cursor = 0;

    cout << "--- Parsing Process ---" << endl;
    cout << left << setw(30) << "Stack"
         << "| " << left << setw(30) << "Input Tokens"
         << "| " << left << setw(30) << "Action" << endl;
    cout << string(94, '-') << endl;

    while (true) {
        int state = stack.back();
        string token = tokens[cursor];

        // --- Print current state ---
        print_stack(stack);
        print_input(tokens, cursor);

        // --- Find action ---
        // Check if state exists in action_table
        if (action_table.find(state) == action_table.end()) {
            cout << "| ERROR: No action for state " << state << endl;
            return false;
        }
        // Check if token exists for that state
        if (action_table[state].find(token) == action_table[state].end()) {
            cout << "| ERROR: No action for state " << state << " and token '" << token << "'" << endl;
            return false;
        }

        string action_entry = action_table[state][token];
        cout << "| Action: " << action_entry << endl;

        // --- Perform Action ---
        if (action_entry[0] == 's') {
            // SHIFT
            int new_state = stoi(action_entry.substr(1));
            stack.push_back(new_state);
            cursor++;

        } else if (action_entry[0] == 'r') {
            // REDUCE
            int rule_num = stoi(action_entry.substr(1));
            string non_terminal = productions[rule_num].first;
            int body_len = productions[rule_num].second;

            // Pop body_len states from the stack
            for (int i = 0; i < body_len; ++i) {
                stack.pop_back();
            }

            // Get the state now on top
            int prev_state = stack.back();

            // Find the GOTO state
            int goto_state = goto_table[prev_state][non_terminal];
            stack.push_back(goto_state);
            
            // Print reduce and goto info
            print_stack(stack);
            print_input(tokens, cursor);
            cout << "| Reduce by " << non_terminal << " -> ... (Rule " << rule_num << "), GOTO State " << goto_state << endl;

        } else if (action_entry == "acc") {
            // ACCEPT
            cout << "\n--- Parse Successful! ---" << endl;
            return true;
        }
    }
}

// --- 4. Tokenizer and Main execution ---

vector<string> tokenize(const string& input_string) {
    vector<string> tokens;
    for (int i = 0; i < input_string.length(); ++i) {
        char c = input_string[i];
        if (isspace(c)) {
            continue;
        } else if (isalpha(c)) {
            // For this simple grammar, any text is 'id'
            tokens.push_back("id");
            while (i + 1 < input_string.length() && isalnum(input_string[i + 1])) {
                i++;
            }
        } else if (c == '+' || c == '*' || c == '(' || c == ')') {
            tokens.push_back(string(1, c));
        } else {
            cout << "Error: Unknown character '" << c << "'" << endl;
            return {}; // Return empty vector on error
        }
    }
    tokens.push_back("$"); // End-of-input marker
    return tokens;
}

int main() {
    // --- Example 1: Valid String ---
    cout << string(30, '=') << endl;
    cout << "  Parsing: id * id + id" << endl;
    cout << string(30, '=') << endl;
    
    string input_str_1 = "id * id + id";
    vector<string> tokens_1 = tokenize(input_str_1);
    if (!tokens_1.empty()) {
        parse(tokens_1);
    }

    cout << "\n" << endl;

    // --- Example 2: Invalid String ---
    cout << string(30, '=') << endl;
    cout << "  Parsing: id * ( id" << endl;
    cout << string(30, '=') << endl;
    
    string input_str_2 = "id * ( id";
    vector<string> tokens_2 = tokenize(input_str_2);
    if (!tokens_2.empty()) {
        parse(tokens_2);
    }

    cout << "\n" << endl;

    // --- Example 3: Valid String with Parentheses ---
    cout << string(30, '=') << endl;
    cout << "  Parsing: ( id + id ) * id" << endl;
    cout << string(30, '=') << endl;

    string input_str_3 = "( id + id ) * id";
    vector<string> tokens_3 = tokenize(input_str_3);
    if (!tokens_3.empty()) {
        parse(tokens_3);
    }

    return 0;
}