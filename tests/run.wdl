println("==== Import classes")
import tests.classes as *, math as *, std
import logger as log

log.init("run.wdl")

STR = "123456789"
ARR = [1, 2, 10231, 42]
AAA = PI
log.warn("Len STR = " + std.len(STR))
log.warn("Len ARR = " + std.len(ARR))
log.info("AAA = " + AAA)
log.info("Sin AAA = " + sin(AAA))
log.info("Cos AAA = " + cos(AAA))

println()
log.info("Проверка создания класса")
p3 = new Point(-5, -5)
log.info("p3 = " + p3)

def testImport() {
    log.info("==== Import other")
    import tests.other_program as pg

    log.info("PG = ")
    log.info(pg)
    log.info("CLASSES = ")
    log.info(p2)
    log.info(p2.toString())

    log.info()
    log.info("pg func = " + pg.testFunc("ARGVal", 2))
}

testImport()