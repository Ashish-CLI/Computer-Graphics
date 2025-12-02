#include <iostream>
#include <string>
#include <vector>
#include <cctype>
#include <map>

using namespace std;

// --- Lexer (Tokenization) ---

enum class TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    LPAREN,
    RPAREN,
    END_OF_FILE,
    UNKNOWN
};

struct Token {
    TokenType type;
    string lexeme;
    double value; // For numbers
};

string tokenTypeToString(TokenType type) {
    static map<TokenType, string> typeMap = {
        {TokenType::NUMBER,     "NUMBER"},
        {TokenType::PLUS,       "PLUS"},
        {TokenType::MINUS,      "MINUS"},
        {TokenType::MULTIPLY,   "MULTIPLY"},
        {TokenType::DIVIDE,     "DIVIDE"},
        {TokenType::LPAREN,     "LPAREN"},
        {TokenType::RPAREN,     "RPAREN"},
        {TokenType::END_OF_FILE,"END_OF_FILE"},
        {TokenType::UNKNOWN,    "UNKNOWN"}
    };
    return typeMap[type];
}

class Lexer {
public:
    Lexer(const string& source) : m_source(source), m_position(0) {}

    Token getNextToken() {
        skipWhitespace();

        if (isAtEnd()) {
            return {TokenType::END_OF_FILE, ""};
        }

        char c = peek();

        if (isdigit(c)) {
            return readNumber();
        }

        switch (c) {
            case '+': advance(); return {TokenType::PLUS, "+"};
            case '-': advance(); return {TokenType::MINUS, "-"};
            case '*': advance(); return {TokenType::MULTIPLY, "*"};
            case '/': advance(); return {TokenType::DIVIDE, "/"};
            case '(': advance(); return {TokenType::LPAREN, "("};
            case ')': advance(); return {TokenType::RPAREN, ")"};
            default: advance(); return {TokenType::UNKNOWN, string(1, c)};
        }
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

    char advance() {
        if (!isAtEnd()) m_position++;
        return m_source[m_position - 1];
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
        return {TokenType::NUMBER, lexeme, stod(lexeme)};
    }
};

// --- Parser (Syntax Analysis) ---

class Parser {
public:
    Parser(Lexer& lexer) : m_lexer(lexer) {
        m_currentToken = m_lexer.getNextToken();
    }

    double parse() {
        return expression();
    }

private:
    Lexer& m_lexer;
    Token m_currentToken;

    void consume(TokenType expectedType) {
        if (m_currentToken.type == expectedType) {
            m_currentToken = m_lexer.getNextToken();
        } else {
            cerr << "Error: Expected " << tokenTypeToString(expectedType)
                 << " but got " << tokenTypeToString(m_currentToken.type)
                 << " with lexeme '" << m_currentToken.lexeme << "'" << endl;
            exit(1);
        }
    }

    // expression -> term ( ( '+' | '-' ) term )*
    double expression() {
        double result = term();

        while (m_currentToken.type == TokenType::PLUS ||
               m_currentToken.type == TokenType::MINUS) {
            TokenType opType = m_currentToken.type;
            consume(opType);
            double right = term();
            if (opType == TokenType::PLUS) {
                result += right;
            } else {
                result -= right;
            }
        }
        return result;
    }

    // term -> factor ( ( '*' | '/' ) factor )*
    double term() {
        double result = factor();

        while (m_currentToken.type == TokenType::MULTIPLY ||
               m_currentToken.type == TokenType::DIVIDE) {
            TokenType opType = m_currentToken.type;
            consume(opType);
            double right = factor();
            if (opType == TokenType::MULTIPLY) {
                result *= right;
            } else {
                if (right == 0) {
                    cerr << "Error: Division by zero" << endl;
                    exit(1);
                }
                result /= right;
            }
        }
        return result;
    }

    // factor -> NUMBER | '(' expression ')'
    double factor() {
        if (m_currentToken.type == TokenType::NUMBER) {
            double value = m_currentToken.value;
            consume(TokenType::NUMBER);
            return value;
        } else if (m_currentToken.type == TokenType::LPAREN) {
            consume(TokenType::LPAREN);
            double result = expression();
            consume(TokenType::RPAREN);
            return result;
        } else {
            cerr << "Error: Expected NUMBER or '(' but got "
                 << tokenTypeToString(m_currentToken.type)
                 << " with lexeme '" << m_currentToken.lexeme << "'" << endl;
            exit(1);
        }
    }
};

int main() {
    string expression_str = "10 + 5 * (2 - 1) / 5";
    cout << "Parsing expression: " << expression_str << endl;

    Lexer lexer(expression_str);
    Parser parser(lexer);

    try {
        double result = parser.parse();
        cout << "Result: " << result << endl;
    } catch (const exception& e) {
        cerr << "Parsing error: " << e.what() << endl;
    }

    return 0;
}