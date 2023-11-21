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
            match(TipoToken.LEFT_PAREN);
            PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            BLOCK();
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

    private void EXPRESSION(){
        if(hayErrores)
            return;
        System.out.println("Dentro de EXPRESSION");
        ASSIGNMENT();
        System.out.println("Fuera de EXPRESSION");
    }

    private void ASSIGNMENT(){
        System.out.println("Dentro de ASSIGNMENT PRENALISIS: "+preanalisis.tipo);
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN ->{
                System.out.println("Dentro de ASSIGNMENT");
                LOGIC_OR();
                ASSIGNMENT_OPC();
               }
        
        }
    }

    private void LOGIC_OR(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de LOGIC_OR");
                LOGIC_AND();
                LOGIC_OR_2();
                break;
        
            default:
                break;
        }
    }

    private void LOGIC_OR_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.OR){
            match(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    private void LOGIC_AND(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de LOGIC_AND");
                EQUALITY();
                LOGIC_AND_2();
                break;
        
            default:
                break;
        }
    }

    private void LOGIC_AND_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.AND){
            match(TipoToken.AND);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    private void EQUALITY(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de EQUALITY");
                COMPARISON();
                EQUALITY_2();
                break;
        
            default:
                break;
        }
    }
    private void EQUALITY_2(){
        if(preanalisis.tipo==TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.tipo==TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }
    private void COMPARISON(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de COMPARISON");
                TERM();
                COMPARISON_2();
                break;
        }
    }
        private void COMPARISON_2(){
            if(preanalisis.tipo==TipoToken.GREATER){
                match(TipoToken.GREATER);
                TERM();
                COMPARISON_2();
            }
            else if(preanalisis.tipo==TipoToken.GREATER_EQUAL){
                match(TipoToken.GREATER_EQUAL);
                TERM();
                COMPARISON_2();
            }
            else if(preanalisis.tipo==TipoToken.LESS){
                match(TipoToken.LESS);
                TERM();
                COMPARISON_2();
            }
            else if(preanalisis.tipo==TipoToken.LESS_EQUAL){
                match(TipoToken.LESS_EQUAL);
                TERM();
                COMPARISON_2();
            }
        }
        private void TERM(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de TERM");
                FACTOR();
                TERM_2();
                break;
        
            default:
                break;
        }
    }

    private void TERM_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.MINUS){
            match(TipoToken.MINUS);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.tipo==TipoToken.PLUS){
            match(TipoToken.PLUS);
            FACTOR();
            TERM_2();
        }
    }

    private void FACTOR(){
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG,MINUS,TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                System.out.println("Dentro de FACTOR");
                UNARY();
                FACTOR_2();
                break;
        }
    }

    private void FACTOR_2(){
        if(preanalisis.tipo==TipoToken.SLASH){
            match(TipoToken.SLASH);
            UNARY();
            FACTOR_2();
        }
        else if(preanalisis.tipo==TipoToken.STAR){
            match(TipoToken.STAR);
            UNARY();
            FACTOR_2();
        }
    }

    private void UNARY(){
        System.out.println("Dentro de UNARY");
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case BANG:
            System.out.println("Dentro de UNARY CASE: BANG");
                match(TipoToken.BANG);
                UNARY();
                break;

            case MINUS:
                match(TipoToken.MINUS);
                System.out.println("Dentro de UNARY CASE: MINUS");
                UNARY();
                break;
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
            System.out.println("Dentro de UNARY CASE: TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN");
                CALL();
                break;
        
            default:
                break;
        }
    }

    private void CALL(){
        System.out.println("Dentro de  CALL");
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_PAREN:
                PRIMARY();
                CALL_2();
                break;
            default:
                break;
        }
    }

    private void CALL_2(){
        if(hayErrores)
            return;
            if(preanalisis.tipo==TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                ARGUMENTS_OPC();
                match(TipoToken.RIGHT_PAREN);
                CALL_2();
            }
    }

    private void ARGUMENTS_OPC(){
        EXPRESSION();
        ARGUMENTS();
    }

    private void ARGUMENTS(){
        if(preanalisis.tipo==TipoToken.COMMA){
            match(TipoToken.COMMA);
            EXPRESSION();
            ARGUMENTS();
        }
    }
    private void FUNCTIONS(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            FUNCTIONS();
        }
    }
    private void PRIMARY(){
        System.out.println("Dentro de PRIMARY");
        if(hayErrores)
            return;
        switch (preanalisis.tipo) {
            case TRUE:
                match(TipoToken.TRUE);
                System.out.println("Dentro de PRIMARY CASE:PRIMARY");
                break;

            case FALSE:
                match(TipoToken.FALSE);
                System.out.println("Dentro de PRIMARY CASE:FALSE");
                break;

            case NULL:
                match(TipoToken.NULL);
                System.out.println("Dentro de PRIMARY CASE:NULL");
                break;

            case NUMBER:
                match(TipoToken.NUMBER);
                System.out.println("Dentro de PRIMARY CASE:NUMBER");
                break;

            case STRING:
                match(TipoToken.STRING);
                System.out.println("Dentro de PRIMARY CASE:STRING");
                break;

            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                System.out.println("Dentro de PRIMARY CASE:IDENTIFIER");
                break;
            
            case LEFT_PAREN:
                System.out.println("Dentro de PRIMARY CASE:LEFT_PAREN");
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                match(TipoToken.RIGHT_PAREN);
                break;
            default:
                break;
        }
    }

    private void ASSIGNMENT_OPC(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
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

    @Override
    public boolean parse(){
        DECLARATION();
        return !hayErrores;
    }
}