import java.util.List;

public class ExprCallFunction extends Expression{
    final Expression callee;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, List<Expression> arguments){
        this.callee=callee;
        this.arguments=arguments;
    }
}
