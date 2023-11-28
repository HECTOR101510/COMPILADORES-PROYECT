import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AST implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    public AST(List<Token> tokens){
        this.tokens=tokens;
        preanalisis=this.tokens.get(i);
    }
    private void DECLARATION(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo){
            case FUN:
                System.out.println("Entro al case FUN");
                FUN_DECL();
                DECLARATION();
            break;
            case VAR:
                System.out.println("Entro al case VAR");
                VAR_DECL();
                DECLARATION();
            break;
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_BRACE:
                System.out.println("Entro al case 3");
                STATEMENT();
                DECLARATION();
            break;
            default:
                //caso de DECLARATION ->Ɛ 
            break;
        }
    }

    private void FUN_DECL(){
        if(preanalisis.tipo==TipoToken.FUN){
            System.out.println("Sigo en fun");
            match(TipoToken.FUN);
            FUNCTION();
        }
        else{
            System.out.println("Se esperaba un fun");
            hayErrores=true;
        }
    }

    private void VAR_DECL(){
        System.out.println("Entramos a VAR_INIT PRENASLISIS: "+preanalisis.tipo);
        if(preanalisis.tipo==TipoToken.VAR){
            match(TipoToken.VAR);
            System.out.println("Prenalisis"+preanalisis.tipo);
            match(TipoToken.IDENTIFIER);
            System.out.println("Prenalisis"+preanalisis.tipo);
            if(preanalisis.tipo==TipoToken.EQUAL){
                VAR_INIT();
                System.out.println("Entramos a VAR_INIT con =");
            }
            match(TipoToken.SEMICOLON);
        }else{
            hayErrores=true;
            System.out.println("Error: Se esperaba un VAR");
        }
    }

    private void STATEMENT(){
        switch (preanalisis.tipo) {
            case IF:
                return IF_STMT();
                break;
            case FOR:
                return FOR_STMT();
                break;
            case PRINT:
                return PRINT_STMT();
                break;
            case RETURN:
                return RETURN_STMT();
                break;
            case WHILE:
                return WHILE_STMT();
                break;
            case LEFT_BRACE:
                return BLOCK();
                break;
            default:
                break;
        }


        EXPR_STMT();
        FOR_STMT();
        IF_STMT();
        PRINT_STMT();
        RETURN_STMT();
        WHILE_STMT();
        BLOCK();
    }

    private void EXPR_STMT(){
        EXPRESSION();
        match(TipoToken.SEMICOLON);
    }

    private void FOR_STMT(){
        if(preanalisis.tipo==TipoToken.FOR){
            System.out.println("Entramos a FOR_STMT");
            match(TipoToken.FOR);
            match(TipoToken.LEFT_PAREN);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            match(TipoToken.RIGHT_PAREN);
            STATEMENT();    
        }else{
            hayErrores=true;
            System.out.println("Error: Se esperaba un for");
        }
    }

    private void FOR_STMT_1(){
        VAR_DECL();
        EXPR_STMT();
        match(TipoToken.SEMICOLON);
    }

    private void FOR_STMT_2(){
        if(preanalisis.tipo==TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else{
            EXPRESSION();
            match(TipoToken.SEMICOLON);
        }
    }

    private void FOR_STMT_3(){
        EXPRESSION();
    }

    private void IF_STMT(){
        if(preanalisis.tipo==TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            STATEMENT();
            ELSE_STATEMENT();
        }
    }

    private void ELSE_STATEMENT(){
        if(preanalisis.tipo==TipoToken.ELSE){
            match(TipoToken.ELSE);
            STATEMENT();
        }
    }

    private void PRINT_STMT(){
        if(preanalisis.tipo==TipoToken.PRINT){
            match(TipoToken.PRINT);
            EXPRESSION();
            match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores=true;
            System.out.println("Error: Se esperaba un print");
        }
    }

    private void RETURN_STMT(){
        if(preanalisis.tipo==TipoToken.RETURN){
            match(TipoToken.RETURN);
            RETURN_EXP_OPC();
            match(TipoToken.SEMICOLON);
        }else{
            hayErrores=true;
            System.out.println("Error: Se esperaba un return");
        }
    }

    private void RETURN_EXP_OPC(){
        EXPRESSION();
    }

    private void WHILE_STMT(){
        if(preanalisis.tipo==TipoToken.WHILE){
           match(TipoToken.WHILE); 
           match(TipoToken.LEFT_PAREN); 
           EXPRESSION();
           match(TipoToken.RIGHT_PAREN); 
           STATEMENT();
        }else{
            hayErrores=true;
            System.out.println("Error: Se esperaba un while");
        }
    }

    private void FUNCTION(){
        if(preanalisis.tipo==TipoToken.IDENTIFIER){
            System.out.println("Estoy en funtion: id y pre:"+preanalisis.tipo+" name: "+preanalisis.lexema);
            match(TipoToken.IDENTIFIER);
            Token t=previous();
            match(TipoToken.LEFT_PAREN);
            PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            BLOCK();
        }
         else{
            System.out.println("Se esperaba un IDENTIFICADOR");
            hayErrores=true;
        }
    }

    private void PARAMETERS_OPC(){
        if(hayErrores)
        return;
        System.out.println("Estoy en PARAMETERS_OPC");
        PARAMETERS();
    }

    private void PARAMETERS(){
        if(hayErrores)
        return;
        if(preanalisis.tipo==TipoToken.IDENTIFIER){
            System.out.println("Estoy en PARAMETERS: id");
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        }
         else{
            System.out.println("Se esperaba un IDENTIFICADOR");
            hayErrores=true;
        }
    }

    private void PARAMETERS_2(){
        if(preanalisis.tipo==TipoToken.COMMA){
            System.out.println("Estoy en PARAMETERS_2: coma");
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        }
    }

    private void BLOCK(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION();
            match(TipoToken.RIGHT_BRACE);
        }else{
            System.out.println("Error: Se esperaba un { } o la declaracion");
        }
    }

    private void VAR_INIT(){
        System.out.println("dentro VAR_INIT Prenalisis: "+preanalisis.tipo);
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.EQUAL){
              match(TipoToken.EQUAL);
              System.out.println("Dentro de VAR_INIT");
              EXPRESSION();
        }
    }

    private Expression EXPRESSION(){
        System.out.println("Dentro de EXPRESSION");
        Expression expr= ASSIGNMENT();
        return expr;
    }

    private Expression ASSIGNMENT(){
                System.out.println("Dentro de ASSIGNMENT");
                Expression expr=LOGIC_OR();
                expr=ASSIGNMENT_OPC(expr);
                return expr;
    }

    private Expression LOGIC_OR(){
                System.out.println("Dentro de LOGIC_OR");
                Expression expr= LOGIC_AND();
                expr= LOGIC_OR_2(expr);
                return expr;
    }

    private Expression LOGIC_OR_2(Expression expr){
        if(preanalisis.tipo==TipoToken.OR){
            match(TipoToken.OR);
            Expression right =LOGIC_AND();
            Token exprr=previous();
            expr=new ExprBinary(expr, exprr, right);
            expr=LOGIC_OR_2(expr);
            return expr;
        }
        return expr;
    }

    private Expression LOGIC_AND(){
                System.out.println("Dentro de LOGIC_AND");
                Expression expr= EQUALITY();
                expr =LOGIC_AND_2(expr);
                return expr;
    }

    private Expression LOGIC_AND_2(Expression expr){
        if(preanalisis.tipo==TipoToken.AND){
            match(TipoToken.AND);
            Expression right= EQUALITY();
            expr=new ExprBinary(expr, new Token(TipoToken.AND, "and"), right);
            expr=LOGIC_AND_2(expr);
            return expr;
        }
        return expr;
    }

    private Expression EQUALITY(){
                System.out.println("Dentro de EQUALITY");
                Expression expr= COMPARISON();
                expr =EQUALITY_2(expr);
                return expr;
    }
    private Expression EQUALITY_2(Expression expr){
        while(preanalisis.tipo==TipoToken.BANG_EQUAL||preanalisis.tipo==TipoToken.EQUAL_EQUAL){
            Token operador=preanalisis;
            match(preanalisis.tipo);
            Expression right = COMPARISON();
            expr= new ExprBinary(expr, operador, right);
        }
        return expr;
    }
    private Expression COMPARISON(){
                System.out.println("Dentro de COMPARISON");
                Expression expr=TERM();
                expr= COMPARISON_2(expr);
                return expr;
    }
        private Expression COMPARISON_2(Expression expr){
            while(preanalisis.tipo==TipoToken.GREATER||
            preanalisis.tipo==TipoToken.GREATER_EQUAL||
            preanalisis.tipo==TipoToken.LESS||
            preanalisis.tipo==TipoToken.LESS_EQUAL){
                Token operador= preanalisis;
                match(preanalisis.tipo);
                Expression right=TERM();
                expr= new ExprBinary(expr, operador, right);
            }
            return expr;
        }
        private Expression TERM(){
                Expression expr=FACTOR();
                expr=TERM_2(expr);
                return expr;
    }

    private Expression TERM_2(Expression expr){
        while(preanalisis.tipo==TipoToken.MINUS||preanalisis.tipo==TipoToken.PLUS){
            Token operador=preanalisis;
            match(preanalisis.tipo);
            Expression right=FACTOR();
            expr=new ExprBinary(expr, operador, right);
        }
        return expr;
    }

    private Expression FACTOR(){
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }

    private Expression FACTOR_2(Expression expr){
        switch (preanalisis.tipo){
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = UNARY();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expr2);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = UNARY();
                expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expr2);
        }
        return expr;
    }

    private Expression UNARY(){
        switch (preanalisis.tipo){
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = UNARY();
                return new ExprUnary(operador, expr);
            case MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr =UNARY();
                return new ExprUnary(operador, expr);
            default:
                return CALL();
        }
    }

    private Expression CALL(){
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }
//revisa esta parte
    private Expression CALL_2(Expression expr){
        switch (preanalisis.tipo){
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = ARGUMENTS_OPC();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return CALL_2(ecf);
        }
        return expr;
    }

    private List<Expression> ARGUMENTS_OPC(){
        if(preanalisis.tipo!=TipoToken.RIGHT_PAREN){
            return ARGUMENTS();
        }
        return Collections.emptyList();//caso-> E
    }

    private List <Expression> ARGUMENTS(){
        List<Expression> argumento=new ArrayList<>();
        if(preanalisis.tipo!=TipoToken.RIGHT_PAREN){
            do{
            argumento.add(EXPRESSION());
                if(preanalisis.tipo==TipoToken.COMMA){
                    match(TipoToken.COMMA);
                }
            }while(preanalisis.tipo!=TipoToken.RIGHT_PAREN);
        }
        return argumento;
    }
    private void FUNCTIONS(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            FUNCTIONS();
        }
    }
    private Expression PRIMARY(){
        switch (preanalisis.tipo){
            case TRUE:
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.tipo);
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.tipo);
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                Expression expr = EXPRESSION();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
        }
        return null;
    }

    private Expression ASSIGNMENT_OPC(Expression exp){
        if(preanalisis.tipo==TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Token t =previous();
            Expression tt=EXPRESSION();
            ExprUnary expr=new ExprUnary(t,tt);
            return expr;
        }
        return exp;
    }

    private void match(TipoToken tt) {
        if (i < tokens.size()) {
            if (preanalisis.tipo == tt) {
                i++;
                if (i < tokens.size()) {
                    preanalisis = tokens.get(i);
                }
            } else {
                hayErrores = true;
            }
        }
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }

    @Override
    public boolean parse(){
        DECLARATION();
        return !hayErrores;
    }
}