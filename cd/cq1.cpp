#include <iostream>
#include <string>
#include <vector>
#include <cctype> // For isalpha, isdigit, isspace

// 1. Define the types of tokens our lexer can recognize
enum class TokenType {
    KEYWORD_IF,
    IDENTIFIER,
    NUMBER,
    LEFT_PAREN,
    RIGHT_PAREN,
    OPERATOR,
    WHITESPACE,
    UNKNOWN,
    END_OF_FILE
};

// 2. A struct to hold information about a single token
struct Token {
    TokenType type;
    std::string value; // The actual text of the token (e.g., "if", "x", "10")
};

// Helper function to convert a token type to a string for printing
std::string tokenTypeToString(TokenType type) {
    switch (type) {
        case TokenType::KEYWORD_IF:     return "KEYWORD_IF";
        case TokenType::IDENTIFIER:   return "IDENTIFIER";
        case TokenType::NUMBER:       return "NUMBER";
        case TokenType::LEFT_PAREN:   return "LEFT_PAREN";
        case TokenType::RIGHT_PAREN:  return "RIGHT_PAREN";
        case TokenType::OPERATOR:     return "OPERATOR";
        case TokenType::WHITESPACE:   return "WHITESPACE";
        case TokenType::UNKNOWN:      return "UNKNOWN";
        case TokenType::END_OF_FILE:  return "END_OF_FILE";
        default:                    return "ERROR";
    }
}

// 3. The Lexical Analyzer (Lexer) function
std::vector<Token> lex(const std::string& input) {
    std::vector<Token> tokens;
    size_t i = 0; // Our position in the input string

    while (i < input.length()) {
        char c = input[i];

        // --- Check for different token types ---

        // a. Skip whitespace
        if (std::isspace(c)) {
            i++;
            continue; // Don't create a token, just skip
        }

        // b. Single-character tokens (parentheses)
        if (c == '(') {
            tokens.push_back({TokenType::LEFT_PAREN, "("});
            i++;
            continue;
        }
        if (c == ')') {
            tokens.push_back({TokenType::RIGHT_PAREN, ")"});
            i++;
            continue;
        }

        // c. Operators (can be one or two characters)
        if (c == '>' || c == '<' || c == '=' || c == '!') {
            std::string op;
            op += c;
            i++;
            // Check for two-character operators (>=, <=, ==, !=)
            if (i < input.length() && input[i] == '=') {
                op += input[i];
                i++;
            }
            tokens.push_back({TokenType::OPERATOR, op});
            continue;
        }

        // d. Keywords or Identifiers (start with a letter)
        if (std::isalpha(c)) {
            std::string word;
            word += c;
            i++;
            // Keep reading while it's alphanumeric
            while (i < input.length() && std::isalnum(input[i])) {
                word += input[i];
                i++;
            }

            // Check if it's a keyword
            if (word == "if") {
                tokens.push_back({TokenType::KEYWORD_IF, word});
            } else {
                // Otherwise, it's an identifier
                tokens.push_back({TokenType::IDENTIFIER, word});
            }
            continue;
        }

        // e. Numbers (start with a digit)
        if (std::isdigit(c)) {
            std::string num;
            num += c;
            i++;
            // Keep reading while it's a digit
            while (i < input.length() && std::isdigit(input[i])) {
                num += input[i];
                i++;
            }
            tokens.push_back({TokenType::NUMBER, num});
            continue;
        }

        // f. If we get here, we don't recognize the character
        tokens.push_back({TokenType::UNKNOWN, std::string(1, c)});
        i++;
    }

    tokens.push_back({TokenType::END_OF_FILE, ""});
    return tokens;
}

// --- Main function to run the lexer ---
int main() {
    std::string line = "if (age >= 18)";

    std::cout << "Lexing input: \"" << line << "\"" << std::endl;
    std::cout << "--------------------------" << std::endl;

    std::vector<Token> tokens = lex(line);

    // Print all the tokens we found
    for (const auto& token : tokens) {
        std::cout << "Type: " << tokenTypeToString(token.type)
                  << ",\tValue: '" << token.value << "'" << std::endl;
    }

    return 0;
}