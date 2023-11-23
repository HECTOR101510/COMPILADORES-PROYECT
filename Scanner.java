import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens= new ArrayList<>();
    private static final Map<String, TipoToken> palabrasReservadas;
    
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
        palabrasReservadas.put("(", TipoToken.LEFT_PAREN);
        palabrasReservadas.put(")", TipoToken.RIGHT_PAREN);
        palabrasReservadas.put("{", TipoToken.RIGHT_BRACE);
        palabrasReservadas.put("}", TipoToken.LEFT_BRACE);
        palabrasReservadas.put(",", TipoToken.COMMA);
        palabrasReservadas.put(".", TipoToken.DOT);
        palabrasReservadas.put("-", TipoToken.MINUS);
        palabrasReservadas.put("+", TipoToken.PLUS);
        palabrasReservadas.put(";", TipoToken.SEMICOLON);
        palabrasReservadas.put("/", TipoToken.SLASH);
        palabrasReservadas.put("*", TipoToken.STAR);
        palabrasReservadas.put("=", TipoToken.EQUAL);
        palabrasReservadas.put("==", TipoToken.BANG_EQUAL);
        palabrasReservadas.put("!", TipoToken.BANG);
        palabrasReservadas.put("!=", TipoToken.BANG_EQUAL);
        palabrasReservadas.put(">", TipoToken.GREATER);
        palabrasReservadas.put(">=", TipoToken.GREATER_EQUAL);
        palabrasReservadas.put("<", TipoToken.LESS);
        palabrasReservadas.put("<=", TipoToken.LESS_EQUAL);
        palabrasReservadas.put("$", TipoToken.EOF);
    }
    Scanner(String source){
        this.source=source+" ";
    }
    public String aCadena(String lexema, char c){//Sirve para quitar las comillas a una cadena 
        String res = "";
        for(int i = 0; i < lexema.length(); i++){
            if(lexema.charAt(i) != c)
                res += lexema.charAt(i);
        }
        return res;
    }
    /**
     * @return
     */
    List<Token> scanTokens(){
        String lexema="";
        int estado=0,lines=1,bandera=0;
        char c;
        Token t;
        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            switch (estado) {
                case 0:
                    if (c == '>') {
                        estado = 1;
                        lexema += c;
                    } else if (c == '<') {
                        estado = 4;
                        lexema += c;
                    } else if (c == '=') {
                        estado = 7;
                        lexema += c;
                    } else if (c == '!') {
                        estado = 10;
                        lexema += c;
                    } else if (Character.isLetter(c)) {
                        estado = 13;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        estado = 15;
                        lexema += c;
                    } else if (c == '"') {
                        estado = 24;
                        lexema += c;
                    } else if (c == '/') {
                        estado = 26;
                        lexema += c;
                    } else if(c == ' ' || c == '\n'){
                        estado = 33;
                        lexema += c;
                    } else {
                        estado = 34;
                        lexema += c;
                    }
                    break;
                case 1:
                    if (c == '=') {
                        lexema += c;
                        t = new Token(TipoToken.GREATER_EQUAL, lexema);

                    } else {
                        t = new Token(TipoToken.GREATER, lexema);
                        i--;
                    }
                    tokens.add(t);
                    lexema = "";
                    estado = 0;
                    break;
                /* case 2:
                    // Vamos a crear el Token de mayor o igual -----------------
                    Token t = new Token(TipoToken.GREATER_EQUAL, lexema, null);
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                    //i--;
                    break;
                case 3:
                    // Vamos a crear el Token de mayor -------------------------
                    Token t1 = new Token(TipoToken.GREATER, lexema, null);
                    tokens.add(t1);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                 */

                case 4:
                    if (c == '=') {
                        lexema += c;
                        t = new Token(TipoToken.LESS_EQUAL, lexema);
                    } else {
                        t = new Token(TipoToken.LESS, lexema);
                        i--;
                    }
                    tokens.add(t);
                    lexema = "";
                    estado = 0;
                    break;
                case 7:
                    if (c == '=') {
                        lexema += c;
                        t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                    } else {
                        t = new Token(TipoToken.EQUAL, lexema);
                        i--;
                    }

                    tokens.add(t);
                    lexema = "";
                    estado = 0;
                    break;
                /*case 8:
                    // Vamos a crear el Token de igual igual -------------------
                    Token t4 = new Token(TipoToken.EQUAL_EQUAL, lexema, null);
                    tokens.add(t4);
                    estado = 0;
                    lexema = "";
                    //i--;
                    break;
                case 9:
                    // Vamos a crear el Token de igual -------------------------
                    Token t5 = new Token(TipoToken.EQUAL, lexema, null);
                    tokens.add(t5);
                    estado = 0;
                    lexema = "";
                    i--;
                    break; */
                case 10:
                    if (c == '=') {
                        lexema += c;
                        t = new Token(TipoToken.BANG_EQUAL, lexema);
                    } else {
                        t = new Token(TipoToken.BANG, lexema);
                        i--;
                    }

                    tokens.add(t);
                    lexema = "";
                    estado = 0;
                    break;
                case 13:
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        lexema += c;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if (tt == null) {
                            t = new Token(TipoToken.IDENTIFIER, lexema);
                        } else {
                            t = new Token(tt, lexema);
                        }

                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 15:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else if (c == '.') {
                        estado = 16;
                        lexema += c;

                    } else if (c == 'E' || c == 'e') {
                        estado = 18;
                        lexema += c;
                    } else {
                        t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 16:
                    if (Character.isDigit(c)) {
                        estado = 17;
                        lexema += c;
                    } else {
                        Principal.error(lines, "Se esperaba: [0-9] -> " + lexema);
                    }
                    break;
                case 17:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 18;
                        lexema += c;
                    } else {
                        // Token numero flotante - Equivalente estado 23
                        t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 18:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else if (c == '+' || c == '-') {
                        estado = 19;
                        lexema += c;
                    } else {
                        Principal.error(lines, "Se  esperaba: [0-9, +, -] -> " + lexema);
                    }
                    break;
                case 19:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema += c;
                    } else {
                        Principal.error(lines, "Se esperaba: [0-9, +, -] -> " + lexema);
                    }
                    break;
                case 20:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    } else {
                        // Token notación cientifica ---------------------------
                        t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 24:              
                    if (c == '\n') {
                        Principal.error(lines, "No se esperaba un salto de línea -> " + lexema);
                        estado = 0;
                        lexema = "";
                        lines++;
                    }
                    else if(c != '"'){
                        estado = 24;
                        lexema += c;
                        bandera = 1;
                    }
                    else if(c == '"'){
                        estado = 25;
                        lexema += c;
                    }
                    break;
                case 25:
                    Token t13 = new Token(TipoToken.STRING, lexema, aCadena(lexema, '"'));
                    tokens.add(t13);
                    estado = 0;
                    lexema = "";
                    i--;
                    bandera = 0;
                    break;
                case 26:
                    if (c == '*') {
                        estado = 27;
                        lexema += c;
                    } else if (c == '/') {
                        estado = 30;
                        lexema += c;
                    } else {
                        t = new Token(TipoToken.SLASH, lexema);
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    break;
                case 27:
                    // En este estado estamos dentro de un comentario multilinea, no
                    // es necesario guardar el lexema porque no se generará un token.
                    if (c == '*') {
                        estado = 28;
                    }
                    break;
                case 28:
                    if (c == '/') {
                        estado = 0;
                        lexema = "";
                        System.out.println("Comentario multilinea");
                    } else if (c != '*') {
                        estado = 27;
                    }
                    break;
                case 30:
                    if (c == '\n') {
                        estado = 0;
                        lexema = "";
                        System.out.println("Comentario en linea");
                        lines++;
                    }
                    break;
                case 33:
                    //System.out.println("Se detecto un espacio");
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 34:
                    // Tokens de un caracter
                    TipoToken tt1 = palabrasReservadas.get(lexema);
                    if(tt1 != null){
                        Token t21 = new Token(tt1, lexema);
                        tokens.add(t21);
                    }                        
                    else if(c == ' ') ;
                    else if(c == '\n') lines++;
                    else Principal.error(lines, "No se reconoce el caracter: " + lexema);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
            }
        }
       // tokens.add(new Token(TipoToken.EOF, "EOF"));
        return tokens;
    }
}
