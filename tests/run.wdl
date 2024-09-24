def testImport() {
    println("==== Import other")
    import tests.other_program as pg
    println("==== Import classes")
    import tests.classes

    println("PG = ")
    println(pg)
    println("CLASSES = ")
    println(classes)
}

testImport()