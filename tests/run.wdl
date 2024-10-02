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

log.info("Проверка работы files и try")
import files
pathToFile = "./testFile.txt"

def handlerError(a, b){
    log.error("ERROR " + a + " / " + b)
    return "ERROR READ STRING"
}

def readFile(path){
    readStringFile = std.try(def() = files.readString(pathToFile), ::handlerError)
    return readStringFile
}

log.info("read string : " + readFile(pathToFile))
log.info("write string...")
files.writeString(pathToFile, "Тут записали текст в файл")
log.info("read string : " + readFile(pathToFile))
files.deleteFile(pathToFile)

def testFunc(func){
    func("Вызов двойной функции")
}
inf = log.info
testFunc(inf)

def customFunc(ope){
    return ope(1)
}
log.info("Test customSin: " + customFunc(::sin))

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