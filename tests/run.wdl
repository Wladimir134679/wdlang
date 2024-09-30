println("==== Import classes")
import tests.classes as *, math as *, std

STR = "123456789"
ARR = [1, 2, 10231, 42]
AAA = PI
println("Len STR = " + std.len(STR))
println("Len ARR = " + std.len(ARR))
println("AAA = " + AAA)
println("Sin AAA = " + sin(AAA))
println("Cos AAA = " + cos(AAA))

println()
println("Проверка создания класса")
p3 = new Point(-5, -5)
println("p3 = " + p3)

def testImport() {
    println("==== Import other")
    import tests.other_program as pg

    println("PG = ")
    println(pg)
    println("CLASSES = ")
    println(p2)
    println(p2.toString())

    println()
    println("pg func = " + pg.testFunc("ARGVal", 2))
}

testImport()