%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token ARR;
%token VAR;
%token IF;
%token PRINT;
%token READINT;
%token READSTRING;
%token ELSE;
%token WHILE;
%token SETNTH;
%token GETNTH;
%token RETURN;

%token IDENTIFIER;
%token INTCONSTANT;
%token STRINGCONSTANT;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token MOD;
%token EQ;
%token BIGGER;
%token BIGGEREQ;
%token LESS;
%token LESSEQ;
%token EQQ;
%token NEQ;
%token AND;
%token OR;

%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token BRACKETOPEN;
%token BRACKETCLOSE;
%token COMMA;

%start Program 

%%
Program : Statement SEMICOLON Program {printf("Program -> Statement ; Program\n");} | Statement SEMICOLON {printf("Program -> Statement ;\n");} ;
Statement : VarStatement {printf("Statement -> VarStatement\n");} | ArrStatement {printf("Statement -> ArrStatement\n");} | AssignStatement {printf("Statement -> AssignStatement\n");} | IfStatement {printf("Statement -> IfStatement\n");} | WhileStatement {printf("Statement -> WhileStatement\n");} | ReturnStatement {printf("Statement -> ReturnStatement\n");} | FunctionCallStatement {printf("Statement -> FunctionCallStatement\n");} ;
VarStatement : VAR IdentifierList {printf("VarStatement -> var IdentifierList\n");} ;
IdentifierList : MaybeEqualExpression {printf("IdentifierList -> MaybeEqualExpression \n");} | MaybeEqualExpression COMMA IdentifierList {printf("IdentifierList -> MaybeEqualExpression , IdentifierList\n");} ;
MaybeEqualExpression : IDENTIFIER {printf("MaybeEqualExpression -> IDENTIFIER \n");} | IDENTIFIER EQ Expression {printf("MaybeEqualExpression -> IDENTIFIER = Expression \n");} ;
Expression : IntExpression {printf("Expression -> IntExpression \n");} | StringExpression {printf("Expression -> StringExpression \n");} ;
MathematicalOperator : PLUS {printf("MathematicalOperator -> + \n");} | MINUS {printf("MathematicalOperator -> - \n");} | TIMES {printf("MathematicalOperator -> * \n");} | DIV {printf("MathematicalOperator -> / \n");} | MOD {printf("MathematicalOperator -> % \n");} ;
IntExpression : INTCONSTANT {printf("IntExpression -> INTCONSTANT \n");} | IDENTIFIER {printf("IntExpression -> IDENTIFIER \n");} | FunctionCallStatement {printf("IntExpression -> FunctionCallStatement \n");} | IntExpression MathematicalOperator IntExpression {printf("IntExpression -> IntExpression MathematicalOperator IntExpression \n");} | OPEN IntExpression MathematicalOperator IntExpression CLOSE {printf("IntExpression -> ( IntExpression MathematicalOperator IntExpression ) \n");} ;
StringExpression : STRINGCONSTANT {printf("StringExpression -> STRINGCONSTANT \n");} ;
ExpressionList : Expression {printf("ExpressionList -> Expression \n");} | Expression COMMA ExpressionList {printf("ExpressionList -> Expression , ExpressionList \n");} ;
ArrStatement : ARR LESS INTCONSTANT BIGGER PureIdentifierList {printf("ArrStatement -> arr < INTCONSTANT > PureIdentifierList \n");} ;
PureIdentifierList : IDENTIFIER {printf("PureIdentifierList -> IDENTIFIER \n");} | IDENTIFIER COMMA PureIdentifierList {printf("PureIdentifierList -> IDENTIFIER , PureIdentifierList \n");} ;
AssignStatement : IDENTIFIER EQ Expression {printf("AssignStatement -> IDENTIFIER = Expression \n");} ;
IfStatement : IF OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE {printf("IfStatement -> if ( Condition ) { Program } \n");} | IF OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE ELSE BRACKETOPEN Program BRACKETCLOSE {printf("IfStatement -> if ( Condition ) { Program } else { Program } \n");} ;
RelationalOperator : EQQ {printf("RelationalOperator -> ==\n");} | LESS {printf("RelationalOperator -> <\n");} | LESSEQ {printf("RelationalOperator -> <=\n");} | BIGGER {printf("RelationalOperator -> >\n");} | BIGGEREQ {printf("RelationalOperator -> >=\n");} ;
Condition : Expression RelationalOperator Expression {printf("Condition -> Expression RelationalOperator Expression\n");}
			| Expression RelationalOperator Expression AND Expression RelationalOperator Expression {printf("Condition -> Expression RelationalOperator Expression && Expression RelationalOperator Expression\n");}
			| Expression RelationalOperator Expression OR Expression RelationalOperator Expression {printf("Condition -> Expression RelationalOperator Expression || Expression RelationalOperator Expression\n");} ;
WhileStatement : WHILE OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE {printf("WhileStatement -> while ( Condition ) { Program }\n");} ;
ReturnStatement : RETURN Expression {printf("ReturnStatement -> return Expression\n");} ;
FunctionCallStatement : FunctionName OPEN ExpressionList CLOSE {printf("FunctionCallStatement -> FunctionName ( ExpressionList )\n");} | FunctionName OPEN CLOSE {printf("FunctionCallStatement -> FunctionName ( )\n");} ;
FunctionName : PRINT {printf("FunctionName -> print\n");} | SETNTH {printf("FunctionName -> setnth\n");} | GETNTH {printf("FunctionName -> getnth\n");} | READINT {printf("FunctionName -> readint\n");} | READSTRING {printf("FunctionName -> readstring\n");} ;
%%
yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin =  fopen(argv[1],"r");
	if(!yyparse()) fprintf(stderr, "\tOK\n");
} 
