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
        
        switch (preanalisis.tipo) {
            case FUN:
                FUN_DECL();
                DECLARATION();
                break;
            case VAR:
                VAR_DECL();
                DECLARATION();
                break;

            case TRUE,FALSE,NULL,NUMBER,STRING,IDENTIFIER,LEFT_BRACE:
                STATEMENT();
                DECLARATION();
                break;
            default:
                hayErrores=true;
                System.out.println("Se encontro errores");
                break;
        }
    }

    private void FUN_DECL(){
        if(preanalisis.tipo==TipoToken.FUN){
            match(TipoToken.FUN);
            FUNCTION();
        }
        else{
            System.out.println("Se esperaba un fun");
            hayErrores=true;
        }
    }

    private void VAR_DECL(){

    }

    private void STATEMENT(){

    }

    private void FUNCTION(){
        if(preanalisis.tipo==TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            match(TipoToken.LEFT_BRACE);
            PARAMETERS_OPC();
            match(TipoToken.RIGHT_BRACE);
            BLOCK();
        }else{
            hayErrores=true;
            System.out.println("Error localizado");
        }
    }

    private void PARAMETERS_OPC(){
        if(hayErrores)
        return;
        PARAMETERS();
    }

    private void PARAMETERS(){
        if(hayErrores)
        return;
        if(preanalisis.tipo==TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        }else{
            hayErrores=true;
            System.out.println("Se esperaba un ID");
        }
    }

    private void PARAMETERS_2(){
        if(preanalisis.tipo==TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        }else{
            hayErrores=true;
            System.out.println("Error: se esperaba una , o ID");
        }
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo==tt){
            i++;
            preanalisis=tokens.get(i);
        }
        else{
            hayErrores=true;
            System.out.println("Error localizado");
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

    @Override
    public boolean parse(){
        DECLARATION();
        return !hayErrores;
    }
}
