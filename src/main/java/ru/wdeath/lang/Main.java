package ru.wdeath.lang;

import ru.wdeath.lang.ast.Expression;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.parser.Lexer;
import ru.wdeath.lang.parser.Parser;
import ru.wdeath.lang.parser.Token;

import java.util.List;

public class Main {

    public static void main(String[] args) {
//        final var input = "(2 + 2) * #ff + 0.5";
//        final var input = "(PI + 2) * #ff + 0.5";
        final var input = """
                word = 2 + 2
                word2 = PI + word
                print word
                print "\\n"
                /*
                dwad
                dawda
                wdawd
                */
                // print "Tut text Типо вцфвфцв\\n"
                print "ToT" * 5 + "\\n"
                print "Hello" + "World\\n"
                print 5 == 5
                print "\\n"
                
                // Проверка условий
                if (1 <= 2) print "1 = 1"
                else print "1 != 1"
                print "\\n"
                print sin(PI/2)

                if(40 < 50 && 50 <= 60) {
                    print "true \\n"
                    print "block\\n"
                    i = 0
                    while (i < 10) {
                        println("i = " + i)
                        i = i + 1
                    }
                    for i = 0, i < 10, i = i + 1 {
                        if i == 4 continue
                        println("i = " + i)
                    }
                }
                i = 0
                do {
                    println("do i = " + i + " sin=" + sin(i))
                    i = i + 1
                } while(i < 10)
                
                a = "PRINT"
                println("a=" + a)
                
                def test(a, b){
                    println("Def test, a=" + a + ", b=" + b)
                }
                def sum(a, b){
                    return a + b
                }
               
                println("a=" + a)
                test("A", 125432)
                println("a=" + a)
                println(sum(10, 5))
                """;
        Lexer lexer = new Lexer(input);
        List<Token> tokenize = lexer.tokenize();
        tokenize.forEach(System.out::println);
        System.out.println("======");
        final var parser = new Parser(tokenize);
        final var blockProgram = parser.parse();
        System.out.println(blockProgram);
        System.out.println("==Run==");
        blockProgram.execute();
        System.out.println("==End==");
    }
}
