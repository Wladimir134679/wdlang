def testImport() {
    import examples.program1 as pg
    import examples.classes

    println(pg)
    println(classes)
}

testImport()