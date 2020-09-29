/*
*
*
*/
grammar MsExpression;

@parser::members { 
	boolean testMode;
	public MsExpressionParser(TokenStream input, boolean testMode) { 
		this(input); this.testMode = testMode;
	} 
}

orExpr: andExpr ('OR' andExpr)* ;

andExpr: notExpr ('AND' notExpr)* ;

notExpr: NEG? (href | '(' orExpr ')' | {testMode}? (ID '.')? ID ) ;

href: '<a' 'href' '=' '"' ID '"' '/>';



NEG : 'NOT';
ID : ('0'..'9' | 'a'..'z' | 'A'..'Z' | '_' | '-' | '#')+; 

WS : [ \t\r\n]+ -> channel(HIDDEN);
