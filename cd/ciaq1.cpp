#include <iostream>
using namespace std;
#include <string>
#include <vector>
#include <cctype>   // For isalpha, isdigit, isspace
#include <map>

enum class TokenType {
    KEYWORD,
    IDENTIFIER,
    NUMBER,
    OPERATOR,
    SYMBOL,
    END_OF_FILE,
    UNKNOWN
};

string tokenTypeToString(TokenType type) {
    static map<TokenType, string> typeMap = {
        {TokenType::KEYWORD,    "KEYWORD"},
        {TokenType::IDENTIFIER, "IDENTIFIER"},
        {TokenType::NUMBER,     "NUMBER"},
        {TokenType::OPERATOR,   "OPERATOR"},
        {TokenType::SYMBOL,     "SYMBOL"},
        {TokenType::END_OF_FILE,"END_OF_FILE"},
        {TokenType::UNKNOWN,    "UNKNOWN"}
    };
    return typeMap[type];
}

struct Token {
    TokenType type;
    string lexeme;
};

class Lexer {
public:
    Lexer(const string& source)
        : m_source(source), m_position(0) {}

    Token getNextToken() {
        skipWhitespace();

        if (isAtEnd()) {
            return {TokenType::END_OF_FILE, ""};
        }

        char c = peek();
        
        if (isalpha(c) || c == '_') {
            return readIdentifierOrKeyword();
        }

        if (isdigit(c)) {
            return readNumber();
        }

        switch (c) {
            case '(': advance(); return {TokenType::SYMBOL, "("};
            case ')': advance(); return {TokenType::SYMBOL, ")"};
            case '{': advance(); return {TokenType::SYMBOL, "{"};
            case '}': advance(); return {TokenType::SYMBOL, "}"};
            case ';': advance(); return {TokenType::SYMBOL, ";"};
            default: break; // Not a symbol, continue to next checks
        }

        switch (c) {
            case '=':
                return createToken(TokenType::OPERATOR, match('=') ? "==" : "=");
            case '!':
                return createToken(TokenType::OPERATOR, match('=') ? "!=" : "!");
            case '<':
                return createToken(TokenType::OPERATOR, match('=') ? "<=" : "<");
            case '>':
                return createToken(TokenType::OPERATOR, match('=') ? ">=" : ">");
            case '+': advance(); return {TokenType::OPERATOR, "+"};
            case '-': advance(); return {TokenType::OPERATOR, "-"};
            case '*': advance(); return {TokenType::OPERATOR, "*"};
            case '/': advance(); return {TokenType::OPERATOR, "/"};
            default: break; // Not an operator, continue to unknown
        }

        advance();
        return {TokenType::UNKNOWN, string(1, c)};
    }

private:
    string m_source;
    size_t m_position;

    bool isAtEnd() {
        return m_position >= m_source.length();
    }

    char peek() {
        if (isAtEnd()) return '\0';
        return m_source[m_position];
    }

    char peekNext() {
        if (m_position + 1 >= m_source.length()) return '\0';
        return m_source[m_position + 1];
    }

    char advance() {
        if (!isAtEnd()) m_position++;
        return m_source[m_position - 1];
    }

    bool match(char expected) {
        if (isAtEnd() || m_source[m_position] != expected) {
            return false;
        }
        m_position++;
        return true;
    }

    Token createToken(TokenType type, const string& lexeme) {
        return {type, lexeme};
    }

    void skipWhitespace() {
        while (!isAtEnd() && isspace(peek())) {
            advance();
        }
    }

    Token readNumber() {
        size_t start = m_position;
        while (!isAtEnd() && isdigit(peek())) {
            advance();
        }
        string lexeme = m_source.substr(start, m_position - start);
        return {TokenType::NUMBER, lexeme};
    }

    Token readIdentifierOrKeyword() {
        size_t start = m_position;
        while (!isAtEnd() && (isalnum(peek()) || peek() == '_')) {
            advance();
        }
        string lexeme = m_source.substr(start, m_position - start);

        static map<string, TokenType> keywords = {
            {"if", TokenType::KEYWORD},
            {"else", TokenType::KEYWORD}
            // Add other keywords here
        };

        auto it = keywords.find(lexeme);
        if (it != keywords.end()) {
            return {it->second, lexeme};
        }

        return {TokenType::IDENTIFIER, lexeme};
    }
};

int main() {
    string code = "if (counter >= 10) { \n"
                       "    x = 5; \n"
                       "} else {\n"
                       "    x = 0;\n"
                       "}";

    cout << "Tokenizing the following code:\n" << code << "\n" << endl;
    cout << "--- Tokens ---" << endl;

    Lexer lexer(code);
    Token token;

    do {
        token = lexer.getNextToken();
        cout << "Type: " << tokenTypeToString(token.type)
                  << ",\tLexeme: '" << token.lexeme << "'" << endl;
    } while (token.type != TokenType::END_OF_FILE);

    return 0;
}