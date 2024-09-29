println("==== Import classes")
import tests.classes

def testImport() {
    println("==== Import other")
    import tests.other_program as pg

    println("PG = ")
    println(pg)
    println("CLASSES = ")
    println(classes)
    println(classes.p2)
    println(classes.p2.toString())

    println()
    println("Проверка создания класса")
    p3 = new classes.Point(-5, -5)
    println("p3 = " + p3)

    println()
    println("pg func = " + pg.testFunc("ARGVal", 2))
}

expansion std, math
STR = "123456789"
AAA = PI
println("Len STR = " + len(STR))
println("AAA = " + AAA)
println("Sin AAA = " + sin(AAA))
println("Cos AAA = " + cos(AAA))

testImport()