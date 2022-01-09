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
Program : Statement SEMICOLON Program | Statement SEMICOLON ;
Statement : VarStatement | ArrStatement | AssignStatement | IfStatement | WhileStatement | ReturnStatement | FunctionCallStatement ;
VarStatement : VAR IdentifierList ;
IdentifierList : MaybeEqualExpression | MaybeEqualExpression COMMA IdentifierList ;
MaybeEqualExpression : IDENTIFIER | IDENTIFIER EQ Expression ;
Expression : IntExpression | StringExpression ;
MathematicalOperator : PLUS | MINUS | TIMES | DIV | MOD ;
IntExpression : INTCONSTANT | IDENTIFIER | FunctionCallStatement | IntExpression MathematicalOperator IntExpression | OPEN IntExpression MathematicalOperator IntExpression CLOSE ;
StringExpression : STRINGCONSTANT ;
ExpressionList : Expression | Expression COMMA ExpressionList ;
ArrStatement : ARR LESS INTCONSTANT BIGGER PureIdentifierList ;
PureIdentifierList : IDENTIFIER | IDENTIFIER COMMA PureIdentifierList ;
AssignStatement : IDENTIFIER EQ Expression ;
IfStatement : IF OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE | IF OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE ELSE BRACKETOPEN Program BRACKETCLOSE ;
RelationalOperator : EQQ | LESS | LESSEQ | BIGGER | BIGGEREQ ;
Condition : Expression RelationalOperator Expression 
			| Expression RelationalOperator Expression AND Expression RelationalOperator Expression 
			| Expression RelationalOperator Expression OR Expression RelationalOperator Expression ;
WhileStatement : WHILE OPEN Condition CLOSE BRACKETOPEN Program BRACKETCLOSE ;
ReturnStatement : RETURN Expression ;
FunctionCallStatement : FunctionName OPEN ExpressionList CLOSE | FunctionName OPEN CLOSE ;
FunctionName : PRINT | SETNTH | GETNTH | READINT | READSTRING ;
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
